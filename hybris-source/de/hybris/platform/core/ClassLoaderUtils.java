package de.hybris.platform.core;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ClassLoaderUtils
{
    private static final String WEB_CLASSLOADER_NAME = "de.hybris.tomcat.HybrisWebappLoader$HybrisWebappClassLoader";


    public static <T> T executeWithWebClassLoaderParentIfNeeded(Supplier<T> supplier)
    {
        AtomicReference<T> result = new AtomicReference<>();
        executeWithWebClassLoaderParentIfNeeded(() -> result.set(supplier.get()));
        return result.get();
    }


    public static void executeWithWebClassLoaderParentIfNeeded(Runnable runnable)
    {
        ClassLoader previousClassLoader = null;
        try
        {
            ClassLoader currentClassloader = Thread.currentThread().getContextClassLoader();
            ClassLoader webClassLoader = findClassLoaderInHierarchy(currentClassloader, "de.hybris.tomcat.HybrisWebappLoader$HybrisWebappClassLoader");
            if(webClassLoader != null)
            {
                previousClassLoader = currentClassloader;
                Thread.currentThread().setContextClassLoader(webClassLoader.getParent());
            }
            runnable.run();
        }
        finally
        {
            if(previousClassLoader != null)
            {
                Thread.currentThread().setContextClassLoader(previousClassLoader);
            }
        }
    }


    public static boolean containsHybrisWebClassLoaderInHierarchy(ClassLoader classLoader)
    {
        return (findClassLoaderInHierarchy(classLoader, "de.hybris.tomcat.HybrisWebappLoader$HybrisWebappClassLoader") != null);
    }


    public static ClassLoader findClassLoaderInHierarchy(ClassLoader classLoader, String targetClassLoaderName)
    {
        while(classLoaderNameDoesNotMatch(classLoader, targetClassLoaderName))
        {
            classLoader = classLoader.getParent();
        }
        return classLoader;
    }


    private static boolean classLoaderNameDoesNotMatch(ClassLoader classLoader, String target)
    {
        return (classLoader != null && !classLoader.getClass().getName().equals(target));
    }
}
