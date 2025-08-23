package com.hybris.datahub.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorData
{
    private Long canonicalItemId;
    private String integrationKey;
    private String itemType;
    private String code;
    private String message;


    public Long getCanonicalItemId()
    {
        return this.canonicalItemId;
    }


    public void setCanonicalItemId(Long itemId)
    {
        this.canonicalItemId = itemId;
    }


    public String getIntegrationKey()
    {
        return this.integrationKey;
    }


    public void setIntegrationKey(String integrationKey)
    {
        this.integrationKey = integrationKey;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public void setItemType(String itemType)
    {
        this.itemType = itemType;
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


    public String toString()
    {
        return "ErrorData{canonicalItemId=" + this.canonicalItemId + ", integrationKey='" + this.integrationKey + "', itemType='" + this.itemType + "', code='" + this.code + "', message='" + this.message + "'}";
    }
}
