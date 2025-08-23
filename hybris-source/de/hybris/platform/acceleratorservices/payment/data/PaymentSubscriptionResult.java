package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;
import java.util.Map;

public class PaymentSubscriptionResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    private boolean success;
    private String decision;
    private String resultCode;
    private Map<String, PaymentErrorField> errors;


    public void setSuccess(boolean success)
    {
        this.success = success;
    }


    public boolean isSuccess()
    {
        return this.success;
    }


    public void setDecision(String decision)
    {
        this.decision = decision;
    }


    public String getDecision()
    {
        return this.decision;
    }


    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }


    public String getResultCode()
    {
        return this.resultCode;
    }


    public void setErrors(Map<String, PaymentErrorField> errors)
    {
        this.errors = errors;
    }


    public Map<String, PaymentErrorField> getErrors()
    {
        return this.errors;
    }
}
