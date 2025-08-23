/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.zkoss.util.resource.LabelLocator2;

/**
 * Mock definitions of labels for {@link org.zkoss.util.resource.Labels}
 */
public class LabelLookup implements LabelLocator2
{
    private final Map<String, String> labels = new HashMap<>();


    void addLabel(final String key, final String label)
    {
        if(labels.containsKey(key))
        {
            throw new IllegalStateException("Label already defined with provided key: ".concat(key));
        }
        labels.put(key, label);
    }


    public void clearLabels()
    {
        labels.clear();
    }


    @Override
    public InputStream locate(final Locale locale)
    {
        final StringBuilder sb = new StringBuilder();
        labels.forEach((key, label) -> sb.append(key).append("=").append(label).append(System.lineSeparator()));
        return new ByteArrayInputStream(sb.toString().getBytes(Charset.defaultCharset()));
    }


    @Override
    public String getCharset()
    {
        return Charset.defaultCharset().name();
    }
}
