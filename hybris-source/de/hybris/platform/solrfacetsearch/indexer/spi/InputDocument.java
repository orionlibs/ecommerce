package de.hybris.platform.solrfacetsearch.indexer.spi;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import java.util.Collection;

public interface InputDocument
{
    void addField(String paramString, Object paramObject) throws FieldValueProviderException;


    void addField(IndexedProperty paramIndexedProperty, Object paramObject) throws FieldValueProviderException;


    void addField(IndexedProperty paramIndexedProperty, Object paramObject, String paramString) throws FieldValueProviderException;


    Object getFieldValue(String paramString);


    Collection<String> getFieldNames();
}
