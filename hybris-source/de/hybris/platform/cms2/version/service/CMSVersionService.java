package de.hybris.platform.cms2.version.service;

import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.Optional;

public interface CMSVersionService
{
    ItemModel createItemFromVersion(CMSVersionModel paramCMSVersionModel);


    ItemModel getItemFromVersion(CMSVersionModel paramCMSVersionModel);


    Optional<CMSVersionModel> getVersionByUid(String paramString);


    Optional<CMSVersionModel> getVersionByLabel(CMSItemModel paramCMSItemModel, String paramString);


    String getTransactionId();


    CMSVersionModel createRevisionForItem(CMSItemModel paramCMSItemModel);


    CMSVersionModel createVersionForItem(CMSItemModel paramCMSItemModel, String paramString1, String paramString2);


    Optional<ItemModel> rollbackVersionForUid(String paramString);


    boolean isVersionable(CMSItemModel paramCMSItemModel);


    String generateVersionUid();


    void deleteVersionsForItem(CMSItemModel paramCMSItemModel);


    Optional<AbstractPageModel> findPageVersionedByTransactionId(String paramString);
}
