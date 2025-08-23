package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleMessageFormatStrategy;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleMessageParameterDecorator;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueNormalizerStrategy;
import de.hybris.platform.servicelayer.i18n.L10NService;
import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleMessageFormatStrategy implements RuleMessageFormatStrategy
{
    protected static final String UNKNOWN_PARAMETER = "?";
    protected static final String DEFAULT_FORMAT_STYLE = "default";
    protected static final Pattern PARAMETER_PATTERN = Pattern.compile("(\\{([^,\\}]*)(,([^,\\}]*))?(,([^,\\}]*))?\\})");
    protected static final Pattern LIST_PATTERN = Pattern.compile("^List\\((.*)\\)");
    protected static final Pattern MAP_PATTERN = Pattern.compile("^Map\\((.+),\\s*(.+)\\)");
    private L10NService l10NService;
    private EnumerationService enumerationService;
    private RuleParameterValueNormalizerStrategy ruleParameterValueNormalizerStrategy;


    public String format(String message, Map<String, RuleParameterData> parameters, Locale locale)
    {
        return format(message, parameters, locale, null);
    }


    public String format(String message, Map<String, RuleParameterData> parameters, Locale locale, RuleMessageParameterDecorator parameterDecorator)
    {
        StringBuffer fmtMessage = new StringBuffer();
        List<RuleMessageParameterData> fmtMessageParameters = new ArrayList<>();
        int matcherIndex = 0;
        Matcher matcher = PARAMETER_PATTERN.matcher(message);
        while(matcher.find())
        {
            String variable = matcher.group(2);
            String replacement = null;
            RuleParameterData parameter = parameters.get(variable);
            if(parameter != null)
            {
                DecoratorFormat decoratorFormat;
                StringBuilder newVariable = new StringBuilder();
                newVariable.append('{');
                newVariable.append(matcherIndex);
                newVariable.append('}');
                Object value = resolveValue(parameter, locale);
                Format format = null;
                if(value == null)
                {
                    value = sanitizeValue(parameter);
                }
                else
                {
                    format = resolveFormat(matcher.group(4), matcher.group(6), locale);
                }
                if(parameterDecorator != null)
                {
                    decoratorFormat = new DecoratorFormat(format, parameter, parameterDecorator);
                }
                RuleMessageParameterData fmtMessageParameter = new RuleMessageParameterData();
                fmtMessageParameter.setValue(value);
                fmtMessageParameter.setFormat((Format)decoratorFormat);
                fmtMessageParameters.add(fmtMessageParameter);
                replacement = newVariable.toString();
                matcherIndex++;
            }
            else
            {
                replacement = "?";
            }
            matcher.appendReplacement(fmtMessage, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(fmtMessage);
        int size = fmtMessageParameters.size();
        Object[] messageParameters = new Object[size];
        Format[] messageFormats = new Format[size];
        int index = 0;
        for(RuleMessageParameterData fmtMessageParameter : fmtMessageParameters)
        {
            messageParameters[index] = fmtMessageParameter.getValue();
            messageFormats[index] = fmtMessageParameter.getFormat();
            index++;
        }
        MessageFormat messageFormat = new MessageFormat(fmtMessage.toString(), locale);
        messageFormat.setFormats(messageFormats);
        return messageFormat.format(messageParameters);
    }


    protected Object resolveValue(RuleParameterData parameter, Locale locale)
    {
        Object value = getRuleParameterValueNormalizerStrategy().normalize(parameter.getValue(), parameter.getType());
        if(value == null)
        {
            return null;
        }
        if(value instanceof de.hybris.platform.ruleengineservices.definitions.RuleParameterEnum)
        {
            String localizationKey = value.getClass().getName() + "." + value.getClass().getName() + ".name";
            return this.l10NService.getLocalizedString(localizationKey.toLowerCase(Locale.ENGLISH));
        }
        if(value instanceof HybrisEnumValue)
        {
            return this.enumerationService.getEnumerationName((HybrisEnumValue)value, locale);
        }
        return value;
    }


    protected Object sanitizeValue(RuleParameterData parameter)
    {
        Matcher listMatcher = LIST_PATTERN.matcher(parameter.getType());
        if(listMatcher.matches())
        {
            return Collections.emptyList();
        }
        Matcher mapMatcher = MAP_PATTERN.matcher(parameter.getType());
        if(mapMatcher.matches())
        {
            return Collections.emptyMap();
        }
        return "?";
    }


    protected Format resolveFormat(String name, String arguments, Locale locale)
    {
        if(name == null)
        {
            return null;
        }
        Format format = null;
        switch(name)
        {
            case "number":
                format = createNumberFormat(arguments, locale);
                return format;
            case "date":
                format = createDateFormat(arguments, locale);
                return format;
            case "time":
                format = createTimeFormat(arguments, locale);
                return format;
            case "choice":
                format = createChoiceFormat(arguments);
                return format;
        }
        throw new IllegalArgumentException("unknown format type: " + name);
    }


    protected NumberFormat createNumberFormat(String arguments, Locale locale)
    {
        NumberFormat format;
        String formatStyle;
        String formatMultiplier;
        if(arguments == null)
        {
            formatStyle = "default";
            formatMultiplier = null;
        }
        else
        {
            int separatorIndex = arguments.indexOf('*');
            if(separatorIndex >= 0)
            {
                formatStyle = arguments.substring(0, separatorIndex);
                formatMultiplier = arguments.substring(separatorIndex + 1, arguments.length());
            }
            else
            {
                formatStyle = arguments;
                formatMultiplier = null;
            }
        }
        switch(formatStyle)
        {
            case "default":
                format = NumberFormat.getInstance(locale);
                break;
            case "currency":
                format = NumberFormat.getCurrencyInstance(locale);
                break;
            case "percent":
                format = NumberFormat.getPercentInstance(locale);
                break;
            case "integer":
                format = NumberFormat.getIntegerInstance(locale);
                break;
            default:
                format = new DecimalFormat(formatStyle, DecimalFormatSymbols.getInstance(locale));
                break;
        }
        if(formatMultiplier != null && format instanceof DecimalFormat)
        {
            int multiplier = Integer.parseInt(formatMultiplier);
            ((DecimalFormat)format).setMultiplier(multiplier);
        }
        return format;
    }


    protected DateFormat createDateFormat(String arguments, Locale locale)
    {
        String formatStyle = (arguments == null) ? "default" : arguments;
        DateFormat dateFormat = new SimpleDateFormat(formatStyle, locale);
        int style = decodeDateFormatStyle(formatStyle);
        if(style > -1)
        {
            dateFormat = DateFormat.getDateInstance(style, locale);
        }
        return dateFormat;
    }


    protected DateFormat createTimeFormat(String arguments, Locale locale)
    {
        String formatStyle = (arguments == null) ? "default" : arguments;
        DateFormat dateFormat = new SimpleDateFormat(formatStyle, locale);
        int style = decodeDateFormatStyle(formatStyle);
        if(style > -1)
        {
            dateFormat = DateFormat.getTimeInstance(style, locale);
        }
        return dateFormat;
    }


    protected int decodeDateFormatStyle(String formatStyle)
    {
        int style = -1;
        if("default".equals(formatStyle))
        {
            style = 2;
        }
        else if("short".equals(formatStyle))
        {
            style = 3;
        }
        else if("medium".equals(formatStyle))
        {
            style = 2;
        }
        else if("long".equals(formatStyle))
        {
            style = 1;
        }
        else if("full".equals(formatStyle))
        {
            style = 0;
        }
        return style;
    }


    protected NumberFormat createChoiceFormat(String arguments)
    {
        try
        {
            return new ChoiceFormat(arguments);
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException("incorrect choice pattern", e);
        }
    }


    @Required
    public L10NService getL10NService()
    {
        return this.l10NService;
    }


    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }


    public EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }


    protected RuleParameterValueNormalizerStrategy getRuleParameterValueNormalizerStrategy()
    {
        return this.ruleParameterValueNormalizerStrategy;
    }


    @Required
    public void setRuleParameterValueNormalizerStrategy(RuleParameterValueNormalizerStrategy ruleParameterValueNormalizerStrategy)
    {
        this.ruleParameterValueNormalizerStrategy = ruleParameterValueNormalizerStrategy;
    }
}
