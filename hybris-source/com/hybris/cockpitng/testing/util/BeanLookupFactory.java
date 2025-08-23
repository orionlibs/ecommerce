/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util;

/**
 *
 *
 */
public class BeanLookupFactory
{
    public static BeanLookupFactory createBeanLookup()
    {
        return new BeanLookupFactory();
    }


    private final BeanLookup lookup;


    private BeanLookupFactory()
    {
        this.lookup = new BeanLookup();
    }


    public BeanLookup getLookup()
    {
        return lookup;
    }


    public BeanLookupFactory registerBean(final String name, final Object bean)
    {
        lookup.addBean(name, bean);
        return this;
    }
}
