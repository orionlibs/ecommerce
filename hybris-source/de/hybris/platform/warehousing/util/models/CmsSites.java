package de.hybris.platform.warehousing.util.models;

import com.google.common.collect.Lists;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.util.builder.CmsSiteModelBuilder;
import de.hybris.platform.warehousing.util.builder.PromotionGroupBuilder;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class CmsSites extends AbstractItems<CMSSiteModel>
{
    public static final String UID_CANADA = "canada";
    private CMSSiteDao cmsSiteDao;
    private BaseStores baseStores;
    private ContentCatalogs contentCatalogs;


    public CMSSiteModel Canada()
    {
        List<CMSSiteModel> cmsSitesList = getCMSSiteDao().findCMSSitesById("canada");
        return (CMSSiteModel)getOrSaveAndReturn(() -> cmsSitesList.isEmpty() ? null : cmsSitesList.get(0),
                        () -> CmsSiteModelBuilder.aModel().withUid("canada").withChannel(SiteChannel.B2C).withStores(new BaseStoreModel[] {getBaseStores().NorthAmerica()}).withDefaultPromotionGroup(PromotionGroupBuilder.aModel().withIdentifier("").build())
                                        .withContentCatalogs(Lists.newArrayList((Object[])new ContentCatalogModel[] {getContentCatalogs().contentCatalog_online()})).build());
    }


    public CMSSiteDao getCMSSiteDao()
    {
        return this.cmsSiteDao;
    }


    @Required
    public void setCMSSiteDao(CMSSiteDao cmsSiteDao)
    {
        this.cmsSiteDao = cmsSiteDao;
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


    public ContentCatalogs getContentCatalogs()
    {
        return this.contentCatalogs;
    }


    @Required
    public void setContentCatalogs(ContentCatalogs contentCatalogs)
    {
        this.contentCatalogs = contentCatalogs;
    }
}
