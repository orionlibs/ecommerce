package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultAdvancedContentBrowser;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import java.util.Set;
import org.apache.log4j.Logger;

public class CmsPageContentBrowser extends DefaultAdvancedContentBrowser
{
    private static final Logger LOG = Logger.getLogger(CmsPageContentBrowser.class);
    protected static final String PERSP_TAG = "persp";
    protected static final String EVENTS_TAG = "events";
    protected static final String LIVE_EDIT_PERSPECTIVE_ID = "cmscockpit.perspective.liveedit";
    protected static final String LIVE_EDIT_PAGE_NAVIGATION_EVENT = "liveeditpagenavigation";
    protected static final String LIVE_EDIT_SITE = "live-site";
    protected static final String LIVE_EDIT_CATALOG = "live-catalog";
    protected static final String LIVE_EDIT_PAGE = "live-page";


    public SectionBrowserModel getModel()
    {
        return (SectionBrowserModel)super.getModel();
    }


    public void updateViewMode()
    {
        updateCaption();
        super.updateViewMode();
    }


    public void setModel(BrowserModel model)
    {
        if(model instanceof SectionBrowserModel)
        {
            super.setModel(model);
        }
        else
        {
            throw new IllegalArgumentException("Browser model must be a section browser model.");
        }
    }


    protected AbstractBrowserComponent createCaptionComponent()
    {
        return (AbstractBrowserComponent)new Object(this, (BrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    protected AbstractBrowserComponent createToolbarComponent()
    {
        CmsPageToolbarBrowserComponent cmsPageToolbarBrowserComponent;
        AbstractBrowserComponent ret = null;
        if(getModel() instanceof CmsPageBrowserModel)
        {
            cmsPageToolbarBrowserComponent = new CmsPageToolbarBrowserComponent((CmsPageBrowserModel)getModel(), (AbstractContentBrowser)this);
        }
        return (AbstractBrowserComponent)cmsPageToolbarBrowserComponent;
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties, Object reason)
    {
        super.updateItem(item, modifiedProperties, reason);
        if(getModel() instanceof CmsPageBrowserModel)
        {
            updateToolbar();
        }
    }


    public void updateStatusBar()
    {
    }
}
