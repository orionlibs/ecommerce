package de.hybris.platform.b2bcommercefacades.company.data;

import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import java.io.Serializable;
import java.util.List;

public class B2BCostCenterData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private B2BUnitData unit;
    private String code;
    private String originalCode;
    private String name;
    private CurrencyData currency;
    private boolean active;
    private List<B2BBudgetData> b2bBudgetData;
    private boolean selected;


    public void setUnit(B2BUnitData unit)
    {
        this.unit = unit;
    }


    public B2BUnitData getUnit()
    {
        return this.unit;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setOriginalCode(String originalCode)
    {
        this.originalCode = originalCode;
    }


    public String getOriginalCode()
    {
        return this.originalCode;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setCurrency(CurrencyData currency)
    {
        this.currency = currency;
    }


    public CurrencyData getCurrency()
    {
        return this.currency;
    }


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setB2bBudgetData(List<B2BBudgetData> b2bBudgetData)
    {
        this.b2bBudgetData = b2bBudgetData;
    }


    public List<B2BBudgetData> getB2bBudgetData()
    {
        return this.b2bBudgetData;
    }


    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    public boolean isSelected()
    {
        return this.selected;
    }
}
