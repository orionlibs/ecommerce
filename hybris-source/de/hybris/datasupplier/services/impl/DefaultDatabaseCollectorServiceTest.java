package de.hybris.datasupplier.services.impl;

import de.hybris.datasupplier.collectors.DatabaseCollector;
import de.hybris.datasupplier.collectors.impl.HanaCollector;
import de.hybris.datasupplier.collectors.impl.HsqlCollector;
import de.hybris.datasupplier.collectors.impl.MySQLCollector;
import de.hybris.datasupplier.collectors.impl.OracleCollector;
import de.hybris.datasupplier.collectors.impl.SqlServerCollector;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultDatabaseCollectorServiceTest
{
    private static final Logger LOG = Logger.getLogger(DefaultDatabaseCollectorServiceTest.class);
    private static DefaultDatabaseCollectorService collectorService;
    private static String LOCAL_ADDRESS;
    private static List<DatabaseEntry> databaseEntries = new ArrayList<>();


    @BeforeClass
    public static void initialize() throws IOException
    {
        LOCAL_ADDRESS = InetAddress.getLocalHost().getHostName().toLowerCase();
        Properties testData = new Properties();
        InputStream in = DefaultDatabaseCollectorServiceTest.class.getClassLoader().getResourceAsStream("test/testConnections.properties");
        try
        {
            testData.load(in);
        }
        finally
        {
            in.close();
        }
        DatabaseEntry entry = null;
        do
        {
            entry = new DatabaseEntry(databaseEntries.size(), testData);
            if(entry.connectionString == null)
            {
                continue;
            }
            databaseEntries.add(entry);
        }
        while(entry.connectionString != null);
        Set<DatabaseCollector> collectors = new HashSet<>();
        collectors.add(new HanaCollector());
        collectors.add(new HsqlCollector());
        collectors.add(new MySQLCollector());
        collectors.add(new OracleCollector());
        collectors.add(new SqlServerCollector());
        collectorService = new DefaultDatabaseCollectorService();
        collectorService.setCollectors(collectors);
    }


    @Test
    public void extractHostAndNameTest()
    {
        for(DatabaseEntry entry : databaseEntries)
        {
            LOG.info("Testing " + entry);
            Assert.assertEquals(entry.host, collectorService.extractHostName(entry.connectionString));
            Assert.assertEquals(entry.name, collectorService.extractDatabaseName(entry.connectionString));
        }
    }
}
