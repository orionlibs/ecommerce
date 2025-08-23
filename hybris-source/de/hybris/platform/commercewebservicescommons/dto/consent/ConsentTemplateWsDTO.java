package de.hybris.platform.commercewebservicescommons.dto.consent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "ConsentTemplate", description = "Representation of a Consent Template")
public class ConsentTemplateWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "id", value = "Consent template identifier")
    private String id;
    @ApiModelProperty(name = "name", value = "Consent template name")
    private String name;
    @ApiModelProperty(name = "description", value = "Consent template description")
    private String description;
    @ApiModelProperty(name = "version", value = "Consent template version")
    private Integer version;
    @ApiModelProperty(name = "currentConsent", value = "Current consent")
    private ConsentWsDTO currentConsent;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
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


    public void setVersion(Integer version)
    {
        this.version = version;
    }


    public Integer getVersion()
    {
        return this.version;
    }


    public void setCurrentConsent(ConsentWsDTO currentConsent)
    {
        this.currentConsent = currentConsent;
    }


    public ConsentWsDTO getCurrentConsent()
    {
        return this.currentConsent;
    }
}
