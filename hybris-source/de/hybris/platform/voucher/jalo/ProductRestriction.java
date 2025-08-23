package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.variants.jalo.VariantProduct;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class ProductRestriction extends GeneratedProductRestriction
{
    private static final Logger LOG = Logger.getLogger(ProductRestriction.class.getName());


    protected String[] getMessageAttributeValues()
    {
        return new String[] {Localization.getLocalizedString("type.restriction.positive." + isPositiveAsPrimitive()), getProductNames()};
    }


    protected String getProductNames()
    {
        StringBuilder productNames = new StringBuilder();
        for(Iterator<Product> iterator = getProducts().iterator(); iterator.hasNext(); )
        {
            Product product = iterator.next();
            productNames.append(product.getName());
            if(iterator.hasNext())
            {
                productNames.append(", ");
            }
        }
        return productNames.toString();
    }


    public VoucherEntrySet getApplicableEntries(AbstractOrder anOrder)
    {
        VoucherEntrySet entries = new VoucherEntrySet();
        if(getProducts().isEmpty() && !isPositiveAsPrimitive())
        {
            entries.addAll(anOrder.getAllEntries());
        }
        else
        {
            for(Iterator<AbstractOrderEntry> iterator = anOrder.getAllEntries().iterator(); iterator.hasNext(); )
            {
                AbstractOrderEntry entry = iterator.next();
                if(isFulfilledInternal(entry.getProduct()))
                {
                    entries.add(new VoucherEntry(entry, entry.getQuantity().longValue(), entry.getUnit()));
                }
            }
        }
        return entries;
    }


    public Collection getProducts(SessionContext ctx)
    {
        Collection products = super.getProducts(ctx);
        return (products != null) ? products : Collections.emptyList();
    }


    protected boolean isFulfilledInternal(AbstractOrder anOrder)
    {
        return !getApplicableEntries(anOrder).isEmpty();
    }


    protected boolean isFulfilledInternal(Product aProduct)
    {
        return (containsProduct(aProduct) == isPositiveAsPrimitive());
    }


    private boolean containsProduct(Product product)
    {
        Collection products = getProducts();
        boolean result = products.contains(product);
        if(product instanceof VariantProduct)
        {
            result = (result || products.contains(((VariantProduct)product).getBaseProduct()));
        }
        return result;
    }
}
