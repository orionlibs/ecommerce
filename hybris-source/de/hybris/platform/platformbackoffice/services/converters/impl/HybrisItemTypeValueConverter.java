package de.hybris.platform.platformbackoffice.services.converters.impl;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.platformbackoffice.services.converters.SavedQueryValue;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class HybrisItemTypeValueConverter extends AbstractValueConverter
{
    private static final Logger LOG = LoggerFactory.getLogger(HybrisItemTypeValueConverter.class);
    private ModelService modelService;


    public boolean canHandle(DataAttribute dataAttribute)
    {
        DataType valueType = dataAttribute.getValueType();
        return (valueType != null && DataType.Type.COMPOUND.equals(valueType.getType()) && ItemModel.class
                        .isAssignableFrom(valueType.getClazz()));
    }


    public Object convertValue(SavedQueryValue savedQueryValue, DataAttribute dataAttribute)
    {
        if(savedQueryValue.getValueRef() == null || this.modelService.isRemoved(savedQueryValue.getValueRef()))
        {
            return null;
        }
        try
        {
            Object value = savedQueryValue.getValueRef();
            if(dataAttribute.isLocalized())
            {
                Locale valueLocale = getSavedQueryValueLocale(savedQueryValue, dataAttribute);
                if(valueLocale != null)
                {
                    return getLocalizedValue(valueLocale, value);
                }
            }
            return value;
        }
        catch(Exception ex)
        {
            LOG.warn(String.format("Cannot convert saved query value of %s.%s", new Object[] {dataAttribute.getDefinedType().getCode(), dataAttribute
                            .getQualifier()}), ex);
            return null;
        }
    }


    public SavedQueryValue convertValue(Object value, DataAttribute dataAttribute)
    {
        if(value == null)
        {
            return null;
        }
        if(dataAttribute.isLocalized())
        {
            Locale valueLocale = getValueLocale(value, dataAttribute);
            if(valueLocale != null)
            {
                Object locValue = ((Map)value).get(valueLocale);
                if(locValue instanceof ItemModel)
                {
                    return new SavedQueryValue(getLocaleCode(valueLocale), (ItemModel)locValue);
                }
            }
        }
        else if(value instanceof ItemModel)
        {
            return new SavedQueryValue((ItemModel)value);
        }
        return null;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
