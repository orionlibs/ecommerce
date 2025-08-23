package de.hybris.platform.cms2.relatedpages.service;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

public interface RelatedPageRejectionService
{
    @Deprecated(since = "2105", forRemoval = true)
    void rejectAllRelatedPages(ItemModel paramItemModel);


    default void rejectAllRelatedPages(ItemModel itemModel, InterceptorContext interceptorContext)
    {
    }


    AbstractPageModel rejectPage(AbstractPageModel paramAbstractPageModel);


    boolean hasUserChangedApprovalStatus(AbstractPageModel paramAbstractPageModel);
}
