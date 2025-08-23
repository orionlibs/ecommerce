package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleData;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterUuidGenerator;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueConverter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractRuleConverter implements InitializingBean
{
    protected static final String RULE_PARAMETER_VALUE_CONVERTER_KEY = "ruleParameterValueConverter";
    private RuleParameterValueConverter ruleParameterValueConverter;
    private RuleParameterUuidGenerator ruleParameterUuidGenerator;
    private boolean debugMode = false;
    private ObjectReader objectReader;
    private ObjectWriter objectWriter;


    public void afterPropertiesSet()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, this.debugMode);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.addMixIn(RuleParameterData.class, RuleParameterDataMixIn.class);
        Map<Object, Object> attributes = new HashMap<>();
        attributes.put("ruleParameterValueConverter", this.ruleParameterValueConverter);
        configureObjectMapper(objectMapper);
        configureAttributes(attributes);
        this.objectReader = objectMapper.reader().withAttributes(attributes);
        this.objectWriter = objectMapper.writer().withAttributes(attributes);
    }


    protected void convertParameters(AbstractRuleData ruleData, AbstractRuleDefinitionData ruleDefinitionData)
    {
        for(Map.Entry<String, RuleParameterDefinitionData> entry : (Iterable<Map.Entry<String, RuleParameterDefinitionData>>)ruleDefinitionData.getParameters().entrySet())
        {
            String parameterId = entry.getKey();
            RuleParameterDefinitionData parameterDefinition = entry.getValue();
            RuleParameterData parameter = (RuleParameterData)ruleData.getParameters().get(parameterId);
            if(parameter == null)
            {
                parameter = new RuleParameterData();
                parameter.setValue(parameterDefinition.getDefaultValue());
                ruleData.getParameters().put(parameterId, parameter);
            }
            else
            {
                Object value = getRuleParameterValueConverter().fromString((String)parameter.getValue(), parameterDefinition
                                .getType());
                parameter.setValue(value);
            }
            parameter.setType(parameterDefinition.getType());
            if(StringUtils.isBlank(parameter.getUuid()))
            {
                parameter.setUuid(getRuleParameterUuidGenerator().generateUuid(parameter, parameterDefinition));
            }
        }
    }


    protected void configureObjectMapper(ObjectMapper objectMapper)
    {
    }


    protected void configureAttributes(Map<Object, Object> attributes)
    {
    }


    protected RuleParameterValueConverter getRuleParameterValueConverter()
    {
        return this.ruleParameterValueConverter;
    }


    @Required
    public void setRuleParameterValueConverter(RuleParameterValueConverter ruleParameterValueConverter)
    {
        this.ruleParameterValueConverter = ruleParameterValueConverter;
    }


    protected RuleParameterUuidGenerator getRuleParameterUuidGenerator()
    {
        return this.ruleParameterUuidGenerator;
    }


    @Required
    public void setRuleParameterUuidGenerator(RuleParameterUuidGenerator ruleParameterUuidGenerator)
    {
        this.ruleParameterUuidGenerator = ruleParameterUuidGenerator;
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
}
