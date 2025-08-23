/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.collectioneditorarea;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;

public class CollectionEditorAreaController extends DefaultWidgetController
{
    protected static final String SOCKET_INPUT_DATA = "inputData";
    protected static final String SOCKET_INPUT_SELECTED_OBJECT = "currentObject";
    protected static final String SOCKET_INPUT_MODIFIED_OBJECT = "modifiedObject";
    protected static final String SOCKET_OUTPUT_EDIT_OBJECT = "selectedObject";
    protected static final String SOCKET_OUTPUT_SINGLE_OBJECT_SAVED = "singleObjectSaved";
    protected static final String SOCKET_OUTPUT_PAGEABLE = "pageable";
    protected static final String WIDGET_STATE_LAST_INPUT = "lastInput";
    protected static final String WIDGET_STATE_SELECTED_OBJECT = "selectedObject";
    protected static final String COMPONENT_NEXT = "next";
    protected static final String COMPONENT_PREVIOUS = "previous";
    protected static final String WIDGET_SETTING_PAGE_SIZE = "pageSize";
    protected static final String WIDGET_SETTING_NAVIGATION_ON = "navigationOn";
    protected static final String SINGLE_REFERENCE_SCLASS = "yw-single-reference";
    protected static final String MULTI_REFERENCE_SCLASS = "yw-multi-reference";
    protected Component next;
    protected Component previous;
    protected Component east;
    protected HtmlBasedComponent mainCnt;
    protected Widgetslot editorArea;
    protected Widgetslot list;
    private transient LabelService labelService;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final TypeAwareSelectionContext context = getValue(WIDGET_STATE_LAST_INPUT, TypeAwareSelectionContext.class);
        if(context != null)
        {
            applyAdditionalContextProperties(context);
        }
        dataInput(context);
    }


    protected void applyAdditionalContextProperties(final TypeAwareSelectionContext context)
    {
        final Set<String> parameterKeys = context.getParameters().keySet();
        final String editorAreaPrefix = "editorArea/";
        final String listPrefix = "list/";
        boolean updateEditorArea = false;
        boolean updateListSlot = false;
        for(final String parameterKey : parameterKeys)
        {
            if(parameterKey.startsWith(editorAreaPrefix))
            {
                editorArea.getSettings().put(parameterKey.substring(editorAreaPrefix.length()),
                                context.getParameters().get(parameterKey));
                updateEditorArea = true;
            }
            else if(parameterKey.startsWith(listPrefix))
            {
                list.getSettings().put(parameterKey.substring(listPrefix.length()), context.getParameters().get(parameterKey));
                updateListSlot = true;
            }
        }
        if(editorArea != null && editorArea.getWidgetInstance() != null //
                        && editorArea.getWidgetInstance().getModel() instanceof Map //
                        && context.getAvailableItems().size() > 1)
        {
            updateEditorArea = true;
        }
        if(updateEditorArea)
        {
            editorArea.updateView();
        }
        if(updateListSlot)
        {
            list.updateView();
        }
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
    public void onDelete(final CockpitEvent cockpitEvent)
    {
        final TypeAwareSelectionContext context = getValue(WIDGET_STATE_LAST_INPUT, TypeAwareSelectionContext.class);
        if(context == null)
        {
            return;
        }
        final List availableItems = context.getAvailableItems();
        final List subtractedItems = new ArrayList(CollectionUtils.subtract(availableItems, cockpitEvent.getDataAsCollection()));
        if(CollectionUtils.isEmpty(subtractedItems) || subtractedItems.size() == availableItems.size())
        {
            return;
        }
        context.setAvailableItems(subtractedItems);
        context.setSelectedItem(subtractedItems.get(0));
        dataInput(context);
    }


    @SocketEvent(socketId = SOCKET_INPUT_DATA)
    public void dataInput(final TypeAwareSelectionContext context)
    {
        if(context == null)
        {
            return;
        }
        setValue(WIDGET_STATE_LAST_INPUT, context);
        applyAdditionalContextProperties(context);
        final List<Object> selectedItems = context.getAvailableItems();
        if(CollectionUtils.isNotEmpty(selectedItems))
        {
            final boolean moreThenOneElement = CollectionUtils.size(selectedItems) > 1;
            east.setVisible(moreThenOneElement);
            if(moreThenOneElement)
            {
                final PageableList<?> pageable = new PageableList<>(selectedItems,
                                getWidgetSettings().getInt(WIDGET_SETTING_PAGE_SIZE), context.getTypeCode());
                sendOutput(SOCKET_OUTPUT_PAGEABLE, pageable);
            }
            UITools.modifySClass(mainCnt, SINGLE_REFERENCE_SCLASS, !moreThenOneElement);
            UITools.modifySClass(mainCnt, MULTI_REFERENCE_SCLASS, moreThenOneElement);
        }
        selectObject(context.getSelectedItem());
    }


    @SocketEvent(socketId = SOCKET_INPUT_SELECTED_OBJECT)
    public void selectObject(final Object object)
    {
        if(object == null && getValue(WIDGET_STATE_SELECTED_OBJECT, Object.class) == null)
        {
            return;
        }
        setValue(WIDGET_STATE_SELECTED_OBJECT, object);
        sendOutput(SOCKET_OUTPUT_EDIT_OBJECT, object);
        if(isNavigationOn())
        {
            next.setVisible(isNotLast(object));
            previous.setVisible(isNotFirst(object));
        }
        updateWidgetTitle();
    }


    @SocketEvent(socketId = SOCKET_INPUT_MODIFIED_OBJECT)
    public void handleModifiedObject(final Object object)
    {
        if(getSizeOfLastInput() == 1)
        {
            sendOutput(SOCKET_OUTPUT_SINGLE_OBJECT_SAVED, Boolean.TRUE);
        }
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = COMPONENT_NEXT)
    public void selectNext()
    {
        final TypeAwareSelectionContext lastInput = getValue(WIDGET_STATE_LAST_INPUT, TypeAwareSelectionContext.class);
        if(!isNavigationOn() || lastInput == null)
        {
            return;
        }
        final Object currentSelected = getValue(WIDGET_STATE_SELECTED_OBJECT, Object.class);
        final List<Object> availableItems = lastInput.getAvailableItems();
        if(currentSelected != null)
        {
            final int index = availableItems.indexOf(currentSelected);
            if(index >= 0 && index < availableItems.size() - 1)
            {
                final Object object = availableItems.get(index + 1);
                selectObject(object);
            }
        }
        updateWidgetTitle();
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = COMPONENT_PREVIOUS)
    public void selectPrevious()
    {
        final TypeAwareSelectionContext lastInput = getValue(WIDGET_STATE_LAST_INPUT, TypeAwareSelectionContext.class);
        if(!isNavigationOn() || lastInput == null)
        {
            return;
        }
        final Object currentSelected = getValue(WIDGET_STATE_SELECTED_OBJECT, Object.class);
        final List<Object> availableItems = lastInput.getAvailableItems();
        if(currentSelected != null)
        {
            final int index = availableItems.indexOf(currentSelected);
            if(index > 0)
            {
                final Object object = availableItems.get(index - 1);
                selectObject(object);
            }
        }
        updateWidgetTitle();
    }


    protected void updateWidgetTitle()
    {
        updateWidgetTitile();
    }


    /**
     * @deprecated since 1808 due to a typo
     * @see #updateWidgetTitle()
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected void updateWidgetTitile()
    {
        final Object currentSelected = getValue(WIDGET_STATE_SELECTED_OBJECT, Object.class);
        if(currentSelected != null)
        {
            getWidgetInstanceManager().setTitle(getLabel("collectioneditor.title", new Object[]
                            {labelService.getObjectLabel(currentSelected)}));
        }
    }


    protected boolean isNotFirst(final Object object)
    {
        return getItemPositionInLastInput(object) > 0;
    }


    protected boolean isNotLast(final Object object)
    {
        final int position = getItemPositionInLastInput(object);
        final int size = getSizeOfLastInput();
        return position >= 0 && position != (size - 1);
    }


    private int getItemPositionInLastInput(final Object object)
    {
        int index = 0;
        final TypeAwareSelectionContext lastInput = getValue(WIDGET_STATE_LAST_INPUT, TypeAwareSelectionContext.class);
        if(lastInput != null && CollectionUtils.isNotEmpty(lastInput.getAvailableItems()))
        {
            index = lastInput.getAvailableItems().indexOf(object);
        }
        return index;
    }


    private int getSizeOfLastInput()
    {
        int size = 0;
        final TypeAwareSelectionContext lastInput = getValue(WIDGET_STATE_LAST_INPUT, TypeAwareSelectionContext.class);
        if(lastInput != null && CollectionUtils.isNotEmpty(lastInput.getAvailableItems()))
        {
            size = lastInput.getAvailableItems().size();
        }
        return size;
    }


    private boolean isNavigationOn()
    {
        return BooleanUtils.isTrue(getWidgetSettings().getBoolean(WIDGET_SETTING_NAVIGATION_ON));
    }


    public Component getNext()
    {
        return next;
    }


    public Component getPrevious()
    {
        return previous;
    }


    public Component getEast()
    {
        return east;
    }


    public HtmlBasedComponent getMainCnt()
    {
        return mainCnt;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }
}
