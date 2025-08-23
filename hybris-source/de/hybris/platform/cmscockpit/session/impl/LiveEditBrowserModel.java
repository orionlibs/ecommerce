package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.misc.UrlUtils;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultLiveEditViewModel;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cmscockpit.events.impl.CmsUrlChangeEvent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractAdvancedBrowserModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Executions;

public class LiveEditBrowserModel extends AbstractAdvancedBrowserModel
{
    private static final Logger LOG = Logger.getLogger(LiveEditBrowserModel.class);
    private DefaultLiveEditViewModel viewModel;
    private String currentUrl = "";
    private CMSSiteModel activeSite;
    private UserModel frontendUser;
    private String frontendSessionId;
    private String relatedPagePk;
    private FrontendAttributes addonOnFrontAttributes;
    private boolean previewDataActive = false;
    private CMSAdminSiteService adminSiteService;
    private I18NService i18nService;
    private ModelService modelService;
    private CatalogVersionModel actiaveCatalogVersion;
    private UserService userService;


    public boolean isPreviewDataActive()
    {
        return this.previewDataActive;
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
    }


    public void clearPreviewPageIfAny()
    {
        PreviewDataModel previewData = getViewModel().getCurrentPreviewData();
        if(previewData != null)
        {
            previewData.setPage(null);
            getModelService().save(previewData);
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Cannot retrieve current preview mode!");
        }
    }


    public void setNavigationEventAttributes(CmsNavigationEvent cmsNavigationEvent)
    {
        if(cmsNavigationEvent.getPage() != null)
        {
            setRelatedPagePk(cmsNavigationEvent.getPage().getPk().toString());
            getViewModel().setPage(cmsNavigationEvent.getPage());
        }
    }


    public void setFrontendAttributes(CmsUrlChangeEvent cmsUrlChangeEvent)
    {
        setCurrentUrl(cmsUrlChangeEvent.getUrl());
        setRelatedPagePk(cmsUrlChangeEvent.getRelatedPagePk());
        setFrontendUser(retriveCurrentFrontendUser(cmsUrlChangeEvent.getFrontendUserUid()));
        setFrontentSessionId(cmsUrlChangeEvent.getJaloSessionUid());
        getViewModel().setPage(null);
        if(cmsUrlChangeEvent instanceof CmsUrlChangeEvent)
        {
            this.addonOnFrontAttributes = cmsUrlChangeEvent.getExtendedFrontendAttributes();
        }
        else
        {
            this.addonOnFrontAttributes = null;
        }
    }


    public Object clone() throws CloneNotSupportedException
    {
        return null;
    }


    public void collapse()
    {
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new LiveEditContentBrowser();
    }


    public void fireModeChange(AbstractContentBrowser content)
    {
        LiveEditContentBrowser currentContentBrowser = null;
        if(content instanceof LiveEditContentBrowser)
        {
            currentContentBrowser = (LiveEditContentBrowser)content;
            currentContentBrowser.fireModeChanged();
        }
    }


    public void fireTogglePreviewDataMode(LiveEditContentBrowser contentBrowser)
    {
        this.previewDataActive = !this.previewDataActive;
        contentBrowser.firePreviewDataModeChanged();
    }


    public void fireTogglePreviewDataMode(LiveEditContentBrowser contentBrowser, boolean previewSectionActive)
    {
        this.previewDataActive = previewSectionActive;
        contentBrowser.firePreviewDataModeChanged();
    }


    public CatalogVersionModel getActiaveCatalogVersion()
    {
        return this.actiaveCatalogVersion;
    }


    public CMSSiteModel getActiveSite()
    {
        return this.activeSite;
    }


