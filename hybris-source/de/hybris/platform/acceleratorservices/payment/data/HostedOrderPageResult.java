package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;
import java.util.Map;

public class HostedOrderPageResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String requestId;
    private Integer reasonCode;
    private Map<Integer, String> errors;


    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }


    public String getRequestId()
    {
        return this.requestId;
    }


    public void setReasonCode(Integer reasonCode)
    {
        this.reasonCode = reasonCode;
    }


    public Integer getReasonCode()
    {
        return this.reasonCode;
    }


    public void setErrors(Map<Integer, String> errors)
    {
        this.errors = errors;
    }


    public Map<Integer, String> getErrors()
    {
        return this.errors;
    }
}
