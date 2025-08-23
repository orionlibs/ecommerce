package de.hybris.platform.servicelayer.web;

import de.hybris.platform.util.collections.CaseInsensitiveStringMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XSSRequestWrapper extends HttpServletRequestWrapper
{
    private final XSSFilter.XSSValueTranslator lazyValueTranslator;
    private Boolean noParametersStripped = null;
    private Map<String, String[]> strippedParametersMap = null;
    private Boolean noHeadersStripped = null;
    private Map<String, String[]> strippedHeadersMap = null;


    public XSSRequestWrapper(HttpServletRequest servletRequest, XSSFilter.XSSValueTranslator translator)
    {
        super(servletRequest);
        this.lazyValueTranslator = translator;
    }


    public XSSRequestWrapper(HttpServletRequest servletRequest, Map<String, String[]> strippedHeadersMap, Map<String, String[]> strippedParametersMap)
    {
        super(servletRequest);
        this.lazyValueTranslator = null;
        this.strippedHeadersMap = strippedHeadersMap;
        this.strippedParametersMap = strippedParametersMap;
    }


    public String[] getParameterValues(String parameter)
    {
        if(parametersAreClean())
        {
            return super.getParameterValues(parameter);
        }
        return (String[])getParameterMap().get(parameter);
    }


    public String getParameter(String parameter)
    {
        if(parametersAreClean())
        {
            return super.getParameter(parameter);
        }
        String[] parameterValues = getParameterValues(parameter);
        return (parameterValues == null || parameterValues.length == 0) ? null : parameterValues[0];
    }


    public Map getParameterMap()
    {
        return parametersAreClean() ? super.getParameterMap() : this.strippedParametersMap;
    }


    protected boolean parametersAreClean()
    {
        ensureParametersTranslated();
        return Boolean.TRUE.equals(this.noParametersStripped);
    }


    protected void ensureParametersTranslated()
    {
        if(this.noParametersStripped == null)
        {
            Map<String, String[]> originalNoCaseInsensitiveMap = super.getParameterMap();
            Map<String, String[]> stripped = this.lazyValueTranslator.translateParameters(originalNoCaseInsensitiveMap);
            if(originalNoCaseInsensitiveMap == stripped)
            {
                this.noParametersStripped = Boolean.TRUE;
            }
            else
            {
                this.noParametersStripped = Boolean.FALSE;
                this.strippedParametersMap = stripped;
            }
        }
    }


    public String getHeader(String name)
    {
        if(headersAreClean())
        {
            return super.getHeader(name);
        }
        String[] strippedHeaderValues = this.strippedHeadersMap.get(name);
        return (strippedHeaderValues == null || strippedHeaderValues.length == 0) ? null : strippedHeaderValues[0];
    }


    public Enumeration getHeaders(String name)
    {
        if(headersAreClean())
        {
            return super.getHeaders(name);
        }
        String[] strippedHeaderValues = this.strippedHeadersMap.get(name);
        if(strippedHeaderValues == null || strippedHeaderValues.length == 0)
        {
            return Collections.emptyEnumeration();
        }
        return Collections.enumeration(Arrays.asList((Object[])strippedHeaderValues));
    }


    protected void ensureHeadersTranslated()
    {
        if(headersAlreadyTranslated())
        {
            return;
        }
        Map<String, String[]> headerValueMap = XSSFilter.getValueMapFromHeaders((HttpServletRequest)getRequest());
        Map<String, String[]> stripped = this.lazyValueTranslator.translateHeaders(headerValueMap);
        if(stripped == headerValueMap)
        {
            this.noHeadersStripped = Boolean.TRUE;
        }
        else
        {
            this.noHeadersStripped = Boolean.FALSE;
            this.strippedHeadersMap = (Map<String, String[]>)new CaseInsensitiveStringMap(stripped);
        }
    }


    protected boolean headersAlreadyTranslated()
    {
        return (this.noHeadersStripped != null);
    }


    protected boolean headersAreClean()
    {
        ensureHeadersTranslated();
        return Boolean.TRUE.equals(this.noHeadersStripped);
    }
}
