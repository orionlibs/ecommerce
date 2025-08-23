package de.hybris.platform.impex.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedImpexDocumentId extends GenericItem
{
    public static final String PROCESSCODE = "processCode";
    public static final String DOCID = "docId";
    public static final String ITEMQUALIFIER = "itemQualifier";
    public static final String ITEMPK = "itemPK";
    public static final String RESOLVED = "resolved";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("processCode", Item.AttributeMode.INITIAL);
        tmp.put("docId", Item.AttributeMode.INITIAL);
        tmp.put("itemQualifier", Item.AttributeMode.INITIAL);
        tmp.put("itemPK", Item.AttributeMode.INITIAL);
        tmp.put("resolved", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getDocId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "docId");
    }


    public String getDocId()
    {
        return getDocId(getSession().getSessionContext());
    }


    public void setDocId(SessionContext ctx, String value)
    {
        setProperty(ctx, "docId", value);
    }


    public void setDocId(String value)
    {
        setDocId(getSession().getSessionContext(), value);
    }


    public PK getItemPK(SessionContext ctx)
    {
        return (PK)getProperty(ctx, "itemPK");
    }


    public PK getItemPK()
    {
        return getItemPK(getSession().getSessionContext());
    }


    public void setItemPK(SessionContext ctx, PK value)
    {
        setProperty(ctx, "itemPK", value);
    }


    public void setItemPK(PK value)
    {
        setItemPK(getSession().getSessionContext(), value);
    }


    public String getItemQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "itemQualifier");
    }


    public String getItemQualifier()
    {
        return getItemQualifier(getSession().getSessionContext());
    }


    public void setItemQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "itemQualifier", value);
    }


    public void setItemQualifier(String value)
    {
        setItemQualifier(getSession().getSessionContext(), value);
    }


    public String getProcessCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "processCode");
    }


    public String getProcessCode()
    {
        return getProcessCode(getSession().getSessionContext());
    }


    public void setProcessCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "processCode", value);
    }


    public void setProcessCode(String value)
    {
        setProcessCode(getSession().getSessionContext(), value);
    }


    public Boolean isResolved(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "resolved");
    }


    public Boolean isResolved()
    {
        return isResolved(getSession().getSessionContext());
    }


    public boolean isResolvedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isResolved(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isResolvedAsPrimitive()
    {
        return isResolvedAsPrimitive(getSession().getSessionContext());
    }


    public void setResolved(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "resolved", value);
    }


    public void setResolved(Boolean value)
    {
        setResolved(getSession().getSessionContext(), value);
    }


    public void setResolved(SessionContext ctx, boolean value)
    {
        setResolved(ctx, Boolean.valueOf(value));
    }


    public void setResolved(boolean value)
    {
        setResolved(getSession().getSessionContext(), value);
    }
}
