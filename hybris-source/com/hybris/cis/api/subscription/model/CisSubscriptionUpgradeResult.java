package com.hybris.cis.api.subscription.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionUpgradeResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionUpgradeResult extends CisSubscriptionData
{
    @XmlElementWrapper(name = "futureBillings")
    @XmlElement(name = "futureBillings")
    private List<CisSubscriptionBillingInfo> futureBillings;


    public List<CisSubscriptionBillingInfo> getFutureBillings()
    {
        return this.futureBillings;
    }


    public void setFutureBillings(List<CisSubscriptionBillingInfo> futureBillings)
    {
        this.futureBillings = futureBillings;
    }
}
