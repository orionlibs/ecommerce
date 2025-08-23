package de.hybris.platform.ordercancel;

public interface OrderCancelCallbackService
{
    void onOrderCancelResponse(OrderCancelResponse paramOrderCancelResponse) throws OrderCancelException;
}
