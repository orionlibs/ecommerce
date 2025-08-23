package com.hybris.backoffice.search.daos;

import de.hybris.platform.core.model.ItemModel;
import java.util.List;

public interface ItemModelSearchDAO
{
    List<ItemModel> findAll(String paramString, List<Long> paramList);
}
