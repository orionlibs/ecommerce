package de.hybris.platform.ordersplitting.daos;

import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import java.util.List;

public interface WarehouseDao extends GenericDao<WarehouseModel>
{
    List<WarehouseModel> getWarehouseForCode(String paramString);


    List<WarehouseModel> getDefWarehouse();


    List<WarehouseModel> getWarehouses(String paramString);


    List<WarehouseModel> getWarehousesWithProductsInStock(String paramString, long paramLong, VendorModel paramVendorModel);
}
