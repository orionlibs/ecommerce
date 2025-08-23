package de.hybris.platform.jalo.security;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.SearchRestriction;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedPrincipal extends GenericItem
{
    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
    public static final String UID = "uid";
    public static final String GROUPS = "groups";
    protected static String PRINCIPALGROUPRELATION_SRC_ORDERED = "relation.PrincipalGroupRelation.source.ordered";
    protected static String PRINCIPALGROUPRELATION_TGT_ORDERED = "relation.PrincipalGroupRelation.target.ordered";
    protected static String PRINCIPALGROUPRELATION_MARKMODIFIED = "relation.PrincipalGroupRelation.markmodified";
    public static final String SEARCHRESTRICTIONS = "searchRestrictions";
    protected static final OneToManyHandler<SearchRestriction> SEARCHRESTRICTIONSHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.SEARCHRESTRICTION, false, "principal", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("uid", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public Set<PrincipalGroup> getGroups(SessionContext ctx)
    {
        List<PrincipalGroup> items = getLinkedItems(ctx, true, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, "PrincipalGroup", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<PrincipalGroup> getGroups()
    {
        return getGroups(getSession().getSessionContext());
    }


    public long getGroupsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, "PrincipalGroup", null);
    }


    public long getGroupsCount()
    {
        return getGroupsCount(getSession().getSessionContext());
    }


    public void setGroups(SessionContext ctx, Set<PrincipalGroup> value)
    {
        setLinkedItems(ctx, true, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPALGROUPRELATION_MARKMODIFIED));
    }


    public void setGroups(Set<PrincipalGroup> value)
    {
        setGroups(getSession().getSessionContext(), value);
    }


    public void addToGroups(SessionContext ctx, PrincipalGroup value)
    {
        addLinkedItems(ctx, true, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPALGROUPRELATION_MARKMODIFIED));
    }


    public void addToGroups(PrincipalGroup value)
    {
        addToGroups(getSession().getSessionContext(), value);
    }


    public void removeFromGroups(SessionContext ctx, PrincipalGroup value)
    {
        removeLinkedItems(ctx, true, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPALGROUPRELATION_MARKMODIFIED));
    }


    public void removeFromGroups(PrincipalGroup value)
    {
        removeFromGroups(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("PrincipalGroup");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRINCIPALGROUPRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public Collection<SearchRestriction> getSearchRestrictions(SessionContext ctx)
    {
        return SEARCHRESTRICTIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<SearchRestriction> getSearchRestrictions()
    {
        return getSearchRestrictions(getSession().getSessionContext());
    }


    public String getUid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uid");
    }


    public String getUid()
    {
        return getUid(getSession().getSessionContext());
    }


    public void setUid(SessionContext ctx, String value) throws ConsistencyCheckException
    {
        setProperty(ctx, "uid", value);
    }


    public void setUid(String value) throws ConsistencyCheckException
    {
        setUid(getSession().getSessionContext(), value);
    }
}
