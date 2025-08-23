package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesPropertiesService;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl.DefaultLicenceChangesService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import java.util.Properties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultLicenceChangesServicetest extends HybrisJUnit4TransactionalTest
{
    private E2EChangesPropertiesService licenceService;


    @Before
    public void setUp()
    {
        this.licenceService = (E2EChangesPropertiesService)Registry.getApplicationContext().getBean("defaultLicenceChanges", DefaultLicenceChangesService.class);
    }


    @After
    public void tearDown()
    {
    }


    @Test
    public void testSaveLicenceInProperties()
    {
        Properties info = this.licenceService.getInfo();
        Assert.assertEquals("check the name file", "support@hybris.com", info.getProperty("licence.email"));
        Assert.assertNull("check the name file", info.getProperty("licence.purpose"));
    }
}
