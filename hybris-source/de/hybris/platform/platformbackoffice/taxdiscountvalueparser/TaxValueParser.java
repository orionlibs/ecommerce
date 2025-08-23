package de.hybris.platform.platformbackoffice.taxdiscountvalueparser;

import de.hybris.platform.util.TaxValue;

public class TaxValueParser extends TaxDiscountValueParser implements ValueParser<TaxValue>
{
    public TaxValue parse(String stringValue) throws ParserException
    {
        return parseTaxOrDiscount(stringValue).toTaxValue();
    }


    public String render(TaxValue taxValue)
    {
        return render(new TaxOrDiscount(taxValue));
    }
}
