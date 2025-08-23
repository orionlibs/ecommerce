package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultRuleParameterValueMapperStrategy implements RuleParameterValueMapperStrategy, ApplicationContextAware, InitializingBean
{
    protected static final Pattern LIST_PATTERN = Pattern.compile("^List\\((.*)\\)");
    protected static final Pattern MAP_PATTERN = Pattern.compile("^Map\\((.+),\\s*(.+)\\)");
    private Set<String> supportedTypes;
    private ApplicationContext applicationContext;
    private final Map<String, RuleParameterValueMapper> mappers = new HashMap<>();


    public void afterPropertiesSet()
    {
        Map<String, RuleParameterValueTypeDefinition> beans = getApplicationContext().getBeansOfType(RuleParameterValueTypeDefinition.class);
        beans.values().forEach(definition -> this.mappers.put(definition.getType(), definition.getMapper()));
    }


    public Object toRuleParameter(Object value, String type)
    {
        if(value == null)
        {
            return null;
        }
        ServicesUtil.validateParameterNotNull(type, "Type parameter cannot be null");
        if(getSupportedTypes().contains(type) || value instanceof Enum)
        {
            return value;
        }
        RuleParameterValueMapper<Object> mapper = getMappers().get(type);
        if(mapper != null)
        {
            return mapper.toString(value);
        }
        if(value instanceof List)
        {
            return getAsList((List<Object>)value, type, this::toRuleParameter);
        }
        if(value instanceof Map)
        {
            return getAsMap((Map<Object, Object>)value, type, this::toRuleParameter);
        }
        throw new RuleParameterValueMapperException("Type is not supported: [type=" + type + "]");
    }


    protected Map<Object, Object> getAsMap(Map<Object, Object> value, String type, BiFunction<Object, String, Object> valueSupplier)
    {
        Matcher listMatcher = MAP_PATTERN.matcher(type);
        if(!listMatcher.matches())
        {
            throw new RuleParameterValueMapperException("Value is instance of Map but type is not Map(.*,.*): [type=" + type + "]");
        }
        String mapKeyType = listMatcher.group(1);
        String mapValueType = listMatcher.group(2);
        Map<Object, Object> newValue = new HashMap<>();
        for(Map.Entry<Object, Object> entry : value.entrySet())
        {
            if(entry.getKey() != null)
            {
                newValue.put(valueSupplier.apply(entry.getKey(), mapKeyType), valueSupplier.apply(entry.getValue(), mapValueType));
            }
        }
        return newValue;
    }


    protected List<Object> getAsList(List<Object> value, String type, BiFunction<Object, String, Object> valueSupplier)
    {
        Matcher listMatcher = LIST_PATTERN.matcher(type);
        if(!listMatcher.matches())
        {
            throw new RuleParameterValueMapperException("Value is instance of List but type is not List(.*): [type=" + type + "]");
        }
        String listElementType = listMatcher.group(1);
        List<Object> newValue = new ArrayList();
        for(Object listValue : value)
        {
            newValue.add(valueSupplier.apply(listValue, listElementType));
        }
        return newValue;
    }


    public Object fromRuleParameter(Object value, String type)
    {
        if(value == null)
        {
            return null;
        }
        ServicesUtil.validateParameterNotNull(type, "Type parameter cannot be null");
        if(this.supportedTypes.contains(type) || value instanceof Enum)
        {
            return value;
        }
        RuleParameterValueMapper<Object> mapper = getMappers().get(type);
        if(mapper != null)
        {
            if(!(value instanceof String))
            {
                throw new RuleParameterValueMapperException("Value is not instance of String: [class=" + value
                                .getClass().getName() + ", type=" + type + "]");
            }
            return mapper.fromString((String)value);
        }
        if(value instanceof List)
        {
            return getAsList((List<Object>)value, type, this::fromRuleParameter);
        }
        if(value instanceof Map)
        {
            return getAsMap((Map<Object, Object>)value, type, this::fromRuleParameter);
        }
        throw new RuleParameterValueMapperException("Type is not supported: [type=" + type + "]");
    }


    protected Set<String> getSupportedTypes()
    {
        return this.supportedTypes;
    }


    @Required
    public void setSupportedTypes(Set<String> supportedTypes)
    {
        this.supportedTypes = supportedTypes;
    }


    protected ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    protected Map<String, RuleParameterValueMapper> getMappers()
    {
        return this.mappers;
    }
}
