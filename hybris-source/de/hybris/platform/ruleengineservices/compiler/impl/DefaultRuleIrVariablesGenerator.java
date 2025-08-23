package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleIrLocalVariablesContainer;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariable;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesContainer;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesGenerator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import org.fest.util.Arrays;

public class DefaultRuleIrVariablesGenerator implements RuleIrVariablesGenerator
{
    public static final String DEFAULT_VARIABLE_PREFIX = "v";
    private int count;
    private final RuleIrVariablesContainer rootContainer;
    private final Deque<RuleIrVariablesContainer> containers;


    public DefaultRuleIrVariablesGenerator()
    {
        this.count = 0;
        this.rootContainer = createNewContainerForId("default", null);
        this.containers = new ArrayDeque<>();
        this.containers.push(this.rootContainer);
    }


    public RuleIrVariablesContainer getRootContainer()
    {
        return this.rootContainer;
    }


    public RuleIrVariablesContainer getCurrentContainer()
    {
        if(this.containers.isEmpty())
        {
            throw new IllegalStateException("There should exist at least one root container but no container found");
        }
        return this.containers.peek();
    }


    public RuleIrVariablesContainer createContainer(String id)
    {
        RuleIrVariablesContainer parentContainer = getCurrentContainer();
        RuleIrVariablesContainer container = createNewContainerForId(id, parentContainer);
        this.containers.push(container);
        return container;
    }


    public void closeContainer()
    {
        if(this.containers.size() == 1)
        {
            throw new IllegalStateException("Root container cannot be closed, only previously created containers can be closed");
        }
        this.containers.pop();
    }


    private static RuleIrVariablesContainer createNewContainerForId(String id, RuleIrVariablesContainer parent)
    {
        RuleIrVariablesContainer container = new RuleIrVariablesContainer();
        container.setName(id);
        container.setVariables(new HashMap<>());
        container.setChildren(new HashMap<>());
        if(parent != null)
        {
            parent.getChildren().put(id, container);
            container.setParent(parent);
            int parentPathLength = (parent.getPath()).length;
            String[] path = (String[])Arrays.copyOf((Object[])parent.getPath(), parentPathLength + 1);
            path[parentPathLength] = id;
            container.setPath(path);
        }
        else
        {
            container.setPath(new String[0]);
        }
        return container;
    }


    public String generateVariable(Class<?> type)
    {
        RuleIrVariablesContainer container = getCurrentContainer();
        RuleIrVariable variable = findVariable(container, type);
        if(variable == null)
        {
            String variableName = generateVariableName(type);
            variable = new RuleIrVariable();
            variable.setName(variableName);
            variable.setType(type);
            variable.setPath(container.getPath());
            container.getVariables().put(variableName, variable);
        }
        return variable.getName();
    }


    protected RuleIrVariable findVariable(RuleIrVariablesContainer container, Class<?> type)
    {
        for(RuleIrVariable variable : container.getVariables().values())
        {
            if(type.equals(variable.getType()))
            {
                return variable;
            }
        }
        if(container.getParent() != null)
        {
            return findVariable(container.getParent(), type);
        }
        return null;
    }


    public RuleIrLocalVariablesContainer createLocalContainer()
    {
        RuleIrLocalVariablesContainer container = new RuleIrLocalVariablesContainer();
        container.setVariables(new HashMap<>());
        return container;
    }


    public String generateLocalVariable(RuleIrLocalVariablesContainer container, Class<?> type)
    {
        String variableName = generateVariableName(type);
        RuleIrVariable variable = new RuleIrVariable();
        variable.setName(variableName);
        variable.setType(type);
        variable.setPath(new String[0]);
        container.getVariables().put(variableName, variable);
        return variableName;
    }


    protected String generateVariableName(Class<?> type)
    {
        this.count++;
        return "v" + this.count;
    }
}
