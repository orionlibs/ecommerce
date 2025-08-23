package de.hybris.platform.spring.security;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import java.util.Collections;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

public class CoreAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware
{
    private static final Logger LOG = Logger.getLogger(CoreAuthenticationProvider.class.getName());
    private UserDetailsService userDetailsService;
    private UserDetailsChecker preAuthenticationChecks;
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private final UserDetailsChecker postAuthenticationChecks = (UserDetailsChecker)new DefaultPostAuthenticationChecks(this);


    public void setMessageSource(MessageSource messageSource)
    {
        this.messages = new MessageSourceAccessor(messageSource);
    }


    public final void afterPropertiesSet() throws Exception
    {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }


    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks)
    {
        this.preAuthenticationChecks = preAuthenticationChecks;
    }


    public UserDetailsChecker getPreAuthenticationChecks()
    {
        if(this.preAuthenticationChecks == null)
        {
            return (UserDetailsChecker)new AccountStatusUserDetailsChecker();
        }
        return this.preAuthenticationChecks;
    }


    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        if(Registry.hasCurrentTenant() && JaloConnection.getInstance().isSystemInitialized())
        {
            String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
            UserDetails userDetails = null;
            try
            {
                userDetails = retrieveUser(username);
            }
            catch(UsernameNotFoundException notFound)
            {
                throw new BadCredentialsException(this.messages
                                .getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"), notFound);
            }
            getPreAuthenticationChecks().check(userDetails);
            User user = UserManager.getInstance().getUserByLogin(userDetails.getUsername());
            Object credential = authentication.getCredentials();
            if(credential instanceof String)
            {
                if(!user.checkPassword((String)credential))
                {
                    throw new BadCredentialsException(this.messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
                }
            }
            else if(credential instanceof LoginToken)
            {
                if(!user.checkPassword((LoginToken)credential))
                {
                    throw new BadCredentialsException(this.messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
                }
            }
            else
            {
                throw new BadCredentialsException(this.messages
                                .getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
            }
            additionalAuthenticationChecks(userDetails, (AbstractAuthenticationToken)authentication);
            this.postAuthenticationChecks.check(userDetails);
            JaloSession.getCurrentSession().setUser(user);
            return createSuccessAuthentication(authentication, userDetails);
        }
        return createSuccessAuthentication(authentication, (UserDetails)new CoreUserDetails("systemNotInitialized", "systemNotInitialized", true, false, true, true, Collections.EMPTY_LIST, null));
    }


    public UserDetailsService getUserDetailsService()
    {
        return this.userDetailsService;
    }


    @Required
    public void setUserDetailsService(UserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }


    protected void additionalAuthenticationChecks(UserDetails details, AbstractAuthenticationToken authentication) throws AuthenticationException
    {
    }


    public boolean supports(Class<?> authentication)
    {
        return (RememberMeAuthenticationToken.class.isAssignableFrom(authentication) || UsernamePasswordAuthenticationToken.class
                        .isAssignableFrom(authentication));
    }


    protected final UserDetails retrieveUser(String username) throws AuthenticationException
    {
        UserDetails loadedUser;
        try
        {
            loadedUser = getUserDetailsService().loadUserByUsername(username);
        }
        catch(DataAccessException repositoryProblem)
        {
            throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }
        if(loadedUser == null)
        {
            throw new AuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }


    protected Authentication createSuccessAuthentication(Authentication authentication, UserDetails user)
    {
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user.getUsername(), authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return (Authentication)result;
    }
}
