package de.hybris.platform.spring;

import de.hybris.platform.core.Registry;
import javax.sql.DataSource;
import org.springframework.beans.factory.FactoryBean;

public class DataSourceFactoryBean implements FactoryBean
{
    public boolean isSingleton()
    {
        return false;
    }


    public Object getObject()
    {
        return Registry.getCurrentTenant().getDataSource();
    }


    public Class getObjectType()
    {
        return DataSource.class;
    }
}
