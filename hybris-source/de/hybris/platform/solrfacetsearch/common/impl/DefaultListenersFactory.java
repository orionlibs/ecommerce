package de.hybris.platform.solrfacetsearch.common.impl;

import de.hybris.platform.solrfacetsearch.common.ListenersFactory;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultListenersFactory implements ListenersFactory, ApplicationContextAware
{
    private Collection<Class<?>> supportedTypes;
    private ApplicationContext applicationContext;
    private Map<Class<?>, List<Object>> globalListeners;


    public Collection<Class<?>> getSupportedTypes()
    {
        return this.supportedTypes;
    }


    @Required
    public void setSupportedTypes(Collection<Class<?>> supportedTypes)
    {
        this.supportedTypes = supportedTypes;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
        loadGlobalListeners();
    }


    public <T> List<T> getListeners(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Class<T> listenerType)
    {
        List<T> listeners = new ArrayList<>();
        List<Object> globalListenersForType = getGlobalListeners().get(listenerType);
        if(globalListenersForType != null)
        {
            listeners.addAll(globalListenersForType);
        }
        listeners.addAll(loadIndexConfigListeners(facetSearchConfig.getIndexConfig(), listenerType));
        listeners.addAll(loadIndexedTypeListeners(indexedType, listenerType));
        return Collections.unmodifiableList(listeners);
    }


    protected void loadGlobalListeners()
    {
        this.globalListeners = new HashMap<>();
        Map<String, ListenerDefinition> listenerDefinitionsMap = this.applicationContext.getBeansOfType(ListenerDefinition.class);
        List<ListenerDefinition> listenerDefinitions = new ArrayList<>(listenerDefinitionsMap.values());
        Collections.sort(listenerDefinitions);
        for(Class<?> type : this.supportedTypes)
        {
            List<Object> listeners = new ArrayList();
            for(ListenerDefinition listenerDefinition : listenerDefinitions)
            {
                Object listener = listenerDefinition.getListener();
                if(listener != null && type.isAssignableFrom(listener.getClass()))
                {
                    listeners.add(listener);
                }
            }
            this.globalListeners.put(type, listeners);
        }
    }


    protected final <T> List<T> loadIndexConfigListeners(IndexConfig indexConfig, Class<T> listenerType)
    {
        return loadListeners(indexConfig.getListeners(), listenerType);
    }


    protected <T> List<T> loadIndexedTypeListeners(IndexedType indexedType, Class<T> listenerType)
    {
        return loadListeners(indexedType.getListeners(), listenerType);
    }


    protected <T> List<T> loadListeners(Collection<String> beanNames, Class<T> listenerType)
    {
        List<T> listeners = new ArrayList<>();
        if(beanNames != null && !beanNames.isEmpty())
        {
            for(String beanName : beanNames)
            {
                if(this.applicationContext.isTypeMatch(beanName, listenerType))
                {
                    T listener = (T)this.applicationContext.getBean(beanName, listenerType);
                    listeners.add(listener);
                }
            }
        }
        return listeners;
    }


    protected ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    protected Map<Class<?>, List<Object>> getGlobalListeners()
    {
        return this.globalListeners;
    }
}
