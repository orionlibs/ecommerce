package de.hybris.platform.ordersplitting.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.sap.sapmodel.model.SAPPlantLogSysOrgModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class WarehouseModel extends ItemModel
{
    public static final String _TYPECODE = "Warehouse";
    public static final String _VENDORWAREHOUSERELATION = "VendorWarehouseRelation";
    public static final String _CONSIGNMENTWAREHOUSERELATION = "ConsignmentWarehouseRelation";
    public static final String _STOCKLEVELWAREHOUSERELATION = "StockLevelWarehouseRelation";
    public static final String _POS2WAREHOUSEREL = "PoS2WarehouseRel";
    public static final String _BASESTORE2WAREHOUSEREL = "BaseStore2WarehouseRel";
    public static final String _SAPPRODUCTPLANTRELATION = "SapProductPlantRelation";
    public static final String _SAPPLANTLOGSYSORG2WAREHOUSERELATION = "SAPPlantLogSysOrg2WarehouseRelation";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DEFAULT = "default";
    public static final String VENDORPOS = "vendorPOS";
    public static final String VENDOR = "vendor";
    public static final String CONSIGNMENTS = "consignments";
    public static final String STOCKLEVELS = "stockLevels";
    public static final String POINTSOFSERVICE = "pointsOfService";
    public static final String BASESTORES = "baseStores";
    public static final String PRODUCT = "Product";
    public static final String SAPPLANTLOGSYSORG = "sapPlantLogSysOrg";


    public WarehouseModel()
    {
    }


    public WarehouseModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WarehouseModel(String _code, VendorModel _vendor)
    {
        setCode(_code);
        setVendor(_vendor);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WarehouseModel(String _code, ItemModel _owner, VendorModel _vendor)
    {
        setCode(_code);
        setOwner(_owner);
        setVendor(_vendor);
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
    public Collection<BaseStoreModel> getBaseStores()
    {
        return (Collection<BaseStoreModel>)getPersistenceContext().getPropertyValue("baseStores");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "consignments", type = Accessor.Type.GETTER)
    public Set<ConsignmentModel> getConsignments()
    {
        return (Set<ConsignmentModel>)getPersistenceContext().getPropertyValue("consignments");
    }


    @Accessor(qualifier = "default", type = Accessor.Type.GETTER)
    public Boolean getDefault()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("default");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "pointsOfService", type = Accessor.Type.GETTER)
    public Collection<PointOfServiceModel> getPointsOfService()
    {
        return (Collection<PointOfServiceModel>)getPersistenceContext().getPropertyValue("pointsOfService");
    }


    @Accessor(qualifier = "sapPlantLogSysOrg", type = Accessor.Type.GETTER)
    public SAPPlantLogSysOrgModel getSapPlantLogSysOrg()
    {
        return (SAPPlantLogSysOrgModel)getPersistenceContext().getPropertyValue("sapPlantLogSysOrg");
    }


    @Accessor(qualifier = "stockLevels", type = Accessor.Type.GETTER)
    public Set<StockLevelModel> getStockLevels()
    {
        return (Set<StockLevelModel>)getPersistenceContext().getPropertyValue("stockLevels");
    }


    @Accessor(qualifier = "vendor", type = Accessor.Type.GETTER)
    public VendorModel getVendor()
    {
        return (VendorModel)getPersistenceContext().getPropertyValue("vendor");
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
    public void setBaseStores(Collection<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("baseStores", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "consignments", type = Accessor.Type.SETTER)
    public void setConsignments(Set<ConsignmentModel> value)
    {
        getPersistenceContext().setPropertyValue("consignments", value);
    }


    @Accessor(qualifier = "default", type = Accessor.Type.SETTER)
    public void setDefault(Boolean value)
    {
        getPersistenceContext().setPropertyValue("default", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "pointsOfService", type = Accessor.Type.SETTER)
    public void setPointsOfService(Collection<PointOfServiceModel> value)
    {
        getPersistenceContext().setPropertyValue("pointsOfService", value);
    }


    @Accessor(qualifier = "sapPlantLogSysOrg", type = Accessor.Type.SETTER)
    public void setSapPlantLogSysOrg(SAPPlantLogSysOrgModel value)
    {
        getPersistenceContext().setPropertyValue("sapPlantLogSysOrg", value);
    }


    @Accessor(qualifier = "stockLevels", type = Accessor.Type.SETTER)
    public void setStockLevels(Set<StockLevelModel> value)
    {
        getPersistenceContext().setPropertyValue("stockLevels", value);
    }


    @Accessor(qualifier = "vendor", type = Accessor.Type.SETTER)
    public void setVendor(VendorModel value)
    {
        getPersistenceContext().setPropertyValue("vendor", value);
    }
}
