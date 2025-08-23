package com.hybris.cis.api.subscription.mock.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

public class PaymentMethodMock implements Serializable
{
    public static final String MERCHANT_PAYMENT_METHOD_ID = "merchantPaymentMethodId";
    public static final String ACTIVE = "active";
    private static final long serialVersionUID = -7366168670988785187L;
    private String merchantPaymentMethodId;
    private boolean isActive = true;
    private PaymentMethodMockData paymentMethodMockData;


    public String getMerchantPaymentMethodId()
    {
        return this.merchantPaymentMethodId;
    }


    public void setMerchantPaymentMethodId(String merchantPaymentMethodId)
    {
        this.merchantPaymentMethodId = merchantPaymentMethodId;
    }


    public boolean isActive()
    {
        return this.isActive;
    }


    public void setActive(boolean active)
    {
        this.isActive = active;
    }


    public PaymentMethodMockData getPaymentMethodMockData()
    {
        return this.paymentMethodMockData;
    }


    public void setPaymentMethodMockData(PaymentMethodMockData paymentMethodMockData)
    {
        this.paymentMethodMockData = new PaymentMethodMockData();
        if(paymentMethodMockData != null)
        {
            this.paymentMethodMockData.setAccountHolderName(paymentMethodMockData.getAccountHolderName());
            this.paymentMethodMockData.setCardNumber(paymentMethodMockData.getCardNumber());
            this.paymentMethodMockData.setCardType(paymentMethodMockData.getCardType());
            this.paymentMethodMockData.setExpiryDate(paymentMethodMockData.getExpiryDate());
            this.paymentMethodMockData.setIssueNumber(paymentMethodMockData.getIssueNumber());
            this.paymentMethodMockData.setId(paymentMethodMockData.getId());
            this.paymentMethodMockData.setStartDate(paymentMethodMockData.getStartDate());
            if(paymentMethodMockData.getAddressMock() != null)
            {
                this.paymentMethodMockData.setAddressMock(paymentMethodMockData.getAddressMock());
            }
        }
    }


    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if(this.paymentMethodMockData != null)
        {
            builder.append("MerchantPaymentMethodId: ").append(this.merchantPaymentMethodId).append(System.lineSeparator());
            builder.append("Active:                  ").append(Boolean.toString(this.isActive)).append(System.lineSeparator());
            builder.append(this.paymentMethodMockData.toString());
        }
        if(this.merchantPaymentMethodId != null)
        {
            builder.append("identifier:        ").append(this.merchantPaymentMethodId).append(System.lineSeparator());
        }
        return builder.toString();
    }


    public Map<String, String> getMap()
    {
        Map<String, String> map = new HashMap<>();
        map.put("merchantPaymentMethodId", this.merchantPaymentMethodId);
        map.put("active", Boolean.toString(this.isActive));
        if(this.paymentMethodMockData != null)
        {
            map.putAll(this.paymentMethodMockData.getMap());
        }
        return map;
    }


    public static PaymentMethodMock copyInstance(PaymentMethodMock instance)
    {
        Assert.notNull(instance, "Parameter instance may not be null.");
        PaymentMethodMock copy = new PaymentMethodMock();
        copy.setActive(instance.isActive());
        copy.setMerchantPaymentMethodId(instance.getMerchantPaymentMethodId());
        copy.setPaymentMethodMockData(PaymentMethodMockData.copyInstance(instance.getPaymentMethodMockData()));
        return copy;
    }
}
