/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Defines parsers for possible elements in CockpitNG Spring namespace
 */
public class CockpitNamespaceHandler extends NamespaceHandlerSupport
{
    public static final String NAMESPACE = "http://www.hybris.com/cockpitng/spring";


    @Override
    public void init()
    {
        registerBeanDefinitionParser(PropertyBeanExtenderParser.ELEMENT_NAME, new PropertyBeanExtenderParser());
        registerBeanDefinitionParser(CollectionBeanExtenderParser.ELEMENT_NAME, new CollectionBeanExtenderParser());
        registerBeanDefinitionParser(ListBeanExtenderParser.ELEMENT_NAME, new ListBeanExtenderParser());
        registerBeanDefinitionParser(MapBeanExtenderParser.ELEMENT_NAME, new MapBeanExtenderParser());
    }
}
