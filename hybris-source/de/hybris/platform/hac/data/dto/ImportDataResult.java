package de.hybris.platform.hac.data.dto;

public class ImportDataResult
{
    private boolean successs;
    private boolean unresolvedLines;
    private String unresolvedData;
    private String logText;


    public boolean isSuccesss()
    {
        return this.successs;
    }


    public void setSuccesss(boolean successs)
    {
        this.successs = successs;
    }


    public boolean isUnresolvedLines()
    {
        return this.unresolvedLines;
    }


    public void setUnresolvedLines(boolean unresolvedLines)
    {
        this.unresolvedLines = unresolvedLines;
    }


    public String getUnresolvedData()
    {
        return this.unresolvedData;
    }


    public void setUnresolvedData(String unresolvedData)
    {
        this.unresolvedData = unresolvedData;
    }


    public String getLogText()
    {
        return this.logText;
    }


    public void setLogText(String logText)
    {
        this.logText = logText;
    }
}
