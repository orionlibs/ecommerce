package de.hybris.platform.solrfacetsearch.config;

import de.hybris.platform.core.Registry;
import de.hybris.platform.solrfacetsearch.provider.FacetDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import java.util.Collections;
import java.util.List;
import org.springframework.util.CollectionUtils;

public final class IndexedProperties
{
    private static final String DEFAULT_RANGE_SET_QUALIFIER = "default";


    public static boolean isRanged(IndexedProperty property)
    {
        return !CollectionUtils.isEmpty(property.getValueRangeSets());
    }


    public static FieldValueProvider getFieldValueProvider(IndexedProperty property)
    {
        String name = property.getFieldValueProvider();
        return (name == null) ? null : (FieldValueProvider)Registry.getGlobalApplicationContext().getBean(name, FieldValueProvider.class);
    }


    public static FacetDisplayNameProvider getFacetDisplayNameProvider(IndexedProperty property)
    {
        String name = property.getFacetDisplayNameProvider();
        return (name == null) ? null : (FacetDisplayNameProvider)Registry.getGlobalApplicationContext().getBean(name, FacetDisplayNameProvider.class);
    }


    public static FacetSortProvider getFacetSortProvider(IndexedProperty property)
    {
        String name = property.getFacetSortProvider();
        return (name == null) ? null : (FacetSortProvider)Registry.getGlobalApplicationContext().getBean(name, FacetSortProvider.class);
    }


    public static List<ValueRange> getValueRanges(IndexedProperty property, String qualifier)
    {
        ValueRangeSet valueRangeSet;
        if(qualifier == null)
        {
            valueRangeSet = (ValueRangeSet)property.getValueRangeSets().get("default");
        }
        else
        {
            valueRangeSet = (ValueRangeSet)property.getValueRangeSets().get(qualifier);
            if(valueRangeSet == null)
            {
                valueRangeSet = (ValueRangeSet)property.getValueRangeSets().get("default");
            }
        }
        if(valueRangeSet != null)
        {
            return valueRangeSet.getValueRanges();
        }
        return Collections.emptyList();
    }
}
