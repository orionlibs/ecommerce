package de.hybris.platform.servicelayer;

import de.hybris.platform.testframework.HybrisSpockSpecification;
import groovy.lang.GroovyObject;
import java.lang.reflect.Field;
import javax.annotation.Resource;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.junit.Ignore;
import org.spockframework.runtime.model.SpecMetadata;
import org.springframework.context.ApplicationContext;

@Ignore
@SpecMetadata(filename = "ServicelayerBaseSpecification.groovy", line = 13)
public class ServicelayerBaseSpecification extends HybrisSpockSpecification
{
    protected static final ServicelayerBaseTestLogic servicelayerBaseTestLogic;

    static
    {
        Object object = $getCallSiteArray()[5].callConstructor(ServicelayerBaseTestLogic.class);
        servicelayerBaseTestLogic = (ServicelayerBaseTestLogic)ScriptBytecodeAdapter.castToType(object, ServicelayerBaseTestLogic.class);
    }

    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[0].callCurrent((GroovyObject)this, "test setup: prepareApplicationContextAndSession");
        return arrayOfCallSite[1].call(servicelayerBaseTestLogic, this);
    }


    protected void autowireProperties(ApplicationContext applicationContext)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[2].call(servicelayerBaseTestLogic, applicationContext, this);
    }


    protected String getBeanName(Resource resource, Field field)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return ShortTypeHandling.castToString(arrayOfCallSite[3].call(servicelayerBaseTestLogic, resource, field));
    }


    protected ApplicationContext getApplicationContext()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return (ApplicationContext)ScriptBytecodeAdapter.castToType(arrayOfCallSite[4].call(servicelayerBaseTestLogic), ApplicationContext.class);
    }
}
