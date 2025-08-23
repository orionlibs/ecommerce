package de.hybris.platform.commerceservices.order;

import java.io.Serializable;
import java.util.List;

public class CommerceCartRestoration implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CommerceCartModification> modifications;


    public void setModifications(List<CommerceCartModification> modifications)
    {
        this.modifications = modifications;
    }


    public List<CommerceCartModification> getModifications()
    {
        return this.modifications;
    }
}
