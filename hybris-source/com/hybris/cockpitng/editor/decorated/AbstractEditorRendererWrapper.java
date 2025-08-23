/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.decorated;

import static java.util.stream.Collectors.toList;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractEditorRendererWrapper extends AbstractCockpitEditorRenderer<Object>
{
    public static final Pattern DECORATED_EDITORS_PATTERN = Pattern
                    .compile("^(\\{[^\\{\\}]+\\}, *)?([^\\{\\}]+)(, *\\{[^\\{\\}]+\\})?$");
    protected static final Pattern DECORATED_EDITORS_EMBEDDED_CONTENT = Pattern.compile(".+?\\((.*)\\)");


    protected String getEditorTypeFromContext(final EditorContext<Object> context, final int index)
    {
        final List<String> definitions = extractDefinitions(context);
        if(definitions.isEmpty())
        {
            return null;
        }
        else if(definitions.size() > index)
        {
            return definitions.get(index);
        }
        else
        {
            throw new IllegalStateException(
                            "Incorrect editor definition: unable to extract nested editor definition at index " + index);
        }
    }


    protected List<String> extractDefinitions(final EditorContext<Object> context)
    {
        final String valueEditor = context.getParameterAs(Editor.VALUE_EDITOR);
        if(StringUtils.isNotBlank(valueEditor))
        {
            final String embeddedEditor = extractDecoratedEditorContent(valueEditor);
            if(StringUtils.isNotBlank(embeddedEditor))
            {
                final Matcher matcher = DECORATED_EDITORS_PATTERN.matcher(embeddedEditor);
                if(matcher.matches())
                {
                    return Arrays.stream(embeddedEditor.split(",")).map(String::trim).collect(toList());
                }
            }
        }
        return Collections.emptyList();
    }


    protected String extractDecoratedEditorContent(final String editor)
    {
        if(StringUtils.isNotBlank(editor))
        {
            final Matcher matcher = DECORATED_EDITORS_EMBEDDED_CONTENT.matcher(editor);
            if(matcher.matches())
            {
                return matcher.group(1);
            }
        }
        return null;
    }


    protected EditorContext<Object> prepareNestedContext(final EditorContext<Object> context, final String editorType,
                    final String valueType)
    {
        final EditorContext<Object> editorContext = new EditorContext<>(context.getInitialValue(), context.getDefinition(),
                        context.getParameters(), context.getLabels(), context.getReadableLocales(), context.getWritableLocales());
        if(editorType == null)
        {
            editorContext.removeParameter(Editor.VALUE_EDITOR);
        }
        else
        {
            editorContext.setParameter(Editor.VALUE_EDITOR, editorType);
        }
        editorContext.setValueType(valueType);
        editorContext.setEditable(context.isEditable());
        editorContext.setOrdered(context.isOrdered());
        editorContext.setOptional(context.isOptional());
        return editorContext;
    }


    protected Editor createEditor(final EditorContext<Object> context, final EditorListener<Object> listener,
                    final String valueType, final String editorType)
    {
        final Editor editorContainer = new Editor();
        editorContainer.setType(valueType);
        editorContainer.setReadOnly(!context.isEditable());
        editorContainer.setOptional(context.isOptional());
        editorContainer.setOrdered(context.isOrdered());
        editorContainer.setWidgetInstanceManager((WidgetInstanceManager)context.getParameter("wim"));
        editorContainer.addParameters(context.getParameters());
        editorContainer.setDefaultEditor(editorType);
        editorContainer.setInitialValue(context.getInitialValue());
        editorContainer.setReadableLocales(context.getReadableLocales());
        editorContainer.setWritableLocales(context.getWritableLocales());
        editorContainer.afterCompose();
        editorContainer.addEventListener(Editor.ON_VALUE_CHANGED, event -> listener.onValueChanged(event.getData()));
        editorContainer.addEventListener(Editor.ON_EDITOR_EVENT, event -> listener.onEditorEvent(Objects.toString(event.getData())));
        return editorContainer;
    }
}
