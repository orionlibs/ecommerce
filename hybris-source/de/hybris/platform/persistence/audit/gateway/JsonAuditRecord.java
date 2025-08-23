package de.hybris.platform.persistence.audit.gateway;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.internal.AuditRecordUtil;
import de.hybris.platform.persistence.audit.payload.converter.PayloadConverterRegistry;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import de.hybris.platform.persistence.audit.payload.json.LocalizedTypedValue;
import de.hybris.platform.persistence.audit.payload.json.LocalizedValue;
import de.hybris.platform.persistence.audit.payload.json.MapBasedTypedValue;
import de.hybris.platform.persistence.audit.payload.json.TypedValue;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.ref.SoftReference;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class JsonAuditRecord implements AuditRecord
{
    private final Long id;
    private final PK pk;
    private final String type;
    private final PK typePk;
    private final String changingUser;
    private final AuditType auditType;
    private final Date timestamp;
    private final Date currentTimestamp;
    private SoftReference<PayloadContent> payloadContent;
    private final BiFunction<String, Long, PayloadContent> payloadContentProvider;
    private final PayloadConverterRegistry payloadConverterRegistry;


    private JsonAuditRecord(Builder builder)
    {
        this.id = builder.id;
        this.pk = builder.pk;
        this.type = builder.type;
        this.typePk = builder.typePk;
        this.changingUser = builder.changingUser;
        this.auditType = builder.auditType;
        this.timestamp = builder.timestamp;
        this.currentTimestamp = builder.currentTimestamp;
        this.payloadConverterRegistry = getPayloadConverterRegistry();
        this.payloadContent = new SoftReference<>(builder.payloadContent);
        this.payloadContentProvider = builder.payloadContentProvider;
    }


    PayloadConverterRegistry getPayloadConverterRegistry()
    {
        return (PayloadConverterRegistry)Registry.getApplicationContext().getBean("payloadConverterRegistry", PayloadConverterRegistry.class);
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public Long getVersion()
    {
        return this.id;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public String getType()
    {
        return this.type;
    }


    public PK getTypePk()
    {
        return this.typePk;
    }


    public String getChangingUser()
    {
        return this.changingUser;
    }


    public AuditType getAuditType()
    {
        return this.auditType;
    }


    public AuditPayload getAuditPayload()
    {
        return getOrLoadPayloadContent().getPayloadBefore();
    }


    public AuditPayload getAuditPayloadAfterOperation()
    {
        return getOrLoadPayloadContent().getPayloadAfter();
    }


    public Object getAttributeBeforeOperation(String key)
    {
        return getAttribute(key, getOrLoadPayloadContent().getPayloadBefore());
    }


    public Object getAttributeAfterOperation(String key)
    {
        return getAttribute(key, getOrLoadPayloadContent().getPayloadAfter());
    }


    public Object getAttributeBeforeOperation(String key, String langIsoCode)
    {
        return getAttribute(key, getOrLoadPayloadContent().getPayloadBefore(), langIsoCode);
    }


    public Object getAttributeAfterOperation(String key, String langIsoCode)
    {
        return getAttribute(key, getOrLoadPayloadContent().getPayloadAfter(), langIsoCode);
    }


    public Map<String, Object> getContext()
    {
        return getOrLoadPayloadContent().getContext();
    }


    private Object getAttribute(String key, AuditPayload payload, String langIsoCode)
    {
        List<String> localizedAttributes = payload.getAttribute(key, langIsoCode);
        if(localizedAttributes == null)
        {
            return null;
        }
        if(payload.isCollection(key))
        {
            return convertCollection(localizedAttributes, payload.getAttributeType(key), payload
                            .getCollectionType(key).intValue());
        }
        LocalizedTypedValue localizedValues = payload.getLocalizedAttribute(key);
        if(localizedValues != null)
        {
            return convertLocalizedTypeValueToMap(localizedValues, payload.getAttributeType(key), langIsoCode);
        }
        return null;
    }


    private Object getAttribute(String key, AuditPayload payload)
    {
        if(payload.isCollection(key))
        {
            return convertCollection(payload.getCollection(key), payload.getAttributeType(key), payload
                            .getCollectionType(key).intValue());
        }
        String attrValue = payload.getAttribute(key);
        Class attributeType = payload.getAttributeType(key);
        if(attrValue == null)
        {
            LocalizedTypedValue localizedValues = payload.getLocalizedAttribute(key);
            if(localizedValues != null)
            {
                return convertLocalizedTypeValueToMap(localizedValues, attributeType, null);
            }
            MapBasedTypedValue mapBasedAttribute = payload.getMapBasedAttribute(key);
            if(mapBasedAttribute != null)
            {
                return deserializeFromString(mapBasedAttribute.getValue());
            }
        }
        return this.payloadConverterRegistry.convertToObject(attrValue, attributeType);
    }


    private static Object deserializeFromString(String value)
    {
        ObjectInputStream ois = null;
        try
        {
            byte[] data = Base64.getDecoder().decode(value);
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            return ois.readObject();
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e);
        }
        finally
        {
            IOUtils.closeQuietly(ois);
        }
    }


    private Map<String, Object> convertLocalizedTypeValueToMap(LocalizedTypedValue localizedValues, Class attributeType, String langIsoCode)
    {
        if(localizedValues == null)
        {
            return null;
        }
        Map<String, Object> localizedValuesMap = new HashMap<>();
        for(LocalizedValue attrValue : localizedValues.getValues())
        {
            List<Object> val = (List<Object>)attrValue.getValue().stream().map(v -> this.payloadConverterRegistry.convertToObject(v, attributeType)).collect(Collectors.toList());
            if(langIsoCode == null || langIsoCode.equalsIgnoreCase(attrValue.getLanguage()))
            {
                if(val.size() == 1)
                {
                    localizedValuesMap.put(attrValue.getLanguage(), val.get(0));
                    continue;
                }
                localizedValuesMap.put(attrValue.getLanguage(), val);
            }
        }
        return localizedValuesMap;
    }


    private Object convertCollection(List<String> collection, Class attributeType, int collectionType)
    {
        Collection<Object> convertedObjects = AuditRecordUtil.buildCollectionForType(collectionType);
        for(String obj : collection)
        {
            convertedObjects.add(this.payloadConverterRegistry.convertToObject(obj, attributeType));
        }
        return convertedObjects;
    }


    public Date getTimestamp()
    {
        return this.timestamp;
    }


    public Date getCurrentTimestamp()
    {
        return this.currentTimestamp;
    }


    public Map<String, Object> getAttributesBeforeOperation()
    {
        return buildAttributesMap(getOrLoadPayloadContent().getPayloadBefore());
    }


    public Map<String, Object> getAttributesAfterOperation()
    {
        return buildAttributesMap(getOrLoadPayloadContent().getPayloadAfter());
    }


    public Map<String, Object> getAttributesBeforeOperation(String langIsoCode)
    {
        return buildAttributesMap(getOrLoadPayloadContent().getPayloadBefore(), langIsoCode);
    }


    public Map<String, Object> getAttributesAfterOperation(String langIsoCode)
    {
        return buildAttributesMap(getOrLoadPayloadContent().getPayloadAfter(), langIsoCode);
    }


    private PayloadContent getOrLoadPayloadContent()
    {
        if(this.payloadContent == null || this.payloadContent.get() == null)
        {
            this.payloadContent = new SoftReference<>(this.payloadContentProvider.apply(this.type, this.id));
        }
        return this.payloadContent.get();
    }


    private Map<String, Object> buildAttributesMap(AuditPayload payload)
    {
        Map<String, Object> attributes = new ConcurrentHashMap<>();
        for(Map.Entry<String, TypedValue> entry : (Iterable<Map.Entry<String, TypedValue>>)payload.getAttributes().entrySet())
        {
            String key = entry.getKey();
            String attribute = payload.getAttribute(key);
            Class attributeType = payload.getAttributeType(key);
            attributes.put(key, this.payloadConverterRegistry.convertToObject(attribute, attributeType));
        }
        return attributes;
    }


    private Map<String, Object> buildAttributesMap(AuditPayload payload, String langIsoCode)
    {
        Map<String, Object> attributes = new ConcurrentHashMap<>();
        for(Map.Entry<String, LocalizedTypedValue> entry : (Iterable<Map.Entry<String, LocalizedTypedValue>>)payload.getLocAttributes().entrySet())
        {
            String key = entry.getKey();
            List<String> languageValues = payload.getAttribute(key, langIsoCode);
            if(CollectionUtils.isNotEmpty(languageValues))
            {
                Class attributeType = payload.getAttributeType(key);
                attributes.put(key,
                                ImmutableMap.of(langIsoCode, this.payloadConverterRegistry
                                                .convertToObject(languageValues.get(0), attributeType)));
            }
        }
        return attributes;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof JsonAuditRecord))
        {
            return false;
        }
        JsonAuditRecord that = (JsonAuditRecord)o;
        return (Objects.equals(this.id, that.id) && Objects.equals(getPk(), that.getPk()) && Objects.equals(getType(), that.getType()) &&
                        Objects.equals(getTypePk(), that.getTypePk()) && Objects.equals(getChangingUser(), that.getChangingUser()) &&
                        getAuditType() == that.getAuditType() && Objects.equals(getTimestamp(), that.getTimestamp()) &&
                        Objects.equals(getCurrentTimestamp(), that.getCurrentTimestamp()));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.id, getPk(), getType(), getTypePk(), getChangingUser(), getAuditType(), getTimestamp(),
                        getCurrentTimestamp()});
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("id", this.id)
                        .append("pk", this.pk)
                        .append("type", this.type)
                        .append("typePk", this.typePk)
                        .append("changingUser", this.changingUser)
                        .append("auditType", this.auditType)
                        .append("timestamp", this.timestamp)
                        .append("currentTimestamp", this.currentTimestamp)
                        .toString();
    }
}
