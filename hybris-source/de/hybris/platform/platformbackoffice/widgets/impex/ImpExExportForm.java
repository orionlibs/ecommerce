package de.hybris.platform.platformbackoffice.widgets.impex;

import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;

public class ImpExExportForm
{
    private ImpExMediaModel impExMedia;
    private ImpExValidationModeEnum validationMode;
    private String scriptContent;
    private boolean asyncExecution = false;
    private char fieldSeparator = ';';
    private char escapeCharacter = '"';
    private char commentCharacter = '#';
    private EncodingEnum encoding = EncodingEnum.valueOf("utf-8");
    private boolean exportAsZip;
    private String exportStatus;
    private String exportLog;
    private String dataDownloadUrl;
    private ImpExMediaModel dataDownloadMedia;
    private String mediaDownloadUrl;
    private ImpExMediaModel mediaDownloadMedia;
    private CronJobModel exportCronJob;


    public boolean isValidForExport()
    {
        return (this.impExMedia != null);
    }


    public EncodingEnum getEncoding()
    {
        return this.encoding;
    }


    public void setEncoding(EncodingEnum encoding)
    {
        this.encoding = encoding;
    }


    public ImpExMediaModel getImpExMedia()
    {
        return this.impExMedia;
    }


    public void setImpExMedia(ImpExMediaModel impExMedia)
    {
        this.impExMedia = impExMedia;
    }


    public ImpExValidationModeEnum getValidationMode()
    {
        return this.validationMode;
    }


    public void setValidationMode(ImpExValidationModeEnum validationMode)
    {
        this.validationMode = validationMode;
    }


    public String getScriptContent()
    {
        return this.scriptContent;
    }


    public void setScriptContent(String scriptContent)
    {
        this.scriptContent = scriptContent;
    }


    public char getFieldSeparator()
    {
        return this.fieldSeparator;
    }


    public void setFieldSeparator(char fieldSeparator)
    {
        this.fieldSeparator = fieldSeparator;
    }


    public char getEscapeCharacter()
    {
        return this.escapeCharacter;
    }


    public void setEscapeCharacter(char escapeCharacter)
    {
        this.escapeCharacter = escapeCharacter;
    }


    public char getCommentCharacter()
    {
        return this.commentCharacter;
    }


    public void setCommentCharacter(char commentCharacter)
    {
        this.commentCharacter = commentCharacter;
    }


    public boolean isExportAsZip()
    {
        return this.exportAsZip;
    }


    public void setExportAsZip(boolean exportAsZip)
    {
        this.exportAsZip = exportAsZip;
    }


    public String getExportLog()
    {
        return this.exportLog;
    }


    public void setExportLog(String exportLog)
    {
        this.exportLog = exportLog;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public String getDataDownloadUrl()
    {
        return this.dataDownloadUrl;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public void setDataDownloadUrl(String dataDownloadUrl)
    {
        this.dataDownloadUrl = dataDownloadUrl;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public String getMediaDownloadUrl()
    {
        return this.mediaDownloadUrl;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public void setMediaDownloadUrl(String mediaDownloadUrl)
    {
        this.mediaDownloadUrl = mediaDownloadUrl;
    }


    public CronJobModel getExportCronJob()
    {
        return this.exportCronJob;
    }


    public void setExportCronJob(CronJobModel exportCronJob)
    {
        this.exportCronJob = exportCronJob;
    }


    public boolean isAsyncExecution()
    {
        return this.asyncExecution;
    }


    public void setAsyncExecution(boolean asyncExecution)
    {
        this.asyncExecution = asyncExecution;
    }


    public String getExportStatus()
    {
        return this.exportStatus;
    }


    public void setExportStatus(String exportStatus)
    {
        this.exportStatus = exportStatus;
    }


    public ImpExMediaModel getDataDownloadMedia()
    {
        return this.dataDownloadMedia;
    }


    public void setDataDownloadMedia(ImpExMediaModel dataDownloadMedia)
    {
        this.dataDownloadMedia = dataDownloadMedia;
    }


    public ImpExMediaModel getMediaDownloadMedia()
    {
        return this.mediaDownloadMedia;
    }


    public void setMediaDownloadMedia(ImpExMediaModel mediaDownloadMedia)
    {
        this.mediaDownloadMedia = mediaDownloadMedia;
    }
}
