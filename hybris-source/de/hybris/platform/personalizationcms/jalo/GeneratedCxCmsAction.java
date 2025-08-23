package de.hybris.platform.personalizationcms.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.personalizationservices.jalo.CxAbstractAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxCmsAction extends CxAbstractAction
{
    public static final String COMPONENTID = "componentId";
    public static final String COMPONENTCATALOG = "componentCatalog";
    public static final String CONTAINERID = "containerId";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CxAbstractAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("componentId", Item.AttributeMode.INITIAL);
        tmp.put("componentCatalog", Item.AttributeMode.INITIAL);
        tmp.put("containerId", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getComponentCatalog(SessionContext ctx)
    {
        return (String)getProperty(ctx, "componentCatalog");
    }


    public String getComponentCatalog()
    {
        return getComponentCatalog(getSession().getSessionContext());
    }


    public void setComponentCatalog(SessionContext ctx, String value)
    {
        setProperty(ctx, "componentCatalog", value);
    }


    public void setComponentCatalog(String value)
    {
        setComponentCatalog(getSession().getSessionContext(), value);
    }


    public String getComponentId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "componentId");
    }


    public String getComponentId()
    {
        return getComponentId(getSession().getSessionContext());
    }


    public void setComponentId(SessionContext ctx, String value)
    {
        setProperty(ctx, "componentId", value);
    }


    public void setComponentId(String value)
    {
        setComponentId(getSession().getSessionContext(), value);
    }


    public String getContainerId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "containerId");
    }


    public String getContainerId()
    {
        return getContainerId(getSession().getSessionContext());
    }


    public void setContainerId(SessionContext ctx, String value)
    {
        setProperty(ctx, "containerId", value);
    }


    public void setContainerId(String value)
    {
        setContainerId(getSession().getSessionContext(), value);
    }
}
