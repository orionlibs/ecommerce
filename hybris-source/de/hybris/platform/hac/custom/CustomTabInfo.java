package de.hybris.platform.hac.custom;

import java.util.List;

public class CustomTabInfo
{
    private String basePath;
    private String mainTabLabel;
    private List<SubTabInfo> subTabs;


    public String getMainTabLabel()
    {
        return this.mainTabLabel;
    }


    public void setMainTabLabel(String mainTabLabel)
    {
        this.mainTabLabel = mainTabLabel;
    }


    public List<SubTabInfo> getSubTabs()
    {
        return this.subTabs;
    }


    public void setSubTabs(List<SubTabInfo> subTabs)
    {
        this.subTabs = subTabs;
    }


    public String getBasePath()
    {
        return this.basePath;
    }


    public void setBasePath(String basePath)
    {
        this.basePath = basePath;
    }


    public CustomTabInfo basePath(String basePath)
    {
        setBasePath(basePath);
        return this;
    }


    public CustomTabInfo mainTabLabel(String mainTabLabel)
    {
        setMainTabLabel(mainTabLabel);
        return this;
    }


    public CustomTabInfo subTabs(List<SubTabInfo> subTabs)
    {
        setSubTabs(subTabs);
        return this;
    }
}
