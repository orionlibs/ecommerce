package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPCpiOutboundOrderItemModel extends ItemModel
{
    public static final String _TYPECODE = "SAPCpiOutboundOrderItem";
    public static final String _SAPCPIOUTBOUNDORDER2SAPCPIOUTBOUNDORDERITEM = "SAPCpiOutboundOrder2SAPCpiOutboundOrderItem";
    public static final String ORDERID = "orderId";
    public static final String ENTRYNUMBER = "entryNumber";
    public static final String QUANTITY = "quantity";
    public static final String CURRENCYISOCODE = "currencyIsoCode";
    public static final String UNIT = "unit";
    public static final String PRODUCTCODE = "productCode";
    public static final String PRODUCTNAME = "productName";
    public static final String PLANT = "plant";
    public static final String NAMEDDELIVERYDATE = "namedDeliveryDate";
    public static final String ITEMCATEGORY = "itemCategory";
    public static final String SAPCPIOUTBOUNDORDER = "sapCpiOutboundOrder";


    public SAPCpiOutboundOrderItemModel()
    {
    }


    public SAPCpiOutboundOrderItemModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundOrderItemModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "currencyIsoCode", type = Accessor.Type.GETTER)
    public String getCurrencyIsoCode()
    {
        return (String)getPersistenceContext().getPropertyValue("currencyIsoCode");
    }


    @Accessor(qualifier = "entryNumber", type = Accessor.Type.GETTER)
    public String getEntryNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("entryNumber");
    }


    @Accessor(qualifier = "itemCategory", type = Accessor.Type.GETTER)
    public String getItemCategory()
    {
        return (String)getPersistenceContext().getPropertyValue("itemCategory");
    }


    @Accessor(qualifier = "namedDeliveryDate", type = Accessor.Type.GETTER)
    public String getNamedDeliveryDate()
    {
        return (String)getPersistenceContext().getPropertyValue("namedDeliveryDate");
    }


    @Accessor(qualifier = "orderId", type = Accessor.Type.GETTER)
    public String getOrderId()
    {
        return (String)getPersistenceContext().getPropertyValue("orderId");
    }


    @Accessor(qualifier = "plant", type = Accessor.Type.GETTER)
    public String getPlant()
    {
        return (String)getPersistenceContext().getPropertyValue("plant");
    }


    @Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
    public String getProductCode()
    {
        return (String)getPersistenceContext().getPropertyValue("productCode");
    }


    @Accessor(qualifier = "productName", type = Accessor.Type.GETTER)
    public String getProductName()
    {
        return (String)getPersistenceContext().getPropertyValue("productName");
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
    public String getQuantity()
    {
        return (String)getPersistenceContext().getPropertyValue("quantity");
    }


    @Accessor(qualifier = "sapCpiOutboundOrder", type = Accessor.Type.GETTER)
    public SAPCpiOutboundOrderModel getSapCpiOutboundOrder()
    {
        return (SAPCpiOutboundOrderModel)getPersistenceContext().getPropertyValue("sapCpiOutboundOrder");
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
    public String getUnit()
    {
        return (String)getPersistenceContext().getPropertyValue("unit");
    }


    @Accessor(qualifier = "currencyIsoCode", type = Accessor.Type.SETTER)
    public void setCurrencyIsoCode(String value)
    {
        getPersistenceContext().setPropertyValue("currencyIsoCode", value);
    }


    @Accessor(qualifier = "entryNumber", type = Accessor.Type.SETTER)
    public void setEntryNumber(String value)
    {
        getPersistenceContext().setPropertyValue("entryNumber", value);
    }


    @Accessor(qualifier = "itemCategory", type = Accessor.Type.SETTER)
    public void setItemCategory(String value)
    {
        getPersistenceContext().setPropertyValue("itemCategory", value);
    }


    @Accessor(qualifier = "namedDeliveryDate", type = Accessor.Type.SETTER)
    public void setNamedDeliveryDate(String value)
    {
        getPersistenceContext().setPropertyValue("namedDeliveryDate", value);
    }


    @Accessor(qualifier = "orderId", type = Accessor.Type.SETTER)
    public void setOrderId(String value)
    {
        getPersistenceContext().setPropertyValue("orderId", value);
    }


    @Accessor(qualifier = "plant", type = Accessor.Type.SETTER)
    public void setPlant(String value)
    {
        getPersistenceContext().setPropertyValue("plant", value);
    }


    @Accessor(qualifier = "productCode", type = Accessor.Type.SETTER)
    public void setProductCode(String value)
    {
        getPersistenceContext().setPropertyValue("productCode", value);
    }


    @Accessor(qualifier = "productName", type = Accessor.Type.SETTER)
    public void setProductName(String value)
    {
        getPersistenceContext().setPropertyValue("productName", value);
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
    public void setQuantity(String value)
    {
        getPersistenceContext().setPropertyValue("quantity", value);
    }


    @Accessor(qualifier = "sapCpiOutboundOrder", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundOrder(SAPCpiOutboundOrderModel value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundOrder", value);
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
    public void setUnit(String value)
    {
        getPersistenceContext().setPropertyValue("unit", value);
    }
}
