package de.hybris.platform.testframework.runlistener;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class ThreadOverviewRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(ThreadOverviewRunListener.class.getName());
    private final ThreadMXBean threads = ManagementFactory.getThreadMXBean();
    private long[] threadIDsAtStart;
    private int threadCountStart;


    public void testRunStarted(Description description) throws Exception
    {
        this.threadIDsAtStart = this.threads.getAllThreadIds();
        this.threadCountStart = this.threadIDsAtStart.length;
        LOG.info("### Thread count before start: " + this.threadCountStart);
        this.threads.resetPeakThreadCount();
    }


    public void testRunFinished(Result result) throws Exception
    {
        long[] threadIDsAtEnd = this.threads.getAllThreadIds();
        int threadCountEnd = threadIDsAtEnd.length;
        int peakThreadCount = this.threads.getPeakThreadCount() - this.threadCountStart;
        if(threadCountEnd > this.threadCountStart)
        {
            Set<Long> startTreadIDs = new HashSet(this.threadCountStart);
            for(int i = 0; i < this.threadCountStart; i++)
            {
                startTreadIDs.add(Long.valueOf(this.threadIDsAtStart[i]));
            }
            for(int j = 0; j < threadCountEnd; j++)
            {
                if(!startTreadIDs.contains(Long.valueOf(threadIDsAtEnd[j])))
                {
                    ThreadInfo threadinfo = this.threads.getThreadInfo(threadIDsAtEnd[j]);
                    LOG.warn("Thread remains: " + threadinfo);
                }
            }
        }
        LOG.info("### Current thread count after finish: " + threadCountEnd);
        LOG.info("### Peak thread count is: " + peakThreadCount);
    }
}
