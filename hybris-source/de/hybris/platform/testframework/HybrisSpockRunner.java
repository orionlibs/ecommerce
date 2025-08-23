package de.hybris.platform.testframework;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.transform.Generated;
import groovy.transform.Internal;
import java.beans.Transient;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class HybrisSpockRunner extends JUnitPlatform implements UnifiedHybrisTestRunner, GroovyObject
{
    private final HybrisTestClassRunnerLogic testClassRunnerLogic;
    private final Class classUnderTest;


    public HybrisSpockRunner(Class clazz) throws InitializationError
    {
        super(clazz);
        MetaClass metaClass = $getStaticMetaClass();
        Class clazz1 = clazz;
        Object object = arrayOfCallSite[0].callConstructor(HybrisTestClassRunnerLogic.class, this, this, clazz);
        this.testClassRunnerLogic = (HybrisTestClassRunnerLogic)ScriptBytecodeAdapter.castToType(object, HybrisTestClassRunnerLogic.class);
    }


    public void filter(Filter filter) throws NoTestsRemainException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[1].call(this.testClassRunnerLogic, filter);
    }


    public void run(RunNotifier notifier)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[2].call(this.testClassRunnerLogic, notifier);
    }


    public Class getCurrentTestClass()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return this.classUnderTest;
    }


    public void superFilter(Filter filter) throws NoTestsRemainException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ScriptBytecodeAdapter.invokeMethodOnSuperN(JUnitPlatform.class, this, "filter", new Object[] {filter});
    }


    public void superRun(RunNotifier notifier)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ScriptBytecodeAdapter.invokeMethodOnSuperN(JUnitPlatform.class, this, "run", new Object[] {notifier});
    }


    @Generated
    @Internal
    @Transient
    public MetaClass getMetaClass()
    {
        if(this.metaClass != null)
        {
            return this.metaClass;
        }
        this.metaClass = $getStaticMetaClass();
        return this.metaClass;
    }


    @Generated
    @Internal
    public void setMetaClass(MetaClass paramMetaClass)
    {
        this.metaClass = paramMetaClass;
    }
}
