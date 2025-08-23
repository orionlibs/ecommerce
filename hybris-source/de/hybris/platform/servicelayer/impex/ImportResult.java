package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;

public interface ImportResult
{
    boolean isSuccessful();


    boolean isError();


    boolean isRunning();


    boolean isFinished();


    boolean hasUnresolvedLines();


    ImpExMediaModel getUnresolvedLines();


    ImpExImportCronJobModel getCronJob();
}
