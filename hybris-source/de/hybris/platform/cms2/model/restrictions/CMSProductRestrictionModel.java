package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

public class CMSProductRestrictionModel extends AbstractRestrictionModel
{
    public static final String _TYPECODE = "CMSProductRestriction";
    public static final String PRODUCTCODES = "productCodes";
    public static final String PRODUCTS = "products";


    public CMSProductRestrictionModel()
    {
    }


    public CMSProductRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSProductRestrictionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSProductRestrictionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "productCodes", type = Accessor.Type.GETTER)
    public List<String> getProductCodes()
    {
        return (List<String>)getPersistenceContext().getPropertyValue("productCodes");
    }


    @Accessor(qualifier = "products", type = Accessor.Type.GETTER)
    public Collection<ProductModel> getProducts()
    {
        return (Collection<ProductModel>)getPersistenceContext().getPropertyValue("products");
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(Collection<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("products", value);
    }
}
