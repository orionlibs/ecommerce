package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.constants.AdaptivesearchConstants;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsConfigurableSearchConfiguration;
import de.hybris.platform.adaptivesearch.data.AsGenericSearchProfile;
import de.hybris.platform.adaptivesearch.data.AsReference;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.strategies.AsMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsGenericSearchProfileCalculationStrategy extends AbstractAsSearchProfileCalculationStrategy<AsGenericSearchProfile>
{
    private AsSearchProfileResultFactory asSearchProfileResultFactory;
    private AsMergeStrategy asMergeStrategy;


    public Serializable getCacheKeyFragment(AsSearchProfileContext context, AsGenericSearchProfile searchProfile)
    {
        List<String> qualifiers = extractQualifiers(context, searchProfile);
        if(CollectionUtils.isEmpty(qualifiers))
        {
            return null;
        }
        Map<String, AsReference> availableSearchConfigurations = searchProfile.getAvailableSearchConfigurations();
        ArrayList<Long> keyFragment = new ArrayList<>();
        for(String qualifier : qualifiers)
        {
            AsReference searchConfigurationReference = availableSearchConfigurations.get(qualifier);
            keyFragment.add(searchConfigurationReference.getPk().getLong());
            keyFragment.add(Long.valueOf(searchConfigurationReference.getVersion()));
        }
        return keyFragment;
    }


    public AsSearchProfileResult calculate(AsSearchProfileContext context, AsGenericSearchProfile searchProfile)
    {
        AsSearchProfileResult defaultResult;
        List<AsSearchProfileResult> results = new ArrayList<>();
        Map<String, AsConfigurableSearchConfiguration> searchConfigurations = searchProfile.getSearchConfigurations();
        AsConfigurableSearchConfiguration defaultSearchConfiguration = searchConfigurations.get(AdaptivesearchConstants.DEFAULT_QUALIFIER);
        if(defaultSearchConfiguration == null)
        {
            defaultResult = this.asSearchProfileResultFactory.createResult();
        }
        else
        {
            defaultResult = this.asSearchProfileResultFactory.createResultFromSearchConfiguration(defaultSearchConfiguration);
        }
        results.add(defaultResult);
        List<String> qualifiers = extractQualifiers(context, searchProfile);
        if(CollectionUtils.isNotEmpty(qualifiers))
        {
            for(String qualifier : qualifiers)
            {
                AsConfigurableSearchConfiguration searchConfiguration = searchConfigurations.get(qualifier);
                if(searchConfiguration != null)
                {
                    results.add(this.asSearchProfileResultFactory.createResultFromSearchConfiguration(searchConfiguration));
                }
            }
        }
        AsSearchProfileResult result = this.asMergeStrategy.merge(context, results, null);
        result.setFacetsMergeMode(defaultResult.getFacetsMergeMode());
        result.setBoostItemsMergeMode(defaultResult.getBoostItemsMergeMode());
        result.setBoostRulesMergeMode(defaultResult.getBoostRulesMergeMode());
        result.setSortsMergeMode(defaultResult.getSortsMergeMode());
        return result;
    }


    protected List<String> extractQualifiers(AsSearchProfileContext context, AsGenericSearchProfile searchProfile)
    {
        if(MapUtils.isEmpty(context.getQualifiers()) || StringUtils.isBlank(searchProfile.getQualifierType()))
        {
            return Collections.emptyList();
        }
        List<String> qualifiers = (List<String>)context.getQualifiers().get(searchProfile.getQualifierType());
        if(CollectionUtils.isEmpty(qualifiers))
        {
            return Collections.emptyList();
        }
        Map<String, AsReference> availableSearchConfigurations = searchProfile.getAvailableSearchConfigurations();
        Objects.requireNonNull(availableSearchConfigurations);
        return (List<String>)qualifiers.stream().filter(Objects::nonNull).filter(availableSearchConfigurations::containsKey)
                        .collect(Collectors.toList());
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


    public AsMergeStrategy getAsMergeStrategy()
    {
        return this.asMergeStrategy;
    }


    @Required
    public void setAsMergeStrategy(AsMergeStrategy asMergeStrategy)
    {
        this.asMergeStrategy = asMergeStrategy;
    }
}
