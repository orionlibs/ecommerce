package de.hybris.platform.promotions.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloOnlyItem;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.internal.jalo.order.JaloOnlyItemHelper;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class CachedPromotionOrderEntryAdjustAction extends GeneratedCachedPromotionOrderEntryAdjustAction implements JaloOnlyItem
{
    private static final Logger LOG = Logger.getLogger(CachedPromotionOrderEntryAdjustAction.class.getName());
    private JaloOnlyItemHelper data;


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("markedApplied", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + " to create a cart ", 0);
        }
        Class<CachedPromotionOrderEntryAdjustAction> cl = type.getJaloClass();
        try
        {
            CachedPromotionOrderEntryAdjustAction newOne = cl.newInstance();
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


    public Boolean isMarkedApplied(SessionContext ctx)
    {
        return (Boolean)this.data.getProperty(ctx, "markedApplied");
    }


    public void setMarkedApplied(SessionContext ctx, Boolean markedApplied)
    {
        this.data.setProperty(ctx, "markedApplied", markedApplied);
    }


    public String getGuid(SessionContext ctx)
    {
        return (String)this.data.getProperty(ctx, "guid");
    }


    public void setGuid(SessionContext ctx, String guid)
    {
        this.data.setProperty(ctx, "guid", guid);
    }


    public PromotionResult getPromotionResult(SessionContext ctx)
    {
        return (PromotionResult)this.data.getProperty(ctx, "promotionResult");
    }


    public void setPromotionResult(SessionContext ctx, PromotionResult promotionResult)
    {
        this.data.setProperty(ctx, "promotionResult", promotionResult);
    }


    public Double getAmount(SessionContext ctx)
    {
        return (Double)this.data.getProperty(ctx, "amount");
    }


    public void setAmount(SessionContext ctx, Double amount)
    {
        this.data.setProperty(ctx, "amount", amount);
    }


    public Integer getOrderEntryNumber(SessionContext ctx)
    {
        return (Integer)this.data.getProperty(ctx, "orderEntryNumber");
    }


    public void setOrderEntryNumber(SessionContext ctx, Integer orderEntryNumber)
    {
        this.data.setProperty(ctx, "orderEntryNumber", orderEntryNumber);
    }


    public Product getOrderEntryProduct(SessionContext ctx)
    {
        return (Product)this.data.getProperty(ctx, "orderEntryProduct");
    }


    public void setOrderEntryProduct(SessionContext ctx, Product orderEntryProduct)
    {
        this.data.setProperty(ctx, "orderEntryProduct", orderEntryProduct);
    }


    public Long getOrderEntryQuantity(SessionContext ctx)
    {
        return (Long)this.data.getProperty(ctx, "orderEntryQuantity");
    }


    public void setOrderEntryQuantity(SessionContext ctx, Long orderEntryQuantity)
    {
        this.data.setProperty(ctx, "orderEntryQuantity", orderEntryQuantity);
    }


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
            ComposedType type = TypeManager.getInstance().getComposedType(PromotionOrderEntryAdjustAction.class);
            try
            {
                return (AbstractPromotionAction)type.newInstance(ctx, values);
            }
            catch(JaloGenericCreationException | de.hybris.platform.jalo.type.JaloAbstractTypeException ex)
            {
                LOG.warn("deepClone [" + this + "] failed to create instance of " + getClass().getSimpleName(), (Throwable)ex);
            }
        }
        catch(JaloSecurityException ex)
        {
            LOG.warn("deepClone [" + this + "] failed to getAllAttributes", (Throwable)ex);
        }
        return null;
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
