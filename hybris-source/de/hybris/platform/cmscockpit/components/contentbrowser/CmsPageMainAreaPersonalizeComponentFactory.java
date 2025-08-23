package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractMainAreaBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.GridMainAreaComponentFactory;
import de.hybris.platform.cockpit.model.gridview.GridItemRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;

public class CmsPageMainAreaPersonalizeComponentFactory extends GridMainAreaComponentFactory
{
    private static final Logger LOG = Logger.getLogger(CmsPageMainAreaPersonalizeComponentFactory.class);
    private CmsCockpitService cmsCockpitService = null;
    private CMSPageLockingService cmsPageLockingService;


    public AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        Object object = new Object(this, model, contentBrowser);
        object.setGridItemRenderer((GridItemRenderer)new PersonalizeGridRenderer(this));
        return (AbstractMainAreaBrowserComponent)object;
    }


    public String getActiveButtonImage()
    {
        return "/cmscockpit/images/button_view_personalize_available_a.png";
    }


    public String getButtonLabel()
    {
        return null;
    }


    public String getButtonTooltip()
    {
        return Labels.getLabel("browser.page.personalize");
    }


    public String getInactiveButtonImage()
    {
        return "/cmscockpit/images/button_view_personalize_available_i.png";
    }


    public String getViewModeID()
    {
        return "PERSONALIZE";
    }


    protected boolean isPageLocked(TypedObject wrappedPageModel)
    {
        return getCmsPageLockingService().isPageLockedFor((AbstractPageModel)wrappedPageModel.getObject(),
                        UISessionUtils.getCurrentSession().getSystemService().getCurrentUser());
    }


    protected CMSPageLockingService getCmsPageLockingService()
    {
        if(this.cmsPageLockingService == null)
        {
            this.cmsPageLockingService = (CMSPageLockingService)SpringUtil.getBean("cmsPageLockingService");
        }
        return this.cmsPageLockingService;
    }


    public CmsCockpitService getCmsCockpitService()
    {
        if(this.cmsCockpitService == null)
        {
            this.cmsCockpitService = (CmsCockpitService)SpringUtil.getBean("cmsCockpitService");
        }
        return this.cmsCockpitService;
    }
}
