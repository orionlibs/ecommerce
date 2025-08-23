package de.hybris.platform.ordersplitting.daos.impl;

import de.hybris.platform.ordersplitting.daos.WarehouseDao;
import de.hybris.platform.ordersplitting.jalo.Warehouse;
import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultWarehouseDao extends DefaultGenericDao<WarehouseModel> implements WarehouseDao
{
    public DefaultWarehouseDao()
    {
        super("Warehouse");
    }


    public List<WarehouseModel> getWarehouseForCode(String code)
    {
        return find(Collections.singletonMap("code", code));
    }


    public List<WarehouseModel> getDefWarehouse()
    {
        return find(Collections.singletonMap("default", Boolean.TRUE));
    }


    public List<WarehouseModel> getWarehouses(String productCode)
    {
        StringBuilder query = (new StringBuilder("select distinct {w:")).append(Warehouse.PK).append('}');
        query.append("   from {").append("StockLevel").append(" as s}, ");
        query.append("        {").append("Warehouse").append(" as w} ");
        query.append("   where {s:").append("warehouse").append("} = {w:").append(Warehouse.PK).append('}');
        query.append("     and {s:").append("productCode").append("} = (?productCode)");
        SearchResult<WarehouseModel> queryResult = getFlexibleSearchService().search(query.toString(),
                        Collections.singletonMap("productCode", productCode));
        return queryResult.getResult();
    }


    public List<WarehouseModel> getWarehousesWithProductsInStock(String productCode, long quantity, VendorModel vendor)
    {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = (new StringBuilder("select distinct {w:")).append(Warehouse.PK).append('}');
        query.append("   from {").append("StockLevel").append(" as s}, ");
        query.append("        {").append("Warehouse").append(" as w} ");
        query.append("   where {s:").append("warehouse").append("} = {w:").append(Warehouse.PK).append('}');
        query.append("     and {s:").append("available").append("} >= ?quantity");
        query.append("     and {s:").append("productCode").append('}').append(" = ?code");
        if(vendor != null && vendor.getPk() != null)
        {
            query.append("     and {w:").append("vendor").append("} = ?vendor");
            params.put("vendor", vendor.getPk());
        }
        params.put("quantity", Long.valueOf(quantity));
        params.put("code", productCode);
        SearchResult<WarehouseModel> result = getFlexibleSearchService().search(query.toString(), params);
        return result.getResult();
    }
}
