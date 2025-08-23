package de.hybris.platform.sap.core.configuration.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.sap.sapmodel.model.SAPLogicalSystemModel;
import de.hybris.platform.sap.sapmodel.model.SAPPricingSalesAreaToCatalogModel;
import de.hybris.platform.sap.sapmodel.model.SAPProductSalesAreaToCatalogMappingModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class SAPGlobalConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "SAPGlobalConfiguration";
    public static final String CORE_NAME = "core_name";
    public static final String REPLICATEREGISTEREDUSER = "replicateregistereduser";
    public static final String SAPCOMMON_ERPLOGICALSYSTEM = "sapcommon_erpLogicalSystem";
    public static final String SAPCOMMON_ERPHTTPDESTINATION = "sapcommon_erpHttpDestination";
    public static final String SAPPRODUCTSALESAREATOCATALOGMAPPING = "sapProductSalesAreaToCatalogMapping";
    public static final String SAPCOMMON_SAPPRICINGSALESAREA = "sapcommon_sapPricingSalesArea";
    public static final String SAPLOGICALSYSTEMGLOBALCONFIG = "sapLogicalSystemGlobalConfig";


    public SAPGlobalConfigurationModel()
    {
    }


    public SAPGlobalConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPGlobalConfigurationModel(boolean _replicateregistereduser)
    {
        setReplicateregistereduser(_replicateregistereduser);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPGlobalConfigurationModel(ItemModel _owner, boolean _replicateregistereduser)
    {
        setOwner(_owner);
        setReplicateregistereduser(_replicateregistereduser);
    }


    @Accessor(qualifier = "core_name", type = Accessor.Type.GETTER)
    public String getCore_name()
    {
        return (String)getPersistenceContext().getPropertyValue("core_name");
    }


    @Accessor(qualifier = "sapcommon_erpHttpDestination", type = Accessor.Type.GETTER)
    public SAPHTTPDestinationModel getSapcommon_erpHttpDestination()
    {
        return (SAPHTTPDestinationModel)getPersistenceContext().getPropertyValue("sapcommon_erpHttpDestination");
    }


    @Accessor(qualifier = "sapcommon_erpLogicalSystem", type = Accessor.Type.GETTER)
    public String getSapcommon_erpLogicalSystem()
    {
        return (String)getPersistenceContext().getPropertyValue("sapcommon_erpLogicalSystem");
    }


    @Accessor(qualifier = "sapcommon_sapPricingSalesArea", type = Accessor.Type.GETTER)
    public Set<SAPPricingSalesAreaToCatalogModel> getSapcommon_sapPricingSalesArea()
    {
        return (Set<SAPPricingSalesAreaToCatalogModel>)getPersistenceContext().getPropertyValue("sapcommon_sapPricingSalesArea");
    }


    @Accessor(qualifier = "sapLogicalSystemGlobalConfig", type = Accessor.Type.GETTER)
    public Set<SAPLogicalSystemModel> getSapLogicalSystemGlobalConfig()
    {
        return (Set<SAPLogicalSystemModel>)getPersistenceContext().getPropertyValue("sapLogicalSystemGlobalConfig");
    }


    @Accessor(qualifier = "sapProductSalesAreaToCatalogMapping", type = Accessor.Type.GETTER)
    public Set<SAPProductSalesAreaToCatalogMappingModel> getSapProductSalesAreaToCatalogMapping()
    {
        return (Set<SAPProductSalesAreaToCatalogMappingModel>)getPersistenceContext().getPropertyValue("sapProductSalesAreaToCatalogMapping");
    }


    @Accessor(qualifier = "replicateregistereduser", type = Accessor.Type.GETTER)
    public boolean isReplicateregistereduser()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("replicateregistereduser"));
    }


    @Accessor(qualifier = "core_name", type = Accessor.Type.SETTER)
    public void setCore_name(String value)
    {
        getPersistenceContext().setPropertyValue("core_name", value);
    }


    @Accessor(qualifier = "replicateregistereduser", type = Accessor.Type.SETTER)
    public void setReplicateregistereduser(boolean value)
    {
        getPersistenceContext().setPropertyValue("replicateregistereduser", toObject(value));
    }


    @Accessor(qualifier = "sapcommon_erpHttpDestination", type = Accessor.Type.SETTER)
    public void setSapcommon_erpHttpDestination(SAPHTTPDestinationModel value)
    {
        getPersistenceContext().setPropertyValue("sapcommon_erpHttpDestination", value);
    }


    @Accessor(qualifier = "sapcommon_erpLogicalSystem", type = Accessor.Type.SETTER)
    public void setSapcommon_erpLogicalSystem(String value)
    {
        getPersistenceContext().setPropertyValue("sapcommon_erpLogicalSystem", value);
    }


    @Accessor(qualifier = "sapcommon_sapPricingSalesArea", type = Accessor.Type.SETTER)
    public void setSapcommon_sapPricingSalesArea(Set<SAPPricingSalesAreaToCatalogModel> value)
    {
        getPersistenceContext().setPropertyValue("sapcommon_sapPricingSalesArea", value);
    }


    @Accessor(qualifier = "sapLogicalSystemGlobalConfig", type = Accessor.Type.SETTER)
    public void setSapLogicalSystemGlobalConfig(Set<SAPLogicalSystemModel> value)
    {
        getPersistenceContext().setPropertyValue("sapLogicalSystemGlobalConfig", value);
    }


    @Accessor(qualifier = "sapProductSalesAreaToCatalogMapping", type = Accessor.Type.SETTER)
    public void setSapProductSalesAreaToCatalogMapping(Set<SAPProductSalesAreaToCatalogMappingModel> value)
    {
        getPersistenceContext().setPropertyValue("sapProductSalesAreaToCatalogMapping", value);
    }
}
