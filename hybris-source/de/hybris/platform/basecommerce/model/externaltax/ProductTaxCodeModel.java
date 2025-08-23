package de.hybris.platform.basecommerce.model.externaltax;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ProductTaxCodeModel extends ItemModel
{
    public static final String _TYPECODE = "ProductTaxCode";
    public static final String PRODUCTCODE = "productCode";
    public static final String TAXAREA = "taxArea";
    public static final String TAXCODE = "taxCode";


    public ProductTaxCodeModel()
    {
    }


    public ProductTaxCodeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductTaxCodeModel(String _productCode, String _taxCode)
    {
        setProductCode(_productCode);
        setTaxCode(_taxCode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductTaxCodeModel(ItemModel _owner, String _productCode, String _taxArea, String _taxCode)
    {
        setOwner(_owner);
        setProductCode(_productCode);
        setTaxArea(_taxArea);
        setTaxCode(_taxCode);
    }


    @Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
    public String getProductCode()
    {
        return (String)getPersistenceContext().getPropertyValue("productCode");
    }


    @Accessor(qualifier = "taxArea", type = Accessor.Type.GETTER)
    public String getTaxArea()
    {
        return (String)getPersistenceContext().getPropertyValue("taxArea");
    }


    @Accessor(qualifier = "taxCode", type = Accessor.Type.GETTER)
    public String getTaxCode()
    {
        return (String)getPersistenceContext().getPropertyValue("taxCode");
    }


    @Accessor(qualifier = "productCode", type = Accessor.Type.SETTER)
    public void setProductCode(String value)
    {
        getPersistenceContext().setPropertyValue("productCode", value);
    }


    @Accessor(qualifier = "taxArea", type = Accessor.Type.SETTER)
    public void setTaxArea(String value)
    {
        getPersistenceContext().setPropertyValue("taxArea", value);
    }


    @Accessor(qualifier = "taxCode", type = Accessor.Type.SETTER)
    public void setTaxCode(String value)
    {
        getPersistenceContext().setPropertyValue("taxCode", value);
    }
}
