package de.hybris.platform.platformbackoffice.widgets.impex;

import com.hybris.cockpitng.util.BackofficeSpringUtil;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.impl.MediaBasedImpExResource;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ThreadUtilities;
import java.io.File;
import java.util.Locale;

public class ImpExImportForm
{
    private File file;
    private ImpExFileUploadResult result = new ImpExFileUploadResult();
    private ImpExFileUploadResult mediaZipResult = new ImpExFileUploadResult();
    private ImpExMediaModel impExMedia;
    private String filename;
    private boolean finished;
    private String importLog;
    private ImpExMediaModel mediaZip;
    private boolean unzipMediaZip = false;
    private boolean strictImportMode;
    private boolean distributedImpex = false;
    private ErrorMode errorMode;
    private int processNumber;
    private String zipImport;
    private LanguageModel locale;
    private ImpExValidationModeEnum validationMode;
    private boolean logAuditTrail = false;
    private boolean codeExecutionAllowed = true;
    private boolean referencedFileContainHeaders = false;
    private boolean codeExecutionFromReferencedFiles = false;
    private boolean asyncExecution = true;
    private boolean sldForData = !Config.getBoolean("persistence.legacy.mode", false);
    private JobLogLevel logLevel;
    private String nodeGroup;
    private String status;
    private ImpExImportCronJobModel importExecutionCronJob;


    public ImpExImportForm()
    {
        ConfigurationService cfgService = (ConfigurationService)BackofficeSpringUtil.getBean("configurationService");
        String numberExpression = cfgService.getConfiguration().getString("impex.import.workers", "4");
        this.processNumber = ThreadUtilities.getNumberOfThreadsFromExpression(numberExpression, 4);
        this.errorMode = ErrorMode.FAIL;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public ImpExImportCronJobModel getImportExecutionCronJob()
    {
        return this.importExecutionCronJob;
    }


    public void setImportExecutionCronJob(ImpExImportCronJobModel importExecutionCronJob)
    {
        this.importExecutionCronJob = importExecutionCronJob;
    }


    public ImpExFileUploadResult getResult()
    {
        return this.result;
    }


    public void setResult(ImpExFileUploadResult result)
    {
        this.result = result;
    }


    public boolean isValidForStart()
    {
        return (this.impExMedia != null);
    }


    public File getFile()
    {
        return this.file;
    }


    public void setFile(File file)
    {
        this.file = file;
    }


    public ImpExMediaModel getImpExMedia()
    {
        return this.impExMedia;
    }


    public void setImpExMedia(ImpExMediaModel impExMedia)
    {
        this.impExMedia = impExMedia;
    }


    public String getImportLog()
    {
        return this.importLog;
    }


    public void setImportLog(String importLog)
    {
        this.importLog = importLog;
    }


    public String getFilename()
    {
        return this.filename;
    }


    public void setFilename(String filename)
    {
        this.filename = filename;
    }


    public boolean isFinished()
    {
        return this.finished;
    }


    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }


    public LanguageModel getLocale()
    {
        return this.locale;
    }


    public void setLocale(LanguageModel locale)
    {
        this.locale = locale;
    }


    public ImpExValidationModeEnum getValidationMode()
    {
        return this.validationMode;
    }


    public void setValidationMode(ImpExValidationModeEnum validationMode)
    {
        this.validationMode = validationMode;
    }


    public String getZipImport()
    {
        return this.zipImport;
    }


    public void setZipImport(String zipImport)
    {
        this.zipImport = zipImport;
    }


    public ErrorMode getErrorMode()
    {
        return this.errorMode;
    }


    public void setErrorMode(ErrorMode errorMode)
    {
        this.errorMode = errorMode;
    }


    public int getProcessNumber()
    {
        return this.processNumber;
    }


    public void setProcessNumber(int processNumber)
    {
        this.processNumber = processNumber;
    }


    public boolean isLogAuditTrail()
    {
        return this.logAuditTrail;
    }


    public void setLogAuditTrail(boolean logAuditTrail)
    {
        this.logAuditTrail = logAuditTrail;
    }


    public boolean isCodeExecutionAllowed()
    {
        return this.codeExecutionAllowed;
    }


