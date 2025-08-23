/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.widgets.common.WidgetComponentRendererListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * Utility methods that can be used in a implementation of
 * {@link com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaComponentRenderer}.
 */
public final class EditorAreaRendererUtils
{
    public static final String MODEL_EDITOR_AREA_AFTER_SAVE_LISTENERS_MAP = "_editorAreaAfterSaveListenersMap";
    public static final String MODEL_EDITOR_AREA_AFTER_CANCEL_LISTENERS_MAP = "_editorAreaAfterCancelListenersMap";
    public static final String MODEL_EDITOR_AREA_BEFORE_CANCEL_LISTENERS_MAP = "_editorAreaBeforeCancelListenersMap";
    public static final String MODEL_EDITOR_AREA_RENDERER_LISTENER = "_editorAreaRendererListener";
    private static final String ANCHOR_ATTRIBUTE = "qualifier";
    private static final ObjectValuePath CONTEXT_OBJECT_PATH = ObjectValuePath.parse(StandardModelKeys.CONTEXT_OBJECT);


    private EditorAreaRendererUtils()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    public static WidgetComponentRendererListener<Component, EditorArea, Object> putRendererListener(final WidgetModel widgetModel,
                    final WidgetComponentRendererListener<Component, EditorArea, Object> listener)
    {
        final WidgetComponentRendererListener<Component, EditorArea, Object> currentListener = widgetModel
                        .getValue(MODEL_EDITOR_AREA_RENDERER_LISTENER, WidgetComponentRendererListener.class);
        widgetModel.setValue(MODEL_EDITOR_AREA_RENDERER_LISTENER, listener);
        return currentListener;
    }


    public static String getEditorAnchorAttributeName()
    {
        return ANCHOR_ATTRIBUTE;
    }


    public static String getEditorAnchor(final String property)
    {
        return ObjectValuePath.getNotLocalizedPath(property);
    }


    public static String getEditorAnchor(final Editor editor)
    {
        final String property = editor.getProperty();
        return getEditorAnchor(property);
    }


    /**
     * Attaches a listener to the after-save listeners map in the widgetModel. This map is usually processed by the
     * corresponding widget controller, e.g. by the editorArea save button event listener.
     */
    public static void setAfterSaveListener(final WidgetModel widgetModel, final String id, final EventListener<Event> listener,
                    final boolean notifyChange)
    {
        Map<String, EventListener<Event>> value = widgetModel.getValue(MODEL_EDITOR_AREA_AFTER_SAVE_LISTENERS_MAP, Map.class);
        if(value == null)
        {
            value = new ConcurrentHashMap<>();
            widgetModel.put(MODEL_EDITOR_AREA_AFTER_SAVE_LISTENERS_MAP, value);
        }
        value.put(id, listener);
        if(notifyChange)
        {
            widgetModel.changed();
        }
    }


    /**
     * Removes the after-save listener with the given id from the model.
     */
    public static void removeAfterSaveListener(final WidgetModel widgetModel, final String id)
    {
        final Map<String, EventListener<Event>> value = widgetModel.getValue(MODEL_EDITOR_AREA_AFTER_SAVE_LISTENERS_MAP, Map.class);
        if(value != null)
        {
            value.remove(id);
        }
    }


    /**
     * Returns all after-save listeners stored at the model.
     */
    public static Map<String, EventListener<Event>> getAfterSaveListeners(final WidgetModel widgetModel)
    {
        return widgetModel.getValue(MODEL_EDITOR_AREA_AFTER_SAVE_LISTENERS_MAP, Map.class);
    }


    /**
     * Removes all after-save listeners stored at the model.
     */
    public static void clearAfterSaveListeners(final WidgetModel widgetModel)
    {
        widgetModel.put(MODEL_EDITOR_AREA_AFTER_SAVE_LISTENERS_MAP, null);
    }


    /**
     * Attaches a listener to the after-cancel listeners map in the widgetModel. This map is usually processed by the
     * corresponding widget controller, e.g. by the editorArea cancel button event listener.
     */
    public static void setAfterCancelListener(final WidgetModel widgetModel, final String id, final EventListener<Event> listener,
                    final boolean notifyChange)
    {
        Map<String, EventListener<Event>> value = widgetModel.getValue(MODEL_EDITOR_AREA_AFTER_CANCEL_LISTENERS_MAP, Map.class);
        if(value == null)
        {
            value = new ConcurrentHashMap<>();
            widgetModel.put(MODEL_EDITOR_AREA_AFTER_CANCEL_LISTENERS_MAP, value);
        }
        value.put(id, listener);
        if(notifyChange)
        {
            widgetModel.changed();
        }
    }


