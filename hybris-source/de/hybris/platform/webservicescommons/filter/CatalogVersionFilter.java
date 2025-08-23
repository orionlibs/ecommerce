package de.hybris.platform.webservicescommons.filter;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.util.YSanitizer;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;

public class CatalogVersionFilter extends OncePerRequestFilter
{
    private static final Logger LOG = LoggerFactory.getLogger(CatalogVersionFilter.class);
    private CatalogVersionService catalogVersionService;
    private String catalogRegexp;
    private String catalogVersionRegexp;
    private String enabledRegexp;


    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException
    {
        if(isEnabled(servletRequest))
        {
            process(servletRequest);
        }
        else
        {
            this.catalogVersionService.setSessionCatalogVersions(this.catalogVersionService.getAllCatalogVersions());
        }
        filterChain.doFilter((ServletRequest)servletRequest, (ServletResponse)servletResponse);
    }


    protected void process(HttpServletRequest servletRequest)
    {
        String catalog = find(servletRequest, this.catalogRegexp);
        String version = find(servletRequest, this.catalogVersionRegexp);
        if(StringUtils.isNotEmpty(catalog) && StringUtils.isNotEmpty(version))
        {
            try
            {
                this.catalogVersionService.setSessionCatalogVersion(catalog, version);
            }
            catch(UnknownIdentifierException e)
            {
                LOG.warn(String.format("Catalog not found for catalog (%s) and given version (%s). All catalog versions will be set in the session", new Object[] {YSanitizer.sanitize(catalog), YSanitizer.sanitize(version)}));
                this.catalogVersionService.setSessionCatalogVersions(this.catalogVersionService.getAllCatalogVersions());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Catalog not found for catalog (%s) and given version (%s)", new Object[] {YSanitizer.sanitize(catalog),
                                    YSanitizer.sanitize(version)}), (Throwable)e);
                }
            }
        }
        else
        {
            if(StringUtils.isEmpty(catalog) && StringUtils.isEmpty(version))
            {
                throw new BadRequestException("Invalid catalog and catalog version in request path");
            }
            if(StringUtils.isEmpty(catalog))
            {
                throw new BadRequestException("Invalid catalog in request path");
            }
            if(StringUtils.isEmpty(version))
            {
                throw new BadRequestException("Invalid catalog version in request path");
            }
        }
    }


    protected boolean isEnabled(HttpServletRequest servletRequest)
    {
        String find = find(servletRequest, this.enabledRegexp);
        return (find != null);
    }


    protected String find(HttpServletRequest servletRequest, String pattern)
    {
        String pathInfo = servletRequest.getPathInfo();
        pathInfo = (pathInfo == null) ? "" : pathInfo;
        String servletPath = servletRequest.getServletPath();
        servletPath = (servletPath == null) ? "" : servletPath;
        return find(servletPath + servletPath, pattern);
    }


    protected String find(String input, String regexp)
    {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(input);
        if(matcher.find() && matcher.groupCount() > 0)
        {
            return matcher.group(1);
        }
        return null;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    @Required
    public void setCatalogRegexp(String catalogRegexp)
    {
        if(StringUtils.isEmpty(catalogRegexp))
        {
            throw new IllegalArgumentException("catalogRegexp can't be empty");
        }
        this.catalogRegexp = catalogRegexp;
    }


    @Required
    public void setCatalogVersionRegexp(String catalogVersionRegexp)
    {
        if(StringUtils.isEmpty(catalogVersionRegexp))
        {
            throw new IllegalArgumentException("catalogVersionRegexp can't be empty");
        }
        this.catalogVersionRegexp = catalogVersionRegexp;
    }


    @Required
    public void setEnabledRegexp(String enabledRegexp)
    {
        if(StringUtils.isEmpty(enabledRegexp))
        {
            throw new IllegalArgumentException("enabledRegexp can't be empty");
        }
        this.enabledRegexp = enabledRegexp;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    protected String getCatalogRegexp()
    {
        return this.catalogRegexp;
    }


    protected String getCatalogVersionRegexp()
    {
        return this.catalogVersionRegexp;
    }


    protected String getEnabledRegexp()
    {
        return this.enabledRegexp;
    }
}
