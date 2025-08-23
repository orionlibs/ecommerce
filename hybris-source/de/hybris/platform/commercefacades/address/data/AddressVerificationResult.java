package de.hybris.platform.commercefacades.address.data;

import de.hybris.platform.commercefacades.user.data.AddressData;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AddressVerificationResult<DECISION> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private DECISION decision;
    private List<AddressData> suggestedAddresses;
    private Map<String, AddressVerificationErrorField> errors;


    public void setDecision(DECISION decision)
    {
        this.decision = decision;
    }


    public DECISION getDecision()
    {
        return this.decision;
    }


    public void setSuggestedAddresses(List<AddressData> suggestedAddresses)
    {
        this.suggestedAddresses = suggestedAddresses;
    }


    public List<AddressData> getSuggestedAddresses()
    {
        return this.suggestedAddresses;
    }


    public void setErrors(Map<String, AddressVerificationErrorField> errors)
    {
        this.errors = errors;
    }


    public Map<String, AddressVerificationErrorField> getErrors()
    {
        return this.errors;
    }
}
