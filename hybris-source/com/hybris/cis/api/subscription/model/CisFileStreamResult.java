package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.CisResult;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fileStream")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisFileStreamResult extends CisResult
{
    @XmlElement(name = "bytes")
    private byte[] bytes;
    @XmlElement(name = "mimeType")
    private String mimeType;
    @XmlElement(name = "fileName")
    private String fileName;


    public String getMimeType()
    {
        return this.mimeType;
    }


    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }


    public byte[] getBytes()
    {
        return this.bytes;
    }


    public void setBytes(byte[] bytes)
    {
        this.bytes = bytes;
    }


    public String getFileName()
    {
        return this.fileName;
    }


    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
