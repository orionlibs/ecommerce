/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultreferenceeditor;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editor.commonreferenceeditor.AbstractReferenceEditor;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.zkoss.zk.ui.Component;

/**
 * Reference editor
 */
public class DefaultReferenceEditor<T> extends AbstractReferenceEditor<T, T> implements CockpitEditorRenderer<T>
{
    private static final Pattern REGEX_EDITOR_PATTERN = Pattern.compile("^Reference\\((.*)\\)$");
    private static final boolean HIDE_BANDBOX_WHEN_ANY_ITEM_SELECTED = true;


    @Override
    public void render(final Component parent, final EditorContext<T> context, final EditorListener<T> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        setTypeCode(readTypeCode(context.getValueType()));
        setEditorParameters(context);
        setEditorLayout(createReferenceLayout(context));
        setParentEditor(findAncestorEditor(parent));
        final Object parentObject = parent.getAttribute(PARENT_OBJECT);
        setParentObject(parentObject);
        setEditorListener(listener);
        setEditorContext(context);
        getEditorLayout().createLayout(parent);
        getEditorLayout().addListeners();
        setInitialValue(context);
        getEditorLayout().setEditableState(context.isEditable());
        addSocketInputEventListener(SOCKET_IN_REFERENCE_EDITOR, createInputSocketEventListener());
        setSuccessNotificationId(context.getSuccessNotificationId());
    }


    protected void setEditorParameters(final EditorContext<T> context)
    {
        setCommonEditorParameters(context.getParameters());
    }


    protected void setInitialValue(final EditorContext<T> context)
    {
        getEditorLayout().clearSelection();
        getEditorLayout().onAddSelectedObject(context.getInitialValue(), HIDE_BANDBOX_WHEN_ANY_ITEM_SELECTED);
    }


    @Override
    public void addSelectedObject(final T obj)
    {
        getEditorLayout().clearSelection();
        getEditorLayout().onAddSelectedObject(obj, HIDE_BANDBOX_WHEN_ANY_ITEM_SELECTED);
        if(getEditorListener() != null)
        {
            getEditorListener().onValueChanged(obj);
        }
    }


    public void removeSelectedObject(final T obj)
    {
        getEditorLayout().onRemoveSelectedObject(obj, HIDE_BANDBOX_WHEN_ANY_ITEM_SELECTED);
        if(getEditorListener() != null)
        {
            getEditorListener().onValueChanged(null);
        }
    }


    @Override
    public void refreshObjects(final Collection<T> objects)
    {
        Validate.notNull("Collection of refreshed objects may not be null", objects);
        if(getEditorListener() != null)
        {
            for(final T obj : objects)
            {
                getEditorListener().onValueChanged(obj);
            }
        }
    }


    @Override
    public void forwardEditorEvent(final String eventCode)
    {
        getEditorListener().onEditorEvent(eventCode);
    }


    @Override
    public boolean isEditable()
    {
        return getEditorContext().isEditable();
    }


    @Override
    protected Pattern getRegexEditorPattern()
    {
        return REGEX_EDITOR_PATTERN;
    }


    @Override
    public String readTypeCode(final String valueType)
    {
        Validate.notNull("Value type may not be null", valueType);
        final Pattern pattern = getRegexEditorPattern();
        if(pattern == null)
        {
            throw new IllegalStateException("Provided Pattern may not be null");
        }
        final Matcher matcher = pattern.matcher(valueType);
        if(matcher.matches())
        {
            final int groupCount = matcher.groupCount();
            if(groupCount < 1)
            {
                throw new IllegalStateException("Could not capture group representing type code. Group count: " + groupCount);
            }
            return matcher.group(1);
        }
        else
        {
            throw new IllegalArgumentException("Improper value type: " + valueType);
        }
    }
}
