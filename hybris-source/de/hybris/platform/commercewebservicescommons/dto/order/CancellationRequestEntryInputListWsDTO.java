package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "CancellationRequestEntryInputList", description = "Representation of a cancellation request entry input list for an order")
public class CancellationRequestEntryInputListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "cancellationRequestEntryInputs", value = "Cancellation request entry inputs which contain information about the order entries which are requested to be cancelled", required = true)
    private List<CancellationRequestEntryInputWsDTO> cancellationRequestEntryInputs;


    public void setCancellationRequestEntryInputs(List<CancellationRequestEntryInputWsDTO> cancellationRequestEntryInputs)
    {
        this.cancellationRequestEntryInputs = cancellationRequestEntryInputs;
    }


    public List<CancellationRequestEntryInputWsDTO> getCancellationRequestEntryInputs()
    {
        return this.cancellationRequestEntryInputs;
    }
}
