package de.hybris.platform.webservicescommons.dto.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "errorList", description = "List of errors")
public class ErrorListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "errors")
    private List<ErrorWsDTO> errors;


    public void setErrors(List<ErrorWsDTO> errors)
    {
        this.errors = errors;
    }


    public List<ErrorWsDTO> getErrors()
    {
        return this.errors;
    }
}
