package de.hybris.platform.ruleengineservices.compiler;

import java.io.Serializable;

public interface RuleCompilerProblem extends Serializable
{
    Severity getSeverity();


    String getMessage();
}
