package com.hybris.cis.api.model;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "errors")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorsDto
{
    @XmlAttribute
    private String serviceId;
    @XmlElement(name = "error")
    private List<ErrorDto> errors;


    public ErrorsDto()
    {
    }


    public ErrorsDto(String serviceId, ErrorDto detail)
    {
        this(serviceId, Collections.singletonList(detail));
    }


    public ErrorsDto(String serviceId, List<ErrorDto> details)
    {
        this.errors = details;
        this.serviceId = serviceId;
    }


    public List<ErrorDto> getErrors()
    {
        return this.errors;
    }


    public void setErrors(List<ErrorDto> errors)
    {
        this.errors = errors;
    }


    public String getServiceId()
    {
        return this.serviceId;
    }


    public void setServiceId(String serviceId)
    {
        this.serviceId = serviceId;
    }


    public String toString()
    {
        StringBuilder sbr = new StringBuilder();
        sbr.append('[').append(this.serviceId).append("] Error details: ");
        List<ErrorDto> dtls = getErrors();
        if(dtls != null)
        {
            for(ErrorDto detail : dtls)
            {
                sbr.append("\n").append(detail.getMessage());
            }
        }
        return sbr.toString();
    }
}
