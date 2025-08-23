/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultmultireferenceeditor;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.editor.commonreferenceeditor.AbstractReferenceEditor;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;

/**
 * Reference editor
 */
public class DefaultMultiReferenceEditor<T> extends AbstractReferenceEditor<T, Collection<T>>
                implements CockpitEditorRenderer<Collection<T>>
{
    public static final String PARAM_SELECTED_ITEMS_MAX_SIZE = "selectedItemsMaxSize";
    public static final String PARAM_RENDER_ON_DEMAND_SIZE = "renderOnDemandSize";
    public static final String COCKPIT_PROPERTY_MULTI_REFERENCE_EDITOR_RENDER_ON_DEMAND_SIZE = "multi.reference.editor.render.on.demand.size";
    public static final String COCKPIT_PROPERTY_SELECTED_ITEMS_MAX_SIZE = "multi.reference.editor.selected.items.max.size";
    protected static final String SELECTED_ITEMS_TYPE_COLLECTION = "COLLECTION";
    protected static final String SELECTED_ITEMS_TYPE_LIST = "LIST";
    protected static final String SELECTED_ITEMS_TYPE_SET = "SET";
    private static final boolean HIDE_BANDBOX_WHEN_ANY_ITEM_SELECTED = false;
    private static final Pattern REGEX_EDITOR_PATTERN = Pattern.compile("^MultiReference-(COLLECTION|LIST|SET)\\((.*)\\)$");
    protected Collection<T> selectedItems = new HashSet<>();
    @Autowired
    protected CockpitProperties cockpitProperties;


    @Override
    public void render(final Component parent, final EditorContext<Collection<T>> context,
                    final EditorListener<Collection<T>> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Object parentObject = parent.getAttribute(PARENT_OBJECT);
        setParentObject(parentObject);
        setEditorContext(context);
        setEditorListener(listener);
        setTypeCode(readTypeCode(context.getValueType()));
        setEditorParameters(context);
        setEditorLayout(createReferenceLayout(context));
        setParentEditor(findAncestorEditor(parent));
        initializeSelectedItemsCollection(getCollectionType(context.getValueType()));
        getEditorLayout().setSelectedItemsMaxSize(selectedItemsMaxSize);
        getEditorLayout().setRenderOnDemandSize(renderOnDemandSize);
        getEditorLayout().createLayout(parent);
        getEditorLayout().addListeners();
        setInitialValue(context);
        getEditorLayout().setEditableState(context.isEditable());
        getEditorLayout().setOrdered(context.isOrdered());
        addSocketInputEventListener(SOCKET_IN_REFERENCE_EDITOR, createInputSocketEventListener());
        setSuccessNotificationId(context.getSuccessNotificationId());
    }


    protected void setEditorParameters(final EditorContext<Collection<T>> context)
    {
        setCommonEditorParameters(context.getParameters());
    }


    protected void setInitialValue(final EditorContext<Collection<T>> context)
    {
        changeSelectedObject(context.getInitialValue());
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
            if(groupCount < 2)
            {
                throw new IllegalStateException("Could not capture group representing type code. Group count: " + groupCount);
            }
            return matcher.group(2);
        }
        else
        {
            throw new IllegalArgumentException("Improper value type: " + valueType);
        }
    }


    protected String getCollectionType(final String valueType)
    {
        final Pattern pattern = getRegexEditorPattern();
        if(pattern == null)
        {
            throw new IllegalStateException("Provided Pattern may not be null");
        }
        final Matcher matcher = pattern.matcher(valueType);
        if(matcher.matches())
        {
            final int groupCount = matcher.groupCount();
            if(groupCount < 2)
            {
                throw new IllegalStateException("Could not capture group representing collection. Group count: " + groupCount);
            }
            return matcher.group(1);
        }
        else
        {
            throw new IllegalArgumentException("Improper collection type: " + valueType);
        }
    }


    protected void initializeSelectedItemsCollection(final String collectionType)
    {
        if(SELECTED_ITEMS_TYPE_COLLECTION.equals(collectionType) || SELECTED_ITEMS_TYPE_LIST.equals(collectionType))
        {
            selectedItems = new ArrayList<>();
        }
        else if(SELECTED_ITEMS_TYPE_SET.equals(collectionType))
        {
            selectedItems = new HashSet<>();
        }
    }


    protected void changeSelectedObject(final Collection<T> list)
    {
        if(list != null)
        {
            selectedItems.addAll(list);
            list.forEach((final T obj) -> {
                if(getPermissionFacade().canReadInstance(obj))
                {
                    getEditorLayout().onAddSelectedObject(obj, HIDE_BANDBOX_WHEN_ANY_ITEM_SELECTED);
                }
            });
        }
    }


    @Override
    public void addSelectedObject(final T obj)
    {
        if(selectedItems.contains(obj))
        {
            selectedItems.remove(obj);
        }
        final boolean added = selectedItems.add(obj);
        if(added)
        {
            getEditorLayout().onAddSelectedObject(obj, HIDE_BANDBOX_WHEN_ANY_ITEM_SELECTED);
            if(getEditorListener() != null)
            {
                getEditorListener().onValueChanged(selectedItems);
            }
        }
    }


    @Override
    public void removeSelectedObject(final T obj)
    {
        final boolean removed = selectedItems.remove(obj);
        if(removed)
        {
            getEditorLayout().onRemoveSelectedObject(obj, HIDE_BANDBOX_WHEN_ANY_ITEM_SELECTED);
            if(getEditorListener() != null)
            {
                getEditorListener().onValueChanged(selectedItems);
            }
        }
    }


    @Override
    public void refreshObjects(final Collection<T> objects)
    {
        selectedItems = objects;
        if(getEditorListener() != null)
        {
            getEditorListener().onValueChanged(selectedItems);
        }
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
    protected void setCommonEditorParameters(final Map<String, Object> parametersFromConfig)
    {
        super.setCommonEditorParameters(parametersFromConfig);
        extractSelectedItemsMaxSize(parametersFromConfig);
        extractRenderOnDemandSize(parametersFromConfig);
    }


    protected void extractSelectedItemsMaxSize(final Map<String, Object> parametersFromConfig)
    {
        if(parametersFromConfig.containsKey(PARAM_SELECTED_ITEMS_MAX_SIZE))
        {
            final String paramValue = (String)parametersFromConfig.get(PARAM_SELECTED_ITEMS_MAX_SIZE);
            setSelectedItemsMaxSize(Integer.parseInt(paramValue));
        }
        else if(cockpitProperties.getProperty(COCKPIT_PROPERTY_SELECTED_ITEMS_MAX_SIZE) != null)
        {
            setSelectedItemsMaxSize(Integer.parseInt(cockpitProperties.getProperty(COCKPIT_PROPERTY_SELECTED_ITEMS_MAX_SIZE)));
        }
        else
        {
            setSelectedItemsMaxSize(5);
        }
    }


    protected void extractRenderOnDemandSize(final Map<String, Object> parametersFromConfig)
    {
        if(parametersFromConfig.containsKey(PARAM_RENDER_ON_DEMAND_SIZE))
        {
            final String paramValue = (String)parametersFromConfig.get(PARAM_RENDER_ON_DEMAND_SIZE);
            setRenderOnDemandSize(Integer.parseInt(paramValue));
        }
        else if(cockpitProperties.getProperty(COCKPIT_PROPERTY_MULTI_REFERENCE_EDITOR_RENDER_ON_DEMAND_SIZE) != null)
        {
            setRenderOnDemandSize(
                            Integer.parseInt(cockpitProperties.getProperty(COCKPIT_PROPERTY_MULTI_REFERENCE_EDITOR_RENDER_ON_DEMAND_SIZE)));
        }
        else
        {
            setRenderOnDemandSize(50);
        }
    }


    @Override
    public void openReferenceAdvancedSearch(final Collection<T> currentlySelected)
    {
        final TypeAwareSelectionContext typeAwareSelectionContext = new TypeAwareSelectionContext(null,
                        currentlySelected == null ? Collections.emptyList() : Lists.newArrayList(currentlySelected));
        typeAwareSelectionContext.setTypeCode(getTypeCode());
        typeAwareSelectionContext.setMultiSelect(true);
        typeAwareSelectionContext.addParameter(TypeAwareSelectionContext.SEARCH_CTX_PARAM, getReferenceSearchContextMap());
        getEditorContext().getParameters().forEach(typeAwareSelectionContext::addParameter);
        sendOutput(SOCKET_OUT_REFERENCE_SEARCH_CTX, typeAwareSelectionContext);
    }
}
