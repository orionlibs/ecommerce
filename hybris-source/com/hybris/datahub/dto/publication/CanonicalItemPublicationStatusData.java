package com.hybris.datahub.dto.publication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hybris.datahub.dto.item.ErrorData;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CanonicalItemPublicationStatusData
{
    private Long targetSystemPublicationId;
    private String targetSystemName;
    private String type;
    private Long canonicalItemId;
    private String integrationKey;
    private String status;
    private String statusDetail;
    private List<ErrorData> publicationErrors = new ArrayList<>();


    public Long getTargetSystemPublicationId()
    {
        return this.targetSystemPublicationId;
    }


    public void setTargetSystemPublicationId(Long targetSystemPublicationId)
    {
        this.targetSystemPublicationId = targetSystemPublicationId;
    }


    public Long getCanonicalItemId()
    {
        return this.canonicalItemId;
    }


    public void setCanonicalItemId(Long canonicalItemId)
    {
        this.canonicalItemId = canonicalItemId;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatusDetail()
    {
        return this.statusDetail;
    }


    public void setStatusDetail(String statusDetail)
    {
        this.statusDetail = statusDetail;
    }


    public List<ErrorData> getPublicationErrors()
    {
        return this.publicationErrors;
    }


    public void setPublicationErrors(List<ErrorData> publicationErrors)
    {
        this.publicationErrors = publicationErrors;
    }


    public String getTargetSystemName()
    {
        return this.targetSystemName;
    }


    public void setTargetSystemName(String targetSystemName)
    {
        this.targetSystemName = targetSystemName;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getIntegrationKey()
    {
        return this.integrationKey;
    }


    public void setIntegrationKey(String integrationKey)
    {
        this.integrationKey = integrationKey;
    }
}
