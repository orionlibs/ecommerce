package de.hybris.platform.spring.security.voter;

import java.util.Collection;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

public class PermitAllVoter implements AccessDecisionVoter<Object>
{
    public boolean supports(ConfigAttribute configAttribute)
    {
        return "PERMIT_ALL".equals(configAttribute.getAttribute());
    }


    public boolean supports(Class<?> aClass)
    {
        return aClass.isAssignableFrom(FilterInvocation.class);
    }


    public int vote(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes)
    {
        for(ConfigAttribute attribute : configAttributes)
        {
            if(supports(attribute))
            {
                return 1;
            }
        }
        return 0;
    }
}
