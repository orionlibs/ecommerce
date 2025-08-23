package de.hybris.platform.cockpit.security;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.util.SessionProxy;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.spring.security.CoreUserDetails;
import de.hybris.platform.spring.security.CoreUserDetailsService;
import java.util.Collection;
import java.util.LinkedHashSet;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultCockpitUserDetailsService extends CoreUserDetailsService
{
    private boolean activateCatalogVersions = false;
    private SessionProxy sessionProxy;
    private CatalogVersionService catalogVersionService;
    private UserService userService;
    private SessionService sessionService;


    public SessionProxy getSessionProxy()
    {
        return this.sessionProxy;
    }


    @Required
    public void setSessionProxy(SessionProxy sessionProxy)
    {
        this.sessionProxy = sessionProxy;
    }


    public CoreUserDetails loadUserByUsername(String username)
    {
        UserModel user;
        CoreUserDetails userDetails = super.loadUserByUsername(username);
        try
        {
            user = this.userService.getUserForUID(username);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UsernameNotFoundException("User not found!");
        }
        getSessionProxy().getSession().setUserByUID(user.getUid());
        if(isActivateCatalogVersions())
        {
            Collection<CatalogVersionModel> allowedVersions = new LinkedHashSet<>();
            this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, user, allowedVersions), user);
            this.catalogVersionService.setSessionCatalogVersions(allowedVersions);
        }
        return userDetails;
    }


    public boolean isActivateCatalogVersions()
    {
        return this.activateCatalogVersions;
    }


    public void setActivateCatalogVersions(boolean activateCatalogVersions)
    {
        this.activateCatalogVersions = activateCatalogVersions;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }
}
