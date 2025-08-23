package de.hybris.platform.cms2.jalo;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class GeneratedRestrictionType extends ComposedType
{
    public static final String PAGETYPES = "pageTypes";
    protected static String APPLICABLERESTRICTIONTYPEFORPAGETYPES_SRC_ORDERED = "relation.ApplicableRestrictionTypeForPageTypes.source.ordered";
    protected static String APPLICABLERESTRICTIONTYPEFORPAGETYPES_TGT_ORDERED = "relation.ApplicableRestrictionTypeForPageTypes.target.ordered";
    protected static String APPLICABLERESTRICTIONTYPEFORPAGETYPES_MARKMODIFIED = "relation.ApplicableRestrictionTypeForPageTypes.markmodified";


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CmsPageType");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(APPLICABLERESTRICTIONTYPEFORPAGETYPES_MARKMODIFIED);
        }
        return true;
    }


    public Collection<CMSPageType> getPageTypes(SessionContext ctx)
    {
        List<CMSPageType> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.APPLICABLERESTRICTIONTYPEFORPAGETYPES, "CMSPageType", null, false, false);
        return items;
    }


    public Collection<CMSPageType> getPageTypes()
    {
        return getPageTypes(getSession().getSessionContext());
    }


    public long getPageTypesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.APPLICABLERESTRICTIONTYPEFORPAGETYPES, "CMSPageType", null);
    }


    public long getPageTypesCount()
    {
        return getPageTypesCount(getSession().getSessionContext());
    }


    public void setPageTypes(SessionContext ctx, Collection<CMSPageType> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.APPLICABLERESTRICTIONTYPEFORPAGETYPES, null, value, false, false,
                        Utilities.getMarkModifiedOverride(APPLICABLERESTRICTIONTYPEFORPAGETYPES_MARKMODIFIED));
    }


    public void setPageTypes(Collection<CMSPageType> value)
    {
        setPageTypes(getSession().getSessionContext(), value);
    }


    public void addToPageTypes(SessionContext ctx, CMSPageType value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.APPLICABLERESTRICTIONTYPEFORPAGETYPES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(APPLICABLERESTRICTIONTYPEFORPAGETYPES_MARKMODIFIED));
    }


    public void addToPageTypes(CMSPageType value)
    {
        addToPageTypes(getSession().getSessionContext(), value);
    }


    public void removeFromPageTypes(SessionContext ctx, CMSPageType value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.APPLICABLERESTRICTIONTYPEFORPAGETYPES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(APPLICABLERESTRICTIONTYPEFORPAGETYPES_MARKMODIFIED));
    }


    public void removeFromPageTypes(CMSPageType value)
    {
        removeFromPageTypes(getSession().getSessionContext(), value);
    }
}
