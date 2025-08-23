package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPExternalIdModel extends ItemModel
{
    public static final String _TYPECODE = "SAPExternalId";
    public static final String _PRODUCT2SAPEXTERNALIDS = "Product2SAPExternalIds";
    public static final String APPLICATION = "application";
    public static final String EXTERNALID = "externalId";
    public static final String TENANTID = "tenantId";
    public static final String VERSIONID = "versionId";
    public static final String ITEM = "item";


    public SAPExternalIdModel()
    {
    }


    public SAPExternalIdModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPExternalIdModel(String _application, String _externalId, String _tenantId)
    {
        setApplication(_application);
        setExternalId(_externalId);
        setTenantId(_tenantId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPExternalIdModel(String _application, String _externalId, ProductModel _item, ItemModel _owner, String _tenantId)
    {
        setApplication(_application);
        setExternalId(_externalId);
        setItem(_item);
        setOwner(_owner);
        setTenantId(_tenantId);
    }


    @Accessor(qualifier = "application", type = Accessor.Type.GETTER)
    public String getApplication()
    {
        return (String)getPersistenceContext().getPropertyValue("application");
    }


    @Accessor(qualifier = "externalId", type = Accessor.Type.GETTER)
    public String getExternalId()
    {
        return (String)getPersistenceContext().getPropertyValue("externalId");
    }


    @Accessor(qualifier = "item", type = Accessor.Type.GETTER)
    public ProductModel getItem()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("item");
    }


    @Accessor(qualifier = "tenantId", type = Accessor.Type.GETTER)
    public String getTenantId()
    {
        return (String)getPersistenceContext().getPropertyValue("tenantId");
    }


    @Accessor(qualifier = "versionId", type = Accessor.Type.GETTER)
    public String getVersionId()
    {
        return (String)getPersistenceContext().getPropertyValue("versionId");
    }


    @Accessor(qualifier = "application", type = Accessor.Type.SETTER)
    public void setApplication(String value)
    {
        getPersistenceContext().setPropertyValue("application", value);
    }


    @Accessor(qualifier = "externalId", type = Accessor.Type.SETTER)
    public void setExternalId(String value)
    {
        getPersistenceContext().setPropertyValue("externalId", value);
    }


    @Accessor(qualifier = "item", type = Accessor.Type.SETTER)
    public void setItem(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("item", value);
    }


    @Accessor(qualifier = "tenantId", type = Accessor.Type.SETTER)
    public void setTenantId(String value)
    {
        getPersistenceContext().setPropertyValue("tenantId", value);
    }


    @Accessor(qualifier = "versionId", type = Accessor.Type.SETTER)
    public void setVersionId(String value)
    {
        getPersistenceContext().setPropertyValue("versionId", value);
    }
}
