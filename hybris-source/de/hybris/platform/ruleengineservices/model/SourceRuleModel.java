package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class SourceRuleModel extends AbstractRuleModel
{
    public static final String _TYPECODE = "SourceRule";
    public static final String _CAMPAIGN2SOURCERULERELATION = "Campaign2SourceRuleRelation";
    public static final String CONDITIONS = "conditions";
    public static final String ACTIONS = "actions";
    public static final String CAMPAIGNS = "campaigns";


    public SourceRuleModel()
    {
    }


    public SourceRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SourceRuleModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SourceRuleModel(String _code, ItemModel _owner, String _uuid)
    {
        setCode(_code);
        setOwner(_owner);
        setUuid(_uuid);
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
    public String getActions()
    {
        return (String)getPersistenceContext().getPropertyValue("actions");
    }


    @Accessor(qualifier = "campaigns", type = Accessor.Type.GETTER)
    public Set<CampaignModel> getCampaigns()
    {
        return (Set<CampaignModel>)getPersistenceContext().getPropertyValue("campaigns");
    }


    @Accessor(qualifier = "conditions", type = Accessor.Type.GETTER)
    public String getConditions()
    {
        return (String)getPersistenceContext().getPropertyValue("conditions");
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
    public void setActions(String value)
    {
        getPersistenceContext().setPropertyValue("actions", value);
    }


    @Accessor(qualifier = "campaigns", type = Accessor.Type.SETTER)
    public void setCampaigns(Set<CampaignModel> value)
    {
        getPersistenceContext().setPropertyValue("campaigns", value);
    }


    @Accessor(qualifier = "conditions", type = Accessor.Type.SETTER)
    public void setConditions(String value)
    {
        getPersistenceContext().setPropertyValue("conditions", value);
    }
}
