/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultfileupload;

public class FileUploadResult
{
    private byte[] data;
    private String contentType;
    private String format;
    private String name;


    public byte[] getData()
    {
        return data;
    }


    public void setData(final byte[] data)
    {
        this.data = data;
    }


    public String getContentType()
    {
        return contentType;
    }


    public void setContentType(final String contentType)
    {
        this.contentType = contentType;
    }


    public String getFormat()
    {
        return format;
    }


    public void setFormat(final String format)
    {
        this.format = format;
    }


    public String getName()
    {
        return name;
    }


    public void setName(final String name)
    {
        this.name = name;
    }
}
