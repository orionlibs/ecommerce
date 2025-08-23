package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.b2b.enums.B2BPeriodRange;
import de.hybris.platform.b2b.enums.B2BRateType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.util.StandardDateRange;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class B2BCreditLimitModel extends B2BMerchantCheckModel
{
    public static final String _TYPECODE = "B2BCreditLimit";
    public static final String _B2BUNIT2B2BCREDITLIMIT = "B2BUnit2B2BCreditLimit";
    public static final String CURRENCY = "currency";
    public static final String AMOUNT = "amount";
    public static final String DATEPERIOD = "datePeriod";
    public static final String DATERANGE = "dateRange";
    public static final String ALERTTHRESHOLD = "alertThreshold";
    public static final String ALERTRATETYPE = "alertRateType";
    public static final String ALERTSENTDATE = "alertSentDate";
    public static final String UNIT = "Unit";


    public B2BCreditLimitModel()
    {
    }


    public B2BCreditLimitModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCreditLimitModel(String _code, CurrencyModel _currency)
    {
        setCode(_code);
        setCurrency(_currency);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCreditLimitModel(String _code, CurrencyModel _currency, ItemModel _owner)
    {
        setCode(_code);
        setCurrency(_currency);
        setOwner(_owner);
    }


    @Accessor(qualifier = "alertRateType", type = Accessor.Type.GETTER)
    public B2BRateType getAlertRateType()
    {
        return (B2BRateType)getPersistenceContext().getPropertyValue("alertRateType");
    }


    @Accessor(qualifier = "alertSentDate", type = Accessor.Type.GETTER)
    public Date getAlertSentDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("alertSentDate");
    }


    @Accessor(qualifier = "alertThreshold", type = Accessor.Type.GETTER)
    public BigDecimal getAlertThreshold()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("alertThreshold");
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
    public BigDecimal getAmount()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("amount");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "datePeriod", type = Accessor.Type.GETTER)
    public StandardDateRange getDatePeriod()
    {
        return (StandardDateRange)getPersistenceContext().getPropertyValue("datePeriod");
    }


    @Accessor(qualifier = "dateRange", type = Accessor.Type.GETTER)
    public B2BPeriodRange getDateRange()
    {
        return (B2BPeriodRange)getPersistenceContext().getPropertyValue("dateRange");
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.GETTER)
    public Collection<B2BUnitModel> getUnit()
    {
        return (Collection<B2BUnitModel>)getPersistenceContext().getPropertyValue("Unit");
    }


    @Accessor(qualifier = "alertRateType", type = Accessor.Type.SETTER)
    public void setAlertRateType(B2BRateType value)
    {
        getPersistenceContext().setPropertyValue("alertRateType", value);
    }


    @Accessor(qualifier = "alertSentDate", type = Accessor.Type.SETTER)
    public void setAlertSentDate(Date value)
    {
        getPersistenceContext().setPropertyValue("alertSentDate", value);
    }


    @Accessor(qualifier = "alertThreshold", type = Accessor.Type.SETTER)
    public void setAlertThreshold(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("alertThreshold", value);
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.SETTER)
    public void setAmount(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("amount", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "datePeriod", type = Accessor.Type.SETTER)
    public void setDatePeriod(StandardDateRange value)
    {
        getPersistenceContext().setPropertyValue("datePeriod", value);
    }


    @Accessor(qualifier = "dateRange", type = Accessor.Type.SETTER)
    public void setDateRange(B2BPeriodRange value)
    {
        getPersistenceContext().setPropertyValue("dateRange", value);
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.SETTER)
    public void setUnit(Collection<B2BUnitModel> value)
    {
        getPersistenceContext().setPropertyValue("Unit", value);
    }
}
