package com.hybris.backoffice.solrsearch.daos;

import com.hybris.backoffice.solrsearch.enums.SolrItemModificationType;
import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import java.util.Collection;

@Deprecated(since = "1808", forRemoval = true)
public interface SolrModifiedItemDAO
{
    Collection<SolrModifiedItemModel> findByModificationType(SolrItemModificationType paramSolrItemModificationType);
}
