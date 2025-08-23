package de.hybris.platform.acceleratorservices.email.data;

import java.io.Serializable;
import java.util.List;

public class EmailPageData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageUid;
    private List<String> jsPaths;
    private List<String> cssPaths;


    public void setPageUid(String pageUid)
    {
        this.pageUid = pageUid;
    }


    public String getPageUid()
    {
        return this.pageUid;
    }


    public void setJsPaths(List<String> jsPaths)
    {
        this.jsPaths = jsPaths;
    }


    public List<String> getJsPaths()
    {
        return this.jsPaths;
    }


    public void setCssPaths(List<String> cssPaths)
    {
        this.cssPaths = cssPaths;
    }


    public List<String> getCssPaths()
    {
        return this.cssPaths;
    }
}
