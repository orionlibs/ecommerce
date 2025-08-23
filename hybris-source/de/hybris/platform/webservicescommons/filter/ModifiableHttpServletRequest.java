package de.hybris.platform.webservicescommons.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ModifiableHttpServletRequest extends HttpServletRequestWrapper
{
    private final Map<String, String[]> additionalParameters;
    private Map<String, String[]> allParameters;
    private final boolean override;


    public ModifiableHttpServletRequest(HttpServletRequest request, Map<String, String[]> additionalParameters)
    {
        this(request, additionalParameters, false);
    }


    public ModifiableHttpServletRequest(HttpServletRequest request, Map<String, String[]> additionalParameters, boolean override)
    {
        super(request);
        this.additionalParameters = (Map)new ConcurrentHashMap<>();
        this.additionalParameters.putAll((Map)additionalParameters);
        this.override = override;
    }


    public Map<String, String[]> getParameterMap()
    {
        if(this.allParameters == null)
        {
            this.allParameters = (Map)new ConcurrentHashMap<>();
            if(this.override)
            {
                this.allParameters.putAll(super.getParameterMap());
                this.allParameters.putAll((Map)this.additionalParameters);
            }
            else
            {
                this.allParameters.putAll((Map)this.additionalParameters);
                this.allParameters.putAll(super.getParameterMap());
            }
        }
        return (Map)Collections.unmodifiableMap((Map)this.allParameters);
    }


    public String getParameter(String name)
    {
        String[] results = getParameterMap().get(name);
        if(results != null)
        {
            return results[0];
        }
        return null;
    }


    public Enumeration<String> getParameterNames()
    {
        return Collections.enumeration(getParameterMap().keySet());
    }


    public String[] getParameterValues(String name)
    {
        return getParameterMap().get(name);
    }
}
