package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleIrProcessor;
import de.hybris.platform.ruleengineservices.compiler.RuleIrProcessorFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultRuleIrProcessorFactory implements RuleIrProcessorFactory, ApplicationContextAware
{
    private List<RuleIrProcessor> ruleIrProcessors;
    private ApplicationContext applicationContext;


    public List<RuleIrProcessor> getRuleIrProcessors()
    {
        return Collections.unmodifiableList(this.ruleIrProcessors);
    }


    public void setApplicationContext(ApplicationContext ctx)
    {
        this.applicationContext = ctx;
        this.ruleIrProcessors = loadRuleIrProcessors();
    }


    protected List<RuleIrProcessor> loadRuleIrProcessors()
    {
        List<RuleIrProcessor> ruleIrProcessorList = new ArrayList<>();
        Map<String, RuleIrProcessorDefinition> processorsDefinitionsMap = this.applicationContext.getBeansOfType(RuleIrProcessorDefinition.class);
        List<RuleIrProcessorDefinition> processorDefinitions = new ArrayList<>(processorsDefinitionsMap.values());
        Collections.sort(processorDefinitions);
        processorDefinitions.forEach(definition -> ruleIrProcessorList.add(definition.getRuleIrProcessor()));
        return ruleIrProcessorList;
    }
}
