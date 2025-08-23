package de.hybris.platform.cms2.version.converter.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.exceptions.ItemNotFoundException;
import de.hybris.platform.cms2.exceptions.ItemRollbackException;
import de.hybris.platform.cms2.items.service.ItemService;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.version.converter.rollback.ItemRollbackConverter;
import de.hybris.platform.cms2.version.converter.rollback.ItemRollbackStrategyConverterProvider;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.persistence.audit.payload.PayloadDeserializer;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import de.hybris.platform.persistence.audit.payload.json.TypedValue;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSVersionToItemModelRollbackConverter implements Converter<CMSVersionModel, ItemModel>
{
    private static final Logger LOGGER = Logger.getLogger(DefaultCMSVersionToItemModelRollbackConverter.class);
    private PayloadDeserializer payloadDeserializer;
    private ModelService modelService;
    private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;
    private Populator<AuditPayload, ItemModel> cmsVersionToItemModelPopulator;
    private ItemService itemService;
    private ItemRollbackStrategyConverterProvider cmsItemRollbackStrategyConverterProvider;


    public ItemModel convert(CMSVersionModel version)
    {
        Map<CMSVersionModel, ItemModel> cachedItems = getCmsVersionSessionContextProvider().getAllGeneratedItemsFromCached();
        if(cachedItems.containsKey(version))
        {
            return cachedItems.get(version);
        }
        AuditPayload auditPayload = getPayloadDeserializer().deserialize(version.getPayload());
        Map<String, Object> mappedAttributes = (Map<String, Object>)auditPayload.getAttributes().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> ((TypedValue)e.getValue()).getValue()));
        try
        {
            ItemModel itemModel = rollbackItemModel(version, auditPayload, mappedAttributes);
            getCmsVersionSessionContextProvider().addGeneratedItemToCache(itemModel, version);
            getCmsVersionToItemModelPopulator().populate(auditPayload, itemModel);
            return itemModel;
        }
        catch(ItemNotFoundException e)
        {
            LOGGER.info(String.format("Failed to find an ItemModel for %s and %s", new Object[] {version.getItemTypeCode(), mappedAttributes}), (Throwable)e);
        }
        catch(ItemRollbackException e)
        {
            LOGGER.info(String.format("Failed to rollback ItemModel for %s and %s", new Object[] {version.getItemTypeCode(), mappedAttributes}), (Throwable)e);
        }
        return null;
    }


    protected ItemModel rollbackItemModel(CMSVersionModel version, AuditPayload auditPayload, Map<String, Object> mappedAttributes) throws ItemNotFoundException, ItemRollbackException
    {
        ItemModel originalItem = getItemService().getOrCreateItemByAttributeValues(version.getItemTypeCode(), mappedAttributes);
        Optional<ItemRollbackConverter> rollbackConverter = getCmsItemRollbackStrategyConverterProvider().getConverter(originalItem);
        if(rollbackConverter.isPresent())
        {
            return ((ItemRollbackConverter)rollbackConverter.get()).rollbackItem(originalItem, version, auditPayload);
        }
        return originalItem;
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


    protected ItemService getItemService()
    {
        return this.itemService;
    }


    @Required
    public void setItemService(ItemService itemService)
    {
        this.itemService = itemService;
    }


    protected ItemRollbackStrategyConverterProvider getCmsItemRollbackStrategyConverterProvider()
    {
        return this.cmsItemRollbackStrategyConverterProvider;
    }


    @Required
    public void setCmsItemRollbackStrategyConverterProvider(ItemRollbackStrategyConverterProvider cmsItemRollbackStrategyConverterProvider)
    {
        this.cmsItemRollbackStrategyConverterProvider = cmsItemRollbackStrategyConverterProvider;
    }
}
