package de.hybris.platform.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.extension.GenericManager;
import de.hybris.platform.jalo.extension.ItemLifecycleListener;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.ManagerEJB;
import de.hybris.platform.util.ExposesRemote;
import de.hybris.platform.util.SingletonCreator;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

public abstract class Manager implements Serializable, ItemLifecycleListener
{
    private static final Logger LOG = Logger.getLogger(Manager.class.getName());
    private final Map transientObjects;
    private volatile Tenant tenant;


    public Manager()
    {
        this.transientObjects = new ConcurrentHashMap<>();
        setTenant(Registry.getCurrentTenantNoFallback());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void init()
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void destroy()
    {
    }


    public void setTenant(Tenant tenant)
    {
        if(tenant == null)
        {
            throw new IllegalStateException("cannot create manager " + getClass().getName() + " outside tenant scope");
        }
        this.tenant = tenant;
    }


    public Tenant getTenant()
    {
        return this.tenant;
    }


    protected SessionContext getAllValuesSessionContext(SessionContext ctx)
    {
        SessionContext myCtx = (ctx != null) ? new SessionContext(ctx) : getSession().createSessionContext();
        myCtx.setLanguage(null);
        return myCtx;
    }


    public JaloSession getSession()
    {
        return JaloSession.getCurrentSession(getTenant());
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        ItemRemote itemRemote = extractNonRequiredRemoteFromItem(item);
        if(itemRemote == null)
        {
            return;
        }
        getRemote().notifyItemRemove(itemRemote);
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
        ItemRemote itemRemote = extractNonRequiredRemoteFromItem(item);
        if(itemRemote == null)
        {
            return;
        }
        if(!getRemote().canRemoveItem(itemRemote))
        {
            throw new ConsistencyCheckException("cannot remove item " + item + " since manager " +
                            getClass().getName() + " does not allow", 0);
        }
    }


    protected final <T extends ItemRemote> T extractRequiredRemoteFromItem(Item item, Class<T> clazz)
    {
        if(item == null)
        {
            return null;
        }
        T result = extractNonRequiredRemoteFromItem(item);
        if(result == null)
        {
            throw new IllegalStateException(
                            String.format("Couldn't extract required Remote '%s' from '%s'.", new Object[] {clazz.getSimpleName(), item
                                            .getClass().getSimpleName()}));
        }
        return result;
    }


    protected final <T extends ItemRemote> T extractNonRequiredRemoteFromItem(Item item)
    {
        if(item == null)
        {
            return null;
        }
        Item.ItemImpl itemImpl = item.getImplementation();
        if(item.isJaloOnly || !(itemImpl instanceof ExposesRemote))
        {
            return null;
        }
        return (T)((ExposesRemote)itemImpl).getRemote();
    }


    public void setAttribute(String key, Object value)
    {
        if(value == null)
        {
            this.transientObjects.remove(key);
        }
        else
        {
            this.transientObjects.put(key, value);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setTransientObject(String key, Object value)
    {
        setAttribute(key, value);
    }


    public Object getAttribute(String key)
    {
        return this.transientObjects.get(key);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Object getTransientObject(String key)
    {
        return getAttribute(key);
    }


    public Map getAttributeMap()
    {
        return this.transientObjects;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getTransientObjectMap()
    {
        return getAttributeMap();
    }


    public Item getFirstItemByAttribute(Class<?> itemClass, String attr, Object attrVal)
    {
        Item item = null;
        List<Item> result = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(itemClass).getCode() + "} WHERE {" + attr + "} = ?attrVal ORDER BY {" + Item.PK + "} ASC", Collections.singletonMap("attrVal", attrVal), Collections.singletonList(itemClass), true, true, 0,
                                        2).getResult();
        if(!result.isEmpty())
        {
            if(result.size() > 1)
            {
                LOG.warn("ambiguous call to getFirstItemByAttribute(" + itemClass + "," + attr + "," + attrVal + ") - found more than one result " + result + "...");
            }
            item = result.get(0);
        }
        return item;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Item getFirstItemByAttribute(String attr, Object attrVal, Class itemClass)
    {
        return getFirstItemByAttribute(itemClass, attr, attrVal);
    }


    public ManagerEJB getRemote()
    {
        Class<ManagerEJB> remoteManagerClass = getRemoteManagerClass();
        if(remoteManagerClass == null)
        {
            throw new JaloSystemException(null, "this manager has no remote manager.", 0);
        }
        return (ManagerEJB)Registry.getSingleton(remoteManagerClass);
    }


    public Class getRemoteManagerClass()
    {
        return null;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static <T> T getSingletonManagerInstance(Class<T> clazz, String extName)
    {
        if(clazz.equals(GenericManager.class))
        {
            return (T)Registry.getSingleton((SingletonCreator.Creator)new GenericManagerSingletonCreator(extName));
        }
        return (T)Registry.getSingleton((SingletonCreator.Creator)new ManagerSingletonCreator(clazz));
    }


    public Object wrap(Object object)
    {
        return WrapperFactory.wrap(getTenant().getCache(), object);
    }


    public abstract Object writeReplace() throws ObjectStreamException;


    public void beforeItemCreation(SessionContext ctx, ComposedType type, Item.ItemAttributeMap attributes) throws JaloBusinessException
    {
    }


    public void afterItemCreation(SessionContext ctx, ComposedType type, Item createdItem, Item.ItemAttributeMap attributes) throws JaloBusinessException
    {
    }
}
