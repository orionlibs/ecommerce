package com.hybris.cis.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CisResult implements Identifiable
{
    @XmlElement(name = "decision")
    private CisDecision decision;
    @XmlAttribute(name = "clientRefId")
    private String clientRefId;
    @XmlAttribute(name = "vendorId")
    private String vendorId;
    @XmlAttribute(name = "vendorReasonCode")
    private String vendorReasonCode;
    @XmlAttribute(name = "vendorStatusCode")
    private String vendorStatusCode;
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "href")
    private String href;
    @XmlElement(name = "vendorResponses")
    private AnnotationHashMap vendorResponses;


    public CisResult()
    {
        this(null);
    }


    public CisResult(CisDecision decision)
    {
        this.decision = decision;
    }


    public CisResult(CisDecision decision, String id)
    {
        this.decision = decision;
        this.id = id;
    }


    public CisDecision getDecision()
    {
        return this.decision;
    }


    public void setDecision(CisDecision decision)
    {
        this.decision = decision;
    }


    public String getId()
    {
        return this.id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getVendorReasonCode()
    {
        return this.vendorReasonCode;
    }


    public void setVendorReasonCode(String vendorReasonCode)
    {
        this.vendorReasonCode = vendorReasonCode;
    }


    public String getVendorStatusCode()
    {
        return this.vendorStatusCode;
    }


    public void setVendorStatusCode(String vendorStatusCode)
    {
        this.vendorStatusCode = vendorStatusCode;
    }


    public String getClientRefId()
    {
        return this.clientRefId;
    }


    public void setClientRefId(String clientRefId)
    {
        this.clientRefId = clientRefId;
    }


    public String getVendorId()
    {
        return this.vendorId;
    }


    public void setVendorId(String vendorId)
    {
        this.vendorId = vendorId;
    }


    public String getHref()
    {
        return this.href;
    }


    public void setHref(String href)
    {
        this.href = href;
    }


    public AnnotationHashMap getVendorResponses()
    {
        return this.vendorResponses;
    }


    public void setVendorResponses(AnnotationHashMap vendorResponses)
    {
        this.vendorResponses = vendorResponses;
    }
}
