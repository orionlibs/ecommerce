package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ObjectValueHandlerRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

public class DefaultObjectValueHandlerRegistry implements ObjectValueHandlerRegistry
{
    private static final String VALUE_HANDLER_CHAIN_REQUEST_CACHE = "valueHandlerChainRequestCache";
    private TypeService typeService;
    private ApplicationContext appContext;
    private final Map<String, List<ObjectValueHandler>> handlersMap = new HashMap<>();


    public List<ObjectValueHandler> getValueHandlerChain(ObjectType type)
    {
        String qualifier = type.getCode().toLowerCase();
        return (List<ObjectValueHandler>)(new Object(this, "valueHandlerChainRequestCache", qualifier, type))
                        .get(qualifier);
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.appContext = applicationContext;
    }


    public void initHandlerMappings()
    {
        Map<String, ObjectValueHandlerMapping> beanMap = this.appContext.getBeansOfType(ObjectValueHandlerMapping.class);
        List<ObjectValueHandlerMapping> mappings = new ArrayList<>(beanMap.values());
        Collections.sort(mappings, (Comparator<? super ObjectValueHandlerMapping>)new Object(this));
        this.handlersMap.clear();
        for(ObjectValueHandlerMapping mapping : mappings)
        {
            List<ObjectValueHandler> handlers = this.handlersMap.get(mapping.getTypeCode().toLowerCase());
            if(handlers == null)
            {
                handlers = new ArrayList<>();
                this.handlersMap.put(mapping.getTypeCode().toLowerCase(), handlers);
            }
            handlers.add(mapping.getValueHandler());
        }
    }


    @Required
    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }
}
