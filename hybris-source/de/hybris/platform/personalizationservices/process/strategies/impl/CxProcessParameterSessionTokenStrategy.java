package de.hybris.platform.personalizationservices.process.strategies.impl;

import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class CxProcessParameterSessionTokenStrategy extends AbstractCxProcessParameterStrategy
{
    private DefaultSessionTokenService defaultSessionTokenService;


    public void load(CxPersonalizationProcessModel process)
    {
        Objects.requireNonNull(this.defaultSessionTokenService);
        consumeProcessParameter(process, "SESSION_TOKEN_KEY", this.defaultSessionTokenService::setSessionToken);
    }


    public void store(CxPersonalizationProcessModel process)
    {
        getProcessParameterHelper().setProcessParameter((BusinessProcessModel)process, "SESSION_TOKEN_KEY", this.defaultSessionTokenService
                        .getOrCreateSessionToken());
    }


    @Required
    public void setDefaultSessionTokenService(DefaultSessionTokenService defaultSessionTokenService)
    {
        this.defaultSessionTokenService = defaultSessionTokenService;
    }


    protected DefaultSessionTokenService getDefaultSessionTokenService()
    {
        return this.defaultSessionTokenService;
    }
}
