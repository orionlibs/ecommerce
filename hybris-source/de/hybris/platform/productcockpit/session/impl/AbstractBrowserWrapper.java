package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public abstract class AbstractBrowserWrapper
{
    private static final Logger log = LoggerFactory.getLogger(AbstractBrowserWrapper.class);
    protected BrowserModel browser = null;
    protected int positionInBrowser;
    private TypeService typeService;


    public AbstractBrowserWrapper(BrowserModel browser)
    {
        try
        {
            if(isBrowserClonable(browser))
            {
                this.browser = (BrowserModel)browser.clone();
            }
        }
        catch(CloneNotSupportedException e)
        {
            log.error("Could not create a clone of the browser.", e);
        }
        this.positionInBrowser = 0;
    }


    public AbstractBrowserWrapper(BrowserModel browser, TypedObject wrapItem)
    {
        try
        {
            if(isBrowserClonable(browser))
            {
                this.browser = (BrowserModel)browser.clone();
            }
        }
        catch(CloneNotSupportedException e)
        {
            log.error("Could not create a clone of the browser.", e);
        }
        this.positionInBrowser = getItemPositionInResult(wrapItem);
    }


    public TypedObject getNextItem()
    {
        boolean itemFound = false;
        while(!itemFound && hasNext().booleanValue())
        {
            if(hasNext().booleanValue())
            {
                this.positionInBrowser++;
                TypedObject typedItem = this.browser.getItem(getPositionInBrowser());
                itemFound = getTypeService().checkItemAlive(typedItem);
                if(itemFound)
                {
                    return typedItem;
                }
            }
        }
        return null;
    }


    public TypedObject getPreviousItem()
    {
        boolean itemFound = false;
        while(!itemFound && hasPrevious().booleanValue())
        {
            if(hasPrevious().booleanValue())
            {
                this.positionInBrowser--;
                TypedObject typedItem = this.browser.getItem(getPositionInBrowser());
                itemFound = getTypeService().checkItemAlive(typedItem);
                if(itemFound)
                {
                    return typedItem;
                }
            }
        }
        return null;
    }


    public abstract Boolean hasNext();


    public Boolean hasPrevious()
    {
        return Boolean.valueOf((this.positionInBrowser > 0));
    }


    protected abstract int getItemPositionInResult(TypedObject paramTypedObject);


    protected abstract int getItemPositionInCurrentPage(TypedObject paramTypedObject);


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


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }


    protected boolean isBrowserClonable(BrowserModel browser)
    {
        return !(browser instanceof de.hybris.platform.cockpit.session.impl.TaskBrowserModel);
    }
}
