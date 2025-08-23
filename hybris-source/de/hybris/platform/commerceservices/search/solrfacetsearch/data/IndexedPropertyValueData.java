package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import java.io.Serializable;

public class IndexedPropertyValueData<INDEXED_PROPERTY_TYPE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private INDEXED_PROPERTY_TYPE indexedProperty;
    private String value;


    public void setIndexedProperty(INDEXED_PROPERTY_TYPE indexedProperty)
    {
        this.indexedProperty = indexedProperty;
    }


    public INDEXED_PROPERTY_TYPE getIndexedProperty()
    {
        return this.indexedProperty;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }
}
