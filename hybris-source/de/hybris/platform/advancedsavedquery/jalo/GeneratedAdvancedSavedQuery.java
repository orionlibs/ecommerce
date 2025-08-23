package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.advancedsavedquery.constants.GeneratedASQConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.SavedQuery;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAdvancedSavedQuery extends SavedQuery
{
    public static final String GENERATEDFLEXIBLESEARCH = "generatedFlexibleSearch";
    public static final String WHEREPARTS = "whereparts";
    protected static final OneToManyHandler<WherePart> WHEREPARTSHANDLER = new OneToManyHandler(GeneratedASQConstants.TC.WHEREPART, true, "savedQuery", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SavedQuery.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getGeneratedFlexibleSearch()
    {
        return getGeneratedFlexibleSearch(getSession().getSessionContext());
    }


    public Collection<WherePart> getWhereparts(SessionContext ctx)
    {
        return WHEREPARTSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<WherePart> getWhereparts()
    {
        return getWhereparts(getSession().getSessionContext());
    }


    public void setWhereparts(SessionContext ctx, Collection<WherePart> value)
    {
        WHEREPARTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setWhereparts(Collection<WherePart> value)
    {
        setWhereparts(getSession().getSessionContext(), value);
    }


    public void addToWhereparts(SessionContext ctx, WherePart value)
    {
        WHEREPARTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToWhereparts(WherePart value)
    {
        addToWhereparts(getSession().getSessionContext(), value);
    }


    public void removeFromWhereparts(SessionContext ctx, WherePart value)
    {
        WHEREPARTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromWhereparts(WherePart value)
    {
        removeFromWhereparts(getSession().getSessionContext(), value);
    }


    public abstract String getGeneratedFlexibleSearch(SessionContext paramSessionContext);
}
