package de.hybris.platform.warehousing.util;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.warehousing.util.models.Addresses;
import de.hybris.platform.warehousing.util.models.Orders;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Users;
import de.hybris.platform.warehousing.util.models.Warehouses;
import javax.annotation.Resource;

public class VerifyOrderAndConsignment extends BaseWarehousingIntegrationTest
{
    @Resource
    protected SourcingService sourcingService;
    @Resource
    protected Orders orders;
    @Resource
    protected Warehouses warehouses;
    @Resource
    protected Addresses addresses;
    @Resource
    protected StockLevels stockLevels;
    @Resource
    protected PointsOfService pointsOfService;
    @Resource
    protected Products products;
    @Resource
    protected Users users;
    private static final String CODE_CAMERA = "camera";


    public Boolean verifyConsignment_Camera(OrderModel order, String location, Long quantityDeclined, Long quantity, Long quantityPending)
    {
        return verifyConsignment(order, "camera", location, quantityDeclined, quantity, quantityPending);
    }


    public Boolean verifyConsignment(OrderModel order, String productCode, String location, Long quantityDeclined, Long quantity, Long quantityPending)
    {
        return Boolean.valueOf(order.getConsignments().stream()
                        .anyMatch(result -> (getWarehouseCompareResult(result, location).booleanValue() && getDeclineQuantityResult(result, quantityDeclined.longValue(), productCode).booleanValue() && getQuantityResult(result, quantity.longValue(), productCode).booleanValue()
                                        && getPendingQuantityResult(result, quantityPending.longValue(), productCode).booleanValue())));
    }


    public Boolean verifyConsignment_Camera_MemoryCard(OrderModel order, String location, Long quantityDeclined_Camera, Long quantity_Camera, Long quantityPending_Camera, Long quantityDeclined_MemoryCard, Long quantity_MemoryCard, Long quantityPending_MemoryCard)
    {
        return Boolean.valueOf(order.getConsignments().stream().anyMatch(result ->
                        (getWarehouseCompareResult(result, location).booleanValue() && getDeclineQuantityResult(result, quantityDeclined_Camera.longValue(), "camera").booleanValue() && getQuantityResult(result, quantity_Camera.longValue(), "camera").booleanValue() && getDeclineQuantityResult(result,
                                        quantityDeclined_MemoryCard.longValue(), "memorycard").booleanValue() && getQuantityResult(result, quantity_MemoryCard.longValue(), "memorycard").booleanValue() && getPendingQuantityResult(result, quantityPending_Camera.longValue(), "camera").booleanValue()
                                        && getPendingQuantityResult(result, quantityPending_MemoryCard.longValue(), "memorycard").booleanValue())));
    }


    private Boolean getQuantityResult(ConsignmentModel consignmentModel, long quantity, String productCode)
    {
        return Boolean.valueOf(consignmentModel.getConsignmentEntries().stream().anyMatch(e ->
                        (e.getQuantity().equals(Long.valueOf(quantity)) && e.getOrderEntry().getProduct().getCode().equals(productCode))));
    }


    private Boolean getDeclineQuantityResult(ConsignmentModel consignmentModel, long quantityDeclined, String productCode)
    {
        return Boolean.valueOf(consignmentModel.getConsignmentEntries().stream().anyMatch(e ->
                        (e.getQuantityDeclined().equals(Long.valueOf(quantityDeclined)) && e.getOrderEntry().getProduct().getCode().equals(productCode))));
    }


    private Boolean getPendingQuantityResult(ConsignmentModel consignmentModel, long quantityPending, String productCode)
    {
        return Boolean.valueOf(consignmentModel.getConsignmentEntries().stream().anyMatch(e ->
                        (e.getQuantityPending().equals(Long.valueOf(quantityPending)) && e.getOrderEntry().getProduct().getCode().equals(productCode))));
    }


    private Boolean getWarehouseCompareResult(ConsignmentModel consignmentModel, String location)
    {
        return Boolean.valueOf(consignmentModel.getWarehouse().getCode().equals(location));
    }
}
