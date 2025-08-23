package de.hybris.platform.spring.security;

import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;

public class RejectUserPreAuthenticationChecks extends AccountStatusUserDetailsChecker
{
    private static final Logger LOG = Logger.getLogger(RejectUserPreAuthenticationChecks.class.getName());
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private Collection<String> allowedUserTypes;
    private Collection<String> allowedUserGroups;


    public void setAllowedUserTypes(Collection<String> allowedUserTypes)
    {
        if(allowedUserTypes != null)
        {
            this.allowedUserTypes = allowedUserTypes;
        }
        else
        {
            this.allowedUserTypes = Collections.EMPTY_SET;
        }
    }


    public void setAllowedUserGroups(Collection<String> allowedUserGroups)
    {
        if(allowedUserGroups != null)
        {
            Set<String> idsLowerCase = new HashSet<>(allowedUserGroups.size() * 2);
            for(String id : allowedUserGroups)
            {
                idsLowerCase.add(id.toLowerCase());
            }
            this.allowedUserGroups = idsLowerCase;
        }
        else
        {
            this.allowedUserGroups = Collections.EMPTY_SET;
        }
    }


    public void check(UserDetails userDetails)
    {
        super.check(userDetails);
        User user = UserManager.getInstance().getUserByLogin(userDetails.getUsername());
        checkUserType(user);
        checkForAllowedGroups(user);
    }


    protected void checkForAllowedGroups(User user)
    {
        if(CollectionUtils.isNotEmpty(this.allowedUserGroups))
        {
            for(PrincipalGroup group : user.getAllGroups())
            {
                if(this.allowedUserGroups.contains(group.getUid().toLowerCase()))
                {
                    return;
                }
            }
            throw new DisabledException(this.messages.getMessage("CoreAuthenticationProvider.usergroup.rejected", "Login attempt rejected (group restriction)"));
        }
    }


    protected void checkUserType(User user)
    {
        if(CollectionUtils.isNotEmpty(this.allowedUserTypes))
        {
            for(String userType : this.allowedUserTypes)
            {
                ComposedType allowedType = getComposedType(userType);
                if(allowedType != null)
                {
                    if(allowedType.isAssignableFrom((Type)user.getComposedType()))
                    {
                        return;
                    }
                }
            }
            throw new DisabledException(this.messages.getMessage("CoreAuthenticationProvider.usertype.rejected", "Login attempt rejected (type restriction)"));
        }
    }


    protected ComposedType getComposedType(String code)
    {
        try
        {
            return TypeManager.getInstance().getComposedType(code);
        }
        catch(Exception e)
        {
            LOG.error("invalid type (" + code + ") defined in spring-security configuration (see: rejectedUserType)");
            return null;
        }
    }
}
