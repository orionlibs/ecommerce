package de.hybris.platform.cmscockpit.components.liveedit.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCatalogRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.components.liveedit.PreviewLoader;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;

public class DefaultPreviewLoader implements PreviewLoader
{
    private static final Logger LOG = Logger.getLogger(DefaultPreviewLoader.class);
    private CMSAdminRestrictionService restrService;
    private CMSAdminSiteService cmsAdminSiteService;


    public void loadValues(PreviewDataModel previewCtx, AbstractPageModel page, Collection<CatalogVersionModel> catVersions, boolean liveEdit, LanguageModel language, String resourcePath)
    {
        if(previewCtx == null)
        {
            throw new IllegalArgumentException("Preview context can not be null.");
        }
        if(CollectionUtils.isEmpty(previewCtx.getCatalogVersions()))
        {
            List<CatalogVersionModel> versions = new ArrayList<>(catVersions);
            previewCtx.setCatalogVersions(versions);
        }
        previewCtx.setLiveEdit(Boolean.valueOf(liveEdit));
        previewCtx.setResourcePath(resourcePath);
        previewCtx.setLanguage(language);
        previewCtx.setActiveSite(getAdminSiteService().getActiveSite());
        previewCtx.setActiveCatalogVersion(getAdminSiteService().getActiveCatalogVersion());
        if(page != null)
        {
            previewCtx.setPage(page);
        }
        try
        {
            loadRestrictionBasedValues(previewCtx, previewCtx.getPage());
        }
        catch(CMSItemNotFoundException cinfe)
        {
            LOG.warn("Restriction based preview values could not be set. Reason: " + cinfe.getMessage(), (Throwable)cinfe);
        }
    }


    protected boolean loadRestrictionBasedValues(PreviewDataModel previewCtx, AbstractPageModel page) throws CMSItemNotFoundException
    {
        boolean oneRestrictionApplied = false;
        if(page == null)
        {
            return oneRestrictionApplied;
        }
        oneRestrictionApplied = loadCommonRestrictionBaseValues(previewCtx, page);
        if(!oneRestrictionApplied)
        {
            if(page instanceof de.hybris.platform.cms2.model.pages.CatalogPageModel)
            {
                Collection<AbstractRestrictionModel> restrictions = new ArrayList<>(getRestrictionService().getRestrictionsForPage(page, GeneratedCms2Constants.TC.CMSCATALOGRESTRICTION));
                if(!restrictions.isEmpty())
                {
                    for(AbstractRestrictionModel restriction : restrictions)
                    {
                        if(oneRestrictionApplied && page.isOnlyOneRestrictionMustApply())
                        {
                            return oneRestrictionApplied;
                        }
                        if(restriction instanceof CMSCatalogRestrictionModel)
                        {
                            loadCatalogRestrictionForCatalogPage(previewCtx, (CMSCatalogRestrictionModel)restriction);
                            oneRestrictionApplied = true;
                        }
                    }
                }
            }
            else if(page instanceof de.hybris.platform.cms2.model.pages.CategoryPageModel)
            {
                Collection<AbstractRestrictionModel> restrictions = new ArrayList<>(getRestrictionService().getRestrictionsForPage(page, GeneratedCms2Constants.TC.CMSCATALOGRESTRICTION));
                restrictions.addAll(getRestrictionService().getRestrictionsForPage(page, GeneratedCms2Constants.TC.CMSCATEGORYRESTRICTION));
                if(!restrictions.isEmpty())
                {
                    for(AbstractRestrictionModel restriction : restrictions)
                    {
                        if(oneRestrictionApplied && page.isOnlyOneRestrictionMustApply())
                        {
                            return oneRestrictionApplied;
                        }
                        if(restriction instanceof CMSCategoryRestrictionModel)
                        {
                            loadCategoryRestrictionForCategoryPage(previewCtx, (CMSCategoryRestrictionModel)restriction);
                            oneRestrictionApplied = true;
                            continue;
                        }
                        if(restriction instanceof CMSCatalogRestrictionModel)
                        {
                            loadCatalogRestrictionForCategoryPage(previewCtx, (CMSCatalogRestrictionModel)restriction);
                            oneRestrictionApplied = true;
                        }
                    }
                }
            }
            else if(page instanceof de.hybris.platform.cms2.model.pages.ProductPageModel)
            {
                Collection<AbstractRestrictionModel> restrictions = new ArrayList<>(getRestrictionService().getRestrictionsForPage(page, GeneratedCms2Constants.TC.CMSCATALOGRESTRICTION));
                restrictions.addAll(getRestrictionService().getRestrictionsForPage(page, GeneratedCms2Constants.TC.CMSCATEGORYRESTRICTION));
                restrictions.addAll(getRestrictionService().getRestrictionsForPage(page, GeneratedCms2Constants.TC.CMSPRODUCTRESTRICTION));
                if(!restrictions.isEmpty())
                {
                    for(AbstractRestrictionModel restriction : restrictions)
                    {
                        if(oneRestrictionApplied && page.isOnlyOneRestrictionMustApply())
                        {
                            return oneRestrictionApplied;
                        }
                        if(restriction instanceof CMSCatalogRestrictionModel)
                        {
                            loadCatalogRestrictionForProductPage(previewCtx, (CMSCatalogRestrictionModel)restriction);
                            oneRestrictionApplied = true;
                        }
                        else if(restriction instanceof CMSCategoryRestrictionModel)
                        {
                            loadCategoryRestrictionForProductPage(previewCtx, (CMSCategoryRestrictionModel)restriction);
                            oneRestrictionApplied = true;
                        }
                        if(restriction instanceof CMSProductRestrictionModel)
                        {
                            loadProductRestrictionForProductPage(previewCtx, (CMSProductRestrictionModel)restriction);
                            oneRestrictionApplied = true;
                        }
                    }
                }
            }
        }
        return (oneRestrictionApplied && page.isOnlyOneRestrictionMustApply());
    }


