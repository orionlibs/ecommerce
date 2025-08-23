package de.hybris.platform.commercefacades.basestore.data;

import de.hybris.platform.commercefacades.order.data.DeliveryModesData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import java.io.Serializable;
import java.util.List;

public class BaseStoreData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private boolean externalTaxEnabled;
    private String paymentProvider;
    private String createReturnProcessCode;
    private Double maxRadiusForPosSearch;
    private String submitOrderProcessCode;
    private List<CurrencyData> currencies;
    private CurrencyData defaultCurrency;
    private PointOfServiceData defaultDeliveryOrigin;
    private LanguageData defaultLanguage;
    private List<CountryData> deliveryCountries;
    private DeliveryModesData deliveryModes;
    private List<LanguageData> languages;
    private List<PointOfServiceData> pointsOfService;
    private boolean expressCheckoutEnabled;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setExternalTaxEnabled(boolean externalTaxEnabled)
    {
        this.externalTaxEnabled = externalTaxEnabled;
    }


    public boolean isExternalTaxEnabled()
    {
        return this.externalTaxEnabled;
    }


    public void setPaymentProvider(String paymentProvider)
    {
        this.paymentProvider = paymentProvider;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }


    public void setCreateReturnProcessCode(String createReturnProcessCode)
    {
        this.createReturnProcessCode = createReturnProcessCode;
    }


    public String getCreateReturnProcessCode()
    {
        return this.createReturnProcessCode;
    }


    public void setMaxRadiusForPosSearch(Double maxRadiusForPosSearch)
    {
        this.maxRadiusForPosSearch = maxRadiusForPosSearch;
    }


    public Double getMaxRadiusForPosSearch()
    {
        return this.maxRadiusForPosSearch;
    }


    public void setSubmitOrderProcessCode(String submitOrderProcessCode)
    {
        this.submitOrderProcessCode = submitOrderProcessCode;
    }


    public String getSubmitOrderProcessCode()
    {
        return this.submitOrderProcessCode;
    }


    public void setCurrencies(List<CurrencyData> currencies)
    {
        this.currencies = currencies;
    }


    public List<CurrencyData> getCurrencies()
    {
        return this.currencies;
    }


    public void setDefaultCurrency(CurrencyData defaultCurrency)
    {
        this.defaultCurrency = defaultCurrency;
    }


    public CurrencyData getDefaultCurrency()
    {
        return this.defaultCurrency;
    }


    public void setDefaultDeliveryOrigin(PointOfServiceData defaultDeliveryOrigin)
    {
        this.defaultDeliveryOrigin = defaultDeliveryOrigin;
    }


    public PointOfServiceData getDefaultDeliveryOrigin()
    {
        return this.defaultDeliveryOrigin;
    }


    public void setDefaultLanguage(LanguageData defaultLanguage)
    {
        this.defaultLanguage = defaultLanguage;
    }


    public LanguageData getDefaultLanguage()
    {
        return this.defaultLanguage;
    }


    public void setDeliveryCountries(List<CountryData> deliveryCountries)
    {
        this.deliveryCountries = deliveryCountries;
    }


    public List<CountryData> getDeliveryCountries()
    {
        return this.deliveryCountries;
    }


    public void setDeliveryModes(DeliveryModesData deliveryModes)
    {
        this.deliveryModes = deliveryModes;
    }


    public DeliveryModesData getDeliveryModes()
    {
        return this.deliveryModes;
    }


    public void setLanguages(List<LanguageData> languages)
    {
        this.languages = languages;
    }


    public List<LanguageData> getLanguages()
    {
        return this.languages;
    }


    public void setPointsOfService(List<PointOfServiceData> pointsOfService)
    {
        this.pointsOfService = pointsOfService;
    }


    public List<PointOfServiceData> getPointsOfService()
    {
        return this.pointsOfService;
    }


    public void setExpressCheckoutEnabled(boolean expressCheckoutEnabled)
    {
        this.expressCheckoutEnabled = expressCheckoutEnabled;
    }


    public boolean isExpressCheckoutEnabled()
    {
        return this.expressCheckoutEnabled;
    }
}
