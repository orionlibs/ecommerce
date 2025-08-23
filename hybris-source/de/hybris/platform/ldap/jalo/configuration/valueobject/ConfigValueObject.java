package de.hybris.platform.ldap.jalo.configuration.valueobject;

import de.hybris.bootstrap.xml.AbstractValueObject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class ConfigValueObject extends AbstractValueObject
{
    private static final Logger LOG = Logger.getLogger(ConfigValueObject.class.getName());
    private final Collection<TypeEntryValueObject> entries;
    private final Map<String, AttributesValueObject> typemap;
    private final Collection<MappingsValueObject> mappings;
    private final Map<String, String> defaultImpExHeaderEntry;


    public ConfigValueObject(Collection<TypeEntryValueObject> entries, Collection<MappingsValueObject> mappings)
    {
        this.entries = entries;
        this.mappings = mappings;
        this.typemap = new HashMap<>();
        for(TypeEntryValueObject entry : entries)
        {
            String type = entry.getTypeCode();
            AttributesValueObject attributes = entry.getAttributes();
            this.typemap.put(type.toLowerCase(), attributes);
        }
        this.defaultImpExHeaderEntry = new HashMap<>();
        for(TypeEntryValueObject entry : entries)
        {
            if(entry.getDefaultHeaderEntry() != null && entry.getDefaultHeaderEntry().trim().length() > 0)
            {
                this.defaultImpExHeaderEntry.put(entry.getTypeCode().toLowerCase(), entry.getDefaultHeaderEntry());
            }
        }
    }


    public Map<String, String> getMappedValues(String attribute)
    {
        for(MappingsValueObject entry : this.mappings)
        {
            if(entry.getAttributes().contains(attribute))
            {
                return entry.getValues();
            }
        }
        return Collections.EMPTY_MAP;
    }


    public AttributesValueObject getTypeConfiguration(String type)
    {
        return (type != null) ? this.typemap.get(type.toLowerCase()) : null;
    }


    public String getDefaultHeaderEntry(String typecode)
    {
        return (typecode != null) ? this.defaultImpExHeaderEntry.get(typecode.toLowerCase()) : null;
    }


    public String getAfter(String typecode)
    {
        for(TypeEntryValueObject entry : this.entries)
        {
            if(typecode.equalsIgnoreCase(entry.getTypeCode()))
            {
                return entry.getAfter();
            }
        }
        return null;
    }


    public String getBefore(String typecode)
    {
        for(TypeEntryValueObject entry : this.entries)
        {
            if(typecode.equalsIgnoreCase(entry.getTypeCode()))
            {
                return entry.getBefore();
            }
        }
        return null;
    }


    public Collection<TypeEntryValueObject> getEntries()
    {
        return this.entries;
    }
}
