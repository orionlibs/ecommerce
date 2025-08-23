package de.hybris.platform.persistence.framework;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.Utilities;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HomeInvocationHandler implements InvocationHandler
{
    private static final String FINDBYPRIMARYKEY = "findByPrimaryKey".intern();
    private static final String CREATE = "create".intern();
    private static final String EJBCREATE = "ejbCreate".intern();
    private static final String EJBPOSTCREATE = "ejbPostCreate".intern();
    private static final String FIND = "find".intern();
    private final String jndiName;
    private final PersistencePool persistencePool;
    private boolean invokeAsSystemOperation = false;


    public HomeInvocationHandler(PersistencePool pool, String jndiName)
    {
        this.jndiName = jndiName;
        this.persistencePool = pool;
    }


    public void setInvokeAsSystemOperation(boolean invokeAsSystemOperation)
    {
        this.invokeAsSystemOperation = invokeAsSystemOperation;
    }


    public Object invoke(Object orig_proxy, Method method, Object[] args) throws Throwable
    {
        if(!this.invokeAsSystemOperation)
        {
            return invokeMethod(orig_proxy, method, args);
        }
        RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
        try
        {
            Object object = invokeMethod(orig_proxy, method, args);
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


    private Object invokeMethod(Object orig_proxy, Method method, Object[] args) throws Throwable
    {
        String methodName = method.getName();
        if(methodName.equals(FINDBYPRIMARYKEY))
        {
            throw new RuntimeException("findByPrimaryKey called on HomeInvocationHandler!!");
        }
        if(methodName.equals(CREATE))
        {
            Transaction curTx = Transaction.current();
            Class<?>[] arrayOfClass = method.getParameterTypes();
            if(curTx.isRunning())
            {
                EntityInstance entityInstance1 = this.persistencePool.createEntityInstance(this.jndiName);
                Utilities.callMethod(entityInstance1, EJBCREATE, arrayOfClass, args);
                curTx.registerEntityInstance(entityInstance1);
                PK pK = entityInstance1.getEntityContext().getPK();
                Utilities.callMethod(entityInstance1, EJBPOSTCREATE, arrayOfClass, args);
                curTx.executeOrDelayStore(entityInstance1);
                return this.persistencePool.getOrCreateUninitializedEntityProxy(this.jndiName, pK);
            }
            EntityInstance entityInstance = this.persistencePool.createEntityInstance(this.jndiName);
            Utilities.callMethod(entityInstance, EJBCREATE, arrayOfClass, args);
            PK pk = entityInstance.getEntityContext().getPK();
            Preconditions.checkArgument((pk != null));
            Utilities.callMethod(entityInstance, EJBPOSTCREATE, arrayOfClass, args);
            entityInstance.ejbStore();
            return this.persistencePool.getOrCreateUninitializedEntityProxy(this.jndiName, pk);
        }
        if(methodName.startsWith(FIND))
        {
            Class<?>[] arrayOfClass = method.getParameterTypes();
            String str = "ejb" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
            EntityInstance entityInstance = this.persistencePool.getEntitySingleton(this.jndiName);
            Object origRes = Utilities.callMethod(entityInstance, str, arrayOfClass, args);
            return EJBTools.convertEntityFinderResult(origRes, this.persistencePool.getTenant().getSystemEJB());
        }
        Class<?>[] argClasses = method.getParameterTypes();
        String methodNameToCall = "ejbHome" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        EntityInstance entity = this.persistencePool.getEntitySingleton(this.jndiName);
        return Utilities.callMethod(entity, methodNameToCall, argClasses, args);
    }
}
