package de.hybris.platform.jalo;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.BridgeAbstraction;
import de.hybris.platform.util.BridgeInterface;
import de.hybris.platform.util.JaloObjectCreator;
import de.hybris.platform.util.SingletonCreator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;

abstract class PreviousJaloImplementationManager
{
    private static final Map mappings = new HashMap<>();
    private static final SingletonCreator.Creator<Map<PK, String>> classCacheCreator = (SingletonCreator.Creator<Map<PK, String>>)new Object();


    private static final Map<PK, String> getTypeClassCache()
    {
        return (Map<PK, String>)Registry.getSingleton(classCacheCreator);
    }


    private static String getCachedClassName(Item.ItemImpl impl)
    {
        PK typeKey = impl.getTypeKey();
        Map<PK, String> cache = getTypeClassCache();
        String ret = cache.get(typeKey);
        if(ret == null)
        {
            ret = impl.getJaloObjectClass().getName();
            cache.put(typeKey, ret);
        }
        return ret;
    }


    static final BridgeAbstraction createJaloObject(Tenant tenant, Item.ItemImpl impl)
    {
        BridgeAbstraction ret;
        ApplicationContext actx = Registry.getGlobalApplicationContext();
        String clname = getCachedClassName(impl);
        if(actx.containsBean(clname))
        {
            ret = (BridgeAbstraction)actx.getBean(clname);
        }
        else
        {
            Class<?> cl = impl.getJaloObjectClass();
            Preconditions.checkArgument(BridgeAbstraction.class.isAssignableFrom(cl));
            JaloObjectCreator creator = null;
            Object mapped = mappings.get(cl.getName());
            if(mapped != null)
            {
                if(mapped instanceof Class)
                {
                    Preconditions.checkArgument(BridgeAbstraction.class.isAssignableFrom((Class)mapped));
                    cl = (Class)mapped;
                }
                else
                {
                    creator = (JaloObjectCreator)mapped;
                }
            }
            try
            {
                ret = (creator != null) ? creator.createInstance(tenant, (BridgeInterface)impl) : (BridgeAbstraction)cl.newInstance();
            }
            catch(Exception e)
            {
                e.printStackTrace(System.err);
                String pk = (impl instanceof Item.ItemImpl) ? impl.getPK().toString() : "n/a";
                throw new JaloSystemException(e, "could not create jalo object instance of " + cl.getName() + " for item " + pk + " impl " + impl, 0);
            }
        }
        ret.setImplementation((BridgeInterface)impl);
        ret.setTenant(tenant);
        return ret;
    }
}
