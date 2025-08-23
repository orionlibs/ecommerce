package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.cms2.jalo.contents.contentslot.ContentSlot;
import de.hybris.platform.cms2.jalo.restrictions.AbstractRestriction;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAbstractCMSComponent extends CMSItem
{
    public static final String VISIBLE = "visible";
    public static final String ONLYONERESTRICTIONMUSTAPPLY = "onlyOneRestrictionMustApply";
    public static final String STYLECLASSES = "styleClasses";
    public static final String TYPE = "type";
    public static final String TYPECODE = "typeCode";
    public static final String CONTAINER = "container";
    public static final String RESTRICTED = "restricted";
    public static final String SLOTS = "slots";
    protected static String ELEMENTSFORSLOT_SRC_ORDERED = "relation.ElementsForSlot.source.ordered";
    protected static String ELEMENTSFORSLOT_TGT_ORDERED = "relation.ElementsForSlot.target.ordered";
    protected static String ELEMENTSFORSLOT_MARKMODIFIED = "relation.ElementsForSlot.markmodified";
    public static final String PARENTS = "parents";
    protected static String CMSCOMPONENTCHILDRENFORCMSCOMPONENT_SRC_ORDERED = "relation.CMSComponentChildrenForCMSComponent.source.ordered";
    protected static String CMSCOMPONENTCHILDRENFORCMSCOMPONENT_TGT_ORDERED = "relation.CMSComponentChildrenForCMSComponent.target.ordered";
    protected static String CMSCOMPONENTCHILDRENFORCMSCOMPONENT_MARKMODIFIED = "relation.CMSComponentChildrenForCMSComponent.markmodified";
    public static final String CHILDREN = "children";
    public static final String RESTRICTIONS = "restrictions";
    protected static String RESTRICTIONSFORCOMPONENTS_SRC_ORDERED = "relation.RestrictionsForComponents.source.ordered";
    protected static String RESTRICTIONSFORCOMPONENTS_TGT_ORDERED = "relation.RestrictionsForComponents.target.ordered";
    protected static String RESTRICTIONSFORCOMPONENTS_MARKMODIFIED = "relation.RestrictionsForComponents.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CMSItem.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("visible", Item.AttributeMode.INITIAL);
        tmp.put("onlyOneRestrictionMustApply", Item.AttributeMode.INITIAL);
        tmp.put("styleClasses", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<AbstractCMSComponent> getChildren(SessionContext ctx)
    {
        List<AbstractCMSComponent> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSCOMPONENTCHILDRENFORCMSCOMPONENT, "AbstractCMSComponent", null,
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_TGT_ORDERED, true));
        return items;
    }


    public List<AbstractCMSComponent> getChildren()
    {
        return getChildren(getSession().getSessionContext());
    }


    public long getChildrenCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.CMSCOMPONENTCHILDRENFORCMSCOMPONENT, "AbstractCMSComponent", null);
    }


    public long getChildrenCount()
    {
        return getChildrenCount(getSession().getSessionContext());
    }


    public void setChildren(SessionContext ctx, List<AbstractCMSComponent> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSCOMPONENTCHILDRENFORCMSCOMPONENT, null, value,
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_MARKMODIFIED));
    }


    public void setChildren(List<AbstractCMSComponent> value)
    {
        setChildren(getSession().getSessionContext(), value);
    }


    public void addToChildren(SessionContext ctx, AbstractCMSComponent value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSCOMPONENTCHILDRENFORCMSCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_MARKMODIFIED));
    }


    public void addToChildren(AbstractCMSComponent value)
    {
        addToChildren(getSession().getSessionContext(), value);
    }


    public void removeFromChildren(SessionContext ctx, AbstractCMSComponent value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSCOMPONENTCHILDRENFORCMSCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_MARKMODIFIED));
    }


    public void removeFromChildren(AbstractCMSComponent value)
    {
        removeFromChildren(getSession().getSessionContext(), value);
    }


    public Boolean isContainer()
    {
        return isContainer(getSession().getSessionContext());
    }


    public boolean isContainerAsPrimitive(SessionContext ctx)
    {
        Boolean value = isContainer(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isContainerAsPrimitive()
    {
        return isContainerAsPrimitive(getSession().getSessionContext());
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("ContentSlot");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(ELEMENTSFORSLOT_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("AbstractCMSComponent");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_MARKMODIFIED);
        }
        ComposedType relationSecondEnd2 = TypeManager.getInstance().getComposedType("AbstractRestriction");
        if(relationSecondEnd2.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(RESTRICTIONSFORCOMPONENTS_MARKMODIFIED);
        }
        return true;
    }


    public Boolean isOnlyOneRestrictionMustApply(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "onlyOneRestrictionMustApply");
    }


    public Boolean isOnlyOneRestrictionMustApply()
    {
        return isOnlyOneRestrictionMustApply(getSession().getSessionContext());
    }


    public boolean isOnlyOneRestrictionMustApplyAsPrimitive(SessionContext ctx)
    {
        Boolean value = isOnlyOneRestrictionMustApply(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isOnlyOneRestrictionMustApplyAsPrimitive()
    {
        return isOnlyOneRestrictionMustApplyAsPrimitive(getSession().getSessionContext());
    }


    public void setOnlyOneRestrictionMustApply(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "onlyOneRestrictionMustApply", value);
    }


    public void setOnlyOneRestrictionMustApply(Boolean value)
    {
        setOnlyOneRestrictionMustApply(getSession().getSessionContext(), value);
    }


    public void setOnlyOneRestrictionMustApply(SessionContext ctx, boolean value)
    {
        setOnlyOneRestrictionMustApply(ctx, Boolean.valueOf(value));
    }


    public void setOnlyOneRestrictionMustApply(boolean value)
    {
        setOnlyOneRestrictionMustApply(getSession().getSessionContext(), value);
    }


    public List<AbstractCMSComponent> getParents(SessionContext ctx)
    {
        List<AbstractCMSComponent> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSCOMPONENTCHILDRENFORCMSCOMPONENT, "AbstractCMSComponent", null,
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_TGT_ORDERED, true));
        return items;
    }


    public List<AbstractCMSComponent> getParents()
    {
        return getParents(getSession().getSessionContext());
    }


    public long getParentsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.CMSCOMPONENTCHILDRENFORCMSCOMPONENT, "AbstractCMSComponent", null);
    }


    public long getParentsCount()
    {
        return getParentsCount(getSession().getSessionContext());
    }


    public void setParents(SessionContext ctx, List<AbstractCMSComponent> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSCOMPONENTCHILDRENFORCMSCOMPONENT, null, value,
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_MARKMODIFIED));
    }


    public void setParents(List<AbstractCMSComponent> value)
    {
        setParents(getSession().getSessionContext(), value);
    }


    public void addToParents(SessionContext ctx, AbstractCMSComponent value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSCOMPONENTCHILDRENFORCMSCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_MARKMODIFIED));
    }


    public void addToParents(AbstractCMSComponent value)
    {
        addToParents(getSession().getSessionContext(), value);
    }


    public void removeFromParents(SessionContext ctx, AbstractCMSComponent value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSCOMPONENTCHILDRENFORCMSCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCOMPONENTCHILDRENFORCMSCOMPONENT_MARKMODIFIED));
    }


    public void removeFromParents(AbstractCMSComponent value)
    {
        removeFromParents(getSession().getSessionContext(), value);
    }


    public Boolean isRestricted()
    {
        return isRestricted(getSession().getSessionContext());
    }


    public boolean isRestrictedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRestricted(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRestrictedAsPrimitive()
    {
        return isRestrictedAsPrimitive(getSession().getSessionContext());
    }


    public List<AbstractRestriction> getRestrictions(SessionContext ctx)
    {
        List<AbstractRestriction> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.RESTRICTIONSFORCOMPONENTS, "AbstractRestriction", null,
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORCOMPONENTS_SRC_ORDERED, true), false);
        return items;
    }


    public List<AbstractRestriction> getRestrictions()
    {
        return getRestrictions(getSession().getSessionContext());
    }


    public long getRestrictionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.RESTRICTIONSFORCOMPONENTS, "AbstractRestriction", null);
    }


    public long getRestrictionsCount()
    {
        return getRestrictionsCount(getSession().getSessionContext());
    }


    public void setRestrictions(SessionContext ctx, List<AbstractRestriction> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.RESTRICTIONSFORCOMPONENTS, null, value,
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORCOMPONENTS_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORCOMPONENTS_MARKMODIFIED));
    }


    public void setRestrictions(List<AbstractRestriction> value)
    {
        setRestrictions(getSession().getSessionContext(), value);
    }


    public void addToRestrictions(SessionContext ctx, AbstractRestriction value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.RESTRICTIONSFORCOMPONENTS, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORCOMPONENTS_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORCOMPONENTS_MARKMODIFIED));
    }


    public void addToRestrictions(AbstractRestriction value)
    {
        addToRestrictions(getSession().getSessionContext(), value);
    }


    public void removeFromRestrictions(SessionContext ctx, AbstractRestriction value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.RESTRICTIONSFORCOMPONENTS, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORCOMPONENTS_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORCOMPONENTS_MARKMODIFIED));
    }


    public void removeFromRestrictions(AbstractRestriction value)
    {
        removeFromRestrictions(getSession().getSessionContext(), value);
    }


    public Collection<ContentSlot> getSlots(SessionContext ctx)
    {
        List<ContentSlot> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.ELEMENTSFORSLOT, "ContentSlot", null,
                        Utilities.getRelationOrderingOverride(ELEMENTSFORSLOT_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<ContentSlot> getSlots()
    {
        return getSlots(getSession().getSessionContext());
    }


    public long getSlotsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.ELEMENTSFORSLOT, "ContentSlot", null);
    }


    public long getSlotsCount()
    {
        return getSlotsCount(getSession().getSessionContext());
    }


    public void setSlots(SessionContext ctx, Collection<ContentSlot> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.ELEMENTSFORSLOT, null, value,
                        Utilities.getRelationOrderingOverride(ELEMENTSFORSLOT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORSLOT_MARKMODIFIED));
    }


    public void setSlots(Collection<ContentSlot> value)
    {
        setSlots(getSession().getSessionContext(), value);
    }


    public void addToSlots(SessionContext ctx, ContentSlot value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.ELEMENTSFORSLOT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ELEMENTSFORSLOT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORSLOT_MARKMODIFIED));
    }


    public void addToSlots(ContentSlot value)
    {
        addToSlots(getSession().getSessionContext(), value);
    }


    public void removeFromSlots(SessionContext ctx, ContentSlot value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.ELEMENTSFORSLOT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ELEMENTSFORSLOT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORSLOT_MARKMODIFIED));
    }


    public void removeFromSlots(ContentSlot value)
    {
        removeFromSlots(getSession().getSessionContext(), value);
    }


    public String getStyleClasses(SessionContext ctx)
    {
        return (String)getProperty(ctx, "styleClasses");
    }


    public String getStyleClasses()
    {
        return getStyleClasses(getSession().getSessionContext());
    }


    public void setStyleClasses(SessionContext ctx, String value)
    {
        setProperty(ctx, "styleClasses", value);
    }


    public void setStyleClasses(String value)
    {
        setStyleClasses(getSession().getSessionContext(), value);
    }


    public String getType()
    {
        return getType(getSession().getSessionContext());
    }


    public Map<Language, String> getAllType()
    {
        return getAllType(getSession().getSessionContext());
    }


    public String getTypeCode()
    {
        return getTypeCode(getSession().getSessionContext());
    }


    public Boolean isVisible(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "visible");
    }


    public Boolean isVisible()
    {
        return isVisible(getSession().getSessionContext());
    }


    public boolean isVisibleAsPrimitive(SessionContext ctx)
    {
        Boolean value = isVisible(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isVisibleAsPrimitive()
    {
        return isVisibleAsPrimitive(getSession().getSessionContext());
    }


    public void setVisible(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "visible", value);
    }


    public void setVisible(Boolean value)
    {
        setVisible(getSession().getSessionContext(), value);
    }


    public void setVisible(SessionContext ctx, boolean value)
    {
        setVisible(ctx, Boolean.valueOf(value));
    }


    public void setVisible(boolean value)
    {
        setVisible(getSession().getSessionContext(), value);
    }


    public abstract Boolean isContainer(SessionContext paramSessionContext);


    public abstract Boolean isRestricted(SessionContext paramSessionContext);


    public abstract String getType(SessionContext paramSessionContext);


    public abstract Map<Language, String> getAllType(SessionContext paramSessionContext);


    public abstract String getTypeCode(SessionContext paramSessionContext);
}
