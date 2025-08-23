/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.EditorState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowStateUtil;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

/**
 * Model observer which is responsible for updating editor state on external changes.
 */
public class InlineEditorRefreshObserver implements ModelObserver
{
    public static final String REFRESH_OBSERVER_ID = "ExtendedMultiReferenceEditorRefreshObserver";
    private final WidgetInstanceManager wim;
    private final String parentObjectKey;
    private final String inlineProperty;
    private final String observerId;
    private ObjectValueService objectValueService;
    private ObjectFacade objectFacade;
    private Object initialParentObject;
    private ValueObserver valueObserver;


    public InlineEditorRefreshObserver(final WidgetInstanceManager wim, final String parentObjectProperty,
                    final String inlineProperty)
    {
        this.wim = wim;
        this.inlineProperty = inlineProperty;
        this.parentObjectKey = parentObjectProperty;
        this.observerId = String.format("%s/%s", REFRESH_OBSERVER_ID, inlineProperty);
    }


    /**
     * Adds this observer as model observer
     */
    public void startObservingModel()
    {
        this.initialParentObject = wim.getModel().getValue(parentObjectKey, Object.class);
        wim.getModel().addObserver(parentObjectKey, this);
    }


    /**
     * Removes this observer from model observers.
     */
    public void stopObservingModel()
    {
        wim.getModel().removeObserver(parentObjectKey, observerId);
    }


    @Override
    public void modelChanged()
    {
        if(hasParentObjectInstanceChanged())
        {
            onParentObjectInstanceChanged();
        }
        else
        {
            performRefresh();
            if(valueObserver != null)
            {
                valueObserver.modelChanged();
            }
        }
    }


    @Override
    public void modelChanged(final String property)
    {
        if(hasParentObjectInstanceChanged())
        {
            onParentObjectInstanceChanged();
        }
        else
        {
            performRefresh();
            if(valueObserver != null)
            {
                valueObserver.modelChanged(property);
            }
        }
    }


    @Override
    public Object getId()
    {
        return observerId;
    }


    /**
     * Registers value observer which will be called model change.
     *
     * @param valueObserver
     *           invoked when model changes.
     */
    public void setValueObserver(final ValueObserver valueObserver)
    {
        this.valueObserver = valueObserver;
    }


    /**
     * Registers event consumer which should refresh state of an editor.
     *
     * @param refreshEventConsumer
     *           refresh event consumer.
     */
    public void setRefreshEventConsumer(final Consumer<InlineEditorRefreshEvent> refreshEventConsumer)
    {
        Validate.notNull("Refresh consumer cannot be null", refreshEventConsumer);
        final String inlineEventName = InlineEditorRefreshEvent.getInlineEventName(inlineProperty);
        wim.getWidgetslot().addEventListener(inlineEventName, new EventListener<InlineEditorRefreshEvent>()
        {
            @Override
            public void onEvent(final InlineEditorRefreshEvent event) throws Exception
            {
                if(event.isReload())
                {
                    wim.getWidgetslot().removeEventListener(event.getName(), this);
                }
                refreshEventConsumer.accept(event);
            }
        });
    }


    protected void onParentObjectInstanceChanged()
    {
        stopObservingModel();
        RowStateUtil.resetEditorState(wim, inlineProperty);
        if(isParentObjectReloaded())
        {
            sendRefreshEvent(new InlineEditorRefreshEvent(inlineProperty));
        }
        else
        {
            removeAllEventListenersFromWidgetSlot();
        }
    }


    protected void removeAllEventListenersFromWidgetSlot()
    {
        final Widgetslot widgetslot = wim.getWidgetslot();
        final String inlineEventName = InlineEditorRefreshEvent.getInlineEventName(inlineProperty);
        widgetslot.getEventListeners(inlineEventName).forEach(event -> widgetslot.removeEventListener(inlineEventName, event));
    }


    protected void performRefresh()
    {
        final EditorState editorState = RowStateUtil.getExtendedMultiReferenceEditorState(wim, inlineProperty);
        if(editorState != null)
        {
            final List<Object> itemsToRefresh = getRowItemsToRefresh(editorState);
            if(CollectionUtils.isNotEmpty(itemsToRefresh))
            {
                refreshCorrespondingRowStates(itemsToRefresh, editorState);
                sendRefreshEvent(new InlineEditorRefreshEvent(inlineProperty, itemsToRefresh));
            }
        }
    }


    protected void sendRefreshEvent(final InlineEditorRefreshEvent event)
    {
        Events.sendEvent(wim.getWidgetslot(), event);
    }


    protected List<Object> getRowItemsToRefresh(final EditorState editorState)
    {
        if(editorState != null)
        {
            final Predicate<Object> isRowObsolete = (item) -> {
                final RowState rowState = editorState.getRowState(item);
                return rowState != null
                                && (rowState.getRow() != item || (rowState.isRowModified() && !getObjectFacade().isModified(item)));
            };
            return getObservedMultiReference().stream().filter(isRowObsolete).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    protected void refreshCorrespondingRowStates(final List<Object> itemsToRefresh, final EditorState editorState)
    {
        if(editorState != null && CollectionUtils.isNotEmpty(itemsToRefresh))
        {
            itemsToRefresh.forEach(item -> {
                final RowState rowState = editorState.getRowState(item);
                if(rowState != null)
                {
                    if(!item.equals(rowState.getRow()))
                    {
                        applyRowChangesOnRefreshedItem(editorState, rowState, item);
                    }
                    else//item saved
                    {
                        rowState.resetModifiedFields();
                    }
                }
            });
        }
    }


    protected void applyRowChangesOnRefreshedItem(final EditorState editorState, final RowState rowState,
                    final Object refreshedItem)
    {
        if(editorState != null && rowState != null)
        {
            final Set<String> modifiedProperties = rowState.getModifiedProperties();
            modifiedProperties.forEach(property -> {
                final Object editedValue = getObjectValueService().getValue(property, rowState.getRow());
                getObjectValueService().setValue(property, refreshedItem, editedValue);
            });
            editorState.refreshRowStateReference(refreshedItem);
        }
    }


    /**
     * Tells if parent object has changed either to different object or just instance has changed.
     *
     * @return true if object is changed to difference instance.
     */
    protected boolean hasParentObjectInstanceChanged()
    {
        return initialParentObject != getCurrentParentObject();
    }


    protected boolean isParentObjectReloaded()
    {
        final Object currentParentObject = getCurrentParentObject();
        return currentParentObject != null && currentParentObject.equals(initialParentObject) && hasParentObjectInstanceChanged();
    }


    protected Object getCurrentParentObject()
    {
        return wim.getModel().getValue(parentObjectKey, Object.class);
    }


    protected Object getInitialParentObject()
    {
        return initialParentObject;
    }


    protected Collection<Object> getObservedMultiReference()
    {
        return ObjectUtils.defaultIfNull(wim.getModel().getValue(inlineProperty, Collection.class), Collections.emptyList());
    }


    protected ObjectFacade getObjectFacade()
    {
        if(objectFacade == null)
        {
            objectFacade = BackofficeSpringUtil.getBean("objectFacade", ObjectFacade.class);
        }
        return objectFacade;
    }


    protected ObjectValueService getObjectValueService()
    {
        if(objectValueService == null)
        {
            objectValueService = BackofficeSpringUtil.getBean("objectValueService", ObjectValueService.class);
        }
        return objectValueService;
    }
}
