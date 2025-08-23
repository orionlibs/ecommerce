package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.util.StandardDateRange;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Set;

public class B2BBudgetModel extends ItemModel
{
    public static final String _TYPECODE = "B2BBudget";
    public static final String _B2BUNIT2B2BBUDGET = "B2BUnit2B2BBudget";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String BUDGET = "budget";
    public static final String CURRENCY = "currency";
    public static final String DATERANGE = "dateRange";
    public static final String ACTIVE = "active";
    public static final String UNIT = "Unit";
    public static final String COSTCENTERS = "CostCenters";


    public B2BBudgetModel()
    {
    }


    public B2BBudgetModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BBudgetModel(B2BUnitModel _Unit, String _code, CurrencyModel _currency)
    {
        setUnit(_Unit);
        setCode(_code);
        setCurrency(_currency);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BBudgetModel(B2BUnitModel _Unit, String _code, CurrencyModel _currency, ItemModel _owner)
    {
        setUnit(_Unit);
        setCode(_code);
        setCurrency(_currency);
        setOwner(_owner);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "budget", type = Accessor.Type.GETTER)
    public BigDecimal getBudget()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("budget");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "CostCenters", type = Accessor.Type.GETTER)
    public Set<B2BCostCenterModel> getCostCenters()
    {
        return (Set<B2BCostCenterModel>)getPersistenceContext().getPropertyValue("CostCenters");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "dateRange", type = Accessor.Type.GETTER)
    public StandardDateRange getDateRange()
    {
        return (StandardDateRange)getPersistenceContext().getPropertyValue("dateRange");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.GETTER)
    public B2BUnitModel getUnit()
    {
        return (B2BUnitModel)getPersistenceContext().getPropertyValue("Unit");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "budget", type = Accessor.Type.SETTER)
    public void setBudget(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("budget", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "CostCenters", type = Accessor.Type.SETTER)
    public void setCostCenters(Set<B2BCostCenterModel> value)
    {
        getPersistenceContext().setPropertyValue("CostCenters", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "dateRange", type = Accessor.Type.SETTER)
    public void setDateRange(StandardDateRange value)
    {
        getPersistenceContext().setPropertyValue("dateRange", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.SETTER)
    public void setUnit(B2BUnitModel value)
    {
        getPersistenceContext().setPropertyValue("Unit", value);
    }
}
