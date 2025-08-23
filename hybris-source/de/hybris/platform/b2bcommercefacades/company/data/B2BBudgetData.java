package de.hybris.platform.b2bcommercefacades.company.data;

import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class B2BBudgetData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private BigDecimal budget;
    private B2BUnitData unit;
    private String code;
    private String name;
    private CurrencyData currency;
    private boolean active;
    private List<B2BCostCenterData> costCenters;
    private List<String> costCenterNames;
    private Date startDate;
    private Date endDate;
    private boolean selected;
    private String originalCode;


    public void setBudget(BigDecimal budget)
    {
        this.budget = budget;
    }


    public BigDecimal getBudget()
    {
        return this.budget;
    }


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


    public void setCostCenters(List<B2BCostCenterData> costCenters)
    {
        this.costCenters = costCenters;
    }


    public List<B2BCostCenterData> getCostCenters()
    {
        return this.costCenters;
    }


    public void setCostCenterNames(List<String> costCenterNames)
    {
        this.costCenterNames = costCenterNames;
    }


    public List<String> getCostCenterNames()
    {
        return this.costCenterNames;
    }


    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    public Date getStartDate()
    {
        return this.startDate;
    }


    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    public Date getEndDate()
    {
        return this.endDate;
    }


    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    public boolean isSelected()
    {
        return this.selected;
    }


    public void setOriginalCode(String originalCode)
    {
        this.originalCode = originalCode;
    }


    public String getOriginalCode()
    {
        return this.originalCode;
    }
}
