/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import org.zkoss.util.resource.Labels;

/**
 * Marker class to be used to recognize list entry that is used to add new references for reference editor.
 */
public final class NestedObjectCreator
{
    public static final String DEFAULT_LABEL = "Create New Reference";
    public static final String LABEL_KEY = "create.reference";
    private String userChosenType;


    /**
     * @return label for the given type
     */
    public String getLabel(final String typeCode)
    {
        return Labels.getLabel(LABEL_KEY, DEFAULT_LABEL, new Object[] {typeCode});
    }


    /**
     * @return type set manually
     */
    public String getUserChosenType()
    {
        return userChosenType;
    }


    /**
     * @param userChosenType type to be used instead of the default, usually a subtype of the default type
     */
    public void setUserChosenType(final String userChosenType)
    {
        this.userChosenType = userChosenType;
    }
}
