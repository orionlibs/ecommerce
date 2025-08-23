package de.hybris.platform.jalo.enumeration;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.jalo.type.ComposedType;
import java.io.IOException;
import java.util.Map;
import org.znerd.xmlenc.XMLOutputter;

@Deprecated(since = "ages", forRemoval = false)
public class EnumerationValue extends LocalizableItem
{
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String SEQUENCENUMBER = "sequenceNumber";
    public static final String DEPRECATED = "deprecated";
    public static final String EXTENSION_NAME = "extensionName";


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!allAttributes.containsKey("code"))
        {
            throw new JaloInvalidParameterException("Missing parameter(code) to create a EnumerationValue", 0);
        }
        if(!(type instanceof EnumerationType))
        {
            throw new JaloInvalidParameterException("type is no enumeration type", 0);
        }
        return
                        (Item)JaloSession.getCurrentSession()
                                        .getEnumerationManager()
                                        .createEnumerationValue((PK)allAttributes.get(Item.PK), (EnumerationType)type, (String)allAttributes
                                                        .get("code"));
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap copyMap = super.getNonInitialAttributes(ctx, allAttributes);
        copyMap.remove(Item.PK);
        copyMap.remove("code");
        return copyMap;
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getComposedType().getCode() + "." + getComposedType().getCode() + "(" + getCode() + ")";
    }


    public boolean isDeprecated()
    {
        return Boolean.TRUE.equals(getProperty("deprecated"));
    }


    public int getSequenceNumber()
    {
        return ((Integer)(new Object(this, "sequenceNumber"))
                        .get()).intValue();
    }


    public void setSequenceNumber(int i)
    {
        (new Object(this, "sequenceNumber", i))
                        .set();
    }


    public String getCode()
    {
        return (String)(new Object(this, "code"))
                        .get();
    }


    public void setCode(String newCode) throws ConsistencyCheckException
    {
        try
        {
            (new Object(this, "code", newCode))
                            .set();
        }
        catch(de.hybris.platform.jalo.Item.JaloCachedComputationException e)
        {
            Throwable cause = e.getCause();
            if(cause instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)cause;
            }
            throw new JaloSystemException(cause);
        }
    }


    public String getCodeLowerCase()
    {
        return getCode().toLowerCase(LocaleHelper.getPersistenceLocale());
    }


    public void setCodeLowerCase(String codeLowerCase)
    {
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("EnumerationValue.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public void setName(String name)
    {
        setName(getSession().getSessionContext(), name);
    }


    public void setName(SessionContext ctx, String name)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("EnumerationValue.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", name);
    }


    public Map getAllNames(SessionContext ctx)
    {
        return (Map)getLocalizedProperty(getAllValuesSessionContext(ctx), "name");
    }


    public void setAllNames(SessionContext ctx, Map names)
    {
        setLocalizedProperty(getAllValuesSessionContext(ctx), "name", names);
    }


    public String getXMLDefinition()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("   <value code=\"").append(getCode()).append("\"/>\n");
        return sb.toString();
    }


    public void exportXMLDefinition(XMLOutputter xout)
    {
        try
        {
            xout.startTag("value");
            xout.attribute("code", getCode());
            xout.endTag();
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public String getExtensionName()
    {
        return getExtensionName(getSession().getSessionContext());
    }


    public String getExtensionName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "extensionName");
    }


    public void setExtensionName(String name)
    {
        setExtensionName(getSession().getSessionContext(), name);
    }


    public void setExtensionName(SessionContext ctx, String name)
    {
        setProperty(ctx, "extensionName", name);
    }


    public int compareTo(Object o)
    {
        return Integer.valueOf(getSequenceNumber()).compareTo(Integer.valueOf(((EnumerationValue)o).getSequenceNumber()));
    }
}
