package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PageableBrowserModel;

public class ProductBrowserWrapper extends AbstractBrowserWrapper
{
    public ProductBrowserWrapper(BrowserModel browser)
    {
        super(browser);
    }


    public ProductBrowserWrapper(BrowserModel browser, TypedObject wrapItem)
    {
        super(browser, wrapItem);
    }


    public Boolean hasNext()
    {
        if(this.browser != null)
        {
            return Boolean.valueOf((this.browser.getTotalCount() - 1 > this.positionInBrowser));
        }
        return Boolean.FALSE;
    }


    protected int getItemPositionInResult(TypedObject wrapItem)
    {
        int index = getItemPositionInCurrentPage(wrapItem);
        if(this.browser instanceof PageableBrowserModel)
        {
            index += ((PageableBrowserModel)this.browser).getCurrentPage() * ((PageableBrowserModel)this.browser).getPageSize();
        }
        return index;
    }


    protected int getItemPositionInCurrentPage(TypedObject wrapItem)
    {
        int index = 0;
        if(this.browser != null)
        {
            Object wrappedObject = wrapItem.getObject();
            for(TypedObject it : this.browser.getItems())
            {
                if(it.getObject().equals(wrappedObject))
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
