package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAbstractAsConfigurableSearchConfiguration extends AbstractAsSearchConfiguration
{
    public static final String FACETSMERGEMODE = "facetsMergeMode";
    public static final String BOOSTITEMSMERGEMODE = "boostItemsMergeMode";
    public static final String BOOSTRULESMERGEMODE = "boostRulesMergeMode";
    public static final String SORTSMERGEMODE = "sortsMergeMode";
    public static final String GROUPMERGEMODE = "groupMergeMode";
    public static final String GROUPEXPRESSION = "groupExpression";
    public static final String GROUPLIMIT = "groupLimit";
    public static final String PROMOTEDFACETS = "promotedFacets";
    public static final String FACETS = "facets";
    public static final String EXCLUDEDFACETS = "excludedFacets";
    public static final String PROMOTEDITEMS = "promotedItems";
    public static final String EXCLUDEDITEMS = "excludedItems";
    public static final String BOOSTRULES = "boostRules";
    public static final String SORTS = "sorts";
    public static final String PROMOTEDSORTS = "promotedSorts";
    public static final String EXCLUDEDSORTS = "excludedSorts";
    protected static final OneToManyHandler<AsPromotedFacet> PROMOTEDFACETSHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASPROMOTEDFACET, true, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<AsFacet> FACETSHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASFACET, true, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<AsExcludedFacet> EXCLUDEDFACETSHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASEXCLUDEDFACET, true, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<AsPromotedItem> PROMOTEDITEMSHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASPROMOTEDITEM, true, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<AsExcludedItem> EXCLUDEDITEMSHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASEXCLUDEDITEM, true, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<AsBoostRule> BOOSTRULESHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASBOOSTRULE, true, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<AsSort> SORTSHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASSORT, true, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<AsPromotedSort> PROMOTEDSORTSHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASPROMOTEDSORT, true, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<AsExcludedSort> EXCLUDEDSORTSHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASEXCLUDEDSORT, true, "searchConfiguration", "searchConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsSearchConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("facetsMergeMode", Item.AttributeMode.INITIAL);
        tmp.put("boostItemsMergeMode", Item.AttributeMode.INITIAL);
        tmp.put("boostRulesMergeMode", Item.AttributeMode.INITIAL);
        tmp.put("sortsMergeMode", Item.AttributeMode.INITIAL);
        tmp.put("groupMergeMode", Item.AttributeMode.INITIAL);
        tmp.put("groupExpression", Item.AttributeMode.INITIAL);
        tmp.put("groupLimit", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getBoostItemsMergeMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "boostItemsMergeMode");
    }


    public EnumerationValue getBoostItemsMergeMode()
    {
        return getBoostItemsMergeMode(getSession().getSessionContext());
    }


    public void setBoostItemsMergeMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "boostItemsMergeMode", value);
    }


    public void setBoostItemsMergeMode(EnumerationValue value)
    {
        setBoostItemsMergeMode(getSession().getSessionContext(), value);
    }


    public List<AsBoostRule> getBoostRules(SessionContext ctx)
    {
        return (List<AsBoostRule>)BOOSTRULESHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsBoostRule> getBoostRules()
    {
        return getBoostRules(getSession().getSessionContext());
    }


    public void setBoostRules(SessionContext ctx, List<AsBoostRule> value)
    {
        BOOSTRULESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setBoostRules(List<AsBoostRule> value)
    {
        setBoostRules(getSession().getSessionContext(), value);
    }


    public void addToBoostRules(SessionContext ctx, AsBoostRule value)
    {
        BOOSTRULESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToBoostRules(AsBoostRule value)
    {
        addToBoostRules(getSession().getSessionContext(), value);
    }


    public void removeFromBoostRules(SessionContext ctx, AsBoostRule value)
    {
        BOOSTRULESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromBoostRules(AsBoostRule value)
    {
        removeFromBoostRules(getSession().getSessionContext(), value);
    }


    public EnumerationValue getBoostRulesMergeMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "boostRulesMergeMode");
    }


    public EnumerationValue getBoostRulesMergeMode()
    {
        return getBoostRulesMergeMode(getSession().getSessionContext());
    }


    public void setBoostRulesMergeMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "boostRulesMergeMode", value);
    }


    public void setBoostRulesMergeMode(EnumerationValue value)
    {
        setBoostRulesMergeMode(getSession().getSessionContext(), value);
    }


    public List<AsExcludedFacet> getExcludedFacets(SessionContext ctx)
    {
        return (List<AsExcludedFacet>)EXCLUDEDFACETSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsExcludedFacet> getExcludedFacets()
    {
        return getExcludedFacets(getSession().getSessionContext());
    }


    public void setExcludedFacets(SessionContext ctx, List<AsExcludedFacet> value)
    {
        EXCLUDEDFACETSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setExcludedFacets(List<AsExcludedFacet> value)
    {
        setExcludedFacets(getSession().getSessionContext(), value);
    }


    public void addToExcludedFacets(SessionContext ctx, AsExcludedFacet value)
    {
        EXCLUDEDFACETSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToExcludedFacets(AsExcludedFacet value)
    {
        addToExcludedFacets(getSession().getSessionContext(), value);
    }


    public void removeFromExcludedFacets(SessionContext ctx, AsExcludedFacet value)
    {
        EXCLUDEDFACETSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromExcludedFacets(AsExcludedFacet value)
    {
        removeFromExcludedFacets(getSession().getSessionContext(), value);
    }


    public List<AsExcludedItem> getExcludedItems(SessionContext ctx)
    {
        return (List<AsExcludedItem>)EXCLUDEDITEMSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsExcludedItem> getExcludedItems()
    {
        return getExcludedItems(getSession().getSessionContext());
    }


    public void setExcludedItems(SessionContext ctx, List<AsExcludedItem> value)
    {
        EXCLUDEDITEMSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setExcludedItems(List<AsExcludedItem> value)
    {
        setExcludedItems(getSession().getSessionContext(), value);
    }


    public void addToExcludedItems(SessionContext ctx, AsExcludedItem value)
    {
        EXCLUDEDITEMSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToExcludedItems(AsExcludedItem value)
    {
        addToExcludedItems(getSession().getSessionContext(), value);
    }


    public void removeFromExcludedItems(SessionContext ctx, AsExcludedItem value)
    {
        EXCLUDEDITEMSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromExcludedItems(AsExcludedItem value)
    {
        removeFromExcludedItems(getSession().getSessionContext(), value);
    }


    public List<AsExcludedSort> getExcludedSorts(SessionContext ctx)
    {
        return (List<AsExcludedSort>)EXCLUDEDSORTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsExcludedSort> getExcludedSorts()
    {
        return getExcludedSorts(getSession().getSessionContext());
    }


    public void setExcludedSorts(SessionContext ctx, List<AsExcludedSort> value)
    {
        EXCLUDEDSORTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setExcludedSorts(List<AsExcludedSort> value)
    {
        setExcludedSorts(getSession().getSessionContext(), value);
    }


    public void addToExcludedSorts(SessionContext ctx, AsExcludedSort value)
    {
        EXCLUDEDSORTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToExcludedSorts(AsExcludedSort value)
    {
        addToExcludedSorts(getSession().getSessionContext(), value);
    }


    public void removeFromExcludedSorts(SessionContext ctx, AsExcludedSort value)
    {
        EXCLUDEDSORTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromExcludedSorts(AsExcludedSort value)
    {
        removeFromExcludedSorts(getSession().getSessionContext(), value);
    }


    public List<AsFacet> getFacets(SessionContext ctx)
    {
        return (List<AsFacet>)FACETSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsFacet> getFacets()
    {
        return getFacets(getSession().getSessionContext());
    }


    public void setFacets(SessionContext ctx, List<AsFacet> value)
    {
        FACETSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setFacets(List<AsFacet> value)
    {
        setFacets(getSession().getSessionContext(), value);
    }


    public void addToFacets(SessionContext ctx, AsFacet value)
    {
        FACETSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToFacets(AsFacet value)
    {
        addToFacets(getSession().getSessionContext(), value);
    }


    public void removeFromFacets(SessionContext ctx, AsFacet value)
    {
        FACETSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromFacets(AsFacet value)
    {
        removeFromFacets(getSession().getSessionContext(), value);
    }


    public EnumerationValue getFacetsMergeMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "facetsMergeMode");
    }


    public EnumerationValue getFacetsMergeMode()
    {
        return getFacetsMergeMode(getSession().getSessionContext());
    }


    public void setFacetsMergeMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "facetsMergeMode", value);
    }


    public void setFacetsMergeMode(EnumerationValue value)
    {
        setFacetsMergeMode(getSession().getSessionContext(), value);
    }


    public String getGroupExpression(SessionContext ctx)
    {
        return (String)getProperty(ctx, "groupExpression");
    }


    public String getGroupExpression()
    {
        return getGroupExpression(getSession().getSessionContext());
    }


    public void setGroupExpression(SessionContext ctx, String value)
    {
        setProperty(ctx, "groupExpression", value);
    }


    public void setGroupExpression(String value)
    {
        setGroupExpression(getSession().getSessionContext(), value);
    }


    public Integer getGroupLimit(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "groupLimit");
    }


    public Integer getGroupLimit()
    {
        return getGroupLimit(getSession().getSessionContext());
    }


    public int getGroupLimitAsPrimitive(SessionContext ctx)
    {
        Integer value = getGroupLimit(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getGroupLimitAsPrimitive()
    {
        return getGroupLimitAsPrimitive(getSession().getSessionContext());
    }


    public void setGroupLimit(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "groupLimit", value);
    }


    public void setGroupLimit(Integer value)
    {
        setGroupLimit(getSession().getSessionContext(), value);
    }


    public void setGroupLimit(SessionContext ctx, int value)
    {
        setGroupLimit(ctx, Integer.valueOf(value));
    }


    public void setGroupLimit(int value)
    {
        setGroupLimit(getSession().getSessionContext(), value);
    }


    public EnumerationValue getGroupMergeMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "groupMergeMode");
    }


    public EnumerationValue getGroupMergeMode()
    {
        return getGroupMergeMode(getSession().getSessionContext());
    }


    public void setGroupMergeMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "groupMergeMode", value);
    }


    public void setGroupMergeMode(EnumerationValue value)
    {
        setGroupMergeMode(getSession().getSessionContext(), value);
    }


    public List<AsPromotedFacet> getPromotedFacets(SessionContext ctx)
    {
        return (List<AsPromotedFacet>)PROMOTEDFACETSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsPromotedFacet> getPromotedFacets()
    {
        return getPromotedFacets(getSession().getSessionContext());
    }


    public void setPromotedFacets(SessionContext ctx, List<AsPromotedFacet> value)
    {
        PROMOTEDFACETSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setPromotedFacets(List<AsPromotedFacet> value)
    {
        setPromotedFacets(getSession().getSessionContext(), value);
    }


    public void addToPromotedFacets(SessionContext ctx, AsPromotedFacet value)
    {
        PROMOTEDFACETSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToPromotedFacets(AsPromotedFacet value)
    {
        addToPromotedFacets(getSession().getSessionContext(), value);
    }


    public void removeFromPromotedFacets(SessionContext ctx, AsPromotedFacet value)
    {
        PROMOTEDFACETSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromPromotedFacets(AsPromotedFacet value)
    {
        removeFromPromotedFacets(getSession().getSessionContext(), value);
    }


    public List<AsPromotedItem> getPromotedItems(SessionContext ctx)
    {
        return (List<AsPromotedItem>)PROMOTEDITEMSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsPromotedItem> getPromotedItems()
    {
        return getPromotedItems(getSession().getSessionContext());
    }


    public void setPromotedItems(SessionContext ctx, List<AsPromotedItem> value)
    {
        PROMOTEDITEMSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setPromotedItems(List<AsPromotedItem> value)
    {
        setPromotedItems(getSession().getSessionContext(), value);
    }


    public void addToPromotedItems(SessionContext ctx, AsPromotedItem value)
    {
        PROMOTEDITEMSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToPromotedItems(AsPromotedItem value)
    {
        addToPromotedItems(getSession().getSessionContext(), value);
    }


    public void removeFromPromotedItems(SessionContext ctx, AsPromotedItem value)
    {
        PROMOTEDITEMSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromPromotedItems(AsPromotedItem value)
    {
        removeFromPromotedItems(getSession().getSessionContext(), value);
    }


    public List<AsPromotedSort> getPromotedSorts(SessionContext ctx)
    {
        return (List<AsPromotedSort>)PROMOTEDSORTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsPromotedSort> getPromotedSorts()
    {
        return getPromotedSorts(getSession().getSessionContext());
    }


    public void setPromotedSorts(SessionContext ctx, List<AsPromotedSort> value)
    {
        PROMOTEDSORTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setPromotedSorts(List<AsPromotedSort> value)
    {
        setPromotedSorts(getSession().getSessionContext(), value);
    }


    public void addToPromotedSorts(SessionContext ctx, AsPromotedSort value)
    {
        PROMOTEDSORTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToPromotedSorts(AsPromotedSort value)
    {
        addToPromotedSorts(getSession().getSessionContext(), value);
    }


    public void removeFromPromotedSorts(SessionContext ctx, AsPromotedSort value)
    {
        PROMOTEDSORTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromPromotedSorts(AsPromotedSort value)
    {
        removeFromPromotedSorts(getSession().getSessionContext(), value);
    }


    public List<AsSort> getSorts(SessionContext ctx)
    {
        return (List<AsSort>)SORTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsSort> getSorts()
    {
        return getSorts(getSession().getSessionContext());
    }


    public void setSorts(SessionContext ctx, List<AsSort> value)
    {
        SORTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSorts(List<AsSort> value)
    {
        setSorts(getSession().getSessionContext(), value);
    }


    public void addToSorts(SessionContext ctx, AsSort value)
    {
        SORTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSorts(AsSort value)
    {
        addToSorts(getSession().getSessionContext(), value);
    }


    public void removeFromSorts(SessionContext ctx, AsSort value)
    {
        SORTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSorts(AsSort value)
    {
        removeFromSorts(getSession().getSessionContext(), value);
    }


    public EnumerationValue getSortsMergeMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "sortsMergeMode");
    }


    public EnumerationValue getSortsMergeMode()
    {
        return getSortsMergeMode(getSession().getSessionContext());
    }


    public void setSortsMergeMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "sortsMergeMode", value);
    }


    public void setSortsMergeMode(EnumerationValue value)
    {
        setSortsMergeMode(getSession().getSessionContext(), value);
    }
}
