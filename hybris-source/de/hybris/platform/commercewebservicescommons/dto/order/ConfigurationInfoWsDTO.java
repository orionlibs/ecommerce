package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "ConfigurationInfo", description = "Representation of a Configuration Info")
public class ConfigurationInfoWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "configuratorType", value = "Type of configuration info")
    private String configuratorType;
    @ApiModelProperty(name = "status", value = "Status of configuration info")
    private String status;
    @ApiModelProperty(name = "configurationLabel", value = "Label of configuration info")
    private String configurationLabel;
    @ApiModelProperty(name = "configurationValue", value = "Value of configuration info")
    private String configurationValue;


    public void setConfiguratorType(String configuratorType)
    {
        this.configuratorType = configuratorType;
    }


    public String getConfiguratorType()
    {
        return this.configuratorType;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setConfigurationLabel(String configurationLabel)
    {
        this.configurationLabel = configurationLabel;
    }


    public String getConfigurationLabel()
    {
        return this.configurationLabel;
    }


    public void setConfigurationValue(String configurationValue)
    {
        this.configurationValue = configurationValue;
    }


    public String getConfigurationValue()
    {
        return this.configurationValue;
    }
}
