package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class ProductQuantityRestriction extends GeneratedProductQuantityRestriction
{
    private static final Logger LOG = Logger.getLogger(ProductQuantityRestriction.class.getName());


    protected String[] getMessageAttributeValues()
    {
        return new String[] {Localization.getLocalizedString("type.restriction.positive." + (isPositiveAsPrimitive() ? "upto" : "from")),
                        getQuantity().toString() + " " + getQuantity().toString(), getProductNames()};
    }


    public VoucherEntrySet getApplicableEntries(AbstractOrder anOrder)
    {
        VoucherEntrySet entries = new VoucherEntrySet();
        Collection restrictedProducts = getProducts();
        boolean isPositive = isPositiveAsPrimitive();
        if(restrictedProducts.isEmpty() && !isPositive)
        {
            entries.addAll(anOrder.getAllEntries());
        }
        else
        {
            for(Iterator<AbstractOrderEntry> iterator = anOrder.getAllEntries().iterator(); iterator.hasNext(); )
            {
                AbstractOrderEntry entry = iterator.next();
                if(restrictedProducts.contains(entry.getProduct()))
                {
                    if(isPositive)
                    {
                        entries.add(new VoucherEntry(entry, getQuantityAsPrimitive(), getUnit()));
                        continue;
                    }
                    long quantity = entry.getQuantity().longValue() - getQuantityAsPrimitive();
                    if(quantity > 0L)
                    {
                        entries.add(new VoucherEntry(entry, quantity, getUnit()));
                    }
                }
            }
        }
        return entries;
    }
}
