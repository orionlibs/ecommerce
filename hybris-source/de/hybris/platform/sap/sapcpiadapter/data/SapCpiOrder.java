package de.hybris.platform.sap.sapcpiadapter.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sapCpiOrder")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SapCpiOrder implements Serializable
{
    private static final long serialVersionUID = 1L;
    private SapCpiConfig sapCpiConfig;
    private String orderId;
    private String creationDate;
    private String currencyIsoCode;
    private String paymentMode;
    private String deliveryMode;
    private String purchaseOrderNumber;
    private String baseStoreUid;
    private String channel;
    private String salesOrganization;
    private String distributionChannel;
    private String division;
    private String transactionType;
    private String shippingCondition;
    private List<SapCpiCreditCardPayment> sapCpiCreditCardPayments;
    private List<SapCpiOrderItem> sapCpiOrderItems;
    private List<SapCpiPartnerRole> sapCpiPartnerRoles;
    private List<SapCpiOrderAddress> sapCpiOrderAddresses;
    private List<SapCpiOrderPriceComponent> sapCpiOrderPriceComponents;


    public void setSapCpiConfig(SapCpiConfig sapCpiConfig)
    {
        this.sapCpiConfig = sapCpiConfig;
    }


    @XmlElement(name = "sapCpiConfig")
    public SapCpiConfig getSapCpiConfig()
    {
        return this.sapCpiConfig;
    }


    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }


    public String getOrderId()
    {
        return this.orderId;
    }


    public void setCreationDate(String creationDate)
    {
        this.creationDate = creationDate;
    }


    public String getCreationDate()
    {
        return this.creationDate;
    }


    public void setCurrencyIsoCode(String currencyIsoCode)
    {
        this.currencyIsoCode = currencyIsoCode;
    }


    public String getCurrencyIsoCode()
    {
        return this.currencyIsoCode;
    }


    public void setPaymentMode(String paymentMode)
    {
        this.paymentMode = paymentMode;
    }


    public String getPaymentMode()
    {
        return this.paymentMode;
    }


    public void setDeliveryMode(String deliveryMode)
    {
        this.deliveryMode = deliveryMode;
    }


    public String getDeliveryMode()
    {
        return this.deliveryMode;
    }


    public void setPurchaseOrderNumber(String purchaseOrderNumber)
    {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }


    public String getPurchaseOrderNumber()
    {
        return this.purchaseOrderNumber;
    }


    public void setBaseStoreUid(String baseStoreUid)
    {
        this.baseStoreUid = baseStoreUid;
    }


    public String getBaseStoreUid()
    {
        return this.baseStoreUid;
    }


    public void setChannel(String channel)
    {
        this.channel = channel;
    }


    public String getChannel()
    {
        return this.channel;
    }


    public void setSalesOrganization(String salesOrganization)
    {
        this.salesOrganization = salesOrganization;
    }


    public String getSalesOrganization()
    {
        return this.salesOrganization;
    }


    public void setDistributionChannel(String distributionChannel)
    {
        this.distributionChannel = distributionChannel;
    }


    public String getDistributionChannel()
    {
        return this.distributionChannel;
    }


    public void setDivision(String division)
    {
        this.division = division;
    }


    public String getDivision()
    {
        return this.division;
    }


    public void setTransactionType(String transactionType)
    {
        this.transactionType = transactionType;
    }


    public String getTransactionType()
    {
        return this.transactionType;
    }


    public void setShippingCondition(String shippingCondition)
    {
        this.shippingCondition = shippingCondition;
    }


    public String getShippingCondition()
    {
        return this.shippingCondition;
    }


    public void setSapCpiCreditCardPayments(List<SapCpiCreditCardPayment> sapCpiCreditCardPayments)
    {
        this.sapCpiCreditCardPayments = sapCpiCreditCardPayments;
    }


    @XmlElement(name = "sapCpiCreditCardPayments")
    public List<SapCpiCreditCardPayment> getSapCpiCreditCardPayments()
    {
        return this.sapCpiCreditCardPayments;
    }


    public void setSapCpiOrderItems(List<SapCpiOrderItem> sapCpiOrderItems)
    {
        this.sapCpiOrderItems = sapCpiOrderItems;
    }


    @XmlElement(name = "sapCpiOrderItems")
    public List<SapCpiOrderItem> getSapCpiOrderItems()
    {
        return this.sapCpiOrderItems;
    }


    public void setSapCpiPartnerRoles(List<SapCpiPartnerRole> sapCpiPartnerRoles)
    {
        this.sapCpiPartnerRoles = sapCpiPartnerRoles;
    }


    @XmlElement(name = "sapCpiPartnerRoles")
    public List<SapCpiPartnerRole> getSapCpiPartnerRoles()
    {
        return this.sapCpiPartnerRoles;
    }


    public void setSapCpiOrderAddresses(List<SapCpiOrderAddress> sapCpiOrderAddresses)
    {
        this.sapCpiOrderAddresses = sapCpiOrderAddresses;
    }


    @XmlElement(name = "sapCpiOrderAddresses")
    public List<SapCpiOrderAddress> getSapCpiOrderAddresses()
    {
        return this.sapCpiOrderAddresses;
    }


    public void setSapCpiOrderPriceComponents(List<SapCpiOrderPriceComponent> sapCpiOrderPriceComponents)
    {
        this.sapCpiOrderPriceComponents = sapCpiOrderPriceComponents;
    }


    @XmlElement(name = "sapCpiOrderPriceComponents")
    public List<SapCpiOrderPriceComponent> getSapCpiOrderPriceComponents()
    {
        return this.sapCpiOrderPriceComponents;
    }
}
