package de.hybris.platform.cockpit.util;

import java.util.List;

public class CockpitThemeConfig
{
    private boolean keepZKCss = false;
    private List<String> uris;
    private List<String> ieCssUris;


    public void setKeepZKCss(boolean keepZKCss)
    {
        this.keepZKCss = keepZKCss;
    }


    public boolean isKeepZKCss()
    {
        return this.keepZKCss;
    }


    public void setUris(List<String> uris)
    {
        this.uris = uris;
    }


    public List<String> getUris()
    {
        return this.uris;
    }


    public List<String> getIeCssUris()
    {
        return this.ieCssUris;
    }


    public void setIeCssUris(List<String> ieCssUris)
    {
        this.ieCssUris = ieCssUris;
    }
}
