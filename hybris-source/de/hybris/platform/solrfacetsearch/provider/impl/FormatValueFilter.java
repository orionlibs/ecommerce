package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.provider.ValueFilter;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class FormatValueFilter implements ValueFilter, BeanFactoryAware
{
    public static final String FORMAT_PARAM = "format";
    public static final String FORMAT_PARAM_DEFAULT_VALUE = null;
    private BeanFactory beanFactory;


    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }


    protected Format getValueFormatter(String formatterBeanName)
    {
        return (Format)this.beanFactory.getBean(formatterBeanName, Format.class);
    }


    public Object doFilter(IndexerBatchContext batchContext, IndexedProperty indexedProperty, Object value)
    {
        if(value == null)
        {
            return null;
        }
        if(value instanceof Collection)
        {
            Collection<Object> values = (Collection<Object>)value;
            if(values.isEmpty())
            {
                return values;
            }
            List<Object> resultValues = new ArrayList();
            for(Object singleValue : values)
            {
                resultValues.add(formatValue(batchContext, indexedProperty, singleValue));
            }
            return resultValues;
        }
        return formatValue(batchContext, indexedProperty, value);
    }


    protected Object formatValue(IndexerBatchContext batchContext, IndexedProperty indexedProperty, Object value)
    {
        if(value != null)
        {
            String formatterBeanName = ValueProviderParameterUtils.getString(indexedProperty, "format", FORMAT_PARAM_DEFAULT_VALUE);
            if(StringUtils.isNotEmpty(formatterBeanName))
            {
                Format formatter = getValueFormatter(formatterBeanName);
                return formatter.format(value);
            }
        }
        return value;
    }
}
