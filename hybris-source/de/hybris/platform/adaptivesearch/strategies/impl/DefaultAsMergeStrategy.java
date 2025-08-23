package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsMergeConfiguration;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;
import de.hybris.platform.adaptivesearch.strategies.AsBoostItemsMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsBoostRulesMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsFacetsMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsGroupMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsMergeStrategyFactory;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;
import de.hybris.platform.adaptivesearch.strategies.AsSortsMergeStrategy;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsMergeStrategy implements AsMergeStrategy
{
    private AsSearchProfileResultFactory asSearchProfileResultFactory;
    private AsMergeStrategyFactory asMergeStrategyFactory;


    public AsSearchProfileResult merge(AsSearchProfileContext context, List<AsSearchProfileResult> results, AsMergeConfiguration mergeConfiguration)
    {
        if(CollectionUtils.isEmpty(results))
        {
            return this.asSearchProfileResultFactory.createResult();
        }
        if(results.size() == 1 && mergeConfiguration == null)
        {
            return results.get(0);
        }
        AsSearchProfileResult mergeResult = this.asSearchProfileResultFactory.createResult();
        AsSearchProfileResult firstResult = results.get(0);
        mergeResult.setFacetsMergeMode(resolveResultFacetsMergeMode(firstResult, mergeConfiguration));
        mergeResult.setBoostItemsMergeMode(resolveResultBoostItemsMergeMode(firstResult, mergeConfiguration));
        mergeResult.setBoostRulesMergeMode(resolveResultBoostRulesMergeMode(firstResult, mergeConfiguration));
        mergeResult.setSortsMergeMode(resolveSortsMergeMode(firstResult, mergeConfiguration));
        mergeResult.setGroupMergeMode(resolveGroupMergeMode(firstResult, mergeConfiguration));
        for(AsSearchProfileResult result : results)
        {
            AsFacetsMergeStrategy facetsMergeStrategy = this.asMergeStrategyFactory.getFacetsMergeStrategy(resolveFacetsMergeMode(result, mergeConfiguration));
            facetsMergeStrategy.mergeFacets(result, mergeResult);
            AsBoostItemsMergeStrategy boostItemsMergeStrategy = this.asMergeStrategyFactory.getBoostItemsMergeStrategy(resolveBoostItemsMergeMode(result, mergeConfiguration));
            boostItemsMergeStrategy.mergeBoostItems(result, mergeResult);
            AsBoostRulesMergeStrategy boostRulesMergeStrategy = this.asMergeStrategyFactory.getBoostRulesMergeStrategy(resolveBoostRulesMergeMode(result, mergeConfiguration));
            boostRulesMergeStrategy.mergeBoostRules(result, mergeResult);
            AsSortsMergeStrategy sortsMergeStrategy = this.asMergeStrategyFactory.getSortsMergeStrategy(resolveSortsMergeMode(result, mergeConfiguration));
            sortsMergeStrategy.mergeSorts(result, mergeResult);
            AsGroupMergeStrategy groupMergeStrategy = this.asMergeStrategyFactory.getGroupMergeStrategy(resolveGroupMergeMode(result, mergeConfiguration));
            groupMergeStrategy.mergeGroup(result, mergeResult);
        }
        return mergeResult;
    }


    protected AsFacetsMergeMode resolveResultFacetsMergeMode(AsSearchProfileResult firstResult, AsMergeConfiguration mergeConfiguration)
    {
        if(mergeConfiguration != null && mergeConfiguration.getResultFacetsMergeMode() != null)
        {
            return mergeConfiguration.getResultFacetsMergeMode();
        }
        return firstResult.getFacetsMergeMode();
    }


    protected AsBoostItemsMergeMode resolveResultBoostItemsMergeMode(AsSearchProfileResult firstResult, AsMergeConfiguration mergeConfiguration)
    {
        if(mergeConfiguration != null && mergeConfiguration.getResultBoostItemsMergeMode() != null)
        {
            return mergeConfiguration.getResultBoostItemsMergeMode();
        }
        return firstResult.getBoostItemsMergeMode();
    }


    protected AsBoostRulesMergeMode resolveResultBoostRulesMergeMode(AsSearchProfileResult firstResult, AsMergeConfiguration mergeConfiguration)
    {
        if(mergeConfiguration != null && mergeConfiguration.getResultBoostRulesMergeMode() != null)
        {
            return mergeConfiguration.getResultBoostRulesMergeMode();
        }
        return firstResult.getBoostRulesMergeMode();
    }


    protected AsFacetsMergeMode resolveFacetsMergeMode(AsSearchProfileResult result, AsMergeConfiguration mergeConfiguration)
    {
        if(mergeConfiguration != null && mergeConfiguration.getFacetsMergeMode() != null)
        {
            return mergeConfiguration.getFacetsMergeMode();
        }
        return result.getFacetsMergeMode();
    }


    protected AsBoostItemsMergeMode resolveBoostItemsMergeMode(AsSearchProfileResult result, AsMergeConfiguration mergeConfiguration)
    {
        if(mergeConfiguration != null && mergeConfiguration.getBoostItemsMergeMode() != null)
        {
            return mergeConfiguration.getBoostItemsMergeMode();
        }
        return result.getBoostItemsMergeMode();
    }


    protected AsBoostRulesMergeMode resolveBoostRulesMergeMode(AsSearchProfileResult result, AsMergeConfiguration mergeConfiguration)
    {
        if(mergeConfiguration != null && mergeConfiguration.getBoostRulesMergeMode() != null)
        {
            return mergeConfiguration.getBoostRulesMergeMode();
        }
        return result.getBoostRulesMergeMode();
    }


    protected AsSortsMergeMode resolveSortsMergeMode(AsSearchProfileResult result, AsMergeConfiguration mergeConfiguration)
    {
        if(mergeConfiguration != null && mergeConfiguration.getSortsMergeMode() != null)
        {
            return mergeConfiguration.getSortsMergeMode();
        }
        return result.getSortsMergeMode();
    }


    protected AsGroupMergeMode resolveGroupMergeMode(AsSearchProfileResult result, AsMergeConfiguration mergeConfiguration)
    {
        if(mergeConfiguration != null && mergeConfiguration.getGroupMergeMode() != null)
        {
            return mergeConfiguration.getGroupMergeMode();
        }
        return result.getGroupMergeMode();
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


    public AsMergeStrategyFactory getAsMergeStrategyFactory()
    {
        return this.asMergeStrategyFactory;
    }


    @Required
    public void setAsMergeStrategyFactory(AsMergeStrategyFactory asMergeStrategyFactory)
    {
        this.asMergeStrategyFactory = asMergeStrategyFactory;
    }
}
