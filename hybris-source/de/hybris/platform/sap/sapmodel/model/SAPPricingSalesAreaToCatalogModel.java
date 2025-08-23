package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.sap.core.configuration.model.SAPGlobalConfigurationModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPPricingSalesAreaToCatalogModel extends ItemModel
{
    public static final String _TYPECODE = "SAPPricingSalesAreaToCatalog";
    public static final String _SAPGLOBALCONFIGURATIONPRICINGSALESAREA = "SAPGlobalConfigurationPricingSalesArea";
    public static final String SALESORGANIZATION = "salesOrganization";
    public static final String DISTRIBUTIONCHANNEL = "distributionChannel";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String NET = "net";
    public static final String SAPCOMMON_SAPGLOBALCONFIGURATION = "sapcommon_sapGlobalConfiguration";


    public SAPPricingSalesAreaToCatalogModel()
    {
    }


    public SAPPricingSalesAreaToCatalogModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPPricingSalesAreaToCatalogModel(CatalogVersionModel _catalogVersion)
    {
        setCatalogVersion(_catalogVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPPricingSalesAreaToCatalogModel(CatalogVersionModel _catalogVersion, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "distributionChannel", type = Accessor.Type.GETTER)
    public String getDistributionChannel()
    {
        return (String)getPersistenceContext().getPropertyValue("distributionChannel");
    }


    @Accessor(qualifier = "salesOrganization", type = Accessor.Type.GETTER)
    public String getSalesOrganization()
    {
        return (String)getPersistenceContext().getPropertyValue("salesOrganization");
    }


    @Accessor(qualifier = "sapcommon_sapGlobalConfiguration", type = Accessor.Type.GETTER)
    public SAPGlobalConfigurationModel getSapcommon_sapGlobalConfiguration()
    {
        return (SAPGlobalConfigurationModel)getPersistenceContext().getPropertyValue("sapcommon_sapGlobalConfiguration");
    }


    @Accessor(qualifier = "net", type = Accessor.Type.GETTER)
    public boolean isNet()
    {
        return toPrimitive((Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "net"));
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "distributionChannel", type = Accessor.Type.SETTER)
    public void setDistributionChannel(String value)
    {
        getPersistenceContext().setPropertyValue("distributionChannel", value);
    }


    @Accessor(qualifier = "salesOrganization", type = Accessor.Type.SETTER)
    public void setSalesOrganization(String value)
    {
        getPersistenceContext().setPropertyValue("salesOrganization", value);
    }


    @Accessor(qualifier = "sapcommon_sapGlobalConfiguration", type = Accessor.Type.SETTER)
    public void setSapcommon_sapGlobalConfiguration(SAPGlobalConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("sapcommon_sapGlobalConfiguration", value);
    }
}
