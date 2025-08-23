package de.hybris.platform.ldap.jalo.configuration.valueobject;

import de.hybris.bootstrap.xml.AbstractValueObject;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class MappingsValueObject extends AbstractValueObject
{
    private final Collection<String> attributes;
    private final Map<String, String> values;


    public MappingsValueObject(Collection<String> attributes, Map<String, String> values)
    {
        this.attributes = attributes;
        this.values = values;
    }


    public Collection<String> getAttributes()
    {
        return this.attributes;
    }


    public String getMappedValue(String ldapValue)
    {
        return this.values.get(ldapValue);
    }


    public Map<String, String> getValues()
    {
        return this.values;
    }


    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("MappingValueObject[ attributes: ");
        for(String attribute : getAttributes())
        {
            buf.append(attribute).append(", ");
        }
        buf.append("values:  ");
        for(Iterator<Map.Entry> it = this.values.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry mapEntry = it.next();
            buf.append(mapEntry.getKey()).append(" ->").append(mapEntry.getValue()).append(", ");
        }
        buf.append("]");
        return buf.toString();
    }
}
