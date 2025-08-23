package de.hybris.platform.processing;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.cronjob.jalo.CronJobManager;

@SystemSetup(extension = "processing")
public class ResetCronJobHistory
{
    @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
    public void resetHistoryStatus()
    {
        CronJobManager.getInstance().findAndFixAllCronJobHistoryEntries();
    }
}
