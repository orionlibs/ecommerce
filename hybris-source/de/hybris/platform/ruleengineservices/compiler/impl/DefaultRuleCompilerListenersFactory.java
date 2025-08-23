package de.hybris.platform.ruleengineservices.compiler.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerListenersFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultRuleCompilerListenersFactory implements RuleCompilerListenersFactory, ApplicationContextAware
{
    private Collection<Class<?>> supportedTypes;
    private ApplicationContext applicationContext;
    private Map<Class<?>, List<Object>> listeners;


    public Collection<Class<?>> getSupportedTypes()
    {
        return this.supportedTypes;
    }


    @Required
    public void setSupportedTypes(Collection<Class<?>> supportedTypes)
    {
        this.supportedTypes = supportedTypes;
    }


    protected ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
        this.listeners = loadListeners();
    }


    protected Map<Class<?>, List<Object>> loadListeners()
    {
        Map<Class<?>, List<Object>> listenersMap = new HashMap<>();
        Map<String, RuleCompilerListenerDefinition> listenerDefinitionsMap = this.applicationContext.getBeansOfType(RuleCompilerListenerDefinition.class);
        List<RuleCompilerListenerDefinition> listenerDefinitions = new ArrayList<>(listenerDefinitionsMap.values());
        Collections.sort(listenerDefinitions);
        for(Class<?> type : this.supportedTypes)
        {
            List<Object> listenersForType = registerListenersForType(listenerDefinitions, type);
            listenersMap.put(type, listenersForType);
        }
        return listenersMap;
    }


    protected List<Object> registerListenersForType(List<RuleCompilerListenerDefinition> listenerDefinitions, Class<?> type)
    {
        List<Object> listenersForType = Lists.newArrayList();
        for(RuleCompilerListenerDefinition listenerDefinition : listenerDefinitions)
        {
            Object listener = listenerDefinition.getListener();
            if(listener != null)
            {
                Class<?> listenerType = listener.getClass();
                if(type.isAssignableFrom(listenerType))
                {
                    listenersForType.add(listener);
                }
            }
        }
        return listenersForType;
    }


    public <T> List<T> getListeners(Class<T> listenerType)
    {
        List<Object> listenersForType = this.listeners.get(listenerType);
        if(listenersForType != null)
        {
            return Collections.unmodifiableList((List)listenersForType);
        }
        return Collections.emptyList();
    }
}
