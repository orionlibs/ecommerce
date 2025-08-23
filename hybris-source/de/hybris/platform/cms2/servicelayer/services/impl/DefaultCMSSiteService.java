package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.store.BaseStoreModel;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSSiteService extends AbstractCMSService implements CMSSiteService
{
    private static final Logger LOG = Logger.getLogger(DefaultCMSSiteService.class.getName());
    private volatile CMSPageService cmsPageService;
    private CMSSiteDao cmsSiteDao;
    private CatalogVersionService catalogVersionService;
    private final ConcurrentMap<CacheKey, List<Pattern>> defaultPatternCache = new ConcurrentHashMap<>();


    public boolean containsCatalog(CMSSiteModel site, CatalogModel catalog, boolean contentOnly)
    {
        if(contentOnly)
        {
            return containsContentCatalogImpl(site, catalog);
        }
        return (containsContentCatalogImpl(site, catalog) || containsProductsCatalogImpl(site, catalog));
    }


    protected boolean containsContentCatalogImpl(CMSSiteModel site, CatalogModel catalog)
    {
        return (site.getContentCatalogs() != null && site.getContentCatalogs().contains(catalog));
    }


    protected boolean containsProductsCatalogImpl(CMSSiteModel site, CatalogModel catalog)
    {
        return (site.getProductCatalogs() != null && site.getProductCatalogs().contains(catalog));
    }


    public Collection<CatalogModel> getAllCatalogs(CMSSiteModel site) throws IllegalArgumentException
    {
        Set<CatalogModel> ret = new HashSet<>();
        if(site == null)
        {
            throw new IllegalArgumentException("No site specified.");
        }
        for(BaseStoreModel baseStore : site.getStores())
        {
            ret.addAll(baseStore.getCatalogs());
        }
        ret.addAll(site.getContentCatalogs());
        return ret;
    }


    public List<CatalogModel> getClassificationCatalogs(CMSSiteModel site)
    {
        List<CatalogModel> ret = new ArrayList<>();
        Collection<BaseStoreModel> stores = site.getStores();
        if(stores != null)
        {
            for(BaseStoreModel baseStore : stores)
            {
                if(baseStore != null)
                {
                    Collection<CatalogModel> catalogs = baseStore.getCatalogs();
                    if(catalogs != null)
                    {
                        for(CatalogModel catalog : catalogs)
                        {
                            if(catalog instanceof de.hybris.platform.catalog.model.classification.ClassificationSystemModel && !ret.contains(catalog))
                            {
                                ret.add(catalog);
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }


    public CMSSiteModel getSiteForURL(URL url) throws CMSItemNotFoundException
    {
        String strUrl = url.toString();
        CMSSiteModel currentSite = getCurrentSite();
        if(currentSite != null)
        {
            if(matchSiteForURL(currentSite, strUrl))
            {
                return currentSite;
            }
        }
        List<CMSSiteModel> sites = new ArrayList<>();
        Collection<CMSSiteModel> allSites = getSites();
        if(allSites != null)
        {
            for(CMSSiteModel site : allSites)
            {
                if(currentSite != site)
                {
                    if(matchSiteForURL(site, strUrl))
                    {
                        sites.add(site);
                    }
                }
            }
        }
        if(sites.isEmpty())
        {
            throw new CMSItemNotFoundException("No site found for URL [" + url.toString() + "]");
        }
        if(sites.size() > 1)
        {
            LOG.warn("More than one site associated with URL [" + url.toString() + "]. Returning first.");
        }
        return sites.iterator().next();
    }


    protected boolean matchSiteForURL(CMSSiteModel site, String strUrl)
    {
        CacheKey cacheKey = new CacheKey(site, site.getModifiedtime());
        List<Pattern> precompiledPatterns = this.defaultPatternCache.get(cacheKey);
        if(precompiledPatterns == null)
        {
            precompiledPatterns = new ArrayList<>();
            precompiledPatterns.addAll(compilePatterns(site.getUrlPatterns()));
            this.defaultPatternCache.put(cacheKey, precompiledPatterns);
        }
        return matches(strUrl, precompiledPatterns);
    }


    public Collection<CMSSiteModel> getSites()
    {
        return getCmsSiteDao().findAllCMSSites();
    }


    public String getStartPageLabelOrId(CMSSiteModel site)
    {
        return Optional.<ContentPageModel>ofNullable(site.getStartingPage())
                        .map(page -> (page.getLabel() != null) ? page.getLabel() : page.getUid())
                        .orElse(null);
    }


    public boolean hasCurrentCatalogVersion()
    {
        return (getSessionService().getAttribute("currentCatalogVersion") != null);
    }


    public boolean hasCurrentSite()
    {
        return (getSessionService().getAttribute("currentSite") != null);
    }


    public CMSPageService lookupCmsPageService()
    {
        throw new UnsupportedOperationException("please override DefaultCMSSiteService#lookupCmsPageService() or use <lookup-method name=\"lookupCmsPageService\" bean=\"..\">");
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


    protected CMSSiteDao getCmsSiteDao()
    {
        return this.cmsSiteDao;
    }


    @Required
    public void setCmsSiteDao(CMSSiteDao cmsSiteDao)
    {
        this.cmsSiteDao = cmsSiteDao;
    }


    public void setCurrentCatalogVersion(CatalogVersionModel catalogVersion) throws CMSItemNotFoundException
    {
        if(catalogVersion != null)
        {
            CMSSiteModel site = getCurrentSite();
            if(site == null)
            {
                throw new CMSItemNotFoundException("No current site set.");
            }
            if(!containsCatalog(site, catalogVersion.getCatalog(), false))
            {
                throw new CMSItemNotFoundException("Catalog [" + catalogVersion
                                .getCatalog() + "] is not part of current site [" + site.getUid() + "]");
            }
        }
        getSessionService().setAttribute("currentCatalogVersion", catalogVersion);
    }


    public void setCurrentSite(CMSSiteModel site)
    {
        getBaseSiteService().setCurrentBaseSite((BaseSiteModel)site, false);
    }


    public CMSSiteModel setCurrentSite(URL url, PreviewDataModel previewData) throws CMSItemNotFoundException
    {
        Set<CatalogVersionModel> activeCatalogVersions = new HashSet<>();
        CMSSiteModel site = getSiteForURL(url);
        setCurrentSite(site);
        Collection<CatalogVersionModel> catalogVersions = previewData.getCatalogVersions();
        for(CatalogVersionModel version : catalogVersions)
        {
            try
            {
                if(!containsCatalog(site, version.getCatalog(), false))
                {
                    LOG.warn("CatalogVersion [" + version + "] is null or not part of the associated site [" + site.getName() + "]");
                    continue;
                }
                activeCatalogVersions.add(version);
            }
            catch(Exception e)
            {
                LOG.warn("Could not set catalog [" + version + "]", e);
            }
        }
        getCatalogVersionService().setSessionCatalogVersions(activeCatalogVersions);
        return site;
    }


    public void setCurrentSiteAndCatalogVersions(CMSSiteModel site, boolean setDefaultCatalog) throws CMSItemNotFoundException
    {
        Set<CatalogVersionModel> catalogVersions = new HashSet<>();
        for(CatalogModel catalog : getAllCatalogs(site))
        {
            CatalogVersionModel activeVersion = catalog.getActiveCatalogVersion();
            if(activeVersion == null)
            {
                throw new CMSItemNotFoundException("catalog [" + catalog
                                .getId() + "] " + catalog.getName() + " has no active catalog version.");
            }
            catalogVersions.add(catalog.getActiveCatalogVersion());
        }
        getCatalogVersionService().setSessionCatalogVersions(catalogVersions);
        CMSSiteModel current = getCurrentSite();
        setCurrentSite(site);
        boolean hasSiteChanged = (current != null && !current.getUid().equals(site.getUid()));
        boolean canSetDefaultCatalog = (setDefaultCatalog && (hasSiteChanged || !hasCurrentCatalogVersion()));
        if(canSetDefaultCatalog)
        {
            CatalogModel catalog = site.getDefaultCatalog();
            if(catalog != null)
            {
                String catalogID = catalog.getId();
                String versionName = site.getDefaultCatalog().getActiveCatalogVersion().getVersion();
                CatalogVersionModel version = getCatalogVersionService().getCatalogVersion(catalogID, versionName);
                setCurrentCatalogVersion(version);
            }
            else
            {
                LOG.debug("No default catalog defined for site [" + site.getUid() + "] " + site.getName());
            }
        }
        setCurrentSite(site);
    }


    public CMSSiteModel setCurrentSiteAndCatalogVersions(String siteId, boolean setDefaultCatalog) throws CMSItemNotFoundException
    {
        List<CMSSiteModel> sites = getCmsSiteDao().findCMSSitesById(siteId);
        if(sites.isEmpty())
        {
            throw new CMSItemNotFoundException("No site with id [" + siteId + "] found.");
        }
        setCurrentSiteAndCatalogVersions(sites.get(0), setDefaultCatalog);
        return sites.get(0);
    }


    public CMSSiteModel setCurrentSiteAndCatalogVersionsForURL(URL url, boolean setDefaultCatalog) throws CMSItemNotFoundException
    {
        CMSSiteModel site = getSiteForURL(url);
        setCurrentSiteAndCatalogVersions(site, setDefaultCatalog);
        return site;
    }


    public CatalogVersionModel getCurrentSiteDefaultContentCatalogActiveVersion()
    {
        CMSSiteModel site = getCurrentSite();
        if(site != null && site.getDefaultContentCatalog() != null)
        {
            return site.getDefaultContentCatalog().getActiveCatalogVersion();
        }
        return null;
    }


    protected List<Pattern> compilePatterns(Collection<String> stringPatterns)
    {
        List<Pattern> ret = new ArrayList<>();
        if(stringPatterns == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("");
            }
            return ret;
        }
        for(String stringPattern : stringPatterns)
        {
            ret.add(Pattern.compile(stringPattern));
        }
        return ret;
    }


    protected CMSPageService getCmsPageService()
    {
        CMSPageService service = this.cmsPageService;
        if(service == null)
        {
            synchronized(this)
            {
                service = this.cmsPageService;
                if(service == null)
                {
                    this.cmsPageService = service = lookupCmsPageService();
                }
            }
        }
        return service;
    }


    protected boolean matches(String input, Collection<Pattern> patterns)
    {
        for(Pattern pattern : patterns)
        {
            try
            {
                Matcher matcher = pattern.matcher(input);
                if(matcher.matches())
                {
                    return true;
                }
            }
            catch(PatternSyntaxException pse)
            {
                LOG.warn("Illegal Pattern", pse);
            }
        }
        return false;
    }
}
