package de.hybris.platform.platformbackoffice.bulkedit.dto;

import de.hybris.platform.classification.features.Feature;

public class ClassificationChangeDto
{
    private Feature feature;
    private String encodedQualifier;
    private String isoCode;
    private boolean clear;
    private boolean merge;


    public Feature getFeature()
    {
        return this.feature;
    }


    public void setFeature(Feature feature)
    {
        this.feature = feature;
    }


    public String getEncodedQualifier()
    {
        return this.encodedQualifier;
    }


    public void setEncodedQualifier(String encodedQualifier)
    {
        this.encodedQualifier = encodedQualifier;
    }


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public void setIsoCode(String isoCode)
    {
        this.isoCode = isoCode;
    }


    public boolean isClear()
    {
        return this.clear;
    }


    public void setClear(boolean clear)
    {
        this.clear = clear;
    }


    public boolean isMerge()
    {
        return this.merge;
    }


    public void setMerge(boolean merge)
    {
        this.merge = merge;
    }
}
