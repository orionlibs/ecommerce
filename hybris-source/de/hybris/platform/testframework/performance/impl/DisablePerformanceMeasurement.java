package de.hybris.platform.testframework.performance.impl;

import de.hybris.platform.testframework.performance.TestMethod;
import de.hybris.platform.testframework.performance.TestMethodPerformanceContext;
import de.hybris.platform.testframework.performance.TestMethodPerformanceMeasurement;

public class DisablePerformanceMeasurement implements TestMethodPerformanceMeasurement
{
    private static final TestMethodPerformanceContext NOP_CONTEXT = (TestMethodPerformanceContext)new Object();


    public TestMethodPerformanceContext startMeasurementOf(TestMethod testMethod)
    {
        return NOP_CONTEXT;
    }
}
