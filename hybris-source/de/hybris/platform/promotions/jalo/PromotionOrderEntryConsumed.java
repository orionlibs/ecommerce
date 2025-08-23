package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import org.apache.log4j.Logger;

public class PromotionOrderEntryConsumed extends GeneratedPromotionOrderEntryConsumed
{
    private static final Logger LOGGER = Logger.getLogger(PromotionOrderEntryConsumed.class.getName());
    private transient boolean removedFromOrder = true;


    public final double getEntryPrice()
    {
        return getEntryPrice(JaloSession.getCurrentSession().getSessionContext());
    }


    public double getEntryPrice(SessionContext ctx)
    {
        return getQuantity(ctx).doubleValue() * getUnitPrice(ctx);
    }


    public final double getUnitPrice()
    {
        return getUnitPrice(JaloSession.getCurrentSession().getSessionContext());
    }


    public double getUnitPrice(SessionContext ctx)
    {
        return getOrderEntry(ctx).getBasePrice(ctx).doubleValue();
    }


    public boolean isRemovedFromOrder()
    {
        return this.removedFromOrder;
    }


    public void setRemovedFromOrder(boolean removedFromOrder)
    {
        this.removedFromOrder = removedFromOrder;
    }


    public final Product getProduct()
    {
        return getProduct(JaloSession.getCurrentSession().getSessionContext());
    }


    public Product getProduct(SessionContext ctx)
    {
        return getOrderEntry(ctx).getProduct(ctx);
    }


    public final Unit getUnit()
    {
        return getUnit(JaloSession.getCurrentSession().getSessionContext());
    }


    public Unit getUnit(SessionContext ctx)
    {
        return getOrderEntry(ctx).getUnit(ctx);
    }


    public final double getAdjustedEntryPrice()
    {
        return getAdjustedEntryPrice(JaloSession.getCurrentSession().getSessionContext());
    }


    public double getAdjustedEntryPrice(SessionContext ctx)
    {
        return getQuantity(ctx).longValue() * getAdjustedUnitPrice(ctx).doubleValue();
    }
}
