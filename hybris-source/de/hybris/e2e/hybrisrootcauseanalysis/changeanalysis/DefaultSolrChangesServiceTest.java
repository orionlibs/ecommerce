package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesPropertiesService;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl.DefaultSolrChangesService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultSolrChangesServiceTest extends HybrisJUnit4TransactionalTest
{
    private E2EChangesPropertiesService solrService;
    private static final String NAME = "solr.properties";


    @Before
    public void setUp()
    {
        this.solrService = (E2EChangesPropertiesService)Registry.getApplicationContext().getBean("defaultSolrChanges", DefaultSolrChangesService.class);
    }


    @After
    public void tearDown()
    {
    }


    @Test
    public void testGetNameFile()
    {
        Assert.assertEquals("check the name file", "solr.properties", this.solrService.getNameFile());
        Assert.assertNotEquals("check name file is different from original", "licensee.properties", this.solrService.getNameFile());
    }
}
