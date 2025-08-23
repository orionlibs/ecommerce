package com.hybris.backoffice.solrsearch.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BackofficeIndexedPropertyPopulatorLoader implements BeanPostProcessor, ApplicationContextAware, BeanFactoryAware
{
    static final String INDEXED_PROPERTY_CONVERTER_ALIAS = "indexedPropertyConverter";
    static final String BACKOFFICE_INDEXED_PROPERTY_POPULATOR_BEAN_NAME = "defaultBackofficeIndexedPropertyPopulator";
    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        if(bean instanceof AbstractPopulatingConverter && isBeanAliasesByIndexedPropertyConverter(beanName))
        {
            BackofficeIndexedPropertyPopulator populator = (BackofficeIndexedPropertyPopulator)this.applicationContext.getBean("defaultBackofficeIndexedPropertyPopulator");
            AbstractPopulatingConverter populatingConverter = (AbstractPopulatingConverter)bean;
            List<Populator> newList = new ArrayList<>(populatingConverter.getPopulators());
            newList.add(populator);
            populatingConverter.setPopulators(newList);
        }
        return bean;
    }


    private boolean isBeanAliasesByIndexedPropertyConverter(String beanName)
    {
        return Arrays.<String>asList(this.beanFactory.getAliases(beanName)).contains("indexedPropertyConverter");
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}
