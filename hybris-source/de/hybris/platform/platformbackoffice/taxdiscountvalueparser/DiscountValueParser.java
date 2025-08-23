package de.hybris.platform.platformbackoffice.taxdiscountvalueparser;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.DiscountValue;
import java.text.NumberFormat;
import java.util.regex.Matcher;

public class DiscountValueParser extends TaxDiscountValueParser implements ValueParser<DiscountValue>
{
    public DiscountValue parse(String stringValue) throws ParserException
    {
        return parseTaxOrDiscount(stringValue).toDiscountValue();
    }


    public String render(DiscountValue discountValue)
    {
        if(discountValue != null && discountValue.isAsTargetPrice() && discountValue.isAbsolute())
        {
            ParsedDiscount parsed = new ParsedDiscount(discountValue);
            StringBuilder sb = new StringBuilder();
            appendPart(sb, parsed.getCode());
            appendPart(sb, " : ");
            appendPart(sb, " T ");
            if(parsed.isAbsolute())
            {
                int digits = 2;
                boolean isoCorrect = true;
                try
                {
                    CurrencyModel currency = this.commonI18NService.getCurrency(parsed.getCurrencyIsoCode());
                    digits = currency.getDigits().intValue();
                }
                catch(UnknownIdentifierException ex)
                {
                    isoCorrect = false;
                }
                NumberFormat format = this.formatFactory.createNumberFormat();
                format.setMinimumFractionDigits(digits);
                format.setMaximumFractionDigits(digits);
                appendPart(sb, format.format(parsed.getValue()));
                if(isoCorrect)
                {
                    appendPart(sb, " ");
                    appendPart(sb, parsed.getCurrencyIsoCode());
                }
                if(Math.abs(discountValue.getAppliedValue()) > 1.0E-5D)
                {
                    appendPart(sb, " = ");
                    appendPart(sb, format.format(discountValue.getAppliedValue()));
                }
            }
            return sb.toString();
        }
        return render((TaxOrDiscount)new ParsedDiscount(discountValue));
    }


    protected TaxOrDiscount createParsedObject(Matcher matcher, String code, double value, double appliedValue, boolean isAbsolute, String currencyIsoCode)
    {
        boolean asTarget = (isAbsolute && "T".equalsIgnoreCase(matcher.group("tgt")));
        return (TaxOrDiscount)new ParsedDiscount(code, value, isAbsolute, appliedValue, (currencyIsoCode == null) ? "%" : currencyIsoCode, asTarget);
    }
}
