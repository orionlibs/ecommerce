package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "PaymentDetailsList", description = "Representation of a Payment details list")
public class PaymentDetailsListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "payments", value = "List of payment details")
    private List<PaymentDetailsWsDTO> payments;


    public void setPayments(List<PaymentDetailsWsDTO> payments)
    {
        this.payments = payments;
    }


    public List<PaymentDetailsWsDTO> getPayments()
    {
        return this.payments;
    }
}
