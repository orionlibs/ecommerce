package de.hybris.platform.testframework.runlistener;

import com.google.common.base.Joiner;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class PlatformConfigurationCheckRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(PlatformConfigurationCheckRunListener.class);
    private final LEVEL currentLevel;
    private Map<String, String> before;


    PlatformConfigurationCheckRunListener(LEVEL given)
    {
        this.currentLevel = given;
    }


    public PlatformConfigurationCheckRunListener()
    {
        this(LEVEL.ROLLBACK);
    }


    public void testStarted(Description description) throws Exception
    {
        this.before = Collections.unmodifiableMap(getPlatformProperties());
    }


    public void testFinished(Description description) throws Exception
    {
        Map<String, String> after = Collections.unmodifiableMap(getPlatformProperties());
        Set<String> removedKeys = new HashSet<>(this.before.keySet());
        removedKeys.removeAll(after.keySet());
        if(!removedKeys.isEmpty())
        {
            handleKeyChanges("!!!The keys [" + Joiner.on(",").join(removedKeys) + "] were removed during the test " + description
                            .getDisplayName());
        }
        Set<String> createdKeys = new HashSet<>(after.keySet());
        createdKeys.removeAll(this.before.keySet());
        if(!createdKeys.isEmpty())
        {
            handleKeyChanges("!!!The keys [" + Joiner.on(",").join(createdKeys) + "] were created during the test " + description
                            .getDisplayName());
        }
        for(String beforeKey : this.before.keySet())
        {
            handleSingleChange(description, beforeKey, this.before.get(beforeKey), after.get(beforeKey));
        }
        if(this.currentLevel == LEVEL.ROLLBACK)
        {
            removeCreatedKeys(createdKeys);
            restoreDeletedKeys(removedKeys);
            restorePreviousValues(after);
        }
    }


    private void restorePreviousValues(Map<String, String> after)
    {
        for(String key : this.before.keySet())
        {
            if(this.before.get(key) != null && !((String)this.before.get(key)).equals(after.get(key)))
            {
                LOG.info("!!!Restoring changed value (" + (String)after.get(key) + ")->(" + (String)this.before.get(key) + ") for the key <" + key + ">");
                Config.setParameter(key, this.before.get(key));
            }
        }
    }


    private void restoreDeletedKeys(Set<String> removedKeys)
    {
        for(String key : removedKeys)
        {
            LOG.info("!!!Restoring removed key <" + key + ">");
            Config.setParameter(key, this.before.get(key));
        }
    }


    private void removeCreatedKeys(Set<String> createdKeys)
    {
        for(String key : createdKeys)
        {
            LOG.info("!!!Removing added key <" + key + ">");
            Config.setParameter(key, null);
        }
    }


    private void handleSingleChange(Description description, String key, String before, String after)
    {
        if(after != null)
        {
            if(!after.equals(before))
            {
                handleKeyChanges("!!!For key <" + key + "> value has changed (" + before + ")->(" + after + ") during the test " + description
                                .getDisplayName());
            }
        }
        else if(before != null)
        {
            handleKeyChanges("!!!For key <" + key + "> value has changed (" + before + ")->(" + after + ") during the test " + description
                            .getDisplayName());
        }
    }


    private void handleKeyChanges(String message)
    {
        if(this.currentLevel == LEVEL.ABORT)
        {
            throw new IllegalStateException(message);
        }
        LOG.warn(message);
    }


    protected Map<String, String> getPlatformProperties()
    {
        return Config.getAllParameters();
    }
}
