package de.hybris.platform.europe1.constants;

import de.hybris.platform.europe1.jalo.AbstractDiscountRow;
import de.hybris.platform.europe1.jalo.DiscountRow;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.order.price.TaxInformation;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.TaxValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Europe1Tools
{
    public static final Collection getTaxValues(List taxInformations)
    {
        Collection<TaxValue> ret = new ArrayList(taxInformations.size());
        for(Iterator<TaxInformation> iter = taxInformations.iterator(); iter.hasNext(); )
        {
            ret.add(((TaxInformation)iter.next()).getTaxValue());
        }
        return ret;
    }


    @Deprecated(since = "6.1.0", forRemoval = true)
    public static final List<DiscountValue> createDiscountValueList(List<AbstractDiscountRow> discountRows)
    {
        return (List<DiscountValue>)discountRows.stream().map(i -> createDiscountValue(i)).collect(Collectors.toList());
    }


    @Deprecated(since = "6.2.0", forRemoval = true)
    public static DiscountValue createDiscountValue(AbstractDiscountRow discountRow)
    {
        DiscountValue value;
        Discount discount = discountRow.getDiscount();
        boolean rowHasValue = (discountRow.getValue() != null);
        boolean isAbsolute = rowHasValue ? discountRow.isAbsoluteAsPrimitive() : discount.isAbsoluteAsPrimitive();
        boolean asTargetPrice = (rowHasValue && isAbsolute && discountRow instanceof DiscountRow && Boolean.TRUE.equals(((DiscountRow)discountRow).isAsTargetPrice()));
        if(asTargetPrice)
        {
            value = DiscountValue.createTargetPrice(discount.getCode(), discountRow.getValue(), discountRow
                            .getCurrency().getIsocode());
        }
        else if(isAbsolute)
        {
            value = rowHasValue ? DiscountValue.createAbsolute(discount.getCode(), discountRow.getValue(), discountRow.getCurrency().getIsocode()) : DiscountValue.createAbsolute(discount.getCode(), discount.getValue(), discount.getCurrency().getIsocode());
        }
        else
        {
            value = DiscountValue.createRelative(discount.getCode(), rowHasValue ? discountRow.getValue() : discount.getValue());
        }
        return value;
    }


    public static final PriceInformation createPriceInformation(PriceRow row, Currency currency)
    {
        Map<Object, Object> qualifiers = new HashMap<>();
        qualifiers.put("minqtd", Long.valueOf(row.getMinQuantity()));
        qualifiers.put("unit", row.getUnit());
        qualifiers.put("pricerow", row);
        StandardDateRange standardDateRange = row.getDateRange();
        if(standardDateRange != null)
        {
            qualifiers.put("dateRange", standardDateRange);
        }
        Currency act_curr = row.getCurrency();
        double basePrice = currency.equals(act_curr) ? (row.getPriceAsPrimitive() / row.getUnitFactorAsPrimitive()) : act_curr.convert(currency, row.getPriceAsPrimitive() / row.getUnitFactorAsPrimitive());
        return new PriceInformation(qualifiers, new PriceValue(currency.getIsoCode(), basePrice, row.isNetAsPrimitive()));
    }
}
