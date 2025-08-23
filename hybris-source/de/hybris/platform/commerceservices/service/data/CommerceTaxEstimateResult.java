package de.hybris.platform.commerceservices.service.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class CommerceTaxEstimateResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    private BigDecimal tax;


    public void setTax(BigDecimal tax)
    {
        this.tax = tax;
    }


    public BigDecimal getTax()
    {
        return this.tax;
    }
}
