package de.hybris.platform.commerceservices.address.data;

import de.hybris.platform.core.model.user.AddressModel;
import java.io.Serializable;
import java.util.List;

public class AddressVerificationResultData<DECISION, FIELD_ERROR> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private DECISION decision;
    private List<FIELD_ERROR> fieldErrors;
    private List<AddressModel> suggestedAddresses;


    public void setDecision(DECISION decision)
    {
        this.decision = decision;
    }


    public DECISION getDecision()
    {
        return this.decision;
    }


    public void setFieldErrors(List<FIELD_ERROR> fieldErrors)
    {
        this.fieldErrors = fieldErrors;
    }


    public List<FIELD_ERROR> getFieldErrors()
    {
        return this.fieldErrors;
    }


    public void setSuggestedAddresses(List<AddressModel> suggestedAddresses)
    {
        this.suggestedAddresses = suggestedAddresses;
    }


    public List<AddressModel> getSuggestedAddresses()
    {
        return this.suggestedAddresses;
    }
}
