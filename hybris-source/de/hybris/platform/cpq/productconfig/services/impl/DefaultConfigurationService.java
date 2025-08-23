/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.cpq.productconfig.services.BusinessContextService;
import de.hybris.platform.cpq.productconfig.services.CacheAccessService;
import de.hybris.platform.cpq.productconfig.services.CacheKeyService;
import de.hybris.platform.cpq.productconfig.services.ConfigurationService;
import de.hybris.platform.cpq.productconfig.services.StrategyDeterminationService;
import de.hybris.platform.cpq.productconfig.services.cache.DefaultCacheKey;
import de.hybris.platform.cpq.productconfig.services.data.ConfigurationSummaryData;
import de.hybris.platform.cpq.productconfig.services.data.ConfigurationSummaryLineItemData;
import de.hybris.platform.cpq.productconfig.services.data.ConfigurationSummaryMessagesData;
import de.hybris.platform.cpq.productconfig.services.strategies.ConfigurationLifecycleStrategy;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Default implementation of {@link ConfigurationService}
 */
public class DefaultConfigurationService implements ConfigurationService
{
    private static final String MSG_CONFIG_ID_MISSING = "configId must not be null or empty";
    private final CacheAccessService<DefaultCacheKey, ConfigurationSummaryData> cacheAccessService;
    private final CacheKeyService cacheKeyService;
    private final StrategyDeterminationService<ConfigurationLifecycleStrategy> lifecycleStrategyDeterminationService;
    private final BusinessContextService businessContextService;


    /**
     * Constructor that gets the mandatory beans injected
     *
     * @param cacheAccessService
     *           cache access service
     * @param cacheKeyService
     *           cache key service
     * @param lifecycleStrategyDeterminationService
     *           For telling which engine is active (productive or mock)
     * @param businessContextService
     *           for determining the current owner
     */
    public DefaultConfigurationService(final CacheAccessService<DefaultCacheKey, ConfigurationSummaryData> cacheAccessService,
                    final CacheKeyService cacheKeyService,
                    final StrategyDeterminationService<ConfigurationLifecycleStrategy> lifecycleStrategyDeterminationService,
                    final BusinessContextService businessContextService)
    {
        this.cacheAccessService = cacheAccessService;
        this.cacheKeyService = cacheKeyService;
        this.lifecycleStrategyDeterminationService = lifecycleStrategyDeterminationService;
        this.businessContextService = businessContextService;
    }


    protected CacheAccessService<DefaultCacheKey, ConfigurationSummaryData> getCacheAccessService()
    {
        return cacheAccessService;
    }


    protected CacheKeyService getCacheKeyService()
    {
        return cacheKeyService;
    }


    @Override
    public ConfigurationSummaryData getConfigurationSummary(final String configId)
    {
        validateStringParameterNotNullOrEmpty(configId, MSG_CONFIG_ID_MISSING);
        return getCacheAccessService().getWithSupplier(getCacheKeyService().createConfigurationSummaryCacheKey(configId),
                        () -> getConfigurationSummaryFromEngine(configId));
    }


    protected ConfigurationSummaryData getConfigurationSummaryFromEngine(final String configId)
    {
        return getLifecycleStrategyDeterminationService().get().getConfigurationSummary(configId);
    }


    @Override
    public String createConfiguration(final String productCode)
    {
        validateStringParameterNotNullOrEmpty(productCode, "productCode must not be null or empty");
        return getLifecycleStrategyDeterminationService().get().createConfiguration(productCode,
                        businessContextService.getOwnerId());
    }


    @Override
    public boolean deleteConfiguration(final String configId)
    {
        validateStringParameterNotNullOrEmpty(configId, MSG_CONFIG_ID_MISSING);
        final boolean success = getLifecycleStrategyDeterminationService().get().deleteConfiguration(configId);
        if(success)
        {
            getCacheAccessService().remove(getCacheKeyService().createConfigurationSummaryCacheKey(configId));
        }
        return success;
    }


    @Override
    public String cloneConfiguration(final String configId, final boolean permanent)
    {
        validateStringParameterNotNullOrEmpty(configId, MSG_CONFIG_ID_MISSING);
        return getLifecycleStrategyDeterminationService().get().cloneConfiguration(configId, permanent);
    }


    @Override
    public void removeCachedConfigurationSummary(final String configId)
    {
        validateStringParameterNotNullOrEmpty(configId, MSG_CONFIG_ID_MISSING);
        getCacheAccessService().remove(getCacheKeyService().createConfigurationSummaryCacheKey(configId));
    }


    @Override
    public int getNumberOfConfigurationIssues(final String configId)
    {
        final ConfigurationSummaryData configurationSummary = getConfigurationSummary(configId);
        Preconditions.checkNotNull(configurationSummary, "We expect a summary");
        final ConfigurationSummaryMessagesData messages = configurationSummary.getConfiguration().getMessages();
        int number = 0;
        if(messages != null)
        {
            number += CollectionUtils.size(messages.getIncompleteMessages());
            number += CollectionUtils.size(messages.getIncompleteAttributes());
            number += CollectionUtils.size(messages.getInvalidMessages());
            number += CollectionUtils.size(messages.getErrorMessages());
            number += CollectionUtils.size(messages.getFailedValidations());
        }
        return number;
    }


    @Override
    public boolean hasConfigurationIssues(final String configId)
    {
        return getNumberOfConfigurationIssues(configId) > 0;
    }


    protected StrategyDeterminationService<ConfigurationLifecycleStrategy> getLifecycleStrategyDeterminationService()
    {
        return lifecycleStrategyDeterminationService;
    }


    @Override
    public void makeConfigurationPermanent(final String configId)
    {
        validateStringParameterNotNullOrEmpty(configId, MSG_CONFIG_ID_MISSING);
        lifecycleStrategyDeterminationService.get().makeConfigurationPermanent(configId);
    }


    @Override
    public List<ConfigurationSummaryLineItemData> getLineItems(final String configId)
    {
        final ConfigurationSummaryData configurationSummary = getConfigurationSummary(configId);
        Preconditions.checkNotNull(configurationSummary, "We expect a summary");
        return configurationSummary.getConfiguration().getLineItems();
    }


    protected static void validateStringParameterNotNullOrEmpty(final String parameter, final String messsage)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(parameter), messsage);
    }
}
