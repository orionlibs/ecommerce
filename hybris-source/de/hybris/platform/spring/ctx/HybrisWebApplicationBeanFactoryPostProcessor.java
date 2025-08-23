package de.hybris.platform.spring.ctx;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class HybrisWebApplicationBeanFactoryPostProcessor implements BeanFactoryPostProcessor
{
    public static String PLATFORM_PROP_PLACEHOLDER = "platformPropertyPlaceHolder";


    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        adjustPropertyPlaceholder(beanFactory);
        beanFactory.addBeanPostProcessor((BeanPostProcessor)new RemoveDefaultSessionFixationStrategyBeanPostProcessor());
    }


    protected void adjustPropertyPlaceholder(ConfigurableListableBeanFactory beanFactory)
    {
        BeanFactoryPostProcessor resolver = (BeanFactoryPostProcessor)beanFactory.getBean(PLATFORM_PROP_PLACEHOLDER, PropertyPlaceholderConfigurer.class);
        resolver.postProcessBeanFactory(beanFactory);
    }
}
