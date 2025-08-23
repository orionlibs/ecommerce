package de.hybris.platform.ruleengineservices.definitions.validation;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesContainer;
import de.hybris.platform.ruleengineservices.compiler.RuleParameterValidator;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleContainersParameterValidator implements RuleParameterValidator
{
    private static final String INVALID_MESSAGE_KEY = "rule.validation.error.containerids.invalid";
    private static final String NOT_EXIST_MESSAGE_KEY = "rule.validation.error.containerids.notexist";
    protected static final Pattern CONTAINER_ID_PATTERN = Pattern.compile("[a-zA-Z0-9_-]*$");
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;


    public void validate(RuleCompilerContext context, AbstractRuleDefinitionData ruleDefinition, RuleParameterData parameter, RuleParameterDefinitionData parameterDefinition)
    {
        if(parameter == null)
        {
            return;
        }
        Map<String, Integer> qualifyingContainers = (Map<String, Integer>)parameter.getValue();
        if(MapUtils.isNotEmpty(qualifyingContainers))
        {
            List<String> invalidContainerIds = new ArrayList<>();
            List<String> notExistContainerIds = new ArrayList<>();
            for(String containerId : qualifyingContainers.keySet())
            {
                if(!isValidContainerId(containerId))
                {
                    invalidContainerIds.add(containerId);
                }
                if(!isContainerExists(context, containerId))
                {
                    notExistContainerIds.add(containerId);
                }
            }
            if(CollectionUtils.isNotEmpty(invalidContainerIds))
            {
                context.addProblem((RuleCompilerProblem)this.ruleCompilerProblemFactory.createParameterProblem(RuleCompilerProblem.Severity.ERROR, "rule.validation.error.containerids.invalid", parameter, parameterDefinition, new Object[] {parameterDefinition
                                .getName(), invalidContainerIds}));
            }
            if(CollectionUtils.isNotEmpty(notExistContainerIds))
            {
                context.addProblem((RuleCompilerProblem)this.ruleCompilerProblemFactory.createParameterProblem(RuleCompilerProblem.Severity.ERROR, "rule.validation.error.containerids.notexist", parameter, parameterDefinition, new Object[] {parameterDefinition
                                .getName(), notExistContainerIds}));
            }
        }
    }


    protected boolean isValidContainerId(String containerId)
    {
        if(StringUtils.isBlank(containerId))
        {
            return false;
        }
        return CONTAINER_ID_PATTERN.matcher(containerId).matches();
    }


    protected boolean isContainerExists(RuleCompilerContext context, String containerId)
    {
        RuleIrVariablesContainer rootContainer = context.getVariablesGenerator().getRootContainer();
        return rootContainer.getChildren().containsKey(containerId);
    }


    public RuleCompilerProblemFactory getRuleCompilerProblemFactory()
    {
        return this.ruleCompilerProblemFactory;
    }


    @Required
    public void setRuleCompilerProblemFactory(RuleCompilerProblemFactory ruleCompilerProblemFactory)
    {
        this.ruleCompilerProblemFactory = ruleCompilerProblemFactory;
    }
}
