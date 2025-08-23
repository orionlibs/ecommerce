package de.hybris.platform.jalo.order;

import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.JaloTools;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class AbstractOrderEntry extends GeneratedAbstractOrderEntry
{
    public static final String DISCOUNTVALUES = "discountValues";
    public static final String TAXVALUES = "taxValues";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String ENTRY_NUMBER = "entryNumber";


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
        allAttributes.setAttributeMode("product", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("quantity", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("unit", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("order", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("entryNumber", Item.AttributeMode.INITIAL);
        AbstractOrder order = (AbstractOrder)allAttributes.get("order");
        Integer pos = (Integer)allAttributes.get("entryNumber");
        allAttributes.put("entryNumber",
                        Integer.valueOf(order
                                        .createNewEntryNumber((pos != null) ? pos.intValue() : -1)));
        AbstractOrderEntry entry = (AbstractOrderEntry)super.createItem(ctx, (type == null) ? order.getCustomEntryType() :
                        type, allAttributes);
        order.setChanged(false);
        entry.setInfo(order.createEntryInformation(entry));
        return (Item)entry;
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        AbstractOrder order = getOrder(ctx);
        if(order != null)
        {
            order.removeEntry(this);
        }
        else
        {
            removeWithoutOrderNotification(ctx);
        }
    }


    protected void removeWithoutOrderNotification(SessionContext ctx) throws ConsistencyCheckException
    {
        super.remove(ctx);
    }


    @ForceJALO(reason = "something else")
    public AbstractOrder getOrder(SessionContext ctx)
    {
        AbstractOrder order = super.getOrder(ctx);
        if(order == null)
        {
            throw new IllegalStateException("no order for AbstractOrderEntry.orderPK " + this);
        }
        return order;
    }


    @ForceJALO(reason = "consistency check")
    public void setOrder(SessionContext ctx, AbstractOrder value)
    {
        if(value == null)
        {
            throw new IllegalArgumentException("entry.order cannot be null");
        }
        super.setOrder(ctx, value);
    }


    @ForceJALO(reason = "something else")
    public void setProduct(SessionContext ctx, Product product)
    {
        synchronized(getOrder().getSyncObject())
        {
            super.setProduct(ctx, product);
            setInfo(getOrder().createEntryInformation(this));
            setChanged();
        }
    }


    @ForceJALO(reason = "something else")
    public void setQuantity(SessionContext ctx, Long qtd)
    {
        synchronized(getOrder().getSyncObject())
        {
            super.setQuantity(ctx, qtd);
            setChanged();
        }
    }


    @ForceJALO(reason = "something else")
    public void setUnit(SessionContext ctx, Unit unit)
    {
        synchronized(getOrder().getSyncObject())
        {
            super.setUnit(ctx, unit);
            setChanged();
        }
    }


    @ForceJALO(reason = "something else")
    public void setBasePrice(SessionContext ctx, Double price)
    {
        synchronized(getOrder().getSyncObject())
        {
            super.setBasePrice(ctx, price);
            setChanged();
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getTaxValues()
    {
        return getTaxValues(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getTaxValues(SessionContext ctx)
    {
        Collection taxValues = convertAndGetTaxValues(ctx);
        return (taxValues != null) ? taxValues : Collections.EMPTY_LIST;
    }


    private Collection convertAndGetTaxValues(SessionContext ctx)
    {
        String taxValues = getTaxValuesInternal(ctx);
        return TaxValue.parseTaxValueCollection(taxValues);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setTaxValues(Collection collection)
    {
        setTaxValues(getSession().getSessionContext(), collection);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setTaxValues(SessionContext ctx, Collection collection)
    {
        removeAllTaxValues(ctx);
        addAllTaxValues(ctx, collection);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addTaxValue(TaxValue taxValue)
    {
        addTaxValue(getSession().getSessionContext(), taxValue);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addTaxValue(SessionContext ctx, TaxValue taxValue)
    {
        Collection<TaxValue> l = convertAndGetTaxValues(ctx);
        if(l == null)
        {
            l = new LinkedList();
        }
        l.add(taxValue);
        convertAndSetTaxValues(ctx, l);
        setChanged();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addAllTaxValues(Collection values)
    {
        addAllTaxValues(getSession().getSessionContext(), values);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addAllTaxValues(SessionContext ctx, Collection values)
    {
        synchronized(getOrder().getSyncObject())
        {
            if(values == null)
            {
                return;
            }
            Collection l = convertAndGetTaxValues(ctx);
            if(l == null)
            {
                l = new LinkedList();
            }
            l.addAll(values);
            convertAndSetTaxValues(ctx, l);
            setChanged();
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeTaxValue(TaxValue taxValue)
    {
        removeTaxValue(getSession().getSessionContext(), taxValue);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeTaxValue(SessionContext ctx, TaxValue taxValue)
    {
        synchronized(getOrder().getSyncObject())
        {
            Collection l = convertAndGetTaxValues(ctx);
            if(l != null)
            {
                l.remove(taxValue);
            }
            convertAndSetTaxValues(ctx, l);
            setChanged();
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeAllTaxValues()
    {
        removeAllTaxValues(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeAllTaxValues(SessionContext ctx)
    {
        synchronized(getOrder().getSyncObject())
        {
            convertAndSetTaxValues(ctx, Collections.EMPTY_LIST);
            setChanged();
        }
    }


    private void convertAndSetTaxValues(SessionContext ctx, Collection taxValues)
    {
        String newValue = TaxValue.toString(taxValues);
        setTaxValuesInternal(ctx, newValue);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getDiscountValues()
    {
        return getDiscountValues(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getDiscountValues(SessionContext ctx)
    {
        List l = convertAndGetDiscountValues(ctx);
        return (l != null) ? l : Collections.EMPTY_LIST;
    }


    private List convertAndGetDiscountValues(SessionContext ctx)
    {
        String discountValues = getDiscountValuesInternal(ctx);
        return (List)DiscountValue.parseDiscountValueCollection(discountValues);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDiscountValues(SessionContext ctx, List discountValues)
    {
        removeAllDiscountValues(ctx);
        addAllDiscountValues(ctx, discountValues);
    }


    public void setDiscountValues(List discountValues)
    {
        setDiscountValues(getSession().getSessionContext(), discountValues);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addDiscountValue(DiscountValue discountValue)
    {
        addDiscountValue(getSession().getSessionContext(), discountValue);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addDiscountValue(SessionContext ctx, DiscountValue discountValue)
    {
        List<DiscountValue> l = convertAndGetDiscountValues(ctx);
        if(l == null)
        {
            l = new LinkedList();
        }
        l.add(discountValue);
        convertAndSetDiscountValues(ctx, l);
        setChanged();
    }


    private void convertAndSetDiscountValues(SessionContext ctx, List values)
    {
        String newValue = DiscountValue.toString(values);
        setDiscountValuesInternal(ctx, newValue);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addAllDisocuntValues(List values)
    {
        addAllDiscountValues(values);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addAllDiscountValues(List values)
    {
        addAllDiscountValues(getSession().getSessionContext(), values);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addAllDiscountValues(SessionContext ctx, List values)
    {
        synchronized(getOrder().getSyncObject())
        {
            List l = convertAndGetDiscountValues(ctx);
            if(l == null)
            {
                l = new LinkedList();
            }
            l.addAll(values);
            convertAndSetDiscountValues(ctx, l);
            setChanged();
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeDiscountValue(DiscountValue discountValue)
    {
        removeDiscountValue(getSession().getSessionContext(), discountValue);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeDiscountValue(SessionContext ctx, DiscountValue discountValue)
    {
        synchronized(getOrder().getSyncObject())
        {
            List l = convertAndGetDiscountValues(ctx);
            if(l != null)
            {
                l.remove(discountValue);
            }
            convertAndSetDiscountValues(ctx, l);
            setChanged();
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeAllDiscountValues()
    {
        removeAllDiscountValues(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeAllDiscountValues(SessionContext ctx)
    {
        synchronized(getOrder().getSyncObject())
        {
            convertAndSetDiscountValues(ctx, Collections.EMPTY_LIST);
            setChanged();
        }
    }


    @ForceJALO(reason = "something else")
    public Boolean isCalculated(SessionContext ctx)
    {
        Boolean result = super.isCalculated(ctx);
        return (result != null) ? result : Boolean.FALSE;
    }


    @ForceJALO(reason = "something else")
    public Boolean isGiveAway(SessionContext ctx)
    {
        Boolean result = super.isGiveAway(ctx);
        return (result != null) ? result : Boolean.FALSE;
    }


    @ForceJALO(reason = "something else")
    public void setGiveAway(SessionContext ctx, Boolean giveaway)
    {
        super.setGiveAway(ctx, giveaway);
        setChanged();
    }


    @ForceJALO(reason = "something else")
    public Boolean isRejected(SessionContext ctx)
    {
        Boolean result = super.isRejected(ctx);
        return (result != null) ? result : Boolean.FALSE;
    }


    @ForceJALO(reason = "something else")
    public void setRejected(SessionContext ctx, Boolean rejected)
    {
        super.setRejected(ctx, rejected);
        setChanged();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void calculate() throws JaloPriceFactoryException
    {
        if(!isCalculated().booleanValue())
        {
            recalculate();
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void recalculate() throws JaloPriceFactoryException
    {
        resetAllValues();
        calculateTotals(true);
    }


    @ForceJALO(reason = "something else")
    public void setEntryNumber(SessionContext ctx, Integer number)
    {
        super.setEntryNumber(ctx, (
                        number == null || -1 >= number.intValue()) ? Integer.valueOf(getOrder()
                        .getNextEntryNumber(this)) :
                        number);
    }


    protected void setEntryNumberDirect(int number)
    {
        setEntryNumber(number);
    }


    protected void resetAllValues() throws JaloPriceFactoryException
    {
        Collection taxes = findTaxes();
        setTaxValues(taxes);
        AbstractOrder order = getOrder();
        setBasePrice(JaloTools.convertPriceIfNecessary(findPrice(), order.isNet().booleanValue(), order.getCurrency(), taxes)
                        .getValue());
        setDiscountValues(findDiscounts());
        setCalculated(false);
    }


    protected PriceValue findPrice() throws JaloPriceFactoryException
    {
        return getSession().getOrderManager().getPriceFactory().getBasePrice(this);
    }


    protected Collection findTaxes() throws JaloPriceFactoryException
    {
        return getSession().getOrderManager().getPriceFactory().getTaxValues(this);
    }


    protected List findDiscounts() throws JaloPriceFactoryException
    {
        return getSession().getOrderManager().getPriceFactory().getDiscountValues(this);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void calculateTotals(boolean recalculate)
    {
        if(recalculate || !isCalculated().booleanValue())
        {
            AbstractOrder order = getOrder();
            Currency curr = order.getCurrency();
            int digits = curr.getDigits().intValue();
            double totalPriceWithoutDiscount = CoreAlgorithms.round(
                            getBasePrice().doubleValue() * getQuantity().longValue(), digits);
            double quantity = getQuantity().longValue();
            List appliedDiscounts = DiscountValue.apply(quantity, totalPriceWithoutDiscount, digits, getOrder()
                            .convertDiscountValues(getDiscountValues()), curr.getIsoCode());
            setDiscountValues(appliedDiscounts);
            double totalPrice = totalPriceWithoutDiscount;
            for(Iterator<DiscountValue> it = appliedDiscounts.iterator(); it.hasNext(); )
            {
                totalPrice -= ((DiscountValue)it.next()).getAppliedValue();
            }
            setTotalPrice(totalPrice);
            setTaxValues(TaxValue.apply(quantity, totalPrice, digits, getTaxValues(), getOrder().isNet().booleanValue(), curr
                            .getIsoCode()));
            setCalculated(true);
        }
    }


    protected double applyDiscounts(double totalWithoutDiscounts)
    {
        List discounts = getDiscountValues();
        if(discounts != null && !discounts.isEmpty())
        {
            double tmp = totalWithoutDiscounts;
            for(Iterator<DiscountValue> it = discounts.iterator(); it.hasNext(); )
            {
                DiscountValue discountValue = it.next();
                tmp -= discountValue.isAbsolute() ? (discountValue.getValue() * getQuantity().longValue()) : (
                                tmp * discountValue.getValue() / 100.0D);
            }
            return CoreAlgorithms.round(tmp, getOrder().getCurrency().getDigits().intValue());
        }
        return totalWithoutDiscounts;
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        StringBuilder result = new StringBuilder("AbstractOrderEntry(\n");
        result.append("\t").append("calc=").append(isCalculated()).append("\n");
        result.append("\t").append("product=").append(getProduct()).append("\n");
        result.append("\t").append("info=").append(getInfo()).append("\n");
        result.append("\t").append("qtd=").append(getQuantity()).append("\n");
        result.append("\t").append("unit=").append(getUnit()).append("\n");
        result.append("\t").append("bp=").append(getBasePrice()).append("\n");
        result.append("\t").append("total=").append(getTotalPrice()).append("\n");
        result.append("\t").append("taxes=").append(getTaxValues()).append("\n");
        result.append("\t").append("discounts=").append(getDiscountValues()).append("\n");
        result.append(")");
        return result.toString();
    }


    public int compareTo(Object order)
    {
        return getEntryNumber().intValue() - ((AbstractOrderEntry)order).getEntryNumber().intValue();
    }


    protected void setChanged()
    {
        if(Boolean.TRUE.equals(getSession().getAttribute("save.from.service.layer")))
        {
            return;
        }
        setCalculated(false);
        getOrder().setCalculated(false);
    }
}
