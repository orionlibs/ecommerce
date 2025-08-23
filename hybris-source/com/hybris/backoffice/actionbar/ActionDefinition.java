/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.actionbar;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;

/**
 * Definition of single action on toolbar.
 * The typical implementation could be a simple POJO.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface ActionDefinition extends Serializable
{
    /**
     * Returns the ID of the node.
     * Should be unique within the tree structure.
     *
     * @return ID of the node
     */
    String getId();


    /**
     * Returns the localization key for the name of the node.
     * This will be used to get the localized name of the node in the current Locale.
     * If this is <code>null</code> then {@link #getName()} is used as the node name.
     *
     * @return localization key for the name of the node or <code>null</code> if {@link #getName()} is should be used as
     *         the node name
     * @see #getName()
     */
    String getNameLocKey();


    /**
     * Returns the name of the node.
     * This is only used as a node name if the localization key for the node name is not set (see
     * {@link #getNameLocKey()}).
     *
     * @return the name of the node
     * @see #getNameLocKey()
     */
    String getName();


    /**
     * Returns the localization key for the description of the node.
     * This will be used to get the localized description of the node in the current Locale.
     * If this is <code>null</code> then {@link #getDescription()} is used as the node description.
     *
     * @return localization key for the description of the node or <code>null</code> if {@link #getDescription()} is
     *         should be used as
     *         the node description
     * @see #getDescription()
     */
    String getDescriptionLocKey();


    /**
     * Returns the description of the node.
     * This is only used as a node description if the localization key for the node description is not set (see
     * {@link #getDescriptionLocKey()}).
     *
     * @return the description of the node
     * @see #getDescriptionLocKey()
     */
    String getDescription();


    /**
     * Returns the localization key for the icon URI of the node.
     * This will be used to get the localized icon URI of the node in the current Locale.
     * If this is <code>null</code> then {@link #getIconUri()} is used as the node description.
     *
     * @return localization key for the icon URI of the node or <code>null</code> if {@link #getIconUri()} is
     *         should be used as
     *         the node icon URI
     * @see #getIconUri()
     */
    String getIconUriLocKey();


    /**
     * Returns the icon URI of the node.
     * This is only used as a node icon URI if the localization key for the node icon URI is not set (see
     * {@link #getIconUriLocKey()}).
     *
     * @return the icon URI of the node
     * @see #getIconUriLocKey()
     */
    String getIconUri();


    /**
     * Returns a group name for a node.
     * <P>
     * Interpretation of group depends totally on toolbar's implementation.
     *
     * @return name of group or <code>null</code> if not applicable
     */
    default String getGroup()
    {
        return null;
    }
}
