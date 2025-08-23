package de.hybris.platform.hac.data.dto;

public class LicenseData
{
    private String id;
    private String name;
    private String signature;
    private boolean unrestricted = false;


    public boolean isUnrestricted()
    {
        return this.unrestricted;
    }


    public void setUnrestricted(boolean unrestricted)
    {
        this.unrestricted = unrestricted;
    }


    public String getId()
    {
        return this.id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getSignature()
    {
        return this.signature;
    }


    public void setSignature(String signature)
    {
        this.signature = signature;
    }
}
