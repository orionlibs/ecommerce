package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import java.util.List;

public class CartRestorationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CartModificationData> modifications;


    public void setModifications(List<CartModificationData> modifications)
    {
        this.modifications = modifications;
    }


    public List<CartModificationData> getModifications()
    {
        return this.modifications;
    }
}
