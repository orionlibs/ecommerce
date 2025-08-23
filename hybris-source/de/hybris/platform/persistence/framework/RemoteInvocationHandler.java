package de.hybris.platform.persistence.framework;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteInvocationHandler implements InvocationHandler
{
    private static final String GETPRIMARYKEY = "getPrimaryKey".intern();
    private static final String GETPK = "getPK".intern();
    private static final String HASHCODE = "hashCode".intern();
    private static final String TOSTRING = "toString".intern();
    private static final String EQUALS = "equals".intern();
    private static final String REMOVE = "remove".intern();
    private final String jndi;
    private final PK pk;
    private final PersistencePool persistencePool;
    private final boolean invokeAsSystemOperation;


    RemoteInvocationHandler(PersistencePool pool, String jndi, PK pk)
    {
        Preconditions.checkArgument((pk != null));
        this.pk = pk;
        this.jndi = jndi;
        this.persistencePool = pool;
        this.invokeAsSystemOperation = pool.isSystemCriticalType(pk.getTypeCode());
    }


    public Object invoke(Object orig_proxy, Method orig_method, Object[] args) throws Throwable
    {
        if(!this.invokeAsSystemOperation)
        {
            return invokeMethod(orig_proxy, orig_method, args);
        }
        RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
        try
        {
            Object object = invokeMethod(orig_proxy, orig_method, args);
            if(theUpdate != null)
            {
                theUpdate.close();
            }
            return object;
        }
        catch(Throwable throwable)
        {
            if(theUpdate != null)
            {
                try
                {
                    theUpdate.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    public Object invokeMethod(Object orig_proxy, Method orig_method, Object[] args) throws Throwable
    {
        String methodName = orig_method.getName();
        PK _pk = this.pk;
        if(methodName.equals(GETPK))
        {
            return _pk;
        }
        if(methodName.equals(HASHCODE))
        {
            return Integer.valueOf(_pk.hashCode());
        }
        if(methodName.equals(GETPRIMARYKEY))
        {
            return _pk.toString();
        }
        if(methodName.equals(TOSTRING))
        {
            return "entity[" + this.pk.toString() + "]";
        }
        if(methodName.equals(EQUALS))
        {
            if(args[0] == null)
            {
                return Boolean.FALSE;
            }
            if(!(args[0] instanceof EntityProxy))
            {
                return Boolean.FALSE;
            }
            EntityProxy proxy = (EntityProxy)args[0];
            if(_pk.equals(proxy.getPK()))
            {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        if(methodName.equals(REMOVE))
        {
            performRemove(_pk);
            return null;
        }
        return performOther(_pk, orig_method, methodName, args);
    }


    private void performRemove(PK pk)
    {
        try
        {
            Transaction curTx = Transaction.current();
            if(curTx.isRunning())
            {
                removeInsideTx(pk, curTx);
            }
            else
            {
                removeOutsideTx(pk);
            }
        }
        catch(YNoSuchEntityException e)
        {
            if(!pk.equals(e.getPk()))
            {
                throw e;
            }
        }
    }


    private void removeInsideTx(PK pk, Transaction curTx)
    {
        EntityInstance entity = curTx.getOrLoadTxBoundEntityInstance(this.persistencePool, this.jndi, pk);
        curTx.addToDelayedRemoval(entity);
    }


    private void removeOutsideTx(PK pk)
    {
        EntityInstance entity = this.persistencePool.createEntityInstance(this.jndi, pk);
        entity.ejbRemove();
    }


    private Object performOther(PK _pk, Method orig_method, String methodName, Object[] args) throws Exception
    {
        Object ret;
        Class<?>[] argClasses = orig_method.getParameterTypes();
        Transaction curTx = Transaction.current();
        if(curTx.isRunning())
        {
            ret = performInsideTx(_pk, methodName, args, argClasses, curTx);
        }
        else
        {
            ret = performOutsideTx(_pk, methodName, args, argClasses);
        }
        return ret;
    }


    private Object performInsideTx(PK pk, String methodName, Object[] args, Class<?>[] argClasses, Transaction curTx) throws Exception
    {
        EntityInstance entity = curTx.getOrLoadTxBoundEntityInstance(this.persistencePool, this.jndi, pk);
        Method method = entity.getClass().getMethod(methodName, argClasses);
        Object ret = Utilities.callMethod(entity, method, args);
        curTx.executeOrDelayStore(entity);
        return ret;
    }


    private Object performOutsideTx(PK pk, String methodName, Object[] args, Class<?>[] argClasses) throws Exception
    {
        EntityInstance entity = this.persistencePool.getEntityInstance(this.jndi, pk);
        Method method = entity.getClass().getMethod(methodName, argClasses);
        entity.ejbLoad();
        Object ret = Utilities.callMethod(entity, method, args);
        if(!Item.isCurrentlyRemoving(pk))
        {
            entity.ejbStore();
        }
        return ret;
    }
}
