package de.hybris.platform.ruleengineservices.rule.evaluation;

import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface RuleActionContext
{
    String getMetaData(String paramString);


    void scheduleForUpdate(Object... paramVarArgs);


    void updateScheduledFacts();


    <T> T getValue(Class<T> paramClass);


    <T> Set<T> getValues(Class<T> paramClass);


    <T> T getValue(Class<T> paramClass, String... paramVarArgs);


    <T> Set<T> getValues(Class<T> paramClass, String... paramVarArgs);


    void insertFacts(Object... paramVarArgs);


    void insertFacts(Collection paramCollection);


    void updateFacts(Object... paramVarArgs);


    CartRAO getCartRao();


    RuleEngineResultRAO getRuleEngineResultRao();


    String getRuleName();


    Map<String, Object> getRuleMetadata();


    Object getDelegate();


    Optional<String> getRulesModuleName();


    Map<String, Object> getParameters();


    Object getParameter(String paramString);


    <T> T getParameter(String paramString, Class<T> paramClass);


    void setParameters(Map<String, Object> paramMap);


    void halt();
}
