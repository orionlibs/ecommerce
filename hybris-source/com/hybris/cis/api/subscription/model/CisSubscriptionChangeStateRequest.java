package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.validation.XSSSafe;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionChangeStateRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionChangeStateRequest extends AbstractCisSubscriptionUpdateRequest
{
    @XmlElement(name = "newState")
    @XSSSafe
    private String newState;


    public String getNewState()
    {
        return this.newState;
    }


    public void setNewState(String newState)
    {
        this.newState = newState;
    }
}
