package de.hybris.platform.ordercancel.impl;

import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelWarehouseAdapter;
import org.apache.log4j.Logger;

public class DefaultWarehouseAdapterMock implements OrderCancelWarehouseAdapter
{
    private static final Logger LOG = Logger.getLogger(DefaultWarehouseAdapterMock.class.getName());


    public void requestOrderCancel(OrderCancelRequest orderCancelRequest)
    {
        LOG
                        .info("MOCK: Order cancel request is being sent to the Warehouse. Cancel will proceed after a response from the Warehouse is received");
    }
}
