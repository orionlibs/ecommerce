package de.hybris.platform.platformbackoffice.taxdiscountvalueparser;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultFormatFactory;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TaxDiscountValueParser
{
    private static final Logger LOG = LoggerFactory.getLogger(TaxDiscountValueParser.class);
    protected static final String SEPARATOR = ":";
    protected String pattern;
    protected String patternExplained;
    protected CurrencyDao currencyDao;
    protected DefaultFormatFactory formatFactory;
    protected DefaultCommonI18NService commonI18NService;
    protected Pattern compiledPattern;


    public TaxOrDiscount parseTaxOrDiscount(String stringValue) throws ParserException
    {
        Matcher matcher = this.compiledPattern.matcher(stringValue);
        double appliedValue = 0.0D;
        if(matcher.matches())
        {
            double value;
            String code = matcher.group("code");
            String currencyIsoCode = matcher.group("currency");
            if(currencyIsoCode != null && this.currencyDao.findCurrenciesByCode(currencyIsoCode).isEmpty())
            {
                throw new ParserException("currency with iso code: " + currencyIsoCode + " does not exist!");
            }
            boolean isAbsolute = (matcher.group("currency") != null);
            try
            {
                Locale locale = this.commonI18NService.getLocaleForLanguage(this.commonI18NService.getCurrentLanguage());
                NumberFormat format = NumberFormat.getNumberInstance(locale);
                value = format.parse(matcher.group("value")).doubleValue();
                if(matcher.group("appliedValue") != null)
                {
                    appliedValue = format.parse(matcher.group("appliedValue")).doubleValue();
                }
            }
            catch(NumberFormatException | java.text.ParseException nfe)
            {
                throw new ParserException(nfe);
            }
            return createParsedObject(matcher, code, value, appliedValue, isAbsolute, currencyIsoCode);
        }
        throw new ParserException("Expression must match the following pattern: " + this.patternExplained);
    }


    protected TaxOrDiscount createParsedObject(Matcher matcher, String code, double value, double appliedValue, boolean isAbsolute, String currencyIsoCode)
    {
        return new TaxOrDiscount(code, value, isAbsolute, appliedValue, (currencyIsoCode == null) ? "%" : currencyIsoCode);
    }


    public String render(TaxOrDiscount parsed)
    {
        StringBuilder sb = new StringBuilder();
        appendPart(sb, parsed.getCode());
        appendPart(sb, " : ");
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
        }
        else
        {
            NumberFormat format = this.formatFactory.createNumberFormat();
            format.setMinimumFractionDigits(2);
            format.setMaximumFractionDigits(2);
            Locale locale = this.commonI18NService.getLocaleForLanguage(this.commonI18NService.getCurrentLanguage());
            NumberFormat percentFormatter = NumberFormat.getPercentInstance(locale);
            percentFormatter.setMinimumFractionDigits(2);
            percentFormatter.setMaximumFractionDigits(2);
            appendPart(sb, percentFormatter.format(parsed.getValue() / 100.0D));
            NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
            numberFormat.setMinimumFractionDigits(2);
            numberFormat.setMaximumFractionDigits(2);
            appendPart(sb, " = ");
            appendPart(sb, numberFormat.format(parsed.getAppliedValue()));
        }
        return sb.toString();
    }


    protected void appendPart(StringBuilder sb, String part)
    {
        String result = (part == null) ? "" : part;
        if(StringUtils.isNotEmpty(result))
        {
            sb.append(result);
        }
    }


    public void setPattern(String pattern)
    {
        this.pattern = pattern;
        try
        {
            this.compiledPattern = Pattern.compile(this.pattern);
        }
        catch(PatternSyntaxException e)
        {
            LOG.warn("Could not compile pattern", e);
        }
    }


    public void setCurrencyDao(CurrencyDao currencyDao)
    {
        this.currencyDao = currencyDao;
    }


    public void setPatternExplained(String patternExplained)
    {
        this.patternExplained = patternExplained;
    }


    public void setFormatFactory(DefaultFormatFactory formatFactory)
    {
        this.formatFactory = formatFactory;
    }


    public void setCommonI18NService(DefaultCommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
