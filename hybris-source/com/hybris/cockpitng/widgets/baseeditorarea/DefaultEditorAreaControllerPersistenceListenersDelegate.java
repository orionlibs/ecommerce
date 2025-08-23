/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultEditorAreaControllerPersistenceListenersDelegate
{
    private final WidgetInstanceManager widgetInstanceManager;
    private final DefaultEditorAreaControllerModelOperationsDelegate modelOperationsDelegate;


    public DefaultEditorAreaControllerPersistenceListenersDelegate(final DefaultEditorAreaController controller)
    {
        widgetInstanceManager = controller.getWidgetInstanceManager();
        modelOperationsDelegate = controller.getModelOperationsDelegate();
    }


    public void executeAfterCancelModificationCallbacks()
    {
        executeModificationCallback("afterCancel", EditorAreaRendererUtils
                        .getAfterCancelListeners(widgetInstanceManager.getModel()));
    }


    public void executeBeforeCancelModificationCallbacks()
    {
        executeModificationCallback("beforeCancel", EditorAreaRendererUtils
                        .getBeforeCancelListeners(widgetInstanceManager.getModel()));
    }


    protected void executeModificationCallback(final String eventName, final Map<String, EventListener<Event>> listeners)
    {
        final Consumer<EventListener<Event>> onEvent = (final EventListener<Event> listener) -> {
            try
            {
                listener.onEvent(new Event(eventName));
            }
            catch(final Exception e)
            {
                throw new IllegalStateException(String.valueOf(modelOperationsDelegate.getCurrentObject()), e);
            }
        };
        if(listeners != null)
        {
            Stream.of(listeners)//
                            .map(Map::values)//
                            .flatMap(Collection::stream)//
                            .forEach(onEvent);
        }
    }


    protected void notifyAfterSaveListeners() throws ObjectSavingException
    {
        final Map<String, EventListener<Event>> afterSaveListeners = EditorAreaRendererUtils
                        .getAfterSaveListeners(widgetInstanceManager.getModel());
        if(afterSaveListeners != null)
        {
            for(final EventListener<Event> listener : afterSaveListeners.values())
            {
                try
                {
                    listener.onEvent(new Event("afterSave"));
                }
                catch(final Exception e)
                {
                    throw new ObjectSavingException(String.valueOf(modelOperationsDelegate.getCurrentObject()), e);
                }
            }
        }
    }


    void resetListeners()
    {
        EditorAreaRendererUtils.clearAfterSaveListeners(widgetInstanceManager.getModel());
        EditorAreaRendererUtils.clearAfterCancelListeners(widgetInstanceManager.getModel());
        EditorAreaRendererUtils.clearBeforeCancelListeners(widgetInstanceManager.getModel());
    }
}

