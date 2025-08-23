package com.hybris.backoffice.jalo;

import com.hybris.backoffice.constants.GeneratedBackofficeConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedBackofficeObjectSpecialCollection extends GenericItem
{
    public static final String COLLECTIONTYPE = "collectionType";
    public static final String USER = "user";
    public static final String ELEMENTS = "elements";
    protected static final BidirectionalOneToManyHandler<GeneratedBackofficeObjectSpecialCollection> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedBackofficeConstants.TC.BACKOFFICEOBJECTSPECIALCOLLECTION, false, "user", null, false, true, 0);
    protected static final OneToManyHandler<BackofficeObjectCollectionItemReference> ELEMENTSHANDLER = new OneToManyHandler(GeneratedBackofficeConstants.TC.BACKOFFICEOBJECTCOLLECTIONITEMREFERENCE, true, "collectionPk", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("collectionType", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getCollectionType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "collectionType");
    }


    public EnumerationValue getCollectionType()
    {
        return getCollectionType(getSession().getSessionContext());
    }


    public void setCollectionType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "collectionType", value);
    }


    public void setCollectionType(EnumerationValue value)
    {
        setCollectionType(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public List<BackofficeObjectCollectionItemReference> getElements(SessionContext ctx)
    {
        return (List<BackofficeObjectCollectionItemReference>)ELEMENTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<BackofficeObjectCollectionItemReference> getElements()
    {
        return getElements(getSession().getSessionContext());
    }


    public void setElements(SessionContext ctx, List<BackofficeObjectCollectionItemReference> value)
    {
        ELEMENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setElements(List<BackofficeObjectCollectionItemReference> value)
    {
        setElements(getSession().getSessionContext(), value);
    }


    public void addToElements(SessionContext ctx, BackofficeObjectCollectionItemReference value)
    {
        ELEMENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToElements(BackofficeObjectCollectionItemReference value)
    {
        addToElements(getSession().getSessionContext(), value);
    }


    public void removeFromElements(SessionContext ctx, BackofficeObjectCollectionItemReference value)
    {
        ELEMENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromElements(BackofficeObjectCollectionItemReference value)
    {
        removeFromElements(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        USERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
