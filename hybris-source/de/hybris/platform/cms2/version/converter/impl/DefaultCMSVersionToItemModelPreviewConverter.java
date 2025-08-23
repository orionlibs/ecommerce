package de.hybris.platform.cms2.version.converter.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.items.service.ItemService;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.persistence.audit.payload.PayloadDeserializer;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import de.hybris.platform.persistence.audit.payload.json.TypedValue;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSVersionToItemModelPreviewConverter implements Converter<CMSVersionModel, ItemModel>
{
    private static final Logger LOGGER = Logger.getLogger(DefaultCMSVersionToItemModelPreviewConverter.class);
    private PayloadDeserializer payloadDeserializer;
    private ModelService modelService;
    private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;
    private Populator<AuditPayload, ItemModel> cmsVersionToItemModelPopulator;
    private ItemService itemService;


    public ItemModel convert(CMSVersionModel version)
    {
        Map<CMSVersionModel, ItemModel> cachedItems = getCmsVersionSessionContextProvider().getAllGeneratedItemsFromCached();
        if(cachedItems.containsKey(version))
        {
            return cachedItems.get(version);
        }
        AuditPayload auditPayload = getPayloadDeserializer().deserialize(version.getPayload());
        Map<String, Object> mappedAttributes = (Map<String, Object>)auditPayload.getAttributes().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> ((TypedValue)e.getValue()).getValue()));
        ItemModel itemModel = getItemService().getOrCreateItemByAttributeValues(version.getItemTypeCode(), mappedAttributes);
        getCmsVersionSessionContextProvider().addGeneratedItemToCache(itemModel, version);
        getCmsVersionToItemModelPopulator().populate(auditPayload, itemModel);
        return itemModel;
    }


    protected PayloadDeserializer getPayloadDeserializer()
    {
        return this.payloadDeserializer;
    }


    @Required
    public void setPayloadDeserializer(PayloadDeserializer payloadDeserializer)
    {
        this.payloadDeserializer = payloadDeserializer;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected CMSVersionSessionContextProvider getCmsVersionSessionContextProvider()
    {
        return this.cmsVersionSessionContextProvider;
    }


    @Required
    public void setCmsVersionSessionContextProvider(CMSVersionSessionContextProvider cmsVersionSessionContextProvider)
    {
        this.cmsVersionSessionContextProvider = cmsVersionSessionContextProvider;
    }


    protected Populator<AuditPayload, ItemModel> getCmsVersionToItemModelPopulator()
    {
        return this.cmsVersionToItemModelPopulator;
    }


    @Required
    public void setCmsVersionToItemModelPopulator(Populator<AuditPayload, ItemModel> cmsVersionToItemModelPopulator)
    {
        this.cmsVersionToItemModelPopulator = cmsVersionToItemModelPopulator;
    }


    public ItemService getItemService()
    {
        return this.itemService;
    }


    @Required
    public void setItemService(ItemService itemService)
    {
        this.itemService = itemService;
    }
}
