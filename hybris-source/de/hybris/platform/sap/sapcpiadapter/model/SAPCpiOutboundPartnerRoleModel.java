package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPCpiOutboundPartnerRoleModel extends ItemModel
{
    public static final String _TYPECODE = "SAPCpiOutboundPartnerRole";
    public static final String _SAPCPIOUTBOUNDORDER2SAPCPIOUTBOUNDPARTNERROLE = "SAPCpiOutboundOrder2SAPCpiOutboundPartnerRole";
    public static final String ORDERID = "orderId";
    public static final String ENTRYNUMBER = "entryNumber";
    public static final String PARTNERROLECODE = "partnerRoleCode";
    public static final String PARTNERID = "partnerId";
    public static final String DOCUMENTADDRESSID = "documentAddressId";
    public static final String SAPCPIOUTBOUNDORDER = "sapCpiOutboundOrder";


    public SAPCpiOutboundPartnerRoleModel()
    {
    }


    public SAPCpiOutboundPartnerRoleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundPartnerRoleModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "documentAddressId", type = Accessor.Type.GETTER)
    public String getDocumentAddressId()
    {
        return (String)getPersistenceContext().getPropertyValue("documentAddressId");
    }


    @Accessor(qualifier = "entryNumber", type = Accessor.Type.GETTER)
    public String getEntryNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("entryNumber");
    }


    @Accessor(qualifier = "orderId", type = Accessor.Type.GETTER)
    public String getOrderId()
    {
        return (String)getPersistenceContext().getPropertyValue("orderId");
    }


    @Accessor(qualifier = "partnerId", type = Accessor.Type.GETTER)
    public String getPartnerId()
    {
        return (String)getPersistenceContext().getPropertyValue("partnerId");
    }


    @Accessor(qualifier = "partnerRoleCode", type = Accessor.Type.GETTER)
    public String getPartnerRoleCode()
    {
        return (String)getPersistenceContext().getPropertyValue("partnerRoleCode");
    }


    @Accessor(qualifier = "sapCpiOutboundOrder", type = Accessor.Type.GETTER)
    public SAPCpiOutboundOrderModel getSapCpiOutboundOrder()
    {
        return (SAPCpiOutboundOrderModel)getPersistenceContext().getPropertyValue("sapCpiOutboundOrder");
    }


    @Accessor(qualifier = "documentAddressId", type = Accessor.Type.SETTER)
    public void setDocumentAddressId(String value)
    {
        getPersistenceContext().setPropertyValue("documentAddressId", value);
    }


    @Accessor(qualifier = "entryNumber", type = Accessor.Type.SETTER)
    public void setEntryNumber(String value)
    {
        getPersistenceContext().setPropertyValue("entryNumber", value);
    }


    @Accessor(qualifier = "orderId", type = Accessor.Type.SETTER)
    public void setOrderId(String value)
    {
        getPersistenceContext().setPropertyValue("orderId", value);
    }


    @Accessor(qualifier = "partnerId", type = Accessor.Type.SETTER)
    public void setPartnerId(String value)
    {
        getPersistenceContext().setPropertyValue("partnerId", value);
    }


    @Accessor(qualifier = "partnerRoleCode", type = Accessor.Type.SETTER)
    public void setPartnerRoleCode(String value)
    {
        getPersistenceContext().setPropertyValue("partnerRoleCode", value);
    }


    @Accessor(qualifier = "sapCpiOutboundOrder", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundOrder(SAPCpiOutboundOrderModel value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundOrder", value);
    }
}
