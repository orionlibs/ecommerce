package de.hybris.platform.cmscockpit.components.liveedit.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditViewModel;
import de.hybris.platform.cmscockpit.components.liveedit.PreviewLoader;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Executions;

public class DefaultLiveEditViewModel implements LiveEditViewModel
{
    private static final Logger LOG = Logger.getLogger(DefaultLiveEditViewModel.class);
    protected static final String SLASH_MARK = "/";
    protected static final String QUESTION_MARK = "?";
    protected static final String EQUALS_MARK = "=";
    private CMSSiteModel site;
    private AbstractPageModel page;
    private PreviewDataModel currentPreviewData;
    private UserModel currentFrontendUser;
    private String currentUrl = "";
    private boolean liveEditModeEnabled;
    private boolean pagePreview = false;
    private boolean contentVisible = false;
    private boolean welcomePanelVisible = true;
    private ModelService modelService;
    private SystemService systemService;
    private CMSPreviewService previewService;
    private I18NService i18nService;
    private CMSSiteService siteService;
    private CMSAdminRestrictionService restrService;
    private CMSAdminSiteService adminSiteService;
    private PreviewLoader previewLoader;


    public void clearPreviewInformation()
    {
        this.currentUrl = extractUrlFromRequest();
        this.currentPreviewData = null;
        this.site = null;
    }


    public String computeFinalUrl()
    {
        String previewUrl = "";
        StringBuilder urlBuffer = new StringBuilder();
        if(getSite() == null)
        {
            LOG.warn("Site can not be null!");
            return previewUrl;
        }
        previewUrl = extractUrlFromRequest();
        PreviewDataModel previewContext = getCMSPreviewService().clonePreviewData(this.currentPreviewData);
        if(previewContext == null)
        {
            previewContext = new PreviewDataModel();
            previewContext.setLiveEdit(Boolean.valueOf(isLiveEditModeEnabled()));
        }
        previewContext.setEditMode(Boolean.valueOf(!isPagePreview()));
        getPreviewLoader().loadValues(previewContext, this.page, retriveCatalogVersions(), isLiveEditModeEnabled(),
                        getCurrentDataLanguageModel(), getCurrentUrl());
        setCurrentPreviewData(previewContext);
        getModelService().save(previewContext);
        CMSPreviewTicketModel ticket = getCMSPreviewService().createPreviewTicket(previewContext);
        String[] urlParts = previewUrl.split("\\?");
        if(isLiveEditModeEnabled())
        {
            this.currentPreviewData.setResourcePath(getCurrentUrl());
            getModelService().save(previewContext);
        }
        urlBuffer.append(urlParts[0]);
        urlBuffer.append("/");
        urlBuffer.append("cx-preview");
        urlBuffer.append("?");
        urlBuffer.append("cmsTicketId");
        urlBuffer.append("=");
        urlBuffer.append(ticket.getId());
        if(urlParts.length > 1)
        {
            urlBuffer.append("&");
            urlBuffer.append(urlParts[1]);
        }
        return urlBuffer.toString();
    }


    public String extractUrlFromRequest()
    {
        StringBuilder urlBuilder = new StringBuilder();
        if(getSite() != null && StringUtils.isNotBlank(getSite().getPreviewURL()))
        {
            if(!getSite().getPreviewURL().matches("(http://|https://)(.*)"))
            {
                HttpServletRequest request = (HttpServletRequest)Executions.getCurrent().getNativeRequest();
                urlBuilder.append(request.getScheme());
                urlBuilder.append("://");
                urlBuilder.append(request.getServerName());
                urlBuilder.append(":");
                urlBuilder.append(request.getServerPort());
                urlBuilder.append(getSite().getPreviewURL());
            }
            else
            {
                urlBuilder.append(getSite().getPreviewURL());
            }
        }
        return urlBuilder.toString();
    }


