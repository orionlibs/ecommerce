/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data;

import java.util.Collections;
import java.util.Map;

/**
 * Agregates information about attribute name and attribute number
 */
public class SearchAttributeDescriptor
{
    private final String attributeName;
    private final int attributeNumber;
    private final Map<String, String> editorParameters;


    /**
     * @param attributeName
     *           name of the attribute
     * @param attributeNumber
     *           0-based index, to distinguish many search condition for the same field
     * @param editorParameters
     *           parameters defined for the editor specifying this attribute
     */
    public SearchAttributeDescriptor(final String attributeName, final int attributeNumber,
                    final Map<String, String> editorParameters)
    {
        this.attributeName = attributeName;
        this.attributeNumber = attributeNumber;
        this.editorParameters = editorParameters;
    }


    /**
     * @param attributeName
     *           name of the attribute
     * @param attributeNumber
     *           0-based index, to distinguish many search condition for the same field
     */
    public SearchAttributeDescriptor(final String attributeName, final int attributeNumber)
    {
        this.attributeName = attributeName;
        this.attributeNumber = attributeNumber;
        this.editorParameters = Collections.emptyMap();
    }


    public SearchAttributeDescriptor(final String attributeName)
    {
        this.attributeName = attributeName;
        this.attributeNumber = 0;
        this.editorParameters = Collections.emptyMap();
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final SearchAttributeDescriptor that = (SearchAttributeDescriptor)o;
        if(attributeNumber != that.attributeNumber)
        {
            return false;
        }
        if(attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
        {
            return false;
        }
        return !(editorParameters != null ? !editorParameters.equals(that.editorParameters) : that.editorParameters != null);
    }


    @Override
    public int hashCode()
    {
        int result = attributeName != null ? attributeName.hashCode() : 0;
        result = 31 * result + attributeNumber;
        result = 31 * result + (editorParameters != null ? editorParameters.hashCode() : 0);
        return result;
    }


    public String getAttributeName()
    {
        return attributeName;
    }


    public int getAttributeNumber()
    {
        return attributeNumber;
    }


    public Map<String, String> getEditorParameters()
    {
        return editorParameters;
    }


    public String getNameAndNumberHash()
    {
        return String.valueOf((getAttributeName() + getAttributeNumber()).hashCode());
    }
}
