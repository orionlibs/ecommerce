/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract class for all cockpit component definition types such as widgets, actions and editors. Component definitions
 * are managed by component registry.
 */
public abstract class AbstractCockpitComponentDefinition
{
    private String code;
    private String name;
    private String description;
    private String categoryTag;
    private String resourcePath;
    private String locationPath;
    private String declaringModule;
    private List<WidgetSocket> inputs;
    private List<WidgetSocket> outputs;
    private Map<String, String> forwardMap;
    private TypedSettingsMap defaultSettings;
    private String parentCode;


    public String getCode()
    {
        return code;
    }


    public void setCode(final String code)
    {
        this.code = code;
    }


    public String getParentCode()
    {
        return parentCode;
    }


    public void setParentCode(final String parentCode)
    {
        this.parentCode = parentCode;
    }


    public String getName()
    {
        return name;
    }


    public void setName(final String name)
    {
        this.name = name;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(final String description)
    {
        this.description = description;
    }


    public String getCategoryTag()
    {
        return categoryTag;
    }


    public void setCategoryTag(final String categoryTag)
    {
        this.categoryTag = categoryTag;
    }


    public String getResourcePath()
    {
        return resourcePath;
    }


    public void setResourcePath(final String resourcePath)
    {
        this.resourcePath = resourcePath;
    }


    /**
     * @return The absolute web path (widget root) of the widget definition file and other widget resources.
     */
    public String getLocationPath()
    {
        return locationPath;
    }


    /**
     * @param locationPath
     *           The absolute web path of the widget definition file and other widget resources.
     */
    public void setLocationPath(final String locationPath)
    {
        this.locationPath = locationPath;
    }


    public TypedSettingsMap getDefaultSettings()
    {
        return defaultSettings;
    }


    public void setDefaultSettings(final TypedSettingsMap defaultSettings)
    {
        this.defaultSettings = defaultSettings;
    }


    public List<WidgetSocket> getInputs()
    {
        return inputs == null ? Collections.emptyList() : Collections.unmodifiableList(inputs);
    }


    public void setInputs(final List<WidgetSocket> inputs)
    {
        this.inputs = inputs;
    }


    public List<WidgetSocket> getOutputs()
    {
        return outputs == null ? Collections.emptyList() : Collections.unmodifiableList(outputs);
    }


    public void setOutputs(final List<WidgetSocket> outputs)
    {
        this.outputs = outputs;
    }


    public Map<String, String> getForwardMap()
    {
        return forwardMap == null ? Collections.emptyMap() : forwardMap;
    }


    public void setForwardMap(final Map<String, String> forwardMap)
    {
        this.forwardMap = forwardMap;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final AbstractCockpitComponentDefinition that = (AbstractCockpitComponentDefinition)o;
        return Objects.equals(code, that.code) && Objects.equals(locationPath, that.locationPath)
                        && Objects.equals(declaringModule, that.declaringModule);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(code, locationPath, declaringModule);
    }


    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[" + code + " | " + name + "]";
    }


    public String getDeclaringModule()
    {
        return declaringModule;
    }


    public void setDeclaringModule(final String declaringModule)
    {
        this.declaringModule = declaringModule;
    }
}
