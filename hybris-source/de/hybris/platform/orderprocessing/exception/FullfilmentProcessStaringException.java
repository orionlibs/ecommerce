package de.hybris.platform.orderprocessing.exception;

public class FullfilmentProcessStaringException extends RuntimeException
{
    private final String orderCode;
    private final String processDefinition;


    public FullfilmentProcessStaringException(String orderCode, String processDefinition, String message, Throwable nested)
    {
        super("Could not start process [" + processDefinition + "] for order [" + orderCode + "] due to : " + message, nested);
        this.orderCode = orderCode;
        this.processDefinition = processDefinition;
    }


    public FullfilmentProcessStaringException(String orderCode, String processDefinition, String message)
    {
        super("Could not start process [" + processDefinition + "] for order [" + orderCode + "] due to : " + message);
        this.orderCode = orderCode;
        this.processDefinition = processDefinition;
    }


    public String getOrderCode()
    {
        return this.orderCode;
    }


    public String getProcessDefinition()
    {
        return this.processDefinition;
    }
}
