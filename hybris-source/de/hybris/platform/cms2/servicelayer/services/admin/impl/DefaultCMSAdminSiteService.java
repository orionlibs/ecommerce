package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSAdminSiteService extends AbstractCMSAdminService implements CMSAdminSiteService
{
    private TypeService typeService;
    private CMSSiteDao cmsSiteDao;
    private CatalogVersionService catalogVersionService;


    public Collection<CMSSiteModel> getSites()
    {
        return getCmsSiteDao().findAllCMSSites();
    }


    public boolean hasActiveSite()
    {
        return (getActiveSite() != null);
    }


    public void setActiveSite(CMSSiteModel site)
    {
        getSessionService().setAttribute("activeSite", (site == null) ? null : site.getPk());
    }


    public boolean hasActiveCatalogVersion()
    {
        return (getActiveCatalogVersion() != null);
    }


    public void setActiveCatalogVersion(String catalogId, String versionId) throws CMSItemNotFoundException
    {
        CatalogVersionModel version = getCatalogVersionService().getCatalogVersion(catalogId, versionId);
        if(version == null)
        {
            throw new CMSItemNotFoundException("No catalog [" + catalogId + ":" + versionId + "] found.");
        }
        setActiveCatalogVersion(version);
    }


    public boolean hasPreviewURL()
    {
        CMSSiteModel site = getActiveSite();
        if(site == null)
        {
            return false;
        }
        return hasPreviewURL(site);
    }


    public boolean hasPreviewURL(CMSSiteModel site)
    {
        return !StringUtils.isEmpty(site.getPreviewURL());
    }


    public void setActiveCatalogVersion(CatalogVersionModel catalogVersion)
    {
        getSessionService().setAttribute("activeCatalogVersion", (catalogVersion == null) ? null : catalogVersion.getPk());
    }


    public Collection<ComposedTypeModel> getAllPageTypes()
    {
        return getTypeService().getComposedTypeForCode(GeneratedCms2Constants.TC.ABSTRACTPAGE).getAllSubTypes();
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


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public Collection<CatalogModel> getAllCatalogs(CMSSiteModel site)
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


    public CMSSiteModel getSiteForId(String id) throws AmbiguousIdentifierException, UnknownIdentifierException
    {
        List<CMSSiteModel> sites = getCmsSiteDao().findCMSSitesById(id);
        if(sites.isEmpty())
        {
            throw new UnknownIdentifierException("No site with id [" + id + "] found.");
        }
        if(sites.size() > 1)
        {
            throw new AmbiguousIdentifierException("Site id '" + id + "' is not unique, " + sites.size() + " sites found!");
        }
        return sites.get(0);
    }


    public void setActiveSiteForId(String id) throws AmbiguousIdentifierException, UnknownIdentifierException
    {
        setActiveSite(getSiteForId(id));
    }


    public void setCloneContext(Map<String, String> context)
    {
        getSessionService().setAttribute("cloneContext", context);
    }


    public Map<String, String> getCloneContext()
    {
        return (Map<String, String>)getSessionService().getAttribute("cloneContext");
    }


    public void setOriginalItemContext(Map<String, Object> context)
    {
        getSessionService().setAttribute("originalItemContext", context);
    }


    public Map<String, Object> getOriginalItemContext()
    {
        return (Map<String, Object>)getSessionService().getAttribute("originalItemContext");
    }


    public void setRestoreContext(Map<String, Object> context)
    {
        getSessionService().setAttribute("restoreContext", context);
    }


    public Map<String, Object> getRestoreContext()
    {
        return (Map<String, Object>)getSessionService().getAttribute("restoreContext");
    }


    public void setTypeContext(Map<String, Object> context)
    {
        getSessionService().setAttribute("typeContext", context);
    }


    public Map<String, Object> getTypeContext()
    {
        return (Map<String, Object>)getSessionService().getAttribute("typeContext");
    }
}
