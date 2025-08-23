package de.hybris.platform.cms2lib.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2lib.enums.CarouselScroll;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class ProductCarouselComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "ProductCarouselComponent";
    public static final String SCROLL = "scroll";
    public static final String PRODUCTCODES = "productCodes";
    public static final String CATEGORYCODES = "categoryCodes";
    public static final String PRODUCTS = "products";
    public static final String CATEGORIES = "categories";
    public static final String TITLE = "title";
    public static final String SEARCHQUERY = "searchQuery";
    public static final String CATEGORYCODE = "categoryCode";
    public static final String POPUP = "popup";


    public ProductCarouselComponentModel()
    {
    }


    public ProductCarouselComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductCarouselComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductCarouselComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
    public List<CategoryModel> getCategories()
    {
        return (List<CategoryModel>)getPersistenceContext().getPropertyValue("categories");
    }


    @Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
    public String getCategoryCode()
    {
        return (String)getPersistenceContext().getPropertyValue("categoryCode");
    }


    @Accessor(qualifier = "categoryCodes", type = Accessor.Type.GETTER)
    public List<String> getCategoryCodes()
    {
        return (List<String>)getPersistenceContext().getPropertyValue("categoryCodes");
    }


    @Accessor(qualifier = "productCodes", type = Accessor.Type.GETTER)
    public List<String> getProductCodes()
    {
        return (List<String>)getPersistenceContext().getPropertyValue("productCodes");
    }


    @Accessor(qualifier = "products", type = Accessor.Type.GETTER)
    public List<ProductModel> getProducts()
    {
        return (List<ProductModel>)getPersistenceContext().getPropertyValue("products");
    }


    @Accessor(qualifier = "scroll", type = Accessor.Type.GETTER)
    public CarouselScroll getScroll()
    {
        return (CarouselScroll)getPersistenceContext().getPropertyValue("scroll");
    }


    @Accessor(qualifier = "searchQuery", type = Accessor.Type.GETTER)
    public String getSearchQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("searchQuery");
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle()
    {
        return getTitle(null);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("title", loc);
    }


    @Accessor(qualifier = "popup", type = Accessor.Type.GETTER)
    public boolean isPopup()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("popup"));
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
    public void setCategories(List<CategoryModel> value)
    {
        getPersistenceContext().setPropertyValue("categories", value);
    }


    @Accessor(qualifier = "categoryCode", type = Accessor.Type.SETTER)
    public void setCategoryCode(String value)
    {
        getPersistenceContext().setPropertyValue("categoryCode", value);
    }


    @Accessor(qualifier = "popup", type = Accessor.Type.SETTER)
    public void setPopup(boolean value)
    {
        getPersistenceContext().setPropertyValue("popup", toObject(value));
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(List<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("products", value);
    }


    @Accessor(qualifier = "scroll", type = Accessor.Type.SETTER)
    public void setScroll(CarouselScroll value)
    {
        getPersistenceContext().setPropertyValue("scroll", value);
    }


    @Accessor(qualifier = "searchQuery", type = Accessor.Type.SETTER)
    public void setSearchQuery(String value)
    {
        getPersistenceContext().setPropertyValue("searchQuery", value);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value)
    {
        setTitle(value, null);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("title", loc, value);
    }
}
