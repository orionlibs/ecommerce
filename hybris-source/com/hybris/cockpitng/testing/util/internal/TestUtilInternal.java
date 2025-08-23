/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util.internal;

import com.google.common.base.Defaults;
import com.google.common.collect.Lists;
import com.hybris.cockpitng.testing.annotation.ExtensibleWidget;
import com.hybris.cockpitng.testing.annotation.NullSafeWidget;
import com.hybris.cockpitng.testing.providers.NullProxyMethodValueProvider;
import com.hybris.cockpitng.testing.providers.ProxyMethodValueProvider;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javassist.Modifier;
import org.slf4j.LoggerFactory;

/**
 * Internal utility class to be used by general purpose test classes.
 *
 * @see com.hybris.cockpitng.testing.AbstractWidgetUnitTest
 * @see com.hybris.cockpitng.testing.AbstractActionUnitTest
 */
public final class TestUtilInternal
{
    private TestUtilInternal()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    /**
     * @param clazz
     *           type to be checked if annotated with @NullSafeWidget
     * @return true if the given type is not annotated with @NullSafeWidget or the annotation has value of true
     * @see com.hybris.cockpitng.testing.annotation.NullSafeWidget
     */
    public static boolean isNullsafe(final Class<?> clazz)
    {
        final NullSafeWidget nsw = clazz.getAnnotation(NullSafeWidget.class);
        return nsw == null || nsw.value();
    }


    /**
     * @param method
     *           method to be checked if annotated with @NullSafeWidget
     * @return true if the given type is not annotated with @NullSafeWidget or the annotation has value of true
     * @see com.hybris.cockpitng.testing.annotation.NullSafeWidget
     */
    public static boolean isNullsafe(final Method method)
    {
        final NullSafeWidget nsw = method.getAnnotation(NullSafeWidget.class);
        return nsw == null || nsw.value();
    }


    /**
     * @param clazz
     *           type to be checked
     * @return true if the class has a default or zero-argument constructor
     * @since 2.0.6
     */
    public static boolean hasDefaultOrNoParameterConstructor(final Class<?> clazz)
    {
        for(final Constructor<?> contructor : clazz.getDeclaredConstructors())
        {
            if(contructor.getParameterTypes().length == 0)
            {
                return true;
            }
        }
        return false;
    }


    /**
     * Helper method to fetch all the methods (public, private, protected, package) of a type with or without supertype
     * declared methods.
     *
     * @param clazz
     *           type to fetch its methods
     * @param includeSuperTypes
     *           if set to true methods from all the supertypes will be included.
     * @return Collection of all the methods declared on the type (and optionally its supertypes).
     */
    public static Collection<Method> getAllMethods(final Class<?> clazz, final boolean includeSuperTypes)
    {
        final Collection<Method> methods = Lists.newArrayList();
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        final Class<?> superclass = clazz.getSuperclass();
        if(includeSuperTypes && superclass != null)
        {
            methods.addAll(getAllMethods(superclass, true));
        }
        return methods;
    }


    /**
     * @param method
     *           a method for which possible input parameters will be generated
     * @param testClass
     *           test class that implements the test
     * @return a list of lists of all possible parameters, sub-lists' positions reflect the position of attribute they may
     *         be used as an input parameter
     */
    public static List<List<Object>> prepareMatchingParametersNullSafe(final Method method, final Class<?> testClass)
    {
        final List<List<Object>> possibleParams = new ArrayList<>();
        final int level = getNullSafeLevel(testClass);
        for(final Class<?> param : method.getParameterTypes())
        {
            possibleParams.add(preparePossibleMatchingValues(param, level, getProxyMethodValueProvider(method)));
        }
        return possibleParams;
    }


    private static int getNullSafeLevel(final Class<?> testClass)
    {
        final NullSafeWidget annotation = testClass.getAnnotation(NullSafeWidget.class);
        if(annotation != null)
        {
            return annotation.level();
        }
        return NullSafeWidget.ALL;
    }


