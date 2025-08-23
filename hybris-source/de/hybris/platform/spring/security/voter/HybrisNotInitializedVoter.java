package de.hybris.platform.spring.security.voter;

import de.hybris.platform.core.Registry;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

public class HybrisNotInitializedVoter implements AccessDecisionVoter<Object>
{
    private static final Logger log = Logger.getLogger(HybrisNotInitializedVoter.class.getName());
    private static final String HYBRIS_NOT_INITIALIZED = "HYBRIS_NOT_INITIALIZED";


    public boolean supports(ConfigAttribute attribute)
    {
        return "HYBRIS_NOT_INITIALIZED".equals(attribute.getAttribute());
    }


    public boolean supports(Class clazz)
    {
        return true;
    }


    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> config)
    {
        for(ConfigAttribute attribute : config)
        {
            if(supports(attribute))
            {
                if(Registry.hasCurrentTenant() &&
                                !Registry.getCurrentTenantNoFallback().getJaloConnection().isSystemInitialized())
                {
                    return 1;
                }
            }
        }
        return 0;
    }
}