    protected CMSAdminSiteService getCMSAdminSiteService()
    {
        if(this.adminSiteService == null)
        {
            this.adminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.adminSiteService;
    }


    protected CMSPreviewService getCMSPreviewService()
    {
        if(this.previewService == null)
        {
            this.previewService = (CMSPreviewService)SpringUtil.getBean("cmsPreviewService");
        }
        return this.previewService;
    }


    protected CMSSiteService getCMSSiteService()
    {
        if(this.siteService == null)
        {
            this.siteService = (CMSSiteService)SpringUtil.getBean("cmsSiteService");
        }
        return this.siteService;
    }


    protected LanguageModel getCurrentDataLanguageModel()
    {
        if(this.currentPreviewData != null && this.currentPreviewData.getLanguage() != null)
        {
            return this.currentPreviewData.getLanguage();
        }
        return getI18NService().getLanguage(UISessionUtils.getCurrentSession().getGlobalDataLanguageIso());
    }


    public PreviewDataModel getCurrentPreviewData()
    {
        return this.currentPreviewData;
    }


    public String getCurrentUrl()
    {
        return this.currentUrl;
    }


    protected I18NService getI18NService()
    {
        if(this.i18nService == null)
        {
            this.i18nService = (I18NService)SpringUtil.getBean("i18nService");
        }
        return this.i18nService;
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    public AbstractPageModel getPage()
    {
        return this.page;
    }


    public PreviewLoader getPreviewLoader()
    {
        if(this.previewLoader == null)
        {
            this.previewLoader = (PreviewLoader)SpringUtil.getBean("cmsPreviewLoader");
        }
        return this.previewLoader;
    }


    protected CMSAdminRestrictionService getRestrictionService()
    {
        if(this.restrService == null)
        {
            this.restrService = (CMSAdminRestrictionService)SpringUtil.getBean("cmsAdminRestrictionService");
        }
        return this.restrService;
    }


    public CMSSiteModel getSite()
    {
        if(this.site == null)
        {
            this.site = getCMSAdminSiteService().getActiveSite();
        }
        return this.site;
    }


    protected SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = (SystemService)SpringUtil.getBean("systemService");
        }
        return this.systemService;
    }


    public boolean isContentVisible()
    {
        return this.contentVisible;
    }


    public boolean isLiveEditModeEnabled()
    {
        return this.liveEditModeEnabled;
    }


    public boolean isPagePreview()
    {
        return this.pagePreview;
    }


    public boolean isPreviewDataValid()
    {
        return !getModelService().isRemoved(getCurrentPreviewData());
    }


    public boolean isSlotLockedForId(String slotId)
    {
        CMSAdminContentSlotService service = (CMSAdminContentSlotService)SpringUtil.getBean("cmsAdminContentSlotService");
        ContentSlotModel contentSlot = service.getContentSlotForIdAndCatalogVersions(slotId, getCurrentPreviewData()
                        .getCatalogVersions());
        if(contentSlot != null)
        {
            return !service.hasRelations(contentSlot);
        }
        LOG.warn(String.format("ContentSlotModel does not exist for id: %s", new Object[] {slotId}));
        return true;
    }


    public boolean isWelcomePanelVisible()
    {
        return this.welcomePanelVisible;
    }


    protected Set<CatalogVersionModel> retriveCatalogVersions()
    {
        Set<CatalogVersionModel> catalogVersions = new HashSet<>();
        Set<CatalogVersionModel> toRemove = null;
        CatalogVersionModel currentCatalogVersion = getCMSAdminSiteService().getActiveCatalogVersion();
        if(currentCatalogVersion != null)
        {
            toRemove = new HashSet<>(currentCatalogVersion.getCatalog().getCatalogVersions());
            toRemove.remove(currentCatalogVersion);
        }
        for(CatalogModel catalogModel : getCMSSiteService().getAllCatalogs(this.site))
        {
            catalogVersions.add(catalogModel.getActiveCatalogVersion());
        }
        if(toRemove != null)
        {
            catalogVersions.removeAll(toRemove);
        }
        if(currentCatalogVersion != null)
        {
            catalogVersions.add(currentCatalogVersion);
        }
        return catalogVersions;
    }


    public void setContentVisible(boolean contentVisible)
    {
        this.contentVisible = contentVisible;
        this.welcomePanelVisible = !this.contentVisible;
    }


    public void setCurrentPreviewData(PreviewDataModel previewData)
    {
        this.currentPreviewData = previewData;
    }


    public void setCurrentUrl(String url)
    {
        if(this.currentUrl != null && !this.currentUrl.equals(url))
        {
            this.currentUrl = url;
            LOG.info("Change current url to : " + url);
        }
    }


    public void setLiveEditModeEnabled(boolean enabled)
    {
        this.liveEditModeEnabled = enabled;
    }


    public void setPage(AbstractPageModel page)
    {
        this.page = page;
    }


    public void setPagePreview(boolean previewPage)
    {
        this.pagePreview = previewPage;
    }


    public void setPreviewLoader(PreviewLoader previewLoader)
    {
        this.previewLoader = previewLoader;
    }


    public void setSite(CMSSiteModel site)
    {
        this.site = site;
    }


    public void setWelcomePanelVisible(boolean welcomePanelVisible)
    {
        this.welcomePanelVisible = welcomePanelVisible;
        this.contentVisible = !this.welcomePanelVisible;
    }


    public UserModel getCurrentFrontendUser()
    {
        return this.currentFrontendUser;
    }


    public void setCurrentFrontendUser(UserModel currentFrontendUser)
    {
        this.currentFrontendUser = currentFrontendUser;
    }
}
