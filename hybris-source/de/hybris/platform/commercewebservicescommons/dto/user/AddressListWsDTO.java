package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "AddressList", description = "Representation of an Address list")
public class AddressListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "addresses", value = "List of addresses")
    private List<AddressWsDTO> addresses;


    public void setAddresses(List<AddressWsDTO> addresses)
    {
        this.addresses = addresses;
    }


    public List<AddressWsDTO> getAddresses()
    {
        return this.addresses;
    }
}
