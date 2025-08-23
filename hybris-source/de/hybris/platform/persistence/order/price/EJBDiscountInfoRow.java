package de.hybris.platform.persistence.order.price;

import java.util.Map;

public class EJBDiscountInfoRow extends EJBInfoRow
{
    private String discountPK;


    public EJBDiscountInfoRow(Map qualifiers, String discountPK)
    {
        super(qualifiers);
        setDiscountPK(discountPK);
    }


    public String getDiscountPK()
    {
        return this.discountPK;
    }


    public void setDiscountPK(String pk)
    {
        this.discountPK = pk;
    }
}
