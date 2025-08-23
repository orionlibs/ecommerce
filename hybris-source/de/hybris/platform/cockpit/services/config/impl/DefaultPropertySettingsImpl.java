package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.DefaultPropertySettings;
import java.util.Collections;
import java.util.Map;

public class DefaultPropertySettingsImpl implements DefaultPropertySettings
{
    private final String defaultEditorCode;
    private final Map<String, String> parameters;
    private final PropertyDescriptor propertyDescriptor;
    private final boolean baseProperty;


    public DefaultPropertySettingsImpl(String defaultEditorCode, Map<String, String> parameters, PropertyDescriptor propertyDescriptor, boolean baseProperty)
    {
        this.defaultEditorCode = defaultEditorCode;
        this.parameters = Collections.unmodifiableMap(parameters);
        this.propertyDescriptor = propertyDescriptor;
        this.baseProperty = baseProperty;
    }


    public String getDefaultEditorCode()
    {
        return this.defaultEditorCode;
    }


    public Map<String, String> getParameters()
    {
        return this.parameters;
    }


    public PropertyDescriptor getPropertyDescriptor()
    {
        return this.propertyDescriptor;
    }


    public boolean isBaseProperty()
    {
        return this.baseProperty;
    }
}
