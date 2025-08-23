package de.hybris.platform.cms2.version.converter.impl;

import de.hybris.platform.cms2.version.converter.customattribute.CMSVersionCustomAttribute;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.persistence.audit.payload.PayloadDeserializer;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import de.hybris.platform.persistence.audit.payload.json.LocalizedTypedValue;
import de.hybris.platform.persistence.audit.payload.json.TypedValue;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CMSVersionPayloadAnalyzer
{
    private final PayloadDeserializer payloadDeserializer;
    private final ModelService modelService;
    public List<PayloadAttribute> attributes;


    public CMSVersionPayloadAnalyzer(PayloadDeserializer payloadDeserializer, FlexibleSearchService flexibleSearchService, ModelService modelService)
    {
        this.attributes = new ArrayList<>();
        this.payloadDeserializer = payloadDeserializer;
        this.modelService = modelService;
    }


    public void analyse(String payload)
    {
        this.attributes.clear();
        AuditPayload auditPayload = this.payloadDeserializer.deserialize(payload);
        List<PayloadAttribute> payloadAttributes = (List<PayloadAttribute>)auditPayload.getAttributes().entrySet().stream().map(entry -> {
            PayloadAttribute payloadAttribute = new PayloadAttribute(this);
            payloadAttribute.name = (String)entry.getKey();
            payloadAttribute.isCollection = !((TypedValue)entry.getValue()).getType().getCollection().isEmpty();
            payloadAttribute.type = ((TypedValue)entry.getValue()).getType().getType();
            List<PayloadValue> payloadValues = (List<PayloadValue>)((TypedValue)entry.getValue()).getValue().stream().map(()).collect(Collectors.toList());
            payloadAttribute.values = payloadValues;
            return payloadAttribute;
        }).collect(Collectors.toList());
        List<PayloadAttribute> localizedPayloadAttributes = (List<PayloadAttribute>)auditPayload.getLocAttributes().entrySet().stream().map(entry -> {
            PayloadAttribute payloadAttribute = new PayloadAttribute(this);
            payloadAttribute.name = (String)entry.getKey();
            payloadAttribute.isCollection = !((LocalizedTypedValue)entry.getValue()).getType().getCollection().isEmpty();
            payloadAttribute.type = ((LocalizedTypedValue)entry.getValue()).getType().getType();
            List<PayloadValue> payloadValues = (List<PayloadValue>)((LocalizedTypedValue)entry.getValue()).getValues().stream().flatMap(()).collect(Collectors.toList());
            payloadAttribute.values = payloadValues;
            return payloadAttribute;
        }).collect(Collectors.toList());
        this.attributes.addAll(payloadAttributes);
        this.attributes.addAll(localizedPayloadAttributes);
    }


    public PayloadAttribute getAttributeByName(String name)
    {
        return this.attributes.stream().filter(attr -> attr.name.equals(name)).findFirst().get();
    }


    protected boolean isCustomAttributeType(String payloadType)
    {
        Class<?> typeClass = null;
        try
        {
            typeClass = Class.forName(payloadType);
        }
        catch(ClassNotFoundException classNotFoundException)
        {
        }
        return (typeClass != null && CMSVersionCustomAttribute.class.isAssignableFrom(typeClass));
    }


    protected boolean isVersionPK(String pkFromVersion)
    {
        try
        {
            ItemModel itemModel = (ItemModel)this.modelService.get(PK.parse(pkFromVersion));
            return itemModel instanceof de.hybris.platform.cms2.model.CMSVersionModel;
        }
        catch(Exception e)
        {
            return false;
        }
    }


    protected boolean isPK(String pkFromVersion)
    {
        try
        {
            PK pk = PK.parse(pkFromVersion);
            return (pk != null);
        }
        catch(de.hybris.platform.core.PK.PKException e)
        {
            return false;
        }
    }


    protected boolean isPKAttributeType(String payloadType)
    {
        Class<?> typeClass = null;
        try
        {
            typeClass = Class.forName(payloadType);
        }
        catch(ClassNotFoundException classNotFoundException)
        {
        }
        return (typeClass != null && PK.class.isAssignableFrom(typeClass));
    }
}
