package de.hybris.platform.ldap.jalo.configuration.valueobject;

import de.hybris.bootstrap.xml.AbstractValueObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesValueObject extends AbstractValueObject
{
    private final Collection<AttributeValueObject> attributes;
    private final Map<String, HeaderEntry> ldap2header;
    private final List<String> supportedLdapAttributes;
    private String typeCode = null;


    public AttributesValueObject(Collection<AttributeValueObject> attributes)
    {
        this.attributes = attributes;
        this.supportedLdapAttributes = new ArrayList<>();
        this.ldap2header = new HashMap<>();
        for(AttributeValueObject avo : attributes)
        {
            this.supportedLdapAttributes.add(avo.getLdap());
            this.ldap2header.put(avo.getLdap(), avo.getHeaderEntry());
        }
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public List<String> getSupportedLDAPAttributes()
    {
        return this.supportedLdapAttributes;
    }


    public boolean isSupportedLDAPAttribute(String ldap)
    {
        return this.supportedLdapAttributes.contains(ldap);
    }


    public Collection<AttributeValueObject> getAllAttributes()
    {
        return this.attributes;
    }


    public HeaderEntry getHeaderEntry(String ldap)
    {
        return this.ldap2header.get(ldap);
    }


    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AttributesValueObject[ ");
        for(AttributeValueObject attrib : getAllAttributes())
        {
            stringBuilder.append(attrib.getLdap()).append(" -> ").append(attrib.getHeaderEntry()).append(", ");
        }
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }
}
