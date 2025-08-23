package de.hybris.platform.commercefacades.promotion.data;

import java.io.Serializable;

public class PromotionRestrictionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String restrictionType;
    private String description;


    public void setRestrictionType(String restrictionType)
    {
        this.restrictionType = restrictionType;
    }


    public String getRestrictionType()
    {
        return this.restrictionType;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }
}
