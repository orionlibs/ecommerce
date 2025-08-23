package de.hybris.platform.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public final class ThreadUtilities
{
    protected static Set getAllThreads()
    {
        Thread t = Thread.currentThread();
        ThreadGroup tg = t.getThreadGroup();
        while(tg.getParent() != null)
        {
            tg = tg.getParent();
        }
        Thread[] threads = new Thread[tg.activeCount() * 2];
        tg.enumerate(threads);
        Set<Thread> result = new HashSet();
        for(int i = 0; i < threads.length; i++)
        {
            Thread next = threads[i];
            if(next != null)
            {
                result.add(next);
            }
        }
        return result;
    }


    public static int getThreadCount()
    {
        return getAllThreads().size();
    }


    public static Map getThreadCountMap()
    {
        Map<Object, Object> result = new HashMap<>();
        Iterator<Thread> threads = getAllThreads().iterator();
        while(threads.hasNext())
        {
            Thread next = threads.next();
            Integer oldCount = (Integer)result.get(next.getClass());
            Integer newCount = (oldCount == null) ? Integer.valueOf(1) : Integer.valueOf(1 + oldCount.intValue());
            result.put(next.getClass(), newCount);
        }
        return result;
    }


    public static void printThreadDump(PrintStream out)
    {
        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
        out.println("Full thread dump at " + (new Date()).toString() + " containing " + traces.size() + " threads:");
        for(Map.Entry<Thread, StackTraceElement[]> trace : traces.entrySet())
        {
            Thread t = trace.getKey();
            out.println("\"" + t.getName() + "\"" + (t.isDaemon() ? " daemon" : "") + " prio=" + t.getPriority() + " tid=" + t
                            .getId() + " " + t.getState().name());
            for(int i = 0; i < ((StackTraceElement[])trace.getValue()).length; i++)
            {
                out.println("\tat " + ((StackTraceElement[])trace.getValue())[i]);
            }
        }
    }


    public static String printThreadDump()
    {
        OutputStream out = new ByteArrayOutputStream();
        PrintStream s = new PrintStream(out);
        printThreadDump(s);
        try
        {
            out.flush();
            String result = ((ByteArrayOutputStream)out).toString("UTF-8");
            out.close();
            return result;
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static int getNumberOfThreadsFromExpression(String expression, int defaultValue)
    {
        Preconditions.checkArgument((defaultValue > 0), "defaultValue must be greater than 0");
        if(StringUtils.isBlank(expression))
        {
            return defaultValue;
        }
        ImmutableMap immutableMap = ImmutableMap.of("cores", Integer.valueOf(getNumberOfAvailableCores()));
        Integer valueOfExpresion = ExpressionUtils.evaluateToInteger(expression, (Map)immutableMap);
        if(valueOfExpresion == null)
        {
            return defaultValue;
        }
        int value = valueOfExpresion.intValue();
        return (value < 1) ? defaultValue : value;
    }


    public static int getNumberOfAvailableCores()
    {
        return Runtime.getRuntime().availableProcessors();
    }
}
