package de.hybris.platform.warehousing.cancellation.executors;

import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordercancel.impl.executors.WarehouseResponseExecutor;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.warehousing.cancellation.ConsignmentCancellationService;
import de.hybris.platform.warehousing.cancellation.OmsOrderCancelService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class OmsWarehouseResponseExecutor extends WarehouseResponseExecutor
{
    private static Logger LOGGER = LoggerFactory.getLogger(OmsWarehouseResponseExecutor.class);
    private OmsOrderCancelService omsOrderCancelService;
    private ConsignmentCancellationService consignmentCancellationService;


    public void processCancelResponse(OrderCancelResponse orderCancelResponse, OrderCancelRecordEntryModel cancelRequestRecordEntry) throws OrderCancelException
    {
        LOGGER.info("Process cancel response from order {}", orderCancelResponse.getOrder().getCode());
        List<OrderCancelEntry> allocatedEntries = getOmsOrderCancelService().processOrderCancel(cancelRequestRecordEntry);
        if(!CollectionUtils.isEmpty(allocatedEntries))
        {
            OrderCancelResponse updatedOrderCancelResponse = new OrderCancelResponse(orderCancelResponse.getOrder(), allocatedEntries);
            super.processCancelResponse(updatedOrderCancelResponse, cancelRequestRecordEntry);
            getConsignmentCancellationService().processConsignmentCancellation(updatedOrderCancelResponse);
        }
        else
        {
            super.processCancelResponse(createCancelResponseWithZeroQtyResponse(orderCancelResponse), cancelRequestRecordEntry);
        }
    }


    protected OrderCancelResponse createCancelResponseWithZeroQtyResponse(OrderCancelResponse orderCancelResponse)
    {
        List<OrderCancelEntry> orderCancelEntries = new ArrayList<>();
        orderCancelResponse.getEntriesToCancel().forEach(cancelEntry -> {
            OrderCancelEntry orderCancelEntry = new OrderCancelEntry(cancelEntry.getOrderEntry(), Long.valueOf(0L).longValue(), cancelEntry.getNotes(), cancelEntry.getCancelReason());
            orderCancelEntries.add(orderCancelEntry);
        });
        return new OrderCancelResponse(orderCancelResponse.getOrder(), orderCancelEntries, orderCancelResponse.getResponseStatus(), orderCancelResponse.getNotes());
    }


    protected OmsOrderCancelService getOmsOrderCancelService()
    {
        return this.omsOrderCancelService;
    }


    @Required
    public void setOmsOrderCancelService(OmsOrderCancelService omsOrderCancelService)
    {
        this.omsOrderCancelService = omsOrderCancelService;
    }


    protected ConsignmentCancellationService getConsignmentCancellationService()
    {
        return this.consignmentCancellationService;
    }


    @Required
    public void setConsignmentCancellationService(ConsignmentCancellationService consignmentCancellationService)
    {
        this.consignmentCancellationService = consignmentCancellationService;
    }
}
