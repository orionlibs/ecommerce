package de.hybris.platform.ordermanagementfacades.order.data;

import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.enums.DeliveryStatus;
import de.hybris.platform.ordermanagementfacades.payment.data.PaymentTransactionData;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class OrderRequestData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String externalOrderCode;
    private String name;
    private String description;
    private String guid;
    private CustomerData user;
    private String siteUid;
    private String storeUid;
    private String currencyIsocode;
    private String languageIsocode;
    private AddressData deliveryAddress;
    private AddressData paymentAddress;
    private String deliveryModeCode;
    private DeliveryStatus deliveryStatus;
    private boolean net;
    private boolean calculated;
    private double totalPrice;
    private double totalTax;
    private double subtotal;
    private double deliveryCost;
    private Date expirationTime;
    private List<OrderEntryRequestData> entries;
    private List<PaymentTransactionData> paymentTransactions;
    private Set<PromotionResultData> allPromotionResults;


    public void setExternalOrderCode(String externalOrderCode)
    {
        this.externalOrderCode = externalOrderCode;
    }


    public String getExternalOrderCode()
    {
        return this.externalOrderCode;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    public String getGuid()
    {
        return this.guid;
    }


    public void setUser(CustomerData user)
    {
        this.user = user;
    }


    public CustomerData getUser()
    {
        return this.user;
    }


    public void setSiteUid(String siteUid)
    {
        this.siteUid = siteUid;
    }


    public String getSiteUid()
    {
        return this.siteUid;
    }


    public void setStoreUid(String storeUid)
    {
        this.storeUid = storeUid;
    }


    public String getStoreUid()
    {
        return this.storeUid;
    }


    public void setCurrencyIsocode(String currencyIsocode)
    {
        this.currencyIsocode = currencyIsocode;
    }


    public String getCurrencyIsocode()
    {
        return this.currencyIsocode;
    }


    public void setLanguageIsocode(String languageIsocode)
    {
        this.languageIsocode = languageIsocode;
    }


    public String getLanguageIsocode()
    {
        return this.languageIsocode;
    }


    public void setDeliveryAddress(AddressData deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }


    public AddressData getDeliveryAddress()
    {
        return this.deliveryAddress;
    }


    public void setPaymentAddress(AddressData paymentAddress)
    {
        this.paymentAddress = paymentAddress;
    }


    public AddressData getPaymentAddress()
    {
        return this.paymentAddress;
    }


    public void setDeliveryModeCode(String deliveryModeCode)
    {
        this.deliveryModeCode = deliveryModeCode;
    }


    public String getDeliveryModeCode()
    {
        return this.deliveryModeCode;
    }


    public void setDeliveryStatus(DeliveryStatus deliveryStatus)
    {
        this.deliveryStatus = deliveryStatus;
    }


    public DeliveryStatus getDeliveryStatus()
    {
        return this.deliveryStatus;
    }


    public void setNet(boolean net)
    {
        this.net = net;
    }


    public boolean isNet()
    {
        return this.net;
    }


    public void setCalculated(boolean calculated)
    {
        this.calculated = calculated;
    }


    public boolean isCalculated()
    {
        return this.calculated;
    }


    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }


    public double getTotalPrice()
    {
        return this.totalPrice;
    }


    public void setTotalTax(double totalTax)
    {
        this.totalTax = totalTax;
    }


    public double getTotalTax()
    {
        return this.totalTax;
    }


    public void setSubtotal(double subtotal)
    {
        this.subtotal = subtotal;
    }


    public double getSubtotal()
    {
        return this.subtotal;
    }


    public void setDeliveryCost(double deliveryCost)
    {
        this.deliveryCost = deliveryCost;
    }


    public double getDeliveryCost()
    {
        return this.deliveryCost;
    }


    public void setExpirationTime(Date expirationTime)
    {
        this.expirationTime = expirationTime;
    }


    public Date getExpirationTime()
    {
        return this.expirationTime;
    }


    public void setEntries(List<OrderEntryRequestData> entries)
    {
        this.entries = entries;
    }


    public List<OrderEntryRequestData> getEntries()
    {
        return this.entries;
    }


    public void setPaymentTransactions(List<PaymentTransactionData> paymentTransactions)
    {
        this.paymentTransactions = paymentTransactions;
    }


    public List<PaymentTransactionData> getPaymentTransactions()
    {
        return this.paymentTransactions;
    }


    public void setAllPromotionResults(Set<PromotionResultData> allPromotionResults)
    {
        this.allPromotionResults = allPromotionResults;
    }


    public Set<PromotionResultData> getAllPromotionResults()
    {
        return this.allPromotionResults;
    }
}
