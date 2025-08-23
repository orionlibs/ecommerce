package de.hybris.platform.regioncache.test.helper;

import java.io.PrintStream;
import java.util.Map;

public class ThreadDump
{
    public static void dumpThreads(PrintStream writer)
    {
        DeadlockDetector.printDeadlocks(writer);
        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
        for(Thread thread : traces.keySet())
        {
            writer.println(String.format("\nThread %s@%d: (state = %s)", new Object[] {thread.getName(), Long.valueOf(thread.getId()), thread
                            .getState()}));
            for(StackTraceElement stackTraceElement : (StackTraceElement[])traces.get(thread))
            {
                writer.println(" - " + stackTraceElement);
            }
        }
    }
}
