package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class SAPPlantLogSysOrgModel extends ItemModel
{
    public static final String _TYPECODE = "SAPPlantLogSysOrg";
    public static final String _SAPCONFIG2PLANTLOGSYSORG = "SAPConfig2PlantLogSysOrg";
    public static final String PLANT = "plant";
    public static final String LOGSYS = "logSys";
    public static final String SALESORG = "salesOrg";
    public static final String SAPCONFIGURATION = "sapConfiguration";
    public static final String WAREHOUSES = "warehouses";


    public SAPPlantLogSysOrgModel()
    {
    }


    public SAPPlantLogSysOrgModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPPlantLogSysOrgModel(WarehouseModel _plant)
    {
        setPlant(_plant);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPPlantLogSysOrgModel(ItemModel _owner, WarehouseModel _plant)
    {
        setOwner(_owner);
        setPlant(_plant);
    }


    @Accessor(qualifier = "logSys", type = Accessor.Type.GETTER)
    public SAPLogicalSystemModel getLogSys()
    {
        return (SAPLogicalSystemModel)getPersistenceContext().getPropertyValue("logSys");
    }


    @Accessor(qualifier = "plant", type = Accessor.Type.GETTER)
    public WarehouseModel getPlant()
    {
        return (WarehouseModel)getPersistenceContext().getPropertyValue("plant");
    }


    @Accessor(qualifier = "salesOrg", type = Accessor.Type.GETTER)
    public SAPSalesOrganizationModel getSalesOrg()
    {
        return (SAPSalesOrganizationModel)getPersistenceContext().getPropertyValue("salesOrg");
    }


    @Accessor(qualifier = "sapConfiguration", type = Accessor.Type.GETTER)
    public SAPConfigurationModel getSapConfiguration()
    {
        return (SAPConfigurationModel)getPersistenceContext().getPropertyValue("sapConfiguration");
    }


    @Accessor(qualifier = "warehouses", type = Accessor.Type.GETTER)
    public Collection<WarehouseModel> getWarehouses()
    {
        return (Collection<WarehouseModel>)getPersistenceContext().getPropertyValue("warehouses");
    }


    @Accessor(qualifier = "logSys", type = Accessor.Type.SETTER)
    public void setLogSys(SAPLogicalSystemModel value)
    {
        getPersistenceContext().setPropertyValue("logSys", value);
    }


    @Accessor(qualifier = "plant", type = Accessor.Type.SETTER)
    public void setPlant(WarehouseModel value)
    {
        getPersistenceContext().setPropertyValue("plant", value);
    }


    @Accessor(qualifier = "salesOrg", type = Accessor.Type.SETTER)
    public void setSalesOrg(SAPSalesOrganizationModel value)
    {
        getPersistenceContext().setPropertyValue("salesOrg", value);
    }


    @Accessor(qualifier = "sapConfiguration", type = Accessor.Type.SETTER)
    public void setSapConfiguration(SAPConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("sapConfiguration", value);
    }


    @Accessor(qualifier = "warehouses", type = Accessor.Type.SETTER)
    public void setWarehouses(Collection<WarehouseModel> value)
    {
        getPersistenceContext().setPropertyValue("warehouses", value);
    }
}
