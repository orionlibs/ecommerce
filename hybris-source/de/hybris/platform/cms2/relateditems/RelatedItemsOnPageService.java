package de.hybris.platform.cms2.relateditems;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import java.util.HashSet;
import java.util.Set;

public interface RelatedItemsOnPageService
{
    Set<CMSItemModel> getRelatedItems(AbstractPageModel paramAbstractPageModel);


    default Set<CMSItemModel> getChildComponentsAndRelatedRestrictions(Set<AbstractCMSComponentModel> cmsComponentModels, CatalogVersionModel catalogVersionModel)
    {
        return new HashSet<>();
    }
}
