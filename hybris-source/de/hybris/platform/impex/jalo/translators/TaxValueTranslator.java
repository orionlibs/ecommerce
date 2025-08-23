package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.util.TaxValue;

public class TaxValueTranslator extends SingleValueTranslator
{
    public void validate(StandardColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        super.validate(columnDescriptor);
        if(!OrderEntry.class.isAssignableFrom(columnDescriptor.getHeader().getConfiguredComposedType().getJaloClass()) &&
                        !CartEntry.class.isAssignableFrom(columnDescriptor.getHeader().getConfiguredComposedType().getJaloClass()))
        {
            throw new HeaderValidationException(columnDescriptor.getHeader(), "TaxValueTranslator may only be used within Order- or CartEntry headers", 0);
        }
    }


    public void init(StandardColumnDescriptor columnDescriptor)
    {
        super.init(columnDescriptor);
    }


    public Object convertToJalo(String valueExpr, Item forItem)
    {
        return TaxValue.parseTaxValue(valueExpr);
    }


    protected String convertToString(Object value)
    {
        return (value != null) ? ((TaxValue)value).toString() : "<null>";
    }
}
