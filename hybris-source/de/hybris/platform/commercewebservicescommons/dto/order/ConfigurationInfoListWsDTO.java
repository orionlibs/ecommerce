package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ConfigurationInfoList", description = "Representation of a Configuration Info List")
public class ConfigurationInfoListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "configurationInfos", value = "List of configuration info")
    private List<ConfigurationInfoWsDTO> configurationInfos;


    public void setConfigurationInfos(List<ConfigurationInfoWsDTO> configurationInfos)
    {
        this.configurationInfos = configurationInfos;
    }


    public List<ConfigurationInfoWsDTO> getConfigurationInfos()
    {
        return this.configurationInfos;
    }
}
