package de.hybris.platform.personalizationcms.jalo;

import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.cms2.jalo.contents.containers.AbstractCMSComponentContainer;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxCmsComponentContainer extends AbstractCMSComponentContainer
{
    public static final String DEFAULTCMSCOMPONENT = "defaultCmsComponent";
    public static final String SOURCEID = "sourceId";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractCMSComponentContainer.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("defaultCmsComponent", Item.AttributeMode.INITIAL);
        tmp.put("sourceId", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public SimpleCMSComponent getDefaultCmsComponent(SessionContext ctx)
    {
        return (SimpleCMSComponent)getProperty(ctx, "defaultCmsComponent");
    }


    public SimpleCMSComponent getDefaultCmsComponent()
    {
        return getDefaultCmsComponent(getSession().getSessionContext());
    }


    public void setDefaultCmsComponent(SessionContext ctx, SimpleCMSComponent value)
    {
        setProperty(ctx, "defaultCmsComponent", value);
    }


    public void setDefaultCmsComponent(SimpleCMSComponent value)
    {
        setDefaultCmsComponent(getSession().getSessionContext(), value);
    }


    public String getSourceId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sourceId");
    }


    public String getSourceId()
    {
        return getSourceId(getSession().getSessionContext());
    }


    public void setSourceId(SessionContext ctx, String value)
    {
        setProperty(ctx, "sourceId", value);
    }


    public void setSourceId(String value)
    {
        setSourceId(getSession().getSessionContext(), value);
    }
}
