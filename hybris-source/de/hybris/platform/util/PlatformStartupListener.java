package de.hybris.platform.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloConnection;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

public class PlatformStartupListener implements ServletContextListener
{
    private static final Logger log = Logger.getLogger(PlatformStartupListener.class.getName());


    public void contextInitialized(ServletContextEvent arg0)
    {
        RedeployUtilities.startup();
        if(Utilities.isSystemInitialized(Registry.getCurrentTenant().getDataSource()))
        {
            try
            {
                JaloConnection.getInstance().createAnonymousCustomerSession();
            }
            catch(Exception e)
            {
                log.error("error creating session: " + e.getMessage());
            }
        }
    }


    public void contextDestroyed(ServletContextEvent arg0)
    {
        RedeployUtilities.shutdown();
        try
        {
            log.info("Starting thread locals cleanup..");
            cleanThreadLocals();
            log.info("End thread locals cleanup");
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }


    private void cleanThreadLocals() throws NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
    {
        Thread[] threadgroup = new Thread[1024];
        Thread.enumerate(threadgroup);
        for(Thread thread : threadgroup)
        {
            if(thread != null)
            {
                cleanThreadLocals(thread);
            }
        }
    }


    private void cleanThreadLocals(Thread thread) throws NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
    {
        Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
        threadLocalsField.setAccessible(true);
        Class<?> threadLocalMapKlazz = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
        Field tableField = threadLocalMapKlazz.getDeclaredField("table");
        tableField.setAccessible(true);
        Object fieldLocal = threadLocalsField.get(thread);
        if(fieldLocal == null)
        {
            return;
        }
        Object table = tableField.get(fieldLocal);
        int threadLocalCount = Array.getLength(table);
        for(int i = 0; i < threadLocalCount; i++)
        {
            Object entry = Array.get(table, i);
            if(entry != null)
            {
                Field valueField = entry.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                Object value = valueField.get(entry);
                if(value != null)
                {
                    if(value.getClass().getName().startsWith("de.hybris"))
                    {
                        valueField.set(entry, null);
                    }
                }
            }
        }
    }
}
