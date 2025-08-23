package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.validation.XSSSafe;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionSessionFinalization")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionSessionFinalizeRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "authorizationRequestId")
    @XSSSafe
    private String authorizationRequestId;
    @XmlElement(name = "authorizationRequestToken")
    @XSSSafe
    private String authorizationRequestToken;


    public String getAuthorizationRequestId()
    {
        return this.authorizationRequestId;
    }


    public void setAuthorizationRequestId(String authorizationRequestId)
    {
        this.authorizationRequestId = authorizationRequestId;
    }


    public String getAuthorizationRequestToken()
    {
        return this.authorizationRequestToken;
    }


    public void setAuthorizationRequestToken(String authorizationRequestToken)
    {
        this.authorizationRequestToken = authorizationRequestToken;
    }


    public String toString()
    {
        return "CisSubscriptionSessionFinalizeRequest [authorizationRequestId=" + this.authorizationRequestId + ", authorizationRequestToken=" + this.authorizationRequestToken + "]";
    }
}
