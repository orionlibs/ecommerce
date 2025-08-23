package de.hybris.platform.ordermanagementfacades.payment.data;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PaymentTransactionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String currencyIsocode;
    private List<PaymentTransactionEntryData> entries;
    private CCPaymentInfoData paymentInfo;
    private String paymentProvider;
    private BigDecimal plannedAmount;
    private String requestId;
    private String requestToken;
    private String versionID;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCurrencyIsocode(String currencyIsocode)
    {
        this.currencyIsocode = currencyIsocode;
    }


    public String getCurrencyIsocode()
    {
        return this.currencyIsocode;
    }


    public void setEntries(List<PaymentTransactionEntryData> entries)
    {
        this.entries = entries;
    }


    public List<PaymentTransactionEntryData> getEntries()
    {
        return this.entries;
    }


    public void setPaymentInfo(CCPaymentInfoData paymentInfo)
    {
        this.paymentInfo = paymentInfo;
    }


    public CCPaymentInfoData getPaymentInfo()
    {
        return this.paymentInfo;
    }


    public void setPaymentProvider(String paymentProvider)
    {
        this.paymentProvider = paymentProvider;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }


    public void setPlannedAmount(BigDecimal plannedAmount)
    {
        this.plannedAmount = plannedAmount;
    }


    public BigDecimal getPlannedAmount()
    {
        return this.plannedAmount;
    }


    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }


    public String getRequestId()
    {
        return this.requestId;
    }


    public void setRequestToken(String requestToken)
    {
        this.requestToken = requestToken;
    }


    public String getRequestToken()
    {
        return this.requestToken;
    }


    public void setVersionID(String versionID)
    {
        this.versionID = versionID;
    }


    public String getVersionID()
    {
        return this.versionID;
    }
}
