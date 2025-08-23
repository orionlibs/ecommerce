package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

public class B2BCostCenterModel extends ItemModel
{
    public static final String _TYPECODE = "B2BCostCenter";
    public static final String _B2BUNIT2B2BCOSTCENTER = "B2BUnit2B2BCostCenter";
    public static final String _B2BBUDGETS2COSTCENTERS = "B2BBudgets2CostCenters";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String CURRENCY = "currency";
    public static final String ACTIVE = "active";
    public static final String UNIT = "Unit";
    public static final String BUDGETS = "Budgets";


    public B2BCostCenterModel()
    {
    }


    public B2BCostCenterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCostCenterModel(B2BUnitModel _Unit, String _code, CurrencyModel _currency)
    {
        setUnit(_Unit);
        setCode(_code);
        setCurrency(_currency);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCostCenterModel(B2BUnitModel _Unit, String _code, CurrencyModel _currency, ItemModel _owner)
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


    @Accessor(qualifier = "Budgets", type = Accessor.Type.GETTER)
    public Set<B2BBudgetModel> getBudgets()
    {
        return (Set<B2BBudgetModel>)getPersistenceContext().getPropertyValue("Budgets");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
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


    @Accessor(qualifier = "Budgets", type = Accessor.Type.SETTER)
    public void setBudgets(Set<B2BBudgetModel> value)
    {
        getPersistenceContext().setPropertyValue("Budgets", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
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
