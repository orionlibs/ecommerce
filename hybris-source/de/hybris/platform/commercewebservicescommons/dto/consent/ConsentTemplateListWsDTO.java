package de.hybris.platform.commercewebservicescommons.dto.consent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ConsentTemplateList", description = "Representation of a Consent Template List")
public class ConsentTemplateListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "consentTemplates", value = "List of consent templates")
    private List<ConsentTemplateWsDTO> consentTemplates;


    public void setConsentTemplates(List<ConsentTemplateWsDTO> consentTemplates)
    {
        this.consentTemplates = consentTemplates;
    }


    public List<ConsentTemplateWsDTO> getConsentTemplates()
    {
        return this.consentTemplates;
    }
}
