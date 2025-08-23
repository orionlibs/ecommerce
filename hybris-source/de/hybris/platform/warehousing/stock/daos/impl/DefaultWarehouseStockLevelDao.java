package de.hybris.platform.warehousing.stock.daos.impl;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.stock.daos.WarehouseStockLevelDao;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWarehouseStockLevelDao extends AbstractItemDao implements WarehouseStockLevelDao
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWarehouseStockLevelDao.class);


    public List<StockLevelModel> getStockLevels(String productCode, String warehouseCode, String binCode, Date releaseDate)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("productCode", productCode);
        ServicesUtil.validateParameterNotNullStandardMessage("warehouseCode", warehouseCode);
        Map<String, Object> params = new HashMap<>();
        params.put("productCode", productCode);
        params.put("code", warehouseCode);
        StringBuilder query = (new StringBuilder("SELECT {s:")).append("pk").append("} FROM { ").append("StockLevel").append(" AS s ").append("JOIN ").append("Warehouse").append(" AS w ON ").append("{s:").append("warehouse").append("}={w:").append("pk").append('}').append("} WHERE {s:")
                        .append("productCode").append("} = ?").append("productCode").append(" AND {w:").append("code").append("} = ?").append("code");
        if(binCode != null)
        {
            params.put("bin", binCode);
            query.append(" AND {s:").append("bin").append("} = ?").append("bin");
        }
        if(releaseDate != null)
        {
            params.put("releaseDate", releaseDate);
            query.append(" AND ({s:").append("releaseDate").append("} = ?").append("releaseDate")
                            .append(" OR {s:").append("releaseDate").append("} IS NULL)");
        }
        else
        {
            params.put("releaseDate", new Date());
            query.append(" AND ({s:").append("releaseDate").append("} <= ?").append("releaseDate")
                            .append(" OR {s:").append("releaseDate").append("} IS NULL)");
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Searching for stockLevel for product code [{}] and warehouse code [{}]", productCode, warehouseCode);
        }
        SearchResult<StockLevelModel> results = getFlexibleSearchService().search(query.toString(), params);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Results: {}", (results == null) ? "null" : String.valueOf(results.getCount()));
        }
        return (results == null || CollectionUtils.isEmpty(results.getResult())) ? Collections.<StockLevelModel>emptyList() : results.getResult();
    }


    public List<StockLevelModel> getFutureStockLevels(String productCode, String warehouseCode, String binCode)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("productCode", productCode);
        ServicesUtil.validateParameterNotNullStandardMessage("warehouseCode", warehouseCode);
        Map<String, Object> params = new HashMap<>();
        params.put("productCode", productCode);
        params.put("code", warehouseCode);
        params.put("releaseDate", new Date());
        StringBuilder query = (new StringBuilder("SELECT {s:")).append("pk").append("} FROM { ").append("StockLevel").append(" AS s ").append("JOIN ").append("Warehouse").append(" AS w ON ").append("{s:").append("warehouse").append("}={w:").append("pk").append('}').append("} WHERE {s:")
                        .append("productCode").append("} = ?").append("productCode").append(" AND {w:").append("code").append("} = ?").append("code").append(" AND {s:").append("releaseDate").append("} > CAST(?").append("releaseDate").append(" AS DATE)");
        if(binCode != null)
        {
            params.put("bin", binCode);
            query.append(" AND {s:").append("bin").append("} = ?").append("bin");
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Searching for future stockLevel for product code [{}] and warehouse code [{}]", productCode, warehouseCode);
        }
        SearchResult<StockLevelModel> results = getFlexibleSearchService().search(query.toString(), params);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Results: {}", (results == null) ? "null" : String.valueOf(results.getCount()));
        }
        return (results == null || CollectionUtils.isEmpty(results.getResult())) ? Collections.<StockLevelModel>emptyList() : results.getResult();
    }
}
