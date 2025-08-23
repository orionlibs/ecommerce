package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.enums.ProductInfoStatus;
import java.io.Serializable;

public class ConfigurationInfoData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ConfiguratorType configuratorType;
    private ProductInfoStatus status;
    private String configurationLabel;
    private String configurationValue;


    public void setConfiguratorType(ConfiguratorType configuratorType)
    {
        this.configuratorType = configuratorType;
    }


    public ConfiguratorType getConfiguratorType()
    {
        return this.configuratorType;
    }


    public void setStatus(ProductInfoStatus status)
    {
        this.status = status;
    }


    public ProductInfoStatus getStatus()
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
