package de.hybris.platform.servicelayer.security.spring.vote;

import java.util.Collection;
import java.util.Iterator;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

public class HybrisNotAnonymousVoter implements AccessDecisionVoter<Object>
{
    public static final String HYBRIS_NOT_ANONYMOUS = "HYBRIS_NOT_ANONYMOUS";
    private String anonymousUserName = "anonymous";


    public boolean supports(ConfigAttribute attribute)
    {
        return "HYBRIS_NOT_ANONYMOUS".equals(attribute.getAttribute());
    }


    public boolean supports(Class clazz)
    {
        return true;
    }


    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> config)
    {
        Iterator<ConfigAttribute> iter = config.iterator();
        while(iter.hasNext())
        {
            ConfigAttribute attribute = iter.next();
            if(supports(attribute))
            {
                String username = (String)authentication.getPrincipal();
                if(!this.anonymousUserName.equals(username))
                {
                    return 1;
                }
                return -1;
            }
        }
        return 0;
    }


    public void setAnonymousUserName(String anonymousUserName)
    {
        this.anonymousUserName = anonymousUserName;
    }
}
