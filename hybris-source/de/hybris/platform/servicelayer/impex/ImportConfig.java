package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.util.Utilities;
import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Locale;

public class ImportConfig
{
    private ImpExResource script = null;
    private Collection<ImpExResource> referencedData = null;
    private ImpExResource mediaArchive = null;
    private int maxThreads = -1;
    private boolean removeOnSuccess = true;
    private boolean hmcSavedValuesEnabled = false;
    private boolean synchronous = true;
    private Boolean legacyMode = null;
    private Locale locale = null;
    private String mainScriptWithinArchive;
    private ValidationMode validationMode = ValidationMode.STRICT;
    private boolean dumpingEnabled = true;
    private boolean failOnError = false;
    private Boolean enableCodeExecution;
    private Boolean distributedImpexEnabled;
    private Boolean removeOnSuccessForDistributedImpex;
    private String distributedImpexProcessCode;
    private JobLogLevel distributedImpexLogLevel;
    private Boolean sldForData = null;
    private String nodeGroup;


    public void setDistributedImpexLogLevel(JobLogLevel level)
    {
        this.distributedImpexLogLevel = level;
    }


    public JobLogLevel getDistributedImpexLogLevel()
    {
        return this.distributedImpexLogLevel;
    }


    public void setDistributedImpexEnabled(boolean enabled)
    {
        this.distributedImpexEnabled = Boolean.valueOf(enabled);
    }


    public Boolean isDistributedImpexEnabled()
    {
        return this.distributedImpexEnabled;
    }


    public void setRemoveOnSuccessForDistributedImpex(boolean removeOnSuccessForDistributedImpex)
    {
        this.removeOnSuccessForDistributedImpex = Boolean.valueOf(removeOnSuccessForDistributedImpex);
    }


    public Boolean isRemoveOnSuccessForDistributedImpex()
    {
        return this.removeOnSuccessForDistributedImpex;
    }


    public String getDistributedImpexProcessCode()
    {
        return this.distributedImpexProcessCode;
    }


    public void setDistributedImpexProcessCode(String distributedImpexProcessCode)
    {
        this.distributedImpexProcessCode = distributedImpexProcessCode;
    }


    public ImpExResource getScript()
    {
        return this.script;
    }


    public void setScript(ImpExResource script)
    {
        this.script = script;
    }


    public void setScript(String script)
    {
        StreamBasedImpExResource resource = new StreamBasedImpExResource(new ByteArrayInputStream(script.getBytes()), "UTF-8");
        setScript((ImpExResource)resource);
    }


    public Collection<ImpExResource> getReferencedData()
    {
        return this.referencedData;
    }


    public void setReferencedData(Collection<ImpExResource> referencedData)
    {
        this.referencedData = referencedData;
    }


    public ImpExResource getMediaArchive()
    {
        return this.mediaArchive;
    }


    public void setMediaArchive(ImpExResource mediaArchive)
    {
        this.mediaArchive = mediaArchive;
    }


    public int getMaxThreads()
    {
        return this.maxThreads;
    }


    public boolean isRemoveOnSuccess()
    {
        return this.removeOnSuccess;
    }


    public boolean isHmcSavedValuesEnabled()
    {
        return this.hmcSavedValuesEnabled;
    }


    public Locale getLocale()
    {
        if(this.locale == null)
        {
            this.locale = Utilities.getDefaultLocale();
        }
        return this.locale;
    }


    public ValidationMode getValidationMode()
    {
        return this.validationMode;
    }


    public boolean isDumpingEnabled()
    {
        return this.dumpingEnabled;
    }


    public boolean isFailOnError()
    {
        return this.failOnError;
    }


    public boolean isSynchronous()
    {
        return this.synchronous;
    }


    public Boolean isSldForData()
    {
        return this.sldForData;
    }


    public void setSldForData(Boolean sldForData)
    {
        this.sldForData = sldForData;
    }


    public void setMaxThreads(int maxThreads)
    {
        this.maxThreads = maxThreads;
    }


    public void setRemoveOnSuccess(boolean removeOnSuccess)
    {
        this.removeOnSuccess = removeOnSuccess;
    }


    public void setHmcSavedValuesEnabled(boolean hmcSavedValuesEnabled)
    {
        this.hmcSavedValuesEnabled = hmcSavedValuesEnabled;
    }


    public void setSynchronous(boolean synchronous)
    {
        this.synchronous = synchronous;
    }


    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }


    public void setValidationMode(ValidationMode validationMode)
    {
        this.validationMode = validationMode;
    }


    public void setDumpingEnabled(boolean dumpingEnabled)
    {
        this.dumpingEnabled = dumpingEnabled;
    }


    public void setFailOnError(boolean failOnError)
    {
        this.failOnError = failOnError;
    }


    public String getMainScriptWithinArchive()
    {
        return this.mainScriptWithinArchive;
    }


    public void setMainScriptWithinArchive(String mainScriptWithinArchive)
    {
        this.mainScriptWithinArchive = mainScriptWithinArchive;
    }


    public Boolean isLegacyMode()
    {
        return this.legacyMode;
    }


    public void setLegacyMode(Boolean legacyMode)
    {
        this.legacyMode = legacyMode;
    }


    public Boolean getEnableCodeExecution()
    {
        return this.enableCodeExecution;
    }


    public void setEnableCodeExecution(Boolean enableCodeExecution)
    {
        this.enableCodeExecution = enableCodeExecution;
    }


    public String getNodeGroup()
    {
        return this.nodeGroup;
    }


    public void setNodeGroup(String nodeGroup)
    {
        this.nodeGroup = nodeGroup;
    }
}
