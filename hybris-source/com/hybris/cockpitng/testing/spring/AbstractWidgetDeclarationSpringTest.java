/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.hybris.cockpitng.actions.ActionDefinition;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.SocketConnectionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.WidgetSocket.Multiplicity;
import com.hybris.cockpitng.core.WidgetSocket.SocketVisibility;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.json.impl.DefaultJSONMapper;
import com.hybris.cockpitng.testing.AbstractWidgetUnitTest;
import com.hybris.cockpitng.testing.annotation.SocketsAreJsonSerializable;
import com.hybris.cockpitng.testing.exception.WidgetTestException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zkoss.zk.ui.event.Event;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractWidgetDeclarationSpringTest
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractWidgetDeclarationSpringTest.class);
    protected DefaultJSONMapper jsonMapper;
    @Resource(name = "cockpitComponentDefinitionService")
    protected CockpitComponentDefinitionService cockpitComponentDefinitionService;
    @Resource(name = "socketConnectionService")
    private SocketConnectionService socketConnectionService;
    private List<AbstractCockpitComponentDefinition> allComponentDefinitions;
    private boolean hasDuplicatedIds = false;


    public List<AbstractCockpitComponentDefinition> getAllComponentDefinitions()
    {
        return allComponentDefinitions;
    }


    @Before
    public void prepareTestInstance()
    {
        try
        {
            allComponentDefinitions = cockpitComponentDefinitionService.getAllComponentDefinitions();
        }
        catch(final IllegalStateException e)
        {
            hasDuplicatedIds = true;
            Assert.fail(String.format("At least two widgets/actions/editors have the same code: %s ", e.getMessage()));
            throw e;
        }
    }


    @Test
    public void testWidgetController()
    {
        Assume.assumeFalse(hasDuplicatedIds);
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final Set<String> errors = new HashSet<>();
        for(final AbstractCockpitComponentDefinition def : allComponentDefinitions)
        {
            if(def instanceof WidgetDefinition)
            {
                final String controllerClass = ((WidgetDefinition)def).getController();
                if(StringUtils.isBlank(controllerClass))
                {
                    continue;
                }
                if(!isValidType(classLoader, controllerClass))
                {
                    errors.add("Widget's controller cannot be resolved: " + controllerClass);
                }
            }
            if(def instanceof ActionDefinition)
            {
                final String actionClassName = ((ActionDefinition)def).getActionClassName();
                if(!isValidType(classLoader, actionClassName))
                {
                    errors.add("Actions's class cannot be resolved: " + actionClassName);
                }
                final String customRendererClassName = ((ActionDefinition)def).getCustomRendererClassName();
                if(StringUtils.isNotBlank(customRendererClassName) && !isValidType(classLoader, customRendererClassName))
                {
                    errors.add("Actions's custom renderer cannot be resolved: " + customRendererClassName);
                }
            }
        }
        if(!errors.isEmpty())
        {
            for(final String error : errors)
            {
                LOG.error(error);
            }
            fail("There are " + errors.size() + " misdeclared widget controller types. See console output for more details.");
        }
    }


    @Test
    public void testSocketUniqueness()
    {
        Assume.assumeFalse(hasDuplicatedIds);
        for(final AbstractCockpitComponentDefinition def : allComponentDefinitions)
        {
            if(def instanceof WidgetDefinition)
            {
                final String controllerClass = ((WidgetDefinition)def).getController();
                if(StringUtils.isBlank(controllerClass))
                {
                    continue;
                }
                final Set<String> ids = new HashSet<>();
                for(final WidgetSocket input : def.getInputs())
                {
                    final String id = input.getId();
                    Assert.assertTrue("Input socket's name is not unique: " + id + " for widget "
                                    + StringUtils.defaultIfBlank(def.getCode(), controllerClass), ids.add(id));
                }
                ids.clear();
                for(final WidgetSocket output : def.getOutputs())
                {
                    final String id = output.getId();
                    Assert.assertTrue("Output socket's name is not unique: " + id + " for widget "
                                    + StringUtils.defaultIfBlank(def.getCode(), controllerClass), ids.add(id));
                }
            }
        }
    }


    @Test
    public void testSocketTypeExists()
    {
        Assume.assumeFalse(hasDuplicatedIds);
        final Set<String> errors = new HashSet<>();
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for(final AbstractCockpitComponentDefinition def : allComponentDefinitions)
        {
            if(def instanceof WidgetDefinition)
            {
                for(final WidgetSocket input : def.getInputs())
                {
                    if(!isValidType(classLoader, input.getDataType()))
                    {
                        errors.add(
                                        "Input socket type does not exist: " + def.getCode() + "->" + input.getId() + ":" + input.getDataType());
                    }
                }
                for(final WidgetSocket output : def.getOutputs())
                {
                    if(!isValidType(classLoader, output.getDataType()))
                    {
                        errors.add("Output socket type does not exist: " + def.getCode() + "->" + output.getId() + ":"
                                        + output.getDataType());
                    }
                }
            }
            if(def instanceof ActionDefinition && (!isValidType(classLoader, ((ActionDefinition)def).getInputType())))
            {
                errors.add("Input socket type does not exist: " + def.getCode() + " Declaring action: " + def.getCode());
            }
        }
        if(!errors.isEmpty())
        {
            for(final String error : errors)
            {
                LOG.error(error);
            }
            fail("There are " + errors.size() + " misdeclared socket types. See console output for more details.");
        }
    }


    private boolean isValidType(final ClassLoader classLoader, final String dataType)
    {
        if(StringUtils.isNotBlank(dataType) && !StringUtils.endsWithIgnoreCase(dataType, ".groovy")
                        && !isGenericTypeDeclaration(dataType) && !dataType.startsWith("&lt;"))
        {
            try
            {
                classLoader.loadClass(dataType.trim());
            }
            catch(final ClassNotFoundException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Error occurred", e);
                }
                return false;
            }
        }
        return true;
    }


    private boolean isGenericTypeDeclaration(final String dataType)
    {
        return dataType.matches("[\\[<].*[\\]>]");
    }


    @Test
    public void testInputSocketNameAndTypeCompatibility() throws ClassNotFoundException
    {
        Assume.assumeFalse(hasDuplicatedIds);
        boolean fail = false;
        for(final AbstractCockpitComponentDefinition def : allComponentDefinitions)
        {
            final List<String> errorMessages = new ArrayList<>();
            if(def instanceof WidgetDefinition)
            {
                final String controllerClass = ((WidgetDefinition)def).getController();
                if(StringUtils.isBlank(controllerClass) || StringUtils.endsWithIgnoreCase(controllerClass, ".groovy"))
                {
                    continue;
                }
                final Map<String, Method> declaredSocketInputs = findAllDeclaredSocketInputs(controllerClass);
                for(final WidgetSocket xmlSocket : def.getInputs())
                {
                    final WidgetSocket declaredSocket = Mockito.mock(WidgetSocket.class);
                    final Method method = declaredSocketInputs.get(xmlSocket.getId());
                    if(method == null && def.getForwardMap().containsKey(xmlSocket.getId()))
                    {
                        continue;
                    }
                    else if(method == null)
                    {
                        fail = true;
                        errorMessages.add("Socket: " + xmlSocket.getId() + " not found on controller class: " + controllerClass);
                        continue;
                    }
                    final Multiplicity multiplicity = method.getParameterTypes().length > 0 ? detectMultiplicity(
                                    method.getParameterTypes()[0], xmlSocket.getDataMultiplicity()) : null;
                    when(declaredSocket.getDataMultiplicity()).thenReturn(multiplicity);
                    when(declaredSocket.getDataType()).thenReturn(multiplicity == null && method.getParameterTypes().length > 0
                                    ? method.getParameterTypes()[0].getName() : Object.class.getName());
                    when(declaredSocket.getId()).thenReturn(xmlSocket.getId());
                    when(declaredSocket.getVisibility()).thenReturn(SocketVisibility.EXTERNAL);
                    when(declaredSocket.toString()).thenCallRealMethod();
                    final Widget widgetA = Mockito.mock(Widget.class);
                    final Widget widgetB = Mockito.mock(Widget.class);
                    final TypedSettingsMap widgetSettings = Mockito.mock(TypedSettingsMap.class);
                    when(widgetSettings.containsKey("socketDataType_$T")).thenReturn(Boolean.TRUE);
                    when(widgetSettings.getString("socketDataType_$T")).thenReturn(Object.class.getCanonicalName());
                    when(widgetA.getWidgetSettings()).thenReturn(widgetSettings);
                    when(widgetB.getWidgetSettings()).thenReturn(widgetSettings);
                    final boolean canReceiveFrom = socketConnectionService.canReceiveFrom(declaredSocket, widgetA, xmlSocket, widgetB);
                    if(!canReceiveFrom)
                    {
                        fail = true;
                        errorMessages.add(controllerClass + "#" + declaredSocket.getId() + " socket definition:");
                        errorMessages.add("\tData type: " + declaredSocket.getDataType() + " expected: " + xmlSocket.getDataType());
                        errorMessages.add("\tData multiplicity: " + declaredSocket.getDataMultiplicity() + " expected: "
                                        + xmlSocket.getDataMultiplicity());
                    }
                }
            }
            if(errorMessages.isEmpty())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Widget: " + def.getCode() + " OK");
                }
            }
            else
            {
                LOG.error("Widget: " + def.getCode() + " FAILED");
                for(final String message : errorMessages)
                {
                    LOG.error(message);
                }
                errorMessages.clear();
            }
        }
        if(fail)
        {
            fail("At least one widget has wrong implementation. See log for further inforamtion.");
        }
    }


    @Test
    public void testForwardSocketEvents()
    {
        Assume.assumeFalse(hasDuplicatedIds);
        final List<String> errors = new ArrayList<>();
        for(final AbstractCockpitComponentDefinition def : allComponentDefinitions)
        {
            if(def instanceof WidgetDefinition)
            {
                final Map<String, String> forwardMap = def.getForwardMap();
                if(!forwardMap.isEmpty())
                {
                    final List<WidgetSocket> inputs = def.getInputs();
                    final List<WidgetSocket> outputs = def.getOutputs();
                    for(final Map.Entry<String, String> entry : forwardMap.entrySet())
                    {
                        final String input = entry.getKey();
                        final String output = entry.getValue();
                        WidgetSocket widgetFrom = null;
                        WidgetSocket widgetTo = null;
                        for(final WidgetSocket inWidget : inputs)
                        {
                            if(input.equals(inWidget.getId()))
                            {
                                widgetFrom = inWidget;
                                break;
                            }
                        }
                        for(final WidgetSocket outWidget : outputs)
                        {
                            if(output.equals(outWidget.getId()))
                            {
                                widgetTo = outWidget;
                                break;
                            }
                        }
                        final Widget targetWidget = new Widget("test1234");
                        final Widget sourceWidget = new Widget("test4321");
                        if(widgetFrom == null || widgetTo == null)
                        {
                            errors.add("Invalid forward from [" + input + "=" + widgetFrom + "] to [" + output + "=" + widgetTo
                                            + "]. At least one socket does not exist. " + def.getCode());
                        }
                        else if(!socketConnectionService.canReceiveFrom(widgetTo, targetWidget, widgetFrom, sourceWidget))
                        {
                            errors.add("Invalid forward from [" + input + "=" + widgetFrom + "] to [" + output + "=" + widgetTo
                                            + "]. Sockets have incompatible types. " + def.getCode());
                        }
                    }
                }
            }
        }
        if(!errors.isEmpty())
        {
            for(final String error : errors)
            {
                LOG.error(error);
            }
            fail("Found " + errors.size() + " misdeclared forward(s).");
        }
    }


    private Map<String, Method> findAllDeclaredSocketInputs(final String controllerClass) throws ClassNotFoundException
    {
        final Class<?> controllerType = Class.forName(controllerClass);
        final AbstractWidgetUnitTest<Object> test = new AbstractWidgetUnitTest<Object>()
        {
            @Override
            protected Class<?> getWidgetType()
            {
                return controllerType;
            }


            @Override
            protected Object getWidgetController()
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Creating mock of widget instance: " + controllerType);
                }
                if(Modifier.isFinal(controllerType.getModifiers()))
                {
                    for(final Constructor<?> constructor : controllerType.getConstructors())
                    {
                        if(constructor.getParameterTypes().length == 0)
                        {
                            try
                            {
                                return constructor.newInstance();
                            }
                            catch(final Exception e)
                            {
                                throw new WidgetTestException(e);
                            }
                        }
                    }
                }
                else
                {
                    return Mockito.mock(controllerType);
                }
                throw new IllegalStateException("Could not instantiate class: " + controllerType);
            }
        };
        AbstractWidgetUnitTest.setUpAbstractWidgetUnitTestClass();
        test.setUpAbstractWidgetUnitTest();
        return AbstractWidgetUnitTest.getDeclaredSocketInputs();
    }


    private Multiplicity detectMultiplicity(final Class<?> type, final Multiplicity multiplicity)
    {
        if(List.class.isAssignableFrom(type))
        {
            return Multiplicity.LIST;
        }
        if(Set.class.isAssignableFrom(type))
        {
            return Multiplicity.SET;
        }
        if(Collection.class.isAssignableFrom(type))
        {
            return Multiplicity.COLLECTION;
        }
        if(Object.class.equals(type) || Event.class.isAssignableFrom(type))
        {
            return multiplicity;
        }
        return null;
    }


    /**
     * Verifies if socket inputs declared in definition.xml files are serializable, what is required to work with
     * Angular.js framework properly. This test is executed only if test class deriving from this class is annotated
     * with @SocketsAreJsonSerializable annotation
     */
    @Test
    public void socketInputsShouldBeSerializable()
    {
        Assume.assumeTrue(allComponentDefinitions.isEmpty() && isSocketJsonSerializableAnnotated());
        allComponentDefinitions.stream().filter(Objects::nonNull).forEach(widget -> {
            final List<WidgetSocket> socketInputs = widget.getInputs();
            socketInputs.forEach(input -> assertThatObjectIsJsonSerializable(input.getDataType(), input.getId()));
        });
    }


    /**
     * Verifies if socket outputs declared in definition.xml files are serializable, what is required to work with
     * Angular.js framework properly. This test is executed only if test class deriving from this class is annotated
     * with @SocketsAreJsonSerializable annotation
     */
    @Test
    public void socketOutputsShouldBeSerializable()
    {
        Assume.assumeTrue(allComponentDefinitions.isEmpty() && isSocketJsonSerializableAnnotated());
        allComponentDefinitions.stream().filter(Objects::nonNull).forEach(widget -> {
            final List<WidgetSocket> socketOutputs = widget.getOutputs();
            socketOutputs.forEach(output -> assertThatObjectIsJsonSerializable(output.getDataType(), output.getId()));
        });
    }


    private boolean isSocketJsonSerializableAnnotated()
    {
        return AnnotationUtils.findAnnotation(this.getClass(), SocketsAreJsonSerializable.class) != null;
    }


    public void assertThatObjectIsJsonSerializable(final Object object, final String socketName)
    {
        assertThat(object).isNotNull();
        assertThat(socketName).isNotEmpty();
        final String jsonRepresentation = jsonMapper.toJSONString(object);
        final String jsonRepresentationErrorMessage = String.format("JSON representation for socket %s is empty", socketName);
        assertThat(jsonRepresentation).isNotEmpty().overridingErrorMessage(jsonRepresentationErrorMessage);
        final Object serializedObject = jsonMapper.fromJSONString(jsonRepresentation, object.getClass());
        final String serializedObjectErrorMessage = String.format("serialized object for socket %s is null", socketName);
        assertThat(serializedObject).isNotNull().overridingErrorMessage(serializedObjectErrorMessage);
    }
}
