package de.hybris.platform.jalo.order.price;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.PriceValue;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class AbstractPriceFactory extends Extension implements PriceFactory
{
    public static AbstractPriceFactory getInstance()
    {
        return JaloSession.getCurrentSession().getOrderManager().getDefaultPriceFactory();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public final ProductPriceInformations getAllPriceInformations(Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return getAllPriceInformations(getSession().getSessionContext(), product, date, net);
    }


    public ProductPriceInformations getAllPriceInformations(SessionContext ctx, Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return new ProductPriceInformations(getProductPriceInformations(ctx, product, date, net),
                        getProductTaxInformations(ctx, product, date),
                        getProductDiscountInformations(ctx, product, date, net));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public final List getProductPriceInformations(Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return getProductPriceInformations(getSession().getSessionContext(), product, date, net);
    }


    public abstract List getProductPriceInformations(SessionContext paramSessionContext, Product paramProduct, Date paramDate, boolean paramBoolean) throws JaloPriceFactoryException;


    @Deprecated(since = "ages", forRemoval = false)
    public final List getProductTaxInformations(Product product, Date date) throws JaloPriceFactoryException
    {
        return getProductTaxInformations(getSession().getSessionContext(), product, date);
    }


    public abstract List getProductTaxInformations(SessionContext paramSessionContext, Product paramProduct, Date paramDate) throws JaloPriceFactoryException;


    @Deprecated(since = "ages", forRemoval = false)
    public final List getProductDiscountInformations(Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return getProductDiscountInformations(getSession().getSessionContext(), product, date, net);
    }


    public abstract List getProductDiscountInformations(SessionContext paramSessionContext, Product paramProduct, Date paramDate, boolean paramBoolean) throws JaloPriceFactoryException;


    public boolean isNetUser(User user)
    {
        return false;
    }


    public Collection getTaxValues(AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        return Collections.EMPTY_LIST;
    }


    public abstract PriceValue getBasePrice(AbstractOrderEntry paramAbstractOrderEntry) throws JaloPriceFactoryException;


    public List getDiscountValues(AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        return Collections.EMPTY_LIST;
    }


    public List getDiscountValues(AbstractOrder order) throws JaloPriceFactoryException
    {
        return Collections.EMPTY_LIST;
    }
}
