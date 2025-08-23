package de.hybris.platform.cmscockpit.components.sectionpanel.renderer;

import de.hybris.platform.cockpit.components.navigationarea.renderer.DefaultSectionSelectorSectionRenderer;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import org.apache.log4j.Logger;
import org.zkoss.zul.ListitemRenderer;

public class CatalogVersionSectionSelectorSectionRenderer extends DefaultSectionSelectorSectionRenderer
{
    private static final Logger LOG = Logger.getLogger(CatalogVersionSectionSelectorSectionRenderer.class.getName());
    private SynchronizationService synchronizationService;
    protected static final String CATALOG_VERSION_SELECTOR_CAPTION_BUTTON = "catalogVersionSelectorCaptionButton";
    protected static final String PERSP_TAG = "persp";
    protected static final String EVENTS_TAG = "events";
    protected static final String LIVE_EDIT_PERSPECTIVE_ID = "cmscockpit.perspective.liveedit";
    protected static final String CMS_NAVIGATION_EVENT = "cmsnavigation";
    protected static final String CMS_NAV_SITE = "nav-site";
    protected static final String CMS_NAV_CATALOG = "nav-catalog";
    protected static final String TITLE = "title";
    private CatalogVersionSelectorListRenderer listItemRenderer = null;


    public ListitemRenderer getListRenderer()
    {
        if(this.listItemRenderer == null)
        {
            this.listItemRenderer = new CatalogVersionSelectorListRenderer(this);
        }
        return (ListitemRenderer)this.listItemRenderer;
    }


    public SynchronizationService getSynchronizationService()
    {
        return this.synchronizationService;
    }


    public void setSynchronizationService(SynchronizationService synchronizationService)
    {
        this.synchronizationService = synchronizationService;
    }
}
