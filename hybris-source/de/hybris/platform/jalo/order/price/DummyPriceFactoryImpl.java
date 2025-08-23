package de.hybris.platform.jalo.order.price;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.PriceValue;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DummyPriceFactoryImpl implements PriceFactory
{
    public ProductPriceInformations getAllPriceInformations(SessionContext ctx, Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return null;
    }


    public PriceValue getBasePrice(AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        throw new JaloPriceFactoryException("no price defined! you have to implement getBasePrice( AbstractOrderEntry entry )", -786345);
    }


    public List getDiscountValues(AbstractOrder order) throws JaloPriceFactoryException
    {
        return Collections.EMPTY_LIST;
    }


    public List getDiscountValues(AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        return Collections.EMPTY_LIST;
    }


    public List getProductDiscountInformations(SessionContext ctx, Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return Collections.EMPTY_LIST;
    }


    public List getProductPriceInformations(SessionContext ctx, Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return Collections.EMPTY_LIST;
    }


    public List getProductTaxInformations(SessionContext ctx, Product product, Date date) throws JaloPriceFactoryException
    {
        return Collections.EMPTY_LIST;
    }


    public Collection getTaxValues(AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        return null;
    }


    public boolean isNetUser(User user)
    {
        return false;
    }
}
