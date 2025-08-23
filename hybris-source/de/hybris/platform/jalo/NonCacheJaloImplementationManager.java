package de.hybris.platform.jalo;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.BridgeAbstraction;
import de.hybris.platform.util.BridgeInterface;
import de.hybris.platform.util.JaloObjectCreator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;

abstract class NonCacheJaloImplementationManager
{
    private static final Map mappings = new HashMap<>();


    private static String getClassName(BridgeInterface impl)
    {
        return impl.getJaloObjectClass().getName();
    }


    static final BridgeAbstraction createJaloObject(Tenant tenant, BridgeInterface impl)
    {
        BridgeAbstraction ret;
        ApplicationContext actx = Registry.getGlobalApplicationContext();
        String clname = getClassName(impl);
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
                ret = (creator != null) ? creator.createInstance(tenant, impl) : (BridgeAbstraction)cl.newInstance();
            }
            catch(Exception e)
            {
                e.printStackTrace(System.err);
                String pk = (impl instanceof Item.ItemImpl) ? ((Item.ItemImpl)impl).getPK().toString() : "n/a";
                throw new JaloSystemException(e, "could not create jalo object instance of " + cl.getName() + " for item " + pk + " impl " + impl, 0);
            }
        }
        ret.setImplementation(impl);
        ret.setTenant(tenant);
        return ret;
    }
}
