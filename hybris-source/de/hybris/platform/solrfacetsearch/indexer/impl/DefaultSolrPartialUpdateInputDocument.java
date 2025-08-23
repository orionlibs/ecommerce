package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.RangeNameProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;

public class DefaultSolrPartialUpdateInputDocument extends DefaultSolrInputDocument
{
    private final Set<String> indexedFields;
    private Set<String> notUpdatedIndexedFields;


    public DefaultSolrPartialUpdateInputDocument(SolrInputDocument delegate, IndexerBatchContext batchContext, FieldNameProvider fieldNameProvider, RangeNameProvider rangeNameProvider, Set<String> indexedFields)
    {
        super(delegate, batchContext, fieldNameProvider, rangeNameProvider);
        this.indexedFields = indexedFields;
    }


    public Set<String> getIndexedFields()
    {
        return this.indexedFields;
    }


    public Set<String> getNotUpdatedIndexedFields()
    {
        return this.notUpdatedIndexedFields;
    }


    public void addField(String fieldName, Object value) throws FieldValueProviderException
    {
        Collection<Object> newFieldValue;
        this.notUpdatedIndexedFields.remove(fieldName);
        Map<String, Object> fieldModifier = (Map<String, Object>)getDelegate().getFieldValue(fieldName);
        if(fieldModifier == null)
        {
            fieldModifier = new HashMap<>();
            fieldModifier.put("set", (value instanceof Collection) ? new ArrayList((Collection)value) : value);
            getDelegate().addField(fieldName, fieldModifier);
            return;
        }
        Object fieldValue = fieldModifier.get("set");
        if(fieldValue instanceof Collection)
        {
            newFieldValue = (Collection)fieldValue;
        }
        else
        {
            newFieldValue = new ArrayList();
            newFieldValue.add(fieldValue);
            fieldModifier.put("set", newFieldValue);
        }
        if(value instanceof Collection)
        {
            newFieldValue.addAll((Collection)value);
        }
        else
        {
            newFieldValue.add(value);
        }
    }


    public Object getFieldValue(String fieldName)
    {
        SolrInputField field = getDelegate().getField(fieldName);
        if(field == null)
        {
            return null;
        }
        Object value = field.getValue();
        if(value instanceof Map)
        {
            return ((Map)value).get("set");
        }
        return value;
    }


    protected void startDocument()
    {
        this.notUpdatedIndexedFields = new HashSet<>(this.indexedFields);
    }


    protected void endDocument()
    {
        for(String fieldName : this.notUpdatedIndexedFields)
        {
            Map<String, Object> fieldModifier = new HashMap<>(1);
            fieldModifier.put("set", null);
            getDelegate().addField(fieldName, fieldModifier);
        }
    }
}
