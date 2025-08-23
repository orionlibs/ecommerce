package com.hybris.backoffice.spring.security;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.spring.security.CoreAuthenticationProvider;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class BackofficeAuthenticationProvider extends CoreAuthenticationProvider
{
    private UserService userService;


    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        try
        {
            EmployeeModel employee = (EmployeeModel)this.userService.getUserForUID(authentication.getName(), EmployeeModel.class);
            checkBackofficeAccess(employee);
        }
        catch(ClassMismatchException e)
        {
            throw new BadCredentialsException("Bad credentials", e);
        }
        catch(IllegalArgumentException | de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException e)
        {
            throw new UsernameNotFoundException("Username not found", e);
        }
        return coreAuthenticate(authentication);
    }


    protected void checkBackofficeAccess(EmployeeModel employee) throws DisabledException
    {
        if(!this.userService.isAdmin((UserModel)employee))
        {
            Boolean disabled = employee.getBackOfficeLoginDisabled();
            if(disabled == null && CollectionUtils.isNotEmpty(employee.getGroups()))
            {
                disabled = Boolean.valueOf(checkBackofficeAccessDisabledForGroups(employee.getGroups()));
            }
            if(BooleanUtils.isNotFalse(disabled))
            {
                throw new DisabledException("User access denied");
            }
        }
    }


    private boolean checkBackofficeAccessDisabledForGroups(Collection<PrincipalGroupModel> groups)
    {
        Boolean disabled = null;
        Set<PrincipalGroupModel> parentGroups = new HashSet<>();
        for(PrincipalGroupModel group : groups)
        {
            if(group.getBackOfficeLoginDisabled() != null)
            {
                disabled = group.getBackOfficeLoginDisabled();
            }
            if(BooleanUtils.isTrue(disabled))
            {
                break;
            }
            if(CollectionUtils.isNotEmpty(group.getGroups()))
            {
                parentGroups.addAll(group.getGroups());
            }
        }
        if(disabled == null && !parentGroups.isEmpty())
        {
            disabled = Boolean.valueOf(checkBackofficeAccessDisabledForGroups(parentGroups));
        }
        return BooleanUtils.isNotFalse(disabled);
    }


    protected Authentication coreAuthenticate(Authentication authentication)
    {
        return super.authenticate(authentication);
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
