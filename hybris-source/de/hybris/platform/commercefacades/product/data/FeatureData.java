package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.util.Collection;

public class FeatureData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String description;
    private String type;
    private boolean range;
    private boolean comparable;
    private FeatureUnitData featureUnit;
    private Collection<FeatureValueData> featureValues;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getType()
    {
        return this.type;
    }


    public void setRange(boolean range)
    {
        this.range = range;
    }


    public boolean isRange()
    {
        return this.range;
    }


    public void setComparable(boolean comparable)
    {
        this.comparable = comparable;
    }


    public boolean isComparable()
    {
        return this.comparable;
    }


    public void setFeatureUnit(FeatureUnitData featureUnit)
    {
        this.featureUnit = featureUnit;
    }


    public FeatureUnitData getFeatureUnit()
    {
        return this.featureUnit;
    }


    public void setFeatureValues(Collection<FeatureValueData> featureValues)
    {
        this.featureValues = featureValues;
    }


    public Collection<FeatureValueData> getFeatureValues()
    {
        return this.featureValues;
    }
}
