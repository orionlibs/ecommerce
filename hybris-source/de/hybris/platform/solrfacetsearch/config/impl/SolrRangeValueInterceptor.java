package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.solrfacetsearch.config.ValueRangeType;
import de.hybris.platform.solrfacetsearch.config.ValueRanges;
import de.hybris.platform.solrfacetsearch.model.config.SolrValueRangeModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrValueRangeSetModel;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class SolrRangeValueInterceptor implements ValidateInterceptor
{
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof SolrValueRangeModel)
        {
            SolrValueRangeModel range = (SolrValueRangeModel)model;
            if(range.getSolrValueRangeSet() != null)
            {
                SolrValueRangeSetModel set = range.getSolrValueRangeSet();
                String type = set.getType();
                if(ValueRangeType.DOUBLE.toString().compareToIgnoreCase(type) == 0)
                {
                    parseDouble(range.getFrom(), false);
                    parseDouble(range.getTo(), true);
                }
                else if(ValueRangeType.FLOAT.toString().compareToIgnoreCase(type) == 0)
                {
                    parseFloat(range.getFrom(), false);
                    parseFloat(range.getTo(), true);
                }
                else if(ValueRangeType.INT.toString().compareToIgnoreCase(type) == 0)
                {
                    parseInt(range.getFrom(), false);
                    parseInt(range.getTo(), true);
                }
                else if(ValueRangeType.DATE.toString().compareToIgnoreCase(type) == 0)
                {
                    parseDate(range.getFrom());
                    parseDate(range.getTo());
                }
            }
        }
    }


    protected double parseDouble(String value, boolean allowEmpty) throws InterceptorException
    {
        try
        {
            if(StringUtils.isBlank(value) && allowEmpty)
            {
                return 0.0D;
            }
            return Double.valueOf(value).doubleValue();
        }
        catch(NumberFormatException e)
        {
            throw new InterceptorException(String.format("'%s' is not a proper double value ", new Object[] {value}), e);
        }
    }


    protected float parseFloat(String value, boolean allowEmpty) throws InterceptorException
    {
        try
        {
            if(StringUtils.isBlank(value) && allowEmpty)
            {
                return 0.0F;
            }
            return Float.valueOf(value).floatValue();
        }
        catch(NumberFormatException e)
        {
            throw new InterceptorException(String.format("'%s' is not a proper float value ", new Object[] {value}), e);
        }
    }


    protected int parseInt(String value, boolean allowEmpty) throws InterceptorException
    {
        try
        {
            if(StringUtils.isBlank(value) && allowEmpty)
            {
                return 0;
            }
            return Integer.valueOf(value).intValue();
        }
        catch(NumberFormatException e)
        {
            throw new InterceptorException(String.format("'%s' is not a proper integer value ", new Object[] {value}), e);
        }
    }


    protected Date parseDate(String value) throws InterceptorException
    {
        try
        {
            return ValueRanges.parseDate(value);
        }
        catch(ParseException e)
        {
            throw new InterceptorException(
                            String.format("'%s' is not a proper date value. Accepted format:  %s", new Object[] {value, "yyyy-MM-dd [HH:mm]"}), e);
        }
    }
}
