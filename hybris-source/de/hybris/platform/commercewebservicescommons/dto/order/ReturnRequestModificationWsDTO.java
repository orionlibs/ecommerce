package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.product.ReturnRequestStatusWsDTOType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "ReturnRequestModification", description = "Representation of modifications for a return request")
public class ReturnRequestModificationWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "status", value = "Status of the return request")
    private ReturnRequestStatusWsDTOType status;


    public void setStatus(ReturnRequestStatusWsDTOType status)
    {
        this.status = status;
    }


    public ReturnRequestStatusWsDTOType getStatus()
    {
        return this.status;
    }
}
