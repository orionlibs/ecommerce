package de.hybris.platform.ruleengineservices.compiler;

import java.util.List;

public interface RuleCompilerListenersFactory
{
    <T> List<T> getListeners(Class<T> paramClass);
}
