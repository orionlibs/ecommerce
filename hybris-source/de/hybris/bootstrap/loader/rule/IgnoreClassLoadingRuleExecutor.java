package de.hybris.bootstrap.loader.rule;

import de.hybris.bootstrap.loader.metrics.ClassLoaderMetricEvent;
import de.hybris.bootstrap.loader.metrics.ClassLoaderMetricEventListener;
import de.hybris.bootstrap.loader.metrics.EventType;
import de.hybris.bootstrap.loader.rule.internal.IgnoreClassLoaderRuleParam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class IgnoreClassLoadingRuleExecutor
{
    private List<IgnoreClassLoadingRule> ignoreClassLoadingRules = new ArrayList<>();
    private final List<IgnoreClassLoaderRuleListener> registeredEventReceivers = new ArrayList<>();
    private final List<ClassLoaderMetricEventListener> classIgnoredEventListeners = new CopyOnWriteArrayList<>();


    public static IgnoreClassLoadingRuleExecutor createRuleFilterClassListExecutorWithFilterList(List<IgnoreClassLoadingRule> ignoreClassLoadingRules)
    {
        IgnoreClassLoadingRuleExecutor executor = new IgnoreClassLoadingRuleExecutor();
        executor.ignoreClassLoadingRules = ignoreClassLoadingRules;
        executor.registerListeners();
        return executor;
    }


    public static IgnoreClassLoadingRuleExecutor createRuleFilterClassListExecutorWithRuleParams(List<IgnoreClassLoaderRuleParam> ruleList)
    {
        IgnoreClassLoadingRuleExecutor executor = new IgnoreClassLoadingRuleExecutor();
        executor.initialize(ruleList);
        executor.registerListeners();
        return executor;
    }


    private void initialize(List<IgnoreClassLoaderRuleParam> ruleList)
    {
        Collections.sort(ruleList, Comparator.comparing(IgnoreClassLoaderRuleParam::getOrder));
        for(IgnoreClassLoaderRuleParam rule : ruleList)
        {
            try
            {
                Object objectClass = Class.forName(rule.getClassName()).newInstance();
                if(objectClass instanceof IgnoreClassLoadingRule)
                {
                    IgnoreClassLoadingRule ignoreRule = (IgnoreClassLoadingRule)objectClass;
                    ignoreRule.initialize(rule.getParams());
                    this.ignoreClassLoadingRules.add(ignoreRule);
                    continue;
                }
                throw new IllegalArgumentException("Class  " + rule
                                .getClassName() + " is not instanceof " + IgnoreClassLoadingRule.class.getSimpleName());
            }
            catch(ClassNotFoundException | IllegalAccessException | InstantiationException e)
            {
                throw new IllegalArgumentException("Cannot instantiate class " + rule.getClassName(), e);
            }
        }
    }


    private void registerListeners()
    {
        this.ignoreClassLoadingRules.stream().filter(rule -> rule instanceof IgnoreClassLoaderRuleListener)
                        .forEach(rule -> this.registeredEventReceivers.add((IgnoreClassLoaderRuleListener)rule));
    }


    List<IgnoreClassLoadingRule> getFilterChainList()
    {
        return Collections.unmodifiableList(this.ignoreClassLoadingRules);
    }


    public boolean shouldClassBeSkipped(String className)
    {
        for(IgnoreClassLoadingRule iclr : this.ignoreClassLoadingRules)
        {
            if(IgnoreClassLoadingRule.IgnoredStatus.IGNORED.equals(iclr.isIgnored(className)))
            {
                publishClassIgnoredEvents(className, iclr.getClass().getName());
                return true;
            }
            if(IgnoreClassLoadingRule.IgnoredStatus.WHITELISTED.equals(iclr.isIgnored(className)))
            {
                break;
            }
        }
        return false;
    }


    public void publishEvent(IgnoreClassLoadingEvent event)
    {
        this.registeredEventReceivers.forEach(receiver -> receiver.onEvent(event));
    }


    private void publishClassIgnoredEvents(String className, String ruleName)
    {
        ClassLoaderMetricEvent event = ClassLoaderMetricEvent.forClass().ofEventType(EventType.REJECTED).withName(className).withRejectionRule(ruleName).build();
        this.classIgnoredEventListeners.forEach(i -> i.onEvent(event));
    }


    public void registerClassloaderMetricEventListener(ClassLoaderMetricEventListener eventListener)
    {
        this.classIgnoredEventListeners.add(eventListener);
    }
}
