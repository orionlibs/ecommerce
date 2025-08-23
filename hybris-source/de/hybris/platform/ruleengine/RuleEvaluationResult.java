package de.hybris.platform.ruleengine;

import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import java.io.Serializable;
import java.util.Set;

public class RuleEvaluationResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    private boolean evaluationFailed;
    private Set<Object> facts;
    private String errorMessage;
    private Object executionResult;
    private RuleEngineResultRAO result;


    public void setEvaluationFailed(boolean evaluationFailed)
    {
        this.evaluationFailed = evaluationFailed;
    }


    public boolean isEvaluationFailed()
    {
        return this.evaluationFailed;
    }


    public void setFacts(Set<Object> facts)
    {
        this.facts = facts;
    }


    public Set<Object> getFacts()
    {
        return this.facts;
    }


    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    public String getErrorMessage()
    {
        return this.errorMessage;
    }


    public void setExecutionResult(Object executionResult)
    {
        this.executionResult = executionResult;
    }


    public Object getExecutionResult()
    {
        return this.executionResult;
    }


    public void setResult(RuleEngineResultRAO result)
    {
        this.result = result;
    }


    public RuleEngineResultRAO getResult()
    {
        return this.result;
    }
}
