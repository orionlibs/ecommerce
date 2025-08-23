package de.hybris.platform.servicelayer;

import de.hybris.platform.testframework.JUnitPlatformSpecification;
import de.hybris.platform.testframework.RunListeners;
import de.hybris.platform.testframework.runlistener.LightPlatformRunListener;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.junit.Ignore;
import org.spockframework.runtime.model.SpecMetadata;
import org.springframework.context.ApplicationContext;

@Ignore
@RunListeners({LightPlatformRunListener.class})
@SpecMetadata(filename = "HybrisSpockLightSpecification.groovy", line = 12)
public abstract class HybrisSpockLightSpecification extends JUnitPlatformSpecification
{
    protected static final ServicelayerBaseTestLogic servicelayerBaseTestLogic;

    static
    {
        Object object = $getCallSiteArray()[2].callConstructor(ServicelayerBaseTestLogic.class);
        servicelayerBaseTestLogic = (ServicelayerBaseTestLogic)ScriptBytecodeAdapter.castToType(object, ServicelayerBaseTestLogic.class);
    }

    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return arrayOfCallSite[0].call(servicelayerBaseTestLogic, this);
    }


    protected ApplicationContext getApplicationContext()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return (ApplicationContext)ScriptBytecodeAdapter.castToType(arrayOfCallSite[1].call(servicelayerBaseTestLogic), ApplicationContext.class);
    }
}
