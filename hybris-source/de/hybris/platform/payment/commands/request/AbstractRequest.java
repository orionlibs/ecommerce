package de.hybris.platform.payment.commands.request;

public abstract class AbstractRequest
{
    private final String merchantTransactionCode;


    protected AbstractRequest(String merchantTransactionCode)
    {
        this.merchantTransactionCode = merchantTransactionCode;
    }


    public String getMerchantTransactionCode()
    {
        return this.merchantTransactionCode;
    }
}
