package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import org.springframework.beans.factory.annotation.Required;

public class CMSItemRemoveInterceptor implements RemoveInterceptor
{
    private CMSVersionService cmsVersionService;


    public void onRemove(Object model, InterceptorContext ctx)
    {
        getCmsVersionService().deleteVersionsForItem((CMSItemModel)model);
    }


    @Required
    public void setCmsVersionService(CMSVersionService cmsVersionService)
    {
        this.cmsVersionService = cmsVersionService;
    }


    public CMSVersionService getCmsVersionService()
    {
        return this.cmsVersionService;
    }
}
