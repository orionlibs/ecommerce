package de.hybris.platform.basecommerce.model.site;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.model.CartRemovalCronJobModel;
import de.hybris.platform.acceleratorservices.model.UncollectedOrdersCronJobModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.enums.SiteTheme;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.store.BaseStoreModel;
import java.util.List;
import java.util.Locale;

public class BaseSiteModel extends ItemModel
{
    public static final String _TYPECODE = "BaseSite";
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String STORES = "stores";
    public static final String REQUIRESAUTHENTICATION = "requiresAuthentication";
    public static final String THEME = "theme";
    public static final String DEFAULTLANGUAGE = "defaultLanguage";
    public static final String LOCALE = "locale";
    public static final String CHANNEL = "channel";
    public static final String DEFAULTPROMOTIONGROUP = "defaultPromotionGroup";
    public static final String SOLRFACETSEARCHCONFIGURATION = "solrFacetSearchConfiguration";
    public static final String DEFAULTSTOCKLEVELTHRESHOLD = "defaultStockLevelThreshold";
    public static final String PRODUCTINDEXTYPE = "productIndexType";
    public static final String ENABLEREGISTRATION = "enableRegistration";
    public static final String CARTREMOVALAGE = "cartRemovalAge";
    public static final String ANONYMOUSCARTREMOVALAGE = "anonymousCartRemovalAge";
    public static final String CARTREMOVALCRONJOB = "cartRemovalCronJob";
    public static final String UNCOLLECTEDORDERSCRONJOB = "uncollectedOrdersCronJob";


    public BaseSiteModel()
    {
    }


