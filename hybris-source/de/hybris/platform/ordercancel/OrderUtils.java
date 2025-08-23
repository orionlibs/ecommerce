package de.hybris.platform.ordercancel;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

public class OrderUtils
{
    public static boolean hasLivingEntries(AbstractOrderModel order)
    {
        for(AbstractOrderEntryModel entry : order.getEntries())
        {
            if(entry.getQuantityStatus() == null || OrderEntryStatus.LIVING.equals(entry.getQuantityStatus()))
            {
                return true;
            }
        }
        return false;
    }
}
