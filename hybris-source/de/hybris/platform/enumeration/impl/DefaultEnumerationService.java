package de.hybris.platform.enumeration.impl;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultEnumerationService extends AbstractService implements EnumerationService
{
    private TypeService typeService;
    private ModelService modelService;
    private static final Logger LOG = Logger.getLogger(DefaultEnumerationService.class);


    public <T extends HybrisEnumValue> List<T> getEnumerationValues(String enumerationCode)
    {
        EnumerationMetaTypeModel enumMetaTypeModel = this.typeService.getEnumerationTypeForCode(enumerationCode);
        Collection<ItemModel> valueModels = enumMetaTypeModel.getValues();
        List<T> enumValues = new ArrayList<>(valueModels.size());
        for(ItemModel evm : valueModels)
        {
            enumValues.add((T)this.modelService.get(evm.getPk()));
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("" + enumValues.size() + " Enumerations found for type code" + enumValues.size());
        }
        return enumValues;
    }


    public <T extends HybrisEnumValue> List<T> getEnumerationValues(Class<T> enumClass)
    {
        if(enumClass.isEnum())
        {
            return Arrays.asList(enumClass.getEnumConstants());
        }
        return getEnumerationValues(enumClass.getSimpleName());
    }


    public <T extends HybrisEnumValue> T getEnumerationValue(String enumerationCode, String valueCode)
    {
        T result = null;
        EnumerationValueModel evm = this.typeService.getEnumerationValue(enumerationCode, valueCode);
        HybrisEnumValue hybrisEnumValue = (HybrisEnumValue)this.modelService.get(evm.getPk());
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Matching Enumeration has been found for typeCode=" + enumerationCode + " and valueCode=" + valueCode);
        }
        return (T)hybrisEnumValue;
    }


    public <T extends HybrisEnumValue> T getEnumerationValue(Class<T> enumClass, String valueCode)
    {
        if(enumClass.isEnum())
        {
            HybrisEnumValue[] arrayOfHybrisEnumValue = (HybrisEnumValue[])enumClass.getEnumConstants();
            for(HybrisEnumValue hybrisEnumValue : arrayOfHybrisEnumValue)
            {
                if(hybrisEnumValue.getCode().equals(valueCode))
                {
                    return (T)hybrisEnumValue;
                }
            }
        }
        else
        {
            return getEnumerationValue(enumClass.getSimpleName(), valueCode);
        }
        return null;
    }


    public String getEnumerationName(HybrisEnumValue enumValue)
    {
        EnumerationValueModel evm = this.typeService.getEnumerationValue(enumValue);
        return evm.getName();
    }


    public void setEnumerationName(HybrisEnumValue enumValue, String name)
    {
        EnumerationValueModel evm = this.typeService.getEnumerationValue(enumValue);
        evm.setName(name);
        this.modelService.save(evm);
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public String getEnumerationName(HybrisEnumValue enumValue, Locale locale)
    {
        EnumerationValueModel evm = this.typeService.getEnumerationValue(enumValue);
        return evm.getName(locale);
    }


    public void setEnumerationName(HybrisEnumValue enumValue, String name, Locale locale)
    {
        EnumerationValueModel evm = this.typeService.getEnumerationValue(enumValue);
        evm.setName(name, locale);
        this.modelService.save(evm);
    }
}
