package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class ProductReferencesComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "ProductReferencesComponent";
    public static final String TITLE = "title";
    public static final String PRODUCTREFERENCETYPES = "productReferenceTypes";
    public static final String MAXIMUMNUMBERPRODUCTS = "maximumNumberProducts";
    public static final String DISPLAYPRODUCTTITLES = "displayProductTitles";
    public static final String DISPLAYPRODUCTPRICES = "displayProductPrices";


    public ProductReferencesComponentModel()
    {
    }


    public ProductReferencesComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductReferencesComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductReferencesComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "maximumNumberProducts", type = Accessor.Type.GETTER)
    public Integer getMaximumNumberProducts()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maximumNumberProducts");
    }


    @Accessor(qualifier = "productReferenceTypes", type = Accessor.Type.GETTER)
    public List<ProductReferenceTypeEnum> getProductReferenceTypes()
    {
        return (List<ProductReferenceTypeEnum>)getPersistenceContext().getPropertyValue("productReferenceTypes");
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


    @Accessor(qualifier = "displayProductPrices", type = Accessor.Type.GETTER)
    public boolean isDisplayProductPrices()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("displayProductPrices"));
    }


    @Accessor(qualifier = "displayProductTitles", type = Accessor.Type.GETTER)
    public boolean isDisplayProductTitles()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("displayProductTitles"));
    }


    @Accessor(qualifier = "displayProductPrices", type = Accessor.Type.SETTER)
    public void setDisplayProductPrices(boolean value)
    {
        getPersistenceContext().setPropertyValue("displayProductPrices", toObject(value));
    }


    @Accessor(qualifier = "displayProductTitles", type = Accessor.Type.SETTER)
    public void setDisplayProductTitles(boolean value)
    {
        getPersistenceContext().setPropertyValue("displayProductTitles", toObject(value));
    }


    @Accessor(qualifier = "maximumNumberProducts", type = Accessor.Type.SETTER)
    public void setMaximumNumberProducts(Integer value)
    {
        getPersistenceContext().setPropertyValue("maximumNumberProducts", value);
    }


    @Accessor(qualifier = "productReferenceTypes", type = Accessor.Type.SETTER)
    public void setProductReferenceTypes(List<ProductReferenceTypeEnum> value)
    {
        getPersistenceContext().setPropertyValue("productReferenceTypes", value);
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
