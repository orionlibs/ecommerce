package de.hybris.platform.cms2.jalo;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.ContentSlotName;
import de.hybris.platform.cms2.jalo.site.CMSSite;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class GeneratedCMSComponentType extends ComposedType
{
    public static final String CMSSITES = "cmsSites";
    protected static String VALIDCOMPONENTTYPESFORSITE_SRC_ORDERED = "relation.ValidComponentTypesForSite.source.ordered";
    protected static String VALIDCOMPONENTTYPESFORSITE_TGT_ORDERED = "relation.ValidComponentTypesForSite.target.ordered";
    protected static String VALIDCOMPONENTTYPESFORSITE_MARKMODIFIED = "relation.ValidComponentTypesForSite.markmodified";
    public static final String CONTENTSLOTNAMES = "contentSlotNames";
    protected static String VALIDCOMPONENTTYPESFORCONTENTSLOTS_SRC_ORDERED = "relation.ValidComponentTypesForContentSlots.source.ordered";
    protected static String VALIDCOMPONENTTYPESFORCONTENTSLOTS_TGT_ORDERED = "relation.ValidComponentTypesForContentSlots.target.ordered";
    protected static String VALIDCOMPONENTTYPESFORCONTENTSLOTS_MARKMODIFIED = "relation.ValidComponentTypesForContentSlots.markmodified";
    public static final String COMPONENTTYPEGROUPS = "componentTypeGroups";
    protected static String COMPONENTTYPEGROUPS2COMPONENTTYPE_SRC_ORDERED = "relation.ComponentTypeGroups2ComponentType.source.ordered";
    protected static String COMPONENTTYPEGROUPS2COMPONENTTYPE_TGT_ORDERED = "relation.ComponentTypeGroups2ComponentType.target.ordered";
    protected static String COMPONENTTYPEGROUPS2COMPONENTTYPE_MARKMODIFIED = "relation.ComponentTypeGroups2ComponentType.markmodified";


    public Set<CMSSite> getCmsSites(SessionContext ctx)
    {
        List<CMSSite> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORSITE, "CMSSite", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<CMSSite> getCmsSites()
    {
        return getCmsSites(getSession().getSessionContext());
    }


    public long getCmsSitesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORSITE, "CMSSite", null);
    }


    public long getCmsSitesCount()
    {
        return getCmsSitesCount(getSession().getSessionContext());
    }


    public void setCmsSites(SessionContext ctx, Set<CMSSite> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORSITE, null, value, false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORSITE_MARKMODIFIED));
    }


    public void setCmsSites(Set<CMSSite> value)
    {
        setCmsSites(getSession().getSessionContext(), value);
    }


    public void addToCmsSites(SessionContext ctx, CMSSite value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORSITE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORSITE_MARKMODIFIED));
    }


    public void addToCmsSites(CMSSite value)
    {
        addToCmsSites(getSession().getSessionContext(), value);
    }


    public void removeFromCmsSites(SessionContext ctx, CMSSite value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORSITE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORSITE_MARKMODIFIED));
    }


    public void removeFromCmsSites(CMSSite value)
    {
        removeFromCmsSites(getSession().getSessionContext(), value);
    }


    public Set<ComponentTypeGroup> getComponentTypeGroups(SessionContext ctx)
    {
        List<ComponentTypeGroup> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.COMPONENTTYPEGROUPS2COMPONENTTYPE, "ComponentTypeGroup", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<ComponentTypeGroup> getComponentTypeGroups()
    {
        return getComponentTypeGroups(getSession().getSessionContext());
    }


    public long getComponentTypeGroupsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.COMPONENTTYPEGROUPS2COMPONENTTYPE, "ComponentTypeGroup", null);
    }


    public long getComponentTypeGroupsCount()
    {
        return getComponentTypeGroupsCount(getSession().getSessionContext());
    }


    public void setComponentTypeGroups(SessionContext ctx, Set<ComponentTypeGroup> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.COMPONENTTYPEGROUPS2COMPONENTTYPE, null, value, false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTTYPEGROUPS2COMPONENTTYPE_MARKMODIFIED));
    }


    public void setComponentTypeGroups(Set<ComponentTypeGroup> value)
    {
        setComponentTypeGroups(getSession().getSessionContext(), value);
    }


    public void addToComponentTypeGroups(SessionContext ctx, ComponentTypeGroup value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.COMPONENTTYPEGROUPS2COMPONENTTYPE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTTYPEGROUPS2COMPONENTTYPE_MARKMODIFIED));
    }


    public void addToComponentTypeGroups(ComponentTypeGroup value)
    {
        addToComponentTypeGroups(getSession().getSessionContext(), value);
    }


    public void removeFromComponentTypeGroups(SessionContext ctx, ComponentTypeGroup value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.COMPONENTTYPEGROUPS2COMPONENTTYPE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTTYPEGROUPS2COMPONENTTYPE_MARKMODIFIED));
    }


    public void removeFromComponentTypeGroups(ComponentTypeGroup value)
    {
        removeFromComponentTypeGroups(getSession().getSessionContext(), value);
    }


    public Set<ContentSlotName> getContentSlotNames(SessionContext ctx)
    {
        List<ContentSlotName> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORCONTENTSLOTS, "ContentSlotName", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<ContentSlotName> getContentSlotNames()
    {
        return getContentSlotNames(getSession().getSessionContext());
    }


    public long getContentSlotNamesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORCONTENTSLOTS, "ContentSlotName", null);
    }


    public long getContentSlotNamesCount()
    {
        return getContentSlotNamesCount(getSession().getSessionContext());
    }


    public void setContentSlotNames(SessionContext ctx, Set<ContentSlotName> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORCONTENTSLOTS, null, value, false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORCONTENTSLOTS_MARKMODIFIED));
    }


    public void setContentSlotNames(Set<ContentSlotName> value)
    {
        setContentSlotNames(getSession().getSessionContext(), value);
    }


    public void addToContentSlotNames(SessionContext ctx, ContentSlotName value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORCONTENTSLOTS, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORCONTENTSLOTS_MARKMODIFIED));
    }


    public void addToContentSlotNames(ContentSlotName value)
    {
        addToContentSlotNames(getSession().getSessionContext(), value);
    }


    public void removeFromContentSlotNames(SessionContext ctx, ContentSlotName value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORCONTENTSLOTS, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORCONTENTSLOTS_MARKMODIFIED));
    }


    public void removeFromContentSlotNames(ContentSlotName value)
    {
        removeFromContentSlotNames(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CMSSite");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORSITE_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("ContentSlotName");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORCONTENTSLOTS_MARKMODIFIED);
        }
        ComposedType relationSecondEnd2 = TypeManager.getInstance().getComposedType("ComponentTypeGroup");
        if(relationSecondEnd2.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(COMPONENTTYPEGROUPS2COMPONENTTYPE_MARKMODIFIED);
        }
        return true;
    }
}
