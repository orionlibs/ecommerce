package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAsSimpleSearchProfile extends AbstractAsSearchProfile
{
    public static final String SEARCHCONFIGURATIONS = "searchConfigurations";
    protected static final OneToManyHandler<AsSimpleSearchConfiguration> SEARCHCONFIGURATIONSHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASSIMPLESEARCHCONFIGURATION, true, "searchProfile", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsSearchProfile.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<AsSimpleSearchConfiguration> getSearchConfigurations(SessionContext ctx)
    {
        return (List<AsSimpleSearchConfiguration>)SEARCHCONFIGURATIONSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsSimpleSearchConfiguration> getSearchConfigurations()
    {
        return getSearchConfigurations(getSession().getSessionContext());
    }


    public void setSearchConfigurations(SessionContext ctx, List<AsSimpleSearchConfiguration> value)
    {
        SEARCHCONFIGURATIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSearchConfigurations(List<AsSimpleSearchConfiguration> value)
    {
        setSearchConfigurations(getSession().getSessionContext(), value);
    }


    public void addToSearchConfigurations(SessionContext ctx, AsSimpleSearchConfiguration value)
    {
        SEARCHCONFIGURATIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSearchConfigurations(AsSimpleSearchConfiguration value)
    {
        addToSearchConfigurations(getSession().getSessionContext(), value);
    }


    public void removeFromSearchConfigurations(SessionContext ctx, AsSimpleSearchConfiguration value)
    {
        SEARCHCONFIGURATIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSearchConfigurations(AsSimpleSearchConfiguration value)
    {
        removeFromSearchConfigurations(getSession().getSessionContext(), value);
    }
}
