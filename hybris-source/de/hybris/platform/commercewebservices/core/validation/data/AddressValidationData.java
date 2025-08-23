package de.hybris.platform.commercewebservices.core.validation.data;

import de.hybris.platform.commercewebservices.core.user.data.AddressDataList;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import java.io.Serializable;

public class AddressValidationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ErrorListWsDTO errors;
    private String decision;
    private AddressDataList suggestedAddressesList;


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


    public void setSuggestedAddressesList(AddressDataList suggestedAddressesList)
    {
        this.suggestedAddressesList = suggestedAddressesList;
    }


    public AddressDataList getSuggestedAddressesList()
    {
        return this.suggestedAddressesList;
    }
}
