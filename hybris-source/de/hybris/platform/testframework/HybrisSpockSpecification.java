package de.hybris.platform.testframework;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.testframework.runlistener.ApplicationContextCheckRunListener;
import de.hybris.platform.testframework.runlistener.C2LSetupRunListener;
import de.hybris.platform.testframework.runlistener.ClassLoaderOverviewRunListener;
import de.hybris.platform.testframework.runlistener.CustomActionsRunListener;
import de.hybris.platform.testframework.runlistener.HsqldbCheckpointRunListener;
import de.hybris.platform.testframework.runlistener.ItemCreationListener;
import de.hybris.platform.testframework.runlistener.LangReferenceRemoverRunListener;
import de.hybris.platform.testframework.runlistener.LogRunListener;
import de.hybris.platform.testframework.runlistener.MemoryOverviewRunListener;
import de.hybris.platform.testframework.runlistener.OpenDBConnectionRunListener;
import de.hybris.platform.testframework.runlistener.PlatformConfigurationCheckRunListener;
import de.hybris.platform.testframework.runlistener.PlatformRunListener;
import de.hybris.platform.testframework.runlistener.ResetMockitoRunListener;
import de.hybris.platform.testframework.runlistener.TableSizeCheckRunListener;
import de.hybris.platform.testframework.runlistener.TransactionRunListener;
import de.hybris.platform.testframework.runlistener.VMBlockTimeRunListener;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.junit.Ignore;
import org.spockframework.runtime.model.FieldMetadata;
import org.spockframework.runtime.model.SpecMetadata;

@Ignore
@RunListeners({CustomActionsRunListener.class, LangReferenceRemoverRunListener.class, ApplicationContextCheckRunListener.class, TransactionRunListener.class, ItemCreationListener.class, C2LSetupRunListener.class, TableSizeCheckRunListener.class, LogRunListener.class,
                ClassLoaderOverviewRunListener.class, PlatformRunListener.class, ResetMockitoRunListener.class, PlatformConfigurationCheckRunListener.class, HsqldbCheckpointRunListener.class, VMBlockTimeRunListener.class, OpenDBConnectionRunListener.class, MemoryOverviewRunListener.class})
@SpecMetadata(filename = "HybrisSpockSpecification.groovy", line = 29)
public abstract class HybrisSpockSpecification extends JUnitPlatformSpecification implements JaloSessionHolder
{
    @FieldMetadata(line = 38, name = "jaloSession", ordinal = 0, initializer = false)
    protected JaloSession jaloSession;
    private static HybrisTestLogic hybrisTestLogic;


    private Object setupSpec()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Object object = arrayOfCallSite[0].callConstructor(HybrisTestLogic.class, this);
        hybrisTestLogic = (HybrisTestLogic)ScriptBytecodeAdapter.castToType(object, HybrisTestLogic.class);
        return object;
    }


    public static boolean intenseChecksActivated()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[1].call(HybrisTestLogic.class));
    }


    private Object setup() throws JaloSystemException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return arrayOfCallSite[2].call(hybrisTestLogic);
    }


    private Object cleanup() throws JaloSecurityException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return arrayOfCallSite[3].call(hybrisTestLogic);
    }


    public static Language getOrCreateLanguage(String isoCode) throws JaloSystemException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return (Language)ScriptBytecodeAdapter.castToType(arrayOfCallSite[4].call(HybrisTestLogic.class, isoCode), Language.class);
    }


    public static Currency getOrCreateCurrency(String isoCode) throws JaloSystemException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return (Currency)ScriptBytecodeAdapter.castToType(arrayOfCallSite[5].call(HybrisTestLogic.class, isoCode), Currency.class);
    }


    public void establishJaloSession(JaloSession jaloSession)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        JaloSession jaloSession1 = jaloSession;
    }


    public JaloSession takeJaloSession()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return this.jaloSession;
    }
}
