package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSet;
import de.hybris.platform.solrfacetsearch.config.ValueRangeType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.config.exceptions.PropertyOutOfRangeException;
import de.hybris.platform.solrfacetsearch.provider.RangeNameProvider;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class DefaultRangeNameProvider implements RangeNameProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRangeNameProvider.class);


    public boolean isRanged(IndexedProperty property)
    {
        return !CollectionUtils.isEmpty(property.getValueRangeSets());
    }


    public List<ValueRange> getValueRanges(IndexedProperty property, String qualifier)
    {
        ValueRangeSet valueRangeSet;
        if(qualifier == null)
        {
            valueRangeSet = (ValueRangeSet)property.getValueRangeSets().get("default");
        }
        else
        {
            valueRangeSet = (ValueRangeSet)property.getValueRangeSets().get(qualifier);
            if(valueRangeSet == null)
            {
                valueRangeSet = (ValueRangeSet)property.getValueRangeSets().get("default");
            }
        }
        if(valueRangeSet != null)
        {
            return valueRangeSet.getValueRanges();
        }
        return Collections.emptyList();
    }


    public List<String> getRangeNameList(IndexedProperty property, Object value) throws FieldValueProviderException
    {
        return getRangeNameList(property, value, null);
    }


    public List<String> getRangeNameList(IndexedProperty property, Object value, String qualifier) throws FieldValueProviderException
    {
        int SINGLE_VALUE_SIZE = 1;
        List<String> rangeNameList = new ArrayList<>();
        if(!isRanged(property) || value == null)
        {
            return rangeNameList;
        }
        try
        {
            for(ValueRange range : getValueRanges(property, qualifier))
            {
                addValueRange(rangeNameList, property, value, range);
            }
            if(rangeNameList.isEmpty())
            {
                throw new PropertyOutOfRangeException("No range found for property: [" + property.getName() + "] with value [" + value + "] and qualifier [" + qualifier + "]");
            }
            if(rangeNameList.size() > 1 && !property.isMultiValue())
            {
                String message = "There was found multiple ranges " + rangeNameList + " for not multiple property [" + property.getName() + "] with value [" + value + "]. Only first range was returned";
                rangeNameList = rangeNameList.subList(0, 1);
                LOG.warn(message);
            }
            return rangeNameList;
        }
        catch(NumberFormatException | ClassCastException e)
        {
            throw new FieldValueProviderException("Cannot get range for property [" + property
                            .getName() + "] with value [" + value + "]", e);
        }
    }


    protected void addValueRange(List<String> rangeNameList, IndexedProperty property, Object value, ValueRange range)
    {
        Comparable<Object> cValue;
        Object from, to;
        Comparable cFrom = range.getFrom();
        Comparable cTo = range.getTo();
        if(isStringOrTextType(property))
        {
            cValue = ((String)value).toLowerCase(Locale.ROOT).substring(0, ((String)cFrom).length());
            from = cFrom.toString();
            to = valueOrDefault(cTo, String::toString, () -> null);
        }
        else if(isDoubleType(property))
        {
            cValue = BigDecimal.valueOf(((Double)value).doubleValue());
            from = new BigDecimal(cFrom.toString());
            to = valueOrDefault(cTo, BigDecimal::new, () -> BigDecimal.valueOf(Double.MAX_VALUE));
        }
        else if(isFloatType(property))
        {
            cValue = BigDecimal.valueOf(((Float)value).floatValue());
            from = new BigDecimal(cFrom.toString());
            to = valueOrDefault(cTo, BigDecimal::new, () -> BigDecimal.valueOf(3.4028234663852886E38D));
        }
        else if(isIntegerType(property))
        {
            cValue = (Integer)value;
            from = Integer.valueOf(cFrom.toString());
            to = valueOrDefault(cTo, Integer::new, () -> Integer.valueOf(2147483647));
        }
        else
        {
            cValue = (Comparable)value;
            from = cFrom;
            to = cTo;
        }
        if(to != null && cValue.compareTo(from) >= 0 && cValue.compareTo(to) <= 0)
        {
            rangeNameList.add(range.getName());
        }
    }


    protected boolean isStringOrTextType(IndexedProperty property)
    {
        return (ValueRangeType.STRING.toString().equalsIgnoreCase(property.getType()) || ValueRangeType.TEXT
                        .toString().equalsIgnoreCase(property.getType()));
    }


    protected boolean isDoubleType(IndexedProperty property)
    {
        return ValueRangeType.DOUBLE.toString().equalsIgnoreCase(property.getType());
    }


    protected boolean isFloatType(IndexedProperty property)
    {
        return ValueRangeType.FLOAT.toString().equalsIgnoreCase(property.getType());
    }


    protected boolean isIntegerType(IndexedProperty property)
    {
        return ValueRangeType.INT.toString().equalsIgnoreCase(property.getType());
    }


    protected Comparable valueOrDefault(Object value, Function<String, Comparable> valueFunction, Supplier<Comparable> defaultSupplier)
    {
        if(value == null)
        {
            return defaultSupplier.get();
        }
        return valueFunction.apply(value.toString());
    }
}
