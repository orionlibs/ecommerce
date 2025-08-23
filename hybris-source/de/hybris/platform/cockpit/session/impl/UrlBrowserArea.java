package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.session.AdvancedBrowserModelListener;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class UrlBrowserArea extends AbstractBrowserArea
{
    private AdvancedBrowserModelListener browserListener = null;
    private String browserUid;
    private List<UrlMainAreaComponentFactory> viewModes = null;
    private boolean wideScreen = false;
    private boolean wideToggleNeeded = true;


    public void update()
    {
        super.update();
        if(this.wideScreen && this.wideToggleNeeded)
        {
            UICockpitPerspective currentPersp = UISessionUtils.getCurrentSession().getCurrentPerspective();
            if(currentPersp instanceof BaseUICockpitPerspective)
            {
                ((BaseUICockpitPerspective)currentPersp).toggleNavAndEditArea();
            }
            this.wideToggleNeeded = false;
        }
    }


    public BrowserModelListener getBrowserListener()
    {
        if(this.browserListener == null)
        {
            this.browserListener = (AdvancedBrowserModelListener)new MyBrowserModelListener(this);
        }
        return (BrowserModelListener)this.browserListener;
    }


    public void saveQuery(BrowserModel browserModel)
    {
    }


    public BrowserModel createNewDefaultBrowser()
    {
        return (BrowserModel)new DefaultUrlBrowserModel(this.viewModes);
    }


    public boolean providesDefaultBrowser()
    {
        return true;
    }


    public String getBrowserUid()
    {
        return this.browserUid;
    }


    @Required
    public void setBrowserUid(String browserUid)
    {
        this.browserUid = browserUid;
    }


    public List<UrlMainAreaComponentFactory> getViewModes()
    {
        return (this.viewModes == null) ? Collections.EMPTY_LIST : Collections.<UrlMainAreaComponentFactory>unmodifiableList(this.viewModes);
    }


    public void setViewModes(List<UrlMainAreaComponentFactory> viewModes)
    {
        this.viewModes = viewModes;
    }


    protected AdvancedBrowserModelListener getBrowserModelListener()
    {
        if(this.browserListener == null)
        {
            this.browserListener = (AdvancedBrowserModelListener)new MyBrowserModelListener(this);
        }
        return this.browserListener;
    }


    public boolean isWideScreen()
    {
        return this.wideScreen;
    }


    public void setWideScreen(boolean wideScreen)
    {
        this.wideScreen = wideScreen;
    }
}
