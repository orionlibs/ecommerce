package com.hybris.backoffice.solrsearch.daos;

import de.hybris.platform.core.model.ItemModel;
import java.util.List;

@Deprecated(since = "2105", forRemoval = true)
public interface SolrFieldSearchDAO
{
    List<ItemModel> findAll(String paramString, List<Long> paramList);
}
