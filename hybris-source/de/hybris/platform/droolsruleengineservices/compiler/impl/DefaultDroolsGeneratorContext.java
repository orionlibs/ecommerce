package de.hybris.platform.droolsruleengineservices.compiler.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleGeneratorContext;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariable;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesContainer;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ClassUtils;

public class DefaultDroolsGeneratorContext implements DroolsRuleGeneratorContext
{
    public static final String DEFAULT_INDENTATION_SIZE = "   ";
    public static final String DEFAULT_VARIABLE_PREFIX = "$";
    public static final String DEFAULT_ATTRIBUTE_DELIMITER = ".";
    private final RuleCompilerContext ruleCompilerContext;
    private final RuleIr ruleIr;
    private DroolsRuleModel droolsRule;
    private final Deque<Map<String, RuleIrVariable>> localVariables;
    private final Map<String, Class<?>> imports;
    private final Map<String, Class<?>> globals;
    private Map<String, RuleIrVariable> variables;


    public DefaultDroolsGeneratorContext(RuleCompilerContext ruleCompilerContext, RuleIr ruleIr, DroolsRuleModel droolsRule)
    {
        this.ruleCompilerContext = ruleCompilerContext;
        this.ruleIr = ruleIr;
        this.droolsRule = droolsRule;
        this.localVariables = new ConcurrentLinkedDeque<>();
        this.imports = Maps.newConcurrentMap();
        this.globals = Maps.newConcurrentMap();
    }


    public String getIndentationSize()
    {
        return "   ";
    }


    public String getVariablePrefix()
    {
        return "$";
    }


    public String getAttributeDelimiter()
    {
        return ".";
    }


    public RuleCompilerContext getRuleCompilerContext()
    {
        return this.ruleCompilerContext;
    }


    public RuleIr getRuleIr()
    {
        return this.ruleIr;
    }


    public DroolsRuleModel getDroolsRule()
    {
        return this.droolsRule;
    }


    public Map<String, RuleIrVariable> getVariables()
    {
        if(this.variables == null)
        {
            this.variables = Maps.newConcurrentMap();
            populateVariables(this.ruleIr.getVariablesContainer());
        }
        return this.variables;
    }


    public Deque<Map<String, RuleIrVariable>> getLocalVariables()
    {
        return this.localVariables;
    }


    public void addLocalVariables(Map<String, RuleIrVariable> ruleIrVariables)
    {
        this.localVariables.offerFirst(ruleIrVariables);
    }


    public Set<Class<?>> getImports()
    {
        return (Set<Class<?>>)ImmutableSet.copyOf(this.imports.values());
    }


    public Map<String, Class<?>> getGlobals()
    {
        return (Map<String, Class<?>>)ImmutableMap.copyOf(this.globals);
    }


    public String generateClassName(Class<?> type)
    {
        String shortClassName = ClassUtils.getShortClassName(type);
        Class<?> existingType = this.imports.get(shortClassName);
        if(existingType == null)
        {
            this.imports.put(shortClassName, type);
            return shortClassName;
        }
        if(existingType.equals(type))
        {
            return shortClassName;
        }
        return type.getName();
    }


    public void addGlobal(String name, Class<?> type)
    {
        this.globals.put(name, type);
    }


    protected void populateVariables(RuleIrVariablesContainer variablesContainer)
    {
        if(MapUtils.isNotEmpty(variablesContainer.getVariables()))
        {
            for(RuleIrVariable variable : variablesContainer.getVariables().values())
            {
                this.variables.put(variable.getName(), variable);
            }
        }
        if(MapUtils.isNotEmpty(variablesContainer.getChildren()))
        {
            for(RuleIrVariablesContainer childVariablesContainer : variablesContainer.getChildren().values())
            {
                populateVariables(childVariablesContainer);
            }
        }
    }
}
