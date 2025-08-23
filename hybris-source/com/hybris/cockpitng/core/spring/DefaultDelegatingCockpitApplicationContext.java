/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.spring;

import com.hybris.cockpitng.core.CockpitApplicationException;
import com.hybris.cockpitng.core.modules.ModuleInfo;
import com.hybris.cockpitng.core.persistence.packaging.CockpitClassLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.util.Resettable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

/**
 * A fallback {@link CockpitApplicationContext} that delegates all calls to methods known to specified standard
 * {@link ApplicationContext} and tries to figure out the result of call to methods unknown to standard one.
 */
public class DefaultDelegatingCockpitApplicationContext implements CockpitApplicationContext
{
    private static final Map<ApplicationContext, DefaultDelegatingCockpitApplicationContext> CONTEXTS = new ConcurrentHashMap<>();
    private File dataRoot;
    private CockpitClassLoader classLoader;
    private final ApplicationContext applicationContext;


    private DefaultDelegatingCockpitApplicationContext(final ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    /**
     * Wraps provided application context with delegating one if needed.
     *
     * @param applicationContext
     *           application context to be wrapped
     * @return wrapped application context
     */
    public static DefaultDelegatingCockpitApplicationContext getInstance(final ApplicationContext applicationContext)
    {
        if(applicationContext instanceof DefaultDelegatingCockpitApplicationContext)
        {
            return (DefaultDelegatingCockpitApplicationContext)applicationContext;
        }
        else
        {
            return CONTEXTS.computeIfAbsent(applicationContext, DefaultDelegatingCockpitApplicationContext::new);
        }
    }


    @Override
    public String getId()
    {
        return applicationContext.getId();
    }


    @Override
    public String getApplicationName()
    {
        return applicationContext.getApplicationName();
    }


    @Override
    public String getDisplayName()
    {
        return applicationContext.getDisplayName();
    }


    @Override
    public long getStartupDate()
    {
        return applicationContext.getStartupDate();
    }


    @Override
    public ApplicationContext getParent()
    {
        return applicationContext.getParent();
    }


    @Override
    public AutowireCapableBeanFactory getAutowireCapableBeanFactory()
    {
        return applicationContext.getAutowireCapableBeanFactory();
    }


    @Override
    public Environment getEnvironment()
    {
        return applicationContext.getEnvironment();
    }


    @Override
    public boolean containsBeanDefinition(final String s)
    {
        return applicationContext.containsBeanDefinition(s);
    }


    @Override
    public int getBeanDefinitionCount()
    {
        return applicationContext.getBeanDefinitionCount();
    }


    @Override
    public String[] getBeanDefinitionNames()
    {
        return applicationContext.getBeanDefinitionNames();
    }


    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> aClass, boolean b)
    {
        return null;
    }


