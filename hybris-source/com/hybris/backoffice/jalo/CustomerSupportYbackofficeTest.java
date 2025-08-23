package com.hybris.backoffice.jalo;

import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomerSupportYbackofficeTest extends HybrisJUnit4TransactionalTest
{
    private static final Logger LOG = Logger.getLogger(CustomerSupportYbackofficeTest.class.getName());


    @Before
    public void setUp()
    {
    }


    @After
    public void tearDown()
    {
    }


    @Test
    public void testYbackoffice()
    {
        boolean testTrue = true;
        Assert.assertTrue("true is not true", true);
    }
}
