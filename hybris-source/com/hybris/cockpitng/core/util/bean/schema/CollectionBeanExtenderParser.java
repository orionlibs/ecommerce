/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean.schema;

import com.hybris.cockpitng.core.util.bean.CollectionBeanExtender;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Handles parsing collection extenders in CockpitNG Spring namespace.
 * <p>
 * Namespace tag: <strong><code>collection-extender</code></strong>
 * </p>
 *
 * @see CollectionBeanExtender
 */
public class CollectionBeanExtenderParser extends AbstractBeanExtenderParser
{
    protected static final String ELEMENT_NAME = "collection-extender";
    private static final String ATTRIBUTE_UNIQUE = "unique";
    private static final String ELEMENT_ADD = "add";
    private static final String ELEMENT_REMOVE = "remove";
    private static final String FIELD_ADD = "add";
    private static final String FIELD_REMOVE = "remove";
    private static final String FIELD_UNIQUE = "unique";


    @Override
    protected Class<?> getBeanClass(final Element element)
    {
        return CollectionBeanExtender.class;
    }


    @Override
    protected String getParentName(final Element element)
    {
        final String parentName = super.getParentName(element);
        return parentName == null ? CollectionBeanExtender.BEAN_NAME : parentName;
    }


    @Override
    protected void doParse(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder)
    {
        super.doParse(element, parserContext, builder);
        BeanParserUtils.getAttributeValue(element, ATTRIBUTE_UNIQUE)
                        .ifPresent((unique) -> builder.addPropertyValue(FIELD_UNIQUE, Boolean.valueOf(Boolean.parseBoolean(unique))));
        parseAdd(element, parserContext, builder);
        parseRemove(element, parserContext, builder);
    }


    protected void parseAdd(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder)
    {
        parseList(element, parserContext, builder, ELEMENT_ADD, FIELD_ADD);
    }


    protected void parseRemove(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder)
    {
        parseList(element, parserContext, builder, ELEMENT_REMOVE, FIELD_REMOVE);
    }


    protected void parseList(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder,
                    final String elementName, final String fieldName)
    {
        BeanParserUtils.mapAndConsume(BeanParserUtils.getFirstChild(element, elementName),
                        (remove) -> parserContext.getDelegate().parseListElement(remove, builder.getBeanDefinition()),
                        (remove) -> builder.addPropertyValue(fieldName, remove));
    }
}
