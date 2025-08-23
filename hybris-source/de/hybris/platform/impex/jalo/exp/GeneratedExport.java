package de.hybris.platform.impex.jalo.exp;

import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedExport extends GenericItem
{
    public static final String CODE = "code";
    public static final String EXPORTEDMEDIAS = "exportedMedias";
    public static final String EXPORTEDDATA = "exportedData";
    public static final String EXPORTSCRIPT = "exportScript";
    public static final String DESCRIPTION = "description";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("exportedMedias", Item.AttributeMode.INITIAL);
        tmp.put("exportedData", Item.AttributeMode.INITIAL);
        tmp.put("exportScript", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedExport.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedExport.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public ImpExExportMedia getExportedData(SessionContext ctx)
    {
        return (ImpExExportMedia)getProperty(ctx, "exportedData");
    }


    public ImpExExportMedia getExportedData()
    {
        return getExportedData(getSession().getSessionContext());
    }


    public void setExportedData(SessionContext ctx, ImpExExportMedia value)
    {
        setProperty(ctx, "exportedData", value);
    }


    public void setExportedData(ImpExExportMedia value)
    {
        setExportedData(getSession().getSessionContext(), value);
    }


    public ImpExExportMedia getExportedMedias(SessionContext ctx)
    {
        return (ImpExExportMedia)getProperty(ctx, "exportedMedias");
    }


    public ImpExExportMedia getExportedMedias()
    {
        return getExportedMedias(getSession().getSessionContext());
    }


    public void setExportedMedias(SessionContext ctx, ImpExExportMedia value)
    {
        setProperty(ctx, "exportedMedias", value);
    }


    public void setExportedMedias(ImpExExportMedia value)
    {
        setExportedMedias(getSession().getSessionContext(), value);
    }


    public ImpExMedia getExportScript(SessionContext ctx)
    {
        return (ImpExMedia)getProperty(ctx, "exportScript");
    }


    public ImpExMedia getExportScript()
    {
        return getExportScript(getSession().getSessionContext());
    }


    public void setExportScript(SessionContext ctx, ImpExMedia value)
    {
        setProperty(ctx, "exportScript", value);
    }


    public void setExportScript(ImpExMedia value)
    {
        setExportScript(getSession().getSessionContext(), value);
    }
}
