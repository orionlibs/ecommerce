package de.hybris.platform.cockpit.model.browser.impl;

import de.hybris.platform.cockpit.model.browser.BrowserModelFactory;
import de.hybris.platform.cockpit.session.BrowserModel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class DefaultBrowserModelFactory implements BrowserModelFactory
{
    private ApplicationContext appContext;


    public BrowserModel createBrowserModel(String name)
    {
        BrowserModel browser = (BrowserModel)this.appContext.getBean(name);
        if(browser == null)
        {
            return null;
        }
        return browser;
    }


    public void initBrowserModelsMappings()
    {
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.appContext = applicationContext;
    }
}
