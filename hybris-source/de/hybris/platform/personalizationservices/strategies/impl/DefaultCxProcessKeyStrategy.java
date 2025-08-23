package de.hybris.platform.personalizationservices.strategies.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.strategies.CxProcessKeyStrategy;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxProcessKeyStrategy implements CxProcessKeyStrategy
{
    private UserService userService;
    private DefaultSessionTokenService defaultSessionTokenService;


    public String getProcessKey(UserModel user, CatalogVersionModel catalogVersions)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.userService.isAnonymousUser(user) ? this.defaultSessionTokenService.getOrCreateSessionToken() : user.getUid());
        sb.append("_");
        sb.append(catalogVersions.getPk());
        return sb.toString();
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setDefaultSessionTokenService(DefaultSessionTokenService defaultSessionTokenService)
    {
        this.defaultSessionTokenService = defaultSessionTokenService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected DefaultSessionTokenService getDefaultSessionTokenService()
    {
        return this.defaultSessionTokenService;
    }
}
