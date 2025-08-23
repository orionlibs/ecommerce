package de.hybris.platform.media.storage.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageRegistry;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.media.url.MediaURLStrategy;
import java.util.Collection;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultMediaStorageRegistry implements MediaStorageRegistry
{
    private static final Logger LOG = Logger.getLogger(DefaultMediaStorageRegistry.class.getName());
    @Autowired
    private Map<String, MediaStorageStrategy> storageStrategies;
    @Autowired
    private Map<String, MediaURLStrategy> urlStrategies;


    @PostConstruct
    public void init()
    {
        logStrategiesInfo();
    }


    private void logStrategiesInfo()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("all media storage strategies: " + this.storageStrategies.keySet());
            LOG.debug("all media url strategies: " + this.urlStrategies.keySet());
        }
    }


    public void setStorageStrategies(Map<String, MediaStorageStrategy> storageStrategies)
    {
        this.storageStrategies = storageStrategies;
    }


    public void setURLStrategies(Map<String, MediaURLStrategy> urlStrategies)
    {
        this.urlStrategies = urlStrategies;
    }


    public MediaStorageStrategy getStorageStrategyForFolder(MediaStorageConfigService.MediaFolderConfig config)
    {
        Preconditions.checkArgument((config != null), "Folder config is required to perform this operation");
        MediaStorageStrategy strategy = null;
        String strategyBeanId = config.getStorageStrategyId();
        if(StringUtils.isNotBlank(strategyBeanId))
        {
            strategy = this.storageStrategies.get(strategyBeanId);
        }
        if(strategy == null)
        {
            LOG.error("Unknow media storage strategy '" + strategyBeanId + "' for folder " + config.getFolderQualifier() + ". Registered strategies are " + this.storageStrategies
                            .keySet() + ". Check 'media.default.storage.strategy' setting and media folder specific settings in project.properties file");
            throw new IllegalStateException("No suitable media storage strategy found.");
        }
        return strategy;
    }


    public MediaURLStrategy getURLStrategyForFolder(MediaStorageConfigService.MediaFolderConfig config, Collection<String> preferredUrlStrategyIds)
    {
        Preconditions.checkArgument((config != null), "Folder config is required to perform this operation");
        MediaURLStrategy strategy = null;
        String strategyBeanId = findURLStrategyBeanIdForFolder(config, preferredUrlStrategyIds);
        if(StringUtils.isNotBlank(strategyBeanId))
        {
            strategy = this.urlStrategies.get(strategyBeanId);
        }
        if(strategy == null)
        {
            LOG.error("Unknow URL storage strategy '" + strategyBeanId + "' for folder " + config.getFolderQualifier() + ". Registered strategies are " + this.urlStrategies
                            .keySet() + ". Check 'media.default.url.strategy' setting and media folder specific settings in project.properties file");
            throw new IllegalStateException("No suitable media URL strategy found.");
        }
        return strategy;
    }


    private String findURLStrategyBeanIdForFolder(MediaStorageConfigService.MediaFolderConfig config, Collection<String> preferredUrlStrategyId)
    {
        String result = null;
        Iterable<String> urlStrategyIds = config.getURLStrategyIds();
        for(String strategyId : urlStrategyIds)
        {
            if(CollectionUtils.isNotEmpty(preferredUrlStrategyId))
            {
                for(String pref : preferredUrlStrategyId)
                {
                    if(pref.equalsIgnoreCase(strategyId))
                    {
                        result = strategyId;
                        break;
                    }
                }
            }
            if(result == null)
            {
                result = strategyId;
            }
        }
        return result;
    }


    public Map<String, MediaStorageStrategy> getStorageStrategies()
    {
        return MapUtils.unmodifiableMap(this.storageStrategies);
    }
}
