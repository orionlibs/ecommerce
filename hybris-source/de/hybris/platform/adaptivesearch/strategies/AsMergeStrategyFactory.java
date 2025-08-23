package de.hybris.platform.adaptivesearch.strategies;

import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;

public interface AsMergeStrategyFactory
{
    AsFacetsMergeStrategy getFacetsMergeStrategy(AsFacetsMergeMode paramAsFacetsMergeMode);


    AsBoostItemsMergeStrategy getBoostItemsMergeStrategy(AsBoostItemsMergeMode paramAsBoostItemsMergeMode);


    AsBoostRulesMergeStrategy getBoostRulesMergeStrategy(AsBoostRulesMergeMode paramAsBoostRulesMergeMode);


    AsSortsMergeStrategy getSortsMergeStrategy(AsSortsMergeMode paramAsSortsMergeMode);


    AsGroupMergeStrategy getGroupMergeStrategy(AsGroupMergeMode paramAsGroupMergeMode);
}
