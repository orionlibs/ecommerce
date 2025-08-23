package de.hybris.platform.cms2.servicelayer.daos;

import java.util.Map;
import java.util.Optional;

public interface ItemDao
{
    <T extends de.hybris.platform.core.model.ItemModel> Optional<T> getItemByUniqueAttributesValues(String paramString, Map<String, Object> paramMap);
}
