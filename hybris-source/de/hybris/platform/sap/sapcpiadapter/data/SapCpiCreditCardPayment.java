package de.hybris.platform.sap.sapcpiadapter.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sapCpiCreditCardPayment")
public class SapCpiCreditCardPayment implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String requestId;
    private String ccOwner;
    private String validToMonth;
    private String validToYear;
    private String subscriptionId;
    private String paymentProvider;


    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }


    public String getOrderId()
    {
        return this.orderId;
    }


    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }


    public String getRequestId()
    {
        return this.requestId;
    }


    public void setCcOwner(String ccOwner)
    {
        this.ccOwner = ccOwner;
    }


    public String getCcOwner()
    {
        return this.ccOwner;
    }


    public void setValidToMonth(String validToMonth)
    {
        this.validToMonth = validToMonth;
    }


    public String getValidToMonth()
    {
        return this.validToMonth;
    }


    public void setValidToYear(String validToYear)
    {
        this.validToYear = validToYear;
    }


    public String getValidToYear()
    {
        return this.validToYear;
    }


    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }


    public String getSubscriptionId()
    {
        return this.subscriptionId;
    }


    public void setPaymentProvider(String paymentProvider)
    {
        this.paymentProvider = paymentProvider;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }
}
