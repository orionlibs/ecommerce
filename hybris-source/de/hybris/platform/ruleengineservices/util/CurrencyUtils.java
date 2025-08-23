package de.hybris.platform.ruleengineservices.util;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleEvaluationException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class CurrencyUtils
{
    private static final int DEFAULT_CURRENCY_DIGITS = 2;
    private CommonI18NService commonI18NService;


    public BigDecimal applyRounding(BigDecimal price, String currencyIsoCode)
    {
        BigDecimal roundedPrice = null;
        if(Objects.nonNull(price))
        {
            Optional<Integer> scale = getDigitsOfCurrency(currencyIsoCode);
            roundedPrice = scale.<BigDecimal>map(d -> price.setScale(d.intValue(), RoundingMode.HALF_EVEN)).orElse(price);
        }
        return roundedPrice;
    }


    public BigDecimal convertCurrency(String sourceCurrencyIsoCode, String targetCurrencyIsoCode, BigDecimal sourceValue)
    {
        CurrencyModel targetCurrency = getCurrency(targetCurrencyIsoCode);
        double targetConversionRate = targetCurrency.getConversion().doubleValue();
        CurrencyModel sourceCurrency = getCurrency(sourceCurrencyIsoCode);
        double sourceConversionRate = sourceCurrency.getConversion().doubleValue();
        int targetCurrencyDigits = ((Integer)Optional.<Integer>ofNullable(targetCurrency.getDigits()).orElse(Integer.valueOf(0))).intValue();
        double convertedCurrencyValue = getCommonI18NService().convertAndRoundCurrency(sourceConversionRate, targetConversionRate, targetCurrencyDigits, sourceValue
                        .doubleValue());
        return BigDecimal.valueOf(convertedCurrencyValue).setScale(targetCurrencyDigits, RoundingMode.HALF_EVEN);
    }


    public Optional<Integer> getDigitsOfCurrency(String currencyIsoCode)
    {
        Optional<Integer> digits = Optional.empty();
        if(Objects.nonNull(currencyIsoCode))
        {
            CurrencyModel currency = getCurrency(currencyIsoCode);
            digits = Optional.ofNullable(currency.getDigits());
        }
        return digits;
    }


    public Integer getDigitsOfCurrencyOrDefault(String currencyIsoCode)
    {
        return getDigitsOfCurrency(currencyIsoCode).orElse(Integer.valueOf(2));
    }


    public CurrencyModel getCurrency(String currencyCode)
    {
        CurrencyModel currency;
        if(StringUtils.isEmpty(currencyCode))
        {
            throw new RuleEvaluationException("Currency code is empty");
        }
        try
        {
            currency = getCommonI18NService().getCurrency(currencyCode);
        }
        catch(UnknownIdentifierException e)
        {
            throw new RuleEvaluationException("No currency found with the code: " + currencyCode, e);
        }
        return currency;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
