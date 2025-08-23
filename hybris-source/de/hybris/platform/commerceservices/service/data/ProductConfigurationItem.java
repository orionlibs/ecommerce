package de.hybris.platform.commerceservices.service.data;

import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.enums.ProductInfoStatus;
import java.io.Serializable;

public class ProductConfigurationItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String key;
    private Serializable value;
    private ProductInfoStatus status;
    private ConfiguratorType configuratorType;


    public void setKey(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return this.key;
    }


    public void setValue(Serializable value)
    {
        this.value = value;
    }


    public Serializable getValue()
    {
        return this.value;
    }


    public void setStatus(ProductInfoStatus status)
    {
        this.status = status;
    }


    public ProductInfoStatus getStatus()
    {
        return this.status;
    }


    public void setConfiguratorType(ConfiguratorType configuratorType)
    {
        this.configuratorType = configuratorType;
    }


    public ConfiguratorType getConfiguratorType()
    {
        return this.configuratorType;
    }
}
