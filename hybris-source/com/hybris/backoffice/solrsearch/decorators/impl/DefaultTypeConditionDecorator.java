package com.hybris.backoffice.solrsearch.decorators.impl;

import com.hybris.backoffice.solrsearch.dataaccess.SearchConditionData;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTypeConditionDecorator extends AbstractOrderedSearchConditionDecorator
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTypeConditionDecorator.class);
    private String typeCodeFieldName;
    private TypeService typeService;


    public void decorate(SearchConditionData conditionData, SearchQueryData queryData, IndexedType indexedType)
    {
        if(hasIndexedTypeCodeField(indexedType))
        {
            SolrSearchCondition typeCondition = prepareTypeCondition(queryData, indexedType);
            conditionData.addFilterQueryCondition(typeCondition);
        }
        else
        {
            LOG.warn("Field name {} is not configured in the indexed type {}. Search results might be inaccurate", this.typeCodeFieldName, indexedType
                            .getIndexName());
        }
    }


    protected SolrSearchCondition prepareTypeCondition(SearchQueryData queryData, IndexedType indexedType)
    {
        ComposedTypeModel composedTypeForCode = this.typeService.getComposedTypeForCode(queryData.getSearchType());
        SolrSearchCondition typeCondition = new SolrSearchCondition(this.typeCodeFieldName, getIndexedPropertyType(indexedType), SearchQuery.Operator.OR);
        typeCondition.addConditionValue(queryData.getSearchType(), ValueComparisonOperator.EQUALS);
        if(queryData.isIncludeSubtypes())
        {
            composedTypeForCode.getAllSubTypes().forEach(ct -> typeCondition.addConditionValue(ct.getCode(), ValueComparisonOperator.EQUALS));
        }
        return typeCondition;
    }


    protected boolean hasIndexedTypeCodeField(IndexedType indexedType)
    {
        return indexedType.getIndexedProperties().containsKey(getTypeCodeFieldName());
    }


    protected String getIndexedPropertyType(IndexedType indexedType)
    {
        return ((IndexedProperty)indexedType.getIndexedProperties().get(getTypeCodeFieldName())).getType();
    }


    @Required
    public void setTypeCodeFieldName(String typeCodeFieldName)
    {
        this.typeCodeFieldName = typeCodeFieldName;
    }


    public String getTypeCodeFieldName()
    {
        return this.typeCodeFieldName;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }
}
