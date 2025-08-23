package com.hybris.backoffice.solrsearch.core.search;

import com.hybris.backoffice.widgets.fulltextsearch.FullTextSearchStrategy;
import com.hybris.backoffice.widgets.fulltextsearch.SearchFilterValidationStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated(since = "2105", forRemoval = true)
public class SolrSearchFilterValidationStrategy implements SearchFilterValidationStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(SolrSearchFilterValidationStrategy.class);
    private FullTextSearchStrategy solrSearchStrategy;
    private TypeFacade typeFacade;


    public boolean isValid(String typeCode, String name, Object value, ValueComparisonOperator operator)
    {
        boolean validNoValueOperator = (!operator.isRequireValue() && (value == null || (value instanceof Map && ((Map)value).isEmpty() && isLocalizedProperty(typeCode, name))));
        return (validNoValueOperator || isValid(typeCode, name, value));
    }


    protected boolean isLocalizedProperty(String typeCode, String name)
    {
        try
        {
            return getTypeFacade().load(typeCode).getAttribute(name).isLocalized();
        }
        catch(TypeNotFoundException e)
        {
            LOG.warn(String.format("Could not find data type: '%s' with attribute '%s'", new Object[] {typeCode, name}), (Throwable)e);
            return false;
        }
    }


    public boolean isValid(String typeCode, String name, Object value)
    {
        Object filterValue;
        boolean isFilterLocalized = getSolrSearchStrategy().isLocalized(typeCode, name);
        boolean isValueLocalized = value instanceof Map;
        if((isFilterLocalized && !isValueLocalized) || (!isFilterLocalized && isValueLocalized))
        {
            return false;
        }
        if(isFilterLocalized && !((Map)value).isEmpty())
        {
            filterValue = ((Map)value).values().stream().filter(Objects::nonNull).findAny().orElse(null);
        }
        else
        {
            filterValue = value;
        }
        String fieldType = getSolrSearchStrategy().getFieldType(typeCode, name);
        return (StringUtils.isNotEmpty(fieldType) && (filterValue == null ||
                        Objects.equals(fieldType, getTypeFacade().getType(filterValue))));
    }


    public boolean canHandle(String searchStrategy)
    {
        return getSolrSearchStrategy().getStrategyName().equals(searchStrategy);
    }


    protected FullTextSearchStrategy getSolrSearchStrategy()
    {
        return this.solrSearchStrategy;
    }


    public void setSolrSearchStrategy(FullTextSearchStrategy solrSearchStrategy)
    {
        this.solrSearchStrategy = solrSearchStrategy;
    }


    protected TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }


    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
