package de.hybris.platform.personalizationcms.occ.handlers.impl;

import de.hybris.platform.personalizationservices.occ.handlers.impl.DefaultOccPersonalizationHandler;

public class PreviewOccPersonalizationHandler extends DefaultOccPersonalizationHandler
{
    protected boolean isPersonalizationEnabled()
    {
        return (isPreviewEnabled() || super.isPersonalizationEnabled());
    }


    protected boolean isPreviewEnabled()
    {
        return (getSessionService().getAttribute("cmsTicketId") != null);
    }
}
