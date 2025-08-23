package com.hybris.backoffice.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;

public abstract class AbstractMultipleBeanParser extends AbstractBeanDefinitionParser
{
    protected void registerBeanDefinition(String beanName, BeanDefinition beanDefinition, ParserContext parserContext)
    {
        parserContext.getRegistry().registerBeanDefinition(beanName, beanDefinition);
        if(shouldFireEvents())
        {
            BeanComponentDefinition componentDefinition = new BeanComponentDefinition(beanDefinition, beanName);
            postProcessComponentDefinition(componentDefinition);
            parserContext.registerComponent((ComponentDefinition)componentDefinition);
        }
    }
}
