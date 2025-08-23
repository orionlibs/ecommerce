/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean.schema;

import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.BEAN_REF_ATTRIBUTE;

import com.hybris.cockpitng.core.util.bean.ListBeanExtender;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Handles parsing list extenders in CockpitNG Spring namespace.
 * <p>
 * Namespace tag: <strong><code>list-extender</code></strong>
 * </p>
 *
 * @see ListBeanExtender
 */
public class ListBeanExtenderParser extends CollectionBeanExtenderParser
{
    protected static final String ELEMENT_NAME = "list-extender";
    private static final String ATTRIBUTE_SORT = "sort";
    private static final String ATTRIBUTE_COMPARATOR = "comparator";
    private static final String FIELD_SORT = "sort";
    private static final String FIELD_COMPARATOR = "comparator";
    private static final String ELEMENT_COMPARATOR = "comparator";


    @Override
    protected String getParentName(final Element element)
    {
        final String parentName = super.getParentName(element);
        return parentName == null ? ListBeanExtender.BEAN_NAME : parentName;
    }


    @Override
    protected Class<?> getBeanClass(final Element element)
    {
        return ListBeanExtender.class;
    }


    @Override
    protected void doParse(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder)
    {
        super.doParse(element, parserContext, builder);
        BeanParserUtils.getAttributeValue(element, ATTRIBUTE_SORT).ifPresent((sort) -> builder.addPropertyValue(FIELD_SORT, sort));
        BeanParserUtils.getAttributeValue(element, ATTRIBUTE_COMPARATOR)
                        .ifPresent((comparator) -> builder.addPropertyReference(FIELD_COMPARATOR, comparator));
        BeanParserUtils.getFirstChild(element, ELEMENT_COMPARATOR)
                        .ifPresent((comparator) -> parseComparator(comparator, parserContext, builder));
    }


    protected void parseComparator(final Element comparatorElement, final ParserContext parserContext,
                    final BeanDefinitionBuilder builder)
    {
        BeanParserUtils.getChildren(comparatorElement).forEach((child) -> {
            final Object value = parserContext.getDelegate().parsePropertySubElement(child,
                            parserContext.getContainingBeanDefinition());
            builder.addPropertyValue(FIELD_COMPARATOR, value);
        });
        BeanParserUtils.getAttributeValue(comparatorElement, BEAN_REF_ATTRIBUTE)
                        .ifPresent((comparator) -> builder.addPropertyReference(FIELD_COMPARATOR, comparator));
    }
}
