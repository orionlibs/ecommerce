package de.hybris.platform.hac.facade;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ThreadMonitor
{
    private final ThreadMXBean tmbean;
    private final RuntimeMXBean runbean;
    private static String INDENT = "\t";
    private final String findDeadlocksMethodName = "findDeadlockedThreads";
    private final boolean canDumpLocks = true;
    private static final SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public ThreadMonitor()
    {
        this.tmbean = ManagementFactory.getThreadMXBean();
        this.runbean = ManagementFactory.getRuntimeMXBean();
    }


    public String threadDump()
    {
        if(this.tmbean.isObjectMonitorUsageSupported() && this.tmbean.isSynchronizerUsageSupported())
        {
            return dumpThreadInfoWithLocks();
        }
        return dumpThreadInfo();
    }


    private String dumpThreadInfo()
    {
        String date = createDateFormat().format(new Date(System.currentTimeMillis()));
        StringBuilder threadDump = new StringBuilder(date + "\n");
        Properties prop = new Properties();
        prop.putAll(this.runbean.getSystemProperties());
        threadDump.append("Full thread dump " + prop
                        .getProperty("java.vm.name") + " (" + prop.getProperty("java.vm.version") + " " + prop
                        .getProperty("java.vm.info") + "):\n");
        long[] tids = this.tmbean.getAllThreadIds();
        ThreadInfo[] tinfos = this.tmbean.getThreadInfo(tids, 2147483647);
        for(ThreadInfo ti : tinfos)
        {
            threadDump.append("\n" + printThreadInfo(ti));
        }
        if(findDeadlock() != null)
        {
            threadDump.append(findDeadlock());
        }
        return threadDump.toString();
    }


    private SimpleDateFormat createDateFormat()
    {
        return defaultFormat;
    }


    private String dumpThreadInfoWithLocks()
    {
        String date = createDateFormat().format(new Date(System.currentTimeMillis()));
        StringBuilder threadDump = new StringBuilder("\n" + date + "\n\n");
        Properties prop = new Properties();
        prop.putAll(this.runbean.getSystemProperties());
        threadDump.append("Full thread dump " + prop
                        .getProperty("java.vm.name") + " (" + prop.getProperty("java.vm.version") + " " + prop
                        .getProperty("java.vm.info") + "):\n");
        ThreadInfo[] tinfos = this.tmbean.dumpAllThreads(true, true);
        for(ThreadInfo ti : tinfos)
        {
            threadDump.append("\n" + printThreadInfo(ti));
            LockInfo[] syncs = ti.getLockedSynchronizers();
            threadDump.append(printLockInfo(syncs));
        }
        if(findDeadlock() != null)
        {
            threadDump.append(findDeadlock());
        }
        return threadDump.toString();
    }


    private String printThreadInfo(ThreadInfo ti)
    {
        StackTraceElement[] stacktrace = ti.getStackTrace();
        MonitorInfo[] monitors = ti.getLockedMonitors();
        currentThreadInfo result = new currentThreadInfo(this, ti);
        StringBuilder threadOutput = new StringBuilder(result.getThreadName());
        threadOutput.append(result.getThreadStateDesc());
        threadOutput.append(result.getThreadStats());
        for(int i = 0; i < stacktrace.length; i++)
        {
            StackTraceElement ste = stacktrace[i];
            if(i == 0)
            {
                threadOutput.append("\n java.lang.Thread.State: " + result.getThreadState());
                threadOutput.append("\n" + INDENT + "at " + ste.toString());
                if(ste.toString().contains("java.lang.Object.wait(Native Method)") && result.getLockName() != null)
                {
                    threadOutput.append("\n" + INDENT + "- waiting on " + result.getLockName());
                }
                if(ste.toString().contains("sun.misc.Unsafe.park(Native Method)") && result.getLockName() != null)
                {
                    threadOutput.append("\n" + INDENT + "- parking to wait for " + result.getLockName());
                }
                if(result.getThreadStateDesc().contains("BLOCKED") && result.getLockName() != null)
                {
                    threadOutput.append("\n" + INDENT + "- waiting to lock " + result.getLockName());
                }
            }
            else
            {
                threadOutput.append("\n" + INDENT + "at " + ste.toString());
            }
            for(MonitorInfo mi : monitors)
            {
                if(mi.getLockedStackDepth() == i)
                {
                    threadOutput.append("\n" + INDENT + " - locked " + mi);
                }
            }
        }
        threadOutput.append("\n");
        return threadOutput.toString();
    }


    private String printLockInfo(LockInfo[] locks)
    {
        StringBuilder lockOutput = new StringBuilder(INDENT + "Locked synchronizers: count = " + INDENT + "\n");
        for(LockInfo li : locks)
        {
            lockOutput.append(INDENT + " - " + INDENT + "\n");
        }
        return lockOutput.toString();
    }


    public String findDeadlock()
    {
        StringBuilder deadlock = new StringBuilder();
        if("findDeadlockedThreads".equals("findDeadlockedThreads") && this.tmbean.isSynchronizerUsageSupported())
        {
            long[] arrayOfLong = this.tmbean.findDeadlockedThreads();
            if(arrayOfLong == null)
            {
                return null;
            }
            deadlock.append("Deadlock found :-");
            ThreadInfo[] arrayOfThreadInfo = this.tmbean.getThreadInfo(arrayOfLong, true, true);
            for(ThreadInfo ti : arrayOfThreadInfo)
            {
                deadlock.append(printThreadInfo(ti));
                deadlock.append(ti.getLockedSynchronizers());
                deadlock.append("\n");
            }
            return deadlock.toString();
        }
        long[] tids = this.tmbean.findMonitorDeadlockedThreads();
        if(tids == null)
        {
            return null;
        }
        ThreadInfo[] infos = this.tmbean.getThreadInfo(tids, 2147483647);
        for(ThreadInfo ti : infos)
        {
            deadlock.append(printThreadInfo(ti));
        }
        return deadlock.toString();
    }
}
