package de.hybris.platform.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;

public class SingletonCreator
{
    public static final Logger log = Logger.getLogger(SingletonCreator.class);
    private final Map<Object, Object> mappings = new ConcurrentHashMap<>(50, 0.75F, 4);
    private final Map<Object, CreatorHolder> creatorMappings = new ConcurrentHashMap<>();
    private final AtomicInteger mappingsCounter = new AtomicInteger(0);
    private final Map<Object, Exception> keysInCreation = new HashMap<>(20);
    private final AtomicBoolean destroyed = new AtomicBoolean(false);


    public <T> T getSingleton(Class<T> clazz)
    {
        CreatorHolder<T> holder = this.creatorMappings.get(clazz.getName());
        return getSingleton((holder == null) ? createClassCreator(clazz) : holder.creator);
    }


    private <T> Creator<T> createClassCreator(Class<T> cl)
    {
        return (Creator<T>)new Object(this, cl);
    }


    public <T> T getSingleton(Creator<T> creator)
    {
        assertNotDestroyed(creator);
        return getOrCreateSingleton(creator, creator.isExpired());
    }


    public <T> T replaceSingleton(Creator<T> creator)
    {
        assertNotDestroyed(creator);
        return getOrCreateSingleton(creator, true);
    }


    public <T> T getSingletonIfExists(Creator<T> creator)
    {
        return (T)this.mappings.get(creator.getID());
    }


    private <T> T getOrCreateSingleton(Creator<T> creator, boolean replaceExisting)
    {
        Object key = creator.getID();
        T ret = (T)this.mappings.get(key);
        if(replaceExisting || ret == null)
        {
            synchronized(this)
            {
                assertNotDestroyed(creator);
                ret = (T)this.mappings.get(key);
                if(replaceExisting || ret == null)
                {
                    T replaced = ret;
                    ret = createAndRegisterSingleton(creator, key, replaced);
                    if(replaced != null)
                    {
                        destroySingleton(replaced, creator);
                    }
                }
            }
        }
        return ret;
    }


    private final <T> void destroySingleton(T replaced, Creator<T> creator)
    {
        try
        {
            creator.destroy(replaced);
        }
        catch(Exception e)
        {
            log.error("error destroying replaced singleton " + replaced + " : " + e.getMessage(), e);
        }
    }


    private final <T> T createAndRegisterSingleton(Creator<T> creator, Object key, T replacedOrNull)
    {
        T o = null;
        assertNotCreatedTwice(creator, key);
        try
        {
            this.keysInCreation.put(key, new Exception());
            o = (T)creator.create();
        }
        catch(Exception e)
        {
            throw wrapCreationException(creator, e);
        }
        finally
        {
            this.keysInCreation.remove(key);
        }
        if(this.mappings.put(key, o) != replacedOrNull)
        {
            throw new Error("SingletonCreator.getSingleton(..): Seems we run into a double-locking issue!? " + key);
        }
        this.creatorMappings.put(key, new CreatorHolder(creator, this.mappingsCounter.incrementAndGet()));
        return o;
    }


    private final <T> void assertNotCreatedTwice(Creator<T> creator, Object key)
    {
        Exception oldEx = this.keysInCreation.get(key);
        if(oldEx != null)
        {
            throw createSingletonException(creator, key, oldEx);
        }
    }


    private <T> RuntimeException wrapCreationException(Creator<T> creator, Exception e)
    {
        if(e instanceof RuntimeException)
        {
            throw (RuntimeException)e;
        }
        throw new SingletonCreateException(creator, e);
    }


    private final <T> void assertNotDestroyed(Creator<T> creator)
    {
        if(isDestroyed())
        {
            if(RedeployUtilities.isShutdownInProgress())
            {
                throw new SingletonCreateException(creator, "system shutdown in progress.");
            }
            throw new SingletonCreateException(creator, "this SingletonCreator instance is already destroyed.");
        }
    }


    private final <T> SingletonCreateException createSingletonException(Creator<T> creator, Object key, Exception oldEx)
    {
        StringBuilder s = new StringBuilder("Detected infinite loop in SingletonCreator.getSingleton(..) key was '" + key + "'. ");
        s.append("This happens if you try to create a Singleton during creation of the same singleton.\n");
        s.append("It was initiated from the following two locations: \n");
        s.append("--------FIRST CODE LOCATION: ");
        s.append(Utilities.getStackTraceAsString(oldEx));
        s.append("--------SECOND CODE LOCATION: ");
        s.append(Utilities.getStackTraceAsString(new Exception()));
        throw new SingletonCreateException(creator, s.toString());
    }


    public synchronized void destroy()
    {
        this.destroyed.set(true);
        clear();
    }


    public boolean isDestroyed()
    {
        return this.destroyed.get();
    }


    public synchronized void clear()
    {
        List<CreatorHolder> holders = new ArrayList<>(this.creatorMappings.values());
        Collections.sort(holders, COMP);
        Map<Object, Object> singletons = new HashMap<>(this.mappings);
        this.mappings.clear();
        this.creatorMappings.clear();
        this.keysInCreation.clear();
        for(CreatorHolder holder : holders)
        {
            Object o = singletons.get(holder.creator.getID());
            if(o != null)
            {
                destroySingleton(o, holder.creator);
            }
        }
    }


    private static final Comparator<CreatorHolder> COMP = (Comparator<CreatorHolder>)new Object();
}
