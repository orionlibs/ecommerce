package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import java.util.Map;
import org.znerd.xmlenc.XMLOutputter;

public abstract class TypeManagerManaged extends LocalizableItem
{
    public static final String NAME = "name";
    public static final String EXTENSION_NAME = "extensionName";
    public static final String DEPRECATED = "deprecated";
    public static final String AUTOCREATE = "autocreate";
    public static final String GENERATE = "generate";


    public String getExtensionName()
    {
        return (String)getProperty("extensionName");
    }


    public void setExtensionName(String extname)
    {
        setProperty("extensionName", extname);
    }


    public boolean isDeprecated()
    {
        return Boolean.TRUE.equals(getProperty("deprecated"));
    }


    public boolean isAutocreate()
    {
        Boolean b = (Boolean)getProperty("autocreate");
        return (b == null || b.booleanValue());
    }


    public void setAutocreate(boolean auto)
    {
        setProperty("autocreate", auto ? Boolean.TRUE : Boolean.FALSE);
    }


    public boolean isGenerate()
    {
        return Boolean.TRUE.equals(getProperty("generate"));
    }


    public void setGenerate(boolean gen)
    {
        setProperty("generate", gen ? Boolean.TRUE : Boolean.FALSE);
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("TypeManagerManaged.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public Map getAllNames(SessionContext ctx)
    {
        return (Map)getLocalizedProperty(getAllValuesSessionContext(ctx), "name");
    }


    public void setAllNames(SessionContext ctx, Map names)
    {
        setLocalizedProperty(getAllValuesSessionContext(ctx), "name", names);
    }


    public Map getAllNames()
    {
        return getAllNames(getSession().getSessionContext());
    }


    public void setName(String name)
    {
        setName(getSession().getSessionContext(), name);
    }


    public void setName(SessionContext ctx, String name)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("TypeManagerManaged.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", name);
    }


    public void setAllNames(Map names)
    {
        setAllNames(getSession().getSessionContext(), names);
    }


    protected TypeManagerManagedImpl getImpl()
    {
        return (TypeManagerManagedImpl)this.impl;
    }


    public abstract String exportXMLDefinition(XMLOutputter paramXMLOutputter);
}
