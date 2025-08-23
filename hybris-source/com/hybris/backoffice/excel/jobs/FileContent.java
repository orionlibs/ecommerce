package com.hybris.backoffice.excel.jobs;

public class FileContent
{
    private final byte[] data;
    private final String contentType;
    private final String name;


    public FileContent(byte[] data, String contentType, String name)
    {
        this.data = data;
        this.contentType = contentType;
        this.name = name;
    }


    public byte[] getData()
    {
        return this.data;
    }


    public String getContentType()
    {
        return this.contentType;
    }


    public String getName()
    {
        return this.name;
    }
}