    public void setCodeExecutionAllowed(boolean codeExecutionAllowed)
    {
        this.codeExecutionAllowed = codeExecutionAllowed;
    }


    public boolean isReferencedFileContainHeaders()
    {
        return this.referencedFileContainHeaders;
    }


    public void setReferencedFileContainHeaders(boolean referencedFileContainHeaders)
    {
        this.referencedFileContainHeaders = referencedFileContainHeaders;
    }


    public boolean isCodeExecutionFromReferencedFiles()
    {
        return this.codeExecutionFromReferencedFiles;
    }


    public void setCodeExecutionFromReferencedFiles(boolean codeExecutionFromReferencedFiles)
    {
        this.codeExecutionFromReferencedFiles = codeExecutionFromReferencedFiles;
    }


    public ImpExMediaModel getMediaZip()
    {
        return this.mediaZip;
    }


    public void setMediaZip(ImpExMediaModel mediaZip)
    {
        this.mediaZip = mediaZip;
    }


    public boolean isUnzipMediaZip()
    {
        return this.unzipMediaZip;
    }


    public void setUnzipMediaZip(boolean unzipMediaZip)
    {
        this.unzipMediaZip = unzipMediaZip;
    }


    public void setStrictImportMode(boolean strictImportMode)
    {
        this.strictImportMode = strictImportMode;
    }


    public boolean isStrictImportMode()
    {
        return this.strictImportMode;
    }


    public ImpExFileUploadResult getMediaZipResult()
    {
        return this.mediaZipResult;
    }


    public void setMediaZipResult(ImpExFileUploadResult mediaZipResult)
    {
        this.mediaZipResult = mediaZipResult;
    }


    public boolean isAsyncExecution()
    {
        return this.asyncExecution;
    }


    public void setAsyncExecution(boolean asyncExecution)
    {
        this.asyncExecution = asyncExecution;
    }


    public boolean isDistributedImpex()
    {
        return this.distributedImpex;
    }


    public void setDistributedImpex(boolean distributedImpex)
    {
        this.distributedImpex = distributedImpex;
    }


    public boolean isSldForData()
    {
        return this.sldForData;
    }


    public void setSldForData(boolean sldForData)
    {
        this.sldForData = sldForData;
    }


    public JobLogLevel getLogLevel()
    {
        return this.logLevel;
    }


    public void setLogLevel(JobLogLevel logLevel)
    {
        this.logLevel = logLevel;
    }


    public String getNodeGroup()
    {
        return this.nodeGroup;
    }


    public void setNodeGroup(String nodeGroup)
    {
        this.nodeGroup = nodeGroup;
    }


    public void mapImportConfig(ImportConfig importConfig)
    {
        if(getLocale() != null)
        {
            importConfig.setLocale(new Locale(getLocale().getIsocode()));
        }
        if(isStrictImportMode())
        {
            importConfig.setValidationMode(ImportConfig.ValidationMode.STRICT);
        }
        else
        {
            importConfig.setValidationMode(ImportConfig.ValidationMode.RELAXED);
        }
        ErrorMode myEnum = getErrorMode();
        if(myEnum == ErrorMode.FAIL)
        {
            importConfig.setFailOnError(false);
        }
        else
        {
            importConfig.setFailOnError(true);
        }
        importConfig.setMainScriptWithinArchive(getZipImport());
        importConfig.setMaxThreads(getProcessNumber());
        importConfig.setEnableCodeExecution(Boolean.valueOf(isCodeExecutionAllowed()));
        importConfig.setHmcSavedValuesEnabled(isLogAuditTrail());
        importConfig.setSynchronous(!this.asyncExecution);
        if(isDistributedImpex())
        {
            importConfig.setDistributedImpexEnabled(true);
            importConfig.setSldForData(Boolean.valueOf(isSldForData()));
            importConfig.setNodeGroup(getNodeGroup());
            importConfig.setDistributedImpexLogLevel(getLogLevel());
        }
        MediaBasedImpExResource mediaBasedImpExResource = new MediaBasedImpExResource(getMediaZip());
        importConfig.setMediaArchive((ImpExResource)mediaBasedImpExResource);
    }
}
