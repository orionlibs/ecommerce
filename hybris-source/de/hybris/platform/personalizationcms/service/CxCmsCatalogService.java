package de.hybris.platform.personalizationcms.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.enums.CxCatalogLookupType;
import de.hybris.platform.personalizationservices.service.impl.DefaultCxCatalogService;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class CxCmsCatalogService extends DefaultCxCatalogService
{
    private SessionService sessionService;


    public List<CatalogVersionModel> getConfiguredCatalogVersions()
    {
        if(getPreviewTicketId() != null)
        {
            return getConfiguredCatalogVersions(CxCatalogLookupType.ALL_CATALOGS);
        }
        return super.getConfiguredCatalogVersions();
    }


    protected String getPreviewTicketId()
    {
        return (String)this.sessionService.getAttribute("cmsTicketId");
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }
}
