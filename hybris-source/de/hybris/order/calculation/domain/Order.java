package de.hybris.order.calculation.domain;

import de.hybris.order.calculation.exception.MissingCalculationDataException;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import de.hybris.order.calculation.strategies.CalculationStrategies;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class Order
{
    private static final Money.MoneyExtractor<LineItem> LINE_ITEM_TOTAL_EXTRACTOR = (Money.MoneyExtractor<LineItem>)new Object();
    private final boolean netMode;
    private final Currency currency;
    private final CalculationStrategies calculationStrategies;
    private final List<LineItem> lineItems;
    private final List<Tax> taxes;
    private final List<OrderDiscount> discounts;
    private final List<OrderCharge> charges;


    public Order(Currency currency, CalculationStrategies calculationStrategies)
    {
        this(currency, false, calculationStrategies);
    }


    public Order(Currency currency, boolean isNet, CalculationStrategies calculationStrategies)
    {
        this.currency = currency;
        this.netMode = isNet;
        this.calculationStrategies = calculationStrategies;
        this.lineItems = new ArrayList<>();
        this.taxes = new ArrayList<>();
        this.discounts = new ArrayList<>();
        this.charges = new ArrayList<>();
    }


    public Money getTotal()
    {
        return getSubTotal().subtract(getTotalDiscount()).add(getTotalCharge());
    }


    public Money getSubTotal()
    {
        if(hasLineItems())
        {
            return Money.sum(getLineItems(), LINE_ITEM_TOTAL_EXTRACTOR);
        }
        return Money.zero(this.currency);
    }


    public Money getTotalTaxFor(Tax tax)
    {
        if(!this.taxes.contains(tax))
        {
            throw new IllegalArgumentException("Tax " + tax + " doesnt belong to order " + this + " - cannot calculate total.");
        }
        double taxCorrectionFactor = getAutomaticTaxCorrectionFactor();
        return calculateTaxTotal(tax, taxCorrectionFactor);
    }


    public Map<Tax, Money> getTotalTaxes()
    {
        Map<Tax, Money> result = new LinkedHashMap<>(this.taxes.size());
        double taxCorrectionFactor = getAutomaticTaxCorrectionFactor();
        for(Tax tax : getTaxes())
        {
            result.put(tax, calculateTaxTotal(tax, taxCorrectionFactor));
        }
        return result;
    }


    protected double getAutomaticTaxCorrectionFactor()
    {
        if(BigDecimal.ZERO.compareTo(getSubTotal().getAmount()) == 0)
        {
            return 1.0D;
        }
        BigDecimal totalWoFixedTaxedCharges = getTotal().subtract(getFixedTaxedAdditionalCharges(getTotalCharges())).getAmount();
        return totalWoFixedTaxedCharges.doubleValue() / getSubTotal().getAmount().doubleValue();
    }


    protected Money getFixedTaxedAdditionalCharges(Map<OrderCharge, Money> aocValues)
    {
        Money sum = new Money(getCurrency());
        for(Map.Entry<OrderCharge, Money> e : aocValues.entrySet())
        {
            if(hasAssignedTaxes((Taxable)e.getKey()))
            {
                sum = sum.add(e.getValue());
            }
        }
        return sum;
    }


    public Collection<Tax> getTaxesFor(Taxable object)
    {
        Collection<Tax> ret = null;
        for(Tax t : getTaxes())
        {
            if(t.getTargets().contains(object))
            {
                if(ret == null)
                {
                    ret = new LinkedHashSet<>();
                }
                ret.add(t);
            }
        }
        return (ret == null) ? Collections.<Tax>emptySet() : ret;
    }


    public boolean hasAssignedTaxes(Taxable object)
    {
        for(Tax t : getTaxes())
        {
            if(t.getTargets().contains(object))
            {
                return true;
            }
        }
        return false;
    }


    public Money getTotalTax()
    {
        Map<Tax, Money> taxValues = getTotalTaxes();
        return taxValues.isEmpty() ? Money.zero(getCurrency()) : Money.sum(taxValues.values());
    }


    protected Money calculateTaxTotal(Tax tax, double autoTaxCorrectionFactor)
    {
        if(tax.getAmount() instanceof Money)
        {
            return (Money)tax.getAmount();
        }
        if(tax.getAmount() instanceof Percentage)
        {
            Money taxedChargeSum = Money.zero(getCurrency());
            for(Taxable taxcharge : tax.getTargets())
            {
                taxedChargeSum = taxedChargeSum.add(taxcharge.getTotal(this));
            }
            BigDecimal taxRate = ((Percentage)tax.getAmount()).getRate();
            BigDecimal costRate = isNet() ? new BigDecimal(100) : taxRate.add(new BigDecimal(100));
            double overallFactor = taxRate.doubleValue() * autoTaxCorrectionFactor / costRate.doubleValue();
            return getCalculationStrategies().getTaxRondingStrategy().multiply(taxedChargeSum, BigDecimal.valueOf(overallFactor));
        }
        throw new IllegalStateException();
    }


    public Money getTotalCharge()
    {
        Map<OrderCharge, Money> map = getTotalCharges();
        return map.isEmpty() ? new Money(getCurrency()) : Money.sum(map.values());
    }


    public Money getTotalChargeOfType(AbstractCharge.ChargeType chargeType)
    {
        Money zero = Money.zero(getCurrency());
        return hasCharges() ? Money.sum(getTotalCharges().entrySet(), (Money.MoneyExtractor)new Object(this, chargeType, zero)) :
                        zero;
    }


    public Money getTotalDiscount()
    {
        Map<OrderDiscount, Money> orderDiscountValues = getTotalDiscounts();
        return orderDiscountValues.isEmpty() ? new Money(getCurrency()) : Money.sum(orderDiscountValues.values());
    }


    public Map<OrderCharge, Money> getTotalCharges()
    {
        if(this.charges.isEmpty())
        {
            return Collections.emptyMap();
        }
        Map<OrderCharge, Money> resultmap = new LinkedHashMap<>(this.charges.size());
        Money currentValue = getSubTotal();
        for(OrderCharge orderCharge : this.charges)
        {
            Money calculatedAddCharge = calculateOrderCharge(currentValue, orderCharge);
            currentValue = currentValue.add(calculatedAddCharge);
            resultmap.put(orderCharge, calculatedAddCharge);
        }
        return resultmap;
    }


    public Map<OrderDiscount, Money> getTotalDiscounts()
    {
        if(this.discounts.isEmpty())
        {
            return Collections.emptyMap();
        }
        Map<OrderDiscount, Money> resultmap = new LinkedHashMap<>(this.discounts.size());
        Money currentValue = getSubTotal();
        for(OrderDiscount orderDisc : this.discounts)
        {
            Money calculatedDiscount = calculateOrderDiscount(currentValue, orderDisc);
            currentValue = currentValue.subtract(calculatedDiscount);
            resultmap.put(orderDisc, calculatedDiscount);
        }
        return resultmap;
    }


    protected Money calculateOrderCharge(Money currentValue, OrderCharge addCharge)
    {
        if(addCharge.isDisabled())
        {
            return new Money(getCurrency());
        }
        if(addCharge.getAmount() instanceof Money)
        {
            return (Money)addCharge.getAmount();
        }
        if(addCharge.getAmount() instanceof Percentage)
        {
            Percentage percent = (Percentage)addCharge.getAmount();
            return getCalculationStrategies().getRoundingStrategy().multiply(currentValue, percent.getRate().movePointLeft(2));
        }
        throw new MissingCalculationDataException("Could not calculate order charge for Order");
    }


    protected Money calculateOrderDiscount(Money currentValue, OrderDiscount discount)
    {
        if(discount.getAmount() instanceof Money)
        {
            return (Money)discount.getAmount();
        }
        if(discount.getAmount() instanceof Percentage)
        {
            Percentage percent = (Percentage)discount.getAmount();
            return getCalculationStrategies().getRoundingStrategy().multiply(currentValue, percent.getRate().movePointLeft(2));
        }
        throw new MissingCalculationDataException("Could not calculate order discount for Order");
    }


    public Money getTotalIncludingTaxes()
    {
        return isNet() ? getTotal().add(getTotalTax()) : getTotal();
    }


    public List<LineItem> getLineItems()
    {
        return Collections.unmodifiableList(this.lineItems);
    }


    public void addLineItems(LineItem... lineItems)
    {
        addLineItems(Arrays.asList(lineItems));
    }


    public void addLineItems(List<LineItem> lineItems)
    {
        for(LineItem lineItem : lineItems)
        {
            if(!this.lineItems.contains(lineItem))
            {
                lineItem.getBasePrice().assertCurreniesAreEqual(getCurrency());
                lineItem.setOrder(this);
                this.lineItems.add(lineItem);
            }
        }
    }


    public void addLineItem(LineItem lineitem)
    {
        addLineItem(this.lineItems.size(), lineitem);
    }


    public void addLineItem(int index, LineItem lineitem)
    {
        lineitem.getBasePrice().assertCurreniesAreEqual(getCurrency());
        lineitem.setOrder(this);
        this.lineItems.add(index, lineitem);
    }


    public void clearLineItems()
    {
        for(LineItem lineitem : this.lineItems)
        {
            lineitem.setOrder(null);
        }
        this.lineItems.clear();
    }


    public void removeLineItem(LineItem lineitem)
    {
        if(this.lineItems.remove(lineitem))
        {
            lineitem.setOrder(null);
        }
        else
        {
            throw new IllegalArgumentException("Line item " + lineitem + " does not belong to order " + this + " - cannot remove it.");
        }
    }


    public Collection<Tax> getTaxes()
    {
        return Collections.unmodifiableList(this.taxes);
    }


    public void addTaxes(Tax... taxes)
    {
        addTaxes(Arrays.asList(taxes));
    }


    public void addTaxes(Collection<Tax> taxes)
    {
        for(Tax tax : taxes)
        {
            if(!this.taxes.contains(tax))
            {
                if(tax.getAmount() instanceof Money)
                {
                    ((Money)tax.getAmount()).assertCurreniesAreEqual(getCurrency());
                }
                this.taxes.add(tax);
            }
        }
    }


    public void addTax(Tax tax)
    {
        if(!this.taxes.contains(tax))
        {
            if(tax.getAmount() instanceof Money)
            {
                ((Money)tax.getAmount()).assertCurreniesAreEqual(getCurrency());
            }
            this.taxes.add(tax);
        }
    }


    public void clearTaxes()
    {
        this.taxes.clear();
    }


    public void removeTax(Tax tax)
    {
        if(!this.taxes.remove(tax))
        {
            throw new IllegalArgumentException("Tax " + tax + " doesnt belong to order " + this + " - cannot remove.");
        }
    }


    public List<OrderDiscount> getDiscounts()
    {
        return Collections.unmodifiableList(this.discounts);
    }


    public void addDiscounts(OrderDiscount... discounts)
    {
        addDiscounts(Arrays.asList(discounts));
    }


    public void addDiscounts(List<OrderDiscount> discounts)
    {
        for(OrderDiscount d : discounts)
        {
            if(!this.discounts.contains(d))
            {
                if(d.getAmount() instanceof Money)
                {
                    ((Money)d.getAmount()).assertCurreniesAreEqual(getCurrency());
                }
                this.discounts.add(d);
            }
        }
    }


    public void addDiscount(OrderDiscount discount)
    {
        addDiscount(this.discounts.size(), discount);
    }


    public void addDiscount(int index, OrderDiscount discount)
    {
        if(!this.discounts.contains(discount))
        {
            if(discount.getAmount() instanceof Money)
            {
                ((Money)discount.getAmount()).assertCurreniesAreEqual(getCurrency());
            }
            this.discounts.add(index, discount);
        }
    }


    public void clearDiscounts()
    {
        this.discounts.clear();
    }


    public void removeDiscount(OrderDiscount orderDiscount)
    {
        if(!this.discounts.remove(orderDiscount))
        {
            throw new IllegalArgumentException("Discount " + orderDiscount + " doesnt belong to order " + this + " - cannot remove");
        }
    }


    public List<OrderCharge> getCharges()
    {
        return Collections.unmodifiableList(this.charges);
    }


    public void addCharges(OrderCharge... charges)
    {
        addCharges(Arrays.asList(charges));
    }


    public void addCharges(List<OrderCharge> charges)
    {
        for(OrderCharge aoc : charges)
        {
            if(!this.charges.contains(aoc))
            {
                if(aoc.getAmount() instanceof Money)
                {
                    ((Money)aoc.getAmount()).assertCurreniesAreEqual(getCurrency());
                }
                this.charges.add(aoc);
            }
        }
    }


    public void addCharge(OrderCharge aoc)
    {
        addCharge(this.charges.size(), aoc);
    }


    public void addCharge(int index, OrderCharge aoc)
    {
        if(!this.charges.contains(aoc))
        {
            if(aoc.getAmount() instanceof Money)
            {
                ((Money)aoc.getAmount()).assertCurreniesAreEqual(getCurrency());
            }
            this.charges.add(index, aoc);
        }
    }


    public void clearCharges()
    {
        this.charges.clear();
    }


    public void removeCharge(OrderCharge aoc)
    {
        if(!this.charges.remove(aoc))
        {
            throw new IllegalArgumentException("Charge " + aoc + " doesnt belong to order " + this + " - cannot remove.");
        }
    }


    public boolean hasLineItems()
    {
        return !this.lineItems.isEmpty();
    }


    public boolean hasCharges()
    {
        return !this.charges.isEmpty();
    }


    public boolean hasDisounts()
    {
        return !this.discounts.isEmpty();
    }


    public boolean hasTaxes()
    {
        return !this.taxes.isEmpty();
    }


    public boolean isNet()
    {
        return this.netMode;
    }


    public boolean isGross()
    {
        return !this.netMode;
    }


    public CalculationStrategies getCalculationStrategies()
    {
        if(this.calculationStrategies == null)
        {
            throw new MissingCalculationDataException("No calculation strategies were set!");
        }
        return this.calculationStrategies;
    }


    public Currency getCurrency()
    {
        return this.currency;
    }
}
