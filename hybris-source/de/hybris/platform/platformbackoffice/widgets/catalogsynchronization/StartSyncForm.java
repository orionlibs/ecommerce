package de.hybris.platform.platformbackoffice.widgets.catalogsynchronization;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import java.util.List;

public class StartSyncForm
{
    private CatalogVersionModel sourceCatalogVersion;
    private List<SyncItemJobModel> syncItemJobs;
    private SyncItemJobModel selectedSyncItemJob;
    private boolean logToFile;
    private boolean logToDatabase;
    private boolean forceUpdate;
    private boolean keepCronJob;
    private boolean ignoreErrors;
    private JobLogLevel logLevelFile;
    private JobLogLevel logLevelDatabase;
    private boolean createSavedValues;
    private boolean runInBackground;


    public boolean isForceUpdate()
    {
        return this.forceUpdate;
    }


    public void setForceUpdate(boolean forceUpdate)
    {
        this.forceUpdate = forceUpdate;
    }


    public boolean isKeepCronJob()
    {
        return this.keepCronJob;
    }


    public void setKeepCronJob(boolean keepCronJob)
    {
        this.keepCronJob = keepCronJob;
    }


    public boolean isIgnoreErrors()
    {
        return this.ignoreErrors;
    }


    public void setIgnoreErrors(boolean ignoreErrors)
    {
        this.ignoreErrors = ignoreErrors;
    }


    public JobLogLevel getLogLevelFile()
    {
        return this.logLevelFile;
    }


    public void setLogLevelFile(JobLogLevel logLevelFile)
    {
        this.logLevelFile = logLevelFile;
    }


    public JobLogLevel getLogLevelDatabase()
    {
        return this.logLevelDatabase;
    }


    public void setLogLevelDatabase(JobLogLevel logLevelDatabase)
    {
        this.logLevelDatabase = logLevelDatabase;
    }


    public boolean isCreateSavedValues()
    {
        return this.createSavedValues;
    }


    public void setCreateSavedValues(boolean createSavedValues)
    {
        this.createSavedValues = createSavedValues;
    }


    public boolean isRunInBackground()
    {
        return this.runInBackground;
    }


    public void setRunInBackground(boolean runInBackground)
    {
        this.runInBackground = runInBackground;
    }


    public CatalogVersionModel getSourceCatalogVersion()
    {
        return this.sourceCatalogVersion;
    }


    public void setSourceCatalogVersion(CatalogVersionModel sourceCatalogVersion)
    {
        this.sourceCatalogVersion = sourceCatalogVersion;
    }


    public boolean isLogToFile()
    {
        return this.logToFile;
    }


    public void setLogToFile(boolean logToFile)
    {
        this.logToFile = logToFile;
    }


    public boolean isLogToDatabase()
    {
        return this.logToDatabase;
    }


    public void setLogToDatabase(boolean logToDatabase)
    {
        this.logToDatabase = logToDatabase;
    }


    public List<SyncItemJobModel> getSyncItemJobs()
    {
        return this.syncItemJobs;
    }


    public void setSyncItemJobs(List<SyncItemJobModel> syncItemJobs)
    {
        this.syncItemJobs = syncItemJobs;
    }


    public SyncItemJobModel getSelectedSyncItemJob()
    {
        return this.selectedSyncItemJob;
    }


    public void setSelectedSyncItemJob(SyncItemJobModel selectedSyncItemJob)
    {
        this.selectedSyncItemJob = selectedSyncItemJob;
    }
}
