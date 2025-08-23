package de.hybris.platform.cmscockpit.components.navigationarea;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.events.impl.CmsLiveEditEvent;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserArea;
import de.hybris.platform.cockpit.components.navigationarea.AbstractSelectorSection;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "4.3", forRemoval = true)
@HybrisDeprecation(sinceVersion = "4.3")
public class CmsSiteSelectorSection extends AbstractSelectorSection
{
    private static final Logger LOG = Logger.getLogger(CmsSiteSelectorSection.class);
    private boolean initialized = false;
    private CMSAdminSiteService siteService = null;
    private CmsCockpitService cmsCockpitService;
    private ModelService modelService = null;


    public void selectionChanged()
    {
        TypedObject selectedItem = getSelectedItem();
        if(selectedItem != null)
        {
            Object object = selectedItem.getObject();
            CMSSiteModel site = null;
            if(object instanceof CMSSiteModel)
            {
                site = (CMSSiteModel)object;
            }
            if(site == null)
            {
                LOG.error("Site could not be loaded. Reason: Selected item not valid");
            }
            else if(getCmsAdminSiteService().hasPreviewURL(site))
            {
                getCmsAdminSiteService().setActiveSite(site);
                UIBrowserArea browserArea = getNavigationAreaModel().getNavigationArea().getPerspective().getBrowserArea();
                if(browserArea instanceof LiveEditBrowserArea)
                {
                    LOG.info("Opening site '" + site.getName() + "'...");
                    ((LiveEditBrowserArea)browserArea).refreshContent(site);
                }
                else
                {
                    LOG.warn("Can not open site '" + site.getName() + "'. Reason: Only Live Edit browser areas supported.");
                }
            }
            else
            {
                LOG.warn("Can not open site '" + site.getName() + "'. Reason: No preview URL available.");
            }
        }
    }


    public List<TypedObject> getItems()
    {
        if(!this.initialized)
        {
            setItems(UISessionUtils.getCurrentSession().getTypeService().wrapItems(getCmsCockpitService().getSites()));
            this.initialized = true;
        }
        return super.getItems();
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService siteService)
    {
        this.siteService = siteService;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.siteService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof CmsLiveEditEvent)
        {
            if(((CmsLiveEditEvent)event).getSite() != null)
            {
                setSelectedItem(getTypeService().wrapItem(((CmsLiveEditEvent)event).getSite()));
            }
        }
    }


    protected CmsCockpitService getCmsCockpitService()
    {
        if(this.cmsCockpitService == null)
        {
            this.cmsCockpitService = (CmsCockpitService)Registry.getApplicationContext().getBean("cmsCockpitService");
        }
        return this.cmsCockpitService;
    }


    public void setCmsCockpitService(CmsCockpitService cmsCockpitService)
    {
        this.cmsCockpitService = cmsCockpitService;
    }
}
