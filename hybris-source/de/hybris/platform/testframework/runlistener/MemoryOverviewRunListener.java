package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.testframework.HybrisJUnit4Test;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class MemoryOverviewRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(MemoryOverviewRunListener.class.getName());
    private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private MemoryUsage heapAtTestStart = null;
    private MemoryUsage nonHeapAtTestStart = null;
    private String currentTestClass;


    public void testRunStarted(Description description) throws Exception
    {
        if(!HybrisJUnit4Test.intenseChecksActivated())
        {
            return;
        }
        this.currentTestClass = description.getDisplayName();
        this.heapAtTestStart = this.memoryBean.getHeapMemoryUsage();
        this.nonHeapAtTestStart = this.memoryBean.getNonHeapMemoryUsage();
    }


    public void testRunFinished(Result result) throws Exception
    {
        if(!HybrisJUnit4Test.intenseChecksActivated())
        {
            return;
        }
        MemoryUsage heapAtTestEnd = this.memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapAtTestEnd = this.memoryBean.getNonHeapMemoryUsage();
        long initHeapDiffInK = (heapAtTestEnd.getInit() - this.heapAtTestStart.getInit()) / 1024L;
        long usedHeapDiffInK = (heapAtTestEnd.getUsed() - this.heapAtTestStart.getUsed()) / 1024L;
        long comittedHeapDiffInK = (heapAtTestEnd.getCommitted() - this.heapAtTestStart.getCommitted()) / 1024L;
        long initNonHeapDiffInK = (nonHeapAtTestEnd.getInit() - this.nonHeapAtTestStart.getInit()) / 1024L;
        long usedNonHeapDiffInK = (nonHeapAtTestEnd.getUsed() - this.nonHeapAtTestStart.getUsed()) / 1024L;
        long comittedNonHeapDiffInK = (nonHeapAtTestEnd.getCommitted() - this.nonHeapAtTestStart.getCommitted()) / 1024L;
        LOG.info("+---------------------------- Memory usage after test " + this.currentTestClass);
        LOG.info("+     heap: init=" + heapAtTestEnd.getInit() / 1024L + "K (" + initHeapDiffInK + "K)\t  used=" + heapAtTestEnd
                        .getUsed() / 1024L + "K (" + usedHeapDiffInK + "K)\t  comitted=" + heapAtTestEnd
                        .getCommitted() / 1024L + "K (" + comittedHeapDiffInK + "K)");
        LOG.info("+ non-heap: init=" + nonHeapAtTestEnd.getInit() / 1024L + "K (" + initNonHeapDiffInK + "K)\t  used=" + nonHeapAtTestEnd
                        .getUsed() / 1024L + "K (" + usedNonHeapDiffInK + "K)\t  comitted=" + nonHeapAtTestEnd
                        .getCommitted() / 1024L + "K (" + comittedNonHeapDiffInK + "K)");
        LOG.info("+ ObjectPendingFinalizationCount: " + this.memoryBean.getObjectPendingFinalizationCount());
        for(MemoryPoolMXBean mpbean : ManagementFactory.getMemoryPoolMXBeans())
        {
            LOG.info("+       " + mpbean.getName() + "(" + mpbean.getType() + "): " + mpbean.getCollectionUsage());
        }
        LOG.info("+--------------------------------------------------------------------------------------------------");
    }
}
