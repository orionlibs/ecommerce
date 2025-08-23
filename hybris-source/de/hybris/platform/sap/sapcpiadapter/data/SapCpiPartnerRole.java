package de.hybris.platform.sap.sapcpiadapter.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sapCpiPartnerRole")
public class SapCpiPartnerRole implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String partnerRoleCode;
    private String partnerId;
    private String documentAddressId;
    private String entryNumber;


    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }


    public String getOrderId()
    {
        return this.orderId;
    }


    public void setPartnerRoleCode(String partnerRoleCode)
    {
        this.partnerRoleCode = partnerRoleCode;
    }


    public String getPartnerRoleCode()
    {
        return this.partnerRoleCode;
    }


    public void setPartnerId(String partnerId)
    {
        this.partnerId = partnerId;
    }


    public String getPartnerId()
    {
        return this.partnerId;
    }


    public void setDocumentAddressId(String documentAddressId)
    {
        this.documentAddressId = documentAddressId;
    }


    public String getDocumentAddressId()
    {
        return this.documentAddressId;
    }


    public void setEntryNumber(String entryNumber)
    {
        this.entryNumber = entryNumber;
    }


    public String getEntryNumber()
    {
        return this.entryNumber;
    }
}
