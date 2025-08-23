package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPPricingConditionModel extends ItemModel
{
    public static final String _TYPECODE = "SAPPricingCondition";
    public static final String _ORDERENTRY2SAPPRICINGCONDITION = "OrderEntry2SapPricingCondition";
    public static final String ORDER = "order";
    public static final String CONDITIONTYPE = "conditionType";
    public static final String STEPNUMBER = "stepNumber";
    public static final String CONDITIONCOUNTER = "conditionCounter";
    public static final String CURRENCYKEY = "currencyKey";
    public static final String CONDITIONPRICINGUNIT = "conditionPricingUnit";
    public static final String CONDITIONUNIT = "conditionUnit";
    public static final String CONDITIONCALCULATIONTYPE = "conditionCalculationType";
    public static final String CONDITIONRATE = "conditionRate";
    public static final String CONDITIONVALUE = "conditionValue";
    public static final String ORDERENTRY = "orderEntry";


    public SAPPricingConditionModel()
    {
    }


    public SAPPricingConditionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPPricingConditionModel(String _conditionType, String _order, AbstractOrderEntryModel _orderEntry, ItemModel _owner)
    {
        setConditionType(_conditionType);
        setOrder(_order);
        setOrderEntry(_orderEntry);
        setOwner(_owner);
    }


    @Accessor(qualifier = "conditionCalculationType", type = Accessor.Type.GETTER)
    public String getConditionCalculationType()
    {
        return (String)getPersistenceContext().getPropertyValue("conditionCalculationType");
    }


    @Accessor(qualifier = "conditionCounter", type = Accessor.Type.GETTER)
    public String getConditionCounter()
    {
        return (String)getPersistenceContext().getPropertyValue("conditionCounter");
    }


    @Accessor(qualifier = "conditionPricingUnit", type = Accessor.Type.GETTER)
    public String getConditionPricingUnit()
    {
        return (String)getPersistenceContext().getPropertyValue("conditionPricingUnit");
    }


    @Accessor(qualifier = "conditionRate", type = Accessor.Type.GETTER)
    public String getConditionRate()
    {
        return (String)getPersistenceContext().getPropertyValue("conditionRate");
    }


    @Accessor(qualifier = "conditionType", type = Accessor.Type.GETTER)
    public String getConditionType()
    {
        return (String)getPersistenceContext().getPropertyValue("conditionType");
    }


    @Accessor(qualifier = "conditionUnit", type = Accessor.Type.GETTER)
    public String getConditionUnit()
    {
        return (String)getPersistenceContext().getPropertyValue("conditionUnit");
    }


    @Accessor(qualifier = "conditionValue", type = Accessor.Type.GETTER)
    public String getConditionValue()
    {
        return (String)getPersistenceContext().getPropertyValue("conditionValue");
    }


    @Accessor(qualifier = "currencyKey", type = Accessor.Type.GETTER)
    public String getCurrencyKey()
    {
        return (String)getPersistenceContext().getPropertyValue("currencyKey");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public String getOrder()
    {
        return (String)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
    public AbstractOrderEntryModel getOrderEntry()
    {
        return (AbstractOrderEntryModel)getPersistenceContext().getPropertyValue("orderEntry");
    }


    @Accessor(qualifier = "stepNumber", type = Accessor.Type.GETTER)
    public String getStepNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("stepNumber");
    }


    @Accessor(qualifier = "conditionCalculationType", type = Accessor.Type.SETTER)
    public void setConditionCalculationType(String value)
    {
        getPersistenceContext().setPropertyValue("conditionCalculationType", value);
    }


    @Accessor(qualifier = "conditionCounter", type = Accessor.Type.SETTER)
    public void setConditionCounter(String value)
    {
        getPersistenceContext().setPropertyValue("conditionCounter", value);
    }


    @Accessor(qualifier = "conditionPricingUnit", type = Accessor.Type.SETTER)
    public void setConditionPricingUnit(String value)
    {
        getPersistenceContext().setPropertyValue("conditionPricingUnit", value);
    }


    @Accessor(qualifier = "conditionRate", type = Accessor.Type.SETTER)
    public void setConditionRate(String value)
    {
        getPersistenceContext().setPropertyValue("conditionRate", value);
    }


    @Accessor(qualifier = "conditionType", type = Accessor.Type.SETTER)
    public void setConditionType(String value)
    {
        getPersistenceContext().setPropertyValue("conditionType", value);
    }


    @Accessor(qualifier = "conditionUnit", type = Accessor.Type.SETTER)
    public void setConditionUnit(String value)
    {
        getPersistenceContext().setPropertyValue("conditionUnit", value);
    }


    @Accessor(qualifier = "conditionValue", type = Accessor.Type.SETTER)
    public void setConditionValue(String value)
    {
        getPersistenceContext().setPropertyValue("conditionValue", value);
    }


    @Accessor(qualifier = "currencyKey", type = Accessor.Type.SETTER)
    public void setCurrencyKey(String value)
    {
        getPersistenceContext().setPropertyValue("currencyKey", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(String value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
    public void setOrderEntry(AbstractOrderEntryModel value)
    {
        getPersistenceContext().setPropertyValue("orderEntry", value);
    }


    @Accessor(qualifier = "stepNumber", type = Accessor.Type.SETTER)
    public void setStepNumber(String value)
    {
        getPersistenceContext().setPropertyValue("stepNumber", value);
    }
}
