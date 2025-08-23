package de.hybris.platform.webservicescommons.dto.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "error", description = "Error message")
public class ErrorWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "type", value = "Type of the error e.g. 'LowStockError'.")
    private String type;
    @ApiModelProperty(name = "reason", value = "Additional classification specific for each error type e.g. 'noStock'.")
    private String reason;
    @ApiModelProperty(name = "message", value = "Descriptive, human readable error message.")
    private String message;
    @ApiModelProperty(name = "subjectType", value = "Type of the object related to the error e.g. 'entry'.")
    private String subjectType;
    @ApiModelProperty(name = "subject", value = "Identifier of the related object e.g. '1'.")
    private String subject;
    @ApiModelProperty(name = "errorCode", value = "Error code")
    private String errorCode;


    public void setType(String type)
    {
        this.type = type;
    }


    public String getType()
    {
        return this.type;
    }


    public void setReason(String reason)
    {
        this.reason = reason;
    }


    public String getReason()
    {
        return this.reason;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }


    public String getMessage()
    {
        return this.message;
    }


    public void setSubjectType(String subjectType)
    {
        this.subjectType = subjectType;
    }


    public String getSubjectType()
    {
        return this.subjectType;
    }


    public void setSubject(String subject)
    {
        this.subject = subject;
    }


    public String getSubject()
    {
        return this.subject;
    }


    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }


    public String getErrorCode()
    {
        return this.errorCode;
    }
}
