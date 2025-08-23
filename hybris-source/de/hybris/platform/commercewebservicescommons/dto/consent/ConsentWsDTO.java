package de.hybris.platform.commercewebservicescommons.dto.consent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "Consent", description = "Representation of a Consent")
public class ConsentWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of consent")
    private String code;
    @ApiModelProperty(name = "consentGivenDate", value = "Date of consenting")
    private Date consentGivenDate;
    @ApiModelProperty(name = "consentWithdrawnDate", value = "Consent withdrawn date")
    private Date consentWithdrawnDate;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setConsentGivenDate(Date consentGivenDate)
    {
        this.consentGivenDate = consentGivenDate;
    }


    public Date getConsentGivenDate()
    {
        return this.consentGivenDate;
    }


    public void setConsentWithdrawnDate(Date consentWithdrawnDate)
    {
        this.consentWithdrawnDate = consentWithdrawnDate;
    }


    public Date getConsentWithdrawnDate()
    {
        return this.consentWithdrawnDate;
    }
}
