/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean.schema;

import com.hybris.cockpitng.core.util.bean.PropertyBeanExtender;
import java.util.Optional;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Handles parsing property extenders in CockpitNG Spring namespace.
 * <p>
 * Namespace tag: <strong><code>property-extender</code></strong>
 * </p>
 *
 * @see PropertyBeanExtender
 */
public class PropertyBeanExtenderParser extends AbstractBeanExtenderParser
{
    protected static final String ELEMENT_NAME = "property-extender";
    private static final String FIELD_VALUE = "value";


    @Override
    protected Class<?> getBeanClass(final Element element)
    {
        return PropertyBeanExtender.class;
    }


    @Override
    protected String getParentName(final Element element)
    {
        final String parentName = super.getParentName(element);
        return parentName == null ? PropertyBeanExtender.BEAN_NAME : parentName;
    }


    @Override
    protected void doParse(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder)
    {
        super.doParse(element, parserContext, builder);
        BeanParserUtils.mapAndConsume(BeanParserUtils.getAttributeValue(element, BeanDefinitionParserDelegate.VALUE_ATTRIBUTE),
                        (value) -> parseRootElement(element, value), (value) -> builder.addPropertyValue(FIELD_VALUE, value));
        BeanParserUtils.getChildren(element).forEach((child) -> {
            final Object value = parseChildElement(child, parserContext, builder);
            builder.addPropertyValue(FIELD_VALUE, value);
        });
    }


    protected Object parseRootElement(final Element element, final String value)
    {
        final Optional<String> valueType = BeanParserUtils.getAttributeValue(element,
                        BeanDefinitionParserDelegate.VALUE_TYPE_ATTRIBUTE);
        if(valueType.isPresent())
        {
            return new TypedStringValue(value, valueType.get());
        }
        else
        {
            return new TypedStringValue(value);
        }
    }


    protected Object parseChildElement(final Element element, final ParserContext parserContext,
                    final BeanDefinitionBuilder builder)
    {
        return parserContext.getDelegate().parsePropertySubElement(element, parserContext.getContainingBeanDefinition());
    }
}
