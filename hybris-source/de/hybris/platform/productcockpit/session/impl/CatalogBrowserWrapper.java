package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;

public class CatalogBrowserWrapper extends AbstractBrowserWrapper
{
    public CatalogBrowserWrapper(BrowserModel browser)
    {
        super(browser);
    }


    public CatalogBrowserWrapper(BrowserModel browser, TypedObject wrapItem)
    {
        super(browser, wrapItem);
    }


    public Boolean hasNext()
    {
        if(this.browser != null)
        {
            return Boolean.valueOf((this.browser.getItems().size() - 1 > this.positionInBrowser));
        }
        return Boolean.FALSE;
    }


    protected int getItemPositionInResult(TypedObject wrapItem)
    {
        return getItemPositionInCurrentPage(wrapItem);
    }


    protected int getItemPositionInCurrentPage(TypedObject wrapItem)
    {
        int index = 0;
        if(this.browser != null)
        {
            Object wrappedItemObject = wrapItem.getObject();
            for(TypedObject it : this.browser.getItems())
            {
                if(it.getObject().equals(wrappedItemObject))
                {
                    return index;
                }
                index++;
            }
        }
        return 0;
    }


    public BrowserModel getBrowser()
    {
        return this.browser;
    }


    public void setBrowser(BrowserModel browser)
    {
        this.browser = browser;
    }


    public int getPositionInBrowser()
    {
        return this.positionInBrowser;
    }


    public void setPositionInBrowser(int positionInBrowser)
    {
        this.positionInBrowser = positionInBrowser;
    }
}
