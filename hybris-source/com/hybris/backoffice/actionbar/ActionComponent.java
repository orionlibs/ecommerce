/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.actionbar;

import org.zkoss.zk.ui.Component;

/**
 * A component interface that represents a single action
 *
 */
public interface ActionComponent
{
    /**
     * Sets new label representation of action
     *
     * @param label label representation
     */
    void setLabel(final String label);


    /**
     * Gets current label representation of action
     *
     * @return label representation
     */
    String getLabel();


    /**
     * Gets uri to action icon
     *
     * @return action's icon uri
     */
    String getImage();


    /**
     * Sets new uri to action icon
     *
     * @param image action's icon uri
     */
    void setImage(final String image);


    /**
     * Gets current action's tooltip
     *
     * @return tooltip
     */
    String getTooltiptext();


    /**
     * Sets new action's tooltip
     *
     * @param tooltiptext new tooltip
     */
    void setTooltiptext(final String tooltiptext);


    /**
     * Gets an attribute of specified name from action representation component
     *
     * @param name name of attribute
     * @return value of component's attribute
     */
    Object getAttribute(final String name);


    /**
     * Checks whether action representation component has defined an attribute of specified name
     *
     * @param name name of attribute
     * @return <code>true</code> if component defines an attribute
     */
    boolean hasAttribute(final String name);


    /**
     * Sets new value of specified attribute in action representation component
     *
     * @param name name of attribute
     * @param value value of attribute
     * @return previous value of attribute
     */
    Object setAttribute(final String name, final Object value);


    /**
     * Removes definition of specified attribute from action representation component
     *
     * @param name name of attribute
     * @return value of attribute just removed
     */
    Object removeAttribute(final String name);


    /**
     * Gets action representation component
     *
     * @return actual component
     */
    Component getComponent();
}
