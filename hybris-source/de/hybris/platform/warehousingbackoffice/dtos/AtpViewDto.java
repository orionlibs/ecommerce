package de.hybris.platform.warehousingbackoffice.dtos;

import de.hybris.platform.warehousing.model.AtpFormulaModel;

public class AtpViewDto
{
    private AtpFormulaModel atpFormula;
    private Long atp;
    private boolean isActive;


    public AtpViewDto(AtpFormulaModel atpFormula, Long atp, Boolean isActive)
    {
        this.atpFormula = atpFormula;
        this.atp = atp;
        this.isActive = isActive.booleanValue();
    }


    public AtpFormulaModel getAtpFormula()
    {
        return this.atpFormula;
    }


    public void setAtpFormula(AtpFormulaModel atpFormula)
    {
        this.atpFormula = atpFormula;
    }


    public Long getAtp()
    {
        return this.atp;
    }


    public void setAtp(Long atp)
    {
        this.atp = atp;
    }


    public Boolean getIsActive()
    {
        return Boolean.valueOf(this.isActive);
    }


    public void setIsActive(Boolean active)
    {
        this.isActive = active.booleanValue();
    }
}
