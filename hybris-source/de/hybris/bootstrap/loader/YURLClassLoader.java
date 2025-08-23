package de.hybris.bootstrap.loader;

import de.hybris.bootstrap.loader.metrics.ClassLoaderMetricEvent;
import de.hybris.bootstrap.loader.metrics.EventType;
import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class YURLClassLoader extends URLClassLoader
{
    private final boolean logClassloading;
    private final Set<Class<?>> loadedClassesByThisOrChild;

    static
    {
        registerAsParallelCapable();
    }

    public YURLClassLoader(URL[] urls)
    {
        super(urls);
        this.loadedClassesByThisOrChild = new HashSet<>();
        this
                        .logClassloading = Boolean.valueOf(System.getProperty("hybris.log.classloading", Boolean.FALSE.toString())).booleanValue();
    }


    public YURLClassLoader(URL[] urls, ClassLoader parent)
    {
        super(urls, parent);
        this.loadedClassesByThisOrChild = new HashSet<>();
        this
                        .logClassloading = Boolean.valueOf(System.getProperty("hybris.log.classloading", Boolean.FALSE.toString())).booleanValue();
    }


    protected Class<?> findClass(String name) throws ClassNotFoundException
    {
        try
        {
            Class<?> aClass = super.findClass(name);
            publishClassFoundEvent(aClass);
            return aClass;
        }
        catch(ClassNotFoundException e)
        {
            publishClassNotFoundEvent(name);
            throw e;
        }
    }


    private void publishClassFoundEvent(Class<?> aClass)
    {
        if(skipClassLoaderEvents())
        {
            return;
        }
        ClassLoaderMetricEvent classFoundEvent = ClassLoaderMetricEvent.forClass(aClass).ofEventType(EventType.FOUND_ON_CLASSPATH).build();
        PlatformInPlaceClassLoader.getClassloaderMetrics().onEvent(classFoundEvent);
    }


    private void publishClassNotFoundEvent(String name)
    {
        if(skipClassLoaderEvents())
        {
            return;
        }
        ClassLoaderMetricEvent classNotFound = ClassLoaderMetricEvent.forClass().ofEventType(EventType.NOT_FOUND_ON_CLASSPATH).withName(name).build();
        PlatformInPlaceClassLoader.getClassloaderMetrics().onEvent(classNotFound);
    }


    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        synchronized(getClassLoadingLock(name))
        {
            Class<?> c = findLoadedClass(name);
            if(c == null)
            {
                c = loadRegisterAndResolveClass(name, resolve);
            }
            else if(resolve)
            {
                resolveClass(c);
            }
            return c;
        }
    }


    private Class<?> loadRegisterAndResolveClass(String name, boolean resolve) throws ClassNotFoundException
    {
        Class<?> c = super.loadClass(name, resolve);
        registerClass(name, c);
        return c;
    }


    private void registerClass(String name, Class<?> cl)
    {
        if(cl != null && loadedByThisOrChild(cl))
        {
            this.loadedClassesByThisOrChild.add(cl);
        }
    }


    private boolean loadedByThisOrChild(Class<?> clazz)
    {
        for(ClassLoader classLoader = clazz.getClassLoader(); null != classLoader; classLoader = classLoader.getParent())
        {
            if(classLoader.equals(this))
            {
                return true;
            }
        }
        return false;
    }


    public URL findResource(String name)
    {
        try
        {
            URL resource = super.findResource(name);
            if(resource != null)
            {
                publishResourceFoundEvent(name, resource);
            }
            else
            {
                publishResourceNotFoundEvent(name);
            }
            return resource;
        }
        catch(Exception e)
        {
            publishResourceNotFoundEvent(name);
            throw e;
        }
    }


    private void publishResourceFoundEvent(String name, URL resource)
    {
        if(skipClassLoaderEvents())
        {
            return;
        }
        ClassLoaderMetricEvent resourceFoundEvent = ClassLoaderMetricEvent.forResource().ofEventType(EventType.FOUND_ON_CLASSPATH).withName(name).withSource(resource.getFile()).build();
        PlatformInPlaceClassLoader.getClassloaderMetrics().onEvent(resourceFoundEvent);
    }


    private void publishResourceNotFoundEvent(String name)
    {
        if(skipClassLoaderEvents())
        {
            return;
        }
        ClassLoaderMetricEvent resourceNotFoundEvent = ClassLoaderMetricEvent.forResource().withName(name).ofEventType(EventType.NOT_FOUND_ON_CLASSPATH).build();
        PlatformInPlaceClassLoader.getClassloaderMetrics().onEvent(resourceNotFoundEvent);
    }


    private boolean skipClassLoaderEvents()
    {
        return (!this.logClassloading || PlatformInPlaceClassLoader.getClassloaderMetrics() == null);
    }


    public void start()
    {
        try
        {
            Class<?> cl = Class.forName("de.hybris.platform.util.RedeployUtilities", true, this);
            Method m = cl.getMethod("noteAboutStartup", new Class[0]);
            m.invoke(null, new Object[0]);
        }
        catch(Exception e)
        {
            System.out.println("internal classloader could not be notified about startup, contact support.");
        }
    }


    public void stop()
    {
        try
        {
            Class<?> cl = Class.forName("de.hybris.platform.util.RedeployUtilities", true, this);
            Method m = cl.getMethod("shutdown", new Class[0]);
            m.invoke(null, new Object[0]);
        }
        catch(ClassNotFoundException e)
        {
            System.out.println("Warning: could not shut down classloader. Maybe it was not yet initialized?");
        }
        catch(NoClassDefFoundError e)
        {
            System.out.println("Warning: could not shut down classloader. Maybe it was not yet initialized?");
        }
        catch(Exception e)
        {
            System.out.println("The following exception occured while shutting down classloader:");
            if(e instanceof java.lang.reflect.InvocationTargetException)
            {
                e.getCause().printStackTrace();
            }
            else
            {
                e.printStackTrace();
            }
        }
    }


    public synchronized void clearAllReferences()
    {
        unregisterJDBCDrivers();
        processRegisteredClasses();
        clearIntrospectionUtilsCacheOnTomcat();
        clearClassloaderBeanIntrospectorReferences();
    }


    private void clearClassloaderBeanIntrospectorReferences()
    {
        Introspector.flushCaches();
    }


    private void clearIntrospectionUtilsCacheOnTomcat()
    {
        try
        {
            Method m = Class.forName("org.apache.tomcat.util.IntrospectionUtils").getMethod("clear", (Class[])null);
            m.invoke(null, (Object[])null);
        }
        catch(ClassNotFoundException classNotFoundException)
        {
        }
        catch(Exception e)
        {
            System.err.println("error clearing tomcat introspection cache: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void processRegisteredClasses()
    {
        for(Class<?> clazz : this.loadedClassesByThisOrChild)
        {
            for(Field field : clazz.getDeclaredFields())
            {
                processField(field, clazz);
            }
        }
        this.loadedClassesByThisOrChild.clear();
    }


    private void processField(Field field, Class<?> clazz)
    {
        int mods = field.getModifiers();
        if(isProcessableField(field, mods))
        {
            try
            {
                field.setAccessible(true);
                if(Modifier.isFinal(mods))
                {
                    if(isProcessableFinalField(field))
                    {
                        nullInstance(field.get(null), clazz);
                    }
                }
                else
                {
                    field.set(null, null);
                }
            }
            catch(Exception e)
            {
                System.err.println("error processing field " + field + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private boolean isProcessableFinalField(Field field)
    {
        String typeName = field.getType().getName();
        return (!typeName.startsWith("java.") && !typeName.startsWith("javax."));
    }


    private boolean isProcessableField(Field field, int modifiers)
    {
        return (Modifier.isStatic(modifiers) && !field.getType().isPrimitive() && field.getName().indexOf("$") == -1);
    }


    private void unregisterJDBCDrivers()
    {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while(drivers.hasMoreElements())
        {
            Driver driver = drivers.nextElement();
            if(equals(driver.getClass().getClassLoader()))
            {
                try
                {
                    DriverManager.deregisterDriver(driver);
                }
                catch(SQLException e)
                {
                    System.out.println("SQL driver deregistration failed");
                    e.printStackTrace();
                }
            }
        }
    }


    private void nullInstance(Object instance, Class<?> usedClass)
    {
        if(instance == null)
        {
            return;
        }
        Field[] fields = instance.getClass().getDeclaredFields();
        for(int i = 0; i < fields.length; i++)
        {
            Field field = fields[i];
            int mods = field.getModifiers();
            if(!field.getType().isPrimitive() && field.getName().indexOf("$") == -1)
            {
                try
                {
                    field.setAccessible(true);
                    if(!Modifier.isStatic(mods) || !Modifier.isFinal(mods))
                    {
                        Object value = field.get(instance);
                        if(null != value)
                        {
                            Class<?> valueClass = value.getClass();
                            if(equals(valueClass.getClassLoader()))
                            {
                                field.set(instance, null);
                            }
                        }
                    }
                }
                catch(Exception exception)
                {
                }
            }
        }
    }


    protected void addClassPathURLs(List<URL> urls, String classpath) throws MalformedURLException
    {
        PathTokenizer st = new PathTokenizer(classpath);
        while(st.hasMoreTokens())
        {
            String s = st.nextToken();
            if(s.endsWith("*.jar"))
            {
                s = s.substring(0, s.length() - 5);
                File[] files = (new File(s)).listFiles();
                if(files != null && files.length > 0)
                {
                    for(File lib : Arrays.<File>asList(files))
                    {
                        urls.add(buildURL(lib, TYPE.FILE));
                    }
                }
                continue;
            }
            File f = new File(s);
            urls.add(buildURL(f, f.isDirectory() ? TYPE.DIR : TYPE.FILE));
        }
    }


    protected URL buildURL(File f, TYPE type) throws MalformedURLException
    {
        return f.toURI().toURL();
    }
}
