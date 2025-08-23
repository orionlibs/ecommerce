package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import com.google.common.collect.Maps;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueNormalizer;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueNormalizerStrategy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultRuleParameterValueNormalizerStrategy implements RuleParameterValueNormalizerStrategy, ApplicationContextAware, InitializingBean
{
    private final Map<String, RuleParameterValueNormalizer> valueNormalizers = new HashMap<>();
    private ApplicationContext applicationContext;


    public Object normalize(Object value, String type)
    {
        Object result = value;
        Objects.requireNonNull(type);
        Collection<RuleParameterValueNormalizer> normalizers = Maps.filterKeys(getValueNormalizers(), type::contains).values();
        if(!normalizers.isEmpty())
        {
            for(RuleParameterValueNormalizer n : normalizers)
            {
                result = n.normalize(result);
            }
        }
        return result;
    }


    public void afterPropertiesSet()
    {
        Map<String, RuleParameterValueTypeDefinition> beans = getApplicationContext().getBeansOfType(RuleParameterValueTypeDefinition.class);
        beans.entrySet().stream().filter(e -> Objects.nonNull(((RuleParameterValueTypeDefinition)e.getValue()).getNormalizer())).map(Map.Entry::getValue)
                        .forEach(definition -> this.valueNormalizers.put(definition.getType(), definition.getNormalizer()));
    }


    protected Map<String, RuleParameterValueNormalizer> getValueNormalizers()
    {
        return this.valueNormalizers;
    }


    protected ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
