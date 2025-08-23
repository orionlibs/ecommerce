package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.model.AtpFormulaModel;
import de.hybris.platform.warehousing.model.SourcingConfigModel;

public class BaseStoreModelBuilder
{
    private final BaseStoreModel model = new BaseStoreModel();


    private BaseStoreModel getModel()
    {
        return this.model;
    }


    public static BaseStoreModelBuilder aModel()
    {
        return new BaseStoreModelBuilder();
    }


    public BaseStoreModel build()
    {
        return getModel();
    }


    public BaseStoreModelBuilder withUid(String uid)
    {
        getModel().setUid(uid);
        return this;
    }


    public BaseStoreModelBuilder withCatalogs(CatalogModel... catalogs)
    {
        getModel().setCatalogs(Lists.newArrayList((Object[])catalogs));
        return this;
    }


    public BaseStoreModelBuilder withCreateReturnProcessCode(String returnProcessCode)
    {
        getModel().setCreateReturnProcessCode(returnProcessCode);
        return this;
    }


    public BaseStoreModelBuilder withCurrencies(CurrencyModel... currencies)
    {
        getModel().setCurrencies(Sets.newHashSet((Object[])currencies));
        return this;
    }


    public BaseStoreModelBuilder withDeliveryCountries(CountryModel... deliveryCountries)
    {
        getModel().setDeliveryCountries(Lists.newArrayList((Object[])deliveryCountries));
        return this;
    }


    public BaseStoreModelBuilder withLanguages(LanguageModel... languages)
    {
        getModel().setLanguages(Sets.newHashSet((Object[])languages));
        return this;
    }


    public BaseStoreModelBuilder withDefaultCurrency(CurrencyModel defaultCurrency)
    {
        getModel().setDefaultCurrency(defaultCurrency);
        return this;
    }


    public BaseStoreModelBuilder withDefaultLanguage(LanguageModel defaultLanguage)
    {
        getModel().setDefaultLanguage(defaultLanguage);
        return this;
    }


    public BaseStoreModelBuilder withNet(Boolean net)
    {
        getModel().setNet(net.booleanValue());
        return this;
    }


    public BaseStoreModelBuilder withSubmitOrderProcessCode(String submitOrderProcessCode)
    {
        getModel().setSubmitOrderProcessCode(submitOrderProcessCode);
        return this;
    }


    public BaseStoreModelBuilder withPaymentProvider(String paymentProvider)
    {
        getModel().setPaymentProvider(paymentProvider);
        return this;
    }


    public BaseStoreModelBuilder withDeliveryModes(DeliveryModeModel... deliveryModes)
    {
        getModel().setDeliveryModes(Sets.newHashSet((Object[])deliveryModes));
        return this;
    }


    public BaseStoreModelBuilder withAtpFormula(AtpFormulaModel atpFormula)
    {
        getModel().setDefaultAtpFormula(atpFormula);
        return this;
    }


    public BaseStoreModelBuilder withSourcingConfig(SourcingConfigModel sourcingConfig)
    {
        getModel().setSourcingConfig(sourcingConfig);
        return this;
    }


    public BaseStoreModelBuilder withExternalTaxEnabled(Boolean externalTaxEnabled)
    {
        getModel().setExternalTaxEnabled(externalTaxEnabled);
        return this;
    }
}
