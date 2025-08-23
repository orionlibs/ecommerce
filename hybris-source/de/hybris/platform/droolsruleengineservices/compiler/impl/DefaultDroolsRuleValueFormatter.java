package de.hybris.platform.droolsruleengineservices.compiler.impl;

import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleGeneratorContext;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleValueFormatter;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleValueFormatterException;
import de.hybris.platform.ruleengineservices.util.DroolsStringUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Supplier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

public class DefaultDroolsRuleValueFormatter implements DroolsRuleValueFormatter
{
    public static final String NULL_VALUE = "null";
    private final Map<String, DroolsRuleValueFormatterHelper> formatters = new HashMap<>();
    private DroolsStringUtils droolsStringUtils;


    public void initFormatters()
    {
        this.formatters.put(Boolean.class.getName(), (context, value) -> ((Boolean)value).booleanValue() ? "Boolean.TRUE" : "Boolean.FALSE");
        this.formatters.put(Character.class.getName(), (context, value) -> "'" + getDroolsStringUtils().encodeMvelStringLiteral(value) + "'");
        this.formatters.put(String.class.getName(), (context, value) -> "\"" + getDroolsStringUtils().encodeMvelStringLiteral(value) + "\"");
        this.formatters.put(Byte.class.getName(), (context, value) -> "new Byte(" + value + ")");
        this.formatters.put(Short.class.getName(), (context, value) -> "Short.valueOf(" + value + ")");
        this.formatters.put(Integer.class.getName(), (context, value) -> "Integer.valueOf(" + value + ")");
        this.formatters.put(Long.class.getName(), (context, value) -> "Long.valueOf(" + value + ")");
        this.formatters.put(Float.class.getName(), (context, value) -> "Float.valueOf(" + value + ")");
        this.formatters.put(Double.class.getName(), (context, value) -> "Double.valueOf(" + value + ")");
        this.formatters.put(BigInteger.class.getName(), (context, value) -> "new " + context.generateClassName(BigInteger.class) + "(" + value + ")");
        this.formatters.put(BigDecimal.class.getName(), (context, value) -> "new " + context.generateClassName(BigDecimal.class) + "(\"" + value + "\")");
        this.formatters.put(Enum.class.getName(), (context, value) -> value.getClass().getName() + "." + value.getClass().getName());
        this.formatters.put(Date.class.getName(), (context, value) -> "new " + context.generateClassName(Date.class) + "(" + ((Date)value).getTime() + ")");
        this.formatters.put(AbstractList.class.getName(), (context, value) -> {
            StringJoiner joiner = new StringJoiner(", ", "(", ")");
            ((List)value).stream().forEach(());
            return joiner.toString();
        });
        this.formatters.put(AbstractMap.class.getName(), (context, value) -> {
            StringJoiner joiner = new StringJoiner(", ", "[", "]");
            ((Map)value).entrySet().stream().forEach(());
            return joiner.toString();
        });
    }


    public String formatValue(DroolsRuleGeneratorContext context, Object value)
    {
        return formatValue(context, value, Collections::emptyMap);
    }


    protected String formatValue(DroolsRuleGeneratorContext context, Object value, Supplier<Map<String, DroolsRuleValueFormatterHelper>> formattersSupplier)
    {
        if(isNullValue(value))
        {
            return "null";
        }
        Class<?> valueClass = value.getClass();
        do
        {
            DroolsRuleValueFormatterHelper formatter = (DroolsRuleValueFormatterHelper)((Map)formattersSupplier.get()).getOrDefault(valueClass.getName(), getFormatters().get(valueClass.getName()));
            if(formatter != null)
            {
                return formatter.format(context, value);
            }
            valueClass = valueClass.getSuperclass();
        }
        while(Objects.nonNull(valueClass));
        throw new DroolsRuleValueFormatterException("Cannot find the value formatter for an object of type: " + value
                        .getClass().getName());
    }


    protected boolean isNullValue(Object value)
    {
        if(value instanceof Collection)
        {
            return CollectionUtils.isEmpty((Collection)value);
        }
        if(value instanceof Map)
        {
            return MapUtils.isEmpty((Map)value);
        }
        return Objects.isNull(value);
    }


    protected Map<String, DroolsRuleValueFormatterHelper> getFormatters()
    {
        return this.formatters;
    }


    protected DroolsStringUtils getDroolsStringUtils()
    {
        return this.droolsStringUtils;
    }


    public void setDroolsStringUtils(DroolsStringUtils droolsStringUtils)
    {
        this.droolsStringUtils = droolsStringUtils;
    }
}
