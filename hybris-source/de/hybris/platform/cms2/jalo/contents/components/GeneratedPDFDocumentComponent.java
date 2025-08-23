package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPDFDocumentComponent extends SimpleCMSComponent
{
    public static final String PDFFILE = "pdfFile";
    public static final String TITLE = "title";
    public static final String HEIGHT = "height";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("pdfFile", Item.AttributeMode.INITIAL);
        tmp.put("title", Item.AttributeMode.INITIAL);
        tmp.put("height", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getHeight(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "height");
    }


    public Integer getHeight()
    {
        return getHeight(getSession().getSessionContext());
    }


    public int getHeightAsPrimitive(SessionContext ctx)
    {
        Integer value = getHeight(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getHeightAsPrimitive()
    {
        return getHeightAsPrimitive(getSession().getSessionContext());
    }


    public void setHeight(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "height", value);
    }


    public void setHeight(Integer value)
    {
        setHeight(getSession().getSessionContext(), value);
    }


    public void setHeight(SessionContext ctx, int value)
    {
        setHeight(ctx, Integer.valueOf(value));
    }


    public void setHeight(int value)
    {
        setHeight(getSession().getSessionContext(), value);
    }


    public Media getPdfFile(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedPDFDocumentComponent.getPdfFile requires a session language", 0);
        }
        return (Media)getLocalizedProperty(ctx, "pdfFile");
    }


    public Media getPdfFile()
    {
        return getPdfFile(getSession().getSessionContext());
    }


    public Map<Language, Media> getAllPdfFile(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "pdfFile", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, Media> getAllPdfFile()
    {
        return getAllPdfFile(getSession().getSessionContext());
    }


    public void setPdfFile(SessionContext ctx, Media value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedPDFDocumentComponent.setPdfFile requires a session language", 0);
        }
        setLocalizedProperty(ctx, "pdfFile", value);
    }


    public void setPdfFile(Media value)
    {
        setPdfFile(getSession().getSessionContext(), value);
    }


    public void setAllPdfFile(SessionContext ctx, Map<Language, Media> value)
    {
        setAllLocalizedProperties(ctx, "pdfFile", value);
    }


    public void setAllPdfFile(Map<Language, Media> value)
    {
        setAllPdfFile(getSession().getSessionContext(), value);
    }


    public String getTitle(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedPDFDocumentComponent.getTitle requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "title");
    }


    public String getTitle()
    {
        return getTitle(getSession().getSessionContext());
    }


    public Map<Language, String> getAllTitle(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "title", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllTitle()
    {
        return getAllTitle(getSession().getSessionContext());
    }


    public void setTitle(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedPDFDocumentComponent.setTitle requires a session language", 0);
        }
        setLocalizedProperty(ctx, "title", value);
    }


    public void setTitle(String value)
    {
        setTitle(getSession().getSessionContext(), value);
    }


    public void setAllTitle(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "title", value);
    }


    public void setAllTitle(Map<Language, String> value)
    {
        setAllTitle(getSession().getSessionContext(), value);
    }
}
