package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.daos.BaseStoreDao;
import de.hybris.platform.warehousing.util.builder.BaseStoreModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class BaseStores extends AbstractItems<BaseStoreModel>
{
    public static final String UID_NORTH_AMERICA = "north-america";
    public static final String CODE_RETURN_PROCESS = "return-process";
    private BaseStoreDao baseStoreDao;
    private Languages languages;
    private Catalogs catalogs;
    private Currencies currencies;
    private Countries countries;
    private DeliveryModes deliveryModes;
    private AtpFormulas atpFormulas;
    private SourcingConfigs sourcingConfigs;
    private ContentCatalogs contentCatalogs;


    public BaseStoreModel NorthAmerica()
    {
        return (BaseStoreModel)getFromCollectionOrSaveAndReturn(() -> getBaseStoreDao().findBaseStoresByUid("north-america"),
                        () -> BaseStoreModelBuilder.aModel().withCatalogs(new CatalogModel[] {(CatalogModel)getContentCatalogs().contentCatalog_online()}).withCurrencies(new CurrencyModel[] {getCurrencies().AmericanDollar()}).withDefaultCurrency(getCurrencies().AmericanDollar())
                                        .withDefaultLanguage(getLanguages().English()).withDeliveryCountries(new CountryModel[] {getCountries().UnitedStates(), getCountries().Canada(), getCountries().France()}).withLanguages(new LanguageModel[] {getLanguages().English()}).withNet(Boolean.FALSE)
                                        .withPaymentProvider("Mockup").withSubmitOrderProcessCode("order-process").withUid("north-america").withDeliveryModes(new DeliveryModeModel[] {getDeliveryModes().Pickup(), getDeliveryModes().Regular()}).withCreateReturnProcessCode("return-process")
                                        .withAtpFormula(this.atpFormulas.Hybris()).withSourcingConfig(this.sourcingConfigs.HybrisConfig()).withExternalTaxEnabled(Boolean.valueOf(true)).build());
    }


    public Languages getLanguages()
    {
        return this.languages;
    }


    @Required
    public void setLanguages(Languages languages)
    {
        this.languages = languages;
    }


    public BaseStoreDao getBaseStoreDao()
    {
        return this.baseStoreDao;
    }


    @Required
    public void setBaseStoreDao(BaseStoreDao baseStoreDao)
    {
        this.baseStoreDao = baseStoreDao;
    }


    public Catalogs getCatalogs()
    {
        return this.catalogs;
    }


    @Required
    public void setCatalogs(Catalogs catalogs)
    {
        this.catalogs = catalogs;
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


    public Currencies getCurrencies()
    {
        return this.currencies;
    }


    @Required
    public void setCurrencies(Currencies currencies)
    {
        this.currencies = currencies;
    }


    public Countries getCountries()
    {
        return this.countries;
    }


    @Required
    public void setCountries(Countries countries)
    {
        this.countries = countries;
    }


    public DeliveryModes getDeliveryModes()
    {
        return this.deliveryModes;
    }


    @Required
    public void setDeliveryModes(DeliveryModes deliveryModes)
    {
        this.deliveryModes = deliveryModes;
    }


    @Required
    public void setAtpFormulas(AtpFormulas atpFormulas)
    {
        this.atpFormulas = atpFormulas;
    }


    public AtpFormulas getAtpFormulas()
    {
        return this.atpFormulas;
    }


    public SourcingConfigs getSourcingConfigs()
    {
        return this.sourcingConfigs;
    }


    @Required
    public void setSourcingConfigs(SourcingConfigs sourcingConfigs)
    {
        this.sourcingConfigs = sourcingConfigs;
    }
}
