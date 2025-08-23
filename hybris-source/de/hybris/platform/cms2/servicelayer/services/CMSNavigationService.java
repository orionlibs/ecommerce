package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CMSNavigationService
{
    CMSNavigationNodeModel getNavigationNodeForId(String paramString) throws CMSItemNotFoundException;


    List<CMSNavigationNodeModel> getNavigationNodesForContentPage(ContentPageModel paramContentPageModel);


    List<CMSNavigationNodeModel> getNavigationNodesForContentPageId(String paramString) throws CMSItemNotFoundException;


    List<CMSNavigationNodeModel> getRootNavigationNodes();


    List<CMSNavigationNodeModel> getRootNavigationNodesForContentPage(ContentPageModel paramContentPageModel);


    List<CMSNavigationNodeModel> getRootNavigationNodesForContentPageId(String paramString) throws CMSItemNotFoundException;


    List<CMSNavigationEntryModel> getNavigationEntriesByPage(AbstractPageModel paramAbstractPageModel);


    Optional<CMSNavigationEntryModel> getNavigationEntryForId(String paramString, CatalogVersionModel paramCatalogVersionModel);


    CMSNavigationEntryModel createCmsNavigationEntry(CMSNavigationNodeModel paramCMSNavigationNodeModel, String paramString, ItemModel paramItemModel);


    CMSNavigationEntryModel createCmsNavigationEntry(CatalogVersionModel paramCatalogVersionModel, ItemModel paramItemModel);


    List<CMSNavigationNodeModel> getRootNavigationNodes(CatalogVersionModel paramCatalogVersionModel);


    void move(CMSNavigationNodeModel paramCMSNavigationNodeModel1, CMSNavigationNodeModel paramCMSNavigationNodeModel2);


    void move(CMSNavigationNodeModel paramCMSNavigationNodeModel, ItemModel paramItemModel1, ItemModel paramItemModel2);


    void move(ItemModel paramItemModel, CMSNavigationNodeModel paramCMSNavigationNodeModel1, CMSNavigationNodeModel paramCMSNavigationNodeModel2);


    @Deprecated(since = "1811", forRemoval = true)
    void delete(CMSNavigationNodeModel paramCMSNavigationNodeModel);


    void remove(CMSNavigationNodeModel paramCMSNavigationNodeModel, ItemModel paramItemModel);


    @Deprecated(since = "1811", forRemoval = true)
    boolean removeNavigationEntryByUid(CMSNavigationNodeModel paramCMSNavigationNodeModel, String paramString);


    @Deprecated(since = "1811", forRemoval = true)
    CMSNavigationNodeModel createNavigationNode(ItemModel paramItemModel, String paramString, boolean paramBoolean, Collection<ItemModel> paramCollection);


    boolean isSuperRootNavigationNode(CMSNavigationNodeModel paramCMSNavigationNodeModel);


    CMSNavigationNodeModel setSuperRootNodeOnNavigationNode(CMSNavigationNodeModel paramCMSNavigationNodeModel, CatalogVersionModel paramCatalogVersionModel);


    CMSNavigationNodeModel getSuperRootNavigationNode(CatalogVersionModel paramCatalogVersionModel);


    void appendRelatedItems(CMSNavigationNodeModel paramCMSNavigationNodeModel, Collection<ItemModel> paramCollection);


    CMSNavigationNodeModel createSuperRootNavigationNode(CatalogVersionModel paramCatalogVersionModel);


    void move(CMSNavigationNodeModel paramCMSNavigationNodeModel1, CMSNavigationNodeModel paramCMSNavigationNodeModel2, boolean paramBoolean1, boolean paramBoolean2);
}
