package de.hybris.platform.cms2.jalo.preview;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCMSPreviewTicket extends GenericItem
{
    public static final String ID = "id";
    public static final String PREVIEWDATA = "previewData";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("previewData", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "id");
    }


    public String getId()
    {
        return getId(getSession().getSessionContext());
    }


    public void setId(SessionContext ctx, String value)
    {
        setProperty(ctx, "id", value);
    }


    public void setId(String value)
    {
        setId(getSession().getSessionContext(), value);
    }


    public PreviewData getPreviewData(SessionContext ctx)
    {
        return (PreviewData)getProperty(ctx, "previewData");
    }


    public PreviewData getPreviewData()
    {
        return getPreviewData(getSession().getSessionContext());
    }


    public void setPreviewData(SessionContext ctx, PreviewData value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setPreviewData(PreviewData value)
    {
        setPreviewData(getSession().getSessionContext(), value);
    }
}