    @Override
    public <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType, boolean b)
    {
        return null;
    }


    @Override
    public String[] getBeanNamesForType(final ResolvableType resolvableType)
    {
        return applicationContext.getBeanNamesForType(resolvableType);
    }


    @Override
    public String[] getBeanNamesForType(final ResolvableType type, final boolean includeNonSingletons, final boolean allowEagerInit)
    {
        return applicationContext.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }


    @Override
    public String[] getBeanNamesForType(final Class<?> aClass)
    {
        return applicationContext.getBeanNamesForType(aClass);
    }


    @Override
    public String[] getBeanNamesForType(final Class<?> aClass, final boolean b, final boolean b1)
    {
        return applicationContext.getBeanNamesForType(aClass, b, b1);
    }


    @Override
    public <T> Map<String, T> getBeansOfType(final Class<T> aClass)
    {
        return applicationContext.getBeansOfType(aClass);
    }


    @Override
    public <T> Map<String, T> getBeansOfType(final Class<T> aClass, final boolean b, final boolean b1)
    {
        return applicationContext.getBeansOfType(aClass, b, b1);
    }


    @Override
    public String[] getBeanNamesForAnnotation(final Class<? extends Annotation> aClass)
    {
        return applicationContext.getBeanNamesForAnnotation(aClass);
    }


    @Override
    public Map<String, Object> getBeansWithAnnotation(final Class<? extends Annotation> aClass)
    {
        return applicationContext.getBeansWithAnnotation(aClass);
    }


    @Override
    public <A extends Annotation> A findAnnotationOnBean(final String s, final Class<A> aClass)
                    throws NoSuchBeanDefinitionException
    {
        return applicationContext.findAnnotationOnBean(s, aClass);
    }


    @Override
    public <A extends Annotation> A findAnnotationOnBean(final String s, final Class<A> aClass, boolean b)
                    throws NoSuchBeanDefinitionException
    {
        return applicationContext.findAnnotationOnBean(s, aClass, b);
    }


    @Override
    public Object getBean(final String s)
    {
        return applicationContext.getBean(s);
    }


    @Override
    public <T> T getBean(final String s, final Class<T> aClass)
    {
        return applicationContext.getBean(s, aClass);
    }


    @Override
    public Object getBean(final String s, final Object... objects)
    {
        return applicationContext.getBean(s, objects);
    }


    @Override
    public <T> T getBean(final Class<T> aClass)
    {
        return applicationContext.getBean(aClass);
    }


    @Override
    public <T> T getBean(final Class<T> aClass, final Object... objects)
    {
        return applicationContext.getBean(aClass, objects);
    }


    @Override
    public <T> ObjectProvider<T> getBeanProvider(final Class<T> aClass)
    {
        return applicationContext.getBeanProvider(aClass);
    }


    @Override
    public <T> ObjectProvider<T> getBeanProvider(final ResolvableType resolvableType)
    {
        return applicationContext.getBeanProvider(resolvableType);
    }


    @Override
    public boolean containsBean(final String s)
    {
        return applicationContext.containsBean(s);
    }


    @Override
    public boolean isSingleton(final String s)
    {
        return applicationContext.isSingleton(s);
    }


    @Override
    public boolean isPrototype(final String s)
    {
        return applicationContext.isPrototype(s);
    }


    @Override
    public boolean isTypeMatch(final String s, final ResolvableType resolvableType)
    {
        return applicationContext.isTypeMatch(s, resolvableType);
    }


    @Override
    public boolean isTypeMatch(final String s, final Class<?> aClass)
    {
        return applicationContext.isTypeMatch(s, aClass);
    }


    @Override
    public Class<?> getType(final String s)
    {
        return applicationContext.getType(s);
    }


    @Override
    public Class<?> getType(final String name, final boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException
    {
        return applicationContext.getType(name, allowFactoryBeanInit);
    }


    @Override
    public String[] getAliases(final String s)
    {
        return applicationContext.getAliases(s);
    }


    @Override
    public BeanFactory getParentBeanFactory()
    {
        return applicationContext.getParentBeanFactory();
    }


    @Override
    public boolean containsLocalBean(final String s)
    {
        return applicationContext.containsLocalBean(s);
    }


    @Override
    public String getMessage(final String s, final Object[] objects, final String s1, final Locale locale)
    {
        return applicationContext.getMessage(s, objects, s1, locale);
    }


    @Override
    public String getMessage(final String s, final Object[] objects, final Locale locale)
    {
        return applicationContext.getMessage(s, objects, locale);
    }


    @Override
    public String getMessage(final MessageSourceResolvable messageSourceResolvable, final Locale locale)
    {
        return applicationContext.getMessage(messageSourceResolvable, locale);
    }


    @Override
    public void publishEvent(final ApplicationEvent applicationEvent)
    {
        applicationContext.publishEvent(applicationEvent);
    }


    @Override
    public void publishEvent(final Object o)
    {
        applicationContext.publishEvent(o);
    }


    @Override
    public Resource[] getResources(final String s) throws IOException
    {
        return applicationContext.getResources(s);
    }


    @Override
    public Resource getResource(final String s)
    {
        return applicationContext.getResource(s);
    }


    @Override
    public CockpitClassLoader getClassLoader()
    {
        if(applicationContext instanceof CockpitApplicationContext)
        {
            return ((CockpitApplicationContext)applicationContext).getClassLoader();
        }
        else
        {
            if(classLoader == null && applicationContext != null)
            {
                classLoader = new DelegatingCockpitClassLoader(applicationContext.getClassLoader());
            }
            else if(classLoader == null)
            {
                classLoader = new DelegatingCockpitClassLoader(Thread.currentThread().getContextClassLoader());
            }
        }
        return classLoader;
    }


    @Override
    public void refresh() throws BeansException
    {
        if(applicationContext instanceof AbstractApplicationContext)
        {
            ((AbstractApplicationContext)applicationContext).refresh();
        }
    }


    @Override
    public File getDataRootDir()
    {
        if(dataRoot == null)
        {
            dataRoot = new File(FileUtils.getTempDirectory(), UUID.randomUUID().toString());
            dataRoot.mkdirs();
        }
        return dataRoot;
    }


    @Override
    public List<String> getLoadedModulesNames()
    {
        return (applicationContext instanceof CockpitApplicationContext)
                        ? ((CockpitApplicationContext)applicationContext).getLoadedModulesNames()
                        : Collections.emptyList();
    }


    @Override
    public Optional<String> getModuleName(final URI moduleURI)
    {
        return (applicationContext instanceof CockpitApplicationContext)
                        ? ((CockpitApplicationContext)applicationContext).getModuleName(moduleURI)
                        : Optional.empty();
    }


    @Override
    public Optional<URI> getModuleURI(final String moduleName)
    {
        return (applicationContext instanceof CockpitApplicationContext)
                        ? ((CockpitApplicationContext)applicationContext).getModuleURI(moduleName)
                        : Optional.empty();
    }


    @Override
    public Optional<ModuleInfo> getModuleInfo(final String moduleName)
    {
        return (applicationContext instanceof CockpitApplicationContext)
                        ? ((CockpitApplicationContext)applicationContext).getModuleInfo(moduleName)
                        : Optional.empty();
    }


    @Override
    public void registerNewModule(final String moduleName, final ModuleContentProvider contentsProvider)
                    throws CockpitApplicationException
    {
        if(applicationContext instanceof CockpitApplicationContext)
        {
            ((CockpitApplicationContext)applicationContext).registerNewModule(moduleName, contentsProvider);
        }
    }


    @Override
    public void unregisterModule(final String moduleName) throws CockpitApplicationException
    {
        if(applicationContext instanceof CockpitApplicationContext)
        {
            ((CockpitApplicationContext)applicationContext).unregisterModule(moduleName);
        }
    }


    @Override
    public boolean isReady()
    {
        if(applicationContext instanceof CockpitApplicationContext)
        {
            return ((CockpitApplicationContext)applicationContext).isReady();
        }
        return true;
    }


    private static class DelegatingCockpitClassLoader extends CockpitClassLoader
    {
        private static final Logger LOGGER = LoggerFactory.getLogger(DelegatingCockpitClassLoader.class);


        public DelegatingCockpitClassLoader(final ClassLoader parent)
        {
            super(parent);
        }


        @Override
        public void reset()
        {
            if(super.getParent() instanceof Resettable)
            {
                ((Resettable)super.getParent()).reset();
            }
        }


        @Override
        public InputStream getResourceAsStream(final WidgetJarLibInfo componentInfo, final String resourceName)
        {
            String resource = resourceName;
            if(StringUtils.isNotBlank(componentInfo.getPrefix()))
            {
                if(!componentInfo.getPrefix().endsWith("/"))
                {
                    resource = "/" + resource;
                }
                resource = componentInfo.getPrefix() + resource;
            }
            if(componentInfo.getJarPath() != null)
            {
                try(final JarFile jarFile = new JarFile(componentInfo.getJarPath()))
                {
                    final JarEntry entry = jarFile.getJarEntry(resource);
                    if(entry != null)
                    {
                        return jarFile.getInputStream(entry);
                    }
                }
                catch(final IOException ex)
                {
                    LOGGER.error(ex.getLocalizedMessage(), ex);
                }
            }
            else
            {
                return getResourceAsStream(resource);
            }
            return null;
        }
    }
}
