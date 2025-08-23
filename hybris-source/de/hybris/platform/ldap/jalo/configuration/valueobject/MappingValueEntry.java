package de.hybris.platform.ldap.jalo.configuration.valueobject;

public class MappingValueEntry
{
    private final String hybris;
    private final String ldap;


    public MappingValueEntry(String ldap, String hybris)
    {
        this.hybris = hybris;
        this.ldap = ldap;
    }


    public String getHybris()
    {
        return this.hybris;
    }


    public String getLDAP()
    {
        return this.ldap;
    }
}
