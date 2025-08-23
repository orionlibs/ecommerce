/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Representation of a node in the widget tree.
 */
public class Widget implements Serializable
{
    private static final long serialVersionUID = -8943922098203089038L;
    private final List<Widget> children = new ArrayList<>();
    private final List<WidgetConnection> outConnections = new ArrayList<>();
    private final List<WidgetConnection> inConnections = new ArrayList<>();
    private final TypedSettingsMap widgetSettings = new TypedSettingsMap();
    private final List<WidgetInstance> widgetInstances = Lists.newCopyOnWriteArrayList();
    private final List<WidgetSocket> virtualInputs = new ArrayList<>(0);
    private final List<WidgetSocket> virtualOutputs = new ArrayList<>(0);
    private String id;
    private String slotId;
    private String title;
    private Widget parent;
    private Widget composedRootInstance;
    private String widgetDefinitionId;
    private boolean template;
    private boolean partOfGroup;
    private String originalID;
    private String lastFocusedTemplateInstanceID;
    private List<String> accessRestrictions;
    private WidgetInstanceSettings widgetInstanceSettings;
    private Widget groupContainer;


    public Widget(final String id)
    {
        this.id = id;
    }


    public boolean isTemplate()
    {
        return template;
    }


    public void setTemplate(final boolean template)
    {
        this.template = template;
    }


    public List<Widget> getChildren()
    {
        return Collections.unmodifiableList(this.children);
    }


    public void clearChildren()
    {
        this.children.clear();
    }


    public void addChild(final Widget widget)
    {
        this.children.add(widget);
    }


    public List<WidgetConnection> getOutConnections()
    {
        return Collections.unmodifiableList(this.outConnections);
    }


    public void clearOutConnections()
    {
        this.outConnections.clear();
    }


    public void addOutConnection(final WidgetConnection conn)
    {
        this.outConnections.add(conn);
    }


    public void removeOutConnection(final WidgetConnection conn)
    {
        this.outConnections.remove(conn);
    }


    public List<WidgetConnection> getInConnections()
    {
        return Collections.unmodifiableList(this.inConnections);
    }


    public void clearInConnections()
    {
        this.inConnections.clear();
    }


    public void addInConnection(final WidgetConnection conn)
    {
        this.inConnections.add(conn);
    }


    public void removeInConnection(final WidgetConnection conn)
    {
        this.inConnections.remove(conn);
    }


    public void addChildAt(final int index, final Widget widget)
    {
        int localIdx = index;
        if(localIdx < 0)
        {
            localIdx = 0;
        }
        if(localIdx > this.children.size())
        {
            localIdx = this.children.size();
        }
        this.children.add(localIdx, widget);
    }


    public void removeChild(final Widget widget)
    {
        this.children.remove(widget);
    }


    public String getId()
    {
        return id;
    }


    public void setId(final String id)
    {
        this.id = id;
    }


    public String getSlotId()
    {
        return slotId;
    }


    public void setSlotId(final String slotId)
    {
        this.slotId = slotId;
    }


    public String getWidgetDefinitionId()
    {
        return widgetDefinitionId;
    }


    public void setWidgetDefinitionId(final String widgetDefinitionId)
    {
        this.widgetDefinitionId = widgetDefinitionId;
    }


    public Widget getParent()
    {
        return parent;
    }


    public void setParent(final Widget parent)
    {
        this.parent = parent;
    }


    public String getTitle()
    {
        return this.title;
    }


    public void setTitle(final String title)
    {
        this.title = title;
    }


    @Override
    public String toString()
    {
        return String.format("[%s] %s", getWidgetDefinitionId() == null ? "nodef " : getWidgetDefinitionId(), getId());
    }


    public String getTitleNoFallback()
    {
        return title;
    }


    public TypedSettingsMap getWidgetSettings()
    {
        return widgetSettings;
    }


    public WidgetInstanceSettings getWidgetInstanceSettings()
    {
        if(widgetInstanceSettings == null)
        {
            widgetInstanceSettings = new WidgetInstanceSettings();
        }
        return widgetInstanceSettings;
    }


    public void setWidgetInstanceSettings(final WidgetInstanceSettings rulesConfiguration)
    {
        this.widgetInstanceSettings = rulesConfiguration;
    }


    public WidgetInstance getLastFocusedInstance()
    {
        if(StringUtils.isNotBlank(lastFocusedTemplateInstanceID))
        {
            for(final WidgetInstance instance : getWidgetInstances())
            {
                if(lastFocusedTemplateInstanceID.equals(instance.getId()))
                {
                    return instance;
                }
            }
        }
        return null;
    }


    public void setLastFocusedTemplateInstance(final WidgetInstance widgetInstance)
    {
        lastFocusedTemplateInstanceID = widgetInstance == null ? null : widgetInstance.getId();
    }


    public boolean isPartOfGroup()
    {
        return partOfGroup;
    }


    public void setPartOfGroup(final boolean partOfGroup)
    {
        this.partOfGroup = partOfGroup;
    }


    public Widget getComposedRootInstance()
    {
        return composedRootInstance;
    }


    public void setComposedRootInstance(final Widget composedRootInstance)
    {
        this.composedRootInstance = composedRootInstance;
    }


    public Widget getGroupContainer()
    {
        return groupContainer;
    }


    public void setGroupContainer(final Widget composedWidget)
    {
        this.groupContainer = composedWidget;
    }


    public void addWidgetInstance(final WidgetInstance instance)
    {
        if(!widgetInstances.contains(instance))
        {
            widgetInstances.add(instance);
        }
    }


    public void removeWidgetInstance(final WidgetInstance instance)
    {
        widgetInstances.remove(instance);
    }


    public List<WidgetInstance> getWidgetInstances()
    {
        return Collections.unmodifiableList(widgetInstances);
    }


    public void addVirtualInput(final WidgetSocket socket)
    {
        if(!virtualInputs.contains(socket))
        {
            virtualInputs.add(socket);
        }
    }


    public void removeVirtualInput(final WidgetSocket socket)
    {
        virtualInputs.remove(socket);
    }


    public List<WidgetSocket> getVirtualInputs()
    {
        return Collections.unmodifiableList(virtualInputs);
    }


    public void addVirtualOutput(final WidgetSocket socket)
    {
        if(!virtualOutputs.contains(socket))
        {
            virtualOutputs.add(socket);
        }
    }


    public void removeVirtualOutput(final WidgetSocket socket)
    {
        virtualOutputs.remove(socket);
    }


    public List<WidgetSocket> getVirtualOutputs()
    {
        return Collections.unmodifiableList(virtualOutputs);
    }


    public String getOriginalID()
    {
        return originalID;
    }


    public void setOriginalID(final String originalID)
    {
        this.originalID = originalID;
    }


    public List<String> getAccessRestrictions()
    {
        return accessRestrictions;
    }


    public void setAccessRestrictions(final List<String> accessRestrictions)
    {
        if(accessRestrictions == null)
        {
            this.accessRestrictions = Collections.emptyList();
        }
        else
        {
            final List<String> nonBlankAccessRestrictions = accessRestrictions.stream() //
                            .filter(StringUtils::isNotBlank)//
                            .map(string -> string.trim())//
                            .distinct()//
                            .collect(Collectors.toList());
            this.accessRestrictions = nonBlankAccessRestrictions;
        }
    }


    @Override
    public boolean equals(final Object object)
    {
        return object != null && (object.getClass() == this.getClass()) && getId().equals(((Widget)object).getId());
    }


    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }
}
