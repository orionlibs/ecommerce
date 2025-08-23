package de.hybris.platform.jalo;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.BridgeAbstraction;
import de.hybris.platform.util.BridgeInterface;
import de.hybris.platform.util.JaloObjectCreator;
import de.hybris.platform.util.SingletonCreator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

public abstract class JaloImplementationManager
{
    private static final Logger log = Logger.getLogger(JaloImplementationManager.class.getName());
    private static final Map mappings = new ConcurrentHashMap<>();
    private static final Object CLASSCACHE_ID = "TypeClassCacheMapCreator";
    private static final SingletonCreator.Creator<JaloClassMappingCache> CLASSCACHECREATOR = (SingletonCreator.Creator<JaloClassMappingCache>)new Object();


    private static final JaloClassMappingCache getTypeClassCache(Tenant tenant)
    {
        return (JaloClassMappingCache)tenant.getSingletonCreator().getSingleton(CLASSCACHECREATOR);
    }


    public static final void registerJaloObject(Class jaloClass, JaloObjectCreator creator)
    {
        if(creator == null)
        {
            throw new JaloInvalidParameterException("jalo object creator for " + jaloClass + " cannot be null", 0);
        }
        mappings.put(jaloClass.getName(), creator);
    }


    public static final void replaceCoreJaloClass(Class jaloClass, Class<?> ownClass)
    {
        Preconditions.checkArgument(BridgeAbstraction.class.isAssignableFrom(ownClass));
        mappings.put(jaloClass.getName(), ownClass);
    }


    public static final void replaceCoreJaloClass(Class jaloClass, JaloObjectCreator creator)
    {
        mappings.put(jaloClass.getName(), creator);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static final JaloObjectCreator getJaloObjectCreator(Class jaloClass)
    {
        Object object = mappings.get(jaloClass.getName());
        return (object instanceof JaloObjectCreator) ? (JaloObjectCreator)object : null;
    }


    public static final void clearJaloObjectMapping(Class jaloClass)
    {
        mappings.remove(jaloClass.getName());
    }


    public static JaloConnection getActiveConnection() throws JaloConnectException
    {
        return JaloConnection.getInstance();
    }


    public static final BridgeAbstraction createJaloObject(Tenant tenant, Item.ItemImpl impl)
    {
        BridgeAbstraction ret = null;
        JaloClassMappingCache typeClassCache = getTypeClassCache(tenant);
        Class clazz = typeClassCache.getClassForEJBImpl(impl);
        ret = typeClassCache.createSpringInstanceIfPossible(clazz);
        if(ret == null)
        {
            ret = createJaloObjectFromClassName(tenant, (BridgeInterface)impl, clazz);
        }
        ret.setImplementation((BridgeInterface)impl);
        ret.setTenant(tenant);
        return ret;
    }


    private static final BridgeAbstraction createJaloObjectFromClassName(Tenant tenant, BridgeInterface impl, Class<BridgeAbstraction> defaultClass)
    {
        Class<BridgeAbstraction> classToUse = defaultClass;
        JaloObjectCreator creator = null;
        Object mapped = mappings.get(classToUse.getName());
        if(mapped != null)
        {
            if(mapped instanceof Class)
            {
                classToUse = (Class)mapped;
            }
            else
            {
                creator = (JaloObjectCreator)mapped;
            }
        }
        try
        {
            if(creator != null)
            {
                return creator.createInstance(tenant, impl);
            }
            return classToUse.newInstance();
        }
        catch(Exception e)
        {
            String pk = (impl instanceof Item.ItemImpl) ? ((Item.ItemImpl)impl).getPK().toString() : "n/a";
            throw new JaloSystemException(e, "Cannot create Jalo instance for item " + pk + " due to " + e.getMessage() + " (defaultClass:" + defaultClass + ",mappedClass:" + classToUse + ",creator:" + creator + ")", 0);
        }
    }


    public static void clearTypeClassCache()
    {
        getTypeClassCache(Registry.getCurrentTenant()).clear();
    }
}
