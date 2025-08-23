package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.category.jalo.Category;
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

public abstract class GeneratedAsCategoryAwareSearchConfiguration extends AbstractAsConfigurableSearchConfiguration
{
    public static final String CATEGORY = "category";
    public static final String SEARCHPROFILE = "searchProfile";
    protected static final BidirectionalOneToManyHandler<GeneratedAsCategoryAwareSearchConfiguration> SEARCHPROFILEHANDLER = new BidirectionalOneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASCATEGORYAWARESEARCHCONFIGURATION, false, "searchProfile", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsConfigurableSearchConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("category", Item.AttributeMode.INITIAL);
        tmp.put("searchProfile", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Category getCategory(SessionContext ctx)
    {
        return (Category)getProperty(ctx, "category");
    }


    public Category getCategory()
    {
        return getCategory(getSession().getSessionContext());
    }


    protected void setCategory(SessionContext ctx, Category value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'category' is not changeable", 0);
        }
        setProperty(ctx, "category", value);
    }


    protected void setCategory(Category value)
    {
        setCategory(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SEARCHPROFILEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public AsCategoryAwareSearchProfile getSearchProfile(SessionContext ctx)
    {
        return (AsCategoryAwareSearchProfile)getProperty(ctx, "searchProfile");
    }


    public AsCategoryAwareSearchProfile getSearchProfile()
    {
        return getSearchProfile(getSession().getSessionContext());
    }


    protected void setSearchProfile(SessionContext ctx, AsCategoryAwareSearchProfile value)
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


    protected void setSearchProfile(AsCategoryAwareSearchProfile value)
    {
        setSearchProfile(getSession().getSessionContext(), value);
    }
}
