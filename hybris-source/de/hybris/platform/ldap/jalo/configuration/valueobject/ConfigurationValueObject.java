package de.hybris.platform.ldap.jalo.configuration.valueobject;

import de.hybris.bootstrap.xml.AbstractValueObject;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationValueObject extends AbstractValueObject
{
    private List<TypeEntryValueObject> entries;


    public ConfigurationValueObject()
    {
        this(null);
    }


    public ConfigurationValueObject(List<TypeEntryValueObject> entries)
    {
        this.entries = entries;
    }


    public List<TypeEntryValueObject> getEntries()
    {
        return (this.entries != null) ? this.entries : new ArrayList<>();
    }


    public void setEntries(List<TypeEntryValueObject> entries)
    {
        this.entries = entries;
    }


    public void addEntry(TypeEntryValueObject entry)
    {
        getEntries().add(entry);
    }
}
