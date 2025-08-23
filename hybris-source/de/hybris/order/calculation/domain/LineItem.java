package de.hybris.order.calculation.domain;

import de.hybris.order.calculation.exception.MissingCalculationDataException;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LineItem implements Taxable
{
    private int giveAwayCount;
    private int numberOfUnits;
    private final List<LineItemDiscount> discounts;
    private final List<LineItemCharge> charges;
    private Order order;
    private final Money basePrice;
    private static final String CALCULATE_ITEM_ERROR = "Could not calculate discount for LineItem ";


    public LineItem(Money basePrice)
    {
        if(basePrice == null)
        {
            throw new IllegalArgumentException("The basePrice for the LineItem is null!");
        }
        this.basePrice = basePrice;
        this.giveAwayCount = 0;
        this.numberOfUnits = 1;
        this.discounts = new ArrayList<>();
        this.charges = new ArrayList<>();
    }


    public LineItem(Money basePrice, int numberOfUnits)
    {
        this(basePrice);
        setNumberOfUnits(numberOfUnits);
    }


    public Money getTotal(Order context)
    {
        return getSubTotal().subtract(getTotalDiscount()).add(getTotalCharge());
    }


    public Money getSubTotal()
    {
        return getOrder().getCalculationStrategies().getRoundingStrategy().multiply(getBasePrice(),
                        BigDecimal.valueOf(getNumberOfUnitsForCalculation()));
    }


    public Money getTotalDiscount()
    {
        Map<LineItemDiscount, Money> discountValues = getTotalDiscounts();
        return discountValues.isEmpty() ? Money.zero(getOrder().getCurrency()) : Money.sum(discountValues.values());
    }


    public Money getTotalCharge()
    {
        Map<LineItemCharge, Money> chargeValues = getTotalCharges();
        return chargeValues.isEmpty() ? Money.zero(getOrder().getCurrency()) : Money.sum(chargeValues.values());
    }


    public Map<LineItemDiscount, Money> getTotalDiscounts()
    {
        if(this.discounts.isEmpty())
        {
            return Collections.emptyMap();
        }
        Map<LineItemDiscount, Money> resultmap = new LinkedHashMap<>(this.discounts.size());
        Money currentValue = getSubTotal();
        for(LineItemDiscount lid : this.discounts)
        {
            Money calculatedDiscount = calculateDiscount(currentValue, lid);
            currentValue = currentValue.subtract(calculatedDiscount);
            resultmap.put(lid, calculatedDiscount);
        }
        return resultmap;
    }


    public Map<LineItemCharge, Money> getTotalCharges()
    {
        if(this.charges.isEmpty())
        {
            return Collections.emptyMap();
        }
        Map<LineItemCharge, Money> resultmap = new LinkedHashMap<>(this.charges.size());
        Money currentValue = getSubTotal();
        for(LineItemCharge lic : this.charges)
        {
            Money calculatedAddCharge = calculateCharge(currentValue, lic);
            currentValue = currentValue.add(calculatedAddCharge);
            resultmap.put(lic, calculatedAddCharge);
        }
        return resultmap;
    }


    protected Money calculateDiscount(Money currentValue, LineItemDiscount discount)
    {
        Money zeroMoney = new Money(getOrder().getCurrency());
        if(zeroMoney.equals(currentValue))
        {
            return zeroMoney;
        }
        if(discount.isPerUnit())
        {
            int numberDiscountUnit = Math.min(discount.getApplicableUnits(), getNumberOfUnits());
            if(discount.getAmount() instanceof Money)
            {
                return getOrder().getCalculationStrategies().getRoundingStrategy().multiply((Money)discount.getAmount(),
                                BigDecimal.valueOf(numberDiscountUnit));
            }
            if(discount.getAmount() instanceof Percentage)
            {
                Percentage percent = (Percentage)discount.getAmount();
                BigDecimal basePriceAmount = getBasePrice().getAmount();
                return getOrder().getCalculationStrategies().getRoundingStrategy().roundToMoney(basePriceAmount
                                                .multiply(percent.getRate()).movePointLeft(2).multiply(BigDecimal.valueOf(numberDiscountUnit)),
                                getOrder().getCurrency());
            }
            throw new MissingCalculationDataException("Could not calculate discount for LineItem " + this);
        }
        if(discount.getAmount() instanceof Money)
        {
            return (Money)discount.getAmount();
        }
        if(discount.getAmount() instanceof Percentage)
        {
            return getOrder().getCalculationStrategies().getRoundingStrategy().getPercentValue(currentValue, (Percentage)discount
                            .getAmount());
        }
        throw new MissingCalculationDataException("Could not calculate discount for LineItem " + this);
    }


    protected Money calculateCharge(Money currentValue, LineItemCharge charge)
    {
        Money zeroMoney = Money.zero(getBasePrice().getCurrency());
        if(charge.isDisabled())
        {
            return zeroMoney;
        }
        if(charge.isPerUnit())
        {
            int applyForThisItemCount = Math.min(charge.getApplicableUnits(), getNumberOfUnits());
            if(charge.getAmount() instanceof Money)
            {
                return getOrder().getCalculationStrategies().getRoundingStrategy().multiply((Money)charge.getAmount(),
                                BigDecimal.valueOf(applyForThisItemCount));
            }
            if(charge.getAmount() instanceof Percentage)
            {
                Percentage percent = (Percentage)charge.getAmount();
                return getOrder().getCalculationStrategies().getRoundingStrategy().roundToMoney(getBasePrice().getAmount()
                                                .multiply(percent.getRate()).movePointLeft(2).multiply(BigDecimal.valueOf(applyForThisItemCount)),
                                getOrder().getCurrency());
            }
            throw new MissingCalculationDataException("Could not calculate discount for LineItem " + this);
        }
        if(charge.getAmount() instanceof Money)
        {
            return (Money)charge.getAmount();
        }
        if(charge.getAmount() instanceof Percentage)
        {
            return getOrder().getCalculationStrategies().getRoundingStrategy().getPercentValue(currentValue, (Percentage)charge
                            .getAmount());
        }
        throw new MissingCalculationDataException("Could not calculate discount for LineItem " + this);
    }


    public int getNumberOfUnits()
    {
        return this.numberOfUnits;
    }


    public final void setNumberOfUnits(int numberOfUnits)
    {
        if(numberOfUnits < 0)
        {
            throw new IllegalArgumentException("The numberOfUnits cannot be negative!");
        }
        this.numberOfUnits = numberOfUnits;
    }


    public void setOrder(Order order)
    {
        this.order = order;
    }


    public Order getOrder()
    {
        if(this.order == null)
        {
            throw new MissingCalculationDataException("Order for LineItem [" + this + "] was not set.");
        }
        return this.order;
    }


    public Money getBasePrice()
    {
        return this.basePrice;
    }


    public String toString()
    {
        return "" + getNumberOfUnits() + "x " + getNumberOfUnits() + getBasePrice() + (
                        (getGiveAwayUnits() > 0) ? ("(free:" + getGiveAwayUnits() + ")") : "") + (
                        this.discounts.isEmpty() ? "" : (" discounts:" + this.discounts));
    }


    public List<LineItemDiscount> getDiscounts()
    {
        return Collections.unmodifiableList(this.discounts);
    }


    public void addDiscounts(LineItemDiscount... discounts)
    {
        addDiscounts(Arrays.asList(discounts));
    }


    public void addDiscounts(List<LineItemDiscount> discounts)
    {
        for(LineItemDiscount prodD : discounts)
        {
            if(!this.discounts.contains(prodD))
            {
                assertCurrency(prodD.getAmount());
                this.discounts.add(prodD);
            }
        }
    }


    public void addDiscount(int index, LineItemDiscount discount)
    {
        if(!this.discounts.contains(discount))
        {
            assertCurrency(discount.getAmount());
            this.discounts.add(index, discount);
        }
    }


    protected void assertCurrency(AbstractAmount amount)
    {
        if(amount instanceof Money)
        {
            ((Money)amount).assertCurreniesAreEqual(getBasePrice().getCurrency());
        }
    }


    public void addDiscount(LineItemDiscount discount)
    {
        addDiscount(this.discounts.size(), discount);
    }


    public void clearDiscounts()
    {
        this.discounts.clear();
    }


    public void removeDiscount(LineItemDiscount discount)
    {
        if(!this.discounts.remove(discount))
        {
            throw new IllegalArgumentException("Discount " + discount + " doesnt belong to line item " + this + " - cannot remove.");
        }
    }


    public List<LineItemCharge> getCharges()
    {
        return Collections.unmodifiableList(this.charges);
    }


    public void addCharges(LineItemCharge... lineItemCharges)
    {
        addCharges(Arrays.asList(lineItemCharges));
    }


    public void addCharges(List<LineItemCharge> lineItemCharges)
    {
        for(LineItemCharge apc : lineItemCharges)
        {
            if(!this.charges.contains(apc))
            {
                assertCurrency(apc.getAmount());
                this.charges.add(apc);
            }
        }
    }


    public void addCharge(LineItemCharge lineItemCharge)
    {
        addCharge(this.charges.size(), lineItemCharge);
    }


    public void addCharge(int index, LineItemCharge lineItemCharge)
    {
        if(!this.charges.contains(lineItemCharge))
        {
            assertCurrency(lineItemCharge.getAmount());
            this.charges.add(index, lineItemCharge);
        }
    }


    public void clearCharges()
    {
        this.charges.clear();
    }


    public void removeCharge(LineItemCharge charge)
    {
        if(!this.charges.remove(charge))
        {
            throw new IllegalArgumentException("Charge " + charge + " doesnt belong to line item " + this + " - cannot remove.");
        }
    }


    public int getNumberOfUnitsForCalculation()
    {
        return Math.max(0, getNumberOfUnits() - getGiveAwayUnits());
    }


    public int getGiveAwayUnits()
    {
        return this.giveAwayCount;
    }


    public final void setGiveAwayUnits(int giveAwayCount)
    {
        if(giveAwayCount < 0)
        {
            throw new IllegalArgumentException("The give away count cannot be negative");
        }
        this.giveAwayCount = giveAwayCount;
    }


    public Collection<Tax> getTaxes()
    {
        return getOrder().getTaxesFor(this);
    }
}
