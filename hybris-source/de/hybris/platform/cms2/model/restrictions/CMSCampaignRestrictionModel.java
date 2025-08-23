package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CMSCampaignRestrictionModel extends AbstractRestrictionModel
{
    public static final String _TYPECODE = "CMSCampaignRestriction";
    public static final String CAMPAIGNS = "campaigns";


    public CMSCampaignRestrictionModel()
    {
    }


    public CMSCampaignRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSCampaignRestrictionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSCampaignRestrictionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "campaigns", type = Accessor.Type.GETTER)
    public Collection<CampaignModel> getCampaigns()
    {
        return (Collection<CampaignModel>)getPersistenceContext().getPropertyValue("campaigns");
    }


    @Accessor(qualifier = "campaigns", type = Accessor.Type.SETTER)
    public void setCampaigns(Collection<CampaignModel> value)
    {
        getPersistenceContext().setPropertyValue("campaigns", value);
    }
}
