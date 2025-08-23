package de.hybris.platform.persistence;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.persistence.property.EJBPropertyContainer;
import de.hybris.platform.util.JaloPropertyContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ExtensibleItemEJBImpl extends ItemEJBImpl implements ExtensibleItem.ExtensibleItemImpl
{
    private static final String CFG_SELF_HEALING_SUPPRESS = "self.healing.suppressForTypeSystemTypes";
    private static final String CFG_SELF_HEALING_SUPPRESS_TYPECODES = "self.healing.suppressForTypeSystemTypes.typecodes";


    protected ExtensibleItemEJBImpl(Tenant tenant, ExtensibleItemRemote remoteObject)
    {
        super(tenant, (ItemRemote)remoteObject);
    }


    public Map getAllProperties(SessionContext ctx)
    {
        Map ejbMap = ((ExtensibleItemRemote)getRemote()).getAllProperties();
        if(ejbMap.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        Cache cache = getCache();
        Map<Object, Object> ret = new HashMap<>();
        for(Iterator<Map.Entry> it = ejbMap.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry mapEntry = it.next();
            String name = (String)mapEntry.getKey();
            Object jaloValue = WrapperFactory.wrap(ctx, cache, mapEntry.getValue(), (WrapperFactory.ItemPropertyWrappingListener)new Object(this, ctx, name));
            if(jaloValue != null)
            {
                ret.put(name, jaloValue);
            }
        }
        return Collections.unmodifiableMap(ret);
    }


    public Set getPropertyNames(SessionContext ctx)
    {
        return Collections.unmodifiableSet(((ExtensibleItemRemote)getRemote()).getPropertyNames());
    }


    public Object setProperty(SessionContext ctx, String name, Object jaloValue)
    {
        Cache cache = getCache();
        return WrapperFactory.wrap(cache, ((ExtensibleItemRemote)
                        getRemote()).setProperty(name, WrapperFactory.unwrap(cache, jaloValue, true)));
    }


    public Object getProperty(SessionContext ctx, String name)
    {
        Cache cache = getCache();
        AtomicBoolean valid = new AtomicBoolean(true);
        Object ret = WrapperFactory.wrap(ctx, cache, ((ExtensibleItemRemote)getRemote()).getPropertyRaw(name), ipv -> {
            boolean suppress = Registry.getCurrentTenant().getConfig().getBoolean("self.healing.suppressForTypeSystemTypes", true);
            String typeCodes = Registry.getCurrentTenant().getConfig().getString("self.healing.suppressForTypeSystemTypes.typecodes", "81,82,83,84,87,91");
            List<String> listOfCodes = new ArrayList<>(Arrays.asList(typeCodes.split(",")));
            if(suppress && getComposedType().hasAttribute(name))
            {
                Type attributeType = getComposedType().getAttributeDescriptorIncludingPrivate(name).getAttributeType();
                if(attributeType instanceof ComposedType)
                {
                    String typeCode = Integer.toString(((ComposedType)attributeType).getItemTypeCode());
                    if(listOfCodes.contains(typeCode))
                    {
                        return;
                    }
                }
            }
            valid.set(false);
        });
        if(!valid.get() && !isSealed(ctx))
        {
            setProperty(ctx, name, ret);
        }
        return ret;
    }


    private boolean isSealed(SessionContext ctx)
    {
        return Boolean.TRUE.equals(getProperty(ctx, "sealed"));
    }


    public Object removeProperty(SessionContext ctx, String name)
    {
        return wrap(((ExtensibleItemRemote)getRemote()).removeProperty(name));
    }


    public void setAllProperties(SessionContext ctx, JaloPropertyContainer jaloContainer) throws ConsistencyCheckException
    {
        ((ExtensibleItemRemote)getRemote()).setAllProperties((EJBPropertyContainer)unwrap(jaloContainer));
    }


    public void commit() throws ConsistencyCheckException
    {
    }


    public void rollback() throws ConsistencyCheckException
    {
    }
}
