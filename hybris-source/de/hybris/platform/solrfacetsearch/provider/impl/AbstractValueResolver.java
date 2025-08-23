package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;
import de.hybris.platform.solrfacetsearch.provider.QualifierProviderAware;
import de.hybris.platform.solrfacetsearch.provider.ValueFilter;
import de.hybris.platform.solrfacetsearch.provider.ValueResolver;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractValueResolver<T extends ItemModel, M, Q> implements ValueResolver<T>, QualifierProviderAware
{
    private SessionService sessionService;
    private QualifierProvider qualifierProvider;
    private Collection<ValueFilter> valueFilters;


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public QualifierProvider getQualifierProvider()
    {
        return this.qualifierProvider;
    }


    @Required
    public void setQualifierProvider(QualifierProvider qualifierProvider)
    {
        this.qualifierProvider = qualifierProvider;
    }


    public Collection<ValueFilter> getValueFilters()
    {
        return this.valueFilters;
    }


    public void setValueFilters(Collection<ValueFilter> valueFilters)
    {
        this.valueFilters = valueFilters;
    }


    public void resolve(InputDocument document, IndexerBatchContext batchContext, Collection<IndexedProperty> indexedProperties, T model) throws FieldValueProviderException
    {
        ServicesUtil.validateParameterNotNull("model", "model instance is null");
        try
        {
            createLocalSessionContext();
            doResolve(document, batchContext, indexedProperties, model);
        }
        finally
        {
            removeLocalSessionContext();
        }
    }


    protected void doResolve(InputDocument document, IndexerBatchContext batchContext, Collection<IndexedProperty> indexedProperties, T model) throws FieldValueProviderException
    {
        M data = loadData(batchContext, indexedProperties, model);
        ValueResolverContext<M, Q> resolverContext = new ValueResolverContext();
        resolverContext.setData(data);
        for(IndexedProperty indexedProperty : indexedProperties)
        {
            if(!this.qualifierProvider.canApply(indexedProperty))
            {
                addFieldValues(document, batchContext, indexedProperty, model, resolverContext);
            }
        }
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
        IndexedType indexedType = batchContext.getIndexedType();
        Collection<Qualifier> qualifiers = this.qualifierProvider.getAvailableQualifiers(facetSearchConfig, indexedType);
        for(Qualifier qualifier : qualifiers)
        {
            this.qualifierProvider.applyQualifier(qualifier);
            String fieldQualifier = qualifier.toFieldQualifier();
            Q qualifierData = loadQualifierData(batchContext, indexedProperties, model, qualifier);
            resolverContext.setQualifier(qualifier);
            resolverContext.setFieldQualifier(fieldQualifier);
            resolverContext.setQualifierData(qualifierData);
            for(IndexedProperty indexedProperty : indexedProperties)
            {
                if(this.qualifierProvider.canApply(indexedProperty))
                {
                    addFieldValues(document, batchContext, indexedProperty, model, resolverContext);
                }
            }
        }
    }


    protected M loadData(IndexerBatchContext batchContext, Collection<IndexedProperty> indexedProperties, T model) throws FieldValueProviderException
    {
        return null;
    }


    protected Q loadQualifierData(IndexerBatchContext batchContext, Collection<IndexedProperty> indexedProperties, T model, Qualifier qualifier) throws FieldValueProviderException
    {
        return null;
    }


    protected abstract void addFieldValues(InputDocument paramInputDocument, IndexerBatchContext paramIndexerBatchContext, IndexedProperty paramIndexedProperty, T paramT, ValueResolverContext<M, Q> paramValueResolverContext) throws FieldValueProviderException;


    protected Object filterFieldValue(IndexerBatchContext batchContext, IndexedProperty indexedProperty, Object value)
    {
        Object filedValue = value;
        if(this.valueFilters != null && !this.valueFilters.isEmpty())
        {
            for(ValueFilter valueFilter : this.valueFilters)
            {
                filedValue = valueFilter.doFilter(batchContext, indexedProperty, filedValue);
            }
        }
        return filedValue;
    }


    protected boolean addFieldValue(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, Object value, String qualifier) throws FieldValueProviderException
    {
        boolean isString = value instanceof String;
        if(isString && StringUtils.isBlank((String)value))
        {
            return false;
        }
        document.addField(indexedProperty, value, qualifier);
        return true;
    }


    protected boolean filterAndAddFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, Object value, String qualifier) throws FieldValueProviderException
    {
        boolean hasValue = false;
        Object fieldValue = value;
        if(fieldValue != null)
        {
            fieldValue = filterFieldValue(batchContext, indexedProperty, fieldValue);
            if(fieldValue instanceof Collection)
            {
                Collection<Object> values = (Collection<Object>)fieldValue;
                for(Object singleValue : values)
                {
                    hasValue |= addFieldValue(document, batchContext, indexedProperty, singleValue, qualifier);
                }
            }
            else
            {
                hasValue = addFieldValue(document, batchContext, indexedProperty, fieldValue, qualifier);
            }
        }
        return hasValue;
    }


    protected void createLocalSessionContext()
    {
        Session session = this.sessionService.getCurrentSession();
        JaloSession jaloSession = (JaloSession)this.sessionService.getRawSession(session);
        jaloSession.createLocalSessionContext();
    }


    protected void removeLocalSessionContext()
    {
        Session session = this.sessionService.getCurrentSession();
        JaloSession jaloSession = (JaloSession)this.sessionService.getRawSession(session);
        jaloSession.removeLocalSessionContext();
    }
}
