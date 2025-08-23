package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesXmlService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultXmlChangesServiceTest extends HybrisJUnit4TransactionalTest
{
    private static final Logger LOG = Logger.getLogger(DefaultXmlChangesServiceTest.class.getName());
    private E2EChangesXmlService changesXmlService;
    private static final String CODE = "cockpitng-config";
    private static final String NAME_FILE = "backoffice.xml";


    @Before
    public void setUp()
    {
        this.changesXmlService = (E2EChangesXmlService)Registry.getApplicationContext().getBean("defaultBackofficeChanges");
    }


    @After
    public void tearDown()
    {
    }


    @Test
    public void getNameFileTest()
    {
        Assert.assertNotEquals("check name file ", "wbackoffice.xml", this.changesXmlService.getNameFile());
        Assert.assertEquals("check name file equal", "backoffice.xml", this.changesXmlService.getNameFile());
    }


    @Test
    public void getCodeTest()
    {
        Assert.assertNotEquals("check name file ", "wbacmycode", this.changesXmlService.getCode());
        Assert.assertEquals("check name file equal", "cockpitng-config", this.changesXmlService.getCode());
    }
}
