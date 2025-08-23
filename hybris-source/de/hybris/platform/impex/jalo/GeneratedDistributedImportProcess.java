package de.hybris.platform.impex.jalo;

import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.processing.jalo.DistributedProcess;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDistributedImportProcess extends DistributedProcess
{
    public static final String IMPEXIMPORTCRONJOB = "impExImportCronJob";
    public static final String METADATA = "metadata";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(DistributedProcess.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("impExImportCronJob", Item.AttributeMode.INITIAL);
        tmp.put("metadata", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public ImpExImportCronJob getImpExImportCronJob(SessionContext ctx)
    {
        return (ImpExImportCronJob)getProperty(ctx, "impExImportCronJob");
    }


    public ImpExImportCronJob getImpExImportCronJob()
    {
        return getImpExImportCronJob(getSession().getSessionContext());
    }


    public void setImpExImportCronJob(SessionContext ctx, ImpExImportCronJob value)
    {
        setProperty(ctx, "impExImportCronJob", value);
    }


    public void setImpExImportCronJob(ImpExImportCronJob value)
    {
        setImpExImportCronJob(getSession().getSessionContext(), value);
    }


    public String getMetadata(SessionContext ctx)
    {
        return (String)getProperty(ctx, "metadata");
    }


    public String getMetadata()
    {
        return getMetadata(getSession().getSessionContext());
    }


    protected void setMetadata(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'metadata' is not changeable", 0);
        }
        setProperty(ctx, "metadata", value);
    }


    protected void setMetadata(String value)
    {
        setMetadata(getSession().getSessionContext(), value);
    }
}
