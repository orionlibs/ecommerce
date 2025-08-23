package de.hybris.platform.hac.data.form;

import de.hybris.platform.impex.enums.ImpExValidationModeEnum;

public class ImpexContentFormData
{
    private String encoding;
    private Integer maxThreads;
    private String scriptContent;
    private ImpExValidationModeEnum validationEnum;
    private boolean legacyMode;
    private boolean enableCodeExecution;
    private boolean distributedMode;
    private boolean sldEnabled;


    public String getEncoding()
    {
        return this.encoding;
    }


    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }


    public Integer getMaxThreads()
    {
        return this.maxThreads;
    }


    public void setMaxThreads(Integer maxThreads)
    {
        this.maxThreads = maxThreads;
    }


    public String getScriptContent()
    {
        return this.scriptContent;
    }


    public void setScriptContent(String scriptContent)
    {
        this.scriptContent = scriptContent;
    }


    public ImpExValidationModeEnum getValidationEnum()
    {
        return this.validationEnum;
    }


    public void setValidationEnum(ImpExValidationModeEnum validationEnum)
    {
        this.validationEnum = validationEnum;
    }


    public boolean isLegacyMode()
    {
        return this.legacyMode;
    }


    public void setLegacyMode(boolean legacyMode)
    {
        this.legacyMode = legacyMode;
    }


    public boolean isEnableCodeExecution()
    {
        return this.enableCodeExecution;
    }


    public void setEnableCodeExecution(boolean enableCodeExecution)
    {
        this.enableCodeExecution = enableCodeExecution;
    }


    public boolean isDistributedMode()
    {
        return this.distributedMode;
    }


    public void setDistributedMode(boolean distributedMode)
    {
        this.distributedMode = distributedMode;
    }


    public boolean isSldEnabled()
    {
        return this.sldEnabled;
    }


    public void setSldEnabled(boolean sldEnabled)
    {
        this.sldEnabled = sldEnabled;
    }
}
