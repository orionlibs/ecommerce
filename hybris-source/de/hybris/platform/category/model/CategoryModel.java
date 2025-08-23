package de.hybris.platform.category.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.model.components.CategoryFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.VideoComponentModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductListComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class CategoryModel extends ItemModel
{
    public static final String _TYPECODE = "Category";
    public static final String _CATEGORYCATEGORYRELATION = "CategoryCategoryRelation";
    public static final String _CATEGORIESFORRESTRICTION = "CategoriesForRestriction";
    public static final String _PRODUCTLISTCOMPONENTSFORCATEGORY = "ProductListComponentsForCategory";
    public static final String _CATEGORIESFORPRODUCTCAROUSELCOMPONENT = "CategoriesForProductCarouselComponent";
    public static final String _CATEGORYFEATURECOMPONENTS2CATEGORYREL = "CategoryFeatureComponents2CategoryRel";
    public static final String DESCRIPTION = "description";
    public static final String ORDER = "order";
    public static final String CATALOG = "catalog";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String NORMAL = "normal";
    public static final String THUMBNAILS = "thumbnails";
    public static final String DETAIL = "detail";
    public static final String LOGO = "logo";
    public static final String DATA_SHEET = "data_sheet";
    public static final String OTHERS = "others";
    public static final String THUMBNAIL = "thumbnail";
    public static final String PICTURE = "picture";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String ALLSUBCATEGORIES = "allSubcategories";
    public static final String ALLSUPERCATEGORIES = "allSupercategories";
    public static final String KEYWORDS = "keywords";
    public static final String ALLOWEDPRINCIPALS = "allowedPrincipals";
    public static final String PRODUCTS = "products";
    public static final String SUPERCATEGORIES = "supercategories";
    public static final String CATEGORIES = "categories";
    public static final String MEDIAS = "medias";
    public static final String RESTRICTIONS = "restrictions";
    public static final String LINKCOMPONENTS = "linkComponents";
    public static final String VIDEOCOMPONENTS = "videoComponents";
    public static final String PRODUCTLISTCOMPONENTS = "productListComponents";
    public static final String PRODUCTCAROUSELCOMPONENTS = "productCarouselComponents";
    public static final String PROMOTIONS = "promotions";
    public static final String STOCKLEVELTHRESHOLD = "stockLevelThreshold";
    public static final String SIMPLEBANNERCOMPONENTS = "simpleBannerComponents";
    public static final String SIMPLERESPONSIVEBANNERCOMPONENTS = "simpleResponsiveBannerComponents";
    public static final String CATEGORYFEATURECOMPONENTS = "categoryFeatureComponents";


    public CategoryModel()
    {
    }


    public CategoryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CategoryModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CategoryModel(CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "allowedPrincipals", type = Accessor.Type.GETTER)
    public List<PrincipalModel> getAllowedPrincipals()
    {
        return (List<PrincipalModel>)getPersistenceContext().getPropertyValue("allowedPrincipals");
    }


    @Accessor(qualifier = "allSubcategories", type = Accessor.Type.GETTER)
    public Collection<CategoryModel> getAllSubcategories()
    {
        return (Collection<CategoryModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "allSubcategories");
    }


    @Accessor(qualifier = "allSupercategories", type = Accessor.Type.GETTER)
    public Collection<CategoryModel> getAllSupercategories()
    {
        return (Collection<CategoryModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "allSupercategories");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
    public List<CategoryModel> getCategories()
    {
        return (List<CategoryModel>)getPersistenceContext().getPropertyValue("categories");
    }


    @Accessor(qualifier = "categoryFeatureComponents", type = Accessor.Type.GETTER)
    public List<CategoryFeatureComponentModel> getCategoryFeatureComponents()
    {
        return (List<CategoryFeatureComponentModel>)getPersistenceContext().getPropertyValue("categoryFeatureComponents");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "data_sheet", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getData_sheet()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("data_sheet");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "detail", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getDetail()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("detail");
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
    public List<KeywordModel> getKeywords()
    {
        return getKeywords(null);
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
    public List<KeywordModel> getKeywords(Locale loc)
    {
        return (List<KeywordModel>)getPersistenceContext().getLocalizedRelationValue("keywords", loc);
    }


    @Accessor(qualifier = "linkComponents", type = Accessor.Type.GETTER)
    public List<CMSLinkComponentModel> getLinkComponents()
    {
        return (List<CMSLinkComponentModel>)getPersistenceContext().getPropertyValue("linkComponents");
    }


    @Accessor(qualifier = "logo", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getLogo()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("logo");
    }


    @Accessor(qualifier = "medias", type = Accessor.Type.GETTER)
    public List<MediaModel> getMedias()
    {
        return (List<MediaModel>)getPersistenceContext().getPropertyValue("medias");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "normal", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getNormal()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("normal");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public Integer getOrder()
    {
        return (Integer)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "others", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getOthers()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("others");
    }


    @Accessor(qualifier = "picture", type = Accessor.Type.GETTER)
    public MediaModel getPicture()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("picture");
    }


    @Accessor(qualifier = "productCarouselComponents", type = Accessor.Type.GETTER)
    public Collection<ProductCarouselComponentModel> getProductCarouselComponents()
    {
        return (Collection<ProductCarouselComponentModel>)getPersistenceContext().getPropertyValue("productCarouselComponents");
    }


    @Accessor(qualifier = "productListComponents", type = Accessor.Type.GETTER)
    public List<ProductListComponentModel> getProductListComponents()
    {
        return (List<ProductListComponentModel>)getPersistenceContext().getPropertyValue("productListComponents");
    }


    @Accessor(qualifier = "products", type = Accessor.Type.GETTER)
    public List<ProductModel> getProducts()
    {
        return (List<ProductModel>)getPersistenceContext().getPropertyValue("products");
    }


    @Accessor(qualifier = "promotions", type = Accessor.Type.GETTER)
    public Collection<ProductPromotionModel> getPromotions()
    {
        return (Collection<ProductPromotionModel>)getPersistenceContext().getPropertyValue("promotions");
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public Collection<CMSCategoryRestrictionModel> getRestrictions()
    {
        return (Collection<CMSCategoryRestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "simpleBannerComponents", type = Accessor.Type.GETTER)
    public List<SimpleBannerComponentModel> getSimpleBannerComponents()
    {
        return (List<SimpleBannerComponentModel>)getPersistenceContext().getPropertyValue("simpleBannerComponents");
    }


    @Accessor(qualifier = "simpleResponsiveBannerComponents", type = Accessor.Type.GETTER)
    public List<SimpleResponsiveBannerComponentModel> getSimpleResponsiveBannerComponents()
    {
        return (List<SimpleResponsiveBannerComponentModel>)getPersistenceContext().getPropertyValue("simpleResponsiveBannerComponents");
    }


    @Accessor(qualifier = "stockLevelThreshold", type = Accessor.Type.GETTER)
    public Integer getStockLevelThreshold()
    {
        return (Integer)getPersistenceContext().getPropertyValue("stockLevelThreshold");
    }


    @Accessor(qualifier = "supercategories", type = Accessor.Type.GETTER)
    public List<CategoryModel> getSupercategories()
    {
        return (List<CategoryModel>)getPersistenceContext().getPropertyValue("supercategories");
    }


    @Accessor(qualifier = "thumbnail", type = Accessor.Type.GETTER)
    public MediaModel getThumbnail()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("thumbnail");
    }


    @Accessor(qualifier = "thumbnails", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getThumbnails()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("thumbnails");
    }


    @Accessor(qualifier = "videoComponents", type = Accessor.Type.GETTER)
    public List<VideoComponentModel> getVideoComponents()
    {
        return (List<VideoComponentModel>)getPersistenceContext().getPropertyValue("videoComponents");
    }


    @Accessor(qualifier = "allowedPrincipals", type = Accessor.Type.SETTER)
    public void setAllowedPrincipals(List<PrincipalModel> value)
    {
        getPersistenceContext().setPropertyValue("allowedPrincipals", value);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
    public void setCategories(List<CategoryModel> value)
    {
        getPersistenceContext().setPropertyValue("categories", value);
    }


    @Accessor(qualifier = "categoryFeatureComponents", type = Accessor.Type.SETTER)
    public void setCategoryFeatureComponents(List<CategoryFeatureComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("categoryFeatureComponents", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "data_sheet", type = Accessor.Type.SETTER)
    public void setData_sheet(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("data_sheet", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "detail", type = Accessor.Type.SETTER)
    public void setDetail(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("detail", value);
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
    public void setKeywords(List<KeywordModel> value)
    {
        setKeywords(value, null);
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
    public void setKeywords(List<KeywordModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("keywords", loc, value);
    }


    @Accessor(qualifier = "linkComponents", type = Accessor.Type.SETTER)
    public void setLinkComponents(List<CMSLinkComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("linkComponents", value);
    }


    @Accessor(qualifier = "logo", type = Accessor.Type.SETTER)
    public void setLogo(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("logo", value);
    }


    @Accessor(qualifier = "medias", type = Accessor.Type.SETTER)
    public void setMedias(List<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("medias", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "normal", type = Accessor.Type.SETTER)
    public void setNormal(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("normal", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(Integer value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "others", type = Accessor.Type.SETTER)
    public void setOthers(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("others", value);
    }


    @Accessor(qualifier = "picture", type = Accessor.Type.SETTER)
    public void setPicture(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("picture", value);
    }


    @Accessor(qualifier = "productCarouselComponents", type = Accessor.Type.SETTER)
    public void setProductCarouselComponents(Collection<ProductCarouselComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("productCarouselComponents", value);
    }


    @Accessor(qualifier = "productListComponents", type = Accessor.Type.SETTER)
    public void setProductListComponents(List<ProductListComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("productListComponents", value);
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(List<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("products", value);
    }


    @Accessor(qualifier = "promotions", type = Accessor.Type.SETTER)
    public void setPromotions(Collection<ProductPromotionModel> value)
    {
        getPersistenceContext().setPropertyValue("promotions", value);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(Collection<CMSCategoryRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }


    @Accessor(qualifier = "simpleBannerComponents", type = Accessor.Type.SETTER)
    public void setSimpleBannerComponents(List<SimpleBannerComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("simpleBannerComponents", value);
    }


    @Accessor(qualifier = "simpleResponsiveBannerComponents", type = Accessor.Type.SETTER)
    public void setSimpleResponsiveBannerComponents(List<SimpleResponsiveBannerComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("simpleResponsiveBannerComponents", value);
    }


    @Accessor(qualifier = "stockLevelThreshold", type = Accessor.Type.SETTER)
    public void setStockLevelThreshold(Integer value)
    {
        getPersistenceContext().setPropertyValue("stockLevelThreshold", value);
    }


    @Accessor(qualifier = "supercategories", type = Accessor.Type.SETTER)
    public void setSupercategories(List<CategoryModel> value)
    {
        getPersistenceContext().setPropertyValue("supercategories", value);
    }


    @Accessor(qualifier = "thumbnail", type = Accessor.Type.SETTER)
    public void setThumbnail(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("thumbnail", value);
    }


    @Accessor(qualifier = "thumbnails", type = Accessor.Type.SETTER)
    public void setThumbnails(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("thumbnails", value);
    }


    @Accessor(qualifier = "videoComponents", type = Accessor.Type.SETTER)
    public void setVideoComponents(List<VideoComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("videoComponents", value);
    }
}
