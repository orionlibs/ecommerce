package de.hybris.platform.oauth2.data;

import java.io.Serializable;

public class AuthenticatedUserData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String displayName;


    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    public String getDisplayName()
    {
        return this.displayName;
    }
}