    /**
     * Attaches a listener to the before-cancel listeners map in the widgetModel. This map is usually processed by the
     * corresponding widget controller, e.g. by the editorArea cancel button event listener.
     */
    public static void setBeforeCancelListener(final WidgetModel widgetModel, final String id, final EventListener<Event> listener,
                    final boolean notifyChange)
    {
        Map<String, EventListener<Event>> value = widgetModel.getValue(MODEL_EDITOR_AREA_BEFORE_CANCEL_LISTENERS_MAP, Map.class);
        if(value == null)
        {
            value = new ConcurrentHashMap<>();
            widgetModel.put(MODEL_EDITOR_AREA_BEFORE_CANCEL_LISTENERS_MAP, value);
        }
        value.put(id, listener);
        if(notifyChange)
        {
            widgetModel.changed();
        }
    }


    /**
     * Removes the after-cancel listener with the given id from the model.
     */
    public static void removeAfterCancelListener(final WidgetModel widgetModel, final String id)
    {
        final Map<String, EventListener<Event>> value = widgetModel.getValue(MODEL_EDITOR_AREA_AFTER_CANCEL_LISTENERS_MAP,
                        Map.class);
        if(value != null)
        {
            value.remove(id);
        }
    }


    /**
     * Removes the before-cancel listener with the given id from the model.
     */
    public static void removeBeforeCancelListener(final WidgetModel widgetModel, final String id)
    {
        final Map<String, EventListener<Event>> value = widgetModel.getValue(MODEL_EDITOR_AREA_BEFORE_CANCEL_LISTENERS_MAP,
                        Map.class);
        if(value != null)
        {
            value.remove(id);
        }
    }


    /**
     * Returns all after-cancel listeners stored at the model.
     */
    public static Map<String, EventListener<Event>> getAfterCancelListeners(final WidgetModel widgetModel)
    {
        return widgetModel.getValue(MODEL_EDITOR_AREA_AFTER_CANCEL_LISTENERS_MAP, Map.class);
    }


    /**
     * Returns all before-cancel listeners stored at the model.
     */
    public static Map<String, EventListener<Event>> getBeforeCancelListeners(final WidgetModel widgetModel)
    {
        return widgetModel.getValue(MODEL_EDITOR_AREA_BEFORE_CANCEL_LISTENERS_MAP, Map.class);
    }


    /**
     * Removes all after-cancel listeners stored at the model.
     */
    public static void clearAfterCancelListeners(final WidgetModel widgetModel)
    {
        widgetModel.put(MODEL_EDITOR_AREA_AFTER_CANCEL_LISTENERS_MAP, null);
    }


    /**
     * Removes all before-cancel listeners stored at the model.
     */
    public static void clearBeforeCancelListeners(final WidgetModel widgetModel)
    {
        widgetModel.put(MODEL_EDITOR_AREA_BEFORE_CANCEL_LISTENERS_MAP, null);
    }


    public static String getAbsoluteAttributePath(final String qualifier)
    {
        final ObjectValuePath path = ObjectValuePath.parse(qualifier);
        if(!path.startsWith(CONTEXT_OBJECT_PATH))
        {
            return path.prepend(CONTEXT_OBJECT_PATH).buildPath();
        }
        else
        {
            return qualifier;
        }
    }


    public static String getRelativeAttributePath(final String path)
    {
        final ObjectValuePath ovp = ObjectValuePath.parse(path);
        if(CONTEXT_OBJECT_PATH.equals(ovp))
        {
            return StringUtils.EMPTY;
        }
        else if(ovp.startsWith(CONTEXT_OBJECT_PATH))
        {
            return ovp.getRelative(CONTEXT_OBJECT_PATH).buildPath();
        }
        else
        {
            return path;
        }
    }


    public static Collection<AbstractSection> getSections(final Tab tab)
    {
        final List<AbstractSection> sections = new ArrayList<AbstractSection>(tab.getCustomSectionOrSection());
        if(tab.getEssentials() != null && tab.isDisplayEssentialSectionIfPresent())
        {
            if(tab.getEssentials().getEssentialSection() != null)
            {
                sections.add(0, tab.getEssentials().getEssentialSection());
            }
            else if(tab.getEssentials().getEssentialCustomSection() != null)
            {
                sections.add(0, tab.getEssentials().getEssentialCustomSection());
            }
        }
        return sections;
    }
}
