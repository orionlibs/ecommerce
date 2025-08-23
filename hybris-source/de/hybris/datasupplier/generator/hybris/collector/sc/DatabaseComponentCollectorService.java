package de.hybris.datasupplier.generator.hybris.collector.sc;

import de.hybris.datasupplier.generator.tomcat.data.DatabaseComponentDeployment;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;

public interface DatabaseComponentCollectorService
{
    void init();


    DatabaseComponentDeployment collectDatabaseComponents(TomcatConfiguration paramTomcatConfiguration);


    void destroy();
}
