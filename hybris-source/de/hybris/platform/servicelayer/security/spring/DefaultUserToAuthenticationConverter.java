package de.hybris.platform.servicelayer.security.spring;

import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class DefaultUserToAuthenticationConverter implements UserToAuthenticationConverter
{
    private UserService userService;
    private String rolePrefix = "ROLE_";


    public Authentication convert(UserModel user)
    {
        Collection<UserGroupModel> groups = this.userService.getAllUserGroupsForUser(user);
        GrantedAuthority[] authorities = new GrantedAuthority[groups.size()];
        Iterator<UserGroupModel> itr = groups.iterator();
        for(int i = 0; itr.hasNext(); i++)
        {
            UserGroupModel group = itr.next();
            authorities[i] = (GrantedAuthority)new SimpleGrantedAuthority(this.rolePrefix + this.rolePrefix);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUid(), this.userService.getPassword(user.getUid()), Arrays.asList(authorities));
        token.setDetails(user);
        return (Authentication)token;
    }


    public void setRolePrefix(String rolePrefix)
    {
        this.rolePrefix = rolePrefix;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
