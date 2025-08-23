package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCockpitUIComponentAccessRight extends GenericItem
{
    public static final String CODE = "code";
    public static final String READPRINCIPALS = "readPrincipals";
    protected static String PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_SRC_ORDERED = "relation.Principal2CockpitUIComponentReadAccessRelation.source.ordered";
    protected static String PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_TGT_ORDERED = "relation.Principal2CockpitUIComponentReadAccessRelation.target.ordered";
    protected static String PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_MARKMODIFIED = "relation.Principal2CockpitUIComponentReadAccessRelation.markmodified";
    public static final String WRITEPRINCIPALS = "writePrincipals";
    protected static String PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_SRC_ORDERED = "relation.Principal2CockpitUIComponentWriteAccessRelation.source.ordered";
    protected static String PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_TGT_ORDERED = "relation.Principal2CockpitUIComponentWriteAccessRelation.target.ordered";
    protected static String PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_MARKMODIFIED = "relation.Principal2CockpitUIComponentWriteAccessRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Collection<Principal> getReadPrincipals(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION, "Principal", null, false, false);
        return items;
    }


    public Collection<Principal> getReadPrincipals()
    {
        return getReadPrincipals(getSession().getSessionContext());
    }


    public long getReadPrincipalsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION, "Principal", null);
    }


    public long getReadPrincipalsCount()
    {
        return getReadPrincipalsCount(getSession().getSessionContext());
    }


    public void setReadPrincipals(SessionContext ctx, Collection<Principal> value)
    {
        setLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_MARKMODIFIED));
    }


    public void setReadPrincipals(Collection<Principal> value)
    {
        setReadPrincipals(getSession().getSessionContext(), value);
    }


    public void addToReadPrincipals(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_MARKMODIFIED));
    }


    public void addToReadPrincipals(Principal value)
    {
        addToReadPrincipals(getSession().getSessionContext(), value);
    }


    public void removeFromReadPrincipals(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_MARKMODIFIED));
    }


    public void removeFromReadPrincipals(Principal value)
    {
        removeFromReadPrincipals(getSession().getSessionContext(), value);
    }


    public Collection<Principal> getWritePrincipals(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION, "Principal", null, false, false);
        return items;
    }


    public Collection<Principal> getWritePrincipals()
    {
        return getWritePrincipals(getSession().getSessionContext());
    }


    public long getWritePrincipalsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION, "Principal", null);
    }


    public long getWritePrincipalsCount()
    {
        return getWritePrincipalsCount(getSession().getSessionContext());
    }


    public void setWritePrincipals(SessionContext ctx, Collection<Principal> value)
    {
        setLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_MARKMODIFIED));
    }


    public void setWritePrincipals(Collection<Principal> value)
    {
        setWritePrincipals(getSession().getSessionContext(), value);
    }


    public void addToWritePrincipals(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_MARKMODIFIED));
    }


    public void addToWritePrincipals(Principal value)
    {
        addToWritePrincipals(getSession().getSessionContext(), value);
    }


    public void removeFromWritePrincipals(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_MARKMODIFIED));
    }


    public void removeFromWritePrincipals(Principal value)
    {
        removeFromWritePrincipals(getSession().getSessionContext(), value);
    }
}
