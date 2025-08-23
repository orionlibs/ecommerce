package de.hybris.platform.commercewebservicescommons.dto.basestore;

import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryModeListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.LanguageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.CountryWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "BaseStore", description = "Representation of a Base Store")
public class BaseStoreWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "name", value = "Base store name")
    private String name;
    @ApiModelProperty(name = "externalTaxEnabled", value = "Flag defining is external tax is enabled")
    private Boolean externalTaxEnabled;
    @ApiModelProperty(name = "paymentProvider", value = "Payment provider")
    private String paymentProvider;
    @ApiModelProperty(name = "createReturnProcessCode", value = "Create return process code")
    private String createReturnProcessCode;
    @ApiModelProperty(name = "maxRadiusForPosSearch", value = "Maximum radius for searching point of service")
    private Double maxRadiusForPosSearch;
    @ApiModelProperty(name = "submitOrderProcessCode", value = "Submit order process code")
    private String submitOrderProcessCode;
    @ApiModelProperty(name = "currencies", value = "List of currencies")
    private List<CurrencyWsDTO> currencies;
    @ApiModelProperty(name = "defaultCurrency", value = "Default currency")
    private CurrencyWsDTO defaultCurrency;
    @ApiModelProperty(name = "defaultDeliveryOrigin", value = "Point of service being default delivery origin")
    private PointOfServiceWsDTO defaultDeliveryOrigin;
    @ApiModelProperty(name = "defaultLanguage", value = "Default language")
    private LanguageWsDTO defaultLanguage;
    @ApiModelProperty(name = "deliveryCountries", value = "List of delivery countries")
    private List<CountryWsDTO> deliveryCountries;
    @ApiModelProperty(name = "deliveryModes", value = "List of delivery modes")
    private DeliveryModeListWsDTO deliveryModes;
    @ApiModelProperty(name = "languages", value = "List of languages")
    private List<LanguageWsDTO> languages;
    @ApiModelProperty(name = "pointsOfService", value = "List of points of service")
    private List<PointOfServiceWsDTO> pointsOfService;
    @ApiModelProperty(name = "expressCheckoutEnabled", value = "Flag specifying whether the express checkout option is enabled")
    private Boolean expressCheckoutEnabled;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setExternalTaxEnabled(Boolean externalTaxEnabled)
    {
        this.externalTaxEnabled = externalTaxEnabled;
    }


    public Boolean getExternalTaxEnabled()
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


    public void setCurrencies(List<CurrencyWsDTO> currencies)
    {
        this.currencies = currencies;
    }


    public List<CurrencyWsDTO> getCurrencies()
    {
        return this.currencies;
    }


    public void setDefaultCurrency(CurrencyWsDTO defaultCurrency)
    {
        this.defaultCurrency = defaultCurrency;
    }


    public CurrencyWsDTO getDefaultCurrency()
    {
        return this.defaultCurrency;
    }


    public void setDefaultDeliveryOrigin(PointOfServiceWsDTO defaultDeliveryOrigin)
    {
        this.defaultDeliveryOrigin = defaultDeliveryOrigin;
    }


    public PointOfServiceWsDTO getDefaultDeliveryOrigin()
    {
        return this.defaultDeliveryOrigin;
    }


    public void setDefaultLanguage(LanguageWsDTO defaultLanguage)
    {
        this.defaultLanguage = defaultLanguage;
    }


    public LanguageWsDTO getDefaultLanguage()
    {
        return this.defaultLanguage;
    }


    public void setDeliveryCountries(List<CountryWsDTO> deliveryCountries)
    {
        this.deliveryCountries = deliveryCountries;
    }


    public List<CountryWsDTO> getDeliveryCountries()
    {
        return this.deliveryCountries;
    }


    public void setDeliveryModes(DeliveryModeListWsDTO deliveryModes)
    {
        this.deliveryModes = deliveryModes;
    }


    public DeliveryModeListWsDTO getDeliveryModes()
    {
        return this.deliveryModes;
    }


    public void setLanguages(List<LanguageWsDTO> languages)
    {
        this.languages = languages;
    }


    public List<LanguageWsDTO> getLanguages()
    {
        return this.languages;
    }


    public void setPointsOfService(List<PointOfServiceWsDTO> pointsOfService)
    {
        this.pointsOfService = pointsOfService;
    }


    public List<PointOfServiceWsDTO> getPointsOfService()
    {
        return this.pointsOfService;
    }


    public void setExpressCheckoutEnabled(Boolean expressCheckoutEnabled)
    {
        this.expressCheckoutEnabled = expressCheckoutEnabled;
    }


    public Boolean getExpressCheckoutEnabled()
    {
        return this.expressCheckoutEnabled;
    }
}
