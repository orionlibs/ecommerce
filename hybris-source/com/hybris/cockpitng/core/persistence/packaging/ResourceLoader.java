/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import java.io.InputStream;

/**
 * Responsible for returning resources.
 */
public interface ResourceLoader
{
    /**
     * Checks whether this loader can load resource identified by path.
     *
     * @param path the path identifying resource.
     * @return {@code true} whether this loader can load resource, otherwise {@code false}.
     */
    boolean hasResource(String path);


    /**
     * Returns resource identified by path as {@link InputStream} or {@code null} if resource does not exist.
     *
     * @param path the path identifying resource.
     * @return Resource as {@link InputStream} or {@code null}.
     */
    InputStream getResourceAsStream(String path);
}
