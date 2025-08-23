package de.hybris.platform.servicelayer.config.impl;

import de.hybris.platform.util.Utilities;
import java.util.Properties;
import org.springframework.beans.factory.FactoryBean;

public class HybrisPropertiesFactory implements FactoryBean<Properties>
{
    public Properties getObject()
    {
        return Utilities.loadPlatformProperties();
    }


    public boolean isSingleton()
    {
        return true;
    }


    public Class<?> getObjectType()
    {
        return Properties.class;
    }
}
