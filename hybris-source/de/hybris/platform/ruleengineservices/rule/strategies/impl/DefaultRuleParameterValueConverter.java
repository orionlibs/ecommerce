package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConverterException;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterTypeFormatter;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueConverter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleParameterValueConverter implements RuleParameterValueConverter, InitializingBean
{
    protected static final Pattern ENUM_PATTERN = Pattern.compile("^Enum\\((.*)\\)");
    protected static final Pattern LIST_PATTERN = Pattern.compile("^List\\((.*)\\)");
    protected static final Pattern MAP_PATTERN = Pattern.compile("^Map\\((.+),\\s*(.+)\\)");
    private Set<String> supportedTypes;
    private RuleParameterTypeFormatter ruleParameterTypeFormatter;
    private boolean debugMode = false;
    private ObjectReader objectReader;
    private ObjectWriter objectWriter;


    public Set<String> getSupportedTypes()
    {
        return this.supportedTypes;
    }


    @Required
    public void setSupportedTypes(Set<String> supportedTypes)
    {
        this.supportedTypes = supportedTypes;
    }


    public RuleParameterTypeFormatter getRuleParameterTypeFormatter()
    {
        return this.ruleParameterTypeFormatter;
    }


    @Required
    public void setRuleParameterTypeFormatter(RuleParameterTypeFormatter ruleParameterTypeFormatter)
    {
        this.ruleParameterTypeFormatter = ruleParameterTypeFormatter;
    }


    public boolean isDebugMode()
    {
        return this.debugMode;
    }


    public void setDebugMode(boolean debugMode)
    {
        this.debugMode = debugMode;
    }


    protected ObjectReader getObjectReader()
    {
        return this.objectReader;
    }


    protected ObjectWriter getObjectWriter()
    {
        return this.objectWriter;
    }


    public void afterPropertiesSet()
    {
        ObjectMapper objectMapper = ((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)JsonMapper.builder().defaultDateFormat((DateFormat)(new StdDateFormat()).withColonInTimeZone(false))).configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY,
                        this.debugMode)).configure(SerializationFeature.INDENT_OUTPUT, this.debugMode)).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)).serializationInclusion(JsonInclude.Include.NON_NULL)).build();
        Map<Object, Object> attributes = new HashMap<>();
        configureObjectMapper(objectMapper);
        configureAttributes(attributes);
        this.objectReader = objectMapper.reader().withAttributes(attributes);
        this.objectWriter = objectMapper.writer().withAttributes(attributes);
    }


    protected void configureObjectMapper(ObjectMapper objectMapper)
    {
    }


    protected void configureAttributes(Map<Object, Object> attributes)
    {
    }


    public String toString(Object value)
    {
        try
        {
            return getObjectWriter().writeValueAsString(value);
        }
        catch(IOException e)
        {
            throw new RuleConverterException(e);
        }
    }


    public Object fromString(String value, String type)
    {
        if(StringUtils.isEmpty(value))
        {
            return null;
        }
        try
        {
            ObjectReader objReader = getObjectReader();
            JavaType javaType = resolveJavaType(objReader.getTypeFactory(), type);
            return objReader.forType(javaType).readValue(value);
        }
        catch(Exception e)
        {
            throw new RuleConverterException(e);
        }
    }


    protected JavaType resolveJavaType(TypeFactory typeFactory, String type) throws ClassNotFoundException
    {
        if(StringUtils.isEmpty(type))
        {
            throw new RuleConverterException("Type cannot be null");
        }
        String valueType = this.ruleParameterTypeFormatter.formatParameterType(type);
        if(this.supportedTypes.contains(valueType))
        {
            Class<?> typeClass = getClassForType(valueType);
            return typeFactory.constructType(typeClass);
        }
        Matcher enumMatcher = ENUM_PATTERN.matcher(valueType);
        if(enumMatcher.matches())
        {
            Class<?> enumClass = getClassForType(enumMatcher.group(1));
            return typeFactory.constructType(enumClass);
        }
        Matcher listMatcher = LIST_PATTERN.matcher(valueType);
        if(listMatcher.matches())
        {
            Class<?> elementClass = getClassForType(listMatcher.group(1));
            return (JavaType)typeFactory.constructCollectionType(List.class, elementClass);
        }
        Matcher mapMatcher = MAP_PATTERN.matcher(valueType);
        if(mapMatcher.matches())
        {
            Class<?> keyClass = getClassForType(mapMatcher.group(1));
            Class<?> valueClass = getClassForType(mapMatcher.group(2));
            return (JavaType)typeFactory.constructMapType(Map.class, keyClass, valueClass);
        }
        throw new RuleConverterException("Type " + type + " is not supported");
    }


    protected Class<?> getClassForType(String type) throws ClassNotFoundException
    {
        return Class.forName(type, true, getClass().getClassLoader());
    }
}
