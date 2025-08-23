package de.hybris.platform.cms2.servicelayer.services.evaluator;

public class CMSRestrictionEvaluatorMapping
{
    private String restrictionTypeCode;
    private CMSRestrictionEvaluator restrictionEvaluator;


    public CMSRestrictionEvaluator getRestrictionEvaluator()
    {
        return this.restrictionEvaluator;
    }


    public void setRestrictionEvaluator(CMSRestrictionEvaluator restrictionEvaluator)
    {
        this.restrictionEvaluator = restrictionEvaluator;
    }


    public String getRestrictionTypeCode()
    {
        return this.restrictionTypeCode;
    }


    public void setRestrictionTypeCode(String restrictionTypeCode)
    {
        this.restrictionTypeCode = restrictionTypeCode;
    }
}
