package de.hybris.platform.platformbackoffice.interceptors;

import de.hybris.platform.catalog.interceptors.CatalogVersionPrepareInterceptor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

public class BackofficeCatalogVersionPrepareInterceptor extends CatalogVersionPrepareInterceptor
{
    protected void assignCurrentUserReadWritePermissions(CatalogVersionModel catalogVersion, InterceptorContext ctx)
    {
        if(ctx.isNew(catalogVersion))
        {
            super.assignCurrentUserReadWritePermissions(catalogVersion, ctx);
        }
    }
}
