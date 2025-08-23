/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.decorated;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zul.Div;
import org.zkoss.zul.Vlayout;

/**
 * The Editor that allows to wrap any already defined editor with some custom components or logic.
 */
public class DecoratedEditorRenderer extends AbstractEditorRendererWrapper implements ApplicationContextAware
{
    private static final Logger LOG = LoggerFactory.getLogger(DecoratedEditorRenderer.class);
    protected static final Pattern DECORATED_EDITORS_EDITOR_PATTERN = Pattern.compile("(?<![\\{\\}])(^[^\\{\\}]+$)");
    protected static final Pattern DECORATED_EDITORS_DECORATOR_PATTERN = Pattern.compile("\\{([^\\{\\}]+)\\}");
    protected static final String YE_EDITOR_CONTAINER = "ye-decorated-container";
    protected static final String YE_EDITOR_DECORATOR = "ye-decorated-decorator";
    protected static final String YE_EDITOR_EMBEDDED = "ye-decorated-editor";
    private ApplicationContext applicationContext;


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Vlayout container = new Vlayout();
        container.setSclass(YE_EDITOR_CONTAINER);
        container.setParent(parent);
        final List<String> definitions = extractDefinitions(context);
        final Map<Integer, Component> positionToComponent = new HashMap<>();
        positionToComponent.putAll(renderEmbeddedEditors(container, definitions, context, listener));
        positionToComponent.putAll(renderDecorators(container, definitions, context, listener));
        layoutComponentsInOrder(container, positionToComponent, definitions);
    }


    /**
     * Renders embedded editors found in the definitions list.
     *
     * @param container
     * @param definitions
     *           list of embedded editors and decorators definitions.
     * @param context
     * @param listener
     * @return map of position in definitions list and rendered embedded editor, which is wrapped in temporary container.
     */
    protected Map<Integer, Component> renderEmbeddedEditors(final Vlayout container, final List<String> definitions,
                    final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        final Map<Integer, Component> positionToComponent = new HashMap<>();
        int position = 0;
        for(final String definition : definitions)
        {
            position++;
            final Matcher m = DECORATED_EDITORS_EDITOR_PATTERN.matcher(definition);
            if(m.find())
            {
                final Vlayout tempContainer = new Vlayout();
                tempContainer.setParent(container);
                renderEmbeddedEditor(tempContainer, context, listener, m.group(1));
                positionToComponent.put(Integer.valueOf(position), tempContainer);
            }
        }
        return positionToComponent;
    }


    /**
     * Renders decorators found in the definitions list.
     *
     * @param container
     * @param definitions
     *           list of embedded editors and decorators definitions.
     * @param context
     * @param listener
     * @return map of position in definitions list and rendered decorator editor which is wrapped in temporary container.
     */
    protected Map<Integer, Component> renderDecorators(final Vlayout container, final List<String> definitions,
                    final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        final Map<Integer, Component> positionToComponent = new HashMap<>();
        int position = 0;
        for(final String definition : definitions)
        {
            position++;
            final Matcher m = DECORATED_EDITORS_DECORATOR_PATTERN.matcher(definition);
            if(m.find())
            {
                final Vlayout tempContainer = new Vlayout();
                tempContainer.setParent(container);
                renderDecorator(tempContainer, context, listener, m.group(1));
                positionToComponent.put(Integer.valueOf(position), tempContainer);
            }
        }
        return positionToComponent;
    }


    /**
     * Layouts editors and decorators in order from definitions list.
     *
     * @param container
     *           container on which components will be layout.
     * @param positionToComponent
     *           map of position definitions list and rendered components (embedded editors and decorators).
     * @param definitions
     *           list of embedded editors and decorators definitions.
     */
    protected void layoutComponentsInOrder(final Vlayout container, final Map<Integer, Component> positionToComponent,
                    final List<String> definitions)
    {
        Components.removeAllChildren(container);
        int position = 0;
        for(final String definition : definitions)
        {
            position++;
            final Component tempContainer = positionToComponent.get(Integer.valueOf(position));
            if(tempContainer != null)
            {
                container.removeChild(tempContainer);
                container.getChildren().addAll(tempContainer.getChildren());
            }
            else
            {
                LOG.error("Unable to parse editor definition: {}", definition);
            }
        }
    }


    protected void renderDecorator(final Vlayout parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener, final String decorator)
    {
        final EditorContext<Object> nestedContext = prepareNestedContext(context, null, context.getValueType());
        final CockpitEditorRenderer<Object> renderer = applicationContext.getBean(decorator, CockpitEditorRenderer.class);
        final Div decoratorContainer = new Div();
        decoratorContainer.setSclass(YE_EDITOR_DECORATOR);
        parent.getChildren().add(decoratorContainer);
        renderer.render(decoratorContainer, nestedContext, new EditorListener<Object>()
        {
            @Override
            public void onValueChanged(final Object value)
            {
                // do nothing
            }


            @Override
            public void onEditorEvent(final String eventCode)
            {
                // do nothing
            }


            @Override
            public void sendSocketOutput(final String outputId, final Object data)
            {
                listener.sendSocketOutput(outputId, data);
            }
        });
    }


    protected void renderEmbeddedEditor(final Vlayout parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener, final String editor)
    {
        final Editor component = createEditor(context, listener, context.getValueType(), editor);
        component.setSclass(YE_EDITOR_EMBEDDED);
        parent.getChildren().add(component);
    }


    protected ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    @Autowired
    public void setApplicationContext(final ApplicationContext context) throws BeansException
    {
        this.applicationContext = context;
    }
}
