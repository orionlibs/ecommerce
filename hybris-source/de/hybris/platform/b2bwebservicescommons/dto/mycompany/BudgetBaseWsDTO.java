package de.hybris.platform.b2bwebservicescommons.dto.mycompany;

import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "BudgetBase", description = "Request body fields required and optional to operate on Budget data. This base bean has no relationship fields to other Org Unit WsDTOs")
public class BudgetBaseWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "active", value = "Boolean flag of whether the Budget is active")
    private Boolean active;
    @ApiModelProperty(name = "budget", value = "Value of Budget", example = "50000")
    private BigDecimal budget;
    @ApiModelProperty(name = "code", value = "Code of the Budget", example = "Monthly_50K_USD")
    private String code;
    @ApiModelProperty(name = "name", value = "The name of the Budget", example = "Monthly 50K USD")
    private String name;
    @ApiModelProperty(name = "currency", value = "Currency of the Budget", example = "USD")
    private CurrencyWsDTO currency;
    @ApiModelProperty(name = "startDate", value = "The start date of the Budget", example = "2020-11-31T09:00:00+0000")
    private Date startDate;
    @ApiModelProperty(name = "endDate", value = "The end date of the Budget", example = "2020-12-31T09:00:00+0000")
    private Date endDate;
    @ApiModelProperty(name = "selected", value = "Boolean flag whether the budget is selected for a cost center", example = "true")
    private Boolean selected;


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }


    public void setBudget(BigDecimal budget)
    {
        this.budget = budget;
    }


    public BigDecimal getBudget()
    {
        return this.budget;
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


    public void setCurrency(CurrencyWsDTO currency)
    {
        this.currency = currency;
    }


    public CurrencyWsDTO getCurrency()
    {
        return this.currency;
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


    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }


    public Boolean getSelected()
    {
        return this.selected;
    }
}
