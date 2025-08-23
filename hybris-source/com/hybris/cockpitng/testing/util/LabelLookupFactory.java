/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util;

/**
 * Factory mock definitions of labels for {@link org.zkoss.util.resource.Labels}
 */
public class LabelLookupFactory
{
    public static LabelLookupFactory createLabelLookup()
    {
        return new LabelLookupFactory();
    }


    private final LabelLookup lookup;


    private LabelLookupFactory()
    {
        this.lookup = new LabelLookup();
    }


    public LabelLookup getLookup()
    {
        return lookup;
    }


    public LabelLookupFactory registerLabel(final String key, final String label)
    {
        lookup.addLabel(key, label);
        return this;
    }
}
