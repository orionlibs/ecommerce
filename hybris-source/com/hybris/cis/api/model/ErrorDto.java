package com.hybris.cis.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorDto
{
    @XmlAttribute
    private String code;
    @XmlValue
    private String message;


    public ErrorDto()
    {
    }


    public ErrorDto(String code, String message)
    {
        this.code = code;
        this.message = message;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getMessage()
    {
        return this.message;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }
}
