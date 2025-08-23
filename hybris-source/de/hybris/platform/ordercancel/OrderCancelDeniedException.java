package de.hybris.platform.ordercancel;

public class OrderCancelDeniedException extends OrderCancelException
{
    private final CancelDecision cancelDecision;


    public OrderCancelDeniedException(String orderCode, CancelDecision cancelDecision)
    {
        super(orderCode);
        this.cancelDecision = cancelDecision;
    }


    public CancelDecision getCancelDecision()
    {
        return this.cancelDecision;
    }
}
