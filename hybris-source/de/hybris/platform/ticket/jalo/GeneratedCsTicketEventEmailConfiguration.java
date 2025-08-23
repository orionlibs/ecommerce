package de.hybris.platform.ticket.jalo;

import de.hybris.platform.comments.jalo.CommentType;
import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCsTicketEventEmailConfiguration extends GenericItem
{
    public static final String CODE = "code";
    public static final String PLAINTEXTTEMPLATE = "plainTextTemplate";
    public static final String HTMLTEMPLATE = "htmlTemplate";
    public static final String SUBJECT = "subject";
    public static final String EVENTTYPE = "eventType";
    public static final String ALTEREDATTRIBUTES = "alteredAttributes";
    public static final String RECIPIENTTYPE = "recipientType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("plainTextTemplate", Item.AttributeMode.INITIAL);
        tmp.put("htmlTemplate", Item.AttributeMode.INITIAL);
        tmp.put("subject", Item.AttributeMode.INITIAL);
        tmp.put("eventType", Item.AttributeMode.INITIAL);
        tmp.put("alteredAttributes", Item.AttributeMode.INITIAL);
        tmp.put("recipientType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Set<AttributeDescriptor> getAlteredAttributes(SessionContext ctx)
    {
        Set<AttributeDescriptor> coll = (Set<AttributeDescriptor>)getProperty(ctx, "alteredAttributes");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<AttributeDescriptor> getAlteredAttributes()
    {
        return getAlteredAttributes(getSession().getSessionContext());
    }


    public void setAlteredAttributes(SessionContext ctx, Set<AttributeDescriptor> value)
    {
        setProperty(ctx, "alteredAttributes", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setAlteredAttributes(Set<AttributeDescriptor> value)
    {
        setAlteredAttributes(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public CommentType getEventType(SessionContext ctx)
    {
        return (CommentType)getProperty(ctx, "eventType");
    }


    public CommentType getEventType()
    {
        return getEventType(getSession().getSessionContext());
    }


    public void setEventType(SessionContext ctx, CommentType value)
    {
        setProperty(ctx, "eventType", value);
    }


    public void setEventType(CommentType value)
    {
        setEventType(getSession().getSessionContext(), value);
    }


    public RendererTemplate getHtmlTemplate(SessionContext ctx)
    {
        return (RendererTemplate)getProperty(ctx, "htmlTemplate");
    }


    public RendererTemplate getHtmlTemplate()
    {
        return getHtmlTemplate(getSession().getSessionContext());
    }


    public void setHtmlTemplate(SessionContext ctx, RendererTemplate value)
    {
        setProperty(ctx, "htmlTemplate", value);
    }


    public void setHtmlTemplate(RendererTemplate value)
    {
        setHtmlTemplate(getSession().getSessionContext(), value);
    }


    public RendererTemplate getPlainTextTemplate(SessionContext ctx)
    {
        return (RendererTemplate)getProperty(ctx, "plainTextTemplate");
    }


    public RendererTemplate getPlainTextTemplate()
    {
        return getPlainTextTemplate(getSession().getSessionContext());
    }


    public void setPlainTextTemplate(SessionContext ctx, RendererTemplate value)
    {
        setProperty(ctx, "plainTextTemplate", value);
    }


    public void setPlainTextTemplate(RendererTemplate value)
    {
        setPlainTextTemplate(getSession().getSessionContext(), value);
    }


    public EnumerationValue getRecipientType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "recipientType");
    }


    public EnumerationValue getRecipientType()
    {
        return getRecipientType(getSession().getSessionContext());
    }


    public void setRecipientType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "recipientType", value);
    }


    public void setRecipientType(EnumerationValue value)
    {
        setRecipientType(getSession().getSessionContext(), value);
    }


    public String getSubject(SessionContext ctx)
    {
        return (String)getProperty(ctx, "subject");
    }


    public String getSubject()
    {
        return getSubject(getSession().getSessionContext());
    }


    public void setSubject(SessionContext ctx, String value)
    {
        setProperty(ctx, "subject", value);
    }


    public void setSubject(String value)
    {
        setSubject(getSession().getSessionContext(), value);
    }
}
