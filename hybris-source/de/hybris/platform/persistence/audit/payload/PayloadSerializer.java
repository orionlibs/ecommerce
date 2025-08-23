package de.hybris.platform.persistence.audit.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.persistence.audit.internal.LocalizedAttributesList;
import de.hybris.platform.persistence.audit.payload.converter.PayloadConverterRegistry;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import de.hybris.platform.persistence.audit.payload.json.ValueType;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class PayloadSerializer
{
    private static final Logger LOG = LoggerFactory.getLogger(PayloadSerializer.class);
    private static final String COLLECTION_TYPE_LIST = "1";
    private static final String COLLECTION_TYPE_SET = "2";
    private static final String COLLECTION_TYPE_SORTED_SET = "3";
    private static final String COLLECTION_TPE_OTHER = "0";
    private volatile PersistenceManager persistenceManager;
    private final ObjectMapper mapper = new ObjectMapper();
    private PayloadConverterRegistry payloadConverterRegistry;
    private ModelService modelService;


    private PersistenceManager getPersistenceManager()
    {
        if(this.persistenceManager == null || (this.persistenceManager != null && !this.persistenceManager.isLoaded()))
        {
            this.persistenceManager = Registry.getCurrentTenantNoFallback().getPersistenceManager();
        }
        return this.persistenceManager;
    }


    public String serialize(Map<String, Object> modelValues)
    {
        try
        {
            List<PayloadRawValue> payloadRawValues = convertValuesForSerialization(modelValues);
            AuditPayload.Builder auditPayloadBuilder = AuditPayload.builder();
            for(PayloadRawValue rawValue : payloadRawValues)
            {
                auditPayloadBuilder.addAttribute(rawValue);
            }
            return this.mapper.writer().writeValueAsString(auditPayloadBuilder.build());
        }
        catch(JsonProcessingException e)
        {
            LOG.error("Failed to marshall audit record to JSON format");
            throw new SystemException(e);
        }
    }


    private List<PayloadRawValue> convertValuesForSerialization(Map<String, Object> originalValues)
    {
        List<PayloadRawValue> payloadRawValues = new ArrayList<>();
        for(Map.Entry<String, Object> entry : originalValues.entrySet())
        {
            String name = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof LocalizedAttributesList)
            {
                LocalizedAttributesList localizedAttributes = (LocalizedAttributesList)value;
                for(Object localizedAttribute : localizedAttributes)
                {
                    List<String> list = getObjectValueAsString((SLDDataContainer.AttributeValue)localizedAttribute);
                    if(list != null)
                    {
                        ValueType type = getObjectTypeAsString(localizedAttribute);
                        String lang = getObjectLanguage(localizedAttribute);
                        PayloadRawValue payloadRawValue = PayloadRawValue.withListValue(name, list, type, lang);
                        payloadRawValues.add(payloadRawValue);
                    }
                }
                continue;
            }
            SLDDataContainer.AttributeValue _value = (SLDDataContainer.AttributeValue)value;
            if(_value.getValue() instanceof Map)
            {
                String str = getObjectValueForMap((Map<Object, Object>)_value.getValue());
                if(StringUtils.isNotEmpty(str))
                {
                    ValueType type = getObjectTypeAsString(value);
                    PayloadRawValue payloadRawValue = PayloadRawValue.withMapValue(name, str, type);
                    payloadRawValues.add(payloadRawValue);
                }
                continue;
            }
            List<String> valueAsString = getObjectValueAsString(_value);
            if(valueAsString != null)
            {
                ValueType type = getObjectTypeAsString(value);
                PayloadRawValue payloadRawValue = PayloadRawValue.withListValue(name, valueAsString, type);
                payloadRawValues.add(payloadRawValue);
            }
        }
        return payloadRawValues;
    }


    private String getObjectValueForMap(Map<Object, Object> value)
    {
        if(value instanceof Serializable)
        {
            return serializeToString((Serializable)value);
        }
        return "";
    }


    private static String serializeToString(Serializable object)
    {
        ObjectOutputStream oos = null;
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e);
        }
        finally
        {
            IOUtils.closeQuietly(oos);
        }
    }


    private List<String> getObjectValueAsString(SLDDataContainer.AttributeValue inputValue)
    {
        Object value = inputValue.getValue();
        if(value == null)
        {
            return null;
        }
        if(value instanceof Collection)
        {
            Collection collection = (Collection)value;
            if(collection.isEmpty())
            {
                return null;
            }
            Object collectionObj = collection.iterator().next();
            if(collectionObj instanceof ItemPropertyValue)
            {
                List<String> collectionPKs = (List<String>)collection.stream().map(i -> convertSingleValueToString(i)).collect(Collectors.toList());
                return collectionPKs;
            }
            if(collectionObj instanceof de.hybris.platform.jalo.Item)
            {
                List<String> collectionPKs = (List<String>)collection.stream().map(i -> convertSingleValueToString(i)).collect(Collectors.toList());
                return collectionPKs;
            }
            return convertCollectionToStringList((Collection<Object>)value);
        }
        String strValue = convertSingleValueToString(value);
        return (strValue == null) ? null : Collections.<String>singletonList(strValue);
    }


    private String convertSingleValueToString(Object value)
    {
        if(value == null)
        {
            return null;
        }
        if(value instanceof ItemPropertyValue && isEnumType((ItemPropertyValue)value))
        {
            try
            {
                Object item = this.modelService.get(((ItemPropertyValue)value).getPK());
                return this.payloadConverterRegistry.convertToString(item, HybrisEnumValue.class);
            }
            catch(ModelLoadingException e)
            {
                LOG.debug("Item already removed", (Throwable)e);
            }
        }
        else if(value instanceof Collection)
        {
            return null;
        }
        return this.payloadConverterRegistry.convertToString(value, value.getClass());
    }


    private List<String> convertCollectionToStringList(Collection<Object> collection)
    {
        List<String> convertedObjects = (List<String>)collection.stream().map(i -> i.toString()).collect(Collectors.toList());
        return convertedObjects;
    }


    private ValueType getObjectTypeAsString(Object inputValue)
    {
        if(!(inputValue instanceof SLDDataContainer.AttributeValue))
        {
            return null;
        }
        SLDDataContainer.AttributeValue attribute = (SLDDataContainer.AttributeValue)inputValue;
        if(attribute.getValue() instanceof ItemPropertyValue)
        {
            return convertItemPropertyValue((ItemPropertyValue)attribute.getValue());
        }
        if(attribute.getValue() instanceof ItemPropertyValueCollection)
        {
            return convertItemPropertyValueCollection((ItemPropertyValueCollection)attribute.getValue());
        }
        if(attribute.getValue() instanceof Collection)
        {
            Collection<E> value = (Collection)attribute.getValue();
            String collectionType = getCollectionTypeCode(value);
            return ValueType.newCollectionType(value.isEmpty() ? null : value.iterator().next().getClass().getName(), collectionType);
        }
        if(attribute.getValue() instanceof de.hybris.platform.jalo.Item)
        {
            return ValueType.newType(PK.class.getName());
        }
        return (attribute.getValue() != null) ? ValueType.newType(attribute.getValue().getClass().getName()) : null;
    }


    private ValueType convertItemPropertyValue(ItemPropertyValue itemPropertyValue)
    {
        if(isEnumType(itemPropertyValue))
        {
            return ValueType.newType(HybrisEnumValue.class.getName());
        }
        return ValueType.newType(itemPropertyValue.getPK().getClass().getName());
    }


    private ValueType convertItemPropertyValueCollection(ItemPropertyValueCollection itemPropertyValueCollection)
    {
        int wrappedCollectionType = itemPropertyValueCollection.getWrapedCollectionType();
        if(itemPropertyValueCollection.isEmpty())
        {
            return null;
        }
        if(isEnumType((ItemPropertyValue)itemPropertyValueCollection.get(0)))
        {
            return ValueType.newCollectionType(HybrisEnumValue.class.getName(), String.valueOf(wrappedCollectionType));
        }
        return ValueType.newCollectionType(PK.class.getName(), String.valueOf(wrappedCollectionType));
    }


    private boolean isEnumType(ItemPropertyValue itemPropertyValue)
    {
        if(itemPropertyValue == null)
        {
            return false;
        }
        ItemDeployment dpl = getPersistenceManager().getItemDeployment(itemPropertyValue.getPK().getTypeCode());
        return (dpl != null && "de.hybris.platform.persistence.enumeration.EnumerationValue".equals(dpl.getName()));
    }


    private static String getCollectionTypeCode(Collection collection)
    {
        String collectionType;
        if(collection instanceof List)
        {
            collectionType = "1";
        }
        else if(collection instanceof java.util.SortedSet)
        {
            collectionType = "3";
        }
        else if(collection instanceof java.util.Set)
        {
            collectionType = "2";
        }
        else
        {
            collectionType = "0";
        }
        return collectionType;
    }


    private String getObjectLanguage(Object inputValue)
    {
        if(inputValue instanceof SLDDataContainer.AttributeValue)
        {
            SLDDataContainer.AttributeValue attribute = (SLDDataContainer.AttributeValue)inputValue;
            if(attribute.getLangPk() != null)
            {
                try
                {
                    LanguageModel lang = (LanguageModel)this.modelService.get(attribute.getLangPk());
                    return lang.getIsocode();
                }
                catch(ModelLoadingException ex)
                {
                    LOG.info("Language {} has been removed. Using 'unknown' ISO Code", attribute.getLangPk());
                    return "unknown";
                }
            }
        }
        return null;
    }


    @Required
    public void setPayloadConverterRegistry(PayloadConverterRegistry payloadConverterRegistry)
    {
        this.payloadConverterRegistry = payloadConverterRegistry;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
