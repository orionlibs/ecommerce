package de.hybris.platform.voucher.jalo.util;

import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Unit;

public class VoucherEntry
{
    private final AbstractOrderEntry theOrderEntry;
    private long theQuantity;
    private final Unit theUnit;


    public VoucherEntry(AbstractOrderEntry anOrderEntry, long quantity, Unit aUnit)
    {
        this.theOrderEntry = anOrderEntry;
        this.theQuantity = quantity;
        this.theUnit = aUnit;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        VoucherEntry entry = (VoucherEntry)o;
        return (getOrderEntry().equals(entry.getOrderEntry()) && ((
                        getQuantity() == entry.getQuantity() && getUnit().equals(entry.getUnit())) || getQuantity() == entry
                        .getUnit().convert(getUnit(), entry.getQuantity())));
    }


    public AbstractOrderEntry getOrderEntry()
    {
        return this.theOrderEntry;
    }


    public long getQuantity()
    {
        return this.theQuantity;
    }


    public Unit getUnit()
    {
        return this.theUnit;
    }


    public int hashCode()
    {
        return getOrderEntry().hashCode();
    }


    public void setQuantity(long quantity)
    {
        this.theQuantity = quantity;
    }
}
