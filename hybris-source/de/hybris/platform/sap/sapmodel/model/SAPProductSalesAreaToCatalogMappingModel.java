package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.sap.core.configuration.model.SAPGlobalConfigurationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPProductSalesAreaToCatalogMappingModel extends ItemModel
{
    public static final String _TYPECODE = "SAPProductSalesAreaToCatalogMapping";
    public static final String _SAPGLOBALCONFIGSAPPRODUCTSALESAREATOCATALOGMAPPINGRELATION = "SAPGlobalConfigSAPProductSalesAreaToCatalogMappingRelation";
    public static final String SALESORGANIZATION = "salesOrganization";
    public static final String DISTRIBUTIONCHANNEL = "distributionChannel";
    public static final String SOURCETENANT = "sourceTenant";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String TAXCLASSCOUNTRY = "taxClassCountry";
    public static final String SAPGLOBALCONFIGURATION = "sapGlobalConfiguration";


    public SAPProductSalesAreaToCatalogMappingModel()
    {
    }


    public SAPProductSalesAreaToCatalogMappingModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPProductSalesAreaToCatalogMappingModel(CatalogVersionModel _catalogVersion, SAPGlobalConfigurationModel _sapGlobalConfiguration)
    {
        setCatalogVersion(_catalogVersion);
        setSapGlobalConfiguration(_sapGlobalConfiguration);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPProductSalesAreaToCatalogMappingModel(CatalogVersionModel _catalogVersion, ItemModel _owner, SAPGlobalConfigurationModel _sapGlobalConfiguration)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setSapGlobalConfiguration(_sapGlobalConfiguration);
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


    @Accessor(qualifier = "sapGlobalConfiguration", type = Accessor.Type.GETTER)
    public SAPGlobalConfigurationModel getSapGlobalConfiguration()
    {
        return (SAPGlobalConfigurationModel)getPersistenceContext().getPropertyValue("sapGlobalConfiguration");
    }


    @Accessor(qualifier = "sourceTenant", type = Accessor.Type.GETTER)
    public String getSourceTenant()
    {
        return (String)getPersistenceContext().getPropertyValue("sourceTenant");
    }


    @Accessor(qualifier = "taxClassCountry", type = Accessor.Type.GETTER)
    public CountryModel getTaxClassCountry()
    {
        return (CountryModel)getPersistenceContext().getPropertyValue("taxClassCountry");
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


    @Accessor(qualifier = "sapGlobalConfiguration", type = Accessor.Type.SETTER)
    public void setSapGlobalConfiguration(SAPGlobalConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("sapGlobalConfiguration", value);
    }


    @Accessor(qualifier = "sourceTenant", type = Accessor.Type.SETTER)
    public void setSourceTenant(String value)
    {
        getPersistenceContext().setPropertyValue("sourceTenant", value);
    }


    @Accessor(qualifier = "taxClassCountry", type = Accessor.Type.SETTER)
    public void setTaxClassCountry(CountryModel value)
    {
        getPersistenceContext().setPropertyValue("taxClassCountry", value);
    }
}
