package de.hybris.platform.impex.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedExternalImportKey extends GenericItem
{
    public static final String SOURCESYSTEMID = "sourceSystemID";
    public static final String SOURCEKEY = "sourceKey";
    public static final String TARGETPK = "targetPK";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("sourceSystemID", Item.AttributeMode.INITIAL);
        tmp.put("sourceKey", Item.AttributeMode.INITIAL);
        tmp.put("targetPK", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getSourceKey(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sourceKey");
    }


    public String getSourceKey()
    {
        return getSourceKey(getSession().getSessionContext());
    }


    public void setSourceKey(SessionContext ctx, String value)
    {
        setProperty(ctx, "sourceKey", value);
    }


    public void setSourceKey(String value)
    {
        setSourceKey(getSession().getSessionContext(), value);
    }


    public String getSourceSystemID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sourceSystemID");
    }


    public String getSourceSystemID()
    {
        return getSourceSystemID(getSession().getSessionContext());
    }


    public void setSourceSystemID(SessionContext ctx, String value)
    {
        setProperty(ctx, "sourceSystemID", value);
    }


    public void setSourceSystemID(String value)
    {
        setSourceSystemID(getSession().getSessionContext(), value);
    }


    public PK getTargetPK(SessionContext ctx)
    {
        return (PK)getProperty(ctx, "targetPK");
    }


    public PK getTargetPK()
    {
        return getTargetPK(getSession().getSessionContext());
    }


    public void setTargetPK(SessionContext ctx, PK value)
    {
        setProperty(ctx, "targetPK", value);
    }


    public void setTargetPK(PK value)
    {
        setTargetPK(getSession().getSessionContext(), value);
    }
}
