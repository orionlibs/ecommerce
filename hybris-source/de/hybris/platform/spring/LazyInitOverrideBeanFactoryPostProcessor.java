package de.hybris.platform.spring;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class LazyInitOverrideBeanFactoryPostProcessor implements BeanFactoryPostProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(LazyInitOverrideBeanFactoryPostProcessor.class);
    private List<Class<?>> eagerlyLoadedClasses = Collections.emptyList();
    private boolean lazyInit = false;


    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        Predicate<BeanDefinition> isAbstract = BeanDefinition::isAbstract;
        LOG.info("Setting lazy-init='{}' for configuration beans", Boolean.valueOf(this.lazyInit));
        for(String beanName : beanFactory.getBeanDefinitionNames())
        {
            getMergedBeanDefinition(beanName, beanFactory)
                            .filter(isAbstract.negate())
                            .map(BeanDefinition::getBeanClassName)
                            .map(this::forName)
                            .filter(this::isSupportedClass)
                            .ifPresent(c -> overrideLazyInit(beanName, beanFactory));
        }
    }


    protected Optional<BeanDefinition> getMergedBeanDefinition(String beanName, ConfigurableListableBeanFactory beanFactory)
    {
        try
        {
            BeanDefinition bd = beanFactory.getMergedBeanDefinition(beanName);
            return Optional.ofNullable(bd);
        }
        catch(NoSuchBeanDefinitionException e)
        {
            return Optional.empty();
        }
    }


    protected Class<?> forName(String name)
    {
        try
        {
            return Class.forName(name);
        }
        catch(ClassNotFoundException e)
        {
            return null;
        }
    }


    protected boolean isSupportedClass(Class<?> clazz)
    {
        return this.eagerlyLoadedClasses.stream()
                        .filter(c -> c.isAssignableFrom(clazz))
                        .findAny().isPresent();
    }


    protected void overrideLazyInit(String beanName, ConfigurableListableBeanFactory beanFactory)
    {
        BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
        if(bd.isLazyInit() != this.lazyInit)
        {
            bd.setLazyInit(this.lazyInit);
            LOG.debug("Override lazyInit to {} for bean {}", Boolean.valueOf(this.lazyInit), beanName);
        }
    }


    public void setEagerlyLoadedClasses(List<Class<?>> eagerlyLoadedClasses)
    {
        this.eagerlyLoadedClasses = eagerlyLoadedClasses;
    }


    protected List<Class<?>> getEagerlyLoadedClasses()
    {
        return this.eagerlyLoadedClasses;
    }


    public void setLazyInit(boolean lazyInit)
    {
        this.lazyInit = lazyInit;
    }


    protected boolean getLazyInit()
    {
        return this.lazyInit;
    }
}
