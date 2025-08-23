package de.hybris.platform.commons.jalo.renderer;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRendererTemplate extends GenericItem
{
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String DEFAULTCONTENT = "defaultContent";
    public static final String CONTENT = "content";
    public static final String CONTEXTCLASS = "contextClass";
    public static final String OUTPUTMIMETYPE = "outputMimeType";
    public static final String RENDERERTYPE = "rendererType";
    public static final String TEMPLATESCRIPT = "templateScript";
    public static final String CONTEXTCLASSDESCRIPTION = "contextClassDescription";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("defaultContent", Item.AttributeMode.INITIAL);
        tmp.put("content", Item.AttributeMode.INITIAL);
        tmp.put("contextClass", Item.AttributeMode.INITIAL);
        tmp.put("outputMimeType", Item.AttributeMode.INITIAL);
        tmp.put("rendererType", Item.AttributeMode.INITIAL);
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


    public Media getContent(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRendererTemplate.getContent requires a session language", 0);
        }
        return (Media)getLocalizedProperty(ctx, "content");
    }


    public Media getContent()
    {
        return getContent(getSession().getSessionContext());
    }


    public Map<Language, Media> getAllContent(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "content", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, Media> getAllContent()
    {
        return getAllContent(getSession().getSessionContext());
    }


    public void setContent(SessionContext ctx, Media value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setContent(Media value)
    {
        setContent(getSession().getSessionContext(), value);
    }


    public void setAllContent(SessionContext ctx, Map<Language, Media> value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setAllContent(Map<Language, Media> value)
    {
        setAllContent(getSession().getSessionContext(), value);
    }


    public String getContextClass(SessionContext ctx)
    {
        return (String)getProperty(ctx, "contextClass");
    }


    public String getContextClass()
    {
        return getContextClass(getSession().getSessionContext());
    }


    public void setContextClass(SessionContext ctx, String value)
    {
        setProperty(ctx, "contextClass", value);
    }


    public void setContextClass(String value)
    {
        setContextClass(getSession().getSessionContext(), value);
    }


    public String getContextClassDescription()
    {
        return getContextClassDescription(getSession().getSessionContext());
    }


    public Media getDefaultContent(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "defaultContent");
    }


    public Media getDefaultContent()
    {
        return getDefaultContent(getSession().getSessionContext());
    }


    public void setDefaultContent(SessionContext ctx, Media value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setDefaultContent(Media value)
    {
        setDefaultContent(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRendererTemplate.getDescription requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedRendererTemplate.setDescription requires a session language", 0);
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


    public String getOutputMimeType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "outputMimeType");
    }


    public String getOutputMimeType()
    {
        return getOutputMimeType(getSession().getSessionContext());
    }


    public void setOutputMimeType(SessionContext ctx, String value)
    {
        setProperty(ctx, "outputMimeType", value);
    }


    public void setOutputMimeType(String value)
    {
        setOutputMimeType(getSession().getSessionContext(), value);
    }


    public EnumerationValue getRendererType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "rendererType");
    }


    public EnumerationValue getRendererType()
    {
        return getRendererType(getSession().getSessionContext());
    }


    public void setRendererType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "rendererType", value);
    }


    public void setRendererType(EnumerationValue value)
    {
        setRendererType(getSession().getSessionContext(), value);
    }


    public String getTemplateScript()
    {
        return getTemplateScript(getSession().getSessionContext());
    }


    public Map<Language, String> getAllTemplateScript()
    {
        return getAllTemplateScript(getSession().getSessionContext());
    }


    public void setTemplateScript(String value)
    {
        setTemplateScript(getSession().getSessionContext(), value);
    }


    public void setAllTemplateScript(Map<Language, String> value)
    {
        setAllTemplateScript(getSession().getSessionContext(), value);
    }


    public abstract String getContextClassDescription(SessionContext paramSessionContext);


    public abstract String getTemplateScript(SessionContext paramSessionContext);


    public abstract Map<Language, String> getAllTemplateScript(SessionContext paramSessionContext);


    public abstract void setTemplateScript(SessionContext paramSessionContext, String paramString);


    public abstract void setAllTemplateScript(SessionContext paramSessionContext, Map<Language, String> paramMap);
}
