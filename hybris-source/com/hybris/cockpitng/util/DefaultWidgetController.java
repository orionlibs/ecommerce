/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.WidgetController;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.dnd.DragAndDropStrategy;
import com.hybris.cockpitng.dnd.WidgetDragAndDropAware;
import com.hybris.cockpitng.dnd.WidgetDragAndDropStrategyRepository;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.WidgetInstanceManagerAware;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class DefaultWidgetController extends ViewAnnotationAwareComposer
                implements WidgetController, WidgetInstanceManagerAware, WidgetDragAndDropAware
{
    private static final long serialVersionUID = 4752872462518708511L;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetController.class);
    private static final String WIDGET_DRAG_AND_DROP_STRATEGY_REPOSITORY = "widgetDragAndDropStrategyRepository";
    private transient WidgetInstanceManager widgetInstanceManager;
    private transient WidgetUtils widgetUtils;


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return this.widgetInstanceManager;
    }


    @Override
    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    /**
     * Override this method to initialize widget settings. Do not refer to view components since they may not have been
     * wired yet.
     */
    public void preInitialize(final Component comp)
    {
        // DO NOTHING BY DEFAULT
    }


    /**
     * Override this method to do initialization stuff for your widget, like initialize widget settings, restore view
     * state from viewmodel, etc. This method will be invoked by {@link #doAfterCompose(Component)} everytime your widget
     * view is created. See {@link GenericForwardComposer#doAfterCompose(Component)} for more details.
     */
    public void initialize(final Component comp)
    {
        // DO NOTHING BY DEFAULT
    }


    @Override
    public void doBeforeComposeChildren(final Component comp) throws Exception
    {
        super.doBeforeComposeChildren(comp);
        preInitialize(comp);
        WidgetControllers.initSettings(comp, getWidgetInstanceManager());
    }


    @Override
    public ComponentInfo doBeforeCompose(final Page page, final Component parent, final ComponentInfo compInfo)
    {
        final Object attribute = parent.getAttribute("moduleAppCtx");
        if(attribute instanceof ApplicationContext)
        {
            WidgetControllers.wireModuleApplicationContext((ApplicationContext)attribute, this, page);
        }
        return super.doBeforeCompose(page, parent, compInfo);
    }


    /**
     * This method is final, use {@link #initialize(Component)} instead.
     */
    @Override
    public final void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        WidgetControllers.setupWidgetEventListeners(comp, this, getWidgetInstanceManager(), widgetUtils);
        initialize(comp);
    }


    /**
     * Returns root path prefix for the given widget. All resource paths within the widget must be preceded by this.
     */
    public String getWidgetRoot()
    {
        final Widgetslot slot = getWidgetslot();
        if(slot != null)
        {
            final WidgetDefinition definition = slot.getWidgetDefinition(slot.getWidgetInstance().getWidget());
            if(definition != null)
            {
                return definition.getLocationPath();
            }
        }
        return "";
    }


    public Widgetslot getWidgetslot()
    {
        return getWidgetInstanceManager().getWidgetslot();
    }


    @Override
    public WidgetModel getModel()
    {
        return getWidgetInstanceManager().getModel();
    }


    public void setValue(final String key, final Object value)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Setting property '{}' to '{}'", key, value);
        }
        getWidgetInstanceManager().getModel().setValue(key, value);
    }


    public <T> T getValue(final String key, final Class<T> expectedClass)
    {
        return getWidgetInstanceManager().getModel().getValue(key, expectedClass);
    }


    private static <A> A getAttribute(final Component component, final String attribute)
    {
        return component != null ? (A)component.getAttribute(attribute) : null;
    }


    @Override
    protected final void invokeListenerMethod(final Method method, final Event event, final ViewEvent viewAnnotation)
                    throws IllegalAccessException, InvocationTargetException
    {
        final Object beforeEvtLogger = getAttribute(getWidgetslot(), "_widgetEventLoggerBefore");
        if(beforeEvtLogger instanceof EventListener)
        {
            try
            {
                ((EventListener)beforeEvtLogger).onEvent(new Event("onLogEvent", getWidgetslot(), viewAnnotation));
            }
            catch(final Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        super.invokeListenerMethod(method, event, viewAnnotation);
        final Object afterEvtLogger = getAttribute(getWidgetslot(), "_widgetEventLoggerAfter");
        if(afterEvtLogger instanceof EventListener)
        {
            try
            {
                ((EventListener)afterEvtLogger).onEvent(new Event("onLogEvent", getWidgetslot(), viewAnnotation));
            }
            catch(final Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
    }


    /**
     * Initializes a widget setting and sets an initial value if there is none, i.e. this method does nothing if there is
     * already a value set for the given key. A common place to invoke this method is within
     * {@link #initialize(Component)}.
     */
    protected void initWidgetSetting(final String key, final Object initialValue)
    {
        getWidgetInstanceManager().initWidgetSetting(key, initialValue);
    }


    /**
     * Same as initWidgetSetting(key, "").
     */
    protected void initWidgetSetting(final String key)
    {
        widgetInstanceManager.initWidgetSetting(key, "");
    }


    /**
     * Same as initWidgetSetting(key, Integer.valueOf(initialValue)).
     */
    protected void initWidgetSetting(final String key, final int initialValue)
    {
        getWidgetInstanceManager().initWidgetSetting(key, Integer.valueOf(initialValue));
    }


    /**
     * Same as initWidgetSetting(key, Boolean.valueOf(initialValue)).
     */
    protected void initWidgetSetting(final String key, final boolean initialValue)
    {
        getWidgetInstanceManager().initWidgetSetting(key, Boolean.valueOf(initialValue));
    }


    /**
     * Same as initWidgetSetting(key, Double.valueOf(initialValue)).
     */
    protected void initWidgetSetting(final String key, final double initialValue)
    {
        getWidgetInstanceManager().initWidgetSetting(key, Double.valueOf(initialValue));
    }


    /**
     * Sends an output event to the output socket with the given id.
     *
     * @param id
     *           The output socket id.
     * @param data
     *           The data to send or null.
     */
    public final void sendOutput(final String id, final Object data)
    {
        getWidgetInstanceManager().sendOutput(id, data);
    }


    /**
     * See {@link WidgetInstanceManager#getLabel(String)}.
     */
    public String getLabel(final String key)
    {
        return getWidgetInstanceManager().getLabel(key);
    }


    /**
     * See {@link WidgetInstanceManager#getLabel(String, Object[])}.
     */
    public String getLabel(final String key, final Object[] args)
    {
        return getWidgetInstanceManager().getLabel(key, args);
    }


    /**
     * Same as sendOutputAfterOperation(id, operation, null, null).
     */
    protected final void sendOutputAfterOperation(final String id, final Operation operation)
    {
        sendOutputAfterOperation(id, operation, null, null);
    }


    /**
     * Same as sendOutputAfterOperation(id, operation, null, busyMessage).
     */
    protected final void sendOutputAfterOperation(final String id, final Operation operation, final String busyMessage)
    {
        sendOutputAfterOperation(id, operation, null, busyMessage);
    }


    /**
     * Executes an {@link Operation} in a separate thread and sends an output event to the output socket with the given
     * id after the operation has been finished.
     *
     * @param id
     *           The output socket id.
     * @param operation
     *           The operation to execute.
     * @param callbackEvent
     *           An optional callback event listener that is invoked after the output event has been sent.
     * @param busyMessage
     *           A busy message that should be displayed while the operation is executed.
     */
    protected final void sendOutputAfterOperation(final String id, final Operation operation,
                    final EventListener<Event> callbackEvent, final String busyMessage)
    {
        getWidgetInstanceManager().sendOutputAfterOperation(id, operation, callbackEvent, busyMessage);
    }


    /**
     * Executes an {@link Operation} in a separate thread.l
     *
     * @param operation
     *           The operation to execute.
     * @param callbackEvent
     *           An optional callback event listener that is invoked after the operation has been finished.
     * @param busyMessage
     *           A busy message that should be displayed while the operation is executed.
     */
    protected final void executeOperation(final Operation operation, final EventListener<Event> callbackEvent,
                    final String busyMessage)
    {
        getWidgetInstanceManager().executeOperation(operation, callbackEvent, busyMessage);
    }


    /**
     * Override this method to handle all input socket events.
     *
     * @param event
     *           Usually an instance of {@link ForwardEvent}. Use {@link ForwardEvent#getOrigin()} to get the original
     *           event.
     */
    public void onSocketInput(final Event event)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Received event '" + event.getName() + "' on widget " + getWidgetslot().getWidgetInstance());
        }
    }


    @Override
    public TypedSettingsMap getWidgetSettings()
    {
        return getWidgetInstanceManager().getWidgetSettings();
    }


    public WidgetUtils getWidgetUtils()
    {
        return widgetUtils;
    }


    /**
     * Gets the title of the current widget instance
     */
    public String getWidgetTitle()
    {
        return getWidgetInstanceManager().getTitle();
    }


    /**
     * Sets the title of the current widget instance
     */
    public void setWidgetTitle(final String title)
    {
        getWidgetInstanceManager().setTitle(title);
    }


    /**
     * @deprecated since 6.6 we don't put beans to Widget Model
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected void initDragAndDropAwareness()
    {
        // do nothing
    }


    @Override
    public DragAndDropStrategy getDragAndDropStrategy()
    {
        final WidgetDragAndDropStrategyRepository repository = getWidgetDragAndDropStrategyRepository();
        if(repository != null)
        {
            return repository.getDragAndDropStrategy(getWidgetInstanceManager(), getWidgetslot().getWidgetInstance().getWidget());
        }
        return null;
    }


    protected WidgetDragAndDropStrategyRepository getWidgetDragAndDropStrategyRepository()
    {
        return (WidgetDragAndDropStrategyRepository)SpringUtil.getBean(WIDGET_DRAG_AND_DROP_STRATEGY_REPOSITORY, WidgetDragAndDropStrategyRepository.class);
    }
}
