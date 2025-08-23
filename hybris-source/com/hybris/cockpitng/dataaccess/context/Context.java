/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.context;

import java.util.Set;

/**
 * <p>
 * This interface represents an abstract context
 * </p>
 */
public interface Context
{
    String TYPE_CODE = "typeCode";


    /**
     * Get value of attribute named by <code>name</code>
     *
     * @param name
     * @return
     */
    Object getAttribute(String name);


    /**
     * Add attribute identified by <code>name</code> with <code>value</code>.
     *
     * @param name - attribute name
     * @param value - attribute value
     */
    void addAttribute(String name, Object value);


    /**
     * Get list of set attribute names.
     *
     * @return list of set attributes
     */
    Set<String> getAttributeNames();


    /**
     * Remove attribute named by <code>name</code>
     *
     * @param name - attribute name
     */
    Object removeAttribute(String name);


    /**
     * Clear all attributes in prepared context;
     */
    void clearAttributes();
}
