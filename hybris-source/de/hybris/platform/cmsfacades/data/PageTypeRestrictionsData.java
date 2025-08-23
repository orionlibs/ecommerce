package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;

public class PageTypeRestrictionsData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageUid;
    private List<String> validComponentTypes;


    public void setPageUid(String pageUid)
    {
        this.pageUid = pageUid;
    }


    public String getPageUid()
    {
        return this.pageUid;
    }


    public void setValidComponentTypes(List<String> validComponentTypes)
    {
        this.validComponentTypes = validComponentTypes;
    }


    public List<String> getValidComponentTypes()
    {
        return this.validComponentTypes;
    }
}
