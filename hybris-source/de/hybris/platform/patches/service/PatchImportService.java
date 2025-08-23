package de.hybris.platform.patches.service;

import de.hybris.platform.patches.data.ImpexImportUnit;
import de.hybris.platform.patches.data.ImpexImportUnitResult;
import java.io.FileNotFoundException;
import java.io.SequenceInputStream;

public interface PatchImportService
{
    public static final String IMPEX_IMPORT_CRON_JOB_REMOVE_ON_SUCCESS_PROPERTY_KEY = "impex.importConfig.cronJob.removeOnSuccess.enabled";
    public static final String IMPEX_IMPORT_FAIL_ON_ERROR_PROPERTY_KEY = "impex.importConfig.failOnError.enabled";


    ImpexImportUnitResult importImpexUnit(ImpexImportUnit paramImpexImportUnit);


    ImpexImportUnitResult importImpexUnitWithoutTracking(ImpexImportUnit paramImpexImportUnit);


    SequenceInputStream getStreamForImpexImportUnit(ImpexImportUnit paramImpexImportUnit) throws FileNotFoundException;
}
