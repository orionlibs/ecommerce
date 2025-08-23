package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.UIComponentConfigurationContext;
import java.util.HashMap;
import java.util.Map;

public class JaxbBasedUIComponentConfigurationContext<JAXBCLASS> implements UIComponentConfigurationContext
{
    private final JAXBCLASS rootJaxbElement;
    private final Map<Object, Object> elementMap = new HashMap<>();


    public JaxbBasedUIComponentConfigurationContext(JAXBCLASS rootJaxbElement)
    {
        this.rootJaxbElement = rootJaxbElement;
    }


    public JAXBCLASS getRootJaxbElement()
    {
        return this.rootJaxbElement;
    }


    public void registerJaxbElement(Object uiConfigElement, Object jaxbElement)
    {
        this.elementMap.put(uiConfigElement, jaxbElement);
    }


    public void unregister(Object uiConfigElement)
    {
        this.elementMap.remove(uiConfigElement);
    }


    public Object getJaxbElement(Object uiConfigElement)
    {
        return this.elementMap.get(uiConfigElement);
    }
}
