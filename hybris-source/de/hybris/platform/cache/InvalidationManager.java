package de.hybris.platform.cache;

import com.google.common.base.Preconditions;
import de.hybris.platform.cluster.InvalidationBroadcastHandler;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import java.util.Arrays;
import org.apache.log4j.Logger;

public class InvalidationManager
{
    private static final Logger log = Logger.getLogger(InvalidationManager.class.getName());
    private final InvalidationTopic rootTopic;
    private final InvalidationTarget invalidationTarget;
    private final Tenant tenant;


    public static InvalidationManager getInstance()
    {
        return Registry.getCurrentTenant().getInvalidationManager();
    }


    public InvalidationManager(InvalidationTarget target)
    {
        this(Registry.getCurrentTenantNoFallback(), target);
    }


    public InvalidationManager(Tenant tenant, InvalidationTarget target)
    {
        Preconditions.checkNotNull(tenant, "Given tenant should be not null");
        this.rootTopic = new InvalidationTopic(this, null, "root");
        this.invalidationTarget = target;
        this.tenant = tenant;
        InvalidationBroadcastHandler.getInstance();
    }


    public String toString()
    {
        return "InvalidationManager(" + this.invalidationTarget + ")";
    }


    public InvalidationTopic getInvalidationTopic(Object[] key, int invalidationDepth)
    {
        return this.rootTopic.getTopic(key, 0, invalidationDepth);
    }


    public InvalidationTopic getInvalidationTopic(Object[] key)
    {
        return this.rootTopic.getTopic(key, 0);
    }


    public InvalidationTopic getOrCreateInvalidationTopic(Object[] key)
    {
        InvalidationTopic topic = getInvalidationTopic(key);
        if(topic != null)
        {
            return topic;
        }
        InvalidationTopic parentTopic = null;
        while(parentTopic == null)
        {
            String[] arrayOfString = new String[key.length - 1];
            System.arraycopy(key, 0, arrayOfString, 0, arrayOfString.length);
            parentTopic = getInvalidationTopic((Object[])arrayOfString);
            if(parentTopic == null)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("Implicitly creating parent topic " + Arrays.<String>asList(arrayOfString));
                }
                parentTopic = getOrCreateInvalidationTopic((Object[])arrayOfString);
            }
        }
        InvalidationTopic newTopic = new InvalidationTopic(this, parentTopic, key[key.length - 1]);
        parentTopic.addSubTopic(newTopic);
        return newTopic;
    }


    public InvalidationTarget getInvalidationTarget()
    {
        return this.invalidationTarget;
    }


    public void multicast(Object[] key, int invalidationType, InvalidationTopic topic)
    {
        InvalidationBroadcastHandler.getInstance().sendMessage(this.tenant, topic.getKey(), key, invalidationType);
    }


    public void destroy()
    {
    }
}