    private static ProxyMethodValueProvider getProxyMethodValueProvider(final Method method)
    {
        NullSafeWidget annotation = method.getAnnotation(NullSafeWidget.class);
        if(annotation == null || annotation.proxyMethodValueProvider() == null)
        {
            annotation = method.getDeclaringClass().getAnnotation(NullSafeWidget.class);
        }
        if(annotation != null)
        {
            final Class<? extends ProxyMethodValueProvider> provider = annotation.proxyMethodValueProvider();
            if(provider != null && !NullProxyMethodValueProvider.class.equals(provider))
            {
                try
                {
                    return provider.getConstructor().newInstance();
                }
                catch(final Exception e)
                {
                    throw new IllegalArgumentException(ProxyMethodValueProvider.class.getCanonicalName() + " implementation " + "must"
                                    + " provide public, no-argument constructor", e);
                }
            }
        }
        return null;
    }


    /**
     * @return a collection of attributes of matching type (clazz is assignable from all the returned values in case of
     *         non-primitive values, the collection may contain null).
     */
    private static List<Object> preparePossibleMatchingValues(final Class<?> clazz, final int level,
                    final ProxyMethodValueProvider proxyMethodValueProvider)
    {
        final List<Object> matchingArgs = new ArrayList<>();
        if(matchesPattern(level, NullSafeWidget.DEFAULT_VALUES))
        {
            matchingArgs.add(Defaults.defaultValue(clazz));
        }
        if(matchesPattern(level, NullSafeWidget.PROXY_VALUES))
        {
            if(clazz.isEnum())
            {
                matchingArgs.addAll(Arrays.asList(clazz.getEnumConstants()));
            }
            else if(clazz.isArray())
            {
                matchingArgs.add(Array.newInstance(clazz.getComponentType(), 0));
            }
            else if(CollectionsDefaults.hasDefaultValue(clazz))
            {
                matchingArgs.add(CollectionsDefaults.defaultValue(clazz));
            }
            else if(clazz.isInterface() && !org.zkoss.zk.ui.Component.class.isAssignableFrom(clazz))
            {
                final Object pattern = determinePattern(clazz, proxyMethodValueProvider);
                matchingArgs.add(pattern);
                if(matchesPattern(level, NullSafeWidget.PROXY_EXCEPTIONS))
                {
                    matchingArgs.add(Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]
                                    {clazz}, (proxy, method, args) -> {
                        if(method.getExceptionTypes().length > 0)
                        {
                            for(final Class<?> exceptionClass : method.getExceptionTypes())
                            {
                                if(!RuntimeException.class.isAssignableFrom(exceptionClass)
                                                && Exception.class.isAssignableFrom(exceptionClass))
                                {
                                    final Exception exceptionInstance = tryToInstantiate((Class<? extends Exception>)exceptionClass);
                                    if(exceptionInstance != null)
                                    {
                                        throw exceptionInstance;
                                    }
                                }
                            }
                        }
                        return method.invoke(pattern, args);
                    }));
                }
            }
            else if(!clazz.isPrimitive())
            {
                final Object newOne = tryToInstantiate(clazz);
                if(newOne != null)
                {
                    matchingArgs.add(newOne);
                }
            }
        }
        return matchingArgs;
    }


    private static Object determinePattern(final Class<?> clazz, final ProxyMethodValueProvider proxyMethodValueProvider)
    {
        final Object pattern;
        if(List.class.isAssignableFrom(clazz))
        {
            pattern = Collections.emptyList();
        }
        else if(Set.class.isAssignableFrom(clazz))
        {
            pattern = Collections.emptySet();
        }
        else if(Map.class.isAssignableFrom(clazz))
        {
            pattern = Collections.emptyMap();
        }
        else if(Collection.class.isAssignableFrom(clazz))
        {
            pattern = Collections.emptyList();
        }
        else if(ListIterator.class.isAssignableFrom(clazz))
        {
            pattern = Collections.emptyListIterator();
        }
        else if(Iterator.class.isAssignableFrom(clazz))
        {
            pattern = Collections.emptyIterator();
        }
        else
        {
            pattern = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]
                            {clazz}, (proxy, method, args) -> prepareDefaultReturnValueInstance(method, proxyMethodValueProvider));
        }
        return pattern;
    }


    private static boolean matchesPattern(final int given, final int patternToMatch)
    {
        return (patternToMatch & given) == patternToMatch || (given & NullSafeWidget.ALL) == NullSafeWidget.ALL;
    }


    private static <T> T tryToInstantiate(final Class<? extends T> clazz)
    {
        for(final Constructor constructor : clazz.getConstructors())
        {
            if(Modifier.isPublic(constructor.getModifiers()) && constructor.getParameterTypes().length == 0)
            {
                try
                {
                    return (T)constructor.newInstance();
                }
                catch(final Exception ex)
                {
                    // swallow by purpose -> we have no guarantees that the class can be instantiated from this point
                    LoggerFactory.getLogger(TestUtilInternal.class).debug("Can't create instance", ex);
                }
            }
        }
        return null;
    }


    private static Object prepareDefaultReturnValueInstance(final Method method,
                    final ProxyMethodValueProvider proxyMethodValueProvider)
    {
        if(proxyMethodValueProvider != null && proxyMethodValueProvider.canProvideValue(method))
        {
            return proxyMethodValueProvider.getValue(method);
        }
        final Class<?> returnType = method.getReturnType();
        if(returnType.isInterface())
        {
            if(CollectionsDefaults.hasDefaultValue(returnType))
            {
                return CollectionsDefaults.defaultValue(returnType);
            }
        }
        else if(!returnType.isPrimitive() && !returnType.isEnum())
        {
            final Object result = tryToInstantiate(returnType);
            if(result != null)
            {
                return result;
            }
        }
        return Defaults.defaultValue(returnType);
    }


    /**
     * Returns a collection of all combinations of the given parameters. For example input:
     *
     * <pre>
     * {@code}[[A,B],[C,D,E],[F]]{@code}
     * </pre>
     *
     * the result will be: {@code}
     *
     * <pre>
     * [[A,C,F],[A,D,F],[A,E,F],[B,C,F],[B,D,F],[B,E,F]]
     * </pre>
     *
     * {@code}
     */
    public static List<Object[]> prepareAllCombinations(final Collection<? extends Collection<?>> possibleParams)
    {
        final Stack<Collection<?>> stack = new Stack<>();
        stack.addAll(possibleParams);
        final List<Object[]> result = new ArrayList<>();
        prepareAllPermutations(stack, 0, new Object[possibleParams.size()], result);
        return result;
    }


    private static void prepareAllPermutations(final Stack<Collection<?>> stack, final int pos, final Object[] args,
                    final List<Object[]> result)
    {
        if(stack.isEmpty())
        {
            result.add(args.clone());
        }
        else
        {
            final Collection<?> pop = stack.pop();
            for(final Object val : pop)
            {
                args[args.length - pos - 1] = val;
                prepareAllPermutations(stack, pos + 1, args, result);
            }
            stack.push(pop);
        }
    }


    /**
     * @param clazz
     *           class to check for com.hybris.cockpitng.testing.annotation.ExtensibleWidget annotation
     * @param modifier
     *           expected level of extensibility
     * @return true if the modifier available matches the required
     * @since 2.0.6
     */
    public static boolean isExtensible(final Class<?> clazz, final int modifier)
    {
        final ExtensibleWidget extensible = clazz.getAnnotation(ExtensibleWidget.class);
        return (extensible.value() && ((extensible.level() & modifier) == modifier));
    }


    /**
     * @param text
     *           to be converted to single line (characters
     * @return text converted to single line (removes characters like \r \n )
     */
    public static String toSingleLine(final String text)
    {
        return text.replaceAll("\\r|\\n", "");
    }
}
