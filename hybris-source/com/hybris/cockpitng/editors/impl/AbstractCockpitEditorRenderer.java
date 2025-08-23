/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors.impl;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;

/**
 * Abstract implementation of {@link CockpitEditorRenderer} interface. Implements the common mechanisms for the editors.
 */
public abstract class AbstractCockpitEditorRenderer<T> implements CockpitEditorRenderer<T>
{
    protected static final String YW_EDITOR_AREA_LABEL_CONTAINER = "yw-editorarea-label-container";
    public static final String HEADER_LABEL_TOOLTIP = "headerLabelTooltip";
    protected static final String ON_DELETE_EVENT = "onDelete";
    protected static final String ON_ADD_EVENT = "onAdd";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCockpitEditorRenderer.class);
    private static final Pattern SUBTYPE_EXTRACTOR_PATTERN = Pattern.compile("[^\\(]*\\((.*?)\\)$");
    private static final Pattern SUBEDITOR_EXTRACTOR_PATTERN = Pattern.compile("^.+\\((.*)\\)$");
    private static final int SUPPRESSED_PARENTHESIS_INDEX = 1;


    /**
     * Retrieves the {@link #INITIAL_EDIT_STRING} field from the editor parameters.
     *
     * @param context
     *           contains various parameters
     */
    protected String getInitialEditString(final EditorContext<T> context)
    {
        try
        {
            return (String)context.getParameter(INITIAL_EDIT_STRING);
        }
        catch(final ClassCastException e)
        {
            LOG.error("Initial input string not of type String.", e);
            return null;
        }
    }


    /**
     * Utility method providing a way for editors with editorType like Range(java.lang.Integer) to retrieve the embedded
     * type Integer.
     *
     * @param context
     *           to extract the editor type from
     * @return The editor embedded editorType or null if no embedded type is referenced in editorType.
     */
    protected String extractEmbeddedType(final EditorContext<T> context)
    {
        String embeddedEditorType = null;
        final String valueType = context.getValueType();
        if(StringUtils.isNotBlank(valueType))
        {
            final Matcher matcher = SUBTYPE_EXTRACTOR_PATTERN.matcher(valueType);
            if(matcher.find())
            {
                embeddedEditorType = matcher.group(SUPPRESSED_PARENTHESIS_INDEX);
            }
        }
        return embeddedEditorType;
    }


    protected String extractEmbeddedEditor(final String editor)
    {
        if(StringUtils.isNotBlank(editor))
        {
            final Matcher matcher = SUBEDITOR_EXTRACTOR_PATTERN.matcher(editor);
            if(matcher.matches())
            {
                return matcher.group(SUPPRESSED_PARENTHESIS_INDEX);
            }
        }
        return null;
    }


    /**
     * Retrieves the value for this Editor's label in the order: As L10n value, provided raw value, default L10n value if
     * nothing is specified. Might return null if both parameterKey and defaultFallbackL10nKey are invalid.
     *
     * @param context
     *           Contains various parameters
     * @param parameterKey
     *           A parameter name to which an L10n key is assigned.
     * @param defaultFallbackL10nKey
     *           An L10n key's value to display when no key is provided as a custom attribute.
     * @return label value which must not be necessarily a L10n value.
     */
    protected String getL10nDecorator(final EditorContext<T> context, final String parameterKey,
                    final String defaultFallbackL10nKey)
    {
        final String parameterValue = (String)context.getParameter(parameterKey);
        if(parameterValue == null)
        {
            return context.getLabel(defaultFallbackL10nKey);
        }
        else
        {
            final UnaryOperator<String> contextSupplier = context::getLabel;
            final UnaryOperator<String> labelsSupplier = Labels::getLabel;
            final UnaryOperator<String> wimSupplier = key -> Optional.ofNullable(context.getParameter("wim"))
                            .map(wim -> (WidgetInstanceManager)wim).map(wim -> wim.getLabel(key)).orElse(null);
            return Stream.of(contextSupplier, wimSupplier, labelsSupplier).map(function -> function.apply(parameterValue))
                            .filter(Objects::nonNull).findFirst().orElse(parameterValue);
        }
    }


    /**
     * Find first component in hierarchy which is an Editor
     *
     * @param component
     *           to lookup first nested editor
     * @return first found instance of editor or null
     */
    protected Editor findAncestorEditor(final Component component)
    {
        Component current = component;
        while(current != null && !(current instanceof Editor) && !(current instanceof Widget))
        {
            current = current.getParent();
        }
        if(current instanceof Editor)
        {
            return (Editor)current;
        }
        return null;
    }


    /**
     * Finds embedded editors for given component. If the component is not instance of an {@link Editor} then method will
     * seek embedded editors in ancestor editor {@link #findAncestorEditor(Component)} of the component.
     *
     * @param component
     *           to look up embedded editors.
     * @return list of embedded editors;
     */
    protected List<Editor> findEmbeddedEditors(final Component component)
    {
        final Editor ancestorEditor = component instanceof Editor ? (Editor)component : findAncestorEditor(component);
        final List<Editor> editors = new ArrayList<>();
        if(ancestorEditor != null)
        {
            final Iterable<Component> components = ancestorEditor.queryAll("editor");
            components.forEach(e -> {
                if(e != ancestorEditor && e instanceof Editor)
                {
                    editors.add((Editor)e);
                }
            });
        }
        return editors;
    }
}
