package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.AnnotationHashMap;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionRequest
{
    @XmlElement(name = "vendorParameters")
    @Valid
    private AnnotationHashMap parameters = new AnnotationHashMap();


    public AnnotationHashMap getParameters()
    {
        return this.parameters;
    }


    public void setParameters(AnnotationHashMap parameters)
    {
        this.parameters = parameters;
    }
}
