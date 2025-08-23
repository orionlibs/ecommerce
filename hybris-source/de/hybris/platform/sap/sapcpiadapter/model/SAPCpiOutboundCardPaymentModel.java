package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPCpiOutboundCardPaymentModel extends ItemModel
{
    public static final String _TYPECODE = "SAPCpiOutboundCardPayment";
    public static final String _SAPCPIOUTBOUNDORDER2SAPCPIOUTBOUNDCARDPAYMENT = "SAPCpiOutboundOrder2SAPCpiOutboundCardPayment";
    public static final String ORDERID = "orderId";
    public static final String REQUESTID = "requestId";
    public static final String CCOWNER = "ccOwner";
    public static final String VALIDTOMONTH = "validToMonth";
    public static final String VALIDTOYEAR = "validToYear";
    public static final String SUBSCRIPTIONID = "subscriptionId";
    public static final String PAYMENTPROVIDER = "paymentProvider";
    public static final String SAPCPIOUTBOUNDORDER = "sapCpiOutboundOrder";


    public SAPCpiOutboundCardPaymentModel()
    {
    }


    public SAPCpiOutboundCardPaymentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundCardPaymentModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "ccOwner", type = Accessor.Type.GETTER)
    public String getCcOwner()
    {
        return (String)getPersistenceContext().getPropertyValue("ccOwner");
    }


    @Accessor(qualifier = "orderId", type = Accessor.Type.GETTER)
    public String getOrderId()
    {
        return (String)getPersistenceContext().getPropertyValue("orderId");
    }


    @Accessor(qualifier = "paymentProvider", type = Accessor.Type.GETTER)
    public String getPaymentProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("paymentProvider");
    }


    @Accessor(qualifier = "requestId", type = Accessor.Type.GETTER)
    public String getRequestId()
    {
        return (String)getPersistenceContext().getPropertyValue("requestId");
    }


    @Accessor(qualifier = "sapCpiOutboundOrder", type = Accessor.Type.GETTER)
    public SAPCpiOutboundOrderModel getSapCpiOutboundOrder()
    {
        return (SAPCpiOutboundOrderModel)getPersistenceContext().getPropertyValue("sapCpiOutboundOrder");
    }


    @Accessor(qualifier = "subscriptionId", type = Accessor.Type.GETTER)
    public String getSubscriptionId()
    {
        return (String)getPersistenceContext().getPropertyValue("subscriptionId");
    }


    @Accessor(qualifier = "validToMonth", type = Accessor.Type.GETTER)
    public String getValidToMonth()
    {
        return (String)getPersistenceContext().getPropertyValue("validToMonth");
    }


    @Accessor(qualifier = "validToYear", type = Accessor.Type.GETTER)
    public String getValidToYear()
    {
        return (String)getPersistenceContext().getPropertyValue("validToYear");
    }


    @Accessor(qualifier = "ccOwner", type = Accessor.Type.SETTER)
    public void setCcOwner(String value)
    {
        getPersistenceContext().setPropertyValue("ccOwner", value);
    }


    @Accessor(qualifier = "orderId", type = Accessor.Type.SETTER)
    public void setOrderId(String value)
    {
        getPersistenceContext().setPropertyValue("orderId", value);
    }


    @Accessor(qualifier = "paymentProvider", type = Accessor.Type.SETTER)
    public void setPaymentProvider(String value)
    {
        getPersistenceContext().setPropertyValue("paymentProvider", value);
    }


    @Accessor(qualifier = "requestId", type = Accessor.Type.SETTER)
    public void setRequestId(String value)
    {
        getPersistenceContext().setPropertyValue("requestId", value);
    }


    @Accessor(qualifier = "sapCpiOutboundOrder", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundOrder(SAPCpiOutboundOrderModel value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundOrder", value);
    }


    @Accessor(qualifier = "subscriptionId", type = Accessor.Type.SETTER)
    public void setSubscriptionId(String value)
    {
        getPersistenceContext().setPropertyValue("subscriptionId", value);
    }


    @Accessor(qualifier = "validToMonth", type = Accessor.Type.SETTER)
    public void setValidToMonth(String value)
    {
        getPersistenceContext().setPropertyValue("validToMonth", value);
    }


    @Accessor(qualifier = "validToYear", type = Accessor.Type.SETTER)
    public void setValidToYear(String value)
    {
        getPersistenceContext().setPropertyValue("validToYear", value);
    }
}
