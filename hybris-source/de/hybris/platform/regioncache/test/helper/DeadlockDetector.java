package de.hybris.platform.regioncache.test.helper;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeadlockDetector
{
    private static final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();


    public static void printDeadlocks(PrintStream writer)
    {
        List<ThreadInfo> deadlocks = findDeadlocks();
        if(deadlocks.isEmpty())
        {
            return;
        }
        print(writer, deadlocks);
    }


    private static void print(PrintStream writer, List<ThreadInfo> deadlocks)
    {
        writer.println("Deadlock detected\n=================\n");
        for(ThreadInfo thread : deadlocks)
        {
            writer.println(String.format("\"%s\":", new Object[] {thread.getThreadName()}));
            writer.println(String.format("  waiting to lock Monitor of %s ", new Object[] {thread.getLockName()}));
            writer.println(String.format("  which is held by \"%s\"", new Object[] {thread.getLockOwnerName()}));
            writer.println();
        }
    }


    private static List<ThreadInfo> findDeadlocks()
    {
        long[] result;
        if(mbean.isSynchronizerUsageSupported())
        {
            result = mbean.findDeadlockedThreads();
        }
        else
        {
            result = mbean.findMonitorDeadlockedThreads();
        }
        long[] monitorDeadlockedThreads = result;
        if(monitorDeadlockedThreads == null)
        {
            return Collections.emptyList();
        }
        return Arrays.asList(mbean.getThreadInfo(monitorDeadlockedThreads));
    }
}
