/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors;

import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.util.labels.CockpitComponentDefinitionLabelLocator;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * A context object given to a cockpit editor to render itself.
 */
public class EditorContext<T> extends DefaultCockpitContext
{
    private final transient T initialValue;
    private final transient Map<String, Object> labels;
    private final transient EditorDefinition definition;
    private final Set<Locale> readableLocales;
    private final Set<Locale> writableLocales;
    private boolean editable;
    private boolean optional;
    private String valueType;
    private String tooltiptext;
    private String editorLabel;
    private boolean ordered;
    private boolean partOf;
    private boolean primitive;
    private String successNotificationId;


    public EditorContext(final T initialValue, final EditorDefinition definition, final Map<String, Object> parameters,
                    final Map<String, Object> labels, final Set<Locale> readableLocales, final Set<Locale> writableLocales)
    {
        super(parameters);
        this.initialValue = initialValue;
        this.definition = definition;
        this.readableLocales = readableLocales;
        this.writableLocales = writableLocales;
        this.labels = labels;
    }


    public String getViewSrc()
    {
        return this.definition.getViewSrc();
    }


    public T getInitialValue()
    {
        return initialValue;
    }


    public <K> K getParameterAs(final String key)
    {
        return (K)getParameter(key);
    }


    public String getLabel(final String key)
    {
        return CockpitComponentDefinitionLabelLocator.getLabel(this.labels, key);
    }


    public String getLabel(final String key, final Object[] args)
    {
        return CockpitComponentDefinitionLabelLocator.getLabel(this.labels, key, args);
    }


    public String getResourceUrl(final String relativeUrl)
    {
        final String relPath;
        if(StringUtils.isEmpty(relativeUrl))
        {
            relPath = "";
        }
        else if(relativeUrl.charAt(0) == '/')
        {
            relPath = relativeUrl;
        }
        else
        {
            relPath = "/" + relativeUrl;
        }
        return Objects.toString(getParameter(CockpitWidgetEngine.COMPONENT_ROOT_PARAM)) + relPath;
    }


    public boolean isEditable()
    {
        return editable;
    }


    public void setEditable(final boolean editable)
    {
        this.editable = editable;
    }


    public boolean isOptional()
    {
        return optional;
    }


    public void setOptional(final boolean optional)
    {
        this.optional = optional;
    }


    /**
     * Value type as specified in the editor definition or for the concrete instance of the editor.
     *
     * @return value type as specified in the editor definition or for the concrete instance of the editor
     */
    public String getValueType()
    {
        return valueType;
    }


    public void setValueType(final String valueType)
    {
        this.valueType = valueType;
    }


    /**
     * @return editor code as specified in the definition
     */
    public String getCode()
    {
        return this.definition.getCode();
    }


    public EditorDefinition getDefinition()
    {
        return definition;
    }


    /**
     * @return the labels
     */
    public Map<String, Object> getLabels()
    {
        return labels;
    }


    public Set<Locale> getReadableLocales()
    {
        if(readableLocales != null)
        {
            return Collections.unmodifiableSet(readableLocales);
        }
        else
        {
            return null;
        }
    }


    public Set<Locale> getWritableLocales()
    {
        if(writableLocales != null)
        {
            return Collections.unmodifiableSet(writableLocales);
        }
        else
        {
            return null;
        }
    }


    public String getEditorLabel()
    {
        return editorLabel;
    }


    public void setEditorLabel(final String editorLabel)
    {
        this.editorLabel = editorLabel;
    }


    public boolean isOrdered()
    {
        return ordered;
    }


    public void setOrdered(final boolean ordered)
    {
        this.ordered = ordered;
    }


    public boolean isPartOf()
    {
        return partOf;
    }


    public void setPartOf(final boolean partOf)
    {
        this.partOf = partOf;
    }


    public boolean isPrimitive()
    {
        return primitive;
    }


    public void setPrimitive(final boolean primitive)
    {
        this.primitive = primitive;
    }


    public String getSuccessNotificationId()
    {
        return successNotificationId;
    }


    public void setSuccessNotificationId(final String successNotificationId)
    {
        this.successNotificationId = successNotificationId;
    }


    public String getTooltiptext()
    {
        return tooltiptext;
    }


    public void setTooltiptext(final String tooltiptext)
    {
        this.tooltiptext = tooltiptext;
    }


    public static <T> EditorContext<T> clone(final EditorContext<T> context, final T initialValue)
    {
        final EditorContext<T> clone = new EditorContext<>(initialValue, context.getDefinition(), context.getParameters(),
                        context.getLabels(), context.getReadableLocales(), context.getWritableLocales());
        clone.setEditable(context.isEditable());
        clone.setEditorLabel(context.getEditorLabel());
        clone.setOptional(context.isOptional());
        clone.setOrdered(context.isOrdered());
        clone.setPartOf(context.isPartOf());
        clone.setPrimitive(context.isPrimitive());
        clone.setSuccessNotificationId(context.getSuccessNotificationId());
        clone.setValueType(context.getValueType());
        clone.setTooltiptext(context.getTooltiptext());
        return clone;
    }
}
