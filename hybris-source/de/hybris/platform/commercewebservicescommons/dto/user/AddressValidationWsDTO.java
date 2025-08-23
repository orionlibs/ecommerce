package de.hybris.platform.commercewebservicescommons.dto.user;

import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "AddressValidation", description = "Representation of an Address Validation")
public class AddressValidationWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "errors", value = "List of errors")
    private ErrorListWsDTO errors;
    @ApiModelProperty(name = "decision", value = "Decision")
    private String decision;
    @ApiModelProperty(name = "suggestedAddresses", value = "List of suggested addresses")
    private List<AddressWsDTO> suggestedAddresses;


    public void setErrors(ErrorListWsDTO errors)
    {
        this.errors = errors;
    }


    public ErrorListWsDTO getErrors()
    {
        return this.errors;
    }


    public void setDecision(String decision)
    {
        this.decision = decision;
    }


    public String getDecision()
    {
        return this.decision;
    }


    public void setSuggestedAddresses(List<AddressWsDTO> suggestedAddresses)
    {
        this.suggestedAddresses = suggestedAddresses;
    }


    public List<AddressWsDTO> getSuggestedAddresses()
    {
        return this.suggestedAddresses;
    }
}
