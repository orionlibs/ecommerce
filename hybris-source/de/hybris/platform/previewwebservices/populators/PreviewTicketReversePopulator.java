package de.hybris.platform.previewwebservices.populators;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.catalogversion.service.CMSCatalogVersionService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.previewwebservices.dto.CatalogVersionWsDTO;
import de.hybris.platform.previewwebservices.dto.PreviewTicketWsDTO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.util.LocalViewExecutor;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class PreviewTicketReversePopulator implements Populator<PreviewTicketWsDTO, PreviewDataModel>
{
    protected static final Logger LOG = Logger.getLogger(PreviewTicketReversePopulator.class.getName());
    private CMSSiteService cmsSiteService;
    private UserService userService;
    private CatalogVersionService catalogVersionService;
    private CommonI18NService commonI18NService;
    private CMSPageService cmsPageService;
    private CMSAdminSiteService cmsAdminSiteService;
    private LocalViewExecutor localViewExecutor;
    private CMSVersionService cmsVersionService;
    private Comparator<CatalogVersionModel> cmsCatalogVersionLevelComparator;
    private CMSCatalogVersionService cmsCatalogVersionService;
    private SessionService sessionService;


    public void populate(PreviewTicketWsDTO source, PreviewDataModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        CMSSiteModel cmsSiteModel = retrieveCmsSite(source, target);
        getLocalViewExecutor().executeWithAllCatalogs(() -> {
            getCmsAdminSiteService().setActiveSite(cmsSiteModel);
            target.setActiveSite(cmsSiteModel);
            target.setResourcePath(source.getResourcePath());
            target.setTime(source.getTime());
            if(source.getUser() != null)
            {
                target.setUser(this.userService.getUserForUID(source.getUser()));
            }
            else
            {
                target.setUser(null);
            }
            if(source.getUserGroup() != null)
            {
                target.setUserGroup(this.userService.getUserGroupForUID(source.getUserGroup()));
            }
            else
            {
                target.setUserGroup(null);
            }
            if(source.getLanguage() != null)
            {
                target.setLanguage(this.commonI18NService.getLanguage(source.getLanguage()));
            }
            else
            {
                target.setLanguage(null);
            }
            setCatalogVersionInSession(source, target);
            setTimeInSession(source.getTime());
            setPageAndVersion(source, target);
            return null;
        });
    }


    protected CMSSiteModel retrieveCmsSite(PreviewTicketWsDTO source, PreviewDataModel target)
    {
        try
        {
            return (source.getSiteId() == null) ? getCmsSiteService().getSiteForURL(new URL(source.getResourcePath())) :
                            getCmsAdminSiteService().getSiteForId(source.getSiteId());
        }
        catch(CMSItemNotFoundException | java.net.MalformedURLException e)
        {
            return (CMSSiteModel)Optional.<PreviewDataModel>ofNullable(target).map(PreviewDataModel::getActiveSite).orElseThrow(() -> new ConversionException("Could not create preview ticket model from source. No site for resource path found.", e));
        }
    }


    protected void setCatalogVersionInSession(PreviewTicketWsDTO source, PreviewDataModel target)
    {
        Set<CatalogVersionModel> activeCatalogVersions = retrieveCatalogVersionsForPreview(source, target);
        CatalogVersionModel previewContentCatalogVersion = findCmsCatalogVersionFromSource(source, activeCatalogVersions);
        if(Objects.nonNull(previewContentCatalogVersion))
        {
            target.setPreviewContentCatalogVersion(previewContentCatalogVersion);
            List<CatalogVersionModel> catalogVersions = getCmsCatalogVersionService().getFullHierarchyForCatalogVersion(previewContentCatalogVersion, target.getActiveSite());
            getCatalogVersionService().setSessionCatalogVersions(
                            getCmsCatalogVersionService().getIntersectionOfCatalogVersions(catalogVersions, activeCatalogVersions));
        }
        target.setCatalogVersions(activeCatalogVersions);
    }


    protected void setTimeInSession(Date date)
    {
        if(Objects.nonNull(date))
        {
            getSessionService().setAttribute("previewTime", date);
        }
        else
        {
            getSessionService().removeAttribute("previewTime");
        }
    }


    protected void setPageAndVersion(PreviewTicketWsDTO source, PreviewDataModel target)
    {
        if(StringUtils.hasText(source.getPageId()))
        {
            try
            {
                target.setPage(getCmsPageService().getPageForId(source.getPageId()));
            }
            catch(CMSItemNotFoundException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("No page with id [" + source.getPageId() + "] found.");
                }
            }
        }
        else
        {
            target.setPage(null);
        }
        if(StringUtils.hasText(source.getVersionId()))
        {
            Optional<CMSVersionModel> cmsVersion = getCmsVersionService().getVersionByUid(source.getVersionId());
            Objects.requireNonNull(target);
            cmsVersion.ifPresent(target::setVersion);
        }
        else
        {
            target.setVersion(null);
        }
    }


    protected CatalogVersionModel findCmsCatalogVersionFromSource(PreviewTicketWsDTO source, Set<CatalogVersionModel> catalogVersions)
    {
        if(CollectionUtils.isNotEmpty(catalogVersions))
        {
            Optional<CatalogVersionModel> result = catalogVersions.stream().filter(cv -> cv.getCatalog() instanceof de.hybris.platform.cms2.model.contents.ContentCatalogModel)
                            .filter(cv -> (Objects.nonNull(source) && Objects.nonNull(source.getCatalogVersions())) ? (!source.getCatalogVersions().stream().filter(()).findAny().isEmpty()) : true).max(getCmsCatalogVersionLevelComparator());
            return result.orElse(null);
        }
        return null;
    }


    protected CatalogVersionModel findCmsCatalogVersion(Collection<CatalogVersionModel> catalogVersions)
    {
        if(CollectionUtils.isNotEmpty(catalogVersions))
        {
            Optional<CatalogVersionModel> result = catalogVersions.stream().filter(cv -> cv.getCatalog() instanceof de.hybris.platform.cms2.model.contents.ContentCatalogModel).max(getCmsCatalogVersionLevelComparator());
            return result.orElse(null);
        }
        return null;
    }


    protected Set<CatalogVersionModel> retrieveCatalogVersionsForPreview(PreviewTicketWsDTO source, PreviewDataModel target)
    {
        Map<String, String> catalogVersionMap = buildRequestedCatalogVersionMap(source);
        Set<CatalogVersionModel> previewCatalogVersions = (Set<CatalogVersionModel>)getCmsSiteService().getAllCatalogs(target.getActiveSite()).stream().map(c -> getValidCatalogVersionModel(c, catalogVersionMap)).collect(Collectors.toSet());
        if(target.getActiveCatalogVersion() != null)
        {
            previewCatalogVersions.removeAll(target.getActiveCatalogVersion().getCatalog().getCatalogVersions());
            previewCatalogVersions.add(target.getActiveCatalogVersion());
        }
        return previewCatalogVersions;
    }


    protected Map<String, String> buildRequestedCatalogVersionMap(PreviewTicketWsDTO source)
    {
        if(source.getCatalogVersions() != null)
        {
            return (Map<String, String>)source.getCatalogVersions().stream()
                            .collect(Collectors.toMap(CatalogVersionWsDTO::getCatalog, CatalogVersionWsDTO::getCatalogVersion));
        }
        return Collections.emptyMap();
    }


    protected CatalogVersionModel getValidCatalogVersionModel(CatalogModel catalog, Map<String, String> catalogVersionMap)
    {
        String version = catalogVersionMap.get(catalog.getId());
        if(version != null)
        {
            Objects.requireNonNull(catalog);
            return catalog.getCatalogVersions().stream().filter(cv -> version.equals(cv.getVersion())).findAny().orElseGet(catalog::getActiveCatalogVersion);
        }
        return catalog.getActiveCatalogVersion();
    }


    protected CMSSiteService getCmsSiteService()
    {
        return this.cmsSiteService;
    }


    @Required
    public void setCmsSiteService(CMSSiteService cmsSiteService)
    {
        this.cmsSiteService = cmsSiteService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected CMSPageService getCmsPageService()
    {
        return this.cmsPageService;
    }


    @Required
    public void setCmsPageService(CMSPageService cmsPageService)
    {
        this.cmsPageService = cmsPageService;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.cmsAdminSiteService;
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService cmsAdminSiteService)
    {
        this.cmsAdminSiteService = cmsAdminSiteService;
    }


    protected LocalViewExecutor getLocalViewExecutor()
    {
        return this.localViewExecutor;
    }


    @Required
    public void setLocalViewExecutor(LocalViewExecutor localViewExecutor)
    {
        this.localViewExecutor = localViewExecutor;
    }


    protected CMSVersionService getCmsVersionService()
    {
        return this.cmsVersionService;
    }


    @Required
    public void setCmsVersionService(CMSVersionService cmsVersionService)
    {
        this.cmsVersionService = cmsVersionService;
    }


    protected Comparator<CatalogVersionModel> getCmsCatalogVersionLevelComparator()
    {
        return this.cmsCatalogVersionLevelComparator;
    }


    @Required
    public void setCmsCatalogVersionLevelComparator(Comparator<CatalogVersionModel> cmsCatalogVersionLevelComparator)
    {
        this.cmsCatalogVersionLevelComparator = cmsCatalogVersionLevelComparator;
    }


    protected CMSCatalogVersionService getCmsCatalogVersionService()
    {
        return this.cmsCatalogVersionService;
    }


    @Required
    public void setCmsCatalogVersionService(CMSCatalogVersionService cmsCatalogVersionService)
    {
        this.cmsCatalogVersionService = cmsCatalogVersionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
