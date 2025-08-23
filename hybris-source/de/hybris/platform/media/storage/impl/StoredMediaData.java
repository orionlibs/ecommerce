package de.hybris.platform.media.storage.impl;

public class StoredMediaData
{
    private final String location;
    private final Long size;
    private final String hashForLocation;
    private final String mime;
    private Long dataPk;


    public StoredMediaData(String location, String hashForLocation, long size, String mime)
    {
        this.location = location;
        this.hashForLocation = hashForLocation;
        this.mime = mime;
        this.size = Long.valueOf(size);
    }


    public String getLocation()
    {
        return this.location;
    }


    public Long getSize()
    {
        return this.size;
    }


    public String getHashForLocation()
    {
        return this.hashForLocation;
    }


    public String getMime()
    {
        return this.mime;
    }


    public Long getDataPk()
    {
        return this.dataPk;
    }


    public void setDataPk(Long dataPk)
    {
        this.dataPk = dataPk;
    }


    public String toString()
    {
        return "StoredMediaData{hashForLocation='" + this.hashForLocation + "', location='" + this.location + "', size=" + this.size + ", mime='" + this.mime + "'}";
    }
}
