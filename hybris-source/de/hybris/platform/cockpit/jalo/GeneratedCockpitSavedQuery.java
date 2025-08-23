package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCockpitSavedQuery extends GenericItem
{
    public static final String LABEL = "label";
    public static final String DESCRIPTION = "description";
    public static final String SIMPLETEXT = "simpleText";
    public static final String SELECTEDTYPECODE = "selectedTypeCode";
    public static final String SELECTEDTEMPLATECODE = "selectedTemplateCode";
    public static final String CODE = "code";
    public static final String DEFAULTVIEWMODE = "defaultViewMode";
    public static final String USER = "user";
    public static final String COCKPITSAVEDFACETVALUES = "cockpitSavedFacetValues";
    public static final String COCKPITSAVEDSORTCRITERIA = "cockpitSavedSortCriteria";
    public static final String COCKPITSAVEDPARAMETERVALUES = "cockpitSavedParameterValues";
    public static final String READSAVEDQUERYPRINCIPALS = "readSavedQueryPrincipals";
    protected static String READPRINCIPAL2COCKPITSAVEDQUERYRELATION_SRC_ORDERED = "relation.ReadPrincipal2CockpitSavedQueryRelation.source.ordered";
    protected static String READPRINCIPAL2COCKPITSAVEDQUERYRELATION_TGT_ORDERED = "relation.ReadPrincipal2CockpitSavedQueryRelation.target.ordered";
    protected static String READPRINCIPAL2COCKPITSAVEDQUERYRELATION_MARKMODIFIED = "relation.ReadPrincipal2CockpitSavedQueryRelation.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedCockpitSavedQuery> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.COCKPITSAVEDQUERY, false, "user", null, false, true, 0);
    protected static final OneToManyHandler<CockpitSavedFacetValue> COCKPITSAVEDFACETVALUESHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.COCKPITSAVEDFACETVALUE, true, "cockpitSavedQuery", null, false, true, 0);
    protected static final OneToManyHandler<CockpitSavedSortCriterion> COCKPITSAVEDSORTCRITERIAHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.COCKPITSAVEDSORTCRITERION, true, "cockpitSavedQuery", null, false, true, 0);
    protected static final OneToManyHandler<CockpitSavedParameterValue> COCKPITSAVEDPARAMETERVALUESHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.COCKPITSAVEDPARAMETERVALUE, true, "cockpitSavedQuery", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("label", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("simpleText", Item.AttributeMode.INITIAL);
        tmp.put("selectedTypeCode", Item.AttributeMode.INITIAL);
        tmp.put("selectedTemplateCode", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("defaultViewMode", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<CockpitSavedFacetValue> getCockpitSavedFacetValues(SessionContext ctx)
    {
        return COCKPITSAVEDFACETVALUESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<CockpitSavedFacetValue> getCockpitSavedFacetValues()
    {
        return getCockpitSavedFacetValues(getSession().getSessionContext());
    }


    public void setCockpitSavedFacetValues(SessionContext ctx, Collection<CockpitSavedFacetValue> value)
    {
        COCKPITSAVEDFACETVALUESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setCockpitSavedFacetValues(Collection<CockpitSavedFacetValue> value)
    {
        setCockpitSavedFacetValues(getSession().getSessionContext(), value);
    }


    public void addToCockpitSavedFacetValues(SessionContext ctx, CockpitSavedFacetValue value)
    {
        COCKPITSAVEDFACETVALUESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCockpitSavedFacetValues(CockpitSavedFacetValue value)
    {
        addToCockpitSavedFacetValues(getSession().getSessionContext(), value);
    }


    public void removeFromCockpitSavedFacetValues(SessionContext ctx, CockpitSavedFacetValue value)
    {
        COCKPITSAVEDFACETVALUESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCockpitSavedFacetValues(CockpitSavedFacetValue value)
    {
        removeFromCockpitSavedFacetValues(getSession().getSessionContext(), value);
    }


    public Collection<CockpitSavedParameterValue> getCockpitSavedParameterValues(SessionContext ctx)
    {
        return COCKPITSAVEDPARAMETERVALUESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<CockpitSavedParameterValue> getCockpitSavedParameterValues()
    {
        return getCockpitSavedParameterValues(getSession().getSessionContext());
    }


    public void setCockpitSavedParameterValues(SessionContext ctx, Collection<CockpitSavedParameterValue> value)
    {
        COCKPITSAVEDPARAMETERVALUESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setCockpitSavedParameterValues(Collection<CockpitSavedParameterValue> value)
    {
        setCockpitSavedParameterValues(getSession().getSessionContext(), value);
    }


    public void addToCockpitSavedParameterValues(SessionContext ctx, CockpitSavedParameterValue value)
    {
        COCKPITSAVEDPARAMETERVALUESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCockpitSavedParameterValues(CockpitSavedParameterValue value)
    {
        addToCockpitSavedParameterValues(getSession().getSessionContext(), value);
    }


    public void removeFromCockpitSavedParameterValues(SessionContext ctx, CockpitSavedParameterValue value)
    {
        COCKPITSAVEDPARAMETERVALUESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCockpitSavedParameterValues(CockpitSavedParameterValue value)
    {
        removeFromCockpitSavedParameterValues(getSession().getSessionContext(), value);
    }


    public Collection<CockpitSavedSortCriterion> getCockpitSavedSortCriteria(SessionContext ctx)
    {
        return COCKPITSAVEDSORTCRITERIAHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<CockpitSavedSortCriterion> getCockpitSavedSortCriteria()
    {
        return getCockpitSavedSortCriteria(getSession().getSessionContext());
    }


    public void setCockpitSavedSortCriteria(SessionContext ctx, Collection<CockpitSavedSortCriterion> value)
    {
        COCKPITSAVEDSORTCRITERIAHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setCockpitSavedSortCriteria(Collection<CockpitSavedSortCriterion> value)
    {
        setCockpitSavedSortCriteria(getSession().getSessionContext(), value);
    }


    public void addToCockpitSavedSortCriteria(SessionContext ctx, CockpitSavedSortCriterion value)
    {
        COCKPITSAVEDSORTCRITERIAHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCockpitSavedSortCriteria(CockpitSavedSortCriterion value)
    {
        addToCockpitSavedSortCriteria(getSession().getSessionContext(), value);
    }


    public void removeFromCockpitSavedSortCriteria(SessionContext ctx, CockpitSavedSortCriterion value)
    {
        COCKPITSAVEDSORTCRITERIAHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCockpitSavedSortCriteria(CockpitSavedSortCriterion value)
    {
        removeFromCockpitSavedSortCriteria(getSession().getSessionContext(), value);
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


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDefaultViewMode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "defaultViewMode");
    }


    public String getDefaultViewMode()
    {
        return getDefaultViewMode(getSession().getSessionContext());
    }


    public void setDefaultViewMode(SessionContext ctx, String value)
    {
        setProperty(ctx, "defaultViewMode", value);
    }


    public void setDefaultViewMode(String value)
    {
        setDefaultViewMode(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCockpitSavedQuery.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCockpitSavedQuery.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITSAVEDQUERYRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getLabel(SessionContext ctx)
    {
        return (String)getProperty(ctx, "label");
    }


    public String getLabel()
    {
        return getLabel(getSession().getSessionContext());
    }


    public void setLabel(SessionContext ctx, String value)
    {
        setProperty(ctx, "label", value);
    }


    public void setLabel(String value)
    {
        setLabel(getSession().getSessionContext(), value);
    }


    public Collection<Principal> getReadSavedQueryPrincipals(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION, "Principal", null, false, false);
        return items;
    }


    public Collection<Principal> getReadSavedQueryPrincipals()
    {
        return getReadSavedQueryPrincipals(getSession().getSessionContext());
    }


    public long getReadSavedQueryPrincipalsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION, "Principal", null);
    }


    public long getReadSavedQueryPrincipalsCount()
    {
        return getReadSavedQueryPrincipalsCount(getSession().getSessionContext());
    }


    public void setReadSavedQueryPrincipals(SessionContext ctx, Collection<Principal> value)
    {
        setLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITSAVEDQUERYRELATION_MARKMODIFIED));
    }


    public void setReadSavedQueryPrincipals(Collection<Principal> value)
    {
        setReadSavedQueryPrincipals(getSession().getSessionContext(), value);
    }


    public void addToReadSavedQueryPrincipals(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITSAVEDQUERYRELATION_MARKMODIFIED));
    }


    public void addToReadSavedQueryPrincipals(Principal value)
    {
        addToReadSavedQueryPrincipals(getSession().getSessionContext(), value);
    }


    public void removeFromReadSavedQueryPrincipals(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITSAVEDQUERYRELATION_MARKMODIFIED));
    }


    public void removeFromReadSavedQueryPrincipals(Principal value)
    {
        removeFromReadSavedQueryPrincipals(getSession().getSessionContext(), value);
    }


    public String getSelectedTemplateCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "selectedTemplateCode");
    }


    public String getSelectedTemplateCode()
    {
        return getSelectedTemplateCode(getSession().getSessionContext());
    }


    public void setSelectedTemplateCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "selectedTemplateCode", value);
    }


    public void setSelectedTemplateCode(String value)
    {
        setSelectedTemplateCode(getSession().getSessionContext(), value);
    }


    public String getSelectedTypeCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "selectedTypeCode");
    }


    public String getSelectedTypeCode()
    {
        return getSelectedTypeCode(getSession().getSessionContext());
    }


    public void setSelectedTypeCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "selectedTypeCode", value);
    }


    public void setSelectedTypeCode(String value)
    {
        setSelectedTypeCode(getSession().getSessionContext(), value);
    }


    public String getSimpleText(SessionContext ctx)
    {
        return (String)getProperty(ctx, "simpleText");
    }


    public String getSimpleText()
    {
        return getSimpleText(getSession().getSessionContext());
    }


    public void setSimpleText(SessionContext ctx, String value)
    {
        setProperty(ctx, "simpleText", value);
    }


    public void setSimpleText(String value)
    {
        setSimpleText(getSession().getSessionContext(), value);
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
