package de.hybris.platform.b2bwebservicescommons.dto.order;

import java.io.Serializable;
import java.util.List;

public class B2BPaymentTypeListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<B2BPaymentTypeWsDTO> paymentTypes;


    public void setPaymentTypes(List<B2BPaymentTypeWsDTO> paymentTypes)
    {
        this.paymentTypes = paymentTypes;
    }


    public List<B2BPaymentTypeWsDTO> getPaymentTypes()
    {
        return this.paymentTypes;
    }
}
