package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPCpiOutboundPriceComponentModel extends ItemModel
{
    public static final String _TYPECODE = "SAPCpiOutboundPriceComponent";
    public static final String _SAPCPIOUTBOUNDORDER2SAPCPIOUTBOUNDPRICECOMPONENT = "SAPCpiOutboundOrder2SAPCpiOutboundPriceComponent";
    public static final String ORDERID = "orderId";
    public static final String ENTRYNUMBER = "entryNumber";
    public static final String VALUE = "value";
    public static final String UNIT = "unit";
    public static final String ABSOLUTE = "absolute";
    public static final String CONDITIONCODE = "conditionCode";
    public static final String CONDITIONCOUNTER = "conditionCounter";
    public static final String CURRENCYISOCODE = "currencyIsoCode";
    public static final String PRICEQUANTITY = "priceQuantity";
    public static final String SAPCPIOUTBOUNDORDER = "sapCpiOutboundOrder";


    public SAPCpiOutboundPriceComponentModel()
    {
    }


    public SAPCpiOutboundPriceComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundPriceComponentModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "absolute", type = Accessor.Type.GETTER)
    public String getAbsolute()
    {
        return (String)getPersistenceContext().getPropertyValue("absolute");
    }


    @Accessor(qualifier = "conditionCode", type = Accessor.Type.GETTER)
    public String getConditionCode()
    {
        return (String)getPersistenceContext().getPropertyValue("conditionCode");
    }


    @Accessor(qualifier = "conditionCounter", type = Accessor.Type.GETTER)
    public String getConditionCounter()
    {
        return (String)getPersistenceContext().getPropertyValue("conditionCounter");
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


    @Accessor(qualifier = "orderId", type = Accessor.Type.GETTER)
    public String getOrderId()
    {
        return (String)getPersistenceContext().getPropertyValue("orderId");
    }


    @Accessor(qualifier = "priceQuantity", type = Accessor.Type.GETTER)
    public String getPriceQuantity()
    {
        return (String)getPersistenceContext().getPropertyValue("priceQuantity");
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


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public String getValue()
    {
        return (String)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "absolute", type = Accessor.Type.SETTER)
    public void setAbsolute(String value)
    {
        getPersistenceContext().setPropertyValue("absolute", value);
    }


    @Accessor(qualifier = "conditionCode", type = Accessor.Type.SETTER)
    public void setConditionCode(String value)
    {
        getPersistenceContext().setPropertyValue("conditionCode", value);
    }


    @Accessor(qualifier = "conditionCounter", type = Accessor.Type.SETTER)
    public void setConditionCounter(String value)
    {
        getPersistenceContext().setPropertyValue("conditionCounter", value);
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


    @Accessor(qualifier = "orderId", type = Accessor.Type.SETTER)
    public void setOrderId(String value)
    {
        getPersistenceContext().setPropertyValue("orderId", value);
    }


    @Accessor(qualifier = "priceQuantity", type = Accessor.Type.SETTER)
    public void setPriceQuantity(String value)
    {
        getPersistenceContext().setPropertyValue("priceQuantity", value);
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


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(String value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
