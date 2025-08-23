/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.util;

import java.util.Enumeration;
import javax.servlet.ServletRequest;

/**
 * Interface providing request attributes that should be copied to executors by {@link BackofficeThreadContextCreator}.
 * Bear in mind that those values will be available in different thread and no thread-related values should be copied.
 */
public interface BackofficeThreadRequestAttributes
{
    /**
     * Gets attributes to be copied from specified request
     *
     * @param request
     *           request that caused {@link BackofficeThreadContextCreator} spawn new executor
     * @return enumeration of attribute names
     */
    Enumeration<String> getAttributeNames(final ServletRequest request);


    /**
     * Gets attribute value to be copied.
     *
     * @param request
     *           request that caused {@link BackofficeThreadContextCreator} spawn new executor
     * @param attribute
     *           name of attribute picked for copying
     * @return value of attribute
     */
    Object getAttribute(final ServletRequest request, final String attribute);
}
