/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.WidgetConfigurationContextDecorator;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.events.callback.CallbackOperation;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.text.MessageFormat;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * Provides widget framework related functionality. Allows you to send data to output sockets, access the widget
 * settings and the widget model to store view related data in the session scope. To use it you need to implement the
 * {@link WidgetInstanceManagerAware} interface in your widget controller
 * (ideally you extend the {@link DefaultWidgetController}) or in your MVVM view model.
 */
public interface WidgetInstanceManager
{
    String CNG_TITLE_CHANGE_LISTENER = "_cng_title_change_listener";


    /**
     * Sends data to the output socked with the given ID.
     *
     * @param socketId output socket ID to sent the data to
     * @param data the data to be sent
     */
    void sendOutput(String socketId, Object data);


    /**
     * Returns the widget settings map.
     *
     * @return widget settings map
     */
    TypedSettingsMap getWidgetSettings();


    /**
     * Returns the widget view model as specified in the definition.xml of the widget.
     * The widget view model has a scope of a session, use it to store the view state data.
     *
     * @return the widget view model
     */
    WidgetModel getModel();


    /**
     * Returns the widget slot the widget related to this manager is placed in.
     *
     * @return the widget slot the widget is placed in
     */
    Widgetslot getWidgetslot();


    /**
     * Initializes a widget setting and sets an initial value if there is none, i.e. this method does nothing if there is
     * already a value set for the given key.
     *
     * @param key
     * @param initialValue
     */
    void initWidgetSetting(String key, Object initialValue);


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
    void sendOutputAfterOperation(String id, Operation operation, EventListener<Event> callbackEvent, String busyMessage);


    /**
     * Executes an {@link Operation} in a separate thread.
     *
     * @param operation
     *           The operation to execute.
     * @param callbackEvent
     *           An optional callback event listener that is invoked after the operation has been finished.
     * @param busyMessage
     *           A busy message that should be displayed while the operation is executed.
     */
    void executeOperation(Operation operation, EventListener<Event> callbackEvent, String busyMessage);


    /**
     * Executes an {@link Operation} in a separate thread regardless is there is already running another operation for
     * the
     * widget.
     *
     * @param operation
     *           The operation to execute.
     * @param callbackEvent
     *           An optional callback event listener that is invoked after the operation has been finished.
     */
    default void executeOperationInParallel(final Operation operation, final EventListener<Event> callbackEvent)
    {
    }


    /**
     * Returns a localized label in the current locale.
     *
     * @param key localization key as specified in the locales_*.properties in your widget definition.
     * @return localized label in the current locale
     */
    String getLabel(String key);


    /**
     * Returns a localized label in the current locale and formats it with the given arguments. The formatting is done by
     * the use of {@link MessageFormat}.
     *
     * @param key localization key as specified in the locales_*.properties in your widget definition.
     * @param args list of arguments for the formating
     * @return localized label in the current locale formatted with the given arguments
     */
    String getLabel(String key, Object[] args);


    /**
     * Returns the current widget title.
     *
     * @return current widget title
     */
    String getTitle();


    /**
     * Changes the title of the widget dynamically.
     *
     * @param title the new title
     */
    void setTitle(String title);


    /**
     * Builds cockpit configuration context for this widget. It takes context parameters from the widget settings and
     * possibly from other sources eg. user role from the session information (see the spring bean
     * <code>widgetConfigurationContextDecoratorList</code> for list of
     * installed {@link WidgetConfigurationContextDecorator}s).
     *
     * @param additionalContext additional configuration context for the widget, this can contain dynamic context
     *           parameters as "type" or similar
     * @param configurationType type of the configuration this context is to be built for. This might influence the logic
     *           in {@link WidgetConfigurationContextDecorator}s.
     * @return a configuration context containing all widget related parameters as well as those included in the given
     *         additionalContext
     */
    <CONFIG> ConfigContext buildConfigurationContext(ConfigContext additionalContext, Class<CONFIG> configurationType);


    /**
     * Delegate to cockpitConfigurationService.
     * Always use this method to load the configuration as it will automatically add all necessary context parameters
     * from the environment - like configuration parameters from widget settings, current user/role from the session etc.
     *
     * Loads a single piece of cockpit configuration according to context provided.
     *
     * @param context
     *           the context to be used to look for the configuration
     * @param configurationType
     *           desired configuration type
     * @return single piece of cockpit configuration according to context provided
     * @throws CockpitConfigurationException
     *            if configuration could not be found or some error occured during loading
     * @see CockpitConfigurationService#loadConfiguration(ConfigContext, Class)
     */
    <CONFIG> CONFIG loadConfiguration(ConfigContext context, Class<CONFIG> configurationType) throws CockpitConfigurationException;


    /**
     * Delegate to cockpitConfigurationService.
     * Always use this method to load the configuration as it will automatically add all necessary context parameters
     * from the environment - like configuration parameters from widget settings, current user/role from the session etc.
     *
     * Stores a single piece of cockpit configuration according to context provided.
     *
     * @param context
     *           the context to be used to store the configuration
     * @param configuration
     *           the configuration to be stored
     * @throws CockpitConfigurationException
     *            if configuration could not be stored for some reason
     *
     * @see CockpitConfigurationService#storeConfiguration(ConfigContext, Object)
     */
    <CONFIG> void storeConfiguration(ConfigContext context, CONFIG configuration) throws CockpitConfigurationException;


    /**
     * Whenever an editor is created in the context of a widget it may be needed to inform the parent widget that the
     * editor wants to interact with its model. In this case this method may come handy to wrap the original listener
     * with a proxy that does some additional - widget related - work.
     *
     * @param context editor context
     * @param listener original editor listener @return wrapped listener (by default it returns the original listener,
     */
    default EditorListener registerEditorListener(final EditorContext context, final EditorListener listener)
    {
        return listener;
    }


    /**
     * Method complementary to com.hybris.cockpitng.engine.WidgetInstanceManager#registerEditorListener. Appropriate
     * implementation should notify all the registered callbacks on listener notifications.
     *
     * Default implementation does nothing.
     *
     * @param callback the callback to be invoked
     */
    default void registerBeforeEditorCallback(final CallbackOperation callback)
    {
        // Do nothing
    }


    /**
     * Method complementary to com.hybris.cockpitng.engine.WidgetInstanceManager#registerEditorListener. Appropriate
     * implementation should notify all the registered callbacks on listener notifications.
     *
     * Default implementation does nothing.
     *
     * @param callback the callback to be invoked
     */
    default void registerAfterEditorCallback(final CallbackOperation callback)
    {
        // Do nothing
    }
}
