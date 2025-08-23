package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class SAPCpiOutboundOrderModel extends ItemModel
{
    public static final String _TYPECODE = "SAPCpiOutboundOrder";
    public static final String ORDERID = "orderId";
    public static final String CREATIONDATE = "creationDate";
    public static final String CURRENCYISOCODE = "currencyIsoCode";
    public static final String PAYMENTMODE = "paymentMode";
    public static final String DELIVERYMODE = "deliveryMode";
    public static final String PURCHASEORDERNUMBER = "purchaseOrderNumber";
    public static final String BASESTOREUID = "baseStoreUid";
    public static final String CHANNEL = "channel";
    public static final String SALESORGANIZATION = "salesOrganization";
    public static final String DISTRIBUTIONCHANNEL = "distributionChannel";
    public static final String DIVISION = "division";
    public static final String TRANSACTIONTYPE = "transactionType";
    public static final String SHIPPINGCONDITION = "shippingCondition";
    public static final String RESPONSESTATUS = "responseStatus";
    public static final String RESPONSEMESSAGE = "responseMessage";
    public static final String SAPCPICONFIG = "sapCpiConfig";
    public static final String SAPCPIOUTBOUNDORDERITEMS = "sapCpiOutboundOrderItems";
    public static final String SAPCPIOUTBOUNDPARTNERROLES = "sapCpiOutboundPartnerRoles";
    public static final String SAPCPIOUTBOUNDCARDPAYMENTS = "sapCpiOutboundCardPayments";
    public static final String SAPCPIOUTBOUNDPRICECOMPONENTS = "sapCpiOutboundPriceComponents";
    public static final String SAPCPIOUTBOUNDADDRESSES = "sapCpiOutboundAddresses";


    public SAPCpiOutboundOrderModel()
    {
    }


    public SAPCpiOutboundOrderModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundOrderModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "baseStoreUid", type = Accessor.Type.GETTER)
    public String getBaseStoreUid()
    {
        return (String)getPersistenceContext().getPropertyValue("baseStoreUid");
    }


    @Accessor(qualifier = "channel", type = Accessor.Type.GETTER)
    public String getChannel()
    {
        return (String)getPersistenceContext().getPropertyValue("channel");
    }


    @Accessor(qualifier = "creationDate", type = Accessor.Type.GETTER)
    public String getCreationDate()
    {
        return (String)getPersistenceContext().getPropertyValue("creationDate");
    }


    @Accessor(qualifier = "currencyIsoCode", type = Accessor.Type.GETTER)
    public String getCurrencyIsoCode()
    {
        return (String)getPersistenceContext().getPropertyValue("currencyIsoCode");
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
    public String getDeliveryMode()
    {
        return (String)getPersistenceContext().getPropertyValue("deliveryMode");
    }


    @Accessor(qualifier = "distributionChannel", type = Accessor.Type.GETTER)
    public String getDistributionChannel()
    {
        return (String)getPersistenceContext().getPropertyValue("distributionChannel");
    }


    @Accessor(qualifier = "division", type = Accessor.Type.GETTER)
    public String getDivision()
    {
        return (String)getPersistenceContext().getPropertyValue("division");
    }


    @Accessor(qualifier = "orderId", type = Accessor.Type.GETTER)
    public String getOrderId()
    {
        return (String)getPersistenceContext().getPropertyValue("orderId");
    }


    @Accessor(qualifier = "paymentMode", type = Accessor.Type.GETTER)
    public String getPaymentMode()
    {
        return (String)getPersistenceContext().getPropertyValue("paymentMode");
    }


    @Accessor(qualifier = "purchaseOrderNumber", type = Accessor.Type.GETTER)
    public String getPurchaseOrderNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("purchaseOrderNumber");
    }


    @Accessor(qualifier = "responseMessage", type = Accessor.Type.GETTER)
    public String getResponseMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("responseMessage");
    }


    @Accessor(qualifier = "responseStatus", type = Accessor.Type.GETTER)
    public String getResponseStatus()
    {
        return (String)getPersistenceContext().getPropertyValue("responseStatus");
    }


    @Accessor(qualifier = "salesOrganization", type = Accessor.Type.GETTER)
    public String getSalesOrganization()
    {
        return (String)getPersistenceContext().getPropertyValue("salesOrganization");
    }


    @Accessor(qualifier = "sapCpiConfig", type = Accessor.Type.GETTER)
    public SAPCpiOutboundConfigModel getSapCpiConfig()
    {
        return (SAPCpiOutboundConfigModel)getPersistenceContext().getPropertyValue("sapCpiConfig");
    }


    @Accessor(qualifier = "sapCpiOutboundAddresses", type = Accessor.Type.GETTER)
    public Set<SAPCpiOutboundAddressModel> getSapCpiOutboundAddresses()
    {
        return (Set<SAPCpiOutboundAddressModel>)getPersistenceContext().getPropertyValue("sapCpiOutboundAddresses");
    }


    @Accessor(qualifier = "sapCpiOutboundCardPayments", type = Accessor.Type.GETTER)
    public Set<SAPCpiOutboundCardPaymentModel> getSapCpiOutboundCardPayments()
    {
        return (Set<SAPCpiOutboundCardPaymentModel>)getPersistenceContext().getPropertyValue("sapCpiOutboundCardPayments");
    }


    @Accessor(qualifier = "sapCpiOutboundOrderItems", type = Accessor.Type.GETTER)
    public Set<SAPCpiOutboundOrderItemModel> getSapCpiOutboundOrderItems()
    {
        return (Set<SAPCpiOutboundOrderItemModel>)getPersistenceContext().getPropertyValue("sapCpiOutboundOrderItems");
    }


    @Accessor(qualifier = "sapCpiOutboundPartnerRoles", type = Accessor.Type.GETTER)
    public Set<SAPCpiOutboundPartnerRoleModel> getSapCpiOutboundPartnerRoles()
    {
        return (Set<SAPCpiOutboundPartnerRoleModel>)getPersistenceContext().getPropertyValue("sapCpiOutboundPartnerRoles");
    }


    @Accessor(qualifier = "sapCpiOutboundPriceComponents", type = Accessor.Type.GETTER)
    public Set<SAPCpiOutboundPriceComponentModel> getSapCpiOutboundPriceComponents()
    {
        return (Set<SAPCpiOutboundPriceComponentModel>)getPersistenceContext().getPropertyValue("sapCpiOutboundPriceComponents");
    }


    @Accessor(qualifier = "shippingCondition", type = Accessor.Type.GETTER)
    public String getShippingCondition()
    {
        return (String)getPersistenceContext().getPropertyValue("shippingCondition");
    }


    @Accessor(qualifier = "transactionType", type = Accessor.Type.GETTER)
    public String getTransactionType()
    {
        return (String)getPersistenceContext().getPropertyValue("transactionType");
    }


    @Accessor(qualifier = "baseStoreUid", type = Accessor.Type.SETTER)
    public void setBaseStoreUid(String value)
    {
        getPersistenceContext().setPropertyValue("baseStoreUid", value);
    }


    @Accessor(qualifier = "channel", type = Accessor.Type.SETTER)
    public void setChannel(String value)
    {
        getPersistenceContext().setPropertyValue("channel", value);
    }


    @Accessor(qualifier = "creationDate", type = Accessor.Type.SETTER)
    public void setCreationDate(String value)
    {
        getPersistenceContext().setPropertyValue("creationDate", value);
    }


    @Accessor(qualifier = "currencyIsoCode", type = Accessor.Type.SETTER)
    public void setCurrencyIsoCode(String value)
    {
        getPersistenceContext().setPropertyValue("currencyIsoCode", value);
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
    public void setDeliveryMode(String value)
    {
        getPersistenceContext().setPropertyValue("deliveryMode", value);
    }


    @Accessor(qualifier = "distributionChannel", type = Accessor.Type.SETTER)
    public void setDistributionChannel(String value)
    {
        getPersistenceContext().setPropertyValue("distributionChannel", value);
    }


    @Accessor(qualifier = "division", type = Accessor.Type.SETTER)
    public void setDivision(String value)
    {
        getPersistenceContext().setPropertyValue("division", value);
    }


    @Accessor(qualifier = "orderId", type = Accessor.Type.SETTER)
    public void setOrderId(String value)
    {
        getPersistenceContext().setPropertyValue("orderId", value);
    }


    @Accessor(qualifier = "paymentMode", type = Accessor.Type.SETTER)
    public void setPaymentMode(String value)
    {
        getPersistenceContext().setPropertyValue("paymentMode", value);
    }


    @Accessor(qualifier = "purchaseOrderNumber", type = Accessor.Type.SETTER)
    public void setPurchaseOrderNumber(String value)
    {
        getPersistenceContext().setPropertyValue("purchaseOrderNumber", value);
    }


    @Accessor(qualifier = "responseMessage", type = Accessor.Type.SETTER)
    public void setResponseMessage(String value)
    {
        getPersistenceContext().setPropertyValue("responseMessage", value);
    }


    @Accessor(qualifier = "responseStatus", type = Accessor.Type.SETTER)
    public void setResponseStatus(String value)
    {
        getPersistenceContext().setPropertyValue("responseStatus", value);
    }


    @Accessor(qualifier = "salesOrganization", type = Accessor.Type.SETTER)
    public void setSalesOrganization(String value)
    {
        getPersistenceContext().setPropertyValue("salesOrganization", value);
    }


    @Accessor(qualifier = "sapCpiConfig", type = Accessor.Type.SETTER)
    public void setSapCpiConfig(SAPCpiOutboundConfigModel value)
    {
        getPersistenceContext().setPropertyValue("sapCpiConfig", value);
    }


    @Accessor(qualifier = "sapCpiOutboundAddresses", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundAddresses(Set<SAPCpiOutboundAddressModel> value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundAddresses", value);
    }


    @Accessor(qualifier = "sapCpiOutboundCardPayments", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundCardPayments(Set<SAPCpiOutboundCardPaymentModel> value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundCardPayments", value);
    }


    @Accessor(qualifier = "sapCpiOutboundOrderItems", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundOrderItems(Set<SAPCpiOutboundOrderItemModel> value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundOrderItems", value);
    }


    @Accessor(qualifier = "sapCpiOutboundPartnerRoles", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundPartnerRoles(Set<SAPCpiOutboundPartnerRoleModel> value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundPartnerRoles", value);
    }


    @Accessor(qualifier = "sapCpiOutboundPriceComponents", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundPriceComponents(Set<SAPCpiOutboundPriceComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundPriceComponents", value);
    }


    @Accessor(qualifier = "shippingCondition", type = Accessor.Type.SETTER)
    public void setShippingCondition(String value)
    {
        getPersistenceContext().setPropertyValue("shippingCondition", value);
    }


    @Accessor(qualifier = "transactionType", type = Accessor.Type.SETTER)
    public void setTransactionType(String value)
    {
        getPersistenceContext().setPropertyValue("transactionType", value);
    }
}
