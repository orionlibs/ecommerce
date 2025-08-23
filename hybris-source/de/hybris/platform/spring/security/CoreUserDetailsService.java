package de.hybris.platform.spring.security;

import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.util.Config;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CoreUserDetailsService implements UserDetailsService
{
    private static final Logger log = Logger.getLogger(CoreUserDetailsService.class.getName());
    private String rolePrefix = "ROLE_";


    public CoreUserDetails loadUserByUsername(String username)
    {
        User user;
        if(username == null)
        {
            return null;
        }
        try
        {
            user = UserManager.getInstance().getUserByLogin(username);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UsernameNotFoundException("User not found!");
        }
        boolean enabled = (isNotAnonymousOrAnonymousLoginIsAllowed(user) && !user.isLoginDisabledAsPrimitive() && !isAccountDeactivated(user));
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        String password = user.getEncodedPassword(JaloSession.getCurrentSession().getSessionContext());
        if(password == null)
        {
            password = "";
        }
        CoreUserDetails userDetails = new CoreUserDetails(user.getLogin(), password, enabled, true, true, true, getAuthorities(user), JaloSession.getCurrentSession().getSessionContext().getLanguage().getIsoCode());
        return userDetails;
    }


    protected boolean isNotAnonymousOrAnonymousLoginIsAllowed(User user)
    {
        return (!user.isAnonymousCustomer() || !Config.getBoolean("login.anonymous.always.disabled", true));
    }


    private boolean isAccountDeactivated(User user)
    {
        return (user.getDeactivationDate() != null && user.getDeactivationDate().toInstant().isBefore(Instant.now()));
    }


    private Collection<GrantedAuthority> getAuthorities(User user)
    {
        Set<PrincipalGroup> groups = user.getGroups();
        Collection<GrantedAuthority> authorities = new ArrayList<>(groups.size());
        Iterator<PrincipalGroup> itr = groups.iterator();
        while(itr.hasNext())
        {
            PrincipalGroup group = itr.next();
            authorities.add(new SimpleGrantedAuthority(this.rolePrefix + this.rolePrefix));
            for(PrincipalGroup gr : group.getAllGroups())
            {
                authorities.add(new SimpleGrantedAuthority(this.rolePrefix + this.rolePrefix));
            }
        }
        return authorities;
    }


    public void setRolePrefix(String rolePrefix)
    {
        this.rolePrefix = rolePrefix;
    }
}
