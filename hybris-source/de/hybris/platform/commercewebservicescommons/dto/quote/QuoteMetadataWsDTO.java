package de.hybris.platform.commercewebservicescommons.dto.quote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "QuoteMetadata", description = "Representation of quote metadata.")
public class QuoteMetadataWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "name", value = "Name of the quote.", required = true, example = "Quote Name")
    private String name;
    @ApiModelProperty(name = "description", value = "Description of the quote.", required = false, example = "Quote Description")
    private String description;
    @ApiModelProperty(name = "expirationTime", value = "Expiration time of the quote.", required = true, example = "yyyy-MM-ddTHH:mm:ss+0000")
    private Date expirationTime;


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


    public void setExpirationTime(Date expirationTime)
    {
        this.expirationTime = expirationTime;
    }


    public Date getExpirationTime()
    {
        return this.expirationTime;
    }
}
