package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.CisResult;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "billingActivityList")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionBillingActivityList extends CisResult
{
    @XmlElement(name = "merchantSubscriptionId")
    private String merchantSubscriptionId;
    @XmlElementWrapper(name = "billings")
    @XmlElement(name = "billing")
    private List<CisSubscriptionBillingInfo> billings;


    public String getMerchantSubscriptionId()
    {
        return this.merchantSubscriptionId;
    }


    public void setMerchantSubscriptionId(String merchantSubscriptionId)
    {
        this.merchantSubscriptionId = merchantSubscriptionId;
    }


    public List<CisSubscriptionBillingInfo> getBillings()
    {
        return this.billings;
    }


    public void setBillings(List<CisSubscriptionBillingInfo> billings)
    {
        this.billings = billings;
    }
}
