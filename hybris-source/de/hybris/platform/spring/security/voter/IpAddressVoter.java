package de.hybris.platform.spring.security.voter;

import java.util.Collection;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class IpAddressVoter implements AccessDecisionVoter<Object>
{
    public static final String IP_PREFIX = "IP_";
    public static final String IP_LOCAL_HOST = "IP_LOCAL_HOST";


    public boolean supports(ConfigAttribute attribute)
    {
        return (attribute.getAttribute() != null && attribute.getAttribute().startsWith("IP_"));
    }


    public boolean supports(Class clazz)
    {
        return true;
    }


    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> config)
    {
        if(!(authentication.getDetails() instanceof WebAuthenticationDetails))
        {
            return -1;
        }
        WebAuthenticationDetails details = (WebAuthenticationDetails)authentication.getDetails();
        String address = details.getRemoteAddress();
        int result = 0;
        for(ConfigAttribute attribute : config)
        {
            if(supports(attribute))
            {
                result = -1;
                if("IP_LOCAL_HOST".equals(attribute.getAttribute()))
                {
                    if("127.0.0.1".equals(address))
                    {
                        return 1;
                    }
                }
            }
        }
        return result;
    }
}
