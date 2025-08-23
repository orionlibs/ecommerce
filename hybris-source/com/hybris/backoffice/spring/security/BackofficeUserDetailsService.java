package com.hybris.backoffice.spring.security;

import com.hybris.backoffice.catalogversioneventhandling.AvailableCatalogVersionsTag;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.spring.security.CoreUserDetails;
import de.hybris.platform.spring.security.CoreUserDetailsService;
import java.util.Collection;
import java.util.LinkedHashSet;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class BackofficeUserDetailsService extends CoreUserDetailsService
{
    private static final String CATALOG_VERSIONS_TAG = "catalog_versions_tag";
    private boolean activateCatalogVersions = false;
    private CatalogVersionService catalogVersionService;
    private UserService userService;
    private SessionService sessionService;
    private AvailableCatalogVersionsTag availableCatalogVersionsTag;


    public CoreUserDetails loadUserByUsername(String username)
    {
        UserModel user;
        CoreUserDetails userDetails = super.loadUserByUsername(username);
        try
        {
            user = getUserService().getUserForUID(username);
        }
        catch(UnknownIdentifierException e)
        {
            throw new UsernameNotFoundException("User not found!", e);
        }
        activateCatalogVersions(user);
        return userDetails;
    }


    public void activateCatalogVersions(UserModel user)
    {
        if(!isActivateCatalogVersions() || user == null)
        {
            return;
        }
        Collection<CatalogVersionModel> allowedVersions = new LinkedHashSet<>();
        if(getUserService().isAdmin(user))
        {
            getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, allowedVersions), (UserModel)
                            getUserService().getAdminUser());
        }
        else
        {
            allowedVersions.addAll(getCatalogVersionService().getAllReadableCatalogVersions((PrincipalModel)user));
            allowedVersions.addAll(getCatalogVersionService().getAllWritableCatalogVersions((PrincipalModel)user));
        }
        getCatalogVersionService().setSessionCatalogVersions(allowedVersions);
        getSessionService().setAttribute("catalog_versions_tag", getAvailableCatalogVersionsTag().getTag());
    }


    public boolean isActivateCatalogVersions()
    {
        return this.activateCatalogVersions;
    }


    public void setActivateCatalogVersions(boolean activateCatalogVersions)
    {
        this.activateCatalogVersions = activateCatalogVersions;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
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


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected AvailableCatalogVersionsTag getAvailableCatalogVersionsTag()
    {
        return this.availableCatalogVersionsTag;
    }


    @Required
    public void setAvailableCatalogVersionsTag(AvailableCatalogVersionsTag availableCatalogVersionsTag)
    {
        this.availableCatalogVersionsTag = availableCatalogVersionsTag;
    }
}
