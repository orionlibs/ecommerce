package de.hybris.platform.europe1.jalo;

import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.StandardDateRange;
import java.util.HashMap;
import java.util.Map;

public class Europe1PriceInformation extends PriceInformation
{
    private final PriceRow src;


    protected static Map createQualifiers(PriceRow row)
    {
        Map<Object, Object> qualifiers = new HashMap<>();
        qualifiers.put("minqtd", Long.valueOf(row.getMinQuantity()));
        qualifiers.put("unit", row.getUnit());
        StandardDateRange standardDateRange = row.getDateRange();
        if(standardDateRange != null)
        {
            qualifiers.put("dateRange", standardDateRange);
        }
        return qualifiers;
    }


    protected static PriceValue createPriceValue(PriceRow row, Currency requested)
    {
        Currency act_curr = row.getCurrency();
        double basePrice = requested.equals(act_curr) ? (row.getPriceAsPrimitive() / row.getUnitFactorAsPrimitive()) : act_curr.convert(requested, row.getPriceAsPrimitive() / row.getUnitFactorAsPrimitive());
        return new PriceValue(requested.getIsoCode(), basePrice, row.isNetAsPrimitive());
    }


    public Europe1PriceInformation(PriceRow src, Currency requested)
    {
        super(createQualifiers(src), createPriceValue(src, requested));
        this.src = src;
    }


    protected Europe1PriceInformation(Europe1PriceInformation src, PriceValue priceValue)
    {
        super(src.getQualifiers(), priceValue);
        this.src = src.getPriceRow();
    }


    public long getMinQuantity()
    {
        Long quanitity = (Long)getQualifierValue("minqtd");
        return (quanitity != null) ? quanitity.longValue() : -1L;
    }


    public Unit getUnit()
    {
        return (Unit)getQualifierValue("unit");
    }


    public StandardDateRange getDateRange()
    {
        return (StandardDateRange)getQualifierValue("dateRange");
    }


    public PriceRow getPriceRow()
    {
        return this.src;
    }
}
