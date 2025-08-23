package com.hybris.cis.api.subscription.mock.dataimport;

import com.hybris.cis.api.subscription.mock.data.BillingInfoMock;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mockBillings")
public class BillingInfoMockWrapper
{
    @XmlElementWrapper(name = "billings")
    @XmlElement(name = "billing")
    private List<BillingInfoMock> billings = new ArrayList<>();


    public List<BillingInfoMock> getBillings()
    {
        return this.billings;
    }


    public void setBillings(List<BillingInfoMock> billings)
    {
        this.billings = billings;
    }
}
