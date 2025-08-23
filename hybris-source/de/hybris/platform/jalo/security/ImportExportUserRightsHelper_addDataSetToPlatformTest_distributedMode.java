package de.hybris.platform.jalo.security;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.impex.ImportConfig;

@IntegrationTest
public class ImportExportUserRightsHelper_addDataSetToPlatformTest_distributedMode extends ImportExportUserRightsHelper_addDataSetToPlatformTest
{
    protected ImportConfig getCompleteImpexConfig(ImportExportUserRightsHelper_addDataSetToPlatformTest.ImpexRow... rows)
    {
        ImportConfig config = getStandardConfig();
        config.setScript(asResource(composeImpex(rows)));
        config.setDistributedImpexEnabled(true);
        return config;
    }


    protected String prefixed(String uid)
    {
        return "import_userrightshelper_distributed_" + uid;
    }
}
