package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Lists;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.store.BaseStoreModel;
import java.util.List;

public class CmsSiteModelBuilder
{
    private final CMSSiteModel model = new CMSSiteModel();


    public static CmsSiteModelBuilder aModel()
    {
        return new CmsSiteModelBuilder();
    }


    private CMSSiteModel getModel()
    {
        return this.model;
    }


    public CMSSiteModel build()
    {
        return getModel();
    }


    public CmsSiteModelBuilder withUid(String uid)
    {
        getModel().setActive(Boolean.valueOf(true));
        getModel().setUid(uid);
        return this;
    }


    public CmsSiteModelBuilder withChannel(SiteChannel channel)
    {
        getModel().setChannel(channel);
        return this;
    }


    public CmsSiteModelBuilder withStores(BaseStoreModel... stores)
    {
        getModel().setStores(Lists.newArrayList((Object[])stores));
        return this;
    }


    public CmsSiteModelBuilder withContentCatalogs(List<ContentCatalogModel> contentCatalogModels)
    {
        getModel().setContentCatalogs(contentCatalogModels);
        return this;
    }


    public CmsSiteModelBuilder withDefaultPromotionGroup(PromotionGroupModel promotionGroup)
    {
        getModel().setDefaultPromotionGroup(promotionGroup);
        return this;
    }
}
