package de.hybris.platform.cache;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;

public final class InvalidationTopic
{
    static final Logger log = Logger.getLogger(InvalidationTopic.class.getName());
    private final InvalidationManager invalidationManager;
    private final InvalidationTopic superTopic;
    private final Object topicName;
    private final List<InvalidationListener> listeners;
    private final ConcurrentMap<Object, InvalidationTopic> subTopics;
    private final Object[] key;


    protected InvalidationTopic(InvalidationManager manager, InvalidationTopic parentTopic, Object name)
    {
        this.invalidationManager = manager;
        this.superTopic = parentTopic;
        this.topicName = name;
        this.key = calculateKey(parentTopic, name);
        this.subTopics = new ConcurrentHashMap<>(10, 0.75F, 32);
        this.listeners = new CopyOnWriteArrayList<>();
    }


    public String toString()
    {
        return "InvalidationTopic" + Arrays.<Object>asList(getKey());
    }


    public Object getName()
    {
        return this.topicName;
    }


    public Object[] getKey()
    {
        return this.key;
    }


    private Object[] calculateKey(InvalidationTopic superTopic, Object name)
    {
        List<Object> keyList = new LinkedList();
        if(superTopic != null)
        {
            keyList.add(name);
        }
        InvalidationTopic next = this;
        while((next = next.getSuperTopic()) != null)
        {
            if(next.getSuperTopic() != null)
            {
                keyList.add(0, next.getName());
            }
        }
        return keyList.toArray(new Object[keyList.size()]);
    }


    public InvalidationTopic getSuperTopic()
    {
        return this.superTopic;
    }


    public void addInvalidationListener(InvalidationListener newListener)
    {
        if(this.listeners.contains(newListener))
        {
            log.error("Invalidation listener " + newListener + " is already registered!");
        }
        else
        {
            this.listeners.add(newListener);
        }
    }


    public void removeInvalidationListener(InvalidationListener newListener)
    {
        boolean removed = this.listeners.remove(newListener);
        if(!removed)
        {
            log.info("removeInvalidationListener: tried to remove non existing listener, no prob!");
        }
    }


    public void addSubTopic(InvalidationTopic topic)
    {
        if(this.subTopics.putIfAbsent(topic.getName(), topic) != null)
        {
            throw new IllegalArgumentException("topic " + getName() + " already has subtopic " + topic.getName());
        }
    }


    protected InvalidationTopic getTopic(Object[] key)
    {
        return getTopic(key, 0);
    }


    protected InvalidationTopic getTopic(Object[] key, int depth)
    {
        return getTopic(key, depth, 2147483647);
    }


    protected InvalidationTopic getTopic(Object[] key, int startDepth, int invalidationDepth)
    {
        InvalidationTopic ret = this;
        for(int i = startDepth; ret != null && i < key.length && i < invalidationDepth; i++)
        {
            ret = ret.subTopics.get(key[i]);
        }
        return ret;
    }


    public void invalidate(Object[] key, int invalidationType)
    {
        try
        {
            this.invalidationManager.multicast(key, invalidationType, this);
        }
        finally
        {
            invalidateLocally(key, invalidationType, null);
        }
    }


    public void invalidateLocally(Object[] key, int invalidationType, InvalidationTarget realTarget, RemoteInvalidationSource remoteSrc)
    {
        for(InvalidationListener listener : this.listeners)
        {
            try
            {
                listener.keyInvalidated(key, invalidationType, realTarget, remoteSrc);
            }
            catch(Exception e)
            {
                log.error("unexpected error invalidating " + this + " via listener " + listener, e);
            }
        }
        if(this.superTopic != null)
        {
            this.superTopic.invalidateLocally(key, invalidationType, realTarget, remoteSrc);
        }
    }


    public void invalidateLocally(Object[] key, int invalidationType, RemoteInvalidationSource remoteSrc)
    {
        invalidateLocally(key, invalidationType, this.invalidationManager.getInvalidationTarget(), remoteSrc);
    }
}
