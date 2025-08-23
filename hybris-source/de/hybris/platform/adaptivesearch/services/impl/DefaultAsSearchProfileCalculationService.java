package de.hybris.platform.adaptivesearch.services.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsSearchProfile;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileActivationGroup;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileCalculationService;
import de.hybris.platform.adaptivesearch.strategies.AsCacheKey;
import de.hybris.platform.adaptivesearch.strategies.AsCacheScope;
import de.hybris.platform.adaptivesearch.strategies.AsCacheStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileCalculationStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileLoadStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileMapping;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileRegistry;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;
import de.hybris.platform.adaptivesearch.strategies.impl.DefaultAsCacheKey;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchProfileCalculationService implements AsSearchProfileCalculationService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAsSearchProfileCalculationService.class);
    private AsSearchProfileResultFactory asSearchProfileResultFactory;
    private AsSearchProfileRegistry asSearchProfileRegistry;
    private AsMergeStrategy asMergeStrategy;
    private AsCacheStrategy asCacheStrategy;


    public AsSearchProfileResult createResult(AsSearchProfileContext context)
    {
        return this.asSearchProfileResultFactory.createResult();
    }


    public <T, R> AsConfigurationHolder<T, R> createConfigurationHolder(AsSearchProfileContext context, T configuration)
    {
        return this.asSearchProfileResultFactory.createConfigurationHolder(configuration);
    }


    public <T, R> AsConfigurationHolder<T, R> createConfigurationHolder(AsSearchProfileContext context, T configuration, Object data)
    {
        return this.asSearchProfileResultFactory.createConfigurationHolder(configuration, data);
    }


    public AsSearchProfileResult calculate(AsSearchProfileContext context, List<AbstractAsSearchProfileModel> searchProfiles)
    {
        return calculate(context, null, searchProfiles);
    }


    public AsSearchProfileResult calculate(AsSearchProfileContext context, AsSearchProfileResult result, List<AbstractAsSearchProfileModel> searchProfiles)
    {
        AsSearchProfileActivationGroup group = new AsSearchProfileActivationGroup();
        group.setSearchProfiles(searchProfiles);
        return calculateGroups(context, result, Collections.singletonList(group));
    }


    public AsSearchProfileResult calculateGroups(AsSearchProfileContext context, List<AsSearchProfileActivationGroup> groups)
    {
        return calculateGroups(context, null, groups);
    }


    public AsSearchProfileResult calculateGroups(AsSearchProfileContext context, AsSearchProfileResult result, List<AsSearchProfileActivationGroup> groups)
    {
        List<AsSearchProfileResult> searchProfileResults = new ArrayList<>();
        if(result != null)
        {
            searchProfileResults.add(result);
        }
        if(CollectionUtils.isNotEmpty(groups))
        {
            for(AsSearchProfileActivationGroup group : groups)
            {
                searchProfileResults.add(doCalculateGroup(context, group));
            }
        }
        return this.asMergeStrategy.merge(context, searchProfileResults, null);
    }


    protected AsSearchProfileResult doCalculateGroup(AsSearchProfileContext context, AsSearchProfileActivationGroup group)
    {
        List<AsSearchProfileResult> results = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(group.getSearchProfiles()))
        {
            for(AbstractAsSearchProfileModel searchProfile : group.getSearchProfiles())
            {
                try
                {
                    AsSearchProfileMapping strategyMapping = this.asSearchProfileRegistry.getSearchProfileMapping(searchProfile);
                    Long baseKey = searchProfile.getPk().getLong();
                    Long baseKeyVersion = Long.valueOf(searchProfile.getModifiedtime().getTime());
                    AsSearchProfileLoadStrategy loadStrategy = strategyMapping.getLoadStrategy();
                    Serializable loadKeyFragment = loadStrategy.getCacheKeyFragment(context, searchProfile);
                    DefaultAsCacheKey defaultAsCacheKey1 = new DefaultAsCacheKey(AsCacheScope.LOAD, new Serializable[] {baseKey, baseKeyVersion, loadKeyFragment});
                    AbstractAsSearchProfile loadValue = (AbstractAsSearchProfile)this.asCacheStrategy.getWithLoader((AsCacheKey)defaultAsCacheKey1, key -> loadStrategy.load(context, searchProfile));
                    AbstractAsSearchProfile loadStrategyData = loadStrategy.map(context, loadValue);
                    AsSearchProfileCalculationStrategy calculationStrategy = strategyMapping.getCalculationStrategy();
                    Serializable calculationKeyFragment = calculationStrategy.getCacheKeyFragment(context, loadStrategyData);
                    DefaultAsCacheKey defaultAsCacheKey2 = new DefaultAsCacheKey(AsCacheScope.CALCULATION, new Serializable[] {baseKey, baseKeyVersion, calculationKeyFragment});
                    AsSearchProfileResult calculationValue = (AsSearchProfileResult)this.asCacheStrategy.getWithLoader((AsCacheKey)defaultAsCacheKey2, key -> calculationStrategy.calculate(context, loadStrategyData));
                    AsSearchProfileResult calculationStrategyData = calculationStrategy.map(context, calculationValue);
                    results.add(calculationStrategyData);
                }
                catch(RuntimeException e)
                {
                    LOG.error(String.format("Could not process search profile with pk %s", new Object[] {searchProfile.getPk()}), e);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(group.getGroups()))
        {
            for(AsSearchProfileActivationGroup childGroup : group.getGroups())
            {
                results.add(doCalculateGroup(context, childGroup));
            }
        }
        return this.asMergeStrategy.merge(context, results, group.getMergeConfiguration());
    }


    public AsSearchProfileResultFactory getAsSearchProfileResultFactory()
    {
        return this.asSearchProfileResultFactory;
    }


    @Required
    public void setAsSearchProfileResultFactory(AsSearchProfileResultFactory asSearchProfileResultFactory)
    {
        this.asSearchProfileResultFactory = asSearchProfileResultFactory;
    }


    public AsSearchProfileRegistry getAsSearchProfileRegistry()
    {
        return this.asSearchProfileRegistry;
    }


    @Required
    public void setAsSearchProfileRegistry(AsSearchProfileRegistry asSearchProfileRegistry)
    {
        this.asSearchProfileRegistry = asSearchProfileRegistry;
    }


    public AsMergeStrategy getAsMergeStrategy()
    {
        return this.asMergeStrategy;
    }


    @Required
    public void setAsMergeStrategy(AsMergeStrategy asMergeStrategy)
    {
        this.asMergeStrategy = asMergeStrategy;
    }


    public AsCacheStrategy getAsCacheStrategy()
    {
        return this.asCacheStrategy;
    }


    @Required
    public void setAsCacheStrategy(AsCacheStrategy asCacheStrategy)
    {
        this.asCacheStrategy = asCacheStrategy;
    }
}
