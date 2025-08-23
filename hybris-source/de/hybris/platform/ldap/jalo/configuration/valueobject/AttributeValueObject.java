package de.hybris.platform.ldap.jalo.configuration.valueobject;

import de.hybris.bootstrap.xml.AbstractValueObject;

public class AttributeValueObject extends AbstractValueObject
{
    private HeaderEntry headerEntry;
    private final String ldap;


    public AttributeValueObject(String ldap, HeaderEntry headerentry)
    {
        this.ldap = ldap;
        this.headerEntry = headerentry;
    }


    public HeaderEntry getHeaderEntry()
    {
        return this.headerEntry;
    }


    public void setHeaderEntry(HeaderEntry hederentry)
    {
        this.headerEntry = hederentry;
    }


    public String getLdap()
    {
        return this.ldap;
    }
}
