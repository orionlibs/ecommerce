package de.hybris.platform.oauth2.filter;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

public class SessionInvalidateFilter extends GenericFilterBean
{
    private static final Logger log = LoggerFactory.getLogger(SessionInvalidateFilter.class);
    private Set<String> includeUris = Collections.emptySet();
    private Set<Pattern> includeUriPatterns = Collections.emptySet();


    public SessionInvalidateFilter(Set<String> includeUris)
    {
        this.includeUris = includeUris;
    }


    @PostConstruct
    public void init()
    {
        Preconditions.checkNotNull(this.includeUris, "includeUris must not be null");
        this.includeUris.forEach(uri -> log.info("invalidate session for uri:{}", uri));
        this.includeUriPatterns = (Set<Pattern>)this.includeUris.stream().map(Pattern::compile).collect(Collectors.toSet());
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        try
        {
            String uri;
            chain.doFilter(request, response);
        }
        finally
        {
            String uri = ((HttpServletRequest)request).getRequestURI();
            this.includeUriPatterns.stream()
                            .map(pattern -> Boolean.valueOf(pattern.matcher(uri).matches()))
                            .filter(a -> a.booleanValue())
                            .findFirst()
                            .map(res -> ((HttpServletRequest)request).getSession(false))
                            .ifPresent(HttpSession::invalidate);
        }
    }


    public void setIncludeUris(Set<String> includeUris)
    {
        this.includeUris = includeUris;
    }


    public SessionInvalidateFilter()
    {
    }
}
