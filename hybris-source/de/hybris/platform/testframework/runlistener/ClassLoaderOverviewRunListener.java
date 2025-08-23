package de.hybris.platform.testframework.runlistener;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class ClassLoaderOverviewRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(ClassLoaderOverviewRunListener.class.getName());
    private final ClassLoadingMXBean clMXbean = ManagementFactory.getClassLoadingMXBean();
    private int loadedClassCount = 0;
    private long totalLoadedClassCount = 0L;
    private long unloadedClassCount = 0L;


    public void testRunStarted(Description description) throws Exception
    {
        this.loadedClassCount = this.clMXbean.getLoadedClassCount();
        this.totalLoadedClassCount = this.clMXbean.getTotalLoadedClassCount();
        this.unloadedClassCount = this.clMXbean.getUnloadedClassCount();
    }


    public void testRunFinished(Result result) throws Exception
    {
        LOG.info("Loaded class count: " + this.clMXbean.getLoadedClassCount() + " diff: " + this.clMXbean
                        .getLoadedClassCount() - this.loadedClassCount);
        LOG.info("Total loaded class count: " + this.clMXbean.getTotalLoadedClassCount() + " diff: " + this.clMXbean
                        .getTotalLoadedClassCount() - this.totalLoadedClassCount);
        LOG.info("Unloaded class count: " + this.clMXbean.getUnloadedClassCount() + " diff: " + this.clMXbean
                        .getUnloadedClassCount() - this.unloadedClassCount);
    }
}
