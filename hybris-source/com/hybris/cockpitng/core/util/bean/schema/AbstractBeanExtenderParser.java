/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean.schema;

import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.BEAN_REF_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.PARENT_ATTRIBUTE;

import java.util.Optional;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Handles parsing common part of extenders in CockpitNG Spring namespace.
 *
 * @see com.hybris.cockpitng.core.util.bean.AbstractBeanExtender
 */
public abstract class AbstractBeanExtenderParser extends AbstractSingleBeanDefinitionParser
{
    private static final String ATTRIBUTE_PROPERTY = "property";
    private static final String ATTRIBUTE_GETTER = "getter";
    private static final String ATTRIBUTE_MAPPER = "mapper";
    private static final String ATTRIBUTE_SETTER = "setter";
    private static final String FIELD_PROPERTY = "property";
    private static final String FIELD_GETTER = "getter";
    private static final String FIELD_MAPPER = "mapper";
    private static final String FIELD_SETTER = "setter";


    @Override
    protected String getParentName(final Element element)
    {
        final Optional<String> parent = BeanParserUtils.getAttributeValue(element, PARENT_ATTRIBUTE);
        return parent.orElse(super.getParentName(element));
    }


    @Override
    protected abstract Class<?> getBeanClass(final Element element);


    @Override
    protected void doParse(final Element element, final BeanDefinitionBuilder builder)
    {
        super.doParse(element, builder);
        BeanParserUtils.getAttributeValue(element, ABSTRACT_ATTRIBUTE)
                        .ifPresent((abstractBean) -> builder.setAbstract(Boolean.parseBoolean(abstractBean)));
        BeanParserUtils.getAttributeValue(element, BEAN_REF_ATTRIBUTE).ifPresent(builder::addConstructorArgValue);
        BeanParserUtils.getAttributeValue(element, BEAN_REF_ATTRIBUTE).ifPresent(builder::addDependsOn);
        BeanParserUtils.getAttributeValue(element, ATTRIBUTE_PROPERTY)
                        .ifPresent((property) -> builder.addPropertyValue(FIELD_PROPERTY, property));
        BeanParserUtils.getAttributeValue(element, ATTRIBUTE_GETTER)
                        .ifPresent((property) -> builder.addPropertyValue(FIELD_GETTER, property));
        BeanParserUtils.getAttributeValue(element, ATTRIBUTE_MAPPER)
                        .ifPresent((property) -> builder.addPropertyValue(FIELD_MAPPER, property));
        BeanParserUtils.getAttributeValue(element, ATTRIBUTE_SETTER)
                        .ifPresent((property) -> builder.addPropertyValue(FIELD_SETTER, property));
    }


    @Override
    protected boolean shouldGenerateIdAsFallback()
    {
        return true;
    }
}
