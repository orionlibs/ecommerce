package de.hybris.platform.persistence.order.price;

import java.util.Map;

public class EJBTaxInfoRow extends EJBInfoRow
{
    private String taxPK;
    private double value;


    public EJBTaxInfoRow(Map qualifiers, String taxPK, double value)
    {
        super(qualifiers);
        setTaxPK(taxPK);
        setValue(value);
    }


    public String getTaxPK()
    {
        return this.taxPK;
    }


    public void setTaxPK(String taxPK)
    {
        this.taxPK = taxPK;
    }


    public double getValue()
    {
        return this.value;
    }


    public void setValue(double value)
    {
        this.value = value;
    }
}
