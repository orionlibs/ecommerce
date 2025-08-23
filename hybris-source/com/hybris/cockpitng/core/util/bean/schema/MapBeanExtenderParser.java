/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean.schema;

import com.hybris.cockpitng.core.util.bean.MapBeanExtender;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Handles parsing map extenders in CockpitNG Spring namespace.
 * <p>Namespace tag: <strong><code>map-extender</code></strong></p>
 *
 * @see MapBeanExtender
 */
public class MapBeanExtenderParser extends AbstractBeanExtenderParser
{
    protected static final String ELEMENT_NAME = "map-extender";
    private static final String ELEMENT_PUT = "put";
    private static final String ELEMENT_REMOVE = "remove";
    private static final String FIELD_PUT = "put";
    private static final String FIELD_REMOVE = "remove";


    @Override
    protected Class<?> getBeanClass(final Element element)
    {
        return MapBeanExtender.class;
    }


    @Override
    protected String getParentName(final Element element)
    {
        final String parentName = super.getParentName(element);
        return parentName == null ? MapBeanExtender.BEAN_NAME : parentName;
    }


    @Override
    protected void doParse(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder)
    {
        super.doParse(element, parserContext, builder);
        BeanParserUtils.mapAndConsume(
                        BeanParserUtils.getFirstChild(element, ELEMENT_PUT),
                        (put) -> parserContext.getDelegate().parseMapElement(put, builder.getBeanDefinition()),
                        (put) -> builder.addPropertyValue(FIELD_PUT, put));
        BeanParserUtils.mapAndConsume(
                        BeanParserUtils.getFirstChild(element, ELEMENT_REMOVE),
                        (remove) -> parserContext.getDelegate().parseListElement(remove, builder.getBeanDefinition()),
                        (remove) -> builder.addPropertyValue(FIELD_REMOVE, remove));
    }
}
