package de.hybris.platform.spring.security.voter;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.system.InitializationLockDao;
import de.hybris.platform.core.system.InitializationLockHandler;
import de.hybris.platform.core.system.impl.DefaultInitLockDao;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

public class HybrisCurrentlyAdminstratingVoter implements AccessDecisionVoter<Object>
{
    private static final Logger log = Logger.getLogger(HybrisCurrentlyAdminstratingVoter.class.getName());
    private static final String HYBRIS_ADMINISTRATING_TOKEN = "HYBRIS_CURRENTLY_ADMINISTRATING";


    public boolean supports(ConfigAttribute attribute)
    {
        return "HYBRIS_CURRENTLY_ADMINISTRATING".equals(attribute.getAttribute());
    }


    public boolean supports(Class clazz)
    {
        return true;
    }


    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> config)
    {
        InitializationLockHandler handler = new InitializationLockHandler((InitializationLockDao)new DefaultInitLockDao());
        for(ConfigAttribute attribute : config)
        {
            if(supports(attribute))
            {
                if(Registry.hasCurrentTenant() && handler.isLocked())
                {
                    return 1;
                }
            }
        }
        return 0;
    }
}
