package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedBatchJob extends Job
{
    public static final String STEPS = "steps";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Job.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<Step> getSteps()
    {
        return getSteps(getSession().getSessionContext());
    }


    public void setSteps(List<Step> value)
    {
        setSteps(getSession().getSessionContext(), value);
    }


    public abstract List<Step> getSteps(SessionContext paramSessionContext);


    public abstract void setSteps(SessionContext paramSessionContext, List<Step> paramList);
}
