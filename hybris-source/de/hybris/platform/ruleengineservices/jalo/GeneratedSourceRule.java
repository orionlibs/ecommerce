package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.campaigns.jalo.Campaign;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.ruleengineservices.constants.GeneratedRuleEngineServicesConstants;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedSourceRule extends AbstractRule
{
    public static final String CONDITIONS = "conditions";
    public static final String ACTIONS = "actions";
    public static final String CAMPAIGNS = "campaigns";
    protected static String CAMPAIGN2SOURCERULERELATION_SRC_ORDERED = "relation.Campaign2SourceRuleRelation.source.ordered";
    protected static String CAMPAIGN2SOURCERULERELATION_TGT_ORDERED = "relation.Campaign2SourceRuleRelation.target.ordered";
    protected static String CAMPAIGN2SOURCERULERELATION_MARKMODIFIED = "relation.Campaign2SourceRuleRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRule.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("conditions", Item.AttributeMode.INITIAL);
        tmp.put("actions", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getActions(SessionContext ctx)
    {
        return (String)getProperty(ctx, "actions");
    }


    public String getActions()
    {
        return getActions(getSession().getSessionContext());
    }


    public void setActions(SessionContext ctx, String value)
    {
        setProperty(ctx, "actions", value);
    }


    public void setActions(String value)
    {
        setActions(getSession().getSessionContext(), value);
    }


    public Set<Campaign> getCampaigns(SessionContext ctx)
    {
        List<Campaign> items = getLinkedItems(ctx, false, GeneratedRuleEngineServicesConstants.Relations.CAMPAIGN2SOURCERULERELATION, "Campaign", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<Campaign> getCampaigns()
    {
        return getCampaigns(getSession().getSessionContext());
    }


    public long getCampaignsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedRuleEngineServicesConstants.Relations.CAMPAIGN2SOURCERULERELATION, "Campaign", null);
    }


    public long getCampaignsCount()
    {
        return getCampaignsCount(getSession().getSessionContext());
    }


    public void setCampaigns(SessionContext ctx, Set<Campaign> value)
    {
        setLinkedItems(ctx, false, GeneratedRuleEngineServicesConstants.Relations.CAMPAIGN2SOURCERULERELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGN2SOURCERULERELATION_MARKMODIFIED));
    }


    public void setCampaigns(Set<Campaign> value)
    {
        setCampaigns(getSession().getSessionContext(), value);
    }


    public void addToCampaigns(SessionContext ctx, Campaign value)
    {
        addLinkedItems(ctx, false, GeneratedRuleEngineServicesConstants.Relations.CAMPAIGN2SOURCERULERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGN2SOURCERULERELATION_MARKMODIFIED));
    }


    public void addToCampaigns(Campaign value)
    {
        addToCampaigns(getSession().getSessionContext(), value);
    }


    public void removeFromCampaigns(SessionContext ctx, Campaign value)
    {
        removeLinkedItems(ctx, false, GeneratedRuleEngineServicesConstants.Relations.CAMPAIGN2SOURCERULERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGN2SOURCERULERELATION_MARKMODIFIED));
    }


    public void removeFromCampaigns(Campaign value)
    {
        removeFromCampaigns(getSession().getSessionContext(), value);
    }


    public String getConditions(SessionContext ctx)
    {
        return (String)getProperty(ctx, "conditions");
    }


    public String getConditions()
    {
        return getConditions(getSession().getSessionContext());
    }


    public void setConditions(SessionContext ctx, String value)
    {
        setProperty(ctx, "conditions", value);
    }


    public void setConditions(String value)
    {
        setConditions(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Campaign");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CAMPAIGN2SOURCERULERELATION_MARKMODIFIED);
        }
        return true;
    }
}
