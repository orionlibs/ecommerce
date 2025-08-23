package de.hybris.platform.cockpit.security;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cockpit.util.SessionProxy;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.spring.security.CoreUserDetails;
import de.hybris.platform.spring.security.CoreUserDetailsService;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CockpitUserDetailsService extends CoreUserDetailsService
{
    private boolean activateCatalogVersions = false;
    private SessionProxy sessionProxy;


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
        User user;
        CoreUserDetails userDetails = super.loadUserByUsername(username);
        try
        {
            user = UserManager.getInstance().getUserByLogin(username);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UsernameNotFoundException("User not found!");
        }
        getSessionProxy().getSession().setUserByUID(user.getUID());
        if(isActivateCatalogVersions())
        {
            SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
            ctx.setUser(user);
            Collection<CatalogVersion> allowedVersions = new ArrayList<>();
            if(user.isAdmin())
            {
                allowedVersions.addAll(CatalogManager.getInstance().getAllCatalogVersions());
            }
            else
            {
                allowedVersions.addAll(CatalogManager.getInstance().getAllReadableCatalogVersions(ctx, user));
            }
            CatalogManager.getInstance().setSessionCatalogVersions(allowedVersions);
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
}
