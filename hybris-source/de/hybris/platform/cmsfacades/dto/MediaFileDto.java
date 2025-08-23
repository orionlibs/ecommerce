package de.hybris.platform.cmsfacades.dto;

import java.io.InputStream;
import java.io.Serializable;

public class MediaFileDto implements Serializable
{
    private static final long serialVersionUID = 1L;
    private InputStream inputStream;
    private String mime;
    private String name;
    private Long size;


    public void setInputStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }


    public InputStream getInputStream()
    {
        return this.inputStream;
    }


    public void setMime(String mime)
    {
        this.mime = mime;
    }


    public String getMime()
    {
        return this.mime;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setSize(Long size)
    {
        this.size = size;
    }


    public Long getSize()
    {
        return this.size;
    }
}
