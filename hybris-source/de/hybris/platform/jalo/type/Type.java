package de.hybris.platform.jalo.type;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Map;

public abstract class Type extends TypeManagerManaged
{
    public static final String DEFAULTVALUE = "defaultValue".intern();
    public static final String DESCRIPTION = "description".intern();
    public static final String CODE = "code".intern();
    public static final String XMLDEFINITION = "xmldefinition".intern();
    private String code = null;


    public String getCode()
    {
        if(this.code == null)
        {
            this
                            .code = (String)(new Object(this, CODE, false, true)).get();
        }
        return this.code;
    }


    public Object getDefaultValue()
    {
        return getDefaultValue(getSession().getSessionContext());
    }


    public Object getDefaultValue(SessionContext ctx)
    {
        return getProperty(ctx, DEFAULTVALUE);
    }


    public void setDefaultValue(Object value) throws JaloInvalidParameterException
    {
        setDefaultValue(getSession().getSessionContext(), value);
    }


    public void setDefaultValue(SessionContext ctx, Object value) throws JaloInvalidParameterException
    {
        setProperty(ctx, DEFAULTVALUE, value);
    }


    public abstract String toString(SessionContext paramSessionContext, Object paramObject) throws JaloInvalidParameterException;


    public abstract Object parseValue(SessionContext paramSessionContext, String paramString) throws JaloInvalidParameterException;


    public abstract boolean isAssignableFrom(Type paramType);


    public abstract boolean isInstance(Object paramObject);


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("Type.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, DESCRIPTION);
    }


    public void setDescription(String description)
    {
        setDescription(getSession().getSessionContext(), description);
    }


    public void setDescription(SessionContext ctx, String description)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("Type.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, DESCRIPTION, description);
    }


    public Map getAllDescriptions()
    {
        return getAllDescriptions(getSession().getSessionContext());
    }


    public Map getAllDescriptions(SessionContext ctx)
    {
        return (Map)getLocalizedProperty(getAllValuesSessionContext(ctx), DESCRIPTION);
    }


    public void setAllDescriptions(Map descriptions)
    {
        setAllDescriptions(getSession().getSessionContext(), descriptions);
    }


    public void setAllDescriptions(SessionContext ctx, Map descriptions)
    {
        setLocalizedProperty(getAllValuesSessionContext(ctx), DESCRIPTION, descriptions);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getTypeImpl().toString();
    }


    public abstract String getXMLDefinition();


    protected TypeImpl getTypeImpl()
    {
        return (TypeImpl)this.impl;
    }


    public int compareTo(Object o)
    {
        String c1 = getCode();
        String c2 = ((Type)o).getCode();
        return (c1 == null || c2 == null) ? 0 :
                        c1.toLowerCase(LocaleHelper.getPersistenceLocale()).compareTo(c2.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }
}
