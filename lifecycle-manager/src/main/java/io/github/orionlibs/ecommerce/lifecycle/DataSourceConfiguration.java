package io.github.orionlibs.ecommerce.lifecycle;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfiguration
{
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties primaryDataSourceProperties()
    {
        return new DataSourceProperties();
    }


    @Bean
    @Primary
    public DataSource primaryDataSource(@Qualifier("primaryDataSourceProperties") DataSourceProperties props)
    {
        DataSource realDs = props.initializeDataSourceBuilder().build();
        return realDs;
    }
}
