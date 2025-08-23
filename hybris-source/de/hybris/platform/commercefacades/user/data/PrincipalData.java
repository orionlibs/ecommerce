package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;

public class PrincipalData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private String name;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }
}
