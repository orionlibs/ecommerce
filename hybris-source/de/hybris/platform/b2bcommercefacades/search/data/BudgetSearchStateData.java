package de.hybris.platform.b2bcommercefacades.search.data;

import de.hybris.platform.commercefacades.search.data.SearchStateData;

public class BudgetSearchStateData extends SearchStateData
{
    private String costCenterCode;


    public void setCostCenterCode(String costCenterCode)
    {
        this.costCenterCode = costCenterCode;
    }


    public String getCostCenterCode()
    {
        return this.costCenterCode;
    }
}
