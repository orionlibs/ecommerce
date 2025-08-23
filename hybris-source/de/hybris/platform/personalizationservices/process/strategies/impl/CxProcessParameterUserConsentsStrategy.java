package de.hybris.platform.personalizationservices.process.strategies.impl;

import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class CxProcessParameterUserConsentsStrategy extends AbstractCxProcessParameterStrategy
{
    private SessionService sessionService;


    public void load(CxPersonalizationProcessModel process)
    {
        consumeProcessParameter(process, "user-consents", this::setSessionConsents);
    }


    public void store(CxPersonalizationProcessModel process)
    {
        Map<String, String> sessionConsents = getSessionConsents();
        if(sessionConsents != null)
        {
            getProcessParameterHelper().setProcessParameter((BusinessProcessModel)process, "user-consents", sessionConsents);
        }
    }


    protected void setSessionConsents(Map sessionConsents)
    {
        this.sessionService.setAttribute("user-consents", sessionConsents);
    }


    public Map<String, String> getSessionConsents()
    {
        Object attribute = this.sessionService.getAttribute("user-consents");
        Map<String, String> consentMap = (Map<String, String>)attribute;
        return consentMap;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
