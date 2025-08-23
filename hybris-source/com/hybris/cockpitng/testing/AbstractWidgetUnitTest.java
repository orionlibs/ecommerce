/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.annotations.ViewEvents;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.impl.DefaultWidgetModel;
import com.hybris.cockpitng.core.util.impl.ReflectionUtils;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.annotation.DeclaredBindingParam;
import com.hybris.cockpitng.testing.annotation.DeclaredCommand;
import com.hybris.cockpitng.testing.annotation.DeclaredCommands;
import com.hybris.cockpitng.testing.annotation.DeclaredGlobalCockpitEvent;
import com.hybris.cockpitng.testing.annotation.DeclaredGlobalCockpitEvents;
import com.hybris.cockpitng.testing.annotation.DeclaredInput;
import com.hybris.cockpitng.testing.annotation.DeclaredInputs;
import com.hybris.cockpitng.testing.annotation.DeclaredViewEvent;
import com.hybris.cockpitng.testing.annotation.DeclaredViewEvents;
import com.hybris.cockpitng.testing.exception.WidgetTestException;
import com.hybris.cockpitng.testing.internal.FeatureToWire;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import com.hybris.cockpitng.testing.util.internal.TestUtilInternal;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javassist.Modifier;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractWidgetUnitTest<T> extends AbstractCockpitngUnitTest<T>
{
    private static final Map<String, Method> DECLARED_SOCKET_INPUTS = new HashMap<>();
    private static final Map<String, Method> DECLARED_COMMANDS = new HashMap<>();
    private static final Map<String, Method> DECLARED_VIEW_EVENTS = new HashMap<>();
    private static final Map<String, Method> DECLARED_GLOBAL_COCKPIT_EVENTS = new HashMap<>();
    private static final Collection<Method> DECLARED_OTHER_CALLABLE_METHODS = new ArrayList<>();
    private static boolean classInitialized;
    @Spy
    protected TypedSettingsMap widgetSettings;
    protected WidgetInstanceManager widgetInstanceManager;
    protected DefaultWidgetModel widgetModel;


    @BeforeClass
    public static void setUpAbstractWidgetUnitTestClass()
    {
        DECLARED_SOCKET_INPUTS.clear();
        DECLARED_COMMANDS.clear();
        DECLARED_VIEW_EVENTS.clear();
        DECLARED_GLOBAL_COCKPIT_EVENTS.clear();
        DECLARED_OTHER_CALLABLE_METHODS.clear();
        classInitialized = false;
    }


    private static void setClassToInitialized()
    {
        classInitialized = true;
    }


    public static Map<String, Method> getDeclaredSocketInputs()
    {
        return Collections.unmodifiableMap(DECLARED_SOCKET_INPUTS);
    }


    @Before
    public void setUpAbstractWidgetUnitTest()
    {
        MockitoAnnotations.initMocks(this);
        final T controller = getWidgetController();
        if(controller.getClass().getAnnotation(InjectMocks.class) != null)
        {
            MockitoAnnotations.initMocks(controller);
        }
        widgetInstanceManager = CockpitTestUtil.mockWidgetInstanceManager(widgetSettings);
        widgetModel = (DefaultWidgetModel)widgetInstanceManager.getModel();
        Mockito.lenient().doAnswer(invocation -> null).when(widgetInstanceManager).sendOutput(anyString(), anyObject());
        try
        {
            final T widget = getWidgetController();
            if(!classInitialized)
            {
                scanDeclaredEventsAndCommands();
                setClassToInitialized();
            }
            injectAutowiredFeatures(widget);
        }
        catch(final IllegalAccessException ex)
        {
            throw new WidgetTestException("Unexpected exception occured", ex);
        }
        mockZKEnvironment();
    }


    /**
     * Mocks the ZK environment. If you need to disable the mocking override the method with empty implementation.
     *
     * @see com.hybris.cockpitng.testing.util.CockpitTestUtil#mockZkEnvironment
     */
    protected void mockZKEnvironment()
    {
        CockpitTestUtil.mockZkEnvironment();
    }


    private void scanDeclaredEventsAndCommands()
    {
        final Class<?> superclass = getWidgetType().getSuperclass();
        for(final Method method : TestUtilInternal.getAllMethods(getWidgetType(),
                        superclass != null && Modifier.isAbstract(superclass.getModifiers())))
        {
            if(Modifier.isAbstract(method.getModifiers()))
            {
                continue;
            }
            if(Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()))
            {
                for(final Annotation annotation : method.getAnnotations())
                {
                    final Class<? extends Annotation> annotationType = annotation.getClass();
                    if(SocketEvent.class.isAssignableFrom(annotationType))
                    {
                        final SocketEvent event = (SocketEvent)annotation;
                        final String socketId = event.socketId();
                        DECLARED_SOCKET_INPUTS.put(StringUtils.isNotBlank(socketId) ? socketId : method.getName(), method);
                    }
                    else if(Command.class.isAssignableFrom(annotationType))
                    {
                        final Command command = (Command)annotation;
                        final String[] commandIds = command.value();
                        if(commandIds.length > 0)
                        {
                            for(final String commandId : commandIds)
                            {
                                DECLARED_COMMANDS.put(commandId, method);
                            }
                        }
                        else
                        {
                            DECLARED_COMMANDS.put(method.getName(), method);
                        }
                    }
                    else if(ViewEvents.class.isAssignableFrom(annotationType))
                    {
                        final ViewEvents events = (ViewEvents)annotation;
                        for(final ViewEvent event : events.value())
                        {
                            final String componentID = event.componentID();
                            final String eventName = event.eventName();
                            DECLARED_VIEW_EVENTS.put(createStringKey(componentID, eventName), method);
                        }
                    }
                    else if(ViewEvent.class.isAssignableFrom(annotationType))
                    {
                        final ViewEvent event = (ViewEvent)annotation;
                        final String componentID = event.componentID();
                        final String eventName = event.eventName();
                        DECLARED_VIEW_EVENTS.put(createStringKey(componentID, eventName), method);
                    }
                    else if(GlobalCockpitEvent.class.isAssignableFrom(annotationType))
                    {
                        final GlobalCockpitEvent event = (GlobalCockpitEvent)annotation;
                        DECLARED_GLOBAL_COCKPIT_EVENTS.put(createStringKey(event.eventName(), event.scope()), method);
                    }
                    else
                    {
                        if(getWidgetType().equals(method.getDeclaringClass()))
                        {
                            DECLARED_OTHER_CALLABLE_METHODS.add(method);
                        }
                    }
                }
            }
            else if((Modifier.isProtected(method.getModifiers()) || Modifier.isPublic(method.getModifiers()))
                            && (getWidgetType().equals(method.getDeclaringClass())))
            {
                DECLARED_OTHER_CALLABLE_METHODS.add(method);
            }
        }
    }


    private static String createStringKey(final String... names)
    {
        final StringBuilder builder = new StringBuilder();
        for(final String name : names)
        {
            builder.append('[').append(name).append(']');
        }
        return builder.toString();
    }


    private Collection<FeatureToWire> scanAutowiredFeatures() throws IllegalAccessException
    {
        final Collection<FeatureToWire> featuresToWire = new ArrayList<>();
        for(final Field field : ReflectionUtils.getAllDeclaredFields(getClass()))
        {
            final WireVariable wiredVariableInject = field.getAnnotation(WireVariable.class);
            if(wiredVariableInject != null || HtmlBasedComponent.class.isAssignableFrom(field.getType()))
            {
                final String qualifier = field.getName();
                final boolean acc = field.isAccessible();
                field.setAccessible(true);
                if(StringUtils.isNotBlank(qualifier))
                {
                    featuresToWire.add(new FeatureToWire(field.getType(), qualifier, field.get(this)));
                }
                else
                {
                    featuresToWire.add(new FeatureToWire(field.getType(), field.get(this)));
                }
                field.setAccessible(acc);
            }
        }
        return featuresToWire;
    }


    private void injectAutowiredFeatures(final Object widget) throws IllegalAccessException
    {
        final Collection<FeatureToWire> featuresToWire = scanAutowiredFeatures();
        for(final Field field : ReflectionUtils.getAllDeclaredFields(getWidgetType()))
        {
            final WireVariable annotation = field.getAnnotation(WireVariable.class);
            if(annotation != null || WidgetInstanceManager.class.isAssignableFrom(field.getType())
                            || HtmlBasedComponent.class.isAssignableFrom(field.getType()))
            {
                if(WidgetInstanceManager.class.isAssignableFrom(field.getType()))
                {
                    final boolean acc = field.isAccessible();
                    field.setAccessible(true);
                    field.set(widget, widgetInstanceManager);
                    field.setAccessible(acc);
                    return;
                }
                else
                {
                    wireVariable(widget, field, featuresToWire);
                }
            }
        }
    }


    private static void wireVariable(final Object widget, final Field field, final Collection<FeatureToWire> featuresToWire)
                    throws IllegalAccessException
    {
        FeatureToWire feature = findFeatureToWireByAnnotation(field, featuresToWire);
        if(feature == null)
        {
            feature = findFeatureToWireByFieldType(field, featuresToWire, feature);
        }
        if(feature != null)
        {
            final boolean acc = field.isAccessible();
            field.setAccessible(true);
            field.set(widget, feature.getToWire());
            field.setAccessible(acc);
        }
    }


    private static FeatureToWire findFeatureToWireByFieldType(Field field, Collection<FeatureToWire> featuresToWire,
                    FeatureToWire feature)
    {
        for(final FeatureToWire toWire : featuresToWire)
        {
            if(StringUtils.isBlank(toWire.getQualifier()) && field.getType().isAssignableFrom(toWire.getType()))
            {
                if(feature != null)
                {
                    throw new WidgetTestException(field.getName() + ": more that one wired variable matches the field by " + "type.");
                }
                feature = toWire;
            }
        }
        return feature;
    }


    private static FeatureToWire findFeatureToWireByAnnotation(Field field, Collection<FeatureToWire> featuresToWire)
    {
        FeatureToWire feature = null;
        for(final FeatureToWire toWire : featuresToWire)
        {
            final WireVariable annotation = field.getAnnotation(WireVariable.class);
            final String qualifierName = annotation == null ? null : annotation.value();
            final String toCompare = StringUtils.isBlank(qualifierName) ? field.getName() : qualifierName;
            if(StringUtils.equals(toWire.getQualifier(), toCompare))
            {
                if(feature != null)
                {
                    throw new WidgetTestException(field.getName() + ": more that one wired variable matches the field by type.");
                }
                feature = toWire;
            }
        }
        return feature;
    }


    /**
     * Sends a socket event to the specified socket.
     *
     * @param socketId
     *           that should receive a socket event
     * @param value
     *           the content of the event that has been emitted towards the specified socket
     */
    protected void executeInputSocketEvent(final String socketId, final Object... value)
    {
        final Method method = DECLARED_SOCKET_INPUTS.get(socketId);
        final boolean socketIgnoresInputObject = method.getParameterCount() == 0;
        if(socketIgnoresInputObject)
        {
            executeMethod(method);
        }
        else
        {
            executeMethod(method, value);
        }
    }


    protected void executeCommand(final String commandId, final Object... value)
    {
        executeMethod(DECLARED_COMMANDS.get(commandId), value);
    }


    protected void executeViewEvent(final String componentID, final String eventName, final Object... value)
    {
        executeMethod(DECLARED_VIEW_EVENTS.get(createStringKey(componentID, eventName)), value);
    }


    protected void executeGlobalEvent(final String eventName, final Object... value)
    {
        executeGlobalEvent(eventName, "", value);
    }


    protected void executeGlobalEvent(final String eventName, final String scope, final Object... value)
    {
        executeMethod(DECLARED_GLOBAL_COCKPIT_EVENTS.get(createStringKey(eventName, scope)), value);
    }


    private void executeMethod(final Method theMethod, final Object... value)
    {
        try
        {
            theMethod.invoke(getWidgetController(), value);
        }
        catch(final IllegalArgumentException | InvocationTargetException | IllegalAccessException e)
        {
            throw new WidgetTestException(theMethod.toGenericString(), e);
        }
    }


    @Test
    public final void testHasAllObligatoryViewEventsDeclared()
    {
        final Set<String> requiredViewEvents = scanRequiredViewEvents();
        assertEquals("All view events declared on the widget must be explicitly declared in the test",
                        DECLARED_VIEW_EVENTS.keySet(), requiredViewEvents);
    }


    private Set<String> scanRequiredViewEvents()
    {
        final Set<String> requiredViewEvents = new HashSet<>();
        final DeclaredViewEvents events = getClass().getAnnotation(DeclaredViewEvents.class);
        if(events == null)
        {
            final DeclaredViewEvent event = getClass().getAnnotation(DeclaredViewEvent.class);
            if(event != null)
            {
                requiredViewEvents.add(createStringKey(event.componentID(), event.eventName()));
            }
        }
        else
        {
            for(final DeclaredViewEvent event : events.value())
            {
                requiredViewEvents.add(createStringKey(event.componentID(), event.eventName()));
            }
        }
        return requiredViewEvents;
    }


    @Test
    public final void testHasAllObligatoryGlobalCockpitEventsDeclared()
    {
        final Set<String> requiredGlobalCockpitEvents = scanRequiredGlobalCockpitEvents();
        assertEquals("All global events declared in the widget must be explicitly declared in the test",
                        DECLARED_GLOBAL_COCKPIT_EVENTS.keySet(), requiredGlobalCockpitEvents);
    }


    private Set<String> scanRequiredGlobalCockpitEvents()
    {
        final Set<String> requiredGlobalCockpitEvents = new HashSet<>();
        final DeclaredGlobalCockpitEvents events = getClass().getAnnotation(DeclaredGlobalCockpitEvents.class);
        if(events == null)
        {
            final DeclaredGlobalCockpitEvent event = getClass().getAnnotation(DeclaredGlobalCockpitEvent.class);
            if(event != null)
            {
                requiredGlobalCockpitEvents.add(createStringKey(event.eventName(), event.scope()));
            }
        }
        else
        {
            for(final DeclaredGlobalCockpitEvent event : events.value())
            {
                requiredGlobalCockpitEvents.add(createStringKey(event.eventName(), event.scope()));
            }
        }
        return requiredGlobalCockpitEvents;
    }


    @Test
    public final void testHasAllObligatorySocketsDeclared()
    {
        final Map<String, Class<?>> requiredSocketInputs = scanRequiredSockets();
        assertEquals("All input sockets declared in the widget must be explicitly declared in the test class",
                        DECLARED_SOCKET_INPUTS.keySet(), requiredSocketInputs.keySet());
        for(final Entry<String, Class<?>> entry : requiredSocketInputs.entrySet())
        {
            final String key = entry.getKey();
            final Class<?> value = requiredSocketInputs.get(key);
            final Class<?>[] parameterTypes = DECLARED_SOCKET_INPUTS.get(key).getParameterTypes();
            if(parameterTypes.length == 0)
            {
                assertSame("Argument final type does not match socket declaration for: " + key, Object.class, value);
            }
            else if(parameterTypes.length == 1)
            {
                assertThat(Event.class.isAssignableFrom(parameterTypes[0]) || parameterTypes[0].isAssignableFrom(value))
                                .overridingErrorMessage("Argument final type does not match socket declaration for: %s", key).isTrue();
            }
            else
            {
                fail("Too long parameter list");
            }
        }
    }


    protected Map<String, Class<?>> scanRequiredSockets()
    {
        final Map<String, Class<?>> requiredSocketInputs = new HashMap<>();
        final DeclaredInputs sockets = getClass().getAnnotation(DeclaredInputs.class);
        if(sockets == null)
        {
            final DeclaredInput socket = getClass().getAnnotation(DeclaredInput.class);
            if(socket != null)
            {
                requiredSocketInputs.put(socket.value(), socket.socketType());
            }
        }
        else
        {
            for(final DeclaredInput socket : sockets.value())
            {
                requiredSocketInputs.put(socket.value(), socket.socketType());
            }
        }
        return requiredSocketInputs;
    }


    @Test
    public final void testHasAllObligatoryCommandsDeclared()
    {
        final Map<String, DeclaredBindingParam[]> requiredCommands = scanRequiredCommands();
        assertEquals("All commands declared on the widget must be explicitly declared in the test", DECLARED_COMMANDS.keySet(),
                        requiredCommands.keySet());
        for(final Entry<String, DeclaredBindingParam[]> entry : requiredCommands.entrySet())
        {
            final Method command = DECLARED_COMMANDS.get(entry.getKey());
            final DeclaredBindingParam[] reqParams = requiredCommands.get(entry.getKey());
            final Annotation[][] parameterAnnotations = command.getParameterAnnotations();
            final Class<?>[] parameterTypes = command.getParameterTypes();
            if(parameterTypes.length != reqParams.length)
            {
                fail("Required parameters list does not match actual parameter list of the command: " + command.toGenericString());
            }
            if(parameterAnnotations.length != reqParams.length)
            {
                fail("Parameters of the command are not properly annotatated with " + BindingParam.class + ": "
                                + command.toGenericString());
            }
            for(int i = 0; i < reqParams.length; i++)
            {
                assertSame("Wrong argument type", reqParams[i].type(), parameterTypes[i]);
                String qualifier = null;
                for(final Annotation annot : parameterAnnotations[i])
                {
                    if(BindingParam.class.isAssignableFrom(annot.annotationType()))
                    {
                        qualifier = ((BindingParam)annot).value();
                        break;
                    }
                }
                assertEquals("Wrong binding qualifier", reqParams[i].qualifier(), qualifier);
            }
        }
    }


    private Map<String, DeclaredBindingParam[]> scanRequiredCommands()
    {
        final Map<String, DeclaredBindingParam[]> requiredCommands = new HashMap<>();
        final DeclaredCommands commands = getClass().getAnnotation(DeclaredCommands.class);
        if(commands == null)
        {
            final DeclaredCommand command = getClass().getAnnotation(DeclaredCommand.class);
            if(command != null)
            {
                requiredCommands.put(command.value(), command.params());
            }
        }
        else
        {
            for(final DeclaredCommand command : commands.value())
            {
                requiredCommands.put(command.value(), command.params());
            }
        }
        return requiredCommands;
    }


    @Test
    public void testDefaultOrNoParameterConstructor()
    {
        assertThat(TestUtilInternal.hasDefaultOrNoParameterConstructor(getWidgetType()))
                        .overridingErrorMessage("Default or no parameter constructor is missing").isTrue();
    }


    @Test
    public void testNulLSafeCommands()
    {
        testNullSafeMethods(getWidgetController(), DECLARED_COMMANDS.values());
    }


    @Test
    public void testNullSafeSocketInputs()
    {
        testNullSafeMethods(getWidgetController(), DECLARED_SOCKET_INPUTS.values());
    }


    @Test
    public void testFrameworkAnnotatedMethodsVisibility()
    {
        final StringBuilder errorMessage = new StringBuilder();
        TestUtilInternal.getAllMethods(getWidgetType(), true).stream()
                        .filter(method -> !Modifier.isPublic(method.getModifiers()) || Modifier.isStatic(method.getModifiers()))
                        .forEach(method -> Stream.of(method.getAnnotations()).forEach(annotation -> {
                            final Class<? extends Annotation> annotationType = annotation.getClass();
                            if(SocketEvent.class.isAssignableFrom(annotationType))
                            {
                                prepareFrameworkAnnotatedMethodVisibilityErrorMessage(method, SocketEvent.class, errorMessage);
                            }
                            else if(Command.class.isAssignableFrom(annotationType))
                            {
                                prepareFrameworkAnnotatedMethodVisibilityErrorMessage(method, Command.class, errorMessage);
                            }
                            else if(ViewEvents.class.isAssignableFrom(annotationType))
                            {
                                prepareFrameworkAnnotatedMethodVisibilityErrorMessage(method, ViewEvents.class, errorMessage);
                            }
                            else if(ViewEvent.class.isAssignableFrom(annotationType))
                            {
                                prepareFrameworkAnnotatedMethodVisibilityErrorMessage(method, ViewEvent.class, errorMessage);
                            }
                            else if(GlobalCockpitEvent.class.isAssignableFrom(annotationType))
                            {
                                prepareFrameworkAnnotatedMethodVisibilityErrorMessage(method, GlobalCockpitEvent.class, errorMessage);
                            }
                        }));
        assertThat(errorMessage.toString()).overridingErrorMessage(errorMessage.toString()).isEmpty();
    }


    private static void prepareFrameworkAnnotatedMethodVisibilityErrorMessage(final Method method, final Class clazz,
                    final StringBuilder errorMessage)
    {
        errorMessage.append(System.lineSeparator())
                        .append(String.format(
                                        "Method [%s] is annotated with [@%s] and declared as [%s] while it should be non-static and public.",
                                        method.toGenericString(), clazz.getCanonicalName(), Modifier.toString(method.getModifiers())));
    }


    @Test
    public void testFrameworkMethodsAnnotatedWithGlobalCockpitEventShouldHaveOneParameterOfTypeCockpitEvent()
    {
        TestUtilInternal.getAllMethods(getWidgetType(), true).forEach(method -> Stream.of(method.getAnnotations())
                        .filter(annotation -> GlobalCockpitEvent.class.isAssignableFrom(annotation.getClass())).forEach(annotation -> {
                            assertThatMethodHasOnlyOneParameter(method);
                            assertThatMethodParameterIsTypeOfCockpitEvent(method);
                        }));
    }


    private static void assertThatMethodHasOnlyOneParameter(final Method method)
    {
        final String errorMessageFormat = "Method '%s' should have one parameter of type %s, but has %d parameters";
        final int numberOfParameters = method.getParameterCount();
        final String cockpitEventTypeName = CockpitEvent.class.getName();
        final String errorMessage = String.format(errorMessageFormat, method.getName(), cockpitEventTypeName, numberOfParameters);
        assertThat(numberOfParameters).overridingErrorMessage(errorMessage).isEqualTo(1);
    }


    private static void assertThatMethodParameterIsTypeOfCockpitEvent(final Method method)
    {
        final String errorMessageFormat = "Parameter of method '%s' is type of %s, but should be of %s type";
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final String paramTypeName = parameterTypes[0].getTypeName();
        final String cockpitEventTypeName = CockpitEvent.class.getName();
        final String errorMessage = String.format(errorMessageFormat, method.getName(), paramTypeName, cockpitEventTypeName);
        final boolean paramIsTypeOfCockpitEvent = CockpitEvent.class.isAssignableFrom(parameterTypes[0]);
        assertThat(paramIsTypeOfCockpitEvent).overridingErrorMessage(errorMessage).isTrue();
    }


    protected abstract T getWidgetController();


    protected void assertValueNotNull(final String key)
    {
        verify(widgetModel).setValue(eq(key), notNull());
    }


    protected <K> void assertValueSet(final String key, final K match)
    {
        assertValueSet(key, 1, match);
    }


    protected <K> void assertValueSet(final String key, final int times, final K match)
    {
        verify(widgetModel, times(times)).setValue(eq(key), eq(match));
    }


    protected void assertValueRemove(final String key)
    {
        assertValueRemove(key, 1);
    }


    protected void assertValueRemove(final String key, final int times)
    {
        verify(widgetModel, times(times)).remove(eq(key));
    }


    protected <K> void assertValuePut(final String key, final K match)
    {
        assertValuePut(key, 1, match);
    }


    protected <K> void assertValuePut(final String key, final int times, final K match)
    {
        final ArgumentCaptor<String> putKeyCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Object> putValueCaptor = ArgumentCaptor.forClass(Object.class);
        final ArgumentCaptor<String> setKeyCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Object> setValueCaptor = ArgumentCaptor.forClass(Object.class);
        verify(widgetModel, atLeast(0)).put(putKeyCaptor.capture(), putValueCaptor.capture());
        verify(widgetModel, atLeast(0)).setValue(setKeyCaptor.capture(), setValueCaptor.capture());
        final List<String> putKeys = putKeyCaptor.getAllValues();
        final List<Object> putValues = putValueCaptor.getAllValues();
        final List<String> setKeys = setKeyCaptor.getAllValues();
        final List<Object> setValues = setValueCaptor.getAllValues();
        long puts = 0;
        long sets = 0;
        for(int i = 0; i < putKeys.size(); i++)
        {
            if(Objects.equals(putKeys.get(i), key) && Objects.equals(putValues.get(i), match))
            {
                puts++;
            }
        }
        for(int i = 0; i < setKeys.size(); i++)
        {
            if(Objects.equals(setKeys.get(i), key) && Objects.equals(setValues.get(i), match))
            {
                sets++;
            }
        }
        assertThat(puts + sets).overridingErrorMessage("Value not set in widget model").isEqualTo(times);
    }


    protected <K> void assertAttributeSet(final String key, final K match)
    {
        assertThat(widgetModel.getValue(key, Object.class)).overridingErrorMessage("Widget attribute does not match")
                        .isEqualTo(match);
    }


    protected <K> void assertWidgetSettingSet(final String key, final K match)
    {
        assertThat(widgetSettings.get(key)).overridingErrorMessage("Widget setting does not match").isEqualTo(match);
    }


    protected <K> void assertSocketOutput(final String socketId, final K outputMatcher)
    {
        assertSocketOutput(socketId, 1, outputMatcher);
    }


    protected <K> void assertSocketOutput(final String socketId, final int times, final K outputMatcher)
    {
        verify(widgetInstanceManager, times(times)).sendOutput(eq(socketId), eq(outputMatcher));
    }


    protected <K> void assertSocketOutput(final String socketId, final Predicate<K> predicate)
    {
        assertSocketOutput(socketId, 1, predicate);
    }


    protected <K> void assertSocketOutput(final String socketId, final int times, final Predicate<K> predicate)
    {
        assertSocketOutput(socketId, times, new ArgumentMatcher<K>()
        {
            @Override
            public boolean matches(final Object o)
            {
                return predicate.test((K)o);
            }
        });
    }


    protected <K> void assertSocketOutput(final String socketId, final ArgumentMatcher<K> matcher)
    {
        assertSocketOutput(socketId, 1, matcher);
    }


    protected <K> void assertSocketOutput(final String socketId, final int times, final ArgumentMatcher<K> matcher)
    {
        verify(widgetInstanceManager, times(times)).sendOutput(eq(socketId), argThat(matcher));
    }


    protected void assertNoSocketOutputInteractions(final String socketId)
    {
        verify(widgetInstanceManager, never()).sendOutput(eq(socketId), any());
    }


    @Override
    protected Class<? extends T> getWidgetType()
    {
        return ReflectionUtils.extractGenericParameterType(getClass(), 0);
    }
}
