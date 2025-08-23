package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;

public class AccountPreferencesData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String accessType;


    public void setAccessType(String accessType)
    {
        this.accessType = accessType;
    }


    public String getAccessType()
    {
        return this.accessType;
    }
}
