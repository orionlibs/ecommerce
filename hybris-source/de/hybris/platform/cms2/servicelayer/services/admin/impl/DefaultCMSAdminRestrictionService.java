package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemCreateException;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.RestrictionTypeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCampaignRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCatalogRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserRestrictionModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSRestrictionDao;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSAdminRestrictionService extends AbstractCMSAdminService implements CMSAdminRestrictionService
{
    private static final Logger LOG = Logger.getLogger(DefaultCMSAdminRestrictionService.class.getName());
    protected static final String CATALOG_VERSIONS = "catalogversions";
    private TypeService typeService;
    private ProductService productService;
    private CategoryService categoryService;
    private CMSAdminPageService cmsAdminPageService;
    private CMSRestrictionDao cmsRestrictionDao;
    private CMSRestrictionService cmsRestrictionService;
    private I18NService i18nService;
    private CommonI18NService commonI18NService;


    public Collection<RestrictionTypeModel> getAllRestrictionTypes()
    {
        return (Collection<RestrictionTypeModel>)getTypeService().getComposedTypeForClass(AbstractRestrictionModel.class).getAllSubTypes().stream()
                        .map(composedType -> (RestrictionTypeModel)composedType)
                        .collect(Collectors.toList());
    }


    public CMSCatalogRestrictionModel createCatalogRestriction(AbstractPageModel page, String id, String name, Collection<CatalogModel> catalogs) throws CMSItemCreateException
    {
        CMSCatalogRestrictionModel res = new CMSCatalogRestrictionModel();
        prepareRestricion((AbstractRestrictionModel)res, id, name);
        res.setCatalogs(catalogs);
        getModelService().save(res);
        createRelation(page, (AbstractRestrictionModel)res);
        return res;
    }


    public CMSCategoryRestrictionModel createCategoryRestriction(AbstractPageModel page, String id, String name, Collection<CategoryModel> categories) throws CMSItemCreateException
    {
        CMSCategoryRestrictionModel res = new CMSCategoryRestrictionModel();
        prepareRestricion((AbstractRestrictionModel)res, id, name);
        res.setCategories(categories);
        getModelService().save(res);
        createRelation(page, (AbstractRestrictionModel)res);
        return res;
    }


    public CMSProductRestrictionModel createProductRestriction(AbstractPageModel page, String id, String name, Collection<ProductModel> products) throws CMSItemCreateException
    {
        CMSProductRestrictionModel res = new CMSProductRestrictionModel();
        prepareRestricion((AbstractRestrictionModel)res, id, name);
        res.setProducts(products);
        getModelService().save(res);
        createRelation(page, (AbstractRestrictionModel)res);
        return res;
    }


    public void createRelation(AbstractPageModel page, AbstractRestrictionModel restriction)
    {
        getModelService().save(restriction);
        List<AbstractRestrictionModel> restrictions = new ArrayList<>(page.getRestrictions());
        restrictions.add(restriction);
        page.setRestrictions(restrictions);
        getModelService().save(page);
    }


    public void createRelation(String pageId, String restrictionId) throws CMSItemNotFoundException
    {
        AbstractPageModel page = getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(pageId);
        AbstractRestrictionModel restriction = getRestriction(restrictionId);
        createRelation(page, restriction);
    }


    public CMSTimeRestrictionModel createTimeRestriction(AbstractPageModel page, String id, String name, Date from, Date until) throws CMSItemCreateException
    {
        CMSTimeRestrictionModel res = new CMSTimeRestrictionModel();
        prepareRestricion((AbstractRestrictionModel)res, id, name);
        res.setActiveFrom(from);
        res.setActiveUntil(until);
        getModelService().save(res);
        createRelation(page, (AbstractRestrictionModel)res);
        return res;
    }


    public CMSUserGroupRestrictionModel createUserGroupRestriction(AbstractPageModel page, String id, String name, Collection<UserGroupModel> userGroups) throws CMSItemCreateException
    {
        CMSUserGroupRestrictionModel res = new CMSUserGroupRestrictionModel();
        prepareRestricion((AbstractRestrictionModel)res, id, name);
        res.setUserGroups(userGroups);
        getModelService().save(res);
        createRelation(page, (AbstractRestrictionModel)res);
        return res;
    }


    public CMSUserRestrictionModel createUserRestriction(AbstractPageModel page, String id, String name, Collection<UserModel> users) throws CMSItemCreateException
    {
        CMSUserRestrictionModel res = new CMSUserRestrictionModel();
        prepareRestricion((AbstractRestrictionModel)res, id, name);
        res.setUsers(users);
        getModelService().save(res);
        createRelation(page, (AbstractRestrictionModel)res);
        return res;
    }


    public CMSCampaignRestrictionModel createCampaignRestriction(AbstractPageModel page, String id, String name, Collection<CampaignModel> campaigns) throws CMSItemCreateException
    {
        CMSCampaignRestrictionModel res = (CMSCampaignRestrictionModel)getModelService().create(CMSCampaignRestrictionModel.class);
        prepareRestricion((AbstractRestrictionModel)res, id, name);
        res.setCampaigns(campaigns);
        getModelService().save(res);
        createRelation(page, (AbstractRestrictionModel)res);
        return res;
    }


    public void deleteRelation(AbstractRestrictionModel restriction, AbstractPageModel page)
    {
        List<AbstractRestrictionModel> res = new ArrayList<>(page.getRestrictions());
        res.remove(restriction);
        page.setRestrictions(res);
        getModelService().save(page);
    }


    public void deleteRelation(String restrictionId, String pageId) throws CMSItemNotFoundException
    {
        AbstractPageModel page = getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(pageId);
        AbstractRestrictionModel restriction = getRestriction(restrictionId);
        deleteRelation(restriction, page);
    }


    public Collection<RestrictionTypeModel> getAllowedRestrictionTypesForPage(AbstractPageModel page)
    {
        ServicesUtil.validateParameterNotNull(page, "page can not be null");
        Collection<RestrictionTypeModel> restrictionTypes = null;
        ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(page.getClass());
        if(composedType instanceof CMSPageTypeModel)
        {
            CMSPageTypeModel cmsPageType = (CMSPageTypeModel)composedType;
            restrictionTypes = cmsPageType.getRestrictionTypes();
            return restrictionTypes;
        }
        throw new IllegalArgumentException("unexpected type of page , expected CMSPageTypeModel");
    }


    public Collection<RestrictionTypeModel> getAllowedRestrictionTypesForPage(String id) throws CMSItemNotFoundException
    {
        return getAllowedRestrictionTypesForPage(getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(id));
    }


    public Collection<AbstractRestrictionModel> getAllRestrictionsByType(String restrictionType) throws CMSItemNotFoundException
    {
        return getCmsRestrictionDao().findRestrictionsByType(getComposedTypeModel(restrictionType), getActiveCatalogVersion());
    }


    public Collection<AbstractRestrictionModel> getAllRestrictionsByTypeNotLinkedToPage(String pageId, String restrictionType) throws CMSItemNotFoundException
    {
        AbstractPageModel page = getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(pageId);
        return getCmsRestrictionDao().findRestrictionsByTypeNotLinkedToPage(page, getComposedTypeModel(restrictionType),
                        getActiveCatalogVersion());
    }


    public List<CategoryModel> getCategories(CMSCategoryRestrictionModel categoryRestriction, PreviewDataModel previewContext)
    {
        List<CategoryModel> ret = new ArrayList<>();
        Collection<String> categoryListCodes = this.cmsRestrictionService.getCategoryCodesForRestriction(categoryRestriction);
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, previewContext, ret, categoryListCodes),
                        getLocalUser(previewContext));
        return ret;
    }


    protected void findCategoriesInLocalSession(PreviewDataModel previewContext, List<CategoryModel> ret, Collection<String> categoryListCodes)
    {
        getSessionService().setAttribute("catalogversions", previewContext.getCatalogVersions());
        this.i18nService.setCurrentLocale(getLocalLocale(previewContext));
        if(categoryListCodes != null)
        {
            for(String code : categoryListCodes)
            {
                try
                {
                    String productCode = code.contains("/") ? StringUtils.substringAfterLast(code, "/") : code;
                    CategoryModel product = getCategoryService().getCategoryForCode(productCode);
                    ret.add(product);
                }
                catch(SystemException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Could not get category with code <" + code + ">: " + e.getMessage(), (Throwable)e);
                    }
                }
            }
        }
    }


    public List<ProductModel> getProducts(CMSProductRestrictionModel productRestriction, PreviewDataModel previewContext)
    {
        List<ProductModel> ret = new ArrayList<>();
        Collection<String> productListCodes = this.cmsRestrictionService.getProductCodesForRestriction(productRestriction);
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, previewContext, ret, productListCodes),
                        getLocalUser(previewContext));
        return ret;
    }


    protected void findProductsInLocalSession(PreviewDataModel previewContext, List<ProductModel> ret, Collection<String> productListCodes)
    {
        getSessionService().setAttribute("catalogversions", previewContext.getCatalogVersions());
        this.i18nService.setCurrentLocale(getLocalLocale(previewContext));
        if(productListCodes != null)
        {
            for(String code : productListCodes)
            {
                try
                {
                    String productCode = code.contains("/") ? StringUtils.substringAfterLast(code, "/") : code;
                    ProductModel product = getProductService().getProductForCode(productCode);
                    ret.add(product);
                }
                catch(SystemException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Could not get product with code <" + code + ">: " + e.getMessage(), (Throwable)e);
                    }
                }
            }
        }
    }


    public AbstractRestrictionModel getRestriction(String id) throws CMSItemNotFoundException
    {
        List<AbstractRestrictionModel> restrictions = getCmsRestrictionDao().findRestrictionsById(id,
                        getActiveCatalogVersion());
        if(restrictions.isEmpty())
        {
            throw new CMSItemNotFoundException("Could not find restriction with id [" + id + "] in active catalog version");
        }
        return restrictions.get(0);
    }


    public List<AbstractRestrictionModel> getRestrictionsByName(String name) throws CMSItemNotFoundException
    {
        List<AbstractRestrictionModel> restrictions = getCmsRestrictionDao().findRestrictionsByName(name,
                        getActiveCatalogVersion());
        if(restrictions.isEmpty())
        {
            throw new CMSItemNotFoundException("Could not find restriction with name [" + name + "] in active catalog version");
        }
        return restrictions;
    }


    public Collection<AbstractRestrictionModel> getRestrictionsForPage(AbstractPageModel page, String restrictionType) throws CMSItemNotFoundException
    {
        return getCmsRestrictionDao().findRestrictionsForPage(getComposedTypeModel(restrictionType), page,
                        getActiveCatalogVersion());
    }


    public Collection<AbstractRestrictionModel> getRestrictionsForPage(String id, String restrictionType) throws CMSItemNotFoundException
    {
        return getRestrictionsForPage(getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(id), restrictionType);
    }


    public boolean hasOtherRelations(AbstractRestrictionModel restriction, AbstractPageModel page)
    {
        for(AbstractPageModel rpage : restriction.getPages())
        {
            if(!rpage.getUid().equals(page.getUid()))
            {
                return true;
            }
        }
        return false;
    }


    public boolean hasOtherRelations(String restrictionId, String pageId) throws CMSItemNotFoundException
    {
        AbstractPageModel page = getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(pageId);
        AbstractRestrictionModel restriction = getRestriction(restrictionId);
        return hasOtherRelations(restriction, page);
    }


    public Collection<AbstractRestrictionModel> getAllRestrictions()
    {
        return getCmsRestrictionDao().findRestrictions(getActiveCatalogVersion());
    }


    public Collection<AbstractRestrictionModel> getRestrictionsForPage(AbstractPageModel page)
    {
        return getCmsRestrictionDao().findRestrictionsForPage(page, getActiveCatalogVersion());
    }


    public boolean hasPageRestrictions(AbstractPageModel page)
    {
        return (getCmsRestrictionDao().getNumPageRestrictions(page, getActiveCatalogVersion()) > 0);
    }


    protected CategoryService getCategoryService()
    {
        return this.categoryService;
    }


    @Required
    public void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }


    protected CMSAdminPageService getCmsAdminPageService()
    {
        return this.cmsAdminPageService;
    }


    @Required
    public void setCmsAdminPageService(CMSAdminPageService cmsAdminPageService)
    {
        this.cmsAdminPageService = cmsAdminPageService;
    }


    protected CMSRestrictionDao getCmsRestrictionDao()
    {
        return this.cmsRestrictionDao;
    }


    @Required
    public void setCmsRestrictionDao(CMSRestrictionDao cmsRestrictionDao)
    {
        this.cmsRestrictionDao = cmsRestrictionDao;
    }


    protected CMSRestrictionService getCmsRestrictionService()
    {
        return this.cmsRestrictionService;
    }


    @Required
    public void setCmsRestrictionService(CMSRestrictionService cmsRestrictionService)
    {
        this.cmsRestrictionService = cmsRestrictionService;
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


    protected I18NService getI18nService()
    {
        return this.i18nService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    protected ProductService getProductService()
    {
        return this.productService;
    }


    @Required
    public void setProductService(ProductService productService)
    {
        this.productService = productService;
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


    protected ComposedTypeModel getComposedTypeModel(String composedType) throws CMSItemNotFoundException
    {
        try
        {
            return getTypeService().getComposedTypeForCode(composedType);
        }
        catch(Exception e)
        {
            throw new CMSItemNotFoundException("No composed type [" + composedType + "] found", e);
        }
    }


    protected Locale getLocalLocale(PreviewDataModel previewContext)
    {
        LanguageModel language = previewContext.getLanguage();
        if(language == null)
        {
            language = this.commonI18NService.getCurrentLanguage();
        }
        return this.commonI18NService.getLocaleForLanguage(language);
    }


    protected UserModel getLocalUser(PreviewDataModel previewContext)
    {
        UserModel user = previewContext.getUser();
        if(user == null)
        {
            return (UserModel)getUserService().getAnonymousUser();
        }
        return user;
    }


    protected void prepareRestricion(AbstractRestrictionModel res, String id, String name) throws CMSItemCreateException
    {
        if(StringUtils.isEmpty(id))
        {
            throw new CMSItemCreateException("Id must not be empty");
        }
        if(StringUtils.isEmpty(name))
        {
            throw new CMSItemCreateException("name must not be empty");
        }
        res.setUid(id);
        res.setName(name);
        res.setCatalogVersion(getActiveCatalogVersion());
    }
}
