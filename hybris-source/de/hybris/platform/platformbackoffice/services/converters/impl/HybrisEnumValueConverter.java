package de.hybris.platform.platformbackoffice.services.converters.impl;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.platformbackoffice.services.converters.SavedQueryValue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class HybrisEnumValueConverter extends AbstractValueConverter
{
    private static final Logger LOG = LoggerFactory.getLogger(HybrisItemTypeValueConverter.class);
    private static final int EXPECTED_AFTER_SPLIT_VALUES = 2;
    private static final String SPLIT_REGEX = "#";
    private static final String MULTI_VALUE_DELIMITER = "&";
    private EnumerationService enumerationService;


    public boolean canHandle(DataAttribute dataAttribute)
    {
        DataType valueType = dataAttribute.getValueType();
        return (valueType != null && DataType.Type.ENUM.equals(valueType.getType()) && HybrisEnumValue.class
                        .isAssignableFrom(valueType.getClazz()));
    }


    public Object convertValue(SavedQueryValue savedQueryValue, DataAttribute dataAttribute)
    {
        String value = savedQueryValue.getValue();
        if(StringUtils.isBlank(value))
        {
            return null;
        }
        Object convertedValue = null;
        if(value.endsWith("&"))
        {
            String[] values = value.split("&");
            convertedValue = Arrays.<String>stream(values).map(v -> convertSingleValue(v, dataAttribute)).collect(Collectors.toSet());
        }
        else
        {
            convertedValue = convertSingleValue(value, dataAttribute);
        }
        if(dataAttribute.isLocalized() && convertedValue != null)
        {
            Locale valueLocale = getLocaleFromCode(savedQueryValue.getLocaleCode());
            if(valueLocale != null)
            {
                return getLocalizedValue(valueLocale, convertedValue);
            }
        }
        return convertedValue;
    }


    public SavedQueryValue convertValue(Object value, DataAttribute dataAttribute)
    {
        if(value == null)
        {
            return null;
        }
        HybrisEnumValue enumValue = null;
        String metaData = null;
        if(dataAttribute.isLocalized())
        {
            Locale valueLocale = getValueLocale(value, dataAttribute);
            if(valueLocale != null)
            {
                Object locValue = ((Map)value).get(valueLocale);
                metaData = getLocaleCode(valueLocale);
                if(locValue instanceof HybrisEnumValue)
                {
                    enumValue = (HybrisEnumValue)locValue;
                }
                else if(locValue instanceof Collection)
                {
                    return convertMultiValues(locValue, metaData);
                }
            }
        }
        else if(value instanceof HybrisEnumValue)
        {
            enumValue = (HybrisEnumValue)value;
        }
        else if(value instanceof Collection)
        {
            return convertMultiValues(value, null);
        }
        if(enumValue != null)
        {
            String convertedValue = String.format("%s#%s", new Object[] {enumValue.getType(), enumValue.getCode()});
            return new SavedQueryValue(metaData, convertedValue);
        }
        return null;
    }


    private SavedQueryValue convertMultiValues(Object value, String localeCode)
    {
        if(value instanceof Collection)
        {
            String convertedValue = ((Collection)value).stream().filter(v -> v instanceof HybrisEnumValue).map(v -> String.format("%s#%s&", new Object[] {((HybrisEnumValue)v).getType(), ((HybrisEnumValue)v).getCode()})).collect(Collectors.joining());
            return convertedValue.isEmpty() ? null : new SavedQueryValue(localeCode, convertedValue);
        }
        return null;
    }


    private HybrisEnumValue convertSingleValue(String value, DataAttribute dataAttribute)
    {
        if(StringUtils.isBlank(value))
        {
            return null;
        }
        String[] typeAndValue = value.split("#");
        if(typeAndValue.length != 2)
        {
            return null;
        }
        try
        {
            return this.enumerationService.getEnumerationValue(typeAndValue[0], typeAndValue[1]);
        }
        catch(Exception ex)
        {
            LOG.warn(String.format("Cannot convert saved query value of %s.%s into HybrisEnumValue", new Object[] {dataAttribute
                            .getDefinedType().getCode(), dataAttribute.getQualifier()}), ex);
            return null;
        }
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }
}
