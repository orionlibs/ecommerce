package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "OrderHistoryFilters", description = "Representation of an Order history request")
public class OrderHistoryFiltersWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "status")
    private String status;
    @ApiModelProperty(name = "accountNumber")
    private String accountNumber;
    @ApiModelProperty(name = "addressId")
    private String addressId;


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }


    public String getAccountNumber()
    {
        return this.accountNumber;
    }


    public void setAddressId(String addressId)
    {
        this.addressId = addressId;
    }


    public String getAddressId()
    {
        return this.addressId;
    }
}
