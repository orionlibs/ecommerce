package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.events.impl.CmsUrlChangeEvent;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultAdvancedContentBrowser;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;

public class LiveEditContentBrowser extends DefaultAdvancedContentBrowser
{
    private static final Logger LOG = Logger.getLogger(LiveEditContentBrowser.class);
    private static final int EDITORS_PER_ROW = 5;
    private static final String PERSP_TAG = "persp";
    private static final String EVENTS_TAG = "events";
    private static final String PAGE_VIEW_PERSPECTIVE_ID = "cmscockpit.perspective.catalog";
    private static final String CMS_NAVIGATION_EVENT = "pageviewnavigation";
    private static final String CMS_PNAV_SITE = "pnav-site";
    private static final String CMS_PNAV_CATALOG = "pnav-catalog";
    private static final String CMS_PNAV_PAGE = "pnav-page";
    private EditorFactory editorFactory = null;
    private CMSPreviewService cmsPreviewService;
    private CMSAdminSiteService cmsAdminSiteService;
    private UserService userService;
    private SessionService sessionService;
    private CmsCockpitService cmsCockpitService;


    public EditorFactory getEditorFactory()
    {
        if(this.editorFactory == null)
        {
            this.editorFactory = (EditorFactory)SpringUtil.getBean("EditorFactory");
        }
        return this.editorFactory;
    }


    public boolean update()
    {
        updateCaption();
        updatePreviewContextSection();
        return getMainAreaComponent().update();
    }


    protected void updatePreviewContextSection()
    {
        AdvancedBrowserModel advancedBrowserModel = getModel();
        if(advancedBrowserModel instanceof LiveEditBrowserModel)
        {
            LiveEditBrowserModel liveEditBrowserModel = (LiveEditBrowserModel)advancedBrowserModel;
            liveEditBrowserModel.fireTogglePreviewDataMode(this, false);
        }
    }


    public void fireModeChanged()
    {
        if(getMainAreaComponent() instanceof DefaultLiveEditMainAreaComponent)
        {
            ((DefaultLiveEditMainAreaComponent)getMainAreaComponent()).fireModeChanged();
            getCaptionComponent().update();
        }
    }


    public void firePreviewDataModeChanged()
    {
        updateCaption();
        resize();
    }


    public void setModel(BrowserModel model)
    {
        if(model instanceof LiveEditBrowserModel)
        {
            CMSSiteModel currentSite = ((LiveEditBrowserArea)model.getArea()).getCurrentSite();
            if(currentSite != null)
            {
                ((LiveEditBrowserModel)model).setCurrentUrl(currentSite.getPreviewURL());
            }
        }
        super.setModel(model);
    }


    protected AbstractBrowserComponent createMainAreaComponent()
    {
        return (AbstractBrowserComponent)new DefaultLiveEditMainAreaComponent(this, getModel(), (AbstractContentBrowser)this);
    }


    protected AbstractBrowserComponent createCaptionComponent()
    {
        return (AbstractBrowserComponent)new DefaultLiveEditCaptionComponent(this, (BrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    protected TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }


    protected void updatePreviewData()
    {
        fireModeChanged();
        fireModeChanged();
    }


    protected Button createRightCaptionButton(String label, String sClass, HtmlBasedComponent parent, EventListener listener)
    {
        Button button = new Button(label);
        button.setSclass(sClass);
        button.addEventListener("onClick", listener);
        parent.appendChild((Component)button);
        return button;
    }


    protected void updateAfterChangedUrl(CmsUrlChangeEvent cmsUrlChangeEvent)
    {
        AdvancedBrowserModel advancedBrowserModel = getModel();
        if(advancedBrowserModel instanceof LiveEditBrowserModel)
        {
            LiveEditBrowserModel liveEditBrowserModel = (LiveEditBrowserModel)advancedBrowserModel;
            liveEditBrowserModel.clearPreviewPageIfAny();
            liveEditBrowserModel.setFrontendAttributes(cmsUrlChangeEvent);
            updateCaption();
        }
        else
        {
            LOG.error("Cannot retrieve current browser model  - wrong type!");
        }
    }


    protected CMSPreviewService getCmsPreviewService()
    {
        if(this.cmsPreviewService == null)
        {
            this.cmsPreviewService = (CMSPreviewService)SpringUtil.getBean("cmsPreviewService");
        }
        return this.cmsPreviewService;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        if(this.cmsAdminSiteService == null)
        {
            this.cmsAdminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.cmsAdminSiteService;
    }


    protected UserService getUserService()
    {
        if(this.userService == null)
        {
            this.userService = (UserService)SpringUtil.getBean("userService");
        }
        return this.userService;
    }


    protected SessionService getSessionService()
    {
        if(this.sessionService == null)
        {
            this.sessionService = (SessionService)SpringUtil.getBean("sessionService");
        }
        return this.sessionService;
    }


    protected CmsCockpitService getCmsCockpitService()
    {
        if(this.cmsCockpitService == null)
        {
            this.cmsCockpitService = (CmsCockpitService)SpringUtil.getBean("cmsCockpitService");
        }
        return this.cmsCockpitService;
    }


    public void setCmsCockpitService(CmsCockpitService cmsCockpitService)
    {
        this.cmsCockpitService = cmsCockpitService;
    }
}
