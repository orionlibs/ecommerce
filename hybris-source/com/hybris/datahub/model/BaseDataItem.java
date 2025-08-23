package com.hybris.datahub.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.hybris.datahub.runtime.domain.DataHubPool;
import com.hybris.datahub.util.AttributelessCloneable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseDataItem implements Cloneable, AttributelessCloneable
{
    protected static Boolean maskingMode = Boolean.FALSE;
    protected static String maskingValue;
    private final String type;
    protected Map<String, Object> fields = new ConcurrentHashMap<>();
    private Long id;
    private DataHubPool dataPool;


    public BaseDataItem(String type)
    {
        this.type = type;
    }


    public static void configureMasking(Boolean maskingMode, String maskingValue)
    {
        BaseDataItem.maskingMode = maskingMode;
        BaseDataItem.maskingValue = maskingValue;
    }


    public List<DataItemAttribute> getAttributeDefinitions()
    {
        return new ArrayList<>(getTypeAttributes().getAll());
    }


    public void setField(String fieldName, Object value)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(fieldName), "The field name cannot be blank");
        Optional<DataItemAttribute> dataItemAttribute = getAttributeDefinition(fieldName);
        if(!dataItemAttribute.isPresent())
        {
            throw new InvalidAttributeException("Field: '" + fieldName + "' is not defined for type: '" + getType() + "' and cannot be set");
        }
        validateAttributeValueType((DataItemAttribute)dataItemAttribute.get(), value);
        this.fields.put(fieldName, Optional.ofNullable(value));
    }


    public Object getField(String fieldName)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(fieldName), "The field name cannot be blank");
        DataItemAttribute attr = (DataItemAttribute)getAttributeDefinition(fieldName).orNull();
        if(attr == null)
        {
            throw new InvalidAttributeException("Field : " + fieldName + " is not defined for type : " + getType() + " and cannot be retrieved");
        }
        Object fieldObj = this.fields.get(fieldName);
        if(fieldObj instanceof Optional)
        {
            Optional<Map<Object, Object>> optionalField = (Optional)fieldObj;
            if(!attr.isLocalizable())
            {
                return optionalField.orElse(null);
            }
            if(optionalField.isPresent())
            {
                Map<Object, Object> f = new HashMap<>();
                Map<Object, Object> fieldLocalizedMap = optionalField.get();
                fieldLocalizedMap.forEach((key, value) -> f.put(key, (fieldLocalizedMap.get(key) instanceof Optional) ? ((Optional)fieldLocalizedMap.get(key)).orElse(null) : fieldLocalizedMap.get(key)));
                if(!f.isEmpty())
                {
                    return f;
                }
            }
            return null;
        }
        return fieldObj;
    }


    public String getType()
    {
        return this.type;
    }


    public Map<String, Object> getFields()
    {
        getTypeAttributes().getAll().stream()
                        .filter(attr -> !this.fields.containsKey(attr.getPropertyName()))
                        .forEach(attr -> this.fields.put(attr.getPropertyName(), Optional.ofNullable(null)));
        Map<String, Object> f = new HashMap<>();
        this.fields.forEach((key, value) -> f.put(key, getField(key)));
        return Collections.unmodifiableMap(f);
    }


    public void setFields(Map<String, Object> fields)
    {
        for(Map.Entry<String, Object> field : fields.entrySet())
        {
            Object val = (field.getValue() instanceof Optional) ? ((Optional)field.getValue()).orElse(null) : field.getValue();
            setField(field.getKey(), val);
        }
    }


    public Map<String, ?> getFieldsSecurely()
    {
        Map<String, Object> secured = new HashMap<>();
        for(Map.Entry<String, Object> entry : getFields().entrySet())
        {
            String attrName = entry.getKey();
            DataItemAttribute attr = (DataItemAttribute)getAttributeDefinition(attrName).orNull();
            if(attr != null)
            {
                Object val;
                if(entry.getValue() == null)
                {
                    val = null;
                }
                else if(entry.getValue() instanceof Optional)
                {
                    val = ((Optional)entry.getValue()).orElse(null);
                }
                else
                {
                    val = entry.getValue();
                }
                secured.put(attrName, (attr.isSecured() && isMaskingActive()) ? maskingValue : val);
            }
        }
        return secured;
    }


    public DataHubPool getDataPool()
    {
        return this.dataPool;
    }


    public void setDataPool(DataHubPool dataPool)
    {
        if(this.dataPool != null && !this.dataPool.equals(dataPool))
        {
            throw new IllegalArgumentException("BaseDataItem pool cannot be changed after it is defined");
        }
        this.dataPool = dataPool;
    }


    public Long getId()
    {
        return this.id;
    }


    public void setId(Long id)
    {
        if(this.id != null && !this.id.equals(id))
        {
            throw new IllegalArgumentException("BaseDataItem id cannot be changed after it is defined");
        }
        this.id = id;
    }


    protected void overrideId(Long id)
    {
        this.id = id;
    }


    private static void validateAttributeValueType(DataItemAttribute attributeData, Object objectValue)
    {
        if(objectValue != null)
        {
            Object value = (objectValue instanceof Optional) ? ((Optional)objectValue).orElse(null) : objectValue;
            String simpleName = (value == null) ? null : value.getClass().getSimpleName();
            if(attributeData.isCollection())
            {
                Preconditions.checkArgument(value instanceof java.util.Collection, "Attribute %s.%s cannot set a value of type %s : value must be a Collection type", attributeData
                                .getItemType(), attributeData.getPropertyName(), simpleName);
            }
            else if(attributeData.isLocalizable())
            {
                Preconditions.checkArgument(value instanceof Map, "Attribute %s.%s cannot set a value of type %s : value must be a Map type", attributeData
                                .getItemType(), attributeData.getPropertyName(), simpleName);
            }
            else
            {
                Preconditions.checkArgument((!(value instanceof Map) && !(value instanceof java.util.Collection)), "Attribute %s.%s cannot set a value of type %s", attributeData
                                .getItemType(), attributeData
                                .getPropertyName(), simpleName);
            }
        }
    }


    public Object cloneWithoutAttributes()
    {
        CanonicalItem canonicalItem;
        if(RawItem.class.isAssignableFrom(getClass()))
        {
            RawItem rawItem = RawItem.instantiate(getType());
        }
        else if(CanonicalItem.class.isAssignableFrom(getClass()))
        {
            canonicalItem = CanonicalItem.instantiate(getType());
        }
        else
        {
            throw new IllegalArgumentException("BaseDataItem clone should not be called with " + getClass().getSimpleName());
        }
        canonicalItem.setId(getId());
        return canonicalItem;
    }


    protected Object clone() throws CloneNotSupportedException
    {
        BaseDataItem item = (BaseDataItem)cloneWithoutAttributes();
        item.setFields(Maps.newHashMap(getFields()));
        return item;
    }


    public String toString()
    {
        return "BaseDataItem{id=" + this.id + ", dataPool=" + (
                        (this.dataPool != null) ? this.dataPool.getPoolName() : null) + ", fields=" +
                        printFields() + "}";
    }


    protected String printFields()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        Iterator<Map.Entry<String, Object>> iter = getFields().entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry<String, Object> entry = iter.next();
            DataItemAttribute attr = (DataItemAttribute)getAttributeDefinition(entry.getKey()).get();
            builder.append(entry.getKey());
            builder.append("=");
            builder.append((attr.isSecured() && isMaskingActive()) ? maskingValue : entry.getValue());
            if(iter.hasNext())
            {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }


    private static boolean isMaskingActive()
    {
        return Boolean.TRUE.equals(maskingMode);
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        BaseDataItem that = (BaseDataItem)o;
        if((this.dataPool != null) ? !this.dataPool.equals(that.dataPool) : (that.dataPool != null))
        {
            return false;
        }
        if((this.fields != null) ? !this.fields.equals(that.fields) : (that.fields != null))
        {
            return false;
        }
        return (this.id == null) ? ((that.id == null)) : this.id.equals(that.id);
    }


    public int hashCode()
    {
        return (this.id != null) ? this.id.hashCode() : 0;
    }


    public abstract Optional<DataItemAttribute> getAttributeDefinition(String paramString);


    public abstract TypeAttributeDefinitions getTypeAttributes();
}
