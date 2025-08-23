package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import java.util.List;

public interface RangeNameProvider
{
    boolean isRanged(IndexedProperty paramIndexedProperty);


    List<ValueRange> getValueRanges(IndexedProperty paramIndexedProperty, String paramString);


    List<String> getRangeNameList(IndexedProperty paramIndexedProperty, Object paramObject) throws FieldValueProviderException;


    List<String> getRangeNameList(IndexedProperty paramIndexedProperty, Object paramObject, String paramString) throws FieldValueProviderException;
}