    protected void loadCatalogRestrictionForCatalogPage(PreviewDataModel previewCtx, CMSCatalogRestrictionModel catalogRestriction)
    {
        Collection<CatalogModel> catalogs = catalogRestriction.getCatalogs();
        if(catalogs != null && !catalogs.isEmpty())
        {
            previewCtx.setPreviewCatalog(catalogs.iterator().next());
        }
    }


    protected void loadCatalogRestrictionForCategoryPage(PreviewDataModel previewCtx, CMSCatalogRestrictionModel catalogRestriction)
    {
        Collection<CatalogModel> catalogs = catalogRestriction.getCatalogs();
        if(catalogs != null && !catalogs.isEmpty())
        {
            CatalogModel catalogModel = catalogs.iterator().next();
            CategoryModel initialCategory = null;
            List<CategoryModel> rootCategories = catalogModel.getActiveCatalogVersion().getRootCategories();
            if(!rootCategories.isEmpty())
            {
                initialCategory = rootCategories.iterator().next();
            }
            previewCtx.setPreviewCatalog(catalogs.iterator().next());
            previewCtx.setPreviewCategory(initialCategory);
            previewCtx.setPreviewProduct(null);
        }
    }


    protected void loadCatalogRestrictionForProductPage(PreviewDataModel previewCtx, CMSCatalogRestrictionModel catalogRestriction)
    {
        Collection<CatalogModel> catalogs = catalogRestriction.getCatalogs();
        if(catalogs != null && !catalogs.isEmpty())
        {
            CatalogModel catalogModel = catalogs.iterator().next();
            CategoryModel initialCategory = null;
            List<CategoryModel> rootCategories = catalogModel.getRootCategories();
            if(!rootCategories.isEmpty())
            {
                initialCategory = rootCategories.iterator().next();
                List<ProductModel> products = new ArrayList<>(initialCategory.getProducts());
                if(products.isEmpty())
                {
                    products.addAll(findProductWithinSubCategories(initialCategory.getAllSubcategories()));
                }
                if(!products.isEmpty())
                {
                    previewCtx.setPreviewProduct(products.iterator().next());
                }
            }
            previewCtx.setPreviewCatalog(catalogModel);
            previewCtx.setPreviewCategory(initialCategory);
        }
    }


    protected void loadCategoryRestrictionForCategoryPage(PreviewDataModel previewCtx, CMSCategoryRestrictionModel categoryRestriction)
    {
        Collection<CategoryModel> categories = getRestrictionService().getCategories(categoryRestriction, previewCtx);
        if(categories != null && !categories.isEmpty())
        {
            previewCtx.setPreviewCategory(categories.iterator().next());
        }
    }


    protected void loadCategoryRestrictionForProductPage(PreviewDataModel previewCtx, CMSCategoryRestrictionModel categoryRestriction)
    {
        Collection<CategoryModel> categories = getRestrictionService().getCategories(categoryRestriction, previewCtx);
        if(categories != null && !categories.isEmpty())
        {
            CategoryModel initialCategory = categories.iterator().next();
            previewCtx.setPreviewCatalog(initialCategory.getCatalogVersion().getCatalog());
            previewCtx.setPreviewCategory(initialCategory);
            List<ProductModel> products = new ArrayList<>(initialCategory.getProducts());
            if(categoryRestriction.isRecursive() && products.isEmpty())
            {
                products.addAll(findProductWithinSubCategories(initialCategory.getAllSubcategories()));
            }
            else
            {
                products.addAll(initialCategory.getProducts());
            }
            if(!products.isEmpty())
            {
                previewCtx.setPreviewProduct(products.iterator().next());
            }
        }
    }


