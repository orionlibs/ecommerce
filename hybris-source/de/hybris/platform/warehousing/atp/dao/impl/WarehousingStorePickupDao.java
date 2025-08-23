package de.hybris.platform.warehousing.atp.dao.impl;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.commerceservices.delivery.dao.StorePickupDao;
import de.hybris.platform.commerceservices.delivery.dao.impl.DefaultStorePickupDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collections;

public class WarehousingStorePickupDao extends DefaultStorePickupDao implements StorePickupDao
{
    protected static final String PICKUP_CODE = "pickup";
    protected static final String PICKUP_WAREHOUSING_CHECK_QUERY = "SELECT 1 FROM {StockLevel as sl JOIN PointOfService as pos ON {pos.baseStore} = ?baseStore JOIN PoS2WarehouseRel as p2w ON {p2w.source} = {pos.pk} AND {p2w.target} = {sl.warehouse} JOIN Warehouse2DeliveryModeRelation as w2d ON {w2d.source} = {sl.warehouse} JOIN DeliveryMode as del ON {del.pk} = {w2d.target}}WHERE {sl.productCode} = ?productCode AND {del.code} = ?pickup ";


    public Boolean checkProductForPickup(String productCode, BaseStoreModel baseStoreModel)
    {
        ServicesUtil.validateParameterNotNull(productCode, "productCode cannot be null");
        ServicesUtil.validateParameterNotNull(baseStoreModel, "baseStoreModel cannot be null");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
                        "SELECT 1 FROM {StockLevel as sl JOIN PointOfService as pos ON {pos.baseStore} = ?baseStore JOIN PoS2WarehouseRel as p2w ON {p2w.source} = {pos.pk} AND {p2w.target} = {sl.warehouse} JOIN Warehouse2DeliveryModeRelation as w2d ON {w2d.source} = {sl.warehouse} JOIN DeliveryMode as del ON {del.pk} = {w2d.target}}WHERE {sl.productCode} = ?productCode AND {del.code} = ?pickup  AND ((({sl.available} + {sl.overselling} - {sl.reserved}) > 0) OR {sl.inStockStatus} = ?forceInStock) AND {sl.inStockStatus} <> ?forceOutOfStock");
        fQuery.addQueryParameter("productCode", productCode);
        fQuery.addQueryParameter("baseStore", baseStoreModel);
        fQuery.addQueryParameter("pickup", "pickup");
        fQuery.addQueryParameter("forceInStock", InStockStatus.FORCEINSTOCK);
        fQuery.addQueryParameter("forceOutOfStock", InStockStatus.FORCEOUTOFSTOCK);
        fQuery.setNeedTotal(false);
        fQuery.setCount(1);
        fQuery.setResultClassList(Collections.singletonList(Integer.class));
        int resultSize = getFlexibleSearchService().search(fQuery).getResult().size();
        return Boolean.valueOf((resultSize > 0));
    }
}
