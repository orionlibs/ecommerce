package de.hybris.platform.impex.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDistributedImportSplitErrorDump extends ImportBatchContent
{
    public static final String PROCESSCODE = "processCode";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ImportBatchContent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("processCode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getProcessCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "processCode");
    }


    public String getProcessCode()
    {
        return getProcessCode(getSession().getSessionContext());
    }


    protected void setProcessCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'processCode' is not changeable", 0);
        }
        setProperty(ctx, "processCode", value);
    }


    protected void setProcessCode(String value)
    {
        setProcessCode(getSession().getSessionContext(), value);
    }
}
