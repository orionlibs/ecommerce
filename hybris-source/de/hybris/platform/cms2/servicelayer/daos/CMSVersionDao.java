package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import java.util.List;
import java.util.Optional;

public interface CMSVersionDao
{
    Optional<CMSVersionModel> findByUid(String paramString);


    Optional<CMSVersionModel> findByItemUidAndLabel(String paramString1, String paramString2, CatalogVersionModel paramCatalogVersionModel);


    List<CMSVersionModel> findAllByItemUidAndItemCatalogVersion(String paramString, CatalogVersionModel paramCatalogVersionModel);


    Optional<AbstractPageModel> findPageVersionedByTransactionId(String paramString);
}
