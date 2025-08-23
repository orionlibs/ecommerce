package de.hybris.platform.apiregistryservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisteredDestinationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("identifier")
    private String identifier;
    @JsonProperty("id")
    private String targetId;


    @JsonProperty("identifier")
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    @JsonProperty("identifier")
    public String getIdentifier()
    {
        return this.identifier;
    }


    @JsonProperty("id")
    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }


    @JsonProperty("id")
    public String getTargetId()
    {
        return this.targetId;
    }
}
