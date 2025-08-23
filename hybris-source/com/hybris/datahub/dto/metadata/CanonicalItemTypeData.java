package com.hybris.datahub.dto.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CanonicalItemType")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CanonicalItemTypeData extends ItemTypeData
{
    private String documentId;


    public String getDocumentId()
    {
        return this.documentId;
    }


    public void setDocumentId(String documentId)
    {
        this.documentId = documentId;
    }


    public String toString()
    {
        return "CanonicalItemTypeData{name='" + getName() + "', description='" +
                        getDescription() + "', documentId='" +
                        getDocumentId() + "'}";
    }
}
