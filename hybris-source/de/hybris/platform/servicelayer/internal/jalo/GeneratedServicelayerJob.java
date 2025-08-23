package de.hybris.platform.servicelayer.internal.jalo;

import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedServicelayerJob extends Job
{
    public static final String SPRINGID = "springId";
    public static final String SPRINGIDCRONJOBFACTORY = "springIdCronJobFactory";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Job.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("springId", Item.AttributeMode.INITIAL);
        tmp.put("springIdCronJobFactory", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getSpringId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "springId");
    }


    public String getSpringId()
    {
        return getSpringId(getSession().getSessionContext());
    }


    public void setSpringId(SessionContext ctx, String value)
    {
        setProperty(ctx, "springId", value);
    }


    public void setSpringId(String value)
    {
        setSpringId(getSession().getSessionContext(), value);
    }


    public String getSpringIdCronJobFactory(SessionContext ctx)
    {
        return (String)getProperty(ctx, "springIdCronJobFactory");
    }


    public String getSpringIdCronJobFactory()
    {
        return getSpringIdCronJobFactory(getSession().getSessionContext());
    }


    public void setSpringIdCronJobFactory(SessionContext ctx, String value)
    {
        setProperty(ctx, "springIdCronJobFactory", value);
    }


    public void setSpringIdCronJobFactory(String value)
    {
        setSpringIdCronJobFactory(getSession().getSessionContext(), value);
    }
}
