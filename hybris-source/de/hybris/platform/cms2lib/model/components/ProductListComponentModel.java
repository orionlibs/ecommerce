package de.hybris.platform.cms2lib.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2lib.enums.ProductListLayouts;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class ProductListComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "ProductListComponent";
    public static final String HEADLINE = "headline";
    public static final String CATEGORYCODE = "categoryCode";
    public static final String PRODUCTSFROMCONTEXT = "productsFromContext";
    public static final String SEARCHQUERY = "searchQuery";
    public static final String PAGINATION = "pagination";
    public static final String LAYOUT = "layout";
    public static final String PRODUCTCODES = "productCodes";
    public static final String PRODUCTS = "products";
    public static final String CATEGORY = "category";


    public ProductListComponentModel()
    {
    }


    public ProductListComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductListComponentModel(CatalogVersionModel _catalogVersion, boolean _pagination, boolean _productsFromContext, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setPagination(_pagination);
        setProductsFromContext(_productsFromContext);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductListComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, boolean _pagination, boolean _productsFromContext, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setPagination(_pagination);
        setProductsFromContext(_productsFromContext);
        setUid(_uid);
    }


    @Accessor(qualifier = "category", type = Accessor.Type.GETTER)
    public CategoryModel getCategory()
    {
        return (CategoryModel)getPersistenceContext().getPropertyValue("category");
    }


    @Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
    public String getCategoryCode()
    {
        return (String)getPersistenceContext().getPropertyValue("categoryCode");
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.GETTER)
    public String getHeadline()
    {
        return getHeadline(null);
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.GETTER)
    public String getHeadline(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("headline", loc);
    }


    @Accessor(qualifier = "layout", type = Accessor.Type.GETTER)
    public ProductListLayouts getLayout()
    {
        return (ProductListLayouts)getPersistenceContext().getPropertyValue("layout");
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


    @Accessor(qualifier = "searchQuery", type = Accessor.Type.GETTER)
    public String getSearchQuery()
    {
        return getSearchQuery(null);
    }


    @Accessor(qualifier = "searchQuery", type = Accessor.Type.GETTER)
    public String getSearchQuery(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("searchQuery", loc);
    }


    @Accessor(qualifier = "pagination", type = Accessor.Type.GETTER)
    public boolean isPagination()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("pagination"));
    }


    @Accessor(qualifier = "productsFromContext", type = Accessor.Type.GETTER)
    public boolean isProductsFromContext()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("productsFromContext"));
    }


    @Accessor(qualifier = "category", type = Accessor.Type.SETTER)
    public void setCategory(CategoryModel value)
    {
        getPersistenceContext().setPropertyValue("category", value);
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.SETTER)
    public void setHeadline(String value)
    {
        setHeadline(value, null);
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.SETTER)
    public void setHeadline(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("headline", loc, value);
    }


    @Accessor(qualifier = "layout", type = Accessor.Type.SETTER)
    public void setLayout(ProductListLayouts value)
    {
        getPersistenceContext().setPropertyValue("layout", value);
    }


    @Accessor(qualifier = "pagination", type = Accessor.Type.SETTER)
    public void setPagination(boolean value)
    {
        getPersistenceContext().setPropertyValue("pagination", toObject(value));
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(List<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("products", value);
    }


    @Accessor(qualifier = "productsFromContext", type = Accessor.Type.SETTER)
    public void setProductsFromContext(boolean value)
    {
        getPersistenceContext().setPropertyValue("productsFromContext", toObject(value));
    }


    @Accessor(qualifier = "searchQuery", type = Accessor.Type.SETTER)
    public void setSearchQuery(String value)
    {
        setSearchQuery(value, null);
    }


    @Accessor(qualifier = "searchQuery", type = Accessor.Type.SETTER)
    public void setSearchQuery(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("searchQuery", loc, value);
    }
}
