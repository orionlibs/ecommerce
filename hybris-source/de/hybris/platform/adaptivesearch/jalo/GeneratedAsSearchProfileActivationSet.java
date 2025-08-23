package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAsSearchProfileActivationSet extends GenericItem
{
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String INDEXTYPE = "indexType";
    public static final String PRIORITY = "priority";
    public static final String SEARCHPROFILES = "searchProfiles";
    protected static final OneToManyHandler<AbstractAsSearchProfile> SEARCHPROFILESHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ABSTRACTASSEARCHPROFILE, false, "activationSet", "activationSetPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("indexType", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    protected void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'catalogVersion' is not changeable", 0);
        }
        setProperty(ctx, "catalogVersion", value);
    }


    protected void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
    }


    public String getIndexType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexType");
    }


    public String getIndexType()
    {
        return getIndexType(getSession().getSessionContext());
    }


    protected void setIndexType(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'indexType' is not changeable", 0);
        }
        setProperty(ctx, "indexType", value);
    }


    protected void setIndexType(String value)
    {
        setIndexType(getSession().getSessionContext(), value);
    }


    public Integer getPriority(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "priority");
    }


    public Integer getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public int getPriorityAsPrimitive(SessionContext ctx)
    {
        Integer value = getPriority(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityAsPrimitive()
    {
        return getPriorityAsPrimitive(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(Integer value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public void setPriority(SessionContext ctx, int value)
    {
        setPriority(ctx, Integer.valueOf(value));
    }


    public void setPriority(int value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public List<AbstractAsSearchProfile> getSearchProfiles(SessionContext ctx)
    {
        return (List<AbstractAsSearchProfile>)SEARCHPROFILESHANDLER.getValues(ctx, (Item)this);
    }


    public List<AbstractAsSearchProfile> getSearchProfiles()
    {
        return getSearchProfiles(getSession().getSessionContext());
    }


    public void setSearchProfiles(SessionContext ctx, List<AbstractAsSearchProfile> value)
    {
        SEARCHPROFILESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSearchProfiles(List<AbstractAsSearchProfile> value)
    {
        setSearchProfiles(getSession().getSessionContext(), value);
    }


    public void addToSearchProfiles(SessionContext ctx, AbstractAsSearchProfile value)
    {
        SEARCHPROFILESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSearchProfiles(AbstractAsSearchProfile value)
    {
        addToSearchProfiles(getSession().getSessionContext(), value);
    }


    public void removeFromSearchProfiles(SessionContext ctx, AbstractAsSearchProfile value)
    {
        SEARCHPROFILESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSearchProfiles(AbstractAsSearchProfile value)
    {
        removeFromSearchProfiles(getSession().getSessionContext(), value);
    }
}
