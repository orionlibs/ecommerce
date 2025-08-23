package de.hybris.platform.stock.impl;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.core.PK;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class DefaultStockLevelDao extends AbstractItemDao implements StockLevelDao
{
    private static final Logger LOG = Logger.getLogger(DefaultStockLevelDao.class);
    private TypeService typeService;
    private TransactionTemplate transactionTemplate;
    private JdbcTemplate jdbcTemplate;
    private StockLevelColumns stockLevelColumns;


    public StockLevelModel findStockLevel(String productCode, WarehouseModel warehouse)
    {
        checkProductCode(productCode);
        checkWarehouse(warehouse);
        String query = "SELECT {pk} FROM {StockLevel} WHERE {productCode} = ?productCode AND {warehouse} = ?warehouse";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {StockLevel} WHERE {productCode} = ?productCode AND {warehouse} = ?warehouse");
        fQuery.addQueryParameter("productCode", productCode);
        fQuery.addQueryParameter("warehouse", warehouse);
        SearchResult<StockLevelModel> result = getFlexibleSearchService().search(fQuery);
        List<StockLevelModel> stockLevels = result.getResult();
        if(stockLevels.isEmpty())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("no stock level for product [" + productCode + "] in warehouse [" + warehouse.getName() + "] found.");
            }
            return null;
        }
        if(stockLevels.size() == 1)
        {
            return stockLevels.get(0);
        }
        LOG.error("more than one stock level with product code [" + productCode + "] and warehouse [" + warehouse.getName() + "] found, and the first one is returned.");
        return stockLevels.get(0);
    }


    public Collection<StockLevelModel> findAllStockLevels(String productCode)
    {
        checkProductCode(productCode);
        String query = "SELECT {pk} FROM {StockLevel} WHERE {productCode} = ?productCode";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {StockLevel} WHERE {productCode} = ?productCode");
        fQuery.addQueryParameter("productCode", productCode);
        SearchResult<StockLevelModel> result = getFlexibleSearchService().search(fQuery);
        return result.getResult();
    }


    public Collection<StockLevelModel> findStockLevels(String productCode, Collection<WarehouseModel> warehouses)
    {
        return findStockLevelsImpl(productCode, warehouses, null);
    }


    public Collection<StockLevelModel> findStockLevels(String productCode, Collection<WarehouseModel> warehouses, int preOrderQuantity)
    {
        return findStockLevelsImpl(productCode, warehouses, Integer.valueOf(preOrderQuantity));
    }


    private Collection<StockLevelModel> findStockLevelsImpl(String productCode, Collection<WarehouseModel> warehouses, Integer preOrderQuantity)
    {
        checkProductCode(productCode);
        List<WarehouseModel> filteredWarehouses = filterWarehouses(warehouses);
        if(filteredWarehouses.isEmpty())
        {
            return Collections.emptyList();
        }
        String warehousesParam = "WAREHOUSES_PARAM";
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} FROM {").append("StockLevel").append("} WHERE {")
                        .append("productCode").append("} = ?").append("productCode");
        if(preOrderQuantity != null)
        {
            query.append(" AND {").append("maxPreOrder").append("} >= ?").append("maxPreOrder");
        }
        query.append(" AND {").append("warehouse").append("} IN (?").append("WAREHOUSES_PARAM").append(")");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameter("productCode", productCode);
        if(preOrderQuantity != null)
        {
            fQuery.addQueryParameter("maxPreOrder", preOrderQuantity);
        }
        fQuery.addQueryParameter("WAREHOUSES_PARAM", filteredWarehouses);
        SearchResult<StockLevelModel> result = getFlexibleSearchService().search(fQuery);
        return result.getResult();
    }


    public Integer getAvailableQuantity(WarehouseModel warehouse, String productCode)
    {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = (new StringBuilder("select {s.")).append("available").append('}');
        query.append("  from {").append("StockLevel").append(" as s}");
        query.append("  where {s.").append("warehouse").append("} = ?").append("warehouse");
        query.append("  and {s.").append("productCode").append("} = ?").append("productCode");
        params.put("warehouse", warehouse);
        params.put("productCode", productCode);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), params);
        List<Class<Integer>> resultClassList = new ArrayList();
        resultClassList.add(Integer.class);
        searchQuery.setResultClassList(resultClassList);
        SearchResult<Integer> result = getFlexibleSearchService().search(searchQuery);
        if(result.getResult().isEmpty())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("NO StockLevel instance found for product '" + productCode + "' and warehouse '" + warehouse + "'!.");
            }
            return Integer.valueOf(0);
        }
        if(result.getResult().size() > 1)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("More than one StockLevel instance found for product '" + productCode + "' and warehouse '" + warehouse + "'!.");
            }
            return Integer.valueOf(0);
        }
        Object res = result.getResult().iterator().next();
        return (Integer)res;
    }


    public Integer reserve(StockLevelModel stockLevel, int amount)
    {
        return (Integer)this.transactionTemplate.execute((TransactionCallback)new Object(this, stockLevel, amount));
    }


    public Integer release(StockLevelModel stockLevel, int amount)
    {
        return (Integer)this.transactionTemplate.execute((TransactionCallback)new Object(this, amount, stockLevel));
    }


    private Integer peekStockLevelReserved(int rows, PK pk, String message)
    {
        Long pkLong = Long.valueOf(pk.getLongValue());
        Integer currentReserved = Integer.valueOf(-1);
        if(rows == 1)
        {
            String requestAmountQuery = assembleRequestStockLevelQuery();
            currentReserved = (Integer)this.jdbcTemplate.queryForObject(requestAmountQuery, Integer.class, new Object[] {pkLong});
        }
        else if(rows > 1)
        {
            throw new IllegalStateException(message + message + "] rows for stock level [" + rows + "]");
        }
        return (rows == 1) ? currentReserved : null;
    }


    public void updateActualAmount(StockLevelModel stockLevel, int actualAmount)
    {
        this.transactionTemplate.execute((TransactionCallback)new Object(this, actualAmount, stockLevel));
    }


    private int runJdbcQuery(String query, int amount, StockLevelModel stockLevel)
    {
        Integer theAmount = Integer.valueOf(amount);
        Long pk = Long.valueOf(stockLevel.getPk().getLongValue());
        int rows = this.jdbcTemplate.update(query, new Object[] {theAmount, pk});
        return rows;
    }


    private String assembleRequestStockLevelQuery()
    {
        prepareStockLevelColumns();
        StringBuilder query = (new StringBuilder("SELECT ")).append(this.stockLevelColumns.reservedCol);
        query.append(" FROM ").append(this.stockLevelColumns.tableName).append(" WHERE ").append(this.stockLevelColumns.pkCol).append("=?");
        return query.toString();
    }


    private String assembleUpdateStockLevelQuery()
    {
        prepareStockLevelColumns();
        StringBuilder query = (new StringBuilder("UPDATE ")).append(this.stockLevelColumns.tableName);
        query.append(" SET ").append(this.stockLevelColumns.reservedCol).append(" = 0, ").append(this.stockLevelColumns.availableCol)
                        .append(" =?");
        query.append(" WHERE ").append(this.stockLevelColumns.pkCol).append("=?");
        return query.toString();
    }


    private String assembleReleaseStockLevelQuery()
    {
        prepareStockLevelColumns();
        StringBuilder query = (new StringBuilder("UPDATE ")).append(this.stockLevelColumns.tableName);
        query.append(" SET ").append(this.stockLevelColumns.reservedCol).append(" = ").append(this.stockLevelColumns.reservedCol)
                        .append(" - ? ");
        query.append(" WHERE ").append(this.stockLevelColumns.pkCol).append("=?");
        return query.toString();
    }


    private String assembleReserveStockLevelQuery(InStockStatus inStockStatus)
    {
        prepareStockLevelColumns();
        StringBuilder query = (new StringBuilder("UPDATE ")).append(this.stockLevelColumns.tableName);
        query.append(" SET ").append(this.stockLevelColumns.reservedCol).append(" = ").append(this.stockLevelColumns.reservedCol)
                        .append(" + ? ");
        query.append(" WHERE ").append(this.stockLevelColumns.pkCol).append("=?");
        if(!InStockStatus.FORCEINSTOCK.equals(inStockStatus))
        {
            query.append(" AND ").append(this.stockLevelColumns.availableCol).append(" + ").append(this.stockLevelColumns.oversellingCol)
                            .append(" - ").append(this.stockLevelColumns.reservedCol).append(" >= ? ");
        }
        return query.toString();
    }


    private void prepareStockLevelColumns()
    {
        if(this.stockLevelColumns == null)
        {
            this.stockLevelColumns = new StockLevelColumns(this, this.typeService);
        }
    }


    private void checkProductCode(String productCode)
    {
        if(productCode == null)
        {
            throw new IllegalArgumentException("product code cannot be null.");
        }
    }


    private void checkWarehouse(WarehouseModel warehouse)
    {
        if(warehouse == null)
        {
            throw new IllegalArgumentException("warehouse cannot be null.");
        }
    }


    private List<WarehouseModel> filterWarehouses(Collection<WarehouseModel> warehouses)
    {
        if(warehouses == null)
        {
            throw new IllegalArgumentException("warehouses cannot be null.");
        }
        Set<WarehouseModel> result = new HashSet<>();
        for(WarehouseModel house : warehouses)
        {
            if(house != null)
            {
                result.add(house);
            }
        }
        return new ArrayList<>(result);
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setTransactionTemplate(TransactionTemplate transactionTemplate)
    {
        this.transactionTemplate = transactionTemplate;
    }


    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }
}
