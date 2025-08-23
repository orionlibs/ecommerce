package de.hybris.platform.promotionengineservices.promotionengine.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionMessageParameterResolutionStrategy;
import de.hybris.platform.promotionengineservices.util.PromotionResultUtils;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCurrencyAmountResolutionStrategy implements PromotionMessageParameterResolutionStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCurrencyAmountResolutionStrategy.class);
    private PromotionResultUtils promotionResultUtils;


    public String getValue(RuleParameterData data, PromotionResultModel promotionResult, Locale locale)
    {
        ServicesUtil.validateParameterNotNull(data, "parameter data must not be null");
        ServicesUtil.validateParameterNotNull(promotionResult, "parameter promotionResult must not be null");
        ServicesUtil.validateParameterNotNull(locale, "parameter locale must not be null");
        Map<String, BigDecimal> values = (Map<String, BigDecimal>)data.getValue();
        AbstractOrderModel order = getPromotionResultUtils().getOrder(promotionResult);
        if(order != null && order.getCurrency() != null)
        {
            String isoCode = order.getCurrency().getIsocode();
            BigDecimal amount = values.get(isoCode);
            if(amount != null)
            {
                return formatCurrencyAmount(locale, order.getCurrency(), amount);
            }
        }
        return null;
    }


    public RuleParameterData getReplacedParameter(RuleParameterData paramToReplace, PromotionResultModel promotionResult, Object actualValueAsObject)
    {
        ServicesUtil.validateParameterNotNull(paramToReplace, "parameter paramToReplace must not be null");
        ServicesUtil.validateParameterNotNull(promotionResult, "parameter promotionResult must not be null");
        AbstractOrderModel order = getPromotionResultUtils().getOrder(promotionResult);
        ServicesUtil.validateParameterNotNull(order, "parameter promotionResult.order must not be null");
        ServicesUtil.validateParameterNotNull(order.getCurrency(), "parameter promotionResult.order.currency must not be null");
        ServicesUtil.validateParameterNotNull(order.getCurrency().getIsocode(), "parameter promotionResult.order.currency.isocode must not be null");
        Preconditions.checkArgument(actualValueAsObject instanceof BigDecimal, "Actual value must be of BigDecimal type");
        Preconditions.checkArgument(paramToReplace.getValue() instanceof Map, "parameter paramToReplace must by of type java.util.Map");
        Map<String, BigDecimal> values = new HashMap<>((Map<? extends String, ? extends BigDecimal>)paramToReplace.getValue());
        String isoCode = order.getCurrency().getIsocode();
        values.put(isoCode, (BigDecimal)actualValueAsObject);
        RuleParameterData result = new RuleParameterData();
        result.setType(paramToReplace.getType());
        result.setUuid(paramToReplace.getUuid());
        result.setValue(values);
        return result;
    }


    protected String formatCurrencyAmount(Locale locale, CurrencyModel currency, BigDecimal amount)
    {
        ServicesUtil.validateParameterNotNull(locale, "locale must not be null");
        ServicesUtil.validateParameterNotNull(currency, "currency must not be null");
        NumberFormat localisedNumberFormat = NumberFormat.getCurrencyInstance(locale);
        String currencyIsoCode = currency.getIsocode();
        Currency javaCurrency = Currency.getInstance(currencyIsoCode);
        if(javaCurrency == null)
        {
            LOG.warn("formatCurrencyAmount failed to lookup java.util.Currency from [{}] ensure this is an ISO 4217 code and is supported by the java runtime.", currencyIsoCode);
        }
        else
        {
            localisedNumberFormat.setCurrency(javaCurrency);
        }
        adjustDigits((DecimalFormat)localisedNumberFormat, currency);
        adjustSymbol((DecimalFormat)localisedNumberFormat, currency);
        String result = localisedNumberFormat.format(amount);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("formatCurrencyAmount locale=[" + locale + "] currency=[" + currency + "] amount=[" + amount + "] currencyIsoCode=[" + currencyIsoCode + "] javaCurrency=[" + javaCurrency + "] result=[" + result + "]");
        }
        return result;
    }


    protected DecimalFormat adjustDigits(DecimalFormat format, CurrencyModel currency)
    {
        int tempDigits = (currency.getDigits() == null) ? 0 : currency.getDigits().intValue();
        int digits = Math.max(0, tempDigits);
        format.setMaximumFractionDigits(digits);
        format.setMinimumFractionDigits(digits);
        if(digits == 0)
        {
            format.setDecimalSeparatorAlwaysShown(false);
        }
        return format;
    }


    protected static DecimalFormat adjustSymbol(DecimalFormat format, CurrencyModel currency)
    {
        String symbol = currency.getSymbol();
        if(symbol != null)
        {
            DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
            String iso = currency.getIsocode();
            boolean changed = false;
            if(!iso.equalsIgnoreCase(symbols.getInternationalCurrencySymbol()))
            {
                symbols.setInternationalCurrencySymbol(iso);
                changed = true;
            }
            if(!symbol.equals(symbols.getCurrencySymbol()))
            {
                symbols.setCurrencySymbol(symbol);
                changed = true;
            }
            if(changed)
            {
                format.setDecimalFormatSymbols(symbols);
            }
        }
        return format;
    }


    protected PromotionResultUtils getPromotionResultUtils()
    {
        return this.promotionResultUtils;
    }


    @Required
    public void setPromotionResultUtils(PromotionResultUtils promotionResultUtils)
    {
        this.promotionResultUtils = promotionResultUtils;
    }
}
