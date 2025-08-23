package de.hybris.platform.servicelayer.web.session;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.session.ExpiringSession;

public class PersistedSession implements ExpiringSession, Serializable
{
    private static final long serialVersionUID = 1381586320351746203L;
    private static final SerializableNullObject NULL = new SerializableNullObject();
    private final String id;
    private final ConcurrentMap<String, Object> atributes = new ConcurrentHashMap<>();
    private int maxInactiveIntervalInSeconds;
    private transient long lastAccessedTime;
    private final long creationTime;
    private final int clusterId;
    private final String extension;
    private final String contextRoot;


    public PersistedSession(String id, int clusterId, String extension, String contextRoot)
    {
        this(id, clusterId, extension, contextRoot, 86400);
    }


    public PersistedSession(String id, int clusterId, String extension, String contextRoot, int maxInactiveIntervalInSeconds)
    {
        Preconditions.checkNotNull(id);
        this.id = id;
        this.clusterId = clusterId;
        this.extension = extension;
        this.contextRoot = contextRoot;
        this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
        this.creationTime = this.lastAccessedTime = System.currentTimeMillis();
    }


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        this.lastAccessedTime = System.currentTimeMillis();
    }


    public String getId()
    {
        return this.id;
    }


    public int getClusterId()
    {
        return this.clusterId;
    }


    public <T> T getAttribute(String attributeName)
    {
        return (T)unmaskNull(this.atributes.get(attributeName));
    }


    public Set<String> getAttributeNames()
    {
        return this.atributes.keySet();
    }


    public void setAttribute(String attributeName, Object attributeValue)
    {
        this.atributes.put(attributeName, maskNull(attributeValue));
    }


    protected Object maskNull(Object attributeValue)
    {
        return (attributeValue == null) ? NULL : attributeValue;
    }


    protected Object unmaskNull(Object mapValue)
    {
        return (mapValue instanceof SerializableNullObject) ? null : mapValue;
    }


    public void removeAttribute(String attributeName)
    {
        this.atributes.remove(attributeName);
    }


    public long getCreationTime()
    {
        return this.creationTime;
    }


    public void setLastAccessedTime(long lastAccessedTime)
    {
        this.lastAccessedTime = lastAccessedTime;
    }


    public long getLastAccessedTime()
    {
        return this.lastAccessedTime;
    }


    public void setMaxInactiveIntervalInSeconds(int interval)
    {
        this.maxInactiveIntervalInSeconds = interval;
    }


    public int getMaxInactiveIntervalInSeconds()
    {
        return this.maxInactiveIntervalInSeconds;
    }


    public boolean isExpired()
    {
        return (System.currentTimeMillis() > this.lastAccessedTime + (getMaxInactiveIntervalInSeconds() * 1000));
    }


    public String getExtension()
    {
        return this.extension;
    }


    public String getContextRoot()
    {
        return this.contextRoot;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        PersistedSession that = (PersistedSession)o;
        if(!this.id.equals(that.id))
        {
            return false;
        }
        if(this.clusterId != that.clusterId)
        {
            return false;
        }
        if(this.maxInactiveIntervalInSeconds != that.maxInactiveIntervalInSeconds)
        {
            return false;
        }
        if(this.lastAccessedTime != that.lastAccessedTime)
        {
            return false;
        }
        if(this.creationTime != that.creationTime)
        {
            return false;
        }
        if(!StringUtils.equals(this.extension, that.extension))
        {
            return false;
        }
        if(!StringUtils.equals(this.contextRoot, that.contextRoot))
        {
            return false;
        }
        return this.atributes.equals(that.atributes);
    }


    public int hashCode()
    {
        return this.id.hashCode();
    }


    public String toString()
    {
        return "PersistedSession{id='" + this.id + "', clusterId=" + this.clusterId + ",extension=" + this.extension + ", contextRoot=" + this.contextRoot + ", lastAccessedTime=" + this.lastAccessedTime + ", creationTime=" + this.creationTime + ", attributeCount=" + this.atributes
                        .size() + "}";
    }
}
