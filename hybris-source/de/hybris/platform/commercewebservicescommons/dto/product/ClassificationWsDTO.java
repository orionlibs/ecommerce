package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;

@ApiModel(value = "Classification", description = "Representation of a Classification")
public class ClassificationWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of the classification")
    private String code;
    @ApiModelProperty(name = "name", value = "Name of the classification")
    private String name;
    @ApiModelProperty(name = "features", value = "List of features for given classification")
    private Collection<FeatureWsDTO> features;


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


    public void setFeatures(Collection<FeatureWsDTO> features)
    {
        this.features = features;
    }


    public Collection<FeatureWsDTO> getFeatures()
    {
        return this.features;
    }
}
