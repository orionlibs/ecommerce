package de.hybris.platform.cms2.jalo;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.pages.PageTemplate;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class GeneratedCMSPageType extends ComposedType
{
    public static final String PREVIEWDISABLED = "previewDisabled";
    public static final String RESTRICTIONTYPES = "restrictionTypes";
    protected static String APPLICABLERESTRICTIONTYPEFORPAGETYPES_SRC_ORDERED = "relation.ApplicableRestrictionTypeForPageTypes.source.ordered";
    protected static String APPLICABLERESTRICTIONTYPEFORPAGETYPES_TGT_ORDERED = "relation.ApplicableRestrictionTypeForPageTypes.target.ordered";
    protected static String APPLICABLERESTRICTIONTYPEFORPAGETYPES_MARKMODIFIED = "relation.ApplicableRestrictionTypeForPageTypes.markmodified";
    public static final String TEMPLATES = "templates";
    protected static String VALIDPAGETYPESFORTEMPLATES_SRC_ORDERED = "relation.ValidPageTypesForTemplates.source.ordered";
    protected static String VALIDPAGETYPESFORTEMPLATES_TGT_ORDERED = "relation.ValidPageTypesForTemplates.target.ordered";
    protected static String VALIDPAGETYPESFORTEMPLATES_MARKMODIFIED = "relation.ValidPageTypesForTemplates.markmodified";


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("RestrictionType");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(APPLICABLERESTRICTIONTYPEFORPAGETYPES_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("PageTemplate");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(VALIDPAGETYPESFORTEMPLATES_MARKMODIFIED);
        }
        return true;
    }


    public Boolean isPreviewDisabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "previewDisabled");
    }


    public Boolean isPreviewDisabled()
    {
        return isPreviewDisabled(getSession().getSessionContext());
    }


    public boolean isPreviewDisabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPreviewDisabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPreviewDisabledAsPrimitive()
    {
        return isPreviewDisabledAsPrimitive(getSession().getSessionContext());
    }


    public void setPreviewDisabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "previewDisabled", value);
    }


    public void setPreviewDisabled(Boolean value)
    {
        setPreviewDisabled(getSession().getSessionContext(), value);
    }


    public void setPreviewDisabled(SessionContext ctx, boolean value)
    {
        setPreviewDisabled(ctx, Boolean.valueOf(value));
    }


    public void setPreviewDisabled(boolean value)
    {
        setPreviewDisabled(getSession().getSessionContext(), value);
    }


    public Collection<RestrictionType> getRestrictionTypes(SessionContext ctx)
    {
        List<RestrictionType> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.APPLICABLERESTRICTIONTYPEFORPAGETYPES, "RestrictionType", null, false, false);
        return items;
    }


    public Collection<RestrictionType> getRestrictionTypes()
    {
        return getRestrictionTypes(getSession().getSessionContext());
    }


    public long getRestrictionTypesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.APPLICABLERESTRICTIONTYPEFORPAGETYPES, "RestrictionType", null);
    }


    public long getRestrictionTypesCount()
    {
        return getRestrictionTypesCount(getSession().getSessionContext());
    }


    public void setRestrictionTypes(SessionContext ctx, Collection<RestrictionType> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.APPLICABLERESTRICTIONTYPEFORPAGETYPES, null, value, false, false,
                        Utilities.getMarkModifiedOverride(APPLICABLERESTRICTIONTYPEFORPAGETYPES_MARKMODIFIED));
    }


    public void setRestrictionTypes(Collection<RestrictionType> value)
    {
        setRestrictionTypes(getSession().getSessionContext(), value);
    }


    public void addToRestrictionTypes(SessionContext ctx, RestrictionType value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.APPLICABLERESTRICTIONTYPEFORPAGETYPES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(APPLICABLERESTRICTIONTYPEFORPAGETYPES_MARKMODIFIED));
    }


    public void addToRestrictionTypes(RestrictionType value)
    {
        addToRestrictionTypes(getSession().getSessionContext(), value);
    }


    public void removeFromRestrictionTypes(SessionContext ctx, RestrictionType value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.APPLICABLERESTRICTIONTYPEFORPAGETYPES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(APPLICABLERESTRICTIONTYPEFORPAGETYPES_MARKMODIFIED));
    }


    public void removeFromRestrictionTypes(RestrictionType value)
    {
        removeFromRestrictionTypes(getSession().getSessionContext(), value);
    }


    public Set<PageTemplate> getTemplates(SessionContext ctx)
    {
        List<PageTemplate> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES, "PageTemplate", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<PageTemplate> getTemplates()
    {
        return getTemplates(getSession().getSessionContext());
    }


    public long getTemplatesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES, "PageTemplate", null);
    }


    public long getTemplatesCount()
    {
        return getTemplatesCount(getSession().getSessionContext());
    }


    public void setTemplates(SessionContext ctx, Set<PageTemplate> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES, null, value, false, false,
                        Utilities.getMarkModifiedOverride(VALIDPAGETYPESFORTEMPLATES_MARKMODIFIED));
    }


    public void setTemplates(Set<PageTemplate> value)
    {
        setTemplates(getSession().getSessionContext(), value);
    }


    public void addToTemplates(SessionContext ctx, PageTemplate value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDPAGETYPESFORTEMPLATES_MARKMODIFIED));
    }


    public void addToTemplates(PageTemplate value)
    {
        addToTemplates(getSession().getSessionContext(), value);
    }


    public void removeFromTemplates(SessionContext ctx, PageTemplate value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDPAGETYPESFORTEMPLATES_MARKMODIFIED));
    }


    public void removeFromTemplates(PageTemplate value)
    {
        removeFromTemplates(getSession().getSessionContext(), value);
    }
}
