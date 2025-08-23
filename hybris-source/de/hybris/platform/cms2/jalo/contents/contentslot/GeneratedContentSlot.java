package de.hybris.platform.cms2.jalo.contents.contentslot;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.cms2.jalo.contents.components.AbstractCMSComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedContentSlot extends CMSItem
{
    public static final String ACTIVE = "active";
    public static final String ACTIVEFROM = "activeFrom";
    public static final String ACTIVEUNTIL = "activeUntil";
    public static final String CURRENTPOSITION = "currentPosition";
    public static final String ORIGINALSLOT = "originalSlot";
    public static final String CMSCOMPONENTS = "cmsComponents";
    protected static String ELEMENTSFORSLOT_SRC_ORDERED = "relation.ElementsForSlot.source.ordered";
    protected static String ELEMENTSFORSLOT_TGT_ORDERED = "relation.ElementsForSlot.target.ordered";
    protected static String ELEMENTSFORSLOT_MARKMODIFIED = "relation.ElementsForSlot.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CMSItem.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("activeFrom", Item.AttributeMode.INITIAL);
        tmp.put("activeUntil", Item.AttributeMode.INITIAL);
        tmp.put("originalSlot", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public Date getActiveFrom(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "activeFrom");
    }


    public Date getActiveFrom()
    {
        return getActiveFrom(getSession().getSessionContext());
    }


    public void setActiveFrom(SessionContext ctx, Date value)
    {
        setProperty(ctx, "activeFrom", value);
    }


    public void setActiveFrom(Date value)
    {
        setActiveFrom(getSession().getSessionContext(), value);
    }


    public Date getActiveUntil(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "activeUntil");
    }


    public Date getActiveUntil()
    {
        return getActiveUntil(getSession().getSessionContext());
    }


    public void setActiveUntil(SessionContext ctx, Date value)
    {
        setProperty(ctx, "activeUntil", value);
    }


    public void setActiveUntil(Date value)
    {
        setActiveUntil(getSession().getSessionContext(), value);
    }


    public List<AbstractCMSComponent> getCmsComponents(SessionContext ctx)
    {
        List<AbstractCMSComponent> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.ELEMENTSFORSLOT, "AbstractCMSComponent", null,
                        Utilities.getRelationOrderingOverride(ELEMENTSFORSLOT_SRC_ORDERED, true), false);
        return items;
    }


    public List<AbstractCMSComponent> getCmsComponents()
    {
        return getCmsComponents(getSession().getSessionContext());
    }


    public long getCmsComponentsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.ELEMENTSFORSLOT, "AbstractCMSComponent", null);
    }


    public long getCmsComponentsCount()
    {
        return getCmsComponentsCount(getSession().getSessionContext());
    }


    public void setCmsComponents(SessionContext ctx, List<AbstractCMSComponent> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.ELEMENTSFORSLOT, null, value,
                        Utilities.getRelationOrderingOverride(ELEMENTSFORSLOT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORSLOT_MARKMODIFIED));
    }


    public void setCmsComponents(List<AbstractCMSComponent> value)
    {
        setCmsComponents(getSession().getSessionContext(), value);
    }


    public void addToCmsComponents(SessionContext ctx, AbstractCMSComponent value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.ELEMENTSFORSLOT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ELEMENTSFORSLOT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORSLOT_MARKMODIFIED));
    }


    public void addToCmsComponents(AbstractCMSComponent value)
    {
        addToCmsComponents(getSession().getSessionContext(), value);
    }


    public void removeFromCmsComponents(SessionContext ctx, AbstractCMSComponent value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.ELEMENTSFORSLOT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ELEMENTSFORSLOT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORSLOT_MARKMODIFIED));
    }


    public void removeFromCmsComponents(AbstractCMSComponent value)
    {
        removeFromCmsComponents(getSession().getSessionContext(), value);
    }


    public String getCurrentPosition()
    {
        return getCurrentPosition(getSession().getSessionContext());
    }


    public void setCurrentPosition(String value)
    {
        setCurrentPosition(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("AbstractCMSComponent");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(ELEMENTSFORSLOT_MARKMODIFIED);
        }
        return true;
    }


    public ContentSlot getOriginalSlot(SessionContext ctx)
    {
        return (ContentSlot)getProperty(ctx, "originalSlot");
    }


    public ContentSlot getOriginalSlot()
    {
        return getOriginalSlot(getSession().getSessionContext());
    }


    public void setOriginalSlot(SessionContext ctx, ContentSlot value)
    {
        setProperty(ctx, "originalSlot", value);
    }


    public void setOriginalSlot(ContentSlot value)
    {
        setOriginalSlot(getSession().getSessionContext(), value);
    }


    public abstract String getCurrentPosition(SessionContext paramSessionContext);


    public abstract void setCurrentPosition(SessionContext paramSessionContext, String paramString);
}
