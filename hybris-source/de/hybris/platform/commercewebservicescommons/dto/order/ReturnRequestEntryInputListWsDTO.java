package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ReturnRequestEntryInputList", description = "Representation of a return request entry input list for an order")
public class ReturnRequestEntryInputListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orderCode", value = "Code of the order which return request is related to", required = true, example = "00000001")
    private String orderCode;
    @ApiModelProperty(name = "returnRequestEntryInputs", value = "Return request entry inputs which contain information about the order entries which are requested to be returned", required = true)
    private List<ReturnRequestEntryInputWsDTO> returnRequestEntryInputs;


    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }


    public String getOrderCode()
    {
        return this.orderCode;
    }


    public void setReturnRequestEntryInputs(List<ReturnRequestEntryInputWsDTO> returnRequestEntryInputs)
    {
        this.returnRequestEntryInputs = returnRequestEntryInputs;
    }


    public List<ReturnRequestEntryInputWsDTO> getReturnRequestEntryInputs()
    {
        return this.returnRequestEntryInputs;
    }
}
