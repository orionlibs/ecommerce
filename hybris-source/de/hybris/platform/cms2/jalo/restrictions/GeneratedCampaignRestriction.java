package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.campaigns.jalo.Campaign;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCampaignRestriction extends AbstractRestriction
{
    public static final String CAMPAIGNS = "campaigns";
    protected static String CAMPAIGNSFORRESTRICTION_SRC_ORDERED = "relation.CampaignsForRestriction.source.ordered";
    protected static String CAMPAIGNSFORRESTRICTION_TGT_ORDERED = "relation.CampaignsForRestriction.target.ordered";
    protected static String CAMPAIGNSFORRESTRICTION_MARKMODIFIED = "relation.CampaignsForRestriction.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Campaign> getCampaigns(SessionContext ctx)
    {
        List<Campaign> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CAMPAIGNSFORRESTRICTION, "Campaign", null, false, false);
        return items;
    }


    public Collection<Campaign> getCampaigns()
    {
        return getCampaigns(getSession().getSessionContext());
    }


    public long getCampaignsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.CAMPAIGNSFORRESTRICTION, "Campaign", null);
    }


    public long getCampaignsCount()
    {
        return getCampaignsCount(getSession().getSessionContext());
    }


    public void setCampaigns(SessionContext ctx, Collection<Campaign> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CAMPAIGNSFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGNSFORRESTRICTION_MARKMODIFIED));
    }


    public void setCampaigns(Collection<Campaign> value)
    {
        setCampaigns(getSession().getSessionContext(), value);
    }


    public void addToCampaigns(SessionContext ctx, Campaign value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CAMPAIGNSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGNSFORRESTRICTION_MARKMODIFIED));
    }


    public void addToCampaigns(Campaign value)
    {
        addToCampaigns(getSession().getSessionContext(), value);
    }


    public void removeFromCampaigns(SessionContext ctx, Campaign value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CAMPAIGNSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGNSFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromCampaigns(Campaign value)
    {
        removeFromCampaigns(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Campaign");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CAMPAIGNSFORRESTRICTION_MARKMODIFIED);
        }
        return true;
    }
}
