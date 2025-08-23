/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing;

import com.google.common.base.Predicates;
import com.hybris.cockpitng.components.WidgetContainer;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.testing.annotation.ExtensibleWidget;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.testing.annotation.NullSafeWidget;
import com.hybris.cockpitng.testing.exception.NotExtensibleWidgetException;
import com.hybris.cockpitng.testing.exception.WidgetTestException;
import com.hybris.cockpitng.testing.util.internal.TestUtilInternal;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javassist.Modifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.junit.Assume;
import org.junit.Test;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

@ExtensibleWidget
public abstract class AbstractCockpitngUnitTest<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCockpitngUnitTest.class);


    protected void testNullSafeMethods(final Object target, final Collection<Method> methodsToTest)
    {
        Assume.assumeTrue(TestUtilInternal.isNullsafe(this.getClass()));
        final StringBuilder errors = new StringBuilder();
        int errorCounter = 0;
        for(final Method method : methodsToTest)
        {
            if(!TestUtilInternal.isNullsafe(method))
            {
                continue;
            }
            for(final Object[] args : TestUtilInternal
                            .prepareAllCombinations(TestUtilInternal.prepareMatchingParametersNullSafe(method, this.getClass())))
            {
                final boolean accessible = method.isAccessible();
                method.setAccessible(true);
                try
                {
                    method.invoke(target, args);
                }
                catch(final Exception e)
                {
                    LOG.debug("Error occurred", e);
                    if(InvocationTargetException.class.isAssignableFrom(e.getClass())
                                    && (e.getCause() != null && IllegalArgumentException.class.isAssignableFrom(e.getCause().getClass())))
                    {
                        // swallow by purpose - the class appropriately reports that the argument is wrong
                        continue;
                    }
                    final StringBuilder argsString = buildArgs(args);
                    final StringWriter stackTrace = new StringWriter();
                    e.printStackTrace(new PrintWriter(stackTrace));
                    errors.append(String.format("\nMethod:%s\nfailed for arguments: (%s)\noriginal stack trace:\n%s\n",
                                    method.toGenericString(), argsString.toString(), stackTrace.toString()));
                    errorCounter++;
                }
                finally
                {
                    method.setAccessible(accessible);
                }
            }
        }
        if(errors.length() > 0)
        {
            throw new WidgetTestException("If the widget is not null-safe annotate the test class with: " + NullSafeWidget.class
                            + ".\nFailed with " + errorCounter + " errors on:\n" + errors.toString().trim());
        }
    }


    private static StringBuilder buildArgs(final Object[] args)
    {
        final StringBuilder argsString = new StringBuilder();
        for(final Object arg : args)
        {
            if(argsString.length() > 0)
            {
                argsString.append(", ");
            }
            argsString.append(arg);
        }
        return argsString;
    }


    @Test
    public void testExtensibleConstructors()
    {
        Assume.assumeTrue(TestUtilInternal.isExtensible(this.getClass(), ExtensibleWidget.CONSTRUCTORS));
        if(Modifier.isFinal(getWidgetType().getModifiers()))
        {
            LOG.error("Not extensible controller class [{}]", getWidgetType());
            throw new NotExtensibleWidgetException(NotExtensibleWidgetException.FINAL_WIDGET_CLASS);
        }
        if(getWidgetType().isInterface())
        {
            return;
        }
        boolean hasAccessibleConstructor = false;
        boolean hasAccessibleNonPackageConstructor = false;
        for(final Constructor<?> constructor : getWidgetType().getDeclaredConstructors())
        {
            final boolean aPublic = Modifier.isPublic(constructor.getModifiers());
            final boolean aProtected = Modifier.isProtected(constructor.getModifiers());
            final boolean aPackage = Modifier.isPackage(constructor.getModifiers());
            if(aPublic || aProtected || aPackage)
            {
                hasAccessibleConstructor = true;
                if(!aPackage)
                {
                    hasAccessibleNonPackageConstructor = true;
                    break;
                }
            }
        }
        if(!hasAccessibleConstructor)
        {
            LOG.error("Not extensible controller class [{}]", getWidgetType());
            throw new NotExtensibleWidgetException(NotExtensibleWidgetException.NO_ACCESSIBLE_CONSTRUCTOR);
        }
        if(!hasAccessibleNonPackageConstructor)
        {
            LOG.warn("The available constructor(s) are of package availability for class [{}]", getWidgetType());
            throw new NotExtensibleWidgetException(NotExtensibleWidgetException.PACKAGE_PRIVATE_CONSTRUCTOR);
        }
    }


    /**
     * The test checks if all service/facade/ZK fields are accessible either via modifier (protected or less restrictive) or
     * getter.
     */
    @Test
    public void testExtensibleFields()
    {
        Assume.assumeTrue(TestUtilInternal.isExtensible(this.getClass(), ExtensibleWidget.FIELDS));
        boolean atLeastOneNonExtensibleFieldExists = false;
        final Class<?> widgetType = getWidgetType();
        final Field[] fields = com.hybris.cockpitng.core.util.impl.ReflectionUtils.getAllDeclaredFields(widgetType);
        for(final Field field : fields)
        {
            if(!checkExtensibleField(widgetType, field))
            {
                atLeastOneNonExtensibleFieldExists = true;
            }
        }
        if(atLeastOneNonExtensibleFieldExists)
        {
            throw new NotExtensibleWidgetException(NotExtensibleWidgetException.INACCESSIBLE_ZK_FIELD);
        }
    }


    private static boolean checkExtensibleField(final Class<?> widgetType, final Field field)
    {
        final boolean zkField = isZKField(field);
        if(zkField || isServiceOrFacadeField(field))
        {
            if(isPrivateOrPackage(field) && !hasGetter(widgetType, field))
            {
                final String fieldType = zkField ? "ZK" : "Service/Facade";
                final String visibility = Modifier.isPrivate(field.getModifiers()) ? "private" : "package";
                LOG.error("Not extensible {} {} field [{}]", visibility, fieldType, field.toGenericString());
                return false;
            }
        }
        return true;
    }


    private static boolean isPrivateOrPackage(final Field field)
    {
        return Modifier.isPrivate(field.getModifiers()) || Modifier.isPackage(field.getModifiers());
    }


    private static boolean isZKField(final Field field)
    {
        final Package typePackage = field.getType().getPackage();
        if(typePackage == null)
        {
            return false;
        }
        final String packageName = typePackage.getName();
        boolean zkField = packageName.startsWith("org.zkoss.zul");
        zkField = zkField || WidgetContainer.class.isAssignableFrom(field.getType());
        zkField = zkField || Component.class.isAssignableFrom(field.getType());
        return zkField;
    }


    private static boolean isServiceOrFacadeField(final Field field)
    {
        final String fieldName = field.getName();
        return fieldName.endsWith("Service") || fieldName.endsWith("Facade");
    }


    private static boolean hasGetter(final Class<?> widgetType, final Field field)
    {
        for(final Method method : ReflectionUtils.getAllMethods(widgetType,
                        Predicates.or(ReflectionUtils.withPrefix("get"), ReflectionUtils.withPrefix("is")),
                        Predicates.or(ReflectionUtils.withModifier(java.lang.reflect.Modifier.PUBLIC),
                                        ReflectionUtils.withModifier(java.lang.reflect.Modifier.PROTECTED))))
        {
            if(method.getParameterTypes().length == 0 && field.getType().isAssignableFrom(method.getReturnType())
                            && field.getDeclaringClass().isAssignableFrom(method.getDeclaringClass()))
            {
                final String methodName = method.getName();
                final String capitalizedName = WordUtils.capitalize(field.getName());
                if(methodName.equals("get" + capitalizedName) || methodName.equals("is" + capitalizedName))
                {
                    return true;
                }
            }
        }
        return false;
    }


    @Test
    public void testExtensibleMethods()
    {
        Assume.assumeTrue(TestUtilInternal.isExtensible(this.getClass(), ExtensibleWidget.METHODS));
        Class type = getWidgetType();
        do
        {
            final ExtensibleWidget extensibleWidget = getClass().getAnnotation(ExtensibleWidget.class);
            final List<Method> methodsToSkipList = getMethodsToSkip(extensibleWidget);
            for(final Method method : type.getDeclaredMethods())
            {
                if(canBeSkipped(methodsToSkipList, method))
                {
                    continue;
                }
                if(!method.isSynthetic() && !Modifier.isStatic(method.getModifiers()) && !Modifier.isNative(method.getModifiers()))
                {
                    throwIfFinal(method);
                    trowIfPrivate(method);
                    throwIfPackage(method);
                }
            }
            type = type.getSuperclass();
        }
        while(type != null && !DefaultWidgetController.class.equals(type) && !ViewAnnotationAwareComposer.class.equals(type)
                        && !AbstractCockpitEditorRenderer.class.equals(type));
    }


    protected boolean canBeSkipped(final List<Method> methodsToSkipList, final Method method)
    {
        return method.getDeclaringClass().getPackage().getName().startsWith("java.lang") || methodsToSkipList.contains(method)
                        || method.getAnnotation(InextensibleMethod.class) != null;
    }


    protected List<Method> getMethodsToSkip(final ExtensibleWidget extensibleWidget)
    {
        if(extensibleWidget != null)
        {
            final String privateMethodsProviderName = extensibleWidget.privateMethodsProviderName();
            if(StringUtils.isNotBlank(privateMethodsProviderName))
            {
                try
                {
                    final Method privateMethodProvider = this.getClass().getMethod(privateMethodsProviderName, null);
                    return (List<Method>)privateMethodProvider.invoke(this);
                }
                catch(final Exception ex)
                {
                    throw new CockpitTestRuntimeException(ex);
                }
            }
        }
        return Collections.emptyList();
    }


    private static void throwIfPackage(final Method method)
    {
        if(Modifier.isPackage(method.getModifiers()))
        {
            throw new NotExtensibleWidgetException(NotExtensibleWidgetException.INEXTENSIBLE_METHOD,
                            String.format("Not extensible package method [%s]", method.toGenericString()));
        }
    }


    private void trowIfPrivate(final Method method)
    {
        if(Modifier.isPrivate(method.getModifiers()) && !isSpecialPrivateMethod(method))
        {
            throw new NotExtensibleWidgetException(NotExtensibleWidgetException.INEXTENSIBLE_METHOD,
                            String.format("Not extensible private method [%s]", method.toGenericString()));
        }
    }


    private static void throwIfFinal(final Method method)
    {
        if(Modifier.isFinal(method.getModifiers()))
        {
            throw new NotExtensibleWidgetException(NotExtensibleWidgetException.INEXTENSIBLE_METHOD,
                            String.format("Not extensible final method [%s]", method.toGenericString()));
        }
    }


    protected boolean isSpecialPrivateMethod(final Method method)
    {
        final boolean serializableWrite = "writeObject".equals(method.getName()) && method.getParameterTypes().length == 1
                        && java.io.ObjectOutputStream.class.equals(method.getParameterTypes()[0]);
        final boolean serializableRead = "readObject".equals(method.getName()) && method.getParameterTypes().length == 1
                        && java.io.ObjectInputStream.class.equals(method.getParameterTypes()[0]);
        final boolean serializableNoData = "readObjectNoData".equals(method.getName()) && method.getParameterTypes().length == 0;
        return serializableRead || serializableWrite || serializableNoData;
    }


    protected Class<? extends T> getWidgetType()
    {
        return com.hybris.cockpitng.core.util.impl.ReflectionUtils.extractGenericParameterType(getClass(),
                        AbstractCockpitngUnitTest.class);
    }


    private static class CockpitTestRuntimeException extends RuntimeException
    {
        public CockpitTestRuntimeException(final Throwable cause)
        {
            super(cause);
        }
    }
}
