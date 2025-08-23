package de.hybris.platform.ruleengineservices.rao;

import java.math.BigDecimal;

public class CartRAO extends AbstractOrderRAO
{
    private BigDecimal originalTotal;


    public void setOriginalTotal(BigDecimal originalTotal)
    {
        this.originalTotal = originalTotal;
    }


    public BigDecimal getOriginalTotal()
    {
        return this.originalTotal;
    }
}
