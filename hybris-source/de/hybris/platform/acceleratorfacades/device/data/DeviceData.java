package de.hybris.platform.acceleratorfacades.device.data;

import java.io.Serializable;
import java.util.Map;

public class DeviceData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String userAgent;
    private Map<String, String> capabilities;
    private Boolean desktopBrowser;
    private Boolean mobileBrowser;
    private Boolean tabletBrowser;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }


    public String getUserAgent()
    {
        return this.userAgent;
    }


    public void setCapabilities(Map<String, String> capabilities)
    {
        this.capabilities = capabilities;
    }


    public Map<String, String> getCapabilities()
    {
        return this.capabilities;
    }


    public void setDesktopBrowser(Boolean desktopBrowser)
    {
        this.desktopBrowser = desktopBrowser;
    }


    public Boolean getDesktopBrowser()
    {
        return this.desktopBrowser;
    }


    public void setMobileBrowser(Boolean mobileBrowser)
    {
        this.mobileBrowser = mobileBrowser;
    }


    public Boolean getMobileBrowser()
    {
        return this.mobileBrowser;
    }


    public void setTabletBrowser(Boolean tabletBrowser)
    {
        this.tabletBrowser = tabletBrowser;
    }


    public Boolean getTabletBrowser()
    {
        return this.tabletBrowser;
    }
}
