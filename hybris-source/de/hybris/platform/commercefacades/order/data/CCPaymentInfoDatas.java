package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import java.util.List;

public class CCPaymentInfoDatas implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CCPaymentInfoData> paymentInfos;


    public void setPaymentInfos(List<CCPaymentInfoData> paymentInfos)
    {
        this.paymentInfos = paymentInfos;
    }


    public List<CCPaymentInfoData> getPaymentInfos()
    {
        return this.paymentInfos;
    }
}
