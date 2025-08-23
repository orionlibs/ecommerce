package de.hybris.platform.mediaweb;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Deprecated(since = "6.1.0", forRemoval = true)
public class MediaNotAuthorizedFilter implements Filter
{
    private final de.hybris.platform.servicelayer.web.MediaNotAuthorizedFilter delegate = new de.hybris.platform.servicelayer.web.MediaNotAuthorizedFilter();


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        this.delegate.doFilter(request, response, chain);
    }


    public void destroy()
    {
        this.delegate.destroy();
    }


    public void init(FilterConfig config) throws ServletException
    {
        this.delegate.init(config);
    }
}
