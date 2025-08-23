package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.provider.ValueFilter;
import java.util.Arrays;

@UnitTest
public class SplitValueFilter implements ValueFilter
{
    public static final String SPLIT_PARAM = "split";
    public static final boolean SPLIT_PARAM_DEFAULT_VALUE = false;
    public static final String SPLIT_REGEX_PARAM = "splitRegex";
    public static final String SPLIT_REGEX_PARAM_DEFAULT_VALUE = "\\s+";


    public Object doFilter(IndexerBatchContext batchContext, IndexedProperty indexedProperty, Object value)
    {
        boolean isStringValue = value instanceof String;
        boolean split = ValueProviderParameterUtils.getBoolean(indexedProperty, "split", false);
        if(isStringValue && split)
        {
            String splitRegex = ValueProviderParameterUtils.getString(indexedProperty, "splitRegex", "\\s+");
            String[] attributeValues = ((String)value).split(splitRegex);
            return Arrays.asList(attributeValues);
        }
        return value;
    }
}
