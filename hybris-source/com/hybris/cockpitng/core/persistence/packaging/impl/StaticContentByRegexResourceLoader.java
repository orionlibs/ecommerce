/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging.impl;

import com.hybris.cockpitng.core.persistence.packaging.ResourceLoader;
import com.hybris.cockpitng.core.util.Validate;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Responsible for returning default resource for paths whose match regular expression.
 * This loader stores resource mapping in format: regular expression → default resource path.
 */
public class StaticContentByRegexResourceLoader implements ResourceLoader
{
    private Map<Pattern, String> mapping;


    /**
     * Constructs a new instance.
     */
    public StaticContentByRegexResourceLoader()
    {
        this(Collections.emptyMap());
    }


    /**
     * Constructs a new instance and sets resource mapping.
     *
     * @param mapping the mapping in format: regular expression → default resource path.
     * @throws IllegalArgumentException if the mapping is blank.
     */
    public StaticContentByRegexResourceLoader(final Map<String, String> mapping)
    {
        fillMapping(mapping);
    }


    /**
     * Sets mapping used to match resource path to default resource path.
     *
     * @param mapping the mapping in format: regular expression → default resource path.
     * @throws IllegalArgumentException if the mapping is blank.
     */
    public void setMapping(final Map<String, String> mapping)
    {
        fillMapping(mapping);
    }


    private void fillMapping(final Map<String, String> map)
    {
        Validate.notNull("Mapping cannot be null", map);
        mapping = new LinkedHashMap<>();
        for(final Map.Entry<String, String> entry : map.entrySet())
        {
            mapping.put(Pattern.compile(entry.getKey()), entry.getValue());
        }
    }


    @Override
    public boolean hasResource(final String path)
    {
        Validate.notBlank("Resource path cannot be blank", path);
        return mapping.entrySet().stream().filter(e -> e.getKey().matcher(path).matches() && isResourceExisting(e.getValue()))
                        .findFirst().isPresent();
    }


    /**
     * Checks whether a resource identified by path exist.
     *
     * @param path the path identifying resource.
     * @return {@code true} if the resource exists, otherwise {@code false}.
     */
    protected boolean isResourceExisting(final String path)
    {
        return getClass().getResource(path) != null;
    }


    /**
     * Returns default resource when the path match regular expression. It matches path to regular expressions in turn
     * until one match and the default resource exists.
     *
     * @throws IllegalArgumentException if the path is blank.
     */
    @Override
    public InputStream getResourceAsStream(final String path)
    {
        Validate.notBlank("Resource path cannot be blank", path);
        for(final Map.Entry<Pattern, String> entry : mapping.entrySet())
        {
            if(entry.getKey().matcher(path).matches())
            {
                final InputStream stream = getResourceByPath(entry.getValue());
                if(stream != null)
                {
                    return stream;
                }
            }
        }
        return null;
    }


    /**
     * Returns resource identified by path as {@link InputStream} or {@code null} if resource does not exist.
     *
     * @param path the path identifying resource.
     * @return the input stream or {@code null} if the resource does not exist.
     */
    protected InputStream getResourceByPath(final String path)
    {
        return getClass().getResourceAsStream(path);
    }
}
