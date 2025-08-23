package de.hybris.platform.catalog.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPreviewTicket extends GenericItem
{
    public static final String PREVIEWCATALOGVERSION = "previewCatalogVersion";
    public static final String VALIDTO = "validTo";
    public static final String CREATEDBY = "createdBy";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("previewCatalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("validTo", Item.AttributeMode.INITIAL);
        tmp.put("createdBy", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public User getCreatedBy(SessionContext ctx)
    {
        return (User)getProperty(ctx, "createdBy");
    }


    public User getCreatedBy()
    {
        return getCreatedBy(getSession().getSessionContext());
    }


    protected void setCreatedBy(SessionContext ctx, User value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'createdBy' is not changeable", 0);
        }
        setProperty(ctx, "createdBy", value);
    }


    protected void setCreatedBy(User value)
    {
        setCreatedBy(getSession().getSessionContext(), value);
    }


    public CatalogVersion getPreviewCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "previewCatalogVersion");
    }


    public CatalogVersion getPreviewCatalogVersion()
    {
        return getPreviewCatalogVersion(getSession().getSessionContext());
    }


    protected void setPreviewCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'previewCatalogVersion' is not changeable", 0);
        }
        setProperty(ctx, "previewCatalogVersion", value);
    }


    protected void setPreviewCatalogVersion(CatalogVersion value)
    {
        setPreviewCatalogVersion(getSession().getSessionContext(), value);
    }


    public Date getValidTo(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "validTo");
    }


    public Date getValidTo()
    {
        return getValidTo(getSession().getSessionContext());
    }


    public void setValidTo(SessionContext ctx, Date value)
    {
        setProperty(ctx, "validTo", value);
    }


    public void setValidTo(Date value)
    {
        setValidTo(getSession().getSessionContext(), value);
    }
}
