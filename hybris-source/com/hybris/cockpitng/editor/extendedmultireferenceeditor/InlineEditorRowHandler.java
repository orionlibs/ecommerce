/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor;

import static com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade.CTX_PARAM_SUPPRESS_EVENT;
import static java.util.stream.Collectors.toMap;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavePermissionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.EditorState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowStateUtil;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Events;

public class InlineEditorRowHandler<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(InlineEditorRowHandler.class);
    private ObjectFacade objectFacade;
    private ObjectValueService objectValueService;
    private LabelService labelService;
    private NotificationService notificationService;


    public Object getInitialValue(final T object, final String property)
    {
        try
        {
            final Object reloaded = getObjectFacade().reload(object);
            return getObjectValueService().getValue(property, reloaded);
        }
        catch(final ObjectNotFoundException e)
        {
            LOG.warn("Cannot get initial changes from row", e);
        }
        return null;
    }


    public void saveRow(final Object entry, final EditorState editorState, final EditorContext context)
    {
        final RowState rowState = editorState.getRowState(entry);
        save(Collections.singletonMap((T)entry, rowState), context);
    }


    public T undoRow(final T entry, final EditorState editorState, final WidgetInstanceManager widgetInstanceManager,
                    final String rowProperty, final EditorContext context)
    {
        T reloaded = null;
        final RowState<T> rowState = editorState.getRowState(entry);
        try
        {
            reloaded = getObjectFacade().reload(entry);
            for(final String key : rowState.getModifiedProperties())
            {
                final Object previousValue = getObjectValueService().getValue(key, reloaded);
                widgetInstanceManager.getModel().setValue(rowProperty + "." + key, previousValue);
            }
            rowState.setError(false);
            rowState.resetModifiedFields();
            refreshRows(context, List.of(reloaded));
        }
        catch(final ObjectNotFoundException e)
        {
            rowState.setError(true);
            LOG.error("Cannot undo changes in row", e);
            getNotificationService().notifyUser(getNotificationSource(context), NotificationEventTypes.EVENT_TYPE_OBJECT_LOAD,
                            Level.FAILURE, Collections.singletonMap(entry, e));
        }
        return reloaded;
    }


    public void doGlobalSave(final WidgetInstanceManager widgetInstanceManager, final String editorProperty,
                    final EditorContext context)
    {
        final EditorState<T> editorState = RowStateUtil.getExtendedMultiReferenceEditorState(widgetInstanceManager, editorProperty);
        final Map<T, RowState<T>> toSave = editorState.getEntries().stream()
                        .filter(rowStateEntry -> rowStateEntry.getValue().isRowModified())
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        save(toSave, context);
    }


    protected ObjectFacadeOperationResult save(final Map<T, RowState<T>> entries, final EditorContext context)
    {
        final ObjectFacadeOperationResult result = new ObjectFacadeOperationResult();
        entries.forEach((key, value) -> {
            try
            {
                saveRowByState(key, value);
                result.addSuccessfulObject(key);
                value.setError(false);
            }
            catch(final ObjectSavingException e)
            {
                LOG.error(e.getLocalizedMessage(), e);
                result.addFailedObject(key, e);
                value.setError(true);
            }
        });
        final String source = getNotificationSource(context);
        if(result.hasError())
        {
            getNotificationService().notifyUser(source, NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE, Level.FAILURE,
                            result.getFailedObjects().stream().collect(Collectors.toMap(e -> e, e -> result.getErrorForObject(e))));
        }
        if(result.countSuccessfulObjects() > 0)
        {
            getNotificationService().notifyUser(source, NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE, Level.SUCCESS,
                            result.getSuccessfulObjects());
        }
        refreshRows(context, entries.keySet());
        return result;
    }


    private void saveRowByState(final Object entry, final RowState rowState) throws ObjectSavingException
    {
        try
        {
            final Context ctx = new DefaultContext();
            ctx.addAttribute(CTX_PARAM_SUPPRESS_EVENT, true);
            getObjectFacade().save(entry, ctx);
            rowState.resetModifiedFields();
        }
        catch(final ObjectSavePermissionException e)
        {
            LOG.warn(e.getLocalizedMessage(), e);
            throw e;
        }
        catch(final ObjectSavingException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            throw e;
        }
    }


    protected void refreshRows(final EditorContext<Collection<T>> editorContext, final Collection<T> rowItemsToRefresh)
    {
        if(CollectionUtils.isNotEmpty(rowItemsToRefresh))
        {
            final WidgetInstanceManager wim = editorContext.getParameterAs("wim");
            final String inlineProperty = editorContext
                            .getParameterAs(DefaultExtendedMultiReferenceEditor.EDITOR_CTX_INLINE_PROPERTY);
            if(wim != null && StringUtils.isNotBlank(inlineProperty))
            {
                sendEvent(wim.getWidgetslot(), new InlineEditorRefreshEvent(inlineProperty, (Collection<Object>)rowItemsToRefresh));
            }
        }
    }


    void sendEvent(final Widgetslot widgetslot, final InlineEditorRefreshEvent event)
    {
        Events.sendEvent(widgetslot, event);
    }


    protected String getNotificationSource(final EditorContext context)
    {
        final WidgetInstanceManager wim = (WidgetInstanceManager)context.getParameter(Editor.WIDGET_INSTANCE_MANAGER);
        final StringBuilder sb = new StringBuilder();
        if(wim != null)
        {
            sb.append(getNotificationService().getWidgetNotificationSource(wim));
        }
        if(sb.length() > 0)
        {
            sb.append("-");
        }
        sb.append(context.getCode());
        return sb.length() > 0 ? sb.toString() : NotificationEvent.EVENT_SOURCE_UNKNOWN;
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
