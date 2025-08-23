package de.hybris.platform.variants.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class VariantProductModel extends ProductModel
{
    public static final String _TYPECODE = "VariantProduct";
    public static final String _PRODUCT2VARIANTRELATION = "Product2VariantRelation";
    public static final String BASEPRODUCT = "baseProduct";


    public VariantProductModel()
    {
    }


    public VariantProductModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VariantProductModel(ProductModel _baseProduct, CatalogVersionModel _catalogVersion, String _code)
    {
        setBaseProduct(_baseProduct);
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VariantProductModel(ProductModel _baseProduct, CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setBaseProduct(_baseProduct);
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "baseProduct", type = Accessor.Type.GETTER)
    public ProductModel getBaseProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("baseProduct");
    }


    @Accessor(qualifier = "baseProduct", type = Accessor.Type.SETTER)
    public void setBaseProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("baseProduct", value);
    }
}
