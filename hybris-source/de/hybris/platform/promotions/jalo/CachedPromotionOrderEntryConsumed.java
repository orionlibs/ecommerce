package de.hybris.platform.promotions.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloOnlyItem;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.internal.jalo.order.JaloOnlyItemHelper;
import java.util.Date;

public class CachedPromotionOrderEntryConsumed extends GeneratedCachedPromotionOrderEntryConsumed implements JaloOnlyItem
{
    private JaloOnlyItemHelper data;


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Class<CachedPromotionOrderEntryConsumed> cl = type.getJaloClass();
        try
        {
            CachedPromotionOrderEntryConsumed newOne = cl.newInstance();
            newOne.setTenant(type.getTenant());
            newOne
                            .data = new JaloOnlyItemHelper((PK)allAttributes.get(PK), (Item)newOne, type, new Date(), null);
            return (Item)newOne;
        }
        catch(ClassCastException | InstantiationException | IllegalAccessException e)
        {
            throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type
                            .getCode() + " : " + e, 0);
        }
    }


    public Double getAdjustedUnitPrice(SessionContext ctx)
    {
        return (Double)this.data.getProperty(ctx, "adjustedUnitPrice");
    }


    public void setAdjustedUnitPrice(SessionContext ctx, Double adjustedUnitPrice)
    {
        this.data.setProperty(ctx, "adjustedUnitPrice", adjustedUnitPrice);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)this.data.getProperty(ctx, "code");
    }


    public void setCode(SessionContext ctx, String code)
    {
        this.data.setProperty(ctx, "code", code);
    }


    public AbstractOrderEntry getOrderEntry(SessionContext ctx)
    {
        return (AbstractOrderEntry)this.data.getProperty(ctx, "orderEntry");
    }


    public void setOrderEntry(SessionContext ctx, AbstractOrderEntry entry)
    {
        this.data.setProperty(ctx, "orderEntry", entry);
    }


    public void setPromotionResult(SessionContext ctx, PromotionResult result)
    {
        this.data.setProperty(ctx, "promotionResult", result);
    }


    public PromotionResult getPromotionResult(SessionContext ctx)
    {
        return (PromotionResult)this.data.getProperty(ctx, "promotionResult");
    }


    public void setQuantity(SessionContext ctx, Long quantity)
    {
        this.data.setProperty(ctx, "quantity", quantity);
    }


    public Long getQuantity(SessionContext ctx)
    {
        return (Long)this.data.getProperty(ctx, "quantity");
    }


    public final ComposedType provideComposedType()
    {
        return this.data.provideComposedType();
    }


    public final Date provideCreationTime()
    {
        return this.data.provideCreationTime();
    }


    public final Date provideModificationTime()
    {
        return this.data.provideModificationTime();
    }


    public final PK providePK()
    {
        return this.data.providePK();
    }


    public void removeJaloOnly() throws ConsistencyCheckException
    {
        this.data.removeJaloOnly();
    }


    public Object doGetAttribute(SessionContext ctx, String attrQualifier) throws JaloSecurityException
    {
        return this.data.doGetAttribute(ctx, attrQualifier);
    }


    public void doSetAttribute(SessionContext ctx, String attrQualifier, Object value) throws JaloBusinessException
    {
        this.data.doSetAttribute(ctx, attrQualifier, value);
    }
}
