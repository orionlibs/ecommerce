package de.hybris.platform.platformbackoffice.services.converters.impl;

import com.google.common.collect.ImmutableMap;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import de.hybris.platform.platformbackoffice.services.converters.BackofficeSavedQueryValueConverter;
import de.hybris.platform.platformbackoffice.services.converters.SavedQueryValue;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractValueConverter implements BackofficeSavedQueryValueConverter
{
    private static final Function<String, Object> NULL_SUPPLIER = value -> null;
    private static final Map<DataType, Function<String, Object>> ATOMIC_TYPE_VALUE_CONVERTERS;

    static
    {
        ATOMIC_TYPE_VALUE_CONVERTERS = (Map<DataType, Function<String, Object>>)(new ImmutableMap.Builder()).put(DataType.BOOLEAN, Boolean::valueOf).put(DataType.STRING, StringUtils::defaultString).put(DataType.BYTE, Byte::valueOf).put(DataType.CHARACTER, value -> Character.valueOf(value.charAt(0)))
                        .put(DataType.SHORT, Short::valueOf).put(DataType.INTEGER, Integer::valueOf).put(DataType.LONG, Long::valueOf).put(DataType.FLOAT, Float::valueOf).put(DataType.DOUBLE, Double::valueOf).put(DataType.BIG_DECIMAL, value -> BigDecimal.valueOf(Long.parseLong(value)))
                        .put(DataType.DATE, value -> {
                            long time = Long.parseLong(value);
                            return (Function)new Date(time);
                        }).build();
    }

    protected Locale getSavedQueryValueLocale(SavedQueryValue savedQueryValue, DataAttribute dataAttribute)
    {
        String langTagMetaData = savedQueryValue.getLocaleCode();
        if(dataAttribute.isLocalized() && StringUtils.isNotBlank(langTagMetaData))
        {
            return getLocaleFromCode(langTagMetaData);
        }
        return null;
    }


    protected Locale getValueLocale(Object value, DataAttribute dataAttribute)
    {
        if(dataAttribute.isLocalized() && value instanceof Map)
        {
            Map<Locale, Object> locValue = (Map<Locale, Object>)value;
            Optional<Map.Entry<Locale, Object>> localized = locValue.entrySet().stream().findAny();
            if(localized.isPresent())
            {
                return (Locale)((Map.Entry)localized.get()).getKey();
            }
        }
        return null;
    }


    protected Map<Locale, Object> getLocalizedValue(Locale locale, Object value)
    {
        Map<Locale, Object> locVal = new HashMap<>();
        locVal.put(locale, value);
        return locVal;
    }


    protected String getLocaleCode(Locale locale)
    {
        return locale.toLanguageTag();
    }


    protected Locale getLocaleFromCode(String metadata)
    {
        return Locale.forLanguageTag(metadata);
    }


    protected Object convertSimpleAtomicTypeValue(String valueString, DataType dataType)
    {
        Function<String, Object> converter = ATOMIC_TYPE_VALUE_CONVERTERS.getOrDefault(dataType, NULL_SUPPLIER);
        return converter.apply(valueString);
    }


    protected String convertSimpleAtomicTypeValueToString(Object value, DataType dataType)
    {
        if(DataType.DATE.equals(dataType))
        {
            return (value instanceof Date) ? String.valueOf(((Date)value).getTime()) : null;
        }
        return String.valueOf(value);
    }


    protected boolean isSimpleAtomicDataType(DataType attributeType)
    {
        return ATOMIC_TYPE_VALUE_CONVERTERS.containsKey(attributeType);
    }
}
