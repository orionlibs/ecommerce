package de.hybris.platform.personalizationservices.jalo.process;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCxDefaultPersonalizationCalculationCronJob extends CronJob
{
    public static final String BASESITES = "baseSites";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("baseSites", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Set<BaseSite> getBaseSites(SessionContext ctx)
    {
        Set<BaseSite> coll = (Set<BaseSite>)getProperty(ctx, "baseSites");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<BaseSite> getBaseSites()
    {
        return getBaseSites(getSession().getSessionContext());
    }


    public void setBaseSites(SessionContext ctx, Set<BaseSite> value)
    {
        setProperty(ctx, "baseSites", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setBaseSites(Set<BaseSite> value)
    {
        setBaseSites(getSession().getSessionContext(), value);
    }
}
