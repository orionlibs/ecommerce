package de.hybris.platform.cmscockpit.resolvers;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.resolvers.CMSUrlResolver;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultLiveEditViewModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class PreviewUrlPageResolver implements CMSUrlResolver<AbstractPageModel>
{
    private CMSAdminSiteService cmsAdminSiteService;


    public String resolve(AbstractPageModel page)
    {
        ServicesUtil.validateParameterNotNull(page, "Page cannot be null");
        DefaultLiveEditViewModel model = new DefaultLiveEditViewModel();
        model.setPage(page);
        model.setSite(this.cmsAdminSiteService.getActiveSite());
        model.setPagePreview(true);
        return model.computeFinalUrl();
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService cmsAdminSiteService)
    {
        this.cmsAdminSiteService = cmsAdminSiteService;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.cmsAdminSiteService;
    }


    public boolean isInternal(String url)
    {
        DefaultLiveEditViewModel model = new DefaultLiveEditViewModel();
        return url.startsWith(model.extractUrlFromRequest());
    }
}
