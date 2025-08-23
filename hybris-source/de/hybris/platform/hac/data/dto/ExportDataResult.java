package de.hybris.platform.hac.data.dto;

public class ExportDataResult
{
    private boolean success;
    private String downloadUrl;
    private String exportDataName;


    public String getDownloadUrl()
    {
        return this.downloadUrl;
    }


    public void setDownloadUrl(String downloadUrl)
    {
        this.downloadUrl = downloadUrl;
    }


    public String getExportDataName()
    {
        return this.exportDataName;
    }


    public void setExportDataName(String exportDataName)
    {
        this.exportDataName = exportDataName;
    }


    public boolean isSuccess()
    {
        return this.success;
    }


    public void setSuccess(boolean success)
    {
        this.success = success;
    }
}
