package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;

@ApiModel(value = "Feature", description = "Representation of a Feature")
public class FeatureWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of the feature")
    private String code;
    @ApiModelProperty(name = "name", value = "Name of the feature")
    private String name;
    @ApiModelProperty(name = "description", value = "Description of the feature")
    private String description;
    @ApiModelProperty(name = "type", value = "Type of the feature")
    private String type;
    @ApiModelProperty(name = "range", value = "Range number of the feature")
    private Boolean range;
    @ApiModelProperty(name = "comparable", value = "Flag defining it feature is comparable")
    private Boolean comparable;
    @ApiModelProperty(name = "featureUnit", value = "Feature unit")
    private FeatureUnitWsDTO featureUnit;
    @ApiModelProperty(name = "featureValues", value = "List of feature values")
    private Collection<FeatureValueWsDTO> featureValues;


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


    public void setRange(Boolean range)
    {
        this.range = range;
    }


    public Boolean getRange()
    {
        return this.range;
    }


    public void setComparable(Boolean comparable)
    {
        this.comparable = comparable;
    }


    public Boolean getComparable()
    {
        return this.comparable;
    }


    public void setFeatureUnit(FeatureUnitWsDTO featureUnit)
    {
        this.featureUnit = featureUnit;
    }


    public FeatureUnitWsDTO getFeatureUnit()
    {
        return this.featureUnit;
    }


    public void setFeatureValues(Collection<FeatureValueWsDTO> featureValues)
    {
        this.featureValues = featureValues;
    }


    public Collection<FeatureValueWsDTO> getFeatureValues()
    {
        return this.featureValues;
    }
}
