/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.impl.DefaultWidgetModel;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.au.AuResponseProcessor;
import com.hybris.cockpitng.testing.util.au.AuResponseProcessorRegistry;
import com.hybris.cockpitng.util.impl.FilteringClassLocator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.fest.assertions.Assertions;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zkoss.lang.Library;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.au.AuResponse;
import org.zkoss.zk.au.out.AuEcho;
import org.zkoss.zk.device.AjaxDevice;
import org.zkoss.zk.device.Devices;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.http.ExecutionImpl;
import org.zkoss.zk.ui.impl.DesktopImpl;
import org.zkoss.zk.ui.metainfo.LanguageDefinition;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.ui.sys.DesktopCache;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zk.ui.sys.SessionsCtrl;
import org.zkoss.zk.ui.sys.WebAppCtrl;
import org.zkoss.zk.ui.sys.WebAppsCtrl;
import org.zkoss.zk.ui.util.Configuration;

/**
 * Utility methods for testing.
 */
public final class CockpitTestUtil
{
    private CockpitTestUtil()
    {
        // just static utility methods
    }


    /**
     * Mocks labels that are accessible by {@link Labels}.
     *
     * @param lookup
     *           labels mock factory
     */
    public static void mockLabelLookup(final LabelLookupFactory lookup)
    {
        mockLabelLookup(lookup.getLookup());
    }


    /**
     * Mocks labels that are accessible by {@link Labels}.
     *
     * @param lookup
     *           labels mock
     */
    public static void mockLabelLookup(final LabelLookup lookup)
    {
        Labels.register(lookup);
    }


    /**
     * Allows to mock ZK labels that are fetched by static methods like {@link Labels#getLabel(String)}. It sets the labels
     * for the time of test, then reverts them after finishing.
     *
     * @param labels
     *           map of labels to use
     * @param runnable
     *           code to run that will see the labels
     */
    public static void executeWithLabels(final Map<String, String> labels, final Runnable runnable)
    {
        final LabelLookup labelLookup = new LabelLookup();
        labels.forEach(labelLookup::addLabel);
        try
        {
            Labels.register(labelLookup);
            runnable.run();
        }
        finally
        {
            labelLookup.clearLabels();
            Labels.reset();
        }
    }


    /**
     * Mocks beans that are accessible by {@link org.zkoss.spring.SpringUtil}.
     * <P>
     * Notice that only one bean mock may be done for single {@link #mockZkEnvironment()} call
     *
     * @param lookup
     *           beans mock factory
     */
    public static void mockBeanLookup(final BeanLookupFactory lookup)
    {
        mockBeanLookup(lookup.getLookup());
    }


    /**
     * Mocks beans that are accessible by {@link org.zkoss.spring.SpringUtil}.
     * <P>
     * Notice that only one bean mock may be done for single {@link #mockZkEnvironment()} call
     *
     * @param lookup
     *           beans mock
     */
    public static void mockBeanLookup(final BeanLookup lookup)
    {
        if(ExecutionsCtrl.getCurrent() == null)
        {
            mockZkEnvironment();
        }
        final ServletContext serverContext = (ServletContext)ExecutionsCtrl.getCurrent().getDesktop().getWebApp()
                        .getNativeContext();
        final WebApplicationContext wac = (WebApplicationContext)serverContext
                        .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        reset(wac);
        ignoreStubs(wac);
        Mockito.lenient().when(wac.getBean(anyString(), any(Class.class)))
                        .then(invoke -> lookup.getBean((String)invoke.getArguments()[0], (Class)invoke.getArguments()[1]));
        Mockito.lenient().when(wac.getBean(anyString())).then(invoke -> lookup.getBean((String)invoke.getArguments()[0]));
        Mockito.lenient().when(wac.getBean(anyString(), Matchers.<Object>anyVararg()))
                        .then(invoke -> lookup.getBean((String)invoke.getArguments()[0]));
        Mockito.lenient().when(wac.containsBean(anyString())).then(invoke -> lookup.containsBean((String)invoke.getArguments()[0]));
        Mockito.lenient().when(wac.getBeansOfType(any())).then(invoke -> lookup.getBeansOfType((Class)invoke.getArguments()[0]));
    }


