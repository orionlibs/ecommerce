package com.hybris.datahub.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.hybris.datahub.service.DataItemService;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public abstract class LocalizedBaseDataItem extends BaseDataItem implements Cloneable
{
    protected static DataItemService dataItemService;


    protected LocalizedBaseDataItem(String type)
    {
        super(type);
    }


    public static void setDataItemService(DataItemService dataItemService)
    {
        LocalizedBaseDataItem.dataItemService = dataItemService;
    }


    public Object getField(String fieldName, Locale language)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(fieldName), "The field name cannot be blank");
        Preconditions.checkArgument((language != null), "The language cannot be null");
        Optional<DataItemAttribute> dataItemAttribute = getAttributeDefinition(fieldName);
        if(!dataItemAttribute.isPresent())
        {
            throw new InvalidAttributeException("Field : " + fieldName + " is not defined for type : " +
                            getType() + " and cannot be retrieved");
        }
        if(!((DataItemAttribute)dataItemAttribute.get()).isLocalizable())
        {
            throw new InvalidAttributeException("This attribute is not localizable.");
        }
        this.fields.computeIfAbsent(fieldName, k -> new HashMap<>());
        Object fieldObj = this.fields.get(fieldName);
        if(fieldObj instanceof Optional)
        {
            fieldObj = ((Optional)fieldObj).orElse(null);
        }
        if(fieldObj == null)
        {
            return null;
        }
        Object fieldLocalizedValue = ((Map)fieldObj).get(language);
        if(fieldLocalizedValue instanceof Optional)
        {
            return ((Optional)fieldLocalizedValue).orElse(null);
        }
        return fieldLocalizedValue;
    }


    public void setField(String fieldName, Object value, Locale language)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(fieldName), "The field name cannot be blank");
        Preconditions.checkArgument((language != null), "The language cannot be null");
        Preconditions.checkArgument((!(value instanceof Map) && !(value instanceof Collection)), "Attribute value cannot be a " + Collection.class
                        .getCanonicalName() + " or a " + Map.class.getCanonicalName() + " type");
        Optional<DataItemAttribute> dataItemAttribute = getAttributeDefinition(fieldName);
        if(!dataItemAttribute.isPresent())
        {
            throw new InvalidAttributeException("Field : " + fieldName + " is not defined for type : " +
                            getType() + " and cannot be set");
        }
        if(!((DataItemAttribute)dataItemAttribute.get()).isLocalizable())
        {
            throw new InvalidAttributeException("Field : " + fieldName + " is not localizable for type : " + getType());
        }
        if(!(this.fields.get(fieldName) instanceof Map))
        {
            this.fields.put(fieldName, new HashMap<>());
        }
        ((Map)this.fields.get(fieldName)).put(language, Optional.ofNullable(value));
    }
}
