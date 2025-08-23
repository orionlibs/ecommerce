package de.hybris.platform.spring.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CoreUserDetails extends User
{
    private final String languageISO;


    public CoreUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities, String iso) throws IllegalArgumentException
    {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.languageISO = iso;
    }


    public String getLanguageISO()
    {
        return this.languageISO;
    }
}
