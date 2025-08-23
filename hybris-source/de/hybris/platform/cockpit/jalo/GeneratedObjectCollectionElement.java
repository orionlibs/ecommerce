package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedObjectCollectionElement extends GenericItem
{
    public static final String OBJECTTYPECODE = "objectTypeCode";
    public static final String POSITION = "position";
    public static final String COLLECTION = "collection";
    protected static final BidirectionalOneToManyHandler<GeneratedObjectCollectionElement> COLLECTIONHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.OBJECTCOLLECTIONELEMENT, false, "collection", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("objectTypeCode", Item.AttributeMode.INITIAL);
        tmp.put("collection", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CockpitObjectAbstractCollection getCollection(SessionContext ctx)
    {
        return (CockpitObjectAbstractCollection)getProperty(ctx, "collection");
    }


    public CockpitObjectAbstractCollection getCollection()
    {
        return getCollection(getSession().getSessionContext());
    }


    public void setCollection(SessionContext ctx, CockpitObjectAbstractCollection value)
    {
        COLLECTIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCollection(CockpitObjectAbstractCollection value)
    {
        setCollection(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        COLLECTIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getObjectTypeCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "objectTypeCode");
    }


    public String getObjectTypeCode()
    {
        return getObjectTypeCode(getSession().getSessionContext());
    }


    public void setObjectTypeCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "objectTypeCode", value);
    }


    public void setObjectTypeCode(String value)
    {
        setObjectTypeCode(getSession().getSessionContext(), value);
    }


    public Integer getPosition()
    {
        return getPosition(getSession().getSessionContext());
    }


    public int getPositionAsPrimitive(SessionContext ctx)
    {
        Integer value = getPosition(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPositionAsPrimitive()
    {
        return getPositionAsPrimitive(getSession().getSessionContext());
    }


    public abstract Integer getPosition(SessionContext paramSessionContext);
}
