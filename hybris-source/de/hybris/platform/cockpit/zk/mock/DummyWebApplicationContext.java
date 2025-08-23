package de.hybris.platform.cockpit.zk.mock;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;

public class DummyWebApplicationContext implements WebApplicationContext
{
    private final ApplicationContext applicationContext;
    private ServletContext servletCtx;


    public DummyWebApplicationContext(ApplicationContext applicationContext, ServletContext servlet)
    {
        this.applicationContext = applicationContext;
    }


    public ServletContext getServletContext()
    {
        return this.servletCtx;
    }


    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException
    {
        return this.applicationContext.getAutowireCapableBeanFactory();
    }


    public String getDisplayName()
    {
        return this.applicationContext.getDisplayName();
    }


    public String getApplicationName()
    {
        return this.applicationContext.getApplicationName();
    }


    public String getId()
    {
        return this.applicationContext.getId();
    }


    public ApplicationContext getParent()
    {
        return this.applicationContext.getParent();
    }


    public long getStartupDate()
    {
        return this.applicationContext.getStartupDate();
    }


    public boolean containsBeanDefinition(String beanName)
    {
        return this.applicationContext.containsBeanDefinition(beanName);
    }


    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
    {
        return (A)this.applicationContext.findAnnotationOnBean(beanName, annotationType);
    }


    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType, boolean allowFactoryBeanInit)
    {
        return (A)this.applicationContext.findAnnotationOnBean(beanName, annotationType, allowFactoryBeanInit);
    }


    public int getBeanDefinitionCount()
    {
        return this.applicationContext.getBeanDefinitionCount();
    }


    public String[] getBeanDefinitionNames()
    {
        return this.applicationContext.getBeanDefinitionNames();
    }


    public <T> ObjectProvider<T> getBeanProvider(Class<T> aClass, boolean b)
    {
        return null;
    }


    public <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType, boolean b)
    {
        return null;
    }


    public String[] getBeanNamesForType(ResolvableType resolvableType)
    {
        return this.applicationContext.getBeanNamesForType(resolvableType);
    }


    public String[] getBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit)
    {
        return this.applicationContext.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }


    public String[] getBeanNamesForType(Class type)
    {
        return this.applicationContext.getBeanNamesForType(type);
    }


    public String[] getBeanNamesForType(Class type, boolean includeNonSingletons, boolean allowEagerInit)
    {
        return this.applicationContext.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }


    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException
    {
        return this.applicationContext.getBeansOfType(type);
    }


    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException
    {
        return this.applicationContext.getBeansOfType(type, includeNonSingletons, allowEagerInit);
    }


    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException
    {
        return this.applicationContext.getBeansWithAnnotation(annotationType);
    }


    public boolean containsBean(String name)
    {
        return this.applicationContext.containsBean(name);
    }


    public String[] getAliases(String name)
    {
        return this.applicationContext.getAliases(name);
    }


    public Object getBean(String name) throws BeansException
    {
        return this.applicationContext.getBean(name);
    }


    public <T> T getBean(Class<T> requiredType) throws BeansException
    {
        return (T)this.applicationContext.getBean(requiredType);
    }


    public <T> T getBean(String name, Class<T> requiredType) throws BeansException
    {
        return (T)this.applicationContext.getBean(name, requiredType);
    }


    public Object getBean(String name, Object... args) throws BeansException
    {
        return this.applicationContext.getBean(name, args);
    }


    public Class<?> getType(String name) throws NoSuchBeanDefinitionException
    {
        return this.applicationContext.getType(name);
    }


    public Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException
    {
        return this.applicationContext.getType(name, allowFactoryBeanInit);
    }


    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException
    {
        return this.applicationContext.isPrototype(name);
    }


    public boolean isTypeMatch(String s, ResolvableType resolvableType) throws NoSuchBeanDefinitionException
    {
        return this.applicationContext.isTypeMatch(s, resolvableType);
    }


    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException
    {
        return this.applicationContext.isSingleton(name);
    }


    public boolean isTypeMatch(String name, Class targetType) throws NoSuchBeanDefinitionException
    {
        return this.applicationContext.isTypeMatch(name, targetType);
    }


    public boolean containsLocalBean(String name)
    {
        return this.applicationContext.containsLocalBean(name);
    }


    public BeanFactory getParentBeanFactory()
    {
        return this.applicationContext.getParentBeanFactory();
    }


    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException
    {
        return this.applicationContext.getMessage(resolvable, locale);
    }


    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException
    {
        return this.applicationContext.getMessage(code, args, locale);
    }


    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale)
    {
        return this.applicationContext.getMessage(code, args, defaultMessage, locale);
    }


    public void publishEvent(ApplicationEvent event)
    {
        this.applicationContext.publishEvent(event);
    }


    public void publishEvent(Object o)
    {
        this.applicationContext.publishEvent(o);
    }


    public Resource[] getResources(String locationPattern) throws IOException
    {
        return this.applicationContext.getResources(locationPattern);
    }


    public ClassLoader getClassLoader()
    {
        return this.applicationContext.getClassLoader();
    }


    public Resource getResource(String location)
    {
        return this.applicationContext.getResource(location);
    }


    public Environment getEnvironment()
    {
        return this.applicationContext.getEnvironment();
    }


    public <T> T getBean(Class<T> tClass, Object... objects) throws BeansException
    {
        return (T)this.applicationContext.getBean(tClass, objects);
    }


    public <T> ObjectProvider<T> getBeanProvider(Class<T> aClass)
    {
        return this.applicationContext.getBeanProvider(aClass);
    }


    public <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType)
    {
        return this.applicationContext.getBeanProvider(resolvableType);
    }


    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> aClass)
    {
        return this.applicationContext.getBeanNamesForAnnotation(aClass);
    }
}
