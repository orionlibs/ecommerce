package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.CisResult;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "paymentMethodResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisPaymentMethodResult extends CisResult
{
    @XmlElement(name = "paymentMethod")
    private CisPaymentMethod paymentMethod;


    public CisPaymentMethod getPaymentMethod()
    {
        return this.paymentMethod;
    }


    public void setPaymentMethod(CisPaymentMethod paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }
}
