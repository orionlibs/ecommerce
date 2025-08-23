package de.hybris.datasupplier.generator.tomcat.collector.config;

import com.sap.sup.admin.sldsupplier.error.ConfigurationDataCollectException;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;

public interface TomcatConfigurationCollectorService
{
    void init();


    TomcatConfiguration collectConfiguration() throws ConfigurationDataCollectException;


    void destroy();
}
