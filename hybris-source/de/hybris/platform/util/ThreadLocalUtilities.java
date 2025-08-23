package de.hybris.platform.util;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

public final class ThreadLocalUtilities
{
    private static final Logger LOG = Logger.getLogger(ThreadLocalUtilities.class.getName());
    private static final String THREAD_LOCALS_FIELD = "threadLocals";
    private static final Map<Class, Map<String, Field>> reflectionLookupMap = (Map)new ConcurrentHashMap<>(10, 0.75F, 16);


    public static void clearThreadLocalMemoryForCurrentThread(Map<ThreadLocal, Object> threadlocalsBefore)
    {
        try
        {
            Map<ThreadLocal, Object> currentTLs = extractThreadLocals(Thread.currentThread(), true);
            for(Map.Entry<ThreadLocal, Object> e : currentTLs.entrySet())
            {
                ThreadLocal tl = e.getKey();
                if(tl != null && threadlocalsBefore.containsKey(tl))
                {
                    tl.set(e.getValue());
                }
            }
        }
        catch(Exception e)
        {
            LOG.error("error clearing ThreadLocal data for " + Thread.currentThread(), e);
        }
    }


    public static Map<ThreadLocal, Object> extractThreadLocalValuesForCurrentThread()
    {
        return extractThreadLocals(Thread.currentThread(), false);
    }


    private static Map<ThreadLocal, Object> extractThreadLocals(Thread t, boolean clear)
    {
        Map<ThreadLocal, Object> ret = null;
        try
        {
            Field f = getOrLookupField(t.getClass(), "threadLocals");
            Object threadLocalsMap = getFieldViaReflection(t, f);
            if(threadLocalsMap != null)
            {
                if(clear)
                {
                    setFieldViaReflection(t, f, null);
                }
                WeakReference[] arrayOfWeakReference = getFieldViaReflection(threadLocalsMap, "table");
                if(arrayOfWeakReference != null)
                {
                    for(WeakReference<ThreadLocal> ref : arrayOfWeakReference)
                    {
                        ThreadLocal tl = (ref == null) ? null : ref.get();
                        if(tl != null)
                        {
                            Object value = getFieldViaReflection(ref, "value");
                            if(value != null)
                            {
                                if(ret == null)
                                {
                                    ret = new WeakHashMap<>();
                                }
                                ret.put(tl, value);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            LOG.error("error extracting ThreadLocal from " + t, e);
        }
        return (ret == null) ? Collections.EMPTY_MAP : ret;
    }


    private static <T> T getFieldViaReflection(Object o, String fieldName)
    {
        try
        {
            return (T)getOrLookupField(o.getClass(), fieldName).get(o);
        }
        catch(Exception e)
        {
            LOG.warn("could not read field '" + fieldName + "' of " + o + " due to " + e.getMessage());
            return null;
        }
    }


    private static <T> T getFieldViaReflection(Object o, Field field)
    {
        try
        {
            return (T)field.get(o);
        }
        catch(Exception e)
        {
            LOG.warn("could not read field '" + field + "' of " + o + " due to " + e.getMessage());
            return null;
        }
    }


    private static void setFieldViaReflection(Object o, Field field, Object value)
    {
        try
        {
            field.set(o, value);
        }
        catch(Exception e)
        {
            LOG.warn("could not write field '" + field + "' of " + o + " due to " + e.getMessage());
        }
    }


    private static Field getOrLookupField(Class cl, String fieldName) throws SecurityException, NoSuchFieldException
    {
        Field field = null;
        Map<String, Field> clMap = reflectionLookupMap.get(cl);
        if(clMap == null)
        {
            clMap = new ConcurrentHashMap<>(3);
            reflectionLookupMap.put(cl, clMap);
        }
        else
        {
            field = clMap.get(fieldName);
        }
        if(field == null)
        {
            field = cl.getDeclaredField(fieldName);
            if(!field.isAccessible())
            {
                field.setAccessible(true);
            }
            clMap.put(fieldName, field);
        }
        return field;
    }
}
