package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.services.ContentCatalogService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import org.springframework.beans.factory.annotation.Required;

public class CatalogVersionRemoveInterceptor implements RemoveInterceptor
{
    private ContentCatalogService contentCatalogService;


    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(!(model instanceof CatalogVersionModel) || getContentCatalogService().hasCMSItems((CatalogVersionModel)model) ||
                        getContentCatalogService().hasCMSRelations((CatalogVersionModel)model))
        {
            throw new InterceptorException("Can not remove catalog version since there are instances of CMSItem or CMSRelation with references to it.");
        }
    }


    protected ContentCatalogService getContentCatalogService()
    {
        return this.contentCatalogService;
    }


    @Required
    public void setContentCatalogService(ContentCatalogService contentCatalogService)
    {
        this.contentCatalogService = contentCatalogService;
    }
}
