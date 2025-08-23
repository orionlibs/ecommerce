package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import org.apache.commons.lang.StringUtils;

@Deprecated
public class OrderLabelProvider extends AbstractObjectLabelProvider<AbstractOrderEntry>
{
    protected String getIconPath(AbstractOrderEntry item)
    {
        return null;
    }


    protected String getIconPath(AbstractOrderEntry item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(AbstractOrderEntry item)
    {
        return "";
    }


    protected String getItemDescription(AbstractOrderEntry item, String languageIso)
    {
        return "";
    }


    protected String getItemLabel(AbstractOrderEntry orderEntry)
    {
        int entryNumber = orderEntry.getEntryNumber().intValue();
        String productName = null;
        Product product = orderEntry.getProduct();
        if(product != null)
        {
            productName = product.getName();
        }
        double totalPrice = orderEntry.getTotalPrice().doubleValue();
        long qty = orderEntry.getQuantity().longValue();
        return "" + entryNumber + " " + entryNumber + (StringUtils.isNotBlank(productName) ? (productName + " ") : "") + " " + totalPrice;
    }


    protected String getItemLabel(AbstractOrderEntry item, String languageIso)
    {
        return getItemLabel(item);
    }
}
