/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.EditorGroup;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.EditorProperty;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Editors;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Parameter;
import java.util.List;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

public class DefaultCockpitEditorsRenderer implements CockpitComponentsRenderer
{
    @Override
    public void render(final AbstractCockpitElementsContainer parent, final Object configuration)
    {
        if(configuration instanceof Editors && parent instanceof com.hybris.cockpitng.components.Editors)
        {
            parent.getChildren().clear();
            final Editors editor = (Editors)configuration;
            if(StringUtils.isBlank(parent.getGroup()))
            {
                // render all groups
                for(final Object groupConfig : editor.getGroup())
                {
                    if(groupConfig instanceof EditorGroup)
                    {
                        parent.appendChild(renderGroup(parent, (EditorGroup)groupConfig, true));
                    }
                }
            }
            else
            {
                // render selected group
                final EditorGroup groupConfig = getGroupConfig(editor, parent.getGroup());
                if(groupConfig != null)
                {
                    parent.appendChild(renderGroup(parent, groupConfig, false));
                }
            }
        }
    }


    protected EditorGroup getGroupConfig(final Editors configuration, final String groupId)
    {
        for(final Object groupConfig : configuration.getGroup())
        {
            if(groupConfig instanceof EditorGroup && groupId.equalsIgnoreCase(((EditorGroup)groupConfig).getQualifier()))
            {
                return ((EditorGroup)groupConfig);
            }
        }
        return null;
    }


    protected HtmlBasedComponent renderGroup(final AbstractCockpitElementsContainer parent, final EditorGroup groupConfig,
                    final boolean showGroupHeader)
    {
        if(showGroupHeader)
        {
            final Panel panel = new Panel();
            String title = parent.getWidgetInstanceManager().getLabel(groupConfig.getLabel());
            if(StringUtils.isBlank(title))
            {
                title = groupConfig.getQualifier();
            }
            panel.setTitle(title);
            final Panelchildren panelChildren = new Panelchildren();
            panel.appendChild(panelChildren);
            for(final EditorProperty propertyConfig : groupConfig.getProperty())
            {
                final HtmlBasedComponent propertyComponent = renderProperty(parent, propertyConfig);
                panelChildren.appendChild(propertyComponent);
            }
            return panel;
        }
        else
        {
            final Div container = new Div();
            for(final EditorProperty propertyConfig : groupConfig.getProperty())
            {
                final HtmlBasedComponent propertyComponent = renderProperty(parent, propertyConfig);
                container.appendChild(propertyComponent);
            }
            return container;
        }
    }


    protected HtmlBasedComponent renderProperty(final AbstractCockpitElementsContainer parent, final EditorProperty propertyConfig)
    {
        final Div wrapperDiv = new Div();
        wrapperDiv.setSclass("editorAreaRow");
        final Hbox hbox = new Hbox();
        wrapperDiv.appendChild(hbox);
        String title = parent.getWidgetInstanceManager().getLabel(propertyConfig.getLabel());
        if(StringUtils.isBlank(title))
        {
            title = propertyConfig.getQualifier();
        }
        Cell cell = new Cell();
        cell.setWidth("140px");
        cell.appendChild(new Label(title + ": "));
        hbox.appendChild(cell);
        cell = new Cell();
        cell.appendChild(createEditor(parent, propertyConfig));
        hbox.appendChild(cell);
        return wrapperDiv;
    }


    private static Component createEditor(final AbstractCockpitElementsContainer parent, final EditorProperty propertyConfig)
    {
        final String qualifier = parent.getQualifier(propertyConfig.getQualifier());
        final Editor editorContainer = new Editor();
        editorContainer.setWidgetInstanceManager(parent.getWidgetInstanceManager());
        editorContainer.setProperty(qualifier);
        editorContainer.setDefaultEditor(propertyConfig.getEditor());
        editorContainer.setReadOnly(BooleanUtils.isTrue(propertyConfig.isReadOnly()));
        editorContainer.setType(propertyConfig.getType());
        final List<Parameter> parameters = propertyConfig.getParameter();
        for(final Parameter parameter : parameters)
        {
            editorContainer.addParameter(parameter.getName(), parameter.getValue());
        }
        editorContainer.addEventListener(com.hybris.cockpitng.components.Editor.ON_VALUE_CHANGED, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                Events.postEvent(parent, event);
            }
        });
        editorContainer.afterCompose();
        return editorContainer;
    }
}
