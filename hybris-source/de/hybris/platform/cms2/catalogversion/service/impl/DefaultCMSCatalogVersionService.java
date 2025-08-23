package de.hybris.platform.cms2.catalogversion.service.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.catalogversion.service.CMSCatalogVersionService;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.multicountry.service.CatalogLevelService;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSCatalogVersionService implements CMSCatalogVersionService
{
    private BaseSiteService baseSiteService;
    private CatalogVersionService catalogVersionService;
    private UserService userService;
    private CatalogLevelService catalogLevelService;
    private SessionSearchRestrictionsDisabler sessionSearchRestrictionDisabler;
    private Comparator<CatalogVersionModel> cmsCatalogVersionLevelComparator;
    private CatalogLevelService cmsCatalogLevelService;


    public Map<CatalogModel, Set<CatalogVersionModel>> getContentCatalogsAndVersions(boolean canRead, boolean canWrite, CMSSiteModel cmsSiteModel)
    {
        Predicate<Map.Entry<CatalogModel, Set<CatalogVersionModel>>> isContentCatalog = entry -> cmsSiteModel.getContentCatalogs().contains(entry.getKey());
        return (Map<CatalogModel, Set<CatalogVersionModel>>)getCatalogsAndVersions(canRead, canWrite, isContentCatalog)
                        .entrySet().stream().collect(Collectors.toMap(entry -> (CatalogModel)entry.getKey(), entry -> (Set)entry.getValue()));
    }


    public Map<CatalogModel, Set<CatalogVersionModel>> getProductCatalogsAndVersions(boolean canRead, boolean canWrite, BaseSiteModel baseSiteModel)
    {
        List<CatalogModel> productCatalogs = getBaseSiteService().getProductCatalogs(baseSiteModel);
        Predicate<Map.Entry<CatalogModel, Set<CatalogVersionModel>>> isPlainCatalog = entry -> productCatalogs.contains(entry.getKey());
        return getCatalogsAndVersions(canRead, canWrite, isPlainCatalog);
    }


    protected Map<CatalogModel, Set<CatalogVersionModel>> getCatalogsAndVersions(boolean canRead, boolean canWrite, Predicate<Map.Entry<CatalogModel, Set<CatalogVersionModel>>> catalogTypePredicate)
    {
        UserModel currentUser = getUserService().getCurrentUser();
        Collection<CatalogVersionModel> allowedCatalogVersions = new ArrayList<>();
        if(canWrite)
        {
            allowedCatalogVersions.addAll(getCatalogVersionService().getAllWritableCatalogVersions((PrincipalModel)currentUser));
        }
        if(canRead)
        {
            allowedCatalogVersions.addAll(getCatalogVersionService().getAllReadableCatalogVersions((PrincipalModel)currentUser));
            allowedCatalogVersions.addAll((Collection<? extends CatalogVersionModel>)getCatalogVersionService().getAllCatalogVersions().stream()
                            .filter(catalogVersionModel -> (catalogVersionModel.getActive() != null && catalogVersionModel.getActive().booleanValue()))
                            .collect(Collectors.toList()));
        }
        Map<CatalogModel, Set<CatalogVersionModel>> catalogAndVersionMap = (Map<CatalogModel, Set<CatalogVersionModel>>)allowedCatalogVersions.stream().collect(Collectors.groupingBy(CatalogVersionModel::getCatalog,
                        Collectors.mapping(catalogVersion -> catalogVersion, Collectors.toSet())));
        return (Map<CatalogModel, Set<CatalogVersionModel>>)catalogAndVersionMap.entrySet().stream()
                        .filter(catalogTypePredicate)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public List<CatalogVersionModel> getWritableChildContentCatalogVersions(PrincipalModel principal, CMSSiteModel cmsSiteModel, CatalogModel catalogModel)
    {
        List<CatalogVersionModel> superCatalogVersions = getSuperCatalogsCatalogVersions((ContentCatalogModel)catalogModel, cmsSiteModel);
        List<CatalogVersionModel> currentLevelCatalogVersions = new ArrayList<>(catalogModel.getCatalogVersions());
        List<CatalogVersionModel> subCatalogVersions = getSubCatalogsCatalogVersionsInSite(cmsSiteModel, (ContentCatalogModel)catalogModel);
        Objects.requireNonNull(subCatalogVersions);
        return (List<CatalogVersionModel>)getCatalogVersionService().getAllWritableCatalogVersions(principal).stream().filter(version -> !superCatalogVersions.contains(version)).filter(version -> !currentLevelCatalogVersions.contains(version)).filter(subCatalogVersions::contains)
                        .collect(Collectors.toList());
    }


    public List<CatalogVersionModel> getWritableContentCatalogVersions(PrincipalModel principal, CatalogModel catalogModel)
    {
        Collection<CatalogVersionModel> allWritableCatalogVersions = getCatalogVersionService().getAllWritableCatalogVersions(principal);
        Objects.requireNonNull(catalogModel.getCatalogVersions());
        return (List<CatalogVersionModel>)allWritableCatalogVersions.stream().filter(catalogModel.getCatalogVersions()::contains).collect(Collectors.toList());
    }


    public List<CatalogVersionModel> getSubCatalogsCatalogVersions(ContentCatalogModel catalog)
    {
        return (List<CatalogVersionModel>)getSessionSearchRestrictionDisabler().execute(() -> (List)getCatalogLevelService().getAllSubCatalogs(catalog).stream().flatMap(()).collect(Collectors.toList()));
    }


    public List<CatalogVersionModel> getSubCatalogsCatalogVersionsInSite(CMSSiteModel site, ContentCatalogModel catalog)
    {
        List<CatalogVersionModel> subCatalogsCatalogVersions = getSubCatalogsCatalogVersions(catalog);
        List<CatalogVersionModel> siteCatalogVersions = (List<CatalogVersionModel>)site.getContentCatalogs().stream().flatMap(catalogModel -> catalogModel.getCatalogVersions().stream()).collect(Collectors.toList());
        return (List<CatalogVersionModel>)getIntersectionOfCatalogVersions(siteCatalogVersions, subCatalogsCatalogVersions);
    }


    public List<CatalogVersionModel> getSuperCatalogsCatalogVersions(ContentCatalogModel catalog, CMSSiteModel cmsSiteModel)
    {
        return (List<CatalogVersionModel>)getSessionSearchRestrictionDisabler()
                        .execute(() -> (List)getCatalogLevelService().getAllSuperCatalogs(catalog).stream().flatMap(()).collect(Collectors.toList()));
    }


    public List<CatalogVersionModel> getSuperCatalogsActiveCatalogVersions(ContentCatalogModel contentCatalog, CMSSiteModel cmsSiteModel)
    {
        Collection<ContentCatalogModel> allCatalogs = (Collection<ContentCatalogModel>)getSessionSearchRestrictionDisabler().execute(() -> getCatalogLevelService().getAllSuperCatalogs(contentCatalog));
        return (List<CatalogVersionModel>)allCatalogs.stream().map(CatalogModel::getActiveCatalogVersion).collect(Collectors.toList());
    }


    public List<CatalogVersionModel> getFullHierarchyForCatalogVersion(CatalogVersionModel catalogVersion, CMSSiteModel cmsSiteModel)
    {
        List<CatalogVersionModel> catalogVersions = getSuperCatalogsActiveCatalogVersions((ContentCatalogModel)catalogVersion
                        .getCatalog(), cmsSiteModel);
        catalogVersions.add(catalogVersion);
        return catalogVersions;
    }


    public boolean areCatalogVersionsRelatives(Collection<CatalogVersionModel> catalogVersionModels)
    {
        List<CatalogVersionModel> sortedCatalogVersions = (List<CatalogVersionModel>)catalogVersionModels.stream().filter(cv -> cv.getCatalog() instanceof ContentCatalogModel).sorted(getCmsCatalogVersionLevelComparator()).collect(Collectors.toList());
        for(int i = 1; sortedCatalogVersions.size() > 1 && i < sortedCatalogVersions.size(); i++)
        {
            if(!isContentCatalogModelAncestor((ContentCatalogModel)((CatalogVersionModel)sortedCatalogVersions.get(i)).getCatalog(), (ContentCatalogModel)((CatalogVersionModel)sortedCatalogVersions
                            .get(i - 1)).getCatalog()))
            {
                return false;
            }
        }
        return true;
    }


    public boolean isContentCatalogModelAncestor(ContentCatalogModel catalogModel, ContentCatalogModel ancestor)
    {
        int level = getCmsCatalogLevelService().getCatalogLevel(catalogModel);
        int ancestorLevel = getCmsCatalogLevelService().getCatalogLevel(ancestor);
        int diffLevel = level - ancestorLevel;
        ContentCatalogModel superCatalogModel = ancestor;
        while(diffLevel-- > 1 && Objects.nonNull(superCatalogModel))
        {
            superCatalogModel = superCatalogModel.getSuperCatalog();
        }
        return (level - ancestorLevel > 0 &&
                        Objects.nonNull(superCatalogModel) &&
                        Objects.nonNull(catalogModel.getSuperCatalog()) && catalogModel
                        .getSuperCatalog().getPk().equals(ancestor.getPk()));
    }


    public Collection<CatalogVersionModel> getIntersectionOfCatalogVersions(Collection<CatalogVersionModel> catalogVersionModelsA, Collection<CatalogVersionModel> catalogVersionModelsB)
    {
        if(Objects.nonNull(catalogVersionModelsA) && Objects.nonNull(catalogVersionModelsB))
        {
            return (Collection<CatalogVersionModel>)catalogVersionModelsA.stream()
                            .filter(cv -> !catalogVersionModelsB.stream().filter(()).findAny().isEmpty())
                            .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
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


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected CatalogLevelService getCatalogLevelService()
    {
        return this.catalogLevelService;
    }


    @Required
    public void setCatalogLevelService(CatalogLevelService catalogLevelService)
    {
        this.catalogLevelService = catalogLevelService;
    }


    protected SessionSearchRestrictionsDisabler getSessionSearchRestrictionDisabler()
    {
        return this.sessionSearchRestrictionDisabler;
    }


    @Required
    public void setSessionSearchRestrictionDisabler(SessionSearchRestrictionsDisabler sessionSearchRestrictionDisabler)
    {
        this.sessionSearchRestrictionDisabler = sessionSearchRestrictionDisabler;
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


    protected CatalogLevelService getCmsCatalogLevelService()
    {
        return this.cmsCatalogLevelService;
    }


    @Required
    public void setCmsCatalogLevelService(CatalogLevelService cmsCatalogLevelService)
    {
        this.cmsCatalogLevelService = cmsCatalogLevelService;
    }
}
