package de.hybris.platform.variants.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class GenericVariantProductModel extends VariantProductModel
{
    public static final String _TYPECODE = "GenericVariantProduct";


    public GenericVariantProductModel()
    {
    }


    public GenericVariantProductModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public GenericVariantProductModel(ProductModel _baseProduct, CatalogVersionModel _catalogVersion, String _code)
    {
        setBaseProduct(_baseProduct);
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public GenericVariantProductModel(ProductModel _baseProduct, CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setBaseProduct(_baseProduct);
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }
}
