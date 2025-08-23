package de.hybris.platform.ldap.connection;

import de.hybris.platform.ldap.jalo.configuration.valueobject.ConfigValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.TypeEntryValueObject;
import de.hybris.platform.util.Base64;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import org.apache.commons.lang.StringUtils;

public class LDAPGenericObject
{
    private static final String OBJECT_CLASS = "objectClass";
    private String name = null;
    private String base = null;
    private LdapContext context = null;
    private final Hashtable<String, List> attributes = new Hashtable<>();
    private String hybrisTypeCode = null;
    private ConfigValueObject config = null;
    private String distinguishedName = null;
    private static final int MAX_LDIF_ENTRY_LENGTH = 76;


    public LDAPGenericObject(ConfigValueObject config) throws NamingException
    {
        this(config, null);
    }


    public LDAPGenericObject(ConfigValueObject config, Attributes attribs) throws NamingException
    {
        this.config = config;
        if(attribs != null)
        {
            setAttributes(attribs);
        }
        this.hybrisTypeCode = getHybrisTypeCode(config);
    }


    public LDAPGenericObject(SearchResult searchResult, String base) throws NamingException
    {
        this((LdapContext)searchResult.getObject(), searchResult.getName(), base);
        setAttributes(searchResult.getAttributes());
    }


    public LDAPGenericObject(LdapContext context, String name, String base)
    {
        this(null, context, name, base, null);
    }


    public LDAPGenericObject(ConfigValueObject config, LdapContext context, String name, String base)
    {
        this(config, context, name, base, null);
    }


    public LDAPGenericObject(LdapContext context, String name, String base, String hybrisTypeCode)
    {
        this(null, context, name, base, hybrisTypeCode);
    }


    public LDAPGenericObject(ConfigValueObject config, LdapContext context, String name, String base, String hybrisTypeCode)
    {
        this.config = config;
        this.name = name;
        this.base = base;
        this.context = context;
    }


    public String getName()
    {
        return this.name;
    }


    public String getBase()
    {
        return this.base;
    }


    public String getAbsoluteName()
    {
        return this.name + "," + this.name;
    }


    public Hashtable<String, List> getAttributes()
    {
        return this.attributes;
    }


    public List getAttribute(String key)
    {
        return this.attributes.get(key);
    }


    public void setAttribute(String key, List<String> values)
    {
        this.attributes.put(key, values);
        if(key.equalsIgnoreCase("dn") || key.equalsIgnoreCase("distinguishedName") || key.equalsIgnoreCase("cn") || key
                        .equalsIgnoreCase("givenName") || key.equalsIgnoreCase("name") || key.equalsIgnoreCase("displayName"))
        {
            if(values.get(0) instanceof byte[])
            {
                values.set(0, new String((byte[])values.get(0), Charset.forName("UTF-8")));
            }
        }
        if(key.equalsIgnoreCase("dn") || key.equalsIgnoreCase("distinguishedName"))
        {
            this.distinguishedName = values.get(0);
        }
    }


    public void setAttribute(String key, NamingEnumeration enumerationValues) throws NamingException
    {
        List values = new ArrayList();
        while(enumerationValues.hasMore())
        {
            values.add(enumerationValues.next());
        }
        setAttribute(key, values);
    }


    public void setAttribute(Attribute attribute) throws NamingException
    {
        setAttribute(attribute.getID(), attribute.getAll());
    }


    public void setAttributes(Attributes attributes) throws NamingException
    {
        NamingEnumeration<? extends Attribute> namingEnumeration = attributes.getAll();
        while(namingEnumeration.hasMore())
        {
            setAttribute(namingEnumeration.next());
        }
    }


    public void setHybrisTypeCode(String code)
    {
        this.hybrisTypeCode = code;
    }


    public String getHybrisTypeCode()
    {
        return this.hybrisTypeCode;
    }


    private String getHybrisTypeCode(ConfigValueObject config)
    {
        if(this.hybrisTypeCode != null)
        {
            return this.hybrisTypeCode;
        }
        List<String> objectClasses = getAttribute("objectClass");
        if(objectClasses != null && !objectClasses.isEmpty())
        {
            Collection<TypeEntryValueObject> supportedTypes = config.getEntries();
            for(String objectClass : objectClasses)
            {
                for(TypeEntryValueObject type : supportedTypes)
                {
                    if(type.getObjectclasses().contains(objectClass))
                    {
                        this.hybrisTypeCode = type.getTypeCode();
                        return this.hybrisTypeCode;
                    }
                }
            }
        }
        return this.hybrisTypeCode;
    }


    public String getValue()
    {
        StringBuilder values = new StringBuilder();
        Hashtable<String, List> attribs = getAttributes();
        for(Map.Entry<String, List> entry2 : attribs.entrySet())
        {
            List list = entry2.getValue();
            for(Object entry : list)
            {
                if(entry.getClass().isArray())
                {
                    String base64 = Base64.encodeBytes((byte[])entry, 8);
                    int lengthOfAttributeEntry = ((String)entry2.getKey()).length() + "::".length() + " ".length();
                    StringBuilder out = new StringBuilder();
                    out.append(base64.substring(0, 76 - lengthOfAttributeEntry));
                    out.append("\n");
                    base64 = base64.substring(76 - lengthOfAttributeEntry);
                    int start = 0;
                    int offset = 75;
                    while(start < base64.length())
                    {
                        offset = start + 76 - 1;
                        if(offset > base64.length())
                        {
                            offset = base64.length();
                        }
                        out.append(" ").append(base64.substring(start, offset)).append("\n");
                        start = offset;
                    }
                    values.append(entry2.getKey()).append(":: ").append(out.toString()).append("\n");
                    continue;
                }
                if(entry instanceof String)
                {
                    values.append(entry2.getKey()).append(": ").append(entry).append("\n");
                    continue;
                }
                entry = "<not a string>";
                values.append(entry2.getKey()).append(": ").append(entry).append("\n");
            }
        }
        return values.toString();
    }


    public String getDN()
    {
        return StringUtils.isBlank(this.distinguishedName) ? "<couldn't resolve DN>" : this.distinguishedName;
    }
}
