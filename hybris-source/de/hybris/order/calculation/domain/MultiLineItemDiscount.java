package de.hybris.order.calculation.domain;

import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MultiLineItemDiscount extends AbstractDiscount
{
    private static final MultiLineDiscountSplitStrategy DEFAULT = (MultiLineDiscountSplitStrategy)new EvenMultiLineDiscountSplitStrategy();
    private List<LineItem> lineitems;
    private List<Percentage> percentages = new ArrayList<>();
    private final MultiLineDiscountSplitStrategy mldss;


    public MultiLineItemDiscount(Money amount)
    {
        this(amount, DEFAULT);
    }


    public MultiLineItemDiscount(Money amount, MultiLineDiscountSplitStrategy mldss)
    {
        super((AbstractAmount)amount);
        this.mldss = (mldss == null) ? DEFAULT : mldss;
    }


    public Map<LineItem, Money> getDiscountValues()
    {
        List<LineItem> lineItems2 = getLineItems();
        List<Money> split = ((Money)getAmount()).split(this.mldss.computeSplitRatio(lineItems2, this.percentages));
        Map<LineItem, Money> retmap = new LinkedHashMap<>(lineItems2.size());
        for(int index = 0; index < lineItems2.size(); index++)
        {
            retmap.put(lineItems2.get(index), split.get(index));
        }
        return retmap;
    }


    public void setLineitems(List<LineItem> lineitems)
    {
        this.lineitems = lineitems;
    }


    public List<LineItem> getLineItems()
    {
        return Collections.unmodifiableList(this.lineitems);
    }
}
