package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.RangeNameProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;

public class DefaultSolrInputDocument implements InputDocument
{
    private final SolrInputDocument delegate;
    private final IndexerBatchContext batchContext;
    private final FieldNameProvider fieldNameProvider;
    private final RangeNameProvider rangeNameProvider;


    public DefaultSolrInputDocument(SolrInputDocument delegate, IndexerBatchContext batchContext, FieldNameProvider fieldNameProvider, RangeNameProvider rangeNameProvider)
    {
        this.delegate = delegate;
        this.batchContext = batchContext;
        this.fieldNameProvider = fieldNameProvider;
        this.rangeNameProvider = rangeNameProvider;
    }


    public SolrInputDocument getDelegate()
    {
        return this.delegate;
    }


    public IndexerBatchContext getBatchContext()
    {
        return this.batchContext;
    }


    public FieldNameProvider getFieldNameProvider()
    {
        return this.fieldNameProvider;
    }


    public RangeNameProvider getRangeNameProvider()
    {
        return this.rangeNameProvider;
    }


    public void addField(String fieldName, Object value) throws FieldValueProviderException
    {
        if(value instanceof Collection)
        {
            this.delegate.addField(fieldName, new ArrayList((Collection)value));
        }
        else
        {
            this.delegate.addField(fieldName, value);
        }
    }


    public void addField(IndexedProperty indexedProperty, Object value) throws FieldValueProviderException
    {
        addField(indexedProperty, value, null);
    }


    public void addField(IndexedProperty indexedProperty, Object value, String qualifier) throws FieldValueProviderException
    {
        Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, qualifier);
        List<String> rangeNameList = this.rangeNameProvider.getRangeNameList(indexedProperty, value, qualifier);
        for(String fieldName : fieldNames)
        {
            if(rangeNameList.isEmpty())
            {
                addField(fieldName, value);
                continue;
            }
            for(String rangeName : rangeNameList)
            {
                addField(fieldName, (rangeName == null) ? value : rangeName);
            }
        }
    }


    public Object getFieldValue(String fieldName)
    {
        SolrInputField field = this.delegate.getField(fieldName);
        if(field == null)
        {
            return null;
        }
        return field.getValue();
    }


    public Collection<String> getFieldNames()
    {
        return this.delegate.getFieldNames();
    }


    protected void startDocument()
    {
    }


    protected void endDocument()
    {
    }
}
