package de.hybris.platform.servicelayer.internal.jalo.order;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloOnlyItem;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryCartEntry extends GeneratedInMemoryCartEntry implements JaloOnlyItem
{
    private JaloOnlyItemHelper data;


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("product", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("quantity", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("unit", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("order", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing parameters to create a AbstractOrderEntry ( missing " + missing + ")", 0);
        }
        if(!(allAttributes.get("order") instanceof AbstractOrder))
        {
            throw new JaloInvalidParameterException("Parameter order should be instance of AbstractOrder", 0);
        }
        Class<InMemoryCartEntry> cl = type.getJaloClass();
        try
        {
            InMemoryCartEntry newOne = cl.newInstance();
            newOne.setTenant(type.getTenant());
            newOne
                            .data = new JaloOnlyItemHelper((PK)allAttributes.get(PK), (Item)newOne, type, new Date(), null);
            newOne.data.setProperty(ctx, "order", allAttributes.get("order"));
            newOne.data.setProperty(ctx, "entryNumber", allAttributes.get("entryNumber"));
            return (Item)newOne;
        }
        catch(ClassCastException e)
        {
            throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type
                            .getCode() + " : " + e, 0);
        }
        catch(InstantiationException e)
        {
            throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type
                            .getCode() + " : " + e, 0);
        }
        catch(IllegalAccessException e)
        {
            throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type
                            .getCode() + " : " + e, 0);
        }
    }


    @ForceJALO(reason = "something else")
    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap ret = new Item.ItemAttributeMap((Map)allAttributes);
        ret.remove(Item.PK);
        ret.remove(Item.TYPE);
        ret.remove("itemtype");
        ret.remove("order");
        ret.remove("entryNumber");
        return ret;
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


    public Object doGetAttribute(SessionContext ctx, String attrQualifier) throws JaloInvalidParameterException, JaloSecurityException
    {
        return this.data.doGetAttribute(ctx, attrQualifier);
    }


    public void doSetAttribute(SessionContext ctx, String attrQualifier, Object value) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        this.data.doSetAttribute(ctx, attrQualifier, value);
    }


    @ForceJALO(reason = "something else")
    public AbstractOrder getOrder(SessionContext ctx)
    {
        return (AbstractOrder)this.data.getProperty(ctx, "order");
    }


    @ForceJALO(reason = "something else")
    public void setOrder(SessionContext ctx, AbstractOrder order)
    {
        this.data.setProperty(ctx, "order", order);
    }


    @ForceJALO(reason = "something else")
    public Product getProduct(SessionContext ctx)
    {
        return (Product)this.data.getProperty(ctx, "product");
    }


    @ForceJALO(reason = "something else")
    public void setProduct(SessionContext ctx, Product p)
    {
        this.data.setProperty(ctx, "product", p);
        setChanged();
    }


    @ForceJALO(reason = "something else")
    public Long getQuantity(SessionContext ctx)
    {
        return Long.valueOf(this.data.getPropertyLong(ctx, "quantity", 0L));
    }


    @ForceJALO(reason = "something else")
    public void setQuantity(SessionContext ctx, long qtd)
    {
        this.data.setProperty(ctx, "quantity", Long.valueOf(qtd));
        setChanged();
    }


    @ForceJALO(reason = "something else")
    public Unit getUnit(SessionContext ctx)
    {
        return (Unit)this.data.getProperty(ctx, "unit");
    }


    @ForceJALO(reason = "something else")
    public void setUnit(SessionContext ctx, Unit unit)
    {
        this.data.setProperty(ctx, "unit", unit);
        setChanged();
    }


    @ForceJALO(reason = "something else")
    public String getInfo(SessionContext ctx)
    {
        return (String)this.data.getProperty(ctx, "info");
    }


    @ForceJALO(reason = "something else")
    public void setInfo(SessionContext ctx, String info)
    {
        this.data.setProperty(ctx, "info", info);
    }


    @ForceJALO(reason = "something else")
    public Double getBasePrice(SessionContext ctx)
    {
        return Double.valueOf(this.data.getPropertyDouble(ctx, "basePrice", 0.0D));
    }


    @ForceJALO(reason = "something else")
    public void setBasePrice(SessionContext ctx, double price)
    {
        this.data.setProperty(ctx, "basePrice", Double.valueOf(price));
        setChanged();
    }


    @ForceJALO(reason = "something else")
    public Double getTotalPrice(SessionContext ctx)
    {
        return Double.valueOf(this.data.getPropertyDouble(ctx, "totalPrice", 0.0D));
    }


    @ForceJALO(reason = "something else")
    public void setTotalPrice(SessionContext ctx, double price)
    {
        this.data.setProperty(ctx, "totalPrice", Double.valueOf(price));
    }


    @ForceJALO(reason = "something else")
    public String getTaxValuesInternal(SessionContext ctx)
    {
        return (String)this.data.getProperty(ctx, "taxValuesInternal");
    }


    @ForceJALO(reason = "something else")
    public void setTaxValuesInternal(SessionContext ctx, String value)
    {
        this.data.setProperty(ctx, "taxValuesInternal", value);
    }


    @ForceJALO(reason = "something else")
    public String getDiscountValuesInternal(SessionContext ctx)
    {
        return (String)this.data.getProperty(ctx, "discountValuesInternal");
    }


    @ForceJALO(reason = "something else")
    public void setDiscountValuesInternal(SessionContext ctx, String value)
    {
        this.data.setProperty(ctx, "discountValuesInternal", value);
    }


    @ForceJALO(reason = "something else")
    public Boolean isCalculated(SessionContext ctx)
    {
        return Boolean.valueOf(this.data.getPropertyBoolean(ctx, "calculated", false));
    }


    @ForceJALO(reason = "something else")
    public void setCalculated(SessionContext ctx, boolean calculated)
    {
        this.data.setProperty(ctx, "calculated", Boolean.valueOf(calculated));
    }


    @ForceJALO(reason = "something else")
    public Boolean isGiveAway(SessionContext ctx)
    {
        return Boolean.valueOf(this.data.getPropertyBoolean(ctx, "giveAway", false));
    }


    @ForceJALO(reason = "something else")
    public void setGiveAway(SessionContext ctx, boolean giveaway)
    {
        this.data.setProperty(ctx, "giveAway", Boolean.valueOf(giveaway));
        setChanged();
    }


    @ForceJALO(reason = "something else")
    public Boolean isRejected(SessionContext ctx)
    {
        return Boolean.valueOf(this.data.getPropertyBoolean(ctx, "rejected", false));
    }


    @ForceJALO(reason = "something else")
    public void setRejected(SessionContext ctx, boolean rejected)
    {
        this.data.setProperty(ctx, "rejected", Boolean.valueOf(rejected));
        setChanged();
    }


    @ForceJALO(reason = "something else")
    public Integer getEntryNumber()
    {
        return Integer.valueOf(this.data.getPropertyInt(null, "entryNumber", -1));
    }


    @ForceJALO(reason = "something else")
    protected void setEntryNumberDirect(int nr)
    {
        this.data.setProperty(null, "entryNumber", Integer.valueOf(nr));
    }


    public void setModificationTime(Date d)
    {
        this.data.markModified(d);
    }
}
