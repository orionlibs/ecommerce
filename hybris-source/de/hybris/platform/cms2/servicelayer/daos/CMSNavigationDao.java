package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import java.util.Collection;
import java.util.List;

public interface CMSNavigationDao
{
    List<CMSNavigationNodeModel> findRootNavigationNodes(Collection<CatalogVersionModel> paramCollection);


    List<CMSNavigationNodeModel> findNavigationNodesById(String paramString, Collection<CatalogVersionModel> paramCollection);


    List<CMSNavigationNodeModel> findNavigationNodesByContentPage(ContentPageModel paramContentPageModel, Collection<CatalogVersionModel> paramCollection);


    CMSNavigationNodeModel findSuperRootNavigationNode(CatalogVersionModel paramCatalogVersionModel);


    List<CMSNavigationEntryModel> findNavigationEntriesByPage(AbstractPageModel paramAbstractPageModel);


    CMSNavigationEntryModel findNavigationEntryByUid(String paramString, CatalogVersionModel paramCatalogVersionModel);
}
