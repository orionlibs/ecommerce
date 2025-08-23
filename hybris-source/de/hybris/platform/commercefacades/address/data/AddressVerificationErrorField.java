package de.hybris.platform.commercefacades.address.data;

import java.io.Serializable;

public class AddressVerificationErrorField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private boolean missing;
    private boolean invalid;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setMissing(boolean missing)
    {
        this.missing = missing;
    }


    public boolean isMissing()
    {
        return this.missing;
    }


    public void setInvalid(boolean invalid)
    {
        this.invalid = invalid;
    }


    public boolean isInvalid()
    {
        return this.invalid;
    }
}
