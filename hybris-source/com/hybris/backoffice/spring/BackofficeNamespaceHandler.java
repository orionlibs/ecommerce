package com.hybris.backoffice.spring;

import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class BackofficeNamespaceHandler extends NamespaceHandlerSupport
{
    public void init()
    {
        registerBeanDefinitionParser("import-modules", (BeanDefinitionParser)new BeansDefinitionImportParser());
    }
}
