package de.hybris.platform.droolsruleengineservices.compiler.impl;

import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleConditionsGenerator;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleGeneratorContext;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleValueFormatter;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleValueFormatterException;
import de.hybris.platform.ruleengineservices.compiler.AbstractRuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.AbstractRuleIrBooleanCondition;
import de.hybris.platform.ruleengineservices.compiler.AbstractRuleIrPatternCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerRuntimeException;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeRelCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrConditionWithChildren;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExecutableCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExistsCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrNotCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrTypeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariable;
import de.hybris.platform.ruleengineservices.util.DroolsStringUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class DefaultDroolsRuleConditionsGenerator implements DroolsRuleConditionsGenerator
{
    public static final String NON_SUPPORTED_CONDITION = "Not supported RuleIrCondition";
    public static final int BUFFER_SIZE = 4096;
    private static final String GENERATE_UNIQUE_FACT_MATCHING_CONDITION_PROPERTY = "droolsruleengineservices.generate.unique.fact.matching.condition";
    private DroolsRuleValueFormatter droolsRuleValueFormatter;
    private List<String> excludedQueryVariableClassNames;
    private List<Class<?>> typesToSkipOperatorEvaluation;
    private ConfigurationService configurationService;
    private DroolsStringUtils droolsStringUtils;
    private static final String VARIABLE_WITH_NAME = "Variable with name '";
    private static final String NOT_FOUND = "' not found";


    public String generateConditions(DroolsRuleGeneratorContext context, String indentation)
    {
        try
        {
            RuleIr ruleIr = context.getRuleIr();
            return generateConditions((DroolsRuleGeneratorContext)new DroolsRuleConditionsGeneratorContext(context), ruleIr.getConditions(), RuleIrGroupOperator.AND, "", indentation);
        }
        catch(DroolsRuleValueFormatterException e)
        {
            throw new RuleCompilerException(e);
        }
    }


    public String generateRequiredFactsCheckPattern(DroolsRuleGeneratorContext context)
    {
        RuleIr ruleIr = context.getRuleIr();
        return generateWhenConditions((DroolsRuleGeneratorContext)new DroolsRuleConditionsGeneratorContext(context), ruleIr
                        .getConditions(), RuleIrGroupOperator.AND, "", context.getIndentationSize());
    }


    public String generateRequiredTypeVariables(DroolsRuleGeneratorContext context)
    {
        RuleIr ruleIr = context.getRuleIr();
        Map<String, RuleIrTypeCondition> typeConditions = new HashMap<>();
        collectTypeConditions(context, ruleIr.getConditions(), typeConditions);
        StringJoiner conditionsJoiner = new StringJoiner("");
        for(Map.Entry<String, RuleIrTypeCondition> entry : typeConditions.entrySet())
        {
            String variableName = getDroolsStringUtils().validateVariableName(entry.getKey());
            RuleIrVariable variable = findVariable(context, variableName);
            if(variable == null)
            {
                throw new RuleCompilerException("Variable with name '" + variableName + "' not found");
            }
            String variableClassName = getDroolsStringUtils().validateFQCN(context.generateClassName(variable.getType()));
            StringBuilder conditionsBuffer = new StringBuilder(4096);
            conditionsBuffer.append(context.getIndentationSize())
                            .append(context.getVariablePrefix())
                            .append(variableName)
                            .append(" := ")
                            .append(variableClassName).append("()\n");
            conditionsJoiner.add(conditionsBuffer);
        }
        return conditionsJoiner.toString();
    }


    protected void collectTypeConditions(DroolsRuleGeneratorContext context, List<RuleIrCondition> conditions, Map<String, RuleIrTypeCondition> typeConditions)
    {
        RuleIrConditionsByType ruleIrConditionsByType = evaluateRuleConditionType(conditions);
        Map<String, Collection<AbstractRuleIrPatternCondition>> patternConditions = ruleIrConditionsByType.getPatternConditions().asMap();
        for(Map.Entry<String, Collection<AbstractRuleIrPatternCondition>> entry : patternConditions.entrySet())
        {
            if(isVariableTerminal(entry.getKey(), context))
            {
                Objects.requireNonNull(RuleIrTypeCondition.class);
                ((Collection)entry.getValue()).stream().filter(RuleIrTypeCondition.class::isInstance).findFirst()
                                .ifPresent(t -> typeConditions.put((String)entry.getKey(), (RuleIrTypeCondition)t));
            }
        }
        if(CollectionUtils.isNotEmpty(ruleIrConditionsByType.getGroupConditions()))
        {
            ruleIrConditionsByType.getGroupConditions().forEach(c -> collectTypeConditions(context, c.getChildren(), typeConditions));
        }
    }


    protected String generateWhenConditions(DroolsRuleGeneratorContext context, List<RuleIrCondition> conditions, RuleIrGroupOperator operator, String conditionPrefix, String indentation)
    {
        if(CollectionUtils.isEmpty(conditions))
        {
            return "";
        }
        RuleIrConditionsByType ruleIrConditionsByType = evaluateRuleConditionType(conditions);
        ListValuedMap<String, AbstractRuleIrPatternCondition> patternConditions = ruleIrConditionsByType.getPatternConditions();
        List<RuleIrGroupCondition> groupConditions = ruleIrConditionsByType.getGroupConditions();
        List<RuleIrGroupCondition> normalizedGroupConditions = (List<RuleIrGroupCondition>)groupConditions.stream().filter(c -> (filterOutNonGroupConditions(c.getChildren()).size() <= 1 || c.getOperator().compareTo((Enum)RuleIrGroupOperator.OR) != 0)).collect(Collectors.toList());
        int twoChildrenSize = 2;
        normalizedGroupConditions.addAll((Collection<? extends RuleIrGroupCondition>)groupConditions
                        .stream()
                        .filter(c ->
                                        (filterOutNonGroupConditions(c.getChildren()).size() == 2 && c.getOperator().compareTo((Enum)RuleIrGroupOperator.OR) == 0 && doesNotContainTargetCustomerConditions(c, context, indentation)))
                        .collect(Collectors.toList()));
        List<RuleIrNotCondition> notConditions = ruleIrConditionsByType.getNotConditions();
        StringJoiner conditionsJoiner = new StringJoiner("");
        int conditionsCount = patternConditions.size() + normalizedGroupConditions.size() + notConditions.size();
        if(conditionsCount > 0)
        {
            String conditionsIndentation;
            if((operator == null || conditionsCount == 1) && StringUtils.isEmpty(conditionPrefix))
            {
                conditionsIndentation = indentation;
            }
            else
            {
                String operatorAsString = (conditionsCount > 1 && operator != null) ? operator.toString().toLowerCase() : "";
                String delimiter = indentation + context.getIndentationSize() + operatorAsString + "\n";
                String prefix = indentation + ((conditionPrefix == null) ? "" : conditionPrefix) + "(\n";
                String suffix = indentation + ")\n";
                conditionsIndentation = indentation + indentation;
                conditionsJoiner = new StringJoiner(delimiter, prefix, suffix);
                conditionsJoiner.setEmptyValue("");
            }
            if(patternConditions.asMap().values().stream().flatMap(Collection::stream)
                            .anyMatch(condition -> isConditionDependentOnOthers((RuleIrCondition)condition, normalizedGroupConditions, patternConditions.asMap().keySet())))
            {
                generateWhenGroupConditions(context, normalizedGroupConditions, conditionsJoiner, conditionsIndentation);
                generateWhenPatternConditions(context, patternConditions.asMap(), operator, conditionsJoiner, conditionsIndentation);
            }
            else
            {
                generateWhenPatternConditions(context, patternConditions.asMap(), operator, conditionsJoiner, conditionsIndentation);
                generateWhenGroupConditions(context, normalizedGroupConditions, conditionsJoiner, conditionsIndentation);
            }
            generateWhenNotConditions(context, notConditions, conditionsJoiner, conditionsIndentation);
        }
        return conditionsJoiner.toString();
    }


    protected boolean doesNotContainTargetCustomerConditions(RuleIrGroupCondition ruleIrGroupCondition, DroolsRuleGeneratorContext context, String indentation)
    {
        String conditionStr = generateWhenConditions(context, ruleIrGroupCondition.getChildren(), ruleIrGroupCondition.getOperator(), "", indentation);
        if(getOrCountOfStr(conditionStr) == 1)
        {
            int indexval = conditionStr.indexOf("or");
            String firstStr = conditionStr.substring(0, indexval);
            String secondStr = conditionStr.substring(indexval);
            return (firstStr.contains("UserGroupRAO") && firstStr.contains("UserRAO") && secondStr.contains("UserGroupRAO") && secondStr.contains("UserRAO"));
        }
        return false;
    }


    private int getOrCountOfStr(String conditionStr)
    {
        if(StringUtils.isEmpty(conditionStr))
        {
            return 0;
        }
        int count = 0;
        String orStr = "or";
        String tempStr = (new StringBuilder(conditionStr)).toString();
        int index;
        while((index = tempStr.indexOf("or")) > -1)
        {
            count++;
            tempStr = tempStr.substring(index + 1);
        }
        return count;
    }


    protected boolean isConditionDependentOnOthers(RuleIrCondition condition, Collection<? extends RuleIrCondition> others, Collection<String> definedVariables)
    {
        Collection<String> variableNamesToCheck = new HashSet<>();
        if(condition instanceof RuleIrAttributeRelCondition)
        {
            variableNamesToCheck.add(((RuleIrAttributeRelCondition)condition).getTargetVariable());
        }
        else if(condition instanceof RuleIrGroupCondition)
        {
            findVariablesOfPatternConditions(variableNamesToCheck, ((RuleIrGroupCondition)condition).getChildren());
        }
        else
        {
            return false;
        }
        variableNamesToCheck.removeAll(definedVariables);
        return isAnyVariableReferredInConditions(variableNamesToCheck, others);
    }


    protected boolean isAnyVariableReferredInConditions(Collection<String> variableNamesToCheck, Collection<? extends RuleIrCondition> others)
    {
        if(isAnyVariableReferredInAttrRelConditions(variableNamesToCheck, others) ||
                        isAnyVariableReferredInPatternConditions(variableNamesToCheck, others))
        {
            return true;
        }
        Collection<RuleIrCondition> conditionsInGroups = getConditionsInGroups(others);
        if(conditionsInGroups.isEmpty())
        {
            return false;
        }
        return isAnyVariableReferredInConditions(variableNamesToCheck, conditionsInGroups);
    }


    protected boolean isAnyVariableReferredInPatternConditions(Collection<String> variableNamesToCheck, Collection<? extends RuleIrCondition> others)
    {
        Objects.requireNonNull(AbstractRuleIrPatternCondition.class);
        Objects.requireNonNull(variableNamesToCheck);
        return others.stream().filter(AbstractRuleIrPatternCondition.class::isInstance).map(c -> ((AbstractRuleIrPatternCondition)c).getVariable()).filter(Objects::nonNull).anyMatch(variableNamesToCheck::contains);
    }


    protected boolean isAnyVariableReferredInAttrRelConditions(Collection<String> variableNamesToCheck, Collection<? extends RuleIrCondition> others)
    {
        Objects.requireNonNull(RuleIrAttributeRelCondition.class);
        Objects.requireNonNull(variableNamesToCheck);
        return others.stream().filter(RuleIrAttributeRelCondition.class::isInstance).map(c -> ((RuleIrAttributeRelCondition)c).getTargetVariable()).filter(Objects::nonNull).anyMatch(variableNamesToCheck::contains);
    }


    protected void findVariablesOfPatternConditions(Collection<String> variableNames, Collection<RuleIrCondition> conditions)
    {
        Objects.requireNonNull(RuleIrAttributeRelCondition.class);
        variableNames.addAll((Collection<? extends String>)conditions.stream().filter(RuleIrAttributeRelCondition.class::isInstance)
                        .map(c -> ((AbstractRuleIrPatternCondition)c).getVariable()).collect(Collectors.toSet()));
        Collection<RuleIrCondition> conditionsInGroups = getConditionsInGroups(conditions);
        if(!conditionsInGroups.isEmpty())
        {
            findVariablesOfPatternConditions(variableNames, conditionsInGroups);
        }
    }


    protected Collection<RuleIrCondition> getConditionsInGroups(Collection<? extends RuleIrCondition> conditions)
    {
        Objects.requireNonNull(RuleIrConditionWithChildren.class);
        Objects.requireNonNull(RuleIrConditionWithChildren.class);
        return (Collection<RuleIrCondition>)conditions.stream().filter(RuleIrConditionWithChildren.class::isInstance).map(RuleIrConditionWithChildren.class::cast).filter(c -> (c.getChildren() != null)).flatMap(c -> c.getChildren().stream())
                        .collect(Collectors.toSet());
    }


    protected Collection<RuleIrCondition> filterOutNonGroupConditions(Collection<RuleIrCondition> conditions)
    {
        if(CollectionUtils.isNotEmpty(conditions))
        {
            Objects.requireNonNull(RuleIrConditionWithChildren.class);
            return (Collection<RuleIrCondition>)conditions.stream().filter(RuleIrConditionWithChildren.class::isInstance).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    protected String generateConditions(DroolsRuleGeneratorContext context, List<RuleIrCondition> conditions, RuleIrGroupOperator operator, String conditionPrefix, String indentation)
    {
        String conditionsIndentation;
        StringJoiner conditionsJoiner;
        if(CollectionUtils.isEmpty(conditions))
        {
            return "";
        }
        RuleIrConditionsByType ruleIrConditionsList = evaluateRuleConditionType(conditions);
        ListValuedMap<Boolean, AbstractRuleIrBooleanCondition> booleanConditions = ruleIrConditionsList.getBooleanConditions();
        ListValuedMap<String, AbstractRuleIrPatternCondition> patternConditions = ruleIrConditionsList.getPatternConditions();
        List<RuleIrGroupCondition> groupConditions = ruleIrConditionsList.getGroupConditions();
        List<RuleIrExecutableCondition> executableConditions = ruleIrConditionsList.getExecutableConditions();
        List<RuleIrExistsCondition> existsConditions = ruleIrConditionsList.getExistsConditions();
        List<RuleIrNotCondition> notConditions = ruleIrConditionsList.getNotConditions();
        int conditionsCount = booleanConditions.size() + patternConditions.size() + groupConditions.size() + existsConditions.size() + notConditions.size() + executableConditions.size();
        if(conditionsCount == 0)
        {
            return "";
        }
        if((operator == null || conditionsCount == 1) && StringUtils.isEmpty(conditionPrefix))
        {
            conditionsIndentation = indentation;
            conditionsJoiner = new StringJoiner("");
        }
        else
        {
            String operatorAsString = (operator != null) ? operator.toString().toLowerCase() : "";
            String delimiter = indentation + context.getIndentationSize() + operatorAsString + "\n";
            String prefix = indentation + ((conditionPrefix == null) ? "" : conditionPrefix) + "(\n";
            String suffix = indentation + ")\n";
            conditionsIndentation = indentation + indentation;
            conditionsJoiner = new StringJoiner(delimiter, prefix, suffix);
            conditionsJoiner.setEmptyValue("");
        }
        generateBooleanConditions(booleanConditions.asMap(), conditionsJoiner, conditionsIndentation);
        if(patternConditions.asMap().values().stream().flatMap(Collection::stream)
                        .anyMatch(condition -> isConditionDependentOnOthers((RuleIrCondition)condition, groupConditions, patternConditions.asMap().keySet())))
        {
            generateGroupConditions(context, groupConditions, conditionsJoiner, conditionsIndentation);
            generatePatternConditions(context, patternConditions.asMap(), operator, conditionsJoiner, conditionsIndentation);
        }
        else
        {
            generatePatternConditions(context, patternConditions.asMap(), operator, conditionsJoiner, conditionsIndentation);
            generateGroupConditions(context, groupConditions, conditionsJoiner, conditionsIndentation);
        }
        generateExecutableConditions(context, executableConditions, conditionsJoiner, conditionsIndentation);
        generateExistsConditions(context, existsConditions, conditionsJoiner, conditionsIndentation);
        generateNotConditions(context, notConditions, conditionsJoiner, conditionsIndentation);
        return conditionsJoiner.toString();
    }


    protected RuleIrConditionsByType evaluateRuleConditionType(List<RuleIrCondition> conditions)
    {
        RuleIrConditionsByType ruleIrConditionsList = new RuleIrConditionsByType();
        ArrayListValuedHashMap arrayListValuedHashMap1 = new ArrayListValuedHashMap();
        ArrayListValuedHashMap arrayListValuedHashMap2 = new ArrayListValuedHashMap();
        List<RuleIrGroupCondition> groupConditions = new ArrayList<>();
        List<RuleIrExecutableCondition> executableConditions = new ArrayList<>();
        List<RuleIrExistsCondition> existsConditions = new ArrayList<>();
        List<RuleIrNotCondition> notConditions = new ArrayList<>();
        for(RuleIrCondition ruleIrCondition : conditions)
        {
            if(ruleIrCondition instanceof de.hybris.platform.ruleengineservices.compiler.RuleIrTrueCondition)
            {
                arrayListValuedHashMap1.put(Boolean.TRUE, ruleIrCondition);
                continue;
            }
            if(ruleIrCondition instanceof de.hybris.platform.ruleengineservices.compiler.RuleIrFalseCondition)
            {
                arrayListValuedHashMap1.put(Boolean.FALSE, ruleIrCondition);
                continue;
            }
            if(ruleIrCondition instanceof AbstractRuleIrPatternCondition)
            {
                AbstractRuleIrPatternCondition ruleIrPatternCondition = (AbstractRuleIrPatternCondition)ruleIrCondition;
                arrayListValuedHashMap2.put(ruleIrPatternCondition.getVariable(), ruleIrPatternCondition);
                continue;
            }
            if(ruleIrCondition instanceof RuleIrGroupCondition)
            {
                groupConditions.add((RuleIrGroupCondition)ruleIrCondition);
                continue;
            }
            if(ruleIrCondition instanceof RuleIrExistsCondition)
            {
                existsConditions.add((RuleIrExistsCondition)ruleIrCondition);
                continue;
            }
            if(ruleIrCondition instanceof RuleIrNotCondition)
            {
                notConditions.add((RuleIrNotCondition)ruleIrCondition);
                continue;
            }
            if(ruleIrCondition instanceof RuleIrExecutableCondition)
            {
                executableConditions.add((RuleIrExecutableCondition)ruleIrCondition);
                continue;
            }
            if(ruleIrCondition instanceof de.hybris.platform.ruleengineservices.compiler.RuleIrEmptyCondition)
            {
                continue;
            }
            throw new RuleCompilerException("Not supported RuleIrCondition");
        }
        ruleIrConditionsList.setBooleanConditions((ListValuedMap)arrayListValuedHashMap1);
        ruleIrConditionsList.setPatternConditions((ListValuedMap)arrayListValuedHashMap2);
        ruleIrConditionsList.setGroupConditions(groupConditions);
        ruleIrConditionsList.setExecutableConditions(executableConditions);
        ruleIrConditionsList.setExistsConditions(existsConditions);
        ruleIrConditionsList.setNotConditions(notConditions);
        return ruleIrConditionsList;
    }


    protected void generateBooleanConditions(Map<Boolean, Collection<AbstractRuleIrBooleanCondition>> booleanConditions, StringJoiner conditionsJoiner, String indentation)
    {
        for(Map.Entry<Boolean, Collection<AbstractRuleIrBooleanCondition>> entry : booleanConditions.entrySet())
        {
            if(Boolean.TRUE.equals(entry.getKey()))
            {
                conditionsJoiner.add(indentation + "eval(true)\n");
                continue;
            }
            if(Boolean.FALSE.equals(entry.getKey()))
            {
                conditionsJoiner.add(indentation + "eval(false)\n");
                continue;
            }
            throw new RuleCompilerException("Not supported RuleIrCondition");
        }
    }


    protected void generateWhenPatternConditions(DroolsRuleGeneratorContext context, Map<String, Collection<AbstractRuleIrPatternCondition>> patternConditions, RuleIrGroupOperator groupOperator, StringJoiner conditionsJoiner, String indentation)
    {
        Set<Dependency> dependencies = buildDependencies(patternConditions);
        Map<String, Collection<AbstractRuleIrPatternCondition>> sortedPatternConditions = new TreeMap<>((Comparator<? super String>)new DependencyComparator(dependencies));
        sortedPatternConditions.putAll(patternConditions);
        for(Map.Entry<String, Collection<AbstractRuleIrPatternCondition>> entry : sortedPatternConditions.entrySet())
        {
            String separator = (groupOperator == RuleIrGroupOperator.AND) ? ", " : " || ";
            StringBuilder conditionsBuffer = new StringBuilder(4096);
            String variableName = getDroolsStringUtils().validateVariableName(entry.getKey());
            RuleIrVariable variable = findVariable(context, variableName);
            if(variable == null)
            {
                throw new RuleCompilerException("Variable with name '" + variableName + "' not found");
            }
            String variableClassName = getDroolsStringUtils().validateFQCN(context.generateClassName(variable.getType()));
            Supplier<String> variablePrefixSupplier = () -> context.getVariablePrefix() + "rao_";
            boolean variableIsTerminal = isVariableTerminal(variableName, context);
            if(variableIsTerminal && ((Collection)entry.getValue()).stream().noneMatch(c -> !(c instanceof RuleIrTypeCondition)))
            {
                continue;
            }
            if(variableIsTerminal)
            {
                conditionsBuffer.append(indentation).append("exists (").append(variableClassName).append('(');
            }
            else
            {
                conditionsBuffer.append(indentation).append(variablePrefixSupplier.get()).append(variableName).append(" := ")
                                .append(variableClassName).append('(');
                ((DroolsRuleConditionsGeneratorContext)context).getGeneratedVariableNames().add(variableName);
            }
            generateUniqueFactMatchingCondition(context, variable, conditionsBuffer, variablePrefixSupplier);
            Objects.requireNonNull(AbstractRuleIrAttributeCondition.class);
            Objects.requireNonNull(AbstractRuleIrAttributeCondition.class);
            ((Collection)entry.getValue()).stream().filter(AbstractRuleIrAttributeCondition.class::isInstance).map(AbstractRuleIrAttributeCondition.class::cast)
                            .forEach(c -> conditionsBuffer.append(evaluateAttributeNameAndOperator(c)).append(evaluatePatternConditionType(context, (AbstractRuleIrPatternCondition)c, variablePrefixSupplier)).append(separator));
            String conditionsBufferStr = conditionsBuffer.toString();
            conditionsBufferStr = conditionsBufferStr.endsWith(separator) ? conditionsBufferStr.substring(0, conditionsBufferStr.length() - separator.length()) : conditionsBufferStr;
            String conditionTerminator = ")\n";
            if(variableIsTerminal)
            {
                conditionTerminator = "))\n";
            }
            conditionsJoiner.add(conditionsBufferStr + conditionsBufferStr);
        }
    }


    protected boolean isVariableTerminal(String variableName, DroolsRuleGeneratorContext context)
    {
        return isVariableTerminal(variableName, context.getRuleIr().getConditions());
    }


    protected boolean isVariableTerminal(String variableName, Collection<RuleIrCondition> conditions)
    {
        Objects.requireNonNull(RuleIrAttributeRelCondition.class);
        Objects.requireNonNull(variableName);
        if(conditions.stream().filter(RuleIrAttributeRelCondition.class::isInstance).map(c -> ((RuleIrAttributeRelCondition)c).getTargetVariable()).filter(Objects::nonNull).anyMatch(variableName::equals))
        {
            return false;
        }
        Collection<RuleIrCondition> conditionsInGroups = getConditionsInGroups(conditions);
        if(conditionsInGroups.isEmpty())
        {
            return true;
        }
        return isVariableTerminal(variableName, conditionsInGroups);
    }


    protected void generatePatternConditions(DroolsRuleGeneratorContext context, Map<String, Collection<AbstractRuleIrPatternCondition>> patternConditions, RuleIrGroupOperator groupOperator, StringJoiner conditionsJoiner, String indentation)
    {
        Set<Dependency> dependencies = buildDependencies(patternConditions);
        Map<String, Collection<AbstractRuleIrPatternCondition>> sortedPatternConditions = new TreeMap<>((Comparator<? super String>)new DependencyComparator(dependencies));
        sortedPatternConditions.putAll(patternConditions);
        for(Map.Entry<String, Collection<AbstractRuleIrPatternCondition>> entry : sortedPatternConditions.entrySet())
        {
            if(isVariableTerminal(entry.getKey(), context) && ((Collection)entry
                            .getValue()).stream().noneMatch(c -> !(c instanceof RuleIrTypeCondition)))
            {
                continue;
            }
            String variableName = getDroolsStringUtils().validateVariableName(entry.getKey());
            RuleIrVariable variable = findVariable(context, variableName);
            if(variable == null)
            {
                throw new RuleCompilerException("Variable with name '" + variableName + "' not found");
            }
            String variableClassName = getDroolsStringUtils().validateFQCN(context.generateClassName(variable.getType()));
            if(getExcludedQueryVariableClassNames().contains(variableClassName))
            {
                continue;
            }
            String separator = (groupOperator == RuleIrGroupOperator.AND) ? ", " : " || ";
            StringBuilder conditionsBuffer = new StringBuilder(4096);
            conditionsBuffer.append(indentation).append(context.getVariablePrefix()).append(variableName).append(" := ")
                            .append(variableClassName).append('(');
            Objects.requireNonNull(context);
            generateUniqueFactMatchingCondition(context, variable, conditionsBuffer, context::getVariablePrefix);
            Objects.requireNonNull(AbstractRuleIrAttributeCondition.class);
            Objects.requireNonNull(AbstractRuleIrAttributeCondition.class);
            ((Collection)entry.getValue()).stream().filter(AbstractRuleIrAttributeCondition.class::isInstance).map(AbstractRuleIrAttributeCondition.class::cast)
                            .forEach(c -> conditionsBuffer.append(evaluateAttributeNameAndOperator(c)).append(evaluatePatternConditionType(context, (AbstractRuleIrPatternCondition)c)).append(separator));
            String conditionsBufferStr = conditionsBuffer.toString();
            conditionsBufferStr = conditionsBufferStr.endsWith(separator) ? conditionsBufferStr.substring(0, conditionsBufferStr.length() - separator.length()) : conditionsBufferStr;
            conditionsJoiner.add(conditionsBufferStr + ")\n");
            ((DroolsRuleConditionsGeneratorContext)context).getGeneratedVariableNames().add(variableName);
        }
    }


    protected String evaluateAttributeNameAndOperator(AbstractRuleIrAttributeCondition condition)
    {
        if(condition instanceof RuleIrAttributeCondition)
        {
            RuleIrAttributeCondition attributeCondition = (RuleIrAttributeCondition)condition;
            if(getTypesToSkipOperatorEvaluation().stream().anyMatch(t -> t.isInstance(attributeCondition.getValue())))
            {
                return "";
            }
        }
        return condition.getAttribute().concat(" ").concat(condition.getOperator().getValue()).concat(" ");
    }


    protected String evaluatePatternConditionType(DroolsRuleGeneratorContext context, AbstractRuleIrPatternCondition patternCondition)
    {
        Objects.requireNonNull(context);
        return evaluatePatternConditionType(context, patternCondition, context::getVariablePrefix);
    }


    protected String evaluatePatternConditionType(DroolsRuleGeneratorContext context, AbstractRuleIrPatternCondition patternCondition, Supplier<String> variablePrefixSupplier)
    {
        if(patternCondition instanceof RuleIrAttributeCondition)
        {
            return getDroolsRuleValueFormatter().formatValue(context, patternCondition);
        }
        if(patternCondition instanceof RuleIrAttributeRelCondition)
        {
            RuleIrAttributeRelCondition attributeRelCondition = (RuleIrAttributeRelCondition)patternCondition;
            String targetVariableName = attributeRelCondition.getTargetVariable();
            if(Objects.isNull(findVariable(context, targetVariableName)))
            {
                throw new RuleCompilerRuntimeException("Variable with name '" + targetVariableName + "' not found");
            }
            String result = (String)variablePrefixSupplier.get() + (String)variablePrefixSupplier.get();
            if(StringUtils.isNotEmpty(attributeRelCondition.getTargetAttribute()))
            {
                result = result + result + context.getAttributeDelimiter();
            }
            return result;
        }
        throw new RuleCompilerRuntimeException("Not supported RuleIrCondition");
    }


    protected void generateUniqueFactMatchingCondition(DroolsRuleGeneratorContext context, RuleIrVariable variable, StringBuilder conditionsBuffer, Supplier<String> variablePrefixSupplier)
    {
        if(getConfigurationService().getConfiguration().getBoolean("droolsruleengineservices.generate.unique.fact.matching.condition", true))
        {
            boolean variableIsLocal = context.getLocalVariables().stream().map(Map::keySet).flatMap(Collection::stream).anyMatch(v -> v.equals(variable.getName()));
            if(!variableIsLocal)
            {
                context.getVariables().entrySet().stream()
                                .filter(e -> ((DroolsRuleConditionsGeneratorContext)context).getGeneratedVariableNames().contains(e.getKey()))
                                .filter(e -> (((RuleIrVariable)e.getValue()).getType().equals(variable.getType()) && !((String)e.getKey()).equals(variable.getName())))
                                .filter(e -> (NumberUtils.toInt(StringUtils.getDigits((String)e.getKey()), 0) < NumberUtils.toInt(StringUtils.getDigits(variable.getName()), 0)))
                                .forEach(e -> conditionsBuffer.append("this != ").append(variablePrefixSupplier.get()).append((String)e.getKey()).append(", "));
            }
        }
    }


    protected void generateWhenGroupConditions(DroolsRuleGeneratorContext context, List<RuleIrGroupCondition> groupConditions, StringJoiner conditionsJoiner, String indentation)
    {
        generateGroupConditions(groupConditions, c -> generateWhenConditions(context, c.getChildren(), c.getOperator(), "", indentation), conditionsJoiner);
    }


    protected void generateGroupConditions(DroolsRuleGeneratorContext context, List<RuleIrGroupCondition> groupConditions, StringJoiner conditionsJoiner, String indentation)
    {
        generateGroupConditions(groupConditions, c -> generateConditions(context, c.getChildren(), c.getOperator(), "", indentation), conditionsJoiner);
    }


    protected void generateGroupConditions(List<RuleIrGroupCondition> groupConditions, Function<RuleIrGroupCondition, String> generateConditionsFunction, StringJoiner conditionsJoiner)
    {
        for(RuleIrGroupCondition groupCondition : groupConditions)
        {
            if(groupCondition.getOperator() == null)
            {
                throw new RuleCompilerException("Group operator cannot be null");
            }
            if(CollectionUtils.isNotEmpty(groupCondition.getChildren()))
            {
                String generatedConditions = generateConditionsFunction.apply(groupCondition);
                if(StringUtils.isNotEmpty(generatedConditions))
                {
                    conditionsJoiner.add(generatedConditions);
                }
            }
        }
    }


    protected void generateExistsConditions(DroolsRuleGeneratorContext context, List<RuleIrExistsCondition> existsConditions, StringJoiner conditionsJoiner, String indentation)
    {
        for(RuleIrExistsCondition exitsCondition : existsConditions)
        {
            if(CollectionUtils.isNotEmpty(exitsCondition.getChildren()))
            {
                if(exitsCondition.getVariablesContainer() != null)
                {
                    context.addLocalVariables(exitsCondition.getVariablesContainer().getVariables());
                }
                String generatedConditions = generateConditions(context, exitsCondition.getChildren(), RuleIrGroupOperator.AND, "exists ", indentation);
                conditionsJoiner.add(generatedConditions);
                if(exitsCondition.getVariablesContainer() != null)
                {
                    context.getLocalVariables().pollFirst();
                }
            }
        }
    }


    protected void generateNotConditions(DroolsRuleGeneratorContext context, List<RuleIrNotCondition> notConditions, StringJoiner conditionsJoiner, String indentation)
    {
        generateNotConditions(context, notConditions, notCondition -> generateConditions(context, notCondition.getChildren(), RuleIrGroupOperator.AND, "not ", indentation), conditionsJoiner);
    }


    protected void generateWhenNotConditions(DroolsRuleGeneratorContext context, List<RuleIrNotCondition> notConditions, StringJoiner conditionsJoiner, String indentation)
    {
        generateNotConditions(context, notConditions, notCondition -> generateWhenConditions(context, notCondition.getChildren(), RuleIrGroupOperator.AND, "not ", indentation), conditionsJoiner);
    }


    protected void generateNotConditions(DroolsRuleGeneratorContext context, List<RuleIrNotCondition> notConditions, Function<RuleIrNotCondition, String> generateConditionsSupplier, StringJoiner conditionsJoiner)
    {
        for(RuleIrNotCondition notCondition : notConditions)
        {
            if(CollectionUtils.isNotEmpty(notCondition.getChildren()))
            {
                if(notCondition.getVariablesContainer() != null)
                {
                    context.addLocalVariables(notCondition.getVariablesContainer().getVariables());
                }
                String generatedConditions = generateConditionsSupplier.apply(notCondition);
                if(StringUtils.isNotEmpty(generatedConditions))
                {
                    conditionsJoiner.add(generatedConditions);
                }
                if(notCondition.getVariablesContainer() != null)
                {
                    context.getLocalVariables().pollFirst();
                }
            }
        }
    }


    protected void generateExecutableConditions(DroolsRuleGeneratorContext context, List<RuleIrExecutableCondition> executableConditions, StringJoiner conditionsJoiner, String indentation)
    {
    }


    protected RuleIrVariable findVariable(DroolsRuleGeneratorContext context, String variableName)
    {
        for(Map<String, RuleIrVariable> variables : (Iterable<Map<String, RuleIrVariable>>)context.getLocalVariables())
        {
            RuleIrVariable variable = variables.get(variableName);
            if(variable != null)
            {
                return variable;
            }
        }
        return (RuleIrVariable)context.getVariables().get(variableName);
    }


    protected Set<Dependency> buildDependencies(Map<String, Collection<AbstractRuleIrPatternCondition>> patternConditions)
    {
        Set<Dependency> dependencies = new HashSet<>();
        for(Collection<AbstractRuleIrPatternCondition> conditions : patternConditions.values())
        {
            for(AbstractRuleIrPatternCondition condition : conditions)
            {
                if(condition instanceof RuleIrAttributeRelCondition)
                {
                    RuleIrAttributeRelCondition attributeRelCondition = (RuleIrAttributeRelCondition)condition;
                    Dependency dependency = new Dependency(attributeRelCondition.getVariable(), attributeRelCondition.getTargetVariable());
                    dependencies.add(dependency);
                }
            }
        }
        expandDependencies(dependencies);
        return dependencies;
    }


    protected void expandDependencies(Set<Dependency> dependencies)
    {
        Set<Dependency> newDependencies = new HashSet<>();
        for(Dependency dependency1 : dependencies)
        {
            String source = dependency1.source;
            for(Dependency dependency2 : dependencies)
            {
                if(dependency2.source.equals(dependency1.target))
                {
                    String target = dependency2.target;
                    newDependencies.add(new Dependency(source, target));
                }
            }
        }
        if(dependencies.addAll(newDependencies))
        {
            expandDependencies(dependencies);
        }
    }


    public DroolsRuleValueFormatter getDroolsRuleValueFormatter()
    {
        return this.droolsRuleValueFormatter;
    }


    public void setDroolsRuleValueFormatter(DroolsRuleValueFormatter droolsRuleValueFormatter)
    {
        this.droolsRuleValueFormatter = droolsRuleValueFormatter;
    }


    protected List<String> getExcludedQueryVariableClassNames()
    {
        return this.excludedQueryVariableClassNames;
    }


    public void setExcludedQueryVariableClassNames(List<String> excludedQueryVariableClassNames)
    {
        this.excludedQueryVariableClassNames = excludedQueryVariableClassNames;
    }


    protected List<Class<?>> getTypesToSkipOperatorEvaluation()
    {
        return this.typesToSkipOperatorEvaluation;
    }


    public void setTypesToSkipOperatorEvaluation(List<Class<?>> typesToSkipOperatorEvaluation)
    {
        this.typesToSkipOperatorEvaluation = typesToSkipOperatorEvaluation;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
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
