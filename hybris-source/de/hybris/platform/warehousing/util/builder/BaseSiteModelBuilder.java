package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Lists;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.store.BaseStoreModel;

public class BaseSiteModelBuilder
{
    private final BaseSiteModel model = new BaseSiteModel();


    private BaseSiteModel getModel()
    {
        return this.model;
    }


    public static BaseSiteModelBuilder aModel()
    {
        return new BaseSiteModelBuilder();
    }


    public BaseSiteModel build()
    {
        return getModel();
    }


    public BaseSiteModelBuilder withUid(String uid)
    {
        getModel().setUid(uid);
        return this;
    }


    public BaseSiteModelBuilder withChannel(SiteChannel channel)
    {
        getModel().setChannel(channel);
        return this;
    }


    public BaseSiteModelBuilder withStores(BaseStoreModel... stores)
    {
        getModel().setStores(Lists.newArrayList((Object[])stores));
        return this;
    }
}
