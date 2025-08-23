package de.hybris.platform.webservicescommons.oauth2;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class HybrisOauth2UserFilter implements Filter
{
    @Resource(name = "userService")
    private UserService userService;


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null)
        {
            if(auth instanceof OAuth2Authentication && !((OAuth2Authentication)auth).isClientOnly())
            {
                UserModel userModel = this.userService.getUserForUID((String)auth.getPrincipal());
                this.userService.setCurrentUser(userModel);
            }
        }
        chain.doFilter(request, response);
    }


    public void destroy()
    {
    }


    public void init(FilterConfig arg0) throws ServletException
    {
    }
}
