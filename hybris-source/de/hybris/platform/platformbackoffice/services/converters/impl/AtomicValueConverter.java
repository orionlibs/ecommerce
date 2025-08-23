package de.hybris.platform.platformbackoffice.services.converters.impl;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.platformbackoffice.services.converters.SavedQueryValue;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtomicValueConverter extends AbstractValueConverter
{
    private static final Logger LOG = LoggerFactory.getLogger(AtomicValueConverter.class);


    public boolean canHandle(DataAttribute dataAttribute)
    {
        DataType valueType = dataAttribute.getValueType();
        return (valueType != null && DataType.Type.ATOMIC.equals(valueType.getType()) && isSimpleAtomicDataType(valueType) && dataAttribute
                        .getMapType() == null);
    }


    public Object convertValue(SavedQueryValue queryValue, DataAttribute dataAttribute)
    {
        if(StringUtils.isNotBlank(queryValue.getValue()) || queryValue.getValue() == null)
        {
            try
            {
                Object atomicValue = convertSimpleAtomicTypeValue(queryValue.getValue(), dataAttribute.getValueType());
                if(dataAttribute.isLocalized() && StringUtils.isNotBlank(queryValue.getLocaleCode()))
                {
                    Locale loc = getSavedQueryValueLocale(queryValue, dataAttribute);
                    return getLocValue(loc, atomicValue, dataAttribute);
                }
                if(queryValue.getValue() != null)
                {
                    return atomicValue;
                }
            }
            catch(NumberFormatException ne)
            {
                LOG.error(String.format("Cannot convert saved query value of %s.%s", new Object[] {dataAttribute.getDefinedType().getCode(), dataAttribute
                                .getQualifier()}), ne);
            }
        }
        return null;
    }


    public SavedQueryValue convertValue(Object value, DataAttribute dataAttribute)
    {
        if(value != null)
        {
            if(dataAttribute.isLocalized())
            {
                return getSavedQueryValue(value, dataAttribute);
            }
            String stringValue = convertSimpleAtomicTypeValueToString(value, dataAttribute.getValueType());
            if(StringUtils.isNotBlank(stringValue))
            {
                return new SavedQueryValue(stringValue);
            }
        }
        return null;
    }


    private Object getLocValue(Locale locale, Object atomicValue, DataAttribute dataAttribute)
    {
        if(locale != null)
        {
            try
            {
                return getLocalizedValue(locale, atomicValue);
            }
            catch(NumberFormatException ne)
            {
                LOG.error(String.format("Cannot convert saved query value of %s.%s", new Object[] {dataAttribute.getDefinedType().getCode(), dataAttribute
                                .getQualifier()}), ne);
            }
        }
        return null;
    }


    private SavedQueryValue getSavedQueryValue(Object value, DataAttribute dataAttribute)
    {
        Locale valueLocale = getValueLocale(value, dataAttribute);
        if(valueLocale != null)
        {
            Object queryValue = ((Map)value).get(valueLocale);
            if(queryValue == null)
            {
                return new SavedQueryValue(getLocaleCode(valueLocale), (ItemModel)queryValue);
            }
            String localizedValue = convertSimpleAtomicTypeValueToString(queryValue, dataAttribute.getValueType());
            if(StringUtils.isNotBlank(localizedValue))
            {
                return new SavedQueryValue(getLocaleCode(valueLocale), localizedValue);
            }
        }
        return null;
    }
}
