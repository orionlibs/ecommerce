package de.hybris.platform.hac.data.dto;

public class PkData
{
    private String pkString;
    private boolean counterBased;
    private String pkAsHex;
    private String pkAsBinary;
    private int pkTypeCode;
    private byte pkClusterId;
    private long pkCreationTime;
    private String pkCreationDate;
    private String[] bits;
    private Exception possibleException;
    private byte pkMilliCnt;
    private String pkComposedTypeCode;


    public String getPkComposedTypeCode()
    {
        return this.pkComposedTypeCode;
    }


    public void setPkComposedTypeCode(String pkComposedTypeCode)
    {
        this.pkComposedTypeCode = pkComposedTypeCode;
    }


    public String[] getBits()
    {
        return this.bits;
    }


    public String getPkAsBinary()
    {
        return this.pkAsBinary;
    }


    public String getPkAsHex()
    {
        return this.pkAsHex;
    }


    public byte getPkClusterId()
    {
        return this.pkClusterId;
    }


    public String getPkCreationDate()
    {
        return this.pkCreationDate;
    }


    public long getPkCreationTime()
    {
        return this.pkCreationTime;
    }


    public byte getPkMilliCnt()
    {
        return this.pkMilliCnt;
    }


    public String getPkString()
    {
        return this.pkString;
    }


    public int getPkTypeCode()
    {
        return this.pkTypeCode;
    }


    public Exception getPossibleException()
    {
        return this.possibleException;
    }


    public boolean isCounterBased()
    {
        return this.counterBased;
    }


    public void setBits(String[] bits)
    {
        this.bits = bits;
    }


    public void setCounterBased(boolean counterBased)
    {
        this.counterBased = counterBased;
    }


    public void setPkAsBinary(String pkAsBinary)
    {
        this.pkAsBinary = pkAsBinary;
    }


    public void setPkAsHex(String pkAsHex)
    {
        this.pkAsHex = pkAsHex;
    }


    public void setPkClusterId(byte pkClusterId)
    {
        this.pkClusterId = pkClusterId;
    }


    public void setPkCreationDate(String pkCreationDate)
    {
        this.pkCreationDate = pkCreationDate;
    }


    public void setPkCreationTime(long pkCreationTime)
    {
        this.pkCreationTime = pkCreationTime;
    }


    public void setPkMilliCnt(byte pkMilliCnt)
    {
        this.pkMilliCnt = pkMilliCnt;
    }


    public void setPkString(String pkString)
    {
        this.pkString = pkString;
    }


    public void setPkTypeCode(int pkTypeCode)
    {
        this.pkTypeCode = pkTypeCode;
    }


    public void setPossibleException(Exception possibleException)
    {
        this.possibleException = possibleException;
    }
}
