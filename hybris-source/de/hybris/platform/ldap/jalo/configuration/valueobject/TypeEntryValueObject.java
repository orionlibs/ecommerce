package de.hybris.platform.ldap.jalo.configuration.valueobject;

import de.hybris.bootstrap.xml.AbstractValueObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeEntryValueObject extends AbstractValueObject
{
    private final String code;
    private List<String> objectclasses;
    private AttributesValueObject attributes;
    private List<String> supporetdLdapAttributes;
    private Map<String, HeaderEntry> headerEntries;
    private String defaultheaderentry;
    private String after;
    private String before;


    public TypeEntryValueObject(String code)
    {
        this.code = code;
    }


    public AttributesValueObject getAttributes()
    {
        return this.attributes;
    }


    public void setAttributes(AttributesValueObject attributes)
    {
        this.attributes = attributes;
        this.supporetdLdapAttributes = new ArrayList<>();
        this.headerEntries = new HashMap<>();
        for(AttributeValueObject avo : attributes.getAllAttributes())
        {
            this.supporetdLdapAttributes.add(avo.getLdap());
            this.headerEntries.put(avo.getLdap(), avo.getHeaderEntry());
        }
    }


    public HeaderEntry getHeaderEntry(String ldap)
    {
        return this.headerEntries.get(ldap);
    }


    public List<String> getObjectclasses()
    {
        return (this.objectclasses != null) ? this.objectclasses : new ArrayList<>();
    }


    public void addObjectclass(String objClass)
    {
        getObjectclasses().add(objClass);
    }


    public void setObjectclasses(Collection<String> objectclasses)
    {
        this.objectclasses = new ArrayList<>(objectclasses);
    }


    public String getTypeCode()
    {
        return this.code;
    }


    public String getDefaultHeaderEntry()
    {
        return this.defaultheaderentry;
    }


    public void setDefaultHeaderEntry(String defaultheaderentry)
    {
        this.defaultheaderentry = defaultheaderentry;
    }


    public void setAfter(String after)
    {
        this.after = after;
    }


    public String getAfter()
    {
        return this.after;
    }


    public void setBefore(String before)
    {
        this.before = before;
    }


    public String getBefore()
    {
        return this.before;
    }
}
