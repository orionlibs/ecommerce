package de.hybris.platform.testframework.performance.impl;

import de.hybris.platform.testframework.performance.TestMethod;
import de.hybris.platform.testframework.performance.TestMethodPerformanceContext;
import de.hybris.platform.testframework.performance.TestMethodPerformanceMeasurement;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.logging.context.LoggingContextFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleLog4jPerformanceMeasurement implements TestMethodPerformanceMeasurement
{
    private static final Logger LOG = LogManager.getLogger(SimpleLog4jPerformanceMeasurement.class);


    public SimpleLog4jPerformanceMeasurement()
    {
        putCommonPropertiesToMDC();
    }


    public TestMethodPerformanceContext startMeasurementOf(TestMethod testMethod)
    {
        return (TestMethodPerformanceContext)new SimpleTestMethodPerformanceContext(testMethod);
    }


    private void putCommonPropertiesToMDC()
    {
        LoggingContextFactory.getLoggingContextHandler().put("operatingSystem", Utilities.getOS().toString());
        LoggingContextFactory.getLoggingContextHandler().put("database", Config.getDatabase());
    }
}
