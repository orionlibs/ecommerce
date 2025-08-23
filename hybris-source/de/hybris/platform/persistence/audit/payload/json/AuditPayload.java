package de.hybris.platform.persistence.audit.payload.json;

import de.hybris.platform.persistence.audit.internal.AuditPayloadClassCache;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class AuditPayload
{
    private final Map<String, MapBasedTypedValue> mapBasedAttributes;
    private final Map<String, TypedValue> attributes;
    private final Map<String, LocalizedTypedValue> locAttributes;


    private AuditPayload()
    {
        this.mapBasedAttributes = Collections.emptyMap();
        this.attributes = Collections.emptyMap();
        this.locAttributes = Collections.emptyMap();
    }


    private AuditPayload(Builder builder)
    {
        this.mapBasedAttributes = builder.mapBasedAttributes;
        this.attributes = builder.attributes;
        this.locAttributes = builder.locAttributes;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public String getAttribute(String key)
    {
        if(!this.attributes.containsKey(key) || ((TypedValue)this.attributes.get(key)).getValue().size() != 1)
        {
            return null;
        }
        return ((TypedValue)this.attributes.get(key)).getValue().get(0);
    }


    public LocalizedTypedValue getLocalizedAttribute(String key)
    {
        return this.locAttributes.get(key);
    }


    public MapBasedTypedValue getMapBasedAttribute(String key)
    {
        return this.mapBasedAttributes.get(key);
    }


    public List<String> getCollection(String key)
    {
        return this.attributes.containsKey(key) ? ((TypedValue)this.attributes.get(key)).getValue() : null;
    }


    public List<String> getAttribute(String key, String lang)
    {
        if(!this.locAttributes.containsKey(key))
        {
            return null;
        }
        LocalizedTypedValue localizedTypedValue = this.locAttributes.get(key);
        for(LocalizedValue value : localizedTypedValue.getValues())
        {
            if(value.getLanguage().equals(lang))
            {
                return value.getValue();
            }
        }
        return null;
    }


    public Class getAttributeType(String key)
    {
        try
        {
            if(this.attributes.containsKey(key))
            {
                return AuditPayloadClassCache.get(((TypedValue)this.attributes.get(key)).getType().getType());
            }
            if(this.locAttributes.containsKey(key))
            {
                return AuditPayloadClassCache.get(((LocalizedTypedValue)this.locAttributes.get(key)).getType().getType());
            }
            return null;
        }
        catch(Exception e)
        {
            throw new SystemException(e);
        }
    }


    public boolean isCollection(String key)
    {
        if(this.attributes.containsKey(key))
        {
            TypedValue typedValue = this.attributes.get(key);
            return StringUtils.isNotBlank(typedValue.getType().getCollection());
        }
        if(this.locAttributes.containsKey(key))
        {
            LocalizedTypedValue localizedTypedValue = this.locAttributes.get(key);
            return StringUtils.isNotBlank(localizedTypedValue.getType().getCollection());
        }
        return false;
    }


    public Integer getCollectionType(String key)
    {
        if(this.attributes.containsKey(key))
        {
            TypedValue typedValue = this.attributes.get(key);
            return Integer.valueOf(typedValue.getType().getCollection());
        }
        if(this.locAttributes.containsKey(key))
        {
            LocalizedTypedValue localizedTypedValue = this.locAttributes.get(key);
            return Integer.valueOf(localizedTypedValue.getType().getCollection());
        }
        return null;
    }


    public Map<String, TypedValue> getAttributes()
    {
        return this.attributes;
    }


    public Map<String, LocalizedTypedValue> getLocAttributes()
    {
        return this.locAttributes;
    }


    public Map<String, MapBasedTypedValue> getMapBasedAttributes()
    {
        return this.mapBasedAttributes;
    }
}
