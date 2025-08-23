package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class ProductPromotionModel extends AbstractPromotionModel
{
    public static final String _TYPECODE = "ProductPromotion";
    public static final String _PRODUCTPROMOTIONRELATION = "ProductPromotionRelation";
    public static final String _CATEGORYPROMOTIONRELATION = "CategoryPromotionRelation";
    public static final String PRODUCTBANNER = "productBanner";
    public static final String PRODUCTS = "products";
    public static final String CATEGORIES = "categories";


    public ProductPromotionModel()
    {
    }


    public ProductPromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPromotionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
    public Collection<CategoryModel> getCategories()
    {
        return (Collection<CategoryModel>)getPersistenceContext().getPropertyValue("categories");
    }


    @Accessor(qualifier = "productBanner", type = Accessor.Type.GETTER)
    public MediaModel getProductBanner()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("productBanner");
    }


    @Accessor(qualifier = "products", type = Accessor.Type.GETTER)
    public Collection<ProductModel> getProducts()
    {
        return (Collection<ProductModel>)getPersistenceContext().getPropertyValue("products");
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
    public void setCategories(Collection<CategoryModel> value)
    {
        getPersistenceContext().setPropertyValue("categories", value);
    }


    @Accessor(qualifier = "productBanner", type = Accessor.Type.SETTER)
    public void setProductBanner(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("productBanner", value);
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(Collection<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("products", value);
    }
}
