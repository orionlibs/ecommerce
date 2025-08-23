package de.hybris.platform.jalo.security;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedPrincipalGroup extends Principal
{
    public static final String LOCNAME = "locName";
    public static final String MAXBRUTEFORCELOGINATTEMPTS = "maxBruteForceLoginAttempts";
    public static final String MEMBERS = "members";
    protected static String PRINCIPALGROUPRELATION_SRC_ORDERED = "relation.PrincipalGroupRelation.source.ordered";
    protected static String PRINCIPALGROUPRELATION_TGT_ORDERED = "relation.PrincipalGroupRelation.target.ordered";
    protected static String PRINCIPALGROUPRELATION_MARKMODIFIED = "relation.PrincipalGroupRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Principal.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("locName", Item.AttributeMode.INITIAL);
        tmp.put("maxBruteForceLoginAttempts", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRINCIPALGROUPRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getLocName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedPrincipalGroup.getLocName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "locName");
    }


    public String getLocName()
    {
        return getLocName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllLocName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "locName", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllLocName()
    {
        return getAllLocName(getSession().getSessionContext());
    }


    public void setLocName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedPrincipalGroup.setLocName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "locName", value);
    }


    public void setLocName(String value)
    {
        setLocName(getSession().getSessionContext(), value);
    }


    public void setAllLocName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "locName", value);
    }


    public void setAllLocName(Map<Language, String> value)
    {
        setAllLocName(getSession().getSessionContext(), value);
    }


    public Integer getMaxBruteForceLoginAttempts(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxBruteForceLoginAttempts");
    }


    public Integer getMaxBruteForceLoginAttempts()
    {
        return getMaxBruteForceLoginAttempts(getSession().getSessionContext());
    }


    public int getMaxBruteForceLoginAttemptsAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxBruteForceLoginAttempts(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxBruteForceLoginAttemptsAsPrimitive()
    {
        return getMaxBruteForceLoginAttemptsAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxBruteForceLoginAttempts(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxBruteForceLoginAttempts", value);
    }


    public void setMaxBruteForceLoginAttempts(Integer value)
    {
        setMaxBruteForceLoginAttempts(getSession().getSessionContext(), value);
    }


    public void setMaxBruteForceLoginAttempts(SessionContext ctx, int value)
    {
        setMaxBruteForceLoginAttempts(ctx, Integer.valueOf(value));
    }


    public void setMaxBruteForceLoginAttempts(int value)
    {
        setMaxBruteForceLoginAttempts(getSession().getSessionContext(), value);
    }


    public Set<Principal> getMembers(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, false, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, "Principal", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<Principal> getMembers()
    {
        return getMembers(getSession().getSessionContext());
    }


    public long getMembersCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, "Principal", null);
    }


    public long getMembersCount()
    {
        return getMembersCount(getSession().getSessionContext());
    }


    public void setMembers(SessionContext ctx, Set<Principal> value)
    {
        setLinkedItems(ctx, false, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPALGROUPRELATION_MARKMODIFIED));
    }


    public void setMembers(Set<Principal> value)
    {
        setMembers(getSession().getSessionContext(), value);
    }


    public void addToMembers(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, false, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPALGROUPRELATION_MARKMODIFIED));
    }


    public void addToMembers(Principal value)
    {
        addToMembers(getSession().getSessionContext(), value);
    }


    public void removeFromMembers(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, false, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPALGROUPRELATION_MARKMODIFIED));
    }


    public void removeFromMembers(Principal value)
    {
        removeFromMembers(getSession().getSessionContext(), value);
    }
}
