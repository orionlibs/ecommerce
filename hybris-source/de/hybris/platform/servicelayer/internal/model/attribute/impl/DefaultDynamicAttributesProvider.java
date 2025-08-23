package de.hybris.platform.servicelayer.internal.model.attribute.impl;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.model.attribute.DynamicAttributesProvider;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.model.attribute.DynamicLocalizedAttributeHandler;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class DefaultDynamicAttributesProvider implements DynamicAttributesProvider, Serializable
{
    private final Map<String, DynamicAttributeHandler> handlers;


    public DefaultDynamicAttributesProvider(Map<String, DynamicAttributeHandler> handlers)
    {
        this.handlers = handlers;
    }


    public Object get(AbstractItemModel model, String attribute)
    {
        DynamicAttributeHandler handler = this.handlers.get(attribute);
        if(handler != null)
        {
            return handler.get(model);
        }
        throw new SystemException("No registered attribute handler for attribute '" + attribute + "'. Check your Spring configuration.");
    }


    public void set(AbstractItemModel model, String attribute, Object value)
    {
        DynamicAttributeHandler handler = this.handlers.get(attribute);
        if(handler != null)
        {
            handler.set(model, value);
        }
        else
        {
            throw new SystemException("No registered attribute handler for attribute '" + attribute + "'. Check your Spring configuration.");
        }
    }


    public Object getLocalized(AbstractItemModel model, String attribute, Locale loc)
    {
        DynamicAttributeHandler handler = this.handlers.get(attribute);
        if(handler instanceof DynamicLocalizedAttributeHandler)
        {
            return ((DynamicLocalizedAttributeHandler)handler).get(model, loc);
        }
        if(handler != null)
        {
            return handler.get(model);
        }
        throw new SystemException("No registered attribute handler for the localized attribute '" + attribute + "'. Check your Spring configuration.");
    }


    public void setLocalized(AbstractItemModel model, String attribute, Object value, Locale loc)
    {
        DynamicAttributeHandler handler = this.handlers.get(attribute);
        if(handler instanceof DynamicLocalizedAttributeHandler)
        {
            ((DynamicLocalizedAttributeHandler)handler).set(model, value, loc);
        }
        else if(handler != null)
        {
            handler.set(model, value);
        }
        else
        {
            throw new SystemException("No registered attribute handler for the localized attribute '" + attribute + "'. Check your Spring configuration.");
        }
    }


    public boolean isDynamic(String attributeName)
    {
        return this.handlers.containsKey(attributeName);
    }
}
