package de.hybris.platform.cms2.items.service;

import de.hybris.platform.cms2.exceptions.ItemNotFoundException;
import de.hybris.platform.core.model.ItemModel;
import java.util.Map;

public interface ItemService
{
    @Deprecated(since = "2105", forRemoval = true)
    ItemModel getItemByAttributeValues(String paramString, Map<String, Object> paramMap) throws ItemNotFoundException;


    default ItemModel getOrCreateItemByAttributeValues(String typeCode, Map<String, Object> attributeValues)
    {
        throw new UnsupportedOperationException("Please implement method ItemService.getOrCreateItemByAttributeValues()");
    }
}
