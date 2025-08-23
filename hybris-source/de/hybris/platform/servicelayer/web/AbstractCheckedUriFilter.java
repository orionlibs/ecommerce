package de.hybris.platform.servicelayer.web;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

public abstract class AbstractCheckedUriFilter extends GenericFilterBean
{
    private List<String> urlPaths;


    protected boolean isUrlPathsContainsUri(String uri)
    {
        if(StringUtils.isNotBlank(uri))
        {
            for(String excludedUrl : getUrlPaths())
            {
                if(uri.contains(excludedUrl))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public List<String> getUrlPaths()
    {
        return (this.urlPaths == null) ? new ArrayList<>() : this.urlPaths;
    }


    public void setUrlPaths(List<String> urlPaths)
    {
        this.urlPaths = urlPaths;
    }
}
