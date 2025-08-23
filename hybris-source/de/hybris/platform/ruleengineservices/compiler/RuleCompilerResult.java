package de.hybris.platform.ruleengineservices.compiler;

import java.io.Serializable;
import java.util.List;

public interface RuleCompilerResult extends Serializable
{
    String getRuleCode();


    long getRuleVersion();


    Result getResult();


    List<RuleCompilerProblem> getProblems();
}