    public BaseSiteModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BaseSiteModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BaseSiteModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "anonymousCartRemovalAge", type = Accessor.Type.GETTER)
    public Integer getAnonymousCartRemovalAge()
    {
        return (Integer)getPersistenceContext().getPropertyValue("anonymousCartRemovalAge");
    }


    @Accessor(qualifier = "cartRemovalAge", type = Accessor.Type.GETTER)
    public Integer getCartRemovalAge()
    {
        return (Integer)getPersistenceContext().getPropertyValue("cartRemovalAge");
    }


    @Accessor(qualifier = "cartRemovalCronJob", type = Accessor.Type.GETTER)
    public CartRemovalCronJobModel getCartRemovalCronJob()
    {
        return (CartRemovalCronJobModel)getPersistenceContext().getPropertyValue("cartRemovalCronJob");
    }


    @Accessor(qualifier = "channel", type = Accessor.Type.GETTER)
    public SiteChannel getChannel()
    {
        return (SiteChannel)getPersistenceContext().getPropertyValue("channel");
    }


    @Accessor(qualifier = "defaultLanguage", type = Accessor.Type.GETTER)
    public LanguageModel getDefaultLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("defaultLanguage");
    }


    @Accessor(qualifier = "defaultPromotionGroup", type = Accessor.Type.GETTER)
    public PromotionGroupModel getDefaultPromotionGroup()
    {
        return (PromotionGroupModel)getPersistenceContext().getPropertyValue("defaultPromotionGroup");
    }


    @Accessor(qualifier = "defaultStockLevelThreshold", type = Accessor.Type.GETTER)
    public Integer getDefaultStockLevelThreshold()
    {
        return (Integer)getPersistenceContext().getPropertyValue("defaultStockLevelThreshold");
    }


    @Accessor(qualifier = "locale", type = Accessor.Type.GETTER)
    public String getLocale()
    {
        return getLocale(null);
    }


    @Accessor(qualifier = "locale", type = Accessor.Type.GETTER)
    public String getLocale(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("locale", loc);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "productIndexType", type = Accessor.Type.GETTER)
    public SnIndexTypeModel getProductIndexType()
    {
        return (SnIndexTypeModel)getPersistenceContext().getPropertyValue("productIndexType");
    }


    @Accessor(qualifier = "solrFacetSearchConfiguration", type = Accessor.Type.GETTER)
    public SolrFacetSearchConfigModel getSolrFacetSearchConfiguration()
    {
        return (SolrFacetSearchConfigModel)getPersistenceContext().getPropertyValue("solrFacetSearchConfiguration");
    }


    @Accessor(qualifier = "stores", type = Accessor.Type.GETTER)
    public List<BaseStoreModel> getStores()
    {
        return (List<BaseStoreModel>)getPersistenceContext().getPropertyValue("stores");
    }


    @Accessor(qualifier = "theme", type = Accessor.Type.GETTER)
    public SiteTheme getTheme()
    {
        return (SiteTheme)getPersistenceContext().getPropertyValue("theme");
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
    public String getUid()
    {
        return (String)getPersistenceContext().getPropertyValue("uid");
    }


    @Accessor(qualifier = "uncollectedOrdersCronJob", type = Accessor.Type.GETTER)
    public UncollectedOrdersCronJobModel getUncollectedOrdersCronJob()
    {
        return (UncollectedOrdersCronJobModel)getPersistenceContext().getPropertyValue("uncollectedOrdersCronJob");
    }


    @Accessor(qualifier = "enableRegistration", type = Accessor.Type.GETTER)
    public boolean isEnableRegistration()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("enableRegistration"));
    }


    @Accessor(qualifier = "requiresAuthentication", type = Accessor.Type.GETTER)
    public boolean isRequiresAuthentication()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("requiresAuthentication"));
    }


    @Accessor(qualifier = "anonymousCartRemovalAge", type = Accessor.Type.SETTER)
    public void setAnonymousCartRemovalAge(Integer value)
    {
        getPersistenceContext().setPropertyValue("anonymousCartRemovalAge", value);
    }


    @Accessor(qualifier = "cartRemovalAge", type = Accessor.Type.SETTER)
    public void setCartRemovalAge(Integer value)
    {
        getPersistenceContext().setPropertyValue("cartRemovalAge", value);
    }


    @Accessor(qualifier = "cartRemovalCronJob", type = Accessor.Type.SETTER)
    public void setCartRemovalCronJob(CartRemovalCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("cartRemovalCronJob", value);
    }


    @Accessor(qualifier = "channel", type = Accessor.Type.SETTER)
    public void setChannel(SiteChannel value)
    {
        getPersistenceContext().setPropertyValue("channel", value);
    }


    @Accessor(qualifier = "defaultLanguage", type = Accessor.Type.SETTER)
    public void setDefaultLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("defaultLanguage", value);
    }


    @Accessor(qualifier = "defaultPromotionGroup", type = Accessor.Type.SETTER)
    public void setDefaultPromotionGroup(PromotionGroupModel value)
    {
        getPersistenceContext().setPropertyValue("defaultPromotionGroup", value);
    }


    @Accessor(qualifier = "defaultStockLevelThreshold", type = Accessor.Type.SETTER)
    public void setDefaultStockLevelThreshold(Integer value)
    {
        getPersistenceContext().setPropertyValue("defaultStockLevelThreshold", value);
    }


    @Accessor(qualifier = "enableRegistration", type = Accessor.Type.SETTER)
    public void setEnableRegistration(boolean value)
    {
        getPersistenceContext().setPropertyValue("enableRegistration", toObject(value));
    }


    @Accessor(qualifier = "locale", type = Accessor.Type.SETTER)
    public void setLocale(String value)
    {
        setLocale(value, null);
    }


    @Accessor(qualifier = "locale", type = Accessor.Type.SETTER)
    public void setLocale(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("locale", loc, value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "productIndexType", type = Accessor.Type.SETTER)
    public void setProductIndexType(SnIndexTypeModel value)
    {
        getPersistenceContext().setPropertyValue("productIndexType", value);
    }


    @Accessor(qualifier = "requiresAuthentication", type = Accessor.Type.SETTER)
    public void setRequiresAuthentication(boolean value)
    {
        getPersistenceContext().setPropertyValue("requiresAuthentication", toObject(value));
    }


    @Accessor(qualifier = "solrFacetSearchConfiguration", type = Accessor.Type.SETTER)
    public void setSolrFacetSearchConfiguration(SolrFacetSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("solrFacetSearchConfiguration", value);
    }


    @Accessor(qualifier = "stores", type = Accessor.Type.SETTER)
    public void setStores(List<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("stores", value);
    }


    @Accessor(qualifier = "theme", type = Accessor.Type.SETTER)
    public void setTheme(SiteTheme value)
    {
        getPersistenceContext().setPropertyValue("theme", value);
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
    public void setUid(String value)
    {
        getPersistenceContext().setPropertyValue("uid", value);
    }


    @Accessor(qualifier = "uncollectedOrdersCronJob", type = Accessor.Type.SETTER)
    public void setUncollectedOrdersCronJob(UncollectedOrdersCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("uncollectedOrdersCronJob", value);
    }
}