    protected CMSAdminSiteService getCMSAdminSiteService()
    {
        if(this.adminSiteService == null)
        {
            this.adminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.adminSiteService;
    }


    public String getCurrentUrl()
    {
        return this.currentUrl;
    }


    public UserModel getFrontendUser()
    {
        return this.frontendUser;
    }


    protected I18NService getI18NService()
    {
        if(this.i18nService == null)
        {
            this.i18nService = (I18NService)SpringUtil.getBean("i18nService");
        }
        return this.i18nService;
    }


    public TypedObject getItem(int index)
    {
        return null;
    }


    public List<TypedObject> getItems()
    {
        return null;
    }


    public String getLabel()
    {
        return "Live Edit Browser";
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    public PreviewDataModel getPreviewData()
    {
        PreviewDataModel previewData = getViewModel().getCurrentPreviewData();
        if(previewData == null || UISessionUtils.getCurrentSession().getModelService().isRemoved(previewData))
        {
            previewData = (PreviewDataModel)UISessionUtils.getCurrentSession().getModelService().create(PreviewDataModel.class);
            previewData.setLanguage(getI18NService().getLanguage(UISessionUtils.getCurrentSession().getGlobalDataLanguageIso()));
            previewData.setTime(null);
            previewData.setUser(this.frontendUser);
            getViewModel().setCurrentPreviewData(previewData);
        }
        return previewData;
    }


    public DefaultLiveEditViewModel getViewModel()
    {
        if(this.viewModel == null)
        {
            this.viewModel = newDefaultLiveEditViewModel();
        }
        return this.viewModel;
    }


    protected DefaultLiveEditViewModel newDefaultLiveEditViewModel()
    {
        return new DefaultLiveEditViewModel();
    }


    public boolean isAdvancedHeaderDropdownSticky()
    {
        return true;
    }


    public boolean isAdvancedHeaderDropdownVisible()
    {
        return isPreviewDataVisible();
    }


    public boolean isCollapsed()
    {
        return false;
    }


    public boolean isDuplicatable()
    {
        return false;
    }


    public boolean isPreviewDataVisible()
    {
        return this.previewDataActive;
    }


    public void onCmsPerpsectiveInitEvent()
    {
        getCMSAdminSiteService().setActiveSite(getActiveSite());
        getCMSAdminSiteService().setActiveCatalogVersion(getActiaveCatalogVersion());
    }


    public void refresh()
    {
        getViewModel().clearPreviewInformation();
        updateItems();
    }


    public void removeItems(Collection<Integer> indexes)
    {
    }


    public void setActiaveCatalogVersion(CatalogVersionModel actiaveCatalogVersion)
    {
        if(this.actiaveCatalogVersion == null || (this.actiaveCatalogVersion != null &&
                        !this.actiaveCatalogVersion.equals(actiaveCatalogVersion)))
        {
            this.actiaveCatalogVersion = actiaveCatalogVersion;
            this.viewModel.setWelcomePanelVisible((actiaveCatalogVersion == null));
        }
    }


    public void setActiveSite(CMSSiteModel activeSite)
    {
        this.activeSite = activeSite;
    }


    public void setCurrentSite(CMSSiteModel site)
    {
        getViewModel().setSite(site);
    }


    public void setCurrentUrl(String currentUrl)
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest)Executions.getCurrent().getNativeRequest();
        this.currentUrl = UrlUtils.extractHostInformationFromRequest(httpServletRequest, currentUrl);
        getViewModel().setCurrentUrl(this.currentUrl);
    }


    public void setFrontendUser(UserModel frontendUser)
    {
        this.frontendUser = frontendUser;
    }


    protected UserModel retriveCurrentFrontendUser(String frontendUserUid)
    {
        UserModel ret = null;
        if(StringUtils.isNotEmpty(frontendUserUid))
        {
            SessionContext ctx = null;
            try
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
                ret = getUserService().getUser(frontendUserUid);
            }
            finally
            {
                if(ctx != null)
                {
                    JaloSession.getCurrentSession().removeLocalSessionContext();
                }
            }
        }
        return ret;
    }


    protected UserService getUserService()
    {
        if(this.userService == null)
        {
            this.userService = (UserService)SpringUtil.getBean("userService");
        }
        return this.userService;
    }


    public void setPreviewData(PreviewDataModel previewData)
    {
        getViewModel().setCurrentPreviewData(previewData);
    }


    public void updateItems()
    {
        fireItemsChanged();
    }


    public String getFrontentSessionId()
    {
        return this.frontendSessionId;
    }


    public void setFrontentSessionId(String frontentSessionId)
    {
        this.frontendSessionId = frontentSessionId;
    }


    public String getRelatedPagePk()
    {
        return this.relatedPagePk;
    }


    public void setRelatedPagePk(String relatedPagePk)
    {
        this.relatedPagePk = relatedPagePk;
    }


    public FrontendAttributes getFrontendAttributes()
    {
        return this.addonOnFrontAttributes;
    }
}
