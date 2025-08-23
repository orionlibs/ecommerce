package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.browser.impl.DefaultExtendedSearchBrowserModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.apache.commons.lang.StringUtils;

public class DefaultSearchBrowserArea extends AbstractBrowserArea
{
    private String rootSearchTypeCode = null;
    private BrowserModelListener browserListener = null;
    private boolean extendedSearchBrowser = false;


    public BrowserModelListener getBrowserListener()
    {
        if(this.browserListener == null)
        {
            this.browserListener = (BrowserModelListener)new DefaultSearchContextBrowserModelListener(this);
        }
        return this.browserListener;
    }


    public void saveQuery(BrowserModel browserModel)
    {
        fireBrowserQuerySaved(browserModel);
    }


    public String getRootSearchTypeCode()
    {
        return this.rootSearchTypeCode;
    }


    public void setRootSearchTypeCode(String typeCode)
    {
        this.rootSearchTypeCode = typeCode;
    }


    public BrowserModel createNewDefaultBrowser()
    {
        DefaultSearchBrowserModel defaultSearchBrowserModel;
        BrowserModel browser = null;
        if(super.providesDefaultBrowser())
        {
            browser = super.createNewDefaultBrowser();
        }
        else if(StringUtils.isNotBlank(getRootSearchTypeCode()))
        {
            ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getRootSearchTypeCode());
            DefaultSearchBrowserModel browserModel = this.extendedSearchBrowser ? (DefaultSearchBrowserModel)new DefaultExtendedSearchBrowserModel(objectTemplate, true, true) : new DefaultSearchBrowserModel(objectTemplate);
            browserModel.setSimpleQuery("");
            defaultSearchBrowserModel = browserModel;
        }
        return (BrowserModel)defaultSearchBrowserModel;
    }


    public boolean providesDefaultBrowser()
    {
        return (super.providesDefaultBrowser() || StringUtils.isNotBlank(getRootSearchTypeCode()));
    }


    public void setExtendedSearchBrowser(boolean extendedSearchBrowser)
    {
        this.extendedSearchBrowser = extendedSearchBrowser;
    }
}
