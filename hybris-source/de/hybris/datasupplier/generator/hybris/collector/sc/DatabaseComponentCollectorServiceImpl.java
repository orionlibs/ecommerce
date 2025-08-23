package de.hybris.datasupplier.generator.hybris.collector.sc;

import de.hybris.datasupplier.generator.tomcat.collector.BaseTomcatCollectorServiceImpl;
import de.hybris.datasupplier.generator.tomcat.data.DatabaseComponentDeployment;
import de.hybris.datasupplier.generator.tomcat.data.DatabaseVersion;
import de.hybris.datasupplier.generator.tomcat.data.Tomcat;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import de.hybris.datasupplier.generator.tomcat.data.TomcatServer;
import de.hybris.datasupplier.services.DatabaseCollectorService;
import de.hybris.platform.core.Registry;
import java.util.Collection;
import java.util.Iterator;

public class DatabaseComponentCollectorServiceImpl extends BaseTomcatCollectorServiceImpl implements DatabaseComponentCollectorService
{
    private DatabaseCollectorService databaseCollectorService;


    public String toString()
    {
        return "DatabaseComponentCollectorServiceImpl";
    }


    public DatabaseComponentDeployment collectDatabaseComponents(TomcatConfiguration tomcatConfiguration)
    {
        return startCollectDatabaseComponents(tomcatConfiguration);
    }


    protected DatabaseComponentDeployment startCollectDatabaseComponents(TomcatConfiguration tomcatConfiguration)
    {
        DatabaseComponentDeployment deployment = new DatabaseComponentDeployment();
        DatabaseVersion dbVersion = new DatabaseVersion();
        dbVersion.setDatabaseName(getDatabaseCollectorService().getDatabaseName());
        dbVersion.setDBTypeForSAP(getDatabaseCollectorService().getDatabaseType());
        dbVersion.setDatabaseHost(getDatabaseCollectorService().getDatabaseHost());
        dbVersion.setDatabaseIPAddress(getDatabaseCollectorService().getDatabaseIPAddress());
        dbVersion.setDatabaseFQDName(getDatabaseCollectorService().getDatabaseFQDName());
        deployment.setDatabaseVersion(dbVersion);
        Tomcat tomcat = tomcatConfiguration.getTomcat();
        Collection servers = tomcat.getServers();
        Iterator<TomcatServer> serversIt = servers.iterator();
        TomcatServer server = serversIt.next();
        deployment.setTechnicalName(server.getTechnicalName());
        return deployment;
    }


    public DatabaseCollectorService getDatabaseCollectorService()
    {
        if(this.databaseCollectorService == null)
        {
            this.databaseCollectorService = (DatabaseCollectorService)Registry.getApplicationContext().getBean("databaseCollectorService", DatabaseCollectorService.class);
        }
        return this.databaseCollectorService;
    }


    public void setDatabaseCollectorService(DatabaseCollectorService databaseCollectorService)
    {
        this.databaseCollectorService = databaseCollectorService;
    }
}
