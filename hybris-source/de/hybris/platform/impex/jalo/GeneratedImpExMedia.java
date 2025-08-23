package de.hybris.platform.impex.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedImpExMedia extends Media
{
    public static final String FIELDSEPARATOR = "fieldSeparator";
    public static final String QUOTECHARACTER = "quoteCharacter";
    public static final String COMMENTCHARACTER = "commentCharacter";
    public static final String ENCODING = "encoding";
    public static final String LINESTOSKIP = "linesToSkip";
    public static final String REMOVEONSUCCESS = "removeOnSuccess";
    public static final String ZIPENTRY = "zipentry";
    public static final String PREVIEW = "preview";
    public static final String EXTRACTIONID = "extractionId";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Media.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("fieldSeparator", Item.AttributeMode.INITIAL);
        tmp.put("quoteCharacter", Item.AttributeMode.INITIAL);
        tmp.put("commentCharacter", Item.AttributeMode.INITIAL);
        tmp.put("encoding", Item.AttributeMode.INITIAL);
        tmp.put("linesToSkip", Item.AttributeMode.INITIAL);
        tmp.put("removeOnSuccess", Item.AttributeMode.INITIAL);
        tmp.put("zipentry", Item.AttributeMode.INITIAL);
        tmp.put("extractionId", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Character getCommentCharacter(SessionContext ctx)
    {
        return (Character)getProperty(ctx, "commentCharacter");
    }


    public Character getCommentCharacter()
    {
        return getCommentCharacter(getSession().getSessionContext());
    }


    public char getCommentCharacterAsPrimitive(SessionContext ctx)
    {
        Character value = getCommentCharacter(ctx);
        return (value != null) ? value.charValue() : Character.MIN_VALUE;
    }


    public char getCommentCharacterAsPrimitive()
    {
        return getCommentCharacterAsPrimitive(getSession().getSessionContext());
    }


    public void setCommentCharacter(SessionContext ctx, Character value)
    {
        setProperty(ctx, "commentCharacter", value);
    }


    public void setCommentCharacter(Character value)
    {
        setCommentCharacter(getSession().getSessionContext(), value);
    }


    public void setCommentCharacter(SessionContext ctx, char value)
    {
        setCommentCharacter(ctx, Character.valueOf(value));
    }


    public void setCommentCharacter(char value)
    {
        setCommentCharacter(getSession().getSessionContext(), value);
    }


    public EnumerationValue getEncoding(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "encoding");
    }


    public EnumerationValue getEncoding()
    {
        return getEncoding(getSession().getSessionContext());
    }


    public void setEncoding(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "encoding", value);
    }


    public void setEncoding(EnumerationValue value)
    {
        setEncoding(getSession().getSessionContext(), value);
    }


    public String getExtractionId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "extractionId");
    }


    public String getExtractionId()
    {
        return getExtractionId(getSession().getSessionContext());
    }


    protected void setExtractionId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'extractionId' is not changeable", 0);
        }
        setProperty(ctx, "extractionId", value);
    }


    protected void setExtractionId(String value)
    {
        setExtractionId(getSession().getSessionContext(), value);
    }


    public Character getFieldSeparator(SessionContext ctx)
    {
        return (Character)getProperty(ctx, "fieldSeparator");
    }


    public Character getFieldSeparator()
    {
        return getFieldSeparator(getSession().getSessionContext());
    }


    public char getFieldSeparatorAsPrimitive(SessionContext ctx)
    {
        Character value = getFieldSeparator(ctx);
        return (value != null) ? value.charValue() : Character.MIN_VALUE;
    }


    public char getFieldSeparatorAsPrimitive()
    {
        return getFieldSeparatorAsPrimitive(getSession().getSessionContext());
    }


    public void setFieldSeparator(SessionContext ctx, Character value)
    {
        setProperty(ctx, "fieldSeparator", value);
    }


    public void setFieldSeparator(Character value)
    {
        setFieldSeparator(getSession().getSessionContext(), value);
    }


    public void setFieldSeparator(SessionContext ctx, char value)
    {
        setFieldSeparator(ctx, Character.valueOf(value));
    }


    public void setFieldSeparator(char value)
    {
        setFieldSeparator(getSession().getSessionContext(), value);
    }


    public Integer getLinesToSkip(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "linesToSkip");
    }


    public Integer getLinesToSkip()
    {
        return getLinesToSkip(getSession().getSessionContext());
    }


    public int getLinesToSkipAsPrimitive(SessionContext ctx)
    {
        Integer value = getLinesToSkip(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getLinesToSkipAsPrimitive()
    {
        return getLinesToSkipAsPrimitive(getSession().getSessionContext());
    }


    public void setLinesToSkip(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "linesToSkip", value);
    }


    public void setLinesToSkip(Integer value)
    {
        setLinesToSkip(getSession().getSessionContext(), value);
    }


    public void setLinesToSkip(SessionContext ctx, int value)
    {
        setLinesToSkip(ctx, Integer.valueOf(value));
    }


    public void setLinesToSkip(int value)
    {
        setLinesToSkip(getSession().getSessionContext(), value);
    }


    public String getPreview()
    {
        return getPreview(getSession().getSessionContext());
    }


    public Character getQuoteCharacter(SessionContext ctx)
    {
        return (Character)getProperty(ctx, "quoteCharacter");
    }


    public Character getQuoteCharacter()
    {
        return getQuoteCharacter(getSession().getSessionContext());
    }


    public char getQuoteCharacterAsPrimitive(SessionContext ctx)
    {
        Character value = getQuoteCharacter(ctx);
        return (value != null) ? value.charValue() : Character.MIN_VALUE;
    }


    public char getQuoteCharacterAsPrimitive()
    {
        return getQuoteCharacterAsPrimitive(getSession().getSessionContext());
    }


    public void setQuoteCharacter(SessionContext ctx, Character value)
    {
        setProperty(ctx, "quoteCharacter", value);
    }


    public void setQuoteCharacter(Character value)
    {
        setQuoteCharacter(getSession().getSessionContext(), value);
    }


    public void setQuoteCharacter(SessionContext ctx, char value)
    {
        setQuoteCharacter(ctx, Character.valueOf(value));
    }


    public void setQuoteCharacter(char value)
    {
        setQuoteCharacter(getSession().getSessionContext(), value);
    }


    public Boolean isRemoveOnSuccess(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "removeOnSuccess");
    }


    public Boolean isRemoveOnSuccess()
    {
        return isRemoveOnSuccess(getSession().getSessionContext());
    }


    public boolean isRemoveOnSuccessAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRemoveOnSuccess(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRemoveOnSuccessAsPrimitive()
    {
        return isRemoveOnSuccessAsPrimitive(getSession().getSessionContext());
    }


    public void setRemoveOnSuccess(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "removeOnSuccess", value);
    }


    public void setRemoveOnSuccess(Boolean value)
    {
        setRemoveOnSuccess(getSession().getSessionContext(), value);
    }


    public void setRemoveOnSuccess(SessionContext ctx, boolean value)
    {
        setRemoveOnSuccess(ctx, Boolean.valueOf(value));
    }


    public void setRemoveOnSuccess(boolean value)
    {
        setRemoveOnSuccess(getSession().getSessionContext(), value);
    }


    public String getZipentry(SessionContext ctx)
    {
        return (String)getProperty(ctx, "zipentry");
    }


    public String getZipentry()
    {
        return getZipentry(getSession().getSessionContext());
    }


    public void setZipentry(SessionContext ctx, String value)
    {
        setProperty(ctx, "zipentry", value);
    }


    public void setZipentry(String value)
    {
        setZipentry(getSession().getSessionContext(), value);
    }


    public abstract String getPreview(SessionContext paramSessionContext);
}
