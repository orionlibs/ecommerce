package de.hybris.platform.adaptivesearch.util;

import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;
import de.hybris.platform.util.Config;

public final class ConfigurationUtils
{
    public static final String DEFAULT_FACETS_MERGE_MODE = "adaptivesearch.merge.facets.default";
    public static final String DEFAULT_BOOST_ITEMS_MERGE_MODE = "adaptivesearch.merge.boostitems.default";
    public static final String DEFAULT_BOOST_RULES_MERGE_MODE = "adaptivesearch.merge.boostrules.default";
    public static final String DEFAULT_SORTS_MERGE_MODE = "adaptivesearch.merge.sorts.default";
    public static final String DEFAULT_GROUP_MERGE_MODE = "adaptivesearch.merge.group.default";


    public static AsFacetsMergeMode getDefaultFacetsMergeMode()
    {
        return AsFacetsMergeMode.valueOf(Config.getString("adaptivesearch.merge.facets.default", AsFacetsMergeMode.ADD_AFTER.name()));
    }


    public static AsBoostItemsMergeMode getDefaultBoostItemsMergeMode()
    {
        return
                        AsBoostItemsMergeMode.valueOf(Config.getString("adaptivesearch.merge.boostitems.default", AsBoostItemsMergeMode.ADD_AFTER.name()));
    }


    public static AsBoostRulesMergeMode getDefaultBoostRulesMergeMode()
    {
        return AsBoostRulesMergeMode.valueOf(Config.getString("adaptivesearch.merge.boostrules.default", AsBoostRulesMergeMode.ADD.name()));
    }


    public static AsSortsMergeMode getDefaultSortsMergeMode()
    {
        return AsSortsMergeMode.valueOf(Config.getString("adaptivesearch.merge.sorts.default", AsSortsMergeMode.ADD_AFTER.name()));
    }


    public static AsGroupMergeMode getDefaultGroupMergeMode()
    {
        return AsGroupMergeMode.valueOf(Config.getString("adaptivesearch.merge.group.default", AsGroupMergeMode.INHERIT.name()));
    }
}
