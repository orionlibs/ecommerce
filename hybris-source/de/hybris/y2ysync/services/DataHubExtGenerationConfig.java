package de.hybris.y2ysync.services;

public class DataHubExtGenerationConfig
{
    private boolean generateRawItems;
    private boolean generateCanonicalItems;
    private boolean generateTargetItems;
    private boolean prettyFormat;
    private String targetType;
    private String targetExportURL;
    private String targetUserName;
    private String targetPassword;
    private String targetExportCodes;


    public boolean isGenerateRawItems()
    {
        return this.generateRawItems;
    }


    public void setGenerateRawItems(boolean generateRawItems)
    {
        this.generateRawItems = generateRawItems;
    }


    public boolean isGenerateCanonicalItems()
    {
        return this.generateCanonicalItems;
    }


    public void setGenerateCanonicalItems(boolean generateCanonicalItems)
    {
        this.generateCanonicalItems = generateCanonicalItems;
    }


    public boolean isGenerateTargetItems()
    {
        return this.generateTargetItems;
    }


    public void setGenerateTargetItems(boolean generateTargetItems)
    {
        this.generateTargetItems = generateTargetItems;
    }


    public boolean isPrettyFormat()
    {
        return this.prettyFormat;
    }


    public void setPrettyFormat(boolean prettyFormat)
    {
        this.prettyFormat = prettyFormat;
    }


    public String getTargetType()
    {
        return this.targetType;
    }


    public void setTargetType(String targetType)
    {
        this.targetType = targetType;
    }


    public String getTargetExportURL()
    {
        return this.targetExportURL;
    }


    public void setTargetExportURL(String targetExportURL)
    {
        this.targetExportURL = targetExportURL;
    }


    public String getTargetUserName()
    {
        return this.targetUserName;
    }


    public void setTargetUserName(String targetUserName)
    {
        this.targetUserName = targetUserName;
    }


    public String getTargetPassword()
    {
        return this.targetPassword;
    }


    public void setTargetPassword(String targetPassword)
    {
        this.targetPassword = targetPassword;
    }


    public String getTargetExportCodes()
    {
        return this.targetExportCodes;
    }


    public void setTargetExportCodes(String targetExportCodes)
    {
        this.targetExportCodes = targetExportCodes;
    }


    public String toString()
    {
        return "DataHubExtGenerationConfig{generateRawItems=" + this.generateRawItems + ", generateCanonicalItems=" + this.generateCanonicalItems + ", generateTargetItems=" + this.generateTargetItems + ", prettyFormat=" + this.prettyFormat + ", targetType='" + this.targetType
                        + "', targetExportURL='" + this.targetExportURL + "', targetUserName='" + this.targetUserName + "', targetPassword='" + this.targetPassword + "', targetExportCodes='" + this.targetExportCodes + "'}";
    }
}
