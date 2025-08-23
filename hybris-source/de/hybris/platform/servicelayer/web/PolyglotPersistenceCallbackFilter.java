package de.hybris.platform.servicelayer.web;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

public class PolyglotPersistenceCallbackFilter extends GenericFilterBean
{
    private final List<PolyglotCallback> polyglotCallbacks;


    public PolyglotPersistenceCallbackFilter(List<PolyglotCallback> polyglotCallbacks)
    {
        this.polyglotCallbacks = Objects.<List<PolyglotCallback>>requireNonNull(polyglotCallbacks);
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        if(this.polyglotCallbacks.isEmpty())
        {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        getRootCallbackCaller(filterChain).call(servletRequest, servletResponse);
    }


    private CallbackCaller getRootCallbackCaller(FilterChain filterChain)
    {
        PolyglotCallbackCaller polyglotCallbackCaller;
        FilterChainCaller filterChainCaller = new FilterChainCaller(filterChain);
        for(int i = this.polyglotCallbacks.size() - 1; i >= 0; i--)
        {
            polyglotCallbackCaller = new PolyglotCallbackCaller((CallbackCaller)filterChainCaller, this.polyglotCallbacks.get(i));
        }
        return (CallbackCaller)polyglotCallbackCaller;
    }
}
