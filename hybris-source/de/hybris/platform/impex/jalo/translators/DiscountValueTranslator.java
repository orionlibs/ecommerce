package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.util.DiscountValue;

public class DiscountValueTranslator extends SingleValueTranslator
{
    public Object convertToJalo(String valueExpr, Item forItem)
    {
        return DiscountValue.parseDiscountValue(valueExpr);
    }


    protected String convertToString(Object value)
    {
        return (value != null) ? ((DiscountValue)value).toString() : "<null>";
    }
}
