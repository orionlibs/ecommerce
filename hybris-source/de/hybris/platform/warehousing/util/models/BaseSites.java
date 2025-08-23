package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.site.dao.BaseSiteDao;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.util.builder.BaseSiteModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class BaseSites extends AbstractItems<BaseSiteModel>
{
    public static final String UID_AMERICAS = "americas";
    private BaseSiteDao baseSiteDao;
    private BaseStores baseStores;


    public BaseSiteModel Americas()
    {
        return (BaseSiteModel)getOrSaveAndReturn(() -> getBaseSiteDao().findBaseSiteByUID("americas"), () -> BaseSiteModelBuilder.aModel().withUid("americas").withChannel(SiteChannel.B2C).withStores(new BaseStoreModel[] {getBaseStores().NorthAmerica()}).build());
    }


    public BaseSiteDao getBaseSiteDao()
    {
        return this.baseSiteDao;
    }


    @Required
    public void setBaseSiteDao(BaseSiteDao baseSiteDao)
    {
        this.baseSiteDao = baseSiteDao;
    }


    public BaseStores getBaseStores()
    {
        return this.baseStores;
    }


    @Required
    public void setBaseStores(BaseStores baseStores)
    {
        this.baseStores = baseStores;
    }
}
