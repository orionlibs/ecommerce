package de.hybris.platform.cms2.servicelayer.interceptor.service;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

public interface ItemModelPrepareInterceptorService
{
    boolean isOnlyChangeSynchronizationBlocked(ItemModel paramItemModel, InterceptorContext paramInterceptorContext);


    boolean isFromActiveCatalogVersion(ItemModel paramItemModel);


    boolean isEnabled();


    void setEnabled(boolean paramBoolean);
}
