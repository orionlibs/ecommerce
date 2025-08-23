package de.hybris.bootstrap.loader.rule.internal;

import de.hybris.bootstrap.loader.rule.IgnoreClassLoaderRuleListener;
import de.hybris.bootstrap.loader.rule.IgnoreClassLoadingEvent;
import de.hybris.bootstrap.loader.rule.IgnoreClassLoadingRule;
import java.util.HashSet;
import java.util.Set;

public class CacheClassNotFoundRule implements IgnoreClassLoadingRule, IgnoreClassLoaderRuleListener
{
    private final Set<String> classNotFoundCache = new HashSet<>();


    public void initialize(String param)
    {
    }


    public IgnoreClassLoadingRule.IgnoredStatus isIgnored(String name)
    {
        if(this.classNotFoundCache.contains(name))
        {
            return IgnoreClassLoadingRule.IgnoredStatus.IGNORED;
        }
        return IgnoreClassLoadingRule.IgnoredStatus.UNDEFINED;
    }


    public void onEvent(IgnoreClassLoadingEvent event)
    {
        if(event instanceof ClassNotFoundRuleEvent)
        {
            ClassNotFoundRuleEvent eventClassNotFound = (ClassNotFoundRuleEvent)event;
            if(!this.classNotFoundCache.contains(eventClassNotFound.getClassName()))
            {
                this.classNotFoundCache.add(eventClassNotFound.getClassName());
            }
        }
    }
}
