package com.hybris.cis.api.subscription.mock.dataimport;

import com.hybris.cis.api.subscription.mock.data.PaymentMethodMockData;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mockPaymentMethods")
public class PaymentMethodMockWrapper
{
    @XmlElementWrapper(name = "paymentMethods")
    @XmlElement(name = "paymentMethodMockData")
    private List<PaymentMethodMockData> paymentMethods = new ArrayList<>();


    public List<PaymentMethodMockData> getPaymentMethods()
    {
        return this.paymentMethods;
    }


    public void setPaymentMethods(List<PaymentMethodMockData> paymentMethods)
    {
        this.paymentMethods = paymentMethods;
    }
}
