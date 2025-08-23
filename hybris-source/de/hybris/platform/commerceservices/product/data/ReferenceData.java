package de.hybris.platform.commerceservices.product.data;

import java.io.Serializable;

public class ReferenceData<TYPE, TARGET> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private TYPE referenceType;
    private String description;
    private Integer quantity;
    private TARGET target;


    public void setReferenceType(TYPE referenceType)
    {
        this.referenceType = referenceType;
    }


    public TYPE getReferenceType()
    {
        return this.referenceType;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }


    public Integer getQuantity()
    {
        return this.quantity;
    }


    public void setTarget(TARGET target)
    {
        this.target = target;
    }


    public TARGET getTarget()
    {
        return this.target;
    }
}
