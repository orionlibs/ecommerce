package de.hybris.platform.cms2.items.service.impl;

import de.hybris.platform.cms2.exceptions.ItemNotFoundException;
import de.hybris.platform.cms2.items.service.ItemService;
import de.hybris.platform.cms2.servicelayer.daos.ItemDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultItemService implements ItemService
{
    private ItemDao itemDao;
    private ModelService modelService;


    public ItemModel getItemByAttributeValues(String typeCode, Map<String, Object> attributeValues) throws ItemNotFoundException
    {
        return (ItemModel)getItemDao().getItemByUniqueAttributesValues(typeCode, attributeValues)
                        .orElseThrow(() -> new ItemNotFoundException("No item found for specified typeCode (" + typeCode + ") and a list of attribute values"));
    }


    public ItemModel getOrCreateItemByAttributeValues(String typeCode, Map<String, Object> attributeValues)
    {
        return getItemDao().getItemByUniqueAttributesValues(typeCode, attributeValues)
                        .orElseGet(() -> (ItemModel)getModelService().create(typeCode));
    }


    protected ItemDao getItemDao()
    {
        return this.itemDao;
    }


    @Required
    public void setItemDao(ItemDao itemDao)
    {
        this.itemDao = itemDao;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
