/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataquality.model;

import java.util.List;

/**
 * This class represents result of data quality calculation
 */
public class DataQuality
{
    private double dataQualityIndex;
    private String description;
    private List<DataQualityProperty> dataQualityProperties;
    private String groupLabel;


    /**
     * Returns the number between 0 and 1 which represents percentage of quality.
     *
     * @return the dataQualityIndex
     */
    public double getDataQualityIndex()
    {
        return dataQualityIndex;
    }


    /**
     * Sets the number between 0 and 1 which represents percentage of quality.
     *
     */
    public void setDataQualityIndex(final double dataQualityIndex)
    {
        this.dataQualityIndex = dataQualityIndex;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(final String description)
    {
        this.description = description;
    }


    public List<DataQualityProperty> getDataQualityProperties()
    {
        return dataQualityProperties;
    }


    public void setDataQualityProperties(final List<DataQualityProperty> dataQualityProperties)
    {
        this.dataQualityProperties = dataQualityProperties;
    }


    public String getGroupLabel()
    {
        return groupLabel;
    }


    public void setGroupLabel(final String groupLabel)
    {
        this.groupLabel = groupLabel;
    }
}