    protected List<ProductModel> findProductWithinSubCategories(Collection<CategoryModel> categories)
    {
        List<ProductModel> ret = new ArrayList<>();
        for(CategoryModel category : categories)
        {
            if(!category.getProducts().isEmpty())
            {
                ret.addAll(category.getProducts());
                break;
            }
        }
        return ret;
    }


    protected void loadProductRestrictionForProductPage(PreviewDataModel previewCtx, CMSProductRestrictionModel productRestriction)
    {
        Collection<ProductModel> products = getRestrictionService().getProducts(productRestriction, previewCtx);
        if(products != null && !products.isEmpty())
        {
            previewCtx.setPreviewProduct(products.iterator().next());
        }
    }


    protected void loadUserRestrictionBaseValues(PreviewDataModel previewCtx, CMSUserRestrictionModel userRestriction)
    {
        Collection<UserModel> userModels = userRestriction.getUsers();
        if(!userModels.isEmpty())
        {
            previewCtx.setUser(userModels.iterator().next());
        }
    }


    protected void loadUserGroupRestrictionBaseValues(PreviewDataModel previewCtx, CMSUserGroupRestrictionModel userGroupRestriction)
    {
        Collection<UserGroupModel> userGroupModels = userGroupRestriction.getUserGroups();
        if(!userGroupModels.isEmpty())
        {
            previewCtx.setUserGroup(userGroupModels.iterator().next());
        }
    }


    protected void loadTimeRestrictionBaseValues(PreviewDataModel previewCtx, CMSTimeRestrictionModel timeRestriction)
    {
        Date previewContextTime = new Date();
        if(timeRestriction.getActiveFrom() != null)
        {
            long activeFromInMilis = timeRestriction.getActiveFrom().getTime() + 1000L;
            previewContextTime = new Date(activeFromInMilis);
        }
        else if(timeRestriction.getActiveUntil() != null)
        {
            long activeToInMilis = timeRestriction.getActiveUntil().getTime() - 1000L;
            previewContextTime = new Date(activeToInMilis);
        }
        previewCtx.setTime(previewContextTime);
    }


    protected boolean loadCommonRestrictionBaseValues(PreviewDataModel previewCtx, AbstractPageModel page)
    {
        boolean oneRestrictionApplied = false;
        previewCtx.setPreviewCatalog(null);
        previewCtx.setPreviewCategory(null);
        previewCtx.setPreviewProduct(null);
        if(!page.getRestrictions().isEmpty())
        {
            previewCtx.setUser(null);
            previewCtx.setUserGroup(null);
            previewCtx.setTime(new Date());
        }
        Collection<AbstractRestrictionModel> restrictions = page.getRestrictions();
        if(restrictions != null && !restrictions.isEmpty())
        {
            for(AbstractRestrictionModel restriction : restrictions)
            {
                if(oneRestrictionApplied && page.isOnlyOneRestrictionMustApply())
                {
                    return oneRestrictionApplied;
                }
                if(restriction instanceof CMSTimeRestrictionModel)
                {
                    loadTimeRestrictionBaseValues(previewCtx, (CMSTimeRestrictionModel)restriction);
                    oneRestrictionApplied = true;
                    continue;
                }
                if(restriction instanceof CMSUserRestrictionModel)
                {
                    loadUserRestrictionBaseValues(previewCtx, (CMSUserRestrictionModel)restriction);
                    oneRestrictionApplied = true;
                    continue;
                }
                if(restriction instanceof CMSUserGroupRestrictionModel)
                {
                    loadUserGroupRestrictionBaseValues(previewCtx, (CMSUserGroupRestrictionModel)restriction);
                    oneRestrictionApplied = true;
                }
            }
        }
        return (oneRestrictionApplied && page.isOnlyOneRestrictionMustApply());
    }


    protected <T> boolean isUserRestrictionIsApplicable(Class<T> restrictionClazz, Collection<AbstractRestrictionModel> restrictions)
    {
        boolean ret = false;
        for(AbstractRestrictionModel restrictionModel : restrictions)
        {
            ret = restrictionModel.getClass().equals(restrictionClazz);
            if(ret)
            {
                break;
            }
        }
        return ret;
    }


    protected CMSAdminRestrictionService getRestrictionService()
    {
        if(this.restrService == null)
        {
            this.restrService = (CMSAdminRestrictionService)SpringUtil.getBean("cmsAdminRestrictionService");
        }
        return this.restrService;
    }


    protected CMSAdminSiteService getAdminSiteService()
    {
        if(this.cmsAdminSiteService == null)
        {
            this.cmsAdminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.cmsAdminSiteService;
    }
}
