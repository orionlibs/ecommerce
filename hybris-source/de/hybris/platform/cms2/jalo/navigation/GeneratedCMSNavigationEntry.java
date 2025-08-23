package de.hybris.platform.cms2.jalo.navigation;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCMSNavigationEntry extends CMSItem
{
    public static final String ITEM = "item";
    public static final String NAVIGATIONNODEPOS = "navigationNodePOS";
    public static final String NAVIGATIONNODE = "navigationNode";
    protected static final BidirectionalOneToManyHandler<GeneratedCMSNavigationEntry> NAVIGATIONNODEHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.CMSNAVIGATIONENTRY, false, "navigationNode", "navigationNodePOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CMSItem.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("item", Item.AttributeMode.INITIAL);
        tmp.put("navigationNodePOS", Item.AttributeMode.INITIAL);
        tmp.put("navigationNode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        NAVIGATIONNODEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Item getItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "item");
    }


    public Item getItem()
    {
        return getItem(getSession().getSessionContext());
    }


    public void setItem(SessionContext ctx, Item value)
    {
        setProperty(ctx, "item", value);
    }


    public void setItem(Item value)
    {
        setItem(getSession().getSessionContext(), value);
    }


    public CMSNavigationNode getNavigationNode(SessionContext ctx)
    {
        return (CMSNavigationNode)getProperty(ctx, "navigationNode");
    }


    public CMSNavigationNode getNavigationNode()
    {
        return getNavigationNode(getSession().getSessionContext());
    }


    public void setNavigationNode(SessionContext ctx, CMSNavigationNode value)
    {
        NAVIGATIONNODEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setNavigationNode(CMSNavigationNode value)
    {
        setNavigationNode(getSession().getSessionContext(), value);
    }


    Integer getNavigationNodePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "navigationNodePOS");
    }


    Integer getNavigationNodePOS()
    {
        return getNavigationNodePOS(getSession().getSessionContext());
    }


    int getNavigationNodePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getNavigationNodePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getNavigationNodePOSAsPrimitive()
    {
        return getNavigationNodePOSAsPrimitive(getSession().getSessionContext());
    }


    void setNavigationNodePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "navigationNodePOS", value);
    }


    void setNavigationNodePOS(Integer value)
    {
        setNavigationNodePOS(getSession().getSessionContext(), value);
    }


    void setNavigationNodePOS(SessionContext ctx, int value)
    {
        setNavigationNodePOS(ctx, Integer.valueOf(value));
    }


    void setNavigationNodePOS(int value)
    {
        setNavigationNodePOS(getSession().getSessionContext(), value);
    }
}
