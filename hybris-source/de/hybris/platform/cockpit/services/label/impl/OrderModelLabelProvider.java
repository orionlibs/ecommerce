package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import org.apache.commons.lang.StringUtils;

public class OrderModelLabelProvider extends AbstractModelLabelProvider<AbstractOrderEntryModel>
{
    protected String getIconPath(AbstractOrderEntryModel item)
    {
        return null;
    }


    protected String getIconPath(AbstractOrderEntryModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(AbstractOrderEntryModel item)
    {
        return "";
    }


    protected String getItemDescription(AbstractOrderEntryModel item, String languageIso)
    {
        return "";
    }


    protected String getItemLabel(AbstractOrderEntryModel orderEntry)
    {
        Integer entryNumber = orderEntry.getEntryNumber();
        String productName = null;
        ProductModel product = orderEntry.getProduct();
        if(product != null)
        {
            productName = product.getName();
        }
        Double totalPrice = orderEntry.getTotalPrice();
        Long qty = orderEntry.getQuantity();
        return "" + entryNumber + " " + entryNumber + (StringUtils.isNotBlank(productName) ? (productName + " ") : "") + " " + totalPrice;
    }


    protected String getItemLabel(AbstractOrderEntryModel item, String languageIso)
    {
        return getItemLabel(item);
    }
}
