package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAsSimpleSearchConfiguration extends AbstractAsConfigurableSearchConfiguration
{
    public static final String SEARCHPROFILE = "searchProfile";
    protected static final BidirectionalOneToManyHandler<GeneratedAsSimpleSearchConfiguration> SEARCHPROFILEHANDLER = new BidirectionalOneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASSIMPLESEARCHCONFIGURATION, false, "searchProfile", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsConfigurableSearchConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("searchProfile", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SEARCHPROFILEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public AsSimpleSearchProfile getSearchProfile(SessionContext ctx)
    {
        return (AsSimpleSearchProfile)getProperty(ctx, "searchProfile");
    }


    public AsSimpleSearchProfile getSearchProfile()
    {
        return getSearchProfile(getSession().getSessionContext());
    }


    protected void setSearchProfile(SessionContext ctx, AsSimpleSearchProfile value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'searchProfile' is not changeable", 0);
        }
        SEARCHPROFILEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setSearchProfile(AsSimpleSearchProfile value)
    {
        setSearchProfile(getSession().getSessionContext(), value);
    }
}
