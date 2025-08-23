package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAutomatedWorkflowActionTemplate extends WorkflowActionTemplate
{
    public static final String JOBCLASS = "jobClass";
    public static final String JOBHANDLER = "jobHandler";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(WorkflowActionTemplate.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("jobClass", Item.AttributeMode.INITIAL);
        tmp.put("jobHandler", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Class getJobClass(SessionContext ctx)
    {
        return (Class)getProperty(ctx, "jobClass");
    }


    public Class getJobClass()
    {
        return getJobClass(getSession().getSessionContext());
    }


    public void setJobClass(SessionContext ctx, Class value)
    {
        setProperty(ctx, "jobClass", value);
    }


    public void setJobClass(Class value)
    {
        setJobClass(getSession().getSessionContext(), value);
    }


    public String getJobHandler(SessionContext ctx)
    {
        return (String)getProperty(ctx, "jobHandler");
    }


    public String getJobHandler()
    {
        return getJobHandler(getSession().getSessionContext());
    }


    public void setJobHandler(SessionContext ctx, String value)
    {
        setProperty(ctx, "jobHandler", value);
    }


    public void setJobHandler(String value)
    {
        setJobHandler(getSession().getSessionContext(), value);
    }
}
