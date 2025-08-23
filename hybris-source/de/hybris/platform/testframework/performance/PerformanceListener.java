package de.hybris.platform.testframework.performance;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import de.hybris.platform.util.Config;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import org.junit.runner.Describable;
import org.junit.runner.Description;

public class PerformanceListener implements TestListener
{
    private static final String DEFAULT_PERFORMANCE_MEASUREMENT = "de.hybris.platform.testframework.performance.impl.SimpleLog4jPerformanceMeasurement";
    private final TestMethodPerformanceMeasurement performanceMeasurement;
    private TestMethodPerformanceContext currentContext;
    private final Function<TestMethodPerformanceContext, Void> withFailure = (Function<TestMethodPerformanceContext, Void>)new Object(this);
    private final Function<TestMethodPerformanceContext, Void> withError = (Function<TestMethodPerformanceContext, Void>)new Object(this);
    private final Function<TestMethodPerformanceContext, Void> successfully = (Function<TestMethodPerformanceContext, Void>)new Object(this);


    public PerformanceListener()
    {
        this(Config.getString("tests.performance.measurement.class", "de.hybris.platform.testframework.performance.impl.SimpleLog4jPerformanceMeasurement"));
    }


    public PerformanceListener(String performanceMeasurementClassName)
    {
        Preconditions.checkNotNull(performanceMeasurementClassName);
        this.performanceMeasurement = instantiatePerformanceMeasurement(performanceMeasurementClassName);
    }


    public void startTest(Test test)
    {
        Description testDescription = getDescriptionFrom(test);
        TestMethod testMethod = new TestMethod(testDescription.getClassName(), testDescription.getMethodName());
        openContextFor(testMethod);
    }


    public void addFailure(Test test, AssertionFailedError t)
    {
        closeContext(this.withFailure);
    }


    public void addError(Test test, Throwable t)
    {
        closeContext(this.withError);
    }


    public void endTest(Test test)
    {
        closeContext(this.successfully);
    }


    private void openContextFor(TestMethod testMethod)
    {
        Preconditions.checkState((this.currentContext == null), "Can't measure performance of more than one method at the same time");
        this.currentContext = this.performanceMeasurement.startMeasurementOf(testMethod);
    }


    private void closeContext(Function<TestMethodPerformanceContext, Void> function)
    {
        if(this.currentContext == null)
        {
            return;
        }
        try
        {
            function.apply(this.currentContext);
        }
        finally
        {
            this.currentContext = null;
        }
    }


    private Description getDescriptionFrom(Test test)
    {
        return ((Describable)test).getDescription();
    }


    private TestMethodPerformanceMeasurement instantiatePerformanceMeasurement(String className)
    {
        try
        {
            return (TestMethodPerformanceMeasurement)Class.forName(className).newInstance();
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Can't instantiate " + className, ex);
        }
    }
}
