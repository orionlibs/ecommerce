package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.cockpit.model.browser.BrowserModelFactory;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.cockpit.session.impl.BrowserAreaListener;
import de.hybris.platform.productcockpit.session.ProductBrowserAreaListener;
import java.util.Collection;
import org.zkoss.spring.SpringUtil;

public class BrowserArea extends AbstractBrowserArea
{
    private MyBrowserListener browserListener;


    public boolean isSaveQueryAvailable()
    {
        return true;
    }


    public void saveQuery(BrowserModel b)
    {
        fireBrowserQuerySaved(b);
    }


    public BrowserModelListener getBrowserListener()
    {
        if(this.browserListener == null)
        {
            this.browserListener = new MyBrowserListener(this, this);
        }
        return (BrowserModelListener)this.browserListener;
    }


    protected void fireBlacklistItems(BrowserModel b, Collection<Integer> indexes)
    {
        for(BrowserAreaListener bal : getBrowserAreaListeners())
        {
            if(bal instanceof ProductBrowserAreaListener)
            {
                ((ProductBrowserAreaListener)bal).blacklistItems(b, indexes);
            }
        }
    }


    public BrowserModel createNewDefaultBrowser()
    {
        DefaultProductSearchBrowserModel defaultProductSearchBrowserModel;
        BrowserModel browser = null;
        if(super.providesDefaultBrowser())
        {
            browser = super.createNewDefaultBrowser();
        }
        else
        {
            BrowserModelFactory factory = (BrowserModelFactory)SpringUtil.getBean("BrowserModelFactory");
            DefaultProductSearchBrowserModel browserModel = (DefaultProductSearchBrowserModel)factory.createBrowserModel("DefaultProductSearchBrowserModel");
            browserModel.setSimpleQuery("");
            browserModel.setShowCreateButton(true);
            defaultProductSearchBrowserModel = browserModel;
        }
        return (BrowserModel)defaultProductSearchBrowserModel;
    }


    public boolean providesDefaultBrowser()
    {
        return true;
    }
}
