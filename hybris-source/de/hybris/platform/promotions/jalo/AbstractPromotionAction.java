package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.promotions.result.PromotionException;
import de.hybris.platform.util.DiscountValue;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class AbstractPromotionAction extends GeneratedAbstractPromotionAction
{
    private static Logger log = Logger.getLogger(AbstractPromotionAction.class.getName());


    public String getGuid(SessionContext ctx)
    {
        String retval = super.getGuid(ctx);
        if(retval == null || retval.length() == 0)
        {
            throw new PromotionException("Action with PK:" + getPK() + " has a null or empty GUID");
        }
        return retval;
    }


    public final boolean apply()
    {
        return apply(getSession().getSessionContext());
    }


    public abstract boolean apply(SessionContext paramSessionContext);


    public final boolean undo()
    {
        return undo(getSession().getSessionContext());
    }


    public abstract boolean undo(SessionContext paramSessionContext);


    public final boolean isAppliedToOrder()
    {
        return isAppliedToOrder(getSession().getSessionContext());
    }


    public abstract boolean isAppliedToOrder(SessionContext paramSessionContext);


    public abstract double getValue(SessionContext paramSessionContext);


    protected AbstractPromotionAction deepClone(SessionContext ctx)
    {
        try
        {
            Map values = getAllAttributes(ctx);
            values.remove(Item.PK);
            values.remove(Item.MODIFIED_TIME);
            values.remove(Item.CREATION_TIME);
            values.remove("savedvalues");
            values.remove("customLinkQualifier");
            values.remove("synchronizedCopies");
            values.remove("synchronizationSources");
            values.remove("alldocuments");
            values.remove(Item.TYPE);
            values.remove(Item.OWNER);
            values.remove("promotionResult");
            deepCloneAttributes(ctx, values);
            ComposedType type = getComposedType();
            try
            {
                return (AbstractPromotionAction)type.newInstance(ctx, values);
            }
            catch(JaloGenericCreationException | de.hybris.platform.jalo.type.JaloAbstractTypeException ex)
            {
                log.warn("deepClone [" + this + "] failed to create instance of " + getClass().getSimpleName(), (Throwable)ex);
            }
        }
        catch(JaloSecurityException ex)
        {
            log.warn("deepClone [" + this + "] failed to getAllAttributes", (Throwable)ex);
        }
        return null;
    }


    protected void deepCloneAttributes(SessionContext ctx, Map values)
    {
    }


    protected static void insertFirstGlobalDiscountValue(SessionContext ctx, AbstractOrder order, DiscountValue discountValue)
    {
        List<DiscountValue> list = order.getGlobalDiscountValues(ctx);
        order.removeAllGlobalDiscountValues(ctx);
        order.addGlobalDiscountValue(ctx, discountValue);
        if(list != null)
        {
            order.addAllGlobalDiscountValues(ctx, list);
        }
    }


    protected static void insertFirstOrderEntryDiscountValue(SessionContext ctx, AbstractOrderEntry orderEntry, DiscountValue discountValue)
    {
        List<DiscountValue> list = orderEntry.getDiscountValues(ctx);
        orderEntry.removeAllDiscountValues(ctx);
        orderEntry.addDiscountValue(ctx, discountValue);
        if(list != null)
        {
            orderEntry.addAllDiscountValues(ctx, list);
        }
    }
}
