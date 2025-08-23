package de.hybris.platform.cockpit.security;

import de.hybris.platform.cockpit.security.exceptions.PerspectivePermissionsDeniedException;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.util.SessionProxy;
import de.hybris.platform.spring.security.CoreAuthenticationProvider;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CockpitAuthenticationProvider extends CoreAuthenticationProvider
{
    private SessionProxy sessionProxy;


    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        Authentication ret = null;
        try
        {
            ret = super.authenticate(authentication);
            List<UICockpitPerspective> availablePerspectives = getSessionProxy().getSession().getAvailablePerspectives();
            if(CollectionUtils.isEmpty(availablePerspectives))
            {
                throw new PerspectivePermissionsDeniedException("No available perspectives for the user.");
            }
        }
        catch(AuthenticationException e)
        {
            getSessionProxy().getSession().setUser(null);
            throw e;
        }
        return ret;
    }


    public SessionProxy getSessionProxy()
    {
        return this.sessionProxy;
    }


    @Required
    public void setSessionProxy(SessionProxy sessionProxy)
    {
        this.sessionProxy = sessionProxy;
    }
}
