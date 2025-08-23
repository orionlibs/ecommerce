package de.hybris.platform.sap.sapmodel.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.variants.model.VariantProductModel;

public class ERPVariantProductModel extends VariantProductModel
{
    public static final String _TYPECODE = "ERPVariantProduct";


    public ERPVariantProductModel()
    {
    }


    public ERPVariantProductModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ERPVariantProductModel(ProductModel _baseProduct, CatalogVersionModel _catalogVersion, String _code)
    {
        setBaseProduct(_baseProduct);
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ERPVariantProductModel(ProductModel _baseProduct, CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setBaseProduct(_baseProduct);
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }
}
