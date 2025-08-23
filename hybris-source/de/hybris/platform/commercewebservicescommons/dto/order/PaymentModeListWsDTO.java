package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "PaymentModeList", description = "Representation of a Payment Mode List")
public class PaymentModeListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "paymentModes", value = "List of payment modes")
    private List<PaymentModeWsDTO> paymentModes;


    public void setPaymentModes(List<PaymentModeWsDTO> paymentModes)
    {
        this.paymentModes = paymentModes;
    }


    public List<PaymentModeWsDTO> getPaymentModes()
    {
        return this.paymentModes;
    }
}
