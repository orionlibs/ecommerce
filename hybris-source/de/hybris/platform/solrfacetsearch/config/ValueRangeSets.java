package de.hybris.platform.solrfacetsearch.config;

import java.util.List;

public final class ValueRangeSets
{
    public static ValueRangeSet createValueRangeSet(String qualifier, List<ValueRange> valueRanges)
    {
        ValueRangeSet set = new ValueRangeSet();
        set.setQualifier(qualifier);
        set.setValueRanges(valueRanges);
        return set;
    }
}
