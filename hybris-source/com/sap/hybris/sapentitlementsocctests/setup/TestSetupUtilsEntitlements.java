/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapentitlementsocctests.setup;

import com.sap.hybris.sapentitlementsintegration.exception.SapEntitlementException;
import de.hybris.platform.core.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to be used in test suites to manage tests (e.g. start server, load data).
 */
public class TestSetupUtilsEntitlements extends de.hybris.platform.commercewebservicestests.setup.TestSetupUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(TestSetupUtilsEntitlements.class);


    public static void loadExtensionDataInJunit() throws SapEntitlementException
    {
        Registry.setCurrentTenantByID("junit");
        loadExtensionData();
    }


    public static void loadExtensionData()
    {
        final SapEntitlementsOCCTestSetup sapEntitlementsOCCTestSetup = Registry.getApplicationContext()
                        .getBean("sapEntitlementsOCCTestSetup", SapEntitlementsOCCTestSetup.class);
        sapEntitlementsOCCTestSetup.loadData();
        LOG.info("No data for current OCC extension.");
    }
}