    /**
     * Mocks http request, response and session. Then it is stored in {@link RequestContextHolder}.
     */
    public static void mockHttpRequestAttributes()
    {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        final HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getValue(any())).then(invocation -> session.getAttribute(invocation.getArgument(0, String.class)));
        doAnswer(invocation -> {
            session.setAttribute(invocation.getArgument(0, String.class), invocation.getArgument(1, Serializable.class));
            return null;
        }).when(session).putValue(any(), any());
        doAnswer(invocation -> {
            session.removeValue(invocation.getArgument(0, String.class));
            return null;
        }).when(session).removeValue(any());
        mockAttributes((key, value) -> when(session.getAttribute(eq(key))).thenReturn(value), session::getAttributeNames)
                        .when(session).setAttribute(any(), any(Serializable.class));
        mockAttributes((key, value) -> when(request.getAttribute(eq(key))).thenReturn(value), request::getAttributeNames)
                        .when(request).setAttribute(any(), any());
        RequestContextHolder.setRequestAttributes(attributes);
    }


    protected static Stubber mockAttributes(final BiFunction<String, Object, Object> mock,
                    final Supplier<Enumeration<String>> keysSupplier)
    {
        final Set<String> attributeKeys = new HashSet<>();
        when(keysSupplier.get()).thenReturn(Collections.enumeration(attributeKeys));
        return doAnswer(createAttributesAnswer(attributeKeys, mock));
    }


    protected static Answer<Object> createAttributesAnswer(final Set<String> keys, final BiFunction<String, Object, Object> mock)
    {
        return invocation -> {
            final String key = invocation.getArgument(0, String.class);
            final Object value = invocation.getArgument(1, Object.class);
            mock.apply(key, value);
            keys.add(key);
            return null;
        };
    }


    /**
     * Mocks all necessary {@link ThreadLocal} variables necessary to deal with zk components, e.g. {@link Execution},
     * {@link Desktop}, {@link WebApp}, {@link WebApplicationContext}, etc.
     */
    public static void mockZkEnvironment()
    {
        final ExecutionImpl execMock = mock(ExecutionImpl.class);
        final PageDefinition pageDefinition = mock(PageDefinition.class);
        Mockito.lenient().when(execMock.getCurrentPageDefinition()).thenReturn(pageDefinition);
        Mockito.lenient().when(pageDefinition.getComponentDefinition(any(Class.class), anyBoolean()))
                        .then((inv) -> LanguageDefinition.getDeviceTypes().stream()
                                        .map(deviceType -> Components.getDefinitionByDeviceType(deviceType, inv.getArgument(0, Class.class)))
                                        .filter(Objects::nonNull).findFirst().orElse(null));
        final Desktop deskMock = mock(DesktopImpl.class);
        Mockito.lenient().when(execMock.getDesktop()).thenReturn(deskMock);
        final DesktopCache deskCacheMock = mock(DesktopCache.class);
        Mockito.lenient().when(deskCacheMock.getDesktop(any())).thenReturn(deskMock);
        Mockito.lenient().when(deskCacheMock.getDesktopIfAny(any())).thenReturn(deskMock);
        Mockito.lenient().when(deskCacheMock.getNextKey()).thenReturn(0);
        final WebApp webAppMock = mock(WebApp.class, withSettings().extraInterfaces(WebAppCtrl.class));
        final Configuration cfg = mock(Configuration.class);
        Mockito.lenient().doReturn(cfg).when(webAppMock).getConfiguration();
        Mockito.lenient().when(deskMock.getWebApp()).thenReturn(webAppMock);
        Mockito.lenient().when(((WebAppCtrl)webAppMock).getDesktopCache(any())).thenReturn(deskCacheMock);
        WebAppsCtrl.setCurrent(webAppMock);
        final ServletContext scMock = mock(ServletContext.class);
        Mockito.lenient().when(webAppMock.getNativeContext()).thenReturn(scMock);
        Mockito.lenient().when(webAppMock.getServletContext()).thenReturn(scMock);
        final WebApplicationContext wacMock = mock(WebApplicationContext.class,
                        withSettings().extraInterfaces(CockpitApplicationContext.class));
        Mockito.lenient().when(scMock.getAttribute(anyString())).thenReturn(wacMock);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.lenient().when(execMock.getNativeRequest()).thenReturn(request);
        ExecutionsCtrl.setCurrent(execMock);
        final Session session = mockSessionWithAttributeHandling();
        Mockito.lenient().when(session.getWebApp()).thenReturn(webAppMock);
        SessionsCtrl.setCurrent(session);
        // ZK device setup
        if(!Devices.exists("ajax"))
        {
            Devices.add("ajax", AjaxDevice.class);
        }
        Library.setProperty("cockpitng.zk.resourcelocator.urlfilter", "^.*/zml-3.6.4.jar.*$");
        Library.setProperty("org.zkoss.zk.ui.sys.XMLResourcesLocator.class", FilteringClassLocator.class.getName());
    }


    static Session mockSessionWithAttributeHandling()
    {
        final Session mock = mock(Session.class);
        final Map<String, Object> sessionAttributes = new HashMap<>();
        Mockito.lenient().doAnswer(args -> {
            final Object[] arguments = args.getArguments();
            sessionAttributes.put((String)arguments[0], arguments[1]);
            return arguments[1];
        }).when(mock).setAttribute(any(), any());
        Mockito.lenient().doAnswer(args -> sessionAttributes.get(args.getArguments()[0])).when(mock).getAttribute(anyString());
        Mockito.lenient().doAnswer(args -> sessionAttributes.remove(args.getArguments()[0])).when(mock).removeAttribute(anyString());
        return mock;
    }


    /**
     * Informs zk {@link Execution} on how to deal with {@link AuResponse}
     *
     * @param processors
     *           registry of processors for {@link AuResponse}
     */
    public static void mockAuResponseProcessing(final AuResponseProcessorRegistry processors)
    {
        doAnswer(invocationOnMock -> {
            final AuResponse response = (AuResponse)invocationOnMock.getArguments()[0];
            processors.process(response);
            return null;
        }).when(Executions.getCurrent()).addAuResponse(any());
    }


    /**
     * Creates a processor for {@link AuEcho} requests. This processor notifies all listeners for this event type
     * immediately.
     *
     * @return processor to be registered in {@link AuResponseProcessorRegistry}
     */
    public static AuResponseProcessor<AuEcho> createEchoProcessor()
    {
        return new AuResponseProcessor<>()
        {
            @Override
            public void process(final AuEcho response)
            {
                final Object[] rawData = response.getRawData();
                if(rawData.length > 1 && (rawData[0] instanceof Component) && (rawData[1] instanceof String))
                {
                    final Component component = (Component)rawData[0];
                    final String eventName = Objects.toString(rawData[1]);
                    final Object dataKey = rawData.length == 2 ? null : rawData[2];
                    final Object data = dataKey != null ? AuEcho.getData(component, dataKey) : null;
                    simulateEvent(component, new Event(eventName, component, data));
                }
            }
        };
    }


    /**
     * Looks for a component of provided type (incl. parent). Method performs a deep-search in whole DOM tree of provided
     * parent.
     *
     * @param <C>
     *           type of component
     * @param parent
     *           component which child is to be found
     * @param childClass
     *           class of a child to be found
     * @return result of search
     */
    public static <C extends Component> Optional<C> find(final Component parent, final Class<? extends C> childClass)
    {
        return find(parent, childClass::isInstance);
    }


    /**
     * Looks for a component that meets of provided requirement (incl. parent). Method performs a deep-search in whole DOM
     * tree of provided parent.
     *
     * @param <C>
     *           expected type of component
     * @param parent
     *           component which child is to be found
     * @param test
     *           requirement for component
     * @return result of search
     */
    public static <C extends Component> Optional<C> find(final Component parent, final Predicate<Component> test)
    {
        if(test.test(parent))
        {
            return Optional.of((C)parent);
        }
        else
        {
            final Stream<C> children = findAllChildren(parent, test);
            return children.findFirst();
        }
    }


    /**
     * Looks for a all components of provided type (incl. parent). Method performs a deep-search in whole DOM tree of
     * provided parent.
     *
     * @param <C>
     *           type of component
     * @param parent
     *           component which child is to be found
     * @param childClass
     *           class of a child to be found
     * @return result of search
     */
    public static <C extends Component> Stream<C> findAll(final Component parent, final Class<? extends C> childClass)
    {
        return findAll(parent, childClass::isInstance);
    }


    /**
     * Looks for a all components that meets of provided requirement (incl. parent). Method performs a deep-search in whole
     * DOM tree of provided parent.
     *
     * @param <C>
     *           expected type of component
     * @param parent
     *           component which child is to be found
     * @param test
     *           requirement for component
     * @return result of search
     */
    public static <C extends Component> Stream<C> findAll(final Component parent, final Predicate<Component> test)
    {
        final Stream<C> parentStream;
        if(test.test(parent))
        {
            parentStream = Stream.of((C)parent);
        }
        else
        {
            parentStream = Stream.empty();
        }
        final Stream<C> children = findAllChildren(parent, test);
        return Stream.concat(parentStream, children);
    }


    /**
     * Looks for a child of provided type. Method performs a deep-search in whole DOM tree of provided parent.
     *
     * @param <C>
     *           type of component
     * @param parent
     *           component which child is to be found
     * @param childClass
     *           class of a child to be found
     * @return result of search
     */
    public static <C extends Component> Optional<C> findChild(final Component parent, final Class<? extends C> childClass)
    {
        return findChild(parent, childClass::isInstance);
    }


    /**
     * Looks for a child that meets of provided requirement. Method performs a deep-search in whole DOM tree of provided
     * parent.
     *
     * @param <C>
     *           expected type of child
     * @param parent
     *           component which child is to be found
     * @param test
     *           requirement for child
     * @return result of search
     */
    public static <C extends Component> Optional<C> findChild(final Component parent, final Predicate<Component> test)
    {
        final Stream<C> children = findAllChildren(parent, test);
        return children.findFirst();
    }


    /**
     * Looks for all children of provided type. Method performs a deep-search in whole DOM tree of provided parent.
     *
     * @param <C>
     *           type of component
     * @param parent
     *           component which child is to be found
     * @param childClass
     *           class of a child to be found
     * @return result of search
     */
    public static <C extends Component> Stream<C> findAllChildren(final Component parent, final Class<? extends C> childClass)
    {
        final Stream<C> shallowSearch = (Stream<C>)parent.getChildren().stream().filter(childClass::isInstance);
        final Stream<C> deepSearch = parent.getChildren().stream().flatMap(child -> findAllChildren(child, childClass));
        return Stream.concat(shallowSearch, deepSearch);
    }


    /**
     * Looks for all children that meets of provided requirement. Method performs a deep-search in whole DOM tree of
     * provided parent.
     *
     * @param <C>
     *           type of component
     * @param parent
     *           component which child is to be found
     * @param test
     *           requirement for child
     * @return result of search
     */
    public static <C extends Component> Stream<C> findAllChildren(final Component parent, final Predicate<Component> test)
    {
        final Stream<C> shallowSearch = (Stream<C>)parent.getChildren().stream().filter(test);
        final Stream<C> deepSearch = parent.getChildren().stream().flatMap(child -> findAllChildren(child, test));
        return Stream.concat(shallowSearch, deepSearch);
    }


    /**
     * Retrieves all CSS classes currently set on provided component
     *
     * @param component
     *           component, which CSS classes are to be retrieved
     * @return <code>true</code> if all classes are set
     */
    public static String[] getSClasses(final HtmlBasedComponent component)
    {
        return getSClasses(component.getSclass());
    }


    /**
     * Retrieves all CSS classes from provided sclass attribute of component
     *
     * @param sclass
     *           component's sclass attribute value
     * @return arrays of separate CSS classes included in provieded sclass attribute
     */
    public static String[] getSClasses(final String sclass)
    {
        if(!StringUtils.isBlank(sclass))
        {
            return sclass.split("\\s+");
        }
        else
        {
            return new String[0];
        }
    }


    /**
     * Checks if provided component has all specified CSS classes set
     *
     * @param component
     *           component, which CSS classes are to be tested
     * @param expectedClasses
     *           names of required classes
     * @return <code>true</code> if all classes are set
     */
    public static boolean isSClassSet(final HtmlBasedComponent component, final String... expectedClasses)
    {
        final List<String> classes = Arrays.asList(getSClasses(component));
        return classes.containsAll(Arrays.asList(expectedClasses));
    }


    /**
     * Checks if provided component has all specified CSS classes set. If not - assertion fails.
     *
     * @param component
     *           component, which CSS classes are to be tested
     * @param expectedClasses
     *           names of required classes
     */
    public static void testSClassSet(final HtmlBasedComponent component, final String... expectedClasses)
    {
        final List<String> classes = Arrays.asList(getSClasses(component));
        Assertions.assertThat(classes).contains((Object[])expectedClasses);
    }


    /**
     * Checks if provided component none of specified CSS classes set. If not - assertion fails.
     *
     * @param component
     *           component, which CSS classes are to be tested
     * @param unexpectedClasses
     *           names of classes that should not appear
     */
    public static void testSClassUnset(final HtmlBasedComponent component, final String... unexpectedClasses)
    {
        final List<String> classes = Arrays.asList(getSClasses(component));
        Assertions.assertThat(classes).excludes((Object[])unexpectedClasses);
    }


    /**
     * Checks if provided component none of specified CSS classes set.
     *
     * @param component
     *           component, which CSS classes are to be tested
     * @param unexpectedClasses
     *           names of classes that should not appear
     * @return <code>true</code> if none of provided classes are set
     */
    public static boolean isSClassUnset(final HtmlBasedComponent component, final String... unexpectedClasses)
    {
        final List<String> classes = Arrays.asList(getSClasses(component));
        return Collections.disjoint(classes, Arrays.asList(unexpectedClasses));
    }


    /**
     * See {@link #simulateEvent(Component, Event)}.
     */
    public static void simulateEvent(final Component component, final String eventName, final Object eventData)
    {
        simulateEvent(component, new Event(eventName, component, eventData));
    }


    /**
     * Simulates an {@link Event} at the specified {@link Component}. Note that it only calls the {@link EventListener}s
     * registered for the given event name and does NOT do zk internal state changes. That means, if you want to test the
     * correct execution of an {@link EventListener}, you have do make these state changes manually before calling this
     * method, e.g. if you simulate an onSelect event on a combobox, its selectedItem field is NOT being updated.
     */
    public static void simulateEvent(final Component component, final Event event)
    {
        final Iterable<EventListener<? extends Event>> eventListeners = component.getEventListeners(event.getName());
        for(final EventListener eventListener : eventListeners)
        {
            try
            {
                eventListener.onEvent(event);
            }
            catch(final Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    public static DefaultWidgetModel mockWidgetModel()
    {
        final DefaultWidgetModel widgetModel = mock(DefaultWidgetModel.class);
        final Answer<Object> widgetModelAnswer = invocation -> {
            final String key = (String)invocation.getArguments()[0];
            final Object value = invocation.getArguments()[1];
            Mockito.lenient().when(widgetModel.getValue(eq(key), any(Class.class))).thenReturn(value);
            return null;
        };
        Mockito.lenient().doAnswer(widgetModelAnswer).when(widgetModel).setValue(anyString(), any());
        Mockito.lenient().doAnswer(widgetModelAnswer).when(widgetModel).put(anyString(), any());
        return widgetModel;
    }


    public static WidgetInstanceManager mockWidgetInstanceManager()
    {
        return mockWidgetInstanceManager(new TypedSettingsMap());
    }


    public static WidgetInstanceManager mockWidgetInstanceManager(final TypedSettingsMap widgetSettings)
    {
        final DefaultWidgetModel widgetModel = mockWidgetModel();
        final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
        Mockito.lenient().when(widgetInstanceManager.getWidgetSettings()).thenReturn(widgetSettings);
        Mockito.lenient().when(widgetInstanceManager.getModel()).thenReturn(widgetModel);
        widgetSettings.clear();
        Mockito.lenient().doAnswer(invocation -> {
            final String key = (String)invocation.getArguments()[0];
            final Object value = invocation.getArguments()[1];
            if(!widgetSettings.containsKey(key))
            {
                widgetSettings.put(key, value);
            }
            return null;
        }).when(widgetInstanceManager).initWidgetSetting(anyString(), anyObject());
        Mockito.lenient().doAnswer(invocation -> invocation.getArguments()[0]).when(widgetInstanceManager)
                        .buildConfigurationContext(any(ConfigContext.class), any(Class.class));
        final Widgetslot widgetslot = mock(Widgetslot.class);
        final WidgetInstance widgetInstance = mock(WidgetInstance.class);
        Mockito.lenient().when(widgetslot.getWidgetInstance()).thenReturn(widgetInstance);
        Mockito.lenient().when(widgetInstanceManager.getWidgetslot()).thenReturn(widgetslot);
        return widgetInstanceManager;
    }
}
