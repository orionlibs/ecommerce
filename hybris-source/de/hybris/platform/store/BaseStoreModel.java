package de.hybris.platform.store;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.DistanceUnit;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.model.restrictions.CMSBaseStoreTimeRestrictionModel;
import de.hybris.platform.commerceservices.enums.PickupInStoreMode;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class BaseStoreModel extends ItemModel
{
    public static final String _TYPECODE = "BaseStore";
    public static final String _STORESFORCMSSITE = "StoresForCMSSite";
    public static final String _STORETIMERESTRICTION2BASESTORE = "StoreTimeRestriction2BaseStore";
    public static final String _BASESTORE2COUNTRYREL = "BaseStore2CountryRel";
    public static final String _BASESTORE2BILLINGCOUNTRYREL = "BaseStore2BillingCountryRel";
    public static final String _AGENT2BASESTORE = "Agent2BaseStore";
    public static final String _CSAGENTGROUP2BASESTORE = "CsAgentGroup2BaseStore";
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String STORELOCATORDISTANCEUNIT = "storelocatorDistanceUnit";
    public static final String CMSSITES = "cmsSites";
    public static final String CATALOGS = "catalogs";
    public static final String POINTSOFSERVICE = "pointsOfService";
    public static final String CMSTIMERESTRICTIONS = "cmsTimeRestrictions";
    public static final String NET = "net";
    public static final String TAXGROUP = "taxGroup";
    public static final String DEFAULTLANGUAGE = "defaultLanguage";
    public static final String DEFAULTCURRENCY = "defaultCurrency";
    public static final String DEFAULTDELIVERYORIGIN = "defaultDeliveryOrigin";
    public static final String SOLRFACETSEARCHCONFIGURATION = "solrFacetSearchConfiguration";
    public static final String SUBMITORDERPROCESSCODE = "submitOrderProcessCode";
    public static final String CREATERETURNPROCESSCODE = "createReturnProcessCode";
    public static final String EXTERNALTAXENABLED = "externalTaxEnabled";
    public static final String PICKUPINSTOREMODE = "pickupInStoreMode";
    public static final String MAXRADIUSFORPOSSEARCH = "maxRadiusForPoSSearch";
    public static final String PRODUCTSEARCHSTRATEGY = "productSearchStrategy";
    public static final String CUSTOMERALLOWEDTOIGNORESUGGESTIONS = "customerAllowedToIgnoreSuggestions";
    public static final String PAYMENTPROVIDER = "paymentProvider";
    public static final String CURRENCIES = "currencies";
    public static final String LANGUAGES = "languages";
    public static final String DELIVERYCOUNTRIES = "deliveryCountries";
    public static final String BILLINGCOUNTRIES = "billingCountries";
    public static final String WAREHOUSES = "warehouses";
    public static final String DELIVERYMODES = "deliveryModes";
    public static final String PRODUCTINDEXTYPE = "productIndexType";
    public static final String TICKETEMPLOYEES = "ticketemployees";
    public static final String TICKETGROUPS = "ticketgroups";
    public static final String EXPRESSCHECKOUTENABLED = "expressCheckoutEnabled";
    public static final String TAXESTIMATIONENABLED = "taxEstimationEnabled";
    public static final String CHECKOUTFLOWGROUP = "checkoutFlowGroup";
    public static final String SAPCONFIGURATION = "SAPConfiguration";


    public BaseStoreModel()
    {
    }


    public BaseStoreModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BaseStoreModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BaseStoreModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "billingCountries", type = Accessor.Type.GETTER)
    public Collection<CountryModel> getBillingCountries()
    {
        return (Collection<CountryModel>)getPersistenceContext().getPropertyValue("billingCountries");
    }


    @Accessor(qualifier = "catalogs", type = Accessor.Type.GETTER)
    public List<CatalogModel> getCatalogs()
    {
        return (List<CatalogModel>)getPersistenceContext().getPropertyValue("catalogs");
    }


    @Accessor(qualifier = "checkoutFlowGroup", type = Accessor.Type.GETTER)
    public String getCheckoutFlowGroup()
    {
        return (String)getPersistenceContext().getPropertyValue("checkoutFlowGroup");
    }


    @Accessor(qualifier = "cmsSites", type = Accessor.Type.GETTER)
    public Collection<BaseSiteModel> getCmsSites()
    {
        return (Collection<BaseSiteModel>)getPersistenceContext().getPropertyValue("cmsSites");
    }


    @Accessor(qualifier = "cmsTimeRestrictions", type = Accessor.Type.GETTER)
    public Collection<CMSBaseStoreTimeRestrictionModel> getCmsTimeRestrictions()
    {
        return (Collection<CMSBaseStoreTimeRestrictionModel>)getPersistenceContext().getPropertyValue("cmsTimeRestrictions");
    }


    @Accessor(qualifier = "createReturnProcessCode", type = Accessor.Type.GETTER)
    public String getCreateReturnProcessCode()
    {
        return (String)getPersistenceContext().getPropertyValue("createReturnProcessCode");
    }


    @Accessor(qualifier = "currencies", type = Accessor.Type.GETTER)
    public Set<CurrencyModel> getCurrencies()
    {
        return (Set<CurrencyModel>)getPersistenceContext().getPropertyValue("currencies");
    }


    @Accessor(qualifier = "defaultCurrency", type = Accessor.Type.GETTER)
    public CurrencyModel getDefaultCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("defaultCurrency");
    }


    @Accessor(qualifier = "defaultDeliveryOrigin", type = Accessor.Type.GETTER)
    public PointOfServiceModel getDefaultDeliveryOrigin()
    {
        return (PointOfServiceModel)getPersistenceContext().getPropertyValue("defaultDeliveryOrigin");
    }


    @Accessor(qualifier = "defaultLanguage", type = Accessor.Type.GETTER)
    public LanguageModel getDefaultLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("defaultLanguage");
    }


    @Accessor(qualifier = "deliveryCountries", type = Accessor.Type.GETTER)
    public Collection<CountryModel> getDeliveryCountries()
    {
        return (Collection<CountryModel>)getPersistenceContext().getPropertyValue("deliveryCountries");
    }


    @Accessor(qualifier = "deliveryModes", type = Accessor.Type.GETTER)
    public Set<DeliveryModeModel> getDeliveryModes()
    {
        return (Set<DeliveryModeModel>)getPersistenceContext().getPropertyValue("deliveryModes");
    }


    @Accessor(qualifier = "expressCheckoutEnabled", type = Accessor.Type.GETTER)
    public Boolean getExpressCheckoutEnabled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("expressCheckoutEnabled");
    }


    @Accessor(qualifier = "externalTaxEnabled", type = Accessor.Type.GETTER)
    public Boolean getExternalTaxEnabled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("externalTaxEnabled");
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
    public Set<LanguageModel> getLanguages()
    {
        return (Set<LanguageModel>)getPersistenceContext().getPropertyValue("languages");
    }


    @Accessor(qualifier = "maxRadiusForPoSSearch", type = Accessor.Type.GETTER)
    public Double getMaxRadiusForPoSSearch()
    {
        return (Double)getPersistenceContext().getPropertyValue("maxRadiusForPoSSearch");
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


    @Accessor(qualifier = "paymentProvider", type = Accessor.Type.GETTER)
    public String getPaymentProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("paymentProvider");
    }


    @Accessor(qualifier = "pickupInStoreMode", type = Accessor.Type.GETTER)
    public PickupInStoreMode getPickupInStoreMode()
    {
        return (PickupInStoreMode)getPersistenceContext().getPropertyValue("pickupInStoreMode");
    }


    @Accessor(qualifier = "pointsOfService", type = Accessor.Type.GETTER)
    public List<PointOfServiceModel> getPointsOfService()
    {
        return (List<PointOfServiceModel>)getPersistenceContext().getPropertyValue("pointsOfService");
    }


    @Accessor(qualifier = "productIndexType", type = Accessor.Type.GETTER)
    public SnIndexTypeModel getProductIndexType()
    {
        return (SnIndexTypeModel)getPersistenceContext().getPropertyValue("productIndexType");
    }


    @Accessor(qualifier = "productSearchStrategy", type = Accessor.Type.GETTER)
    public String getProductSearchStrategy()
    {
        return (String)getPersistenceContext().getPropertyValue("productSearchStrategy");
    }


    @Accessor(qualifier = "SAPConfiguration", type = Accessor.Type.GETTER)
    public SAPConfigurationModel getSAPConfiguration()
    {
        return (SAPConfigurationModel)getPersistenceContext().getPropertyValue("SAPConfiguration");
    }


    @Accessor(qualifier = "solrFacetSearchConfiguration", type = Accessor.Type.GETTER)
    public SolrFacetSearchConfigModel getSolrFacetSearchConfiguration()
    {
        return (SolrFacetSearchConfigModel)getPersistenceContext().getPropertyValue("solrFacetSearchConfiguration");
    }


    @Accessor(qualifier = "storelocatorDistanceUnit", type = Accessor.Type.GETTER)
    public DistanceUnit getStorelocatorDistanceUnit()
    {
        return (DistanceUnit)getPersistenceContext().getPropertyValue("storelocatorDistanceUnit");
    }


    @Accessor(qualifier = "submitOrderProcessCode", type = Accessor.Type.GETTER)
    public String getSubmitOrderProcessCode()
    {
        return (String)getPersistenceContext().getPropertyValue("submitOrderProcessCode");
    }


    @Accessor(qualifier = "taxEstimationEnabled", type = Accessor.Type.GETTER)
    public Boolean getTaxEstimationEnabled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("taxEstimationEnabled");
    }


    @Accessor(qualifier = "taxGroup", type = Accessor.Type.GETTER)
    public UserTaxGroup getTaxGroup()
    {
        return (UserTaxGroup)getPersistenceContext().getPropertyValue("taxGroup");
    }


    @Accessor(qualifier = "ticketemployees", type = Accessor.Type.GETTER)
    public List<EmployeeModel> getTicketemployees()
    {
        return (List<EmployeeModel>)getPersistenceContext().getPropertyValue("ticketemployees");
    }


    @Accessor(qualifier = "ticketgroups", type = Accessor.Type.GETTER)
    public List<CsAgentGroupModel> getTicketgroups()
    {
        return (List<CsAgentGroupModel>)getPersistenceContext().getPropertyValue("ticketgroups");
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
    public String getUid()
    {
        return (String)getPersistenceContext().getPropertyValue("uid");
    }


    @Accessor(qualifier = "warehouses", type = Accessor.Type.GETTER)
    public List<WarehouseModel> getWarehouses()
    {
        return (List<WarehouseModel>)getPersistenceContext().getPropertyValue("warehouses");
    }


    @Accessor(qualifier = "customerAllowedToIgnoreSuggestions", type = Accessor.Type.GETTER)
    public boolean isCustomerAllowedToIgnoreSuggestions()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("customerAllowedToIgnoreSuggestions"));
    }


    @Accessor(qualifier = "net", type = Accessor.Type.GETTER)
    public boolean isNet()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("net"));
    }


    @Accessor(qualifier = "billingCountries", type = Accessor.Type.SETTER)
    public void setBillingCountries(Collection<CountryModel> value)
    {
        getPersistenceContext().setPropertyValue("billingCountries", value);
    }


    @Accessor(qualifier = "catalogs", type = Accessor.Type.SETTER)
    public void setCatalogs(List<CatalogModel> value)
    {
        getPersistenceContext().setPropertyValue("catalogs", value);
    }


    @Accessor(qualifier = "checkoutFlowGroup", type = Accessor.Type.SETTER)
    public void setCheckoutFlowGroup(String value)
    {
        getPersistenceContext().setPropertyValue("checkoutFlowGroup", value);
    }


    @Accessor(qualifier = "cmsSites", type = Accessor.Type.SETTER)
    public void setCmsSites(Collection<BaseSiteModel> value)
    {
        getPersistenceContext().setPropertyValue("cmsSites", value);
    }


    @Accessor(qualifier = "cmsTimeRestrictions", type = Accessor.Type.SETTER)
    public void setCmsTimeRestrictions(Collection<CMSBaseStoreTimeRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("cmsTimeRestrictions", value);
    }


    @Accessor(qualifier = "createReturnProcessCode", type = Accessor.Type.SETTER)
    public void setCreateReturnProcessCode(String value)
    {
        getPersistenceContext().setPropertyValue("createReturnProcessCode", value);
    }


    @Accessor(qualifier = "currencies", type = Accessor.Type.SETTER)
    public void setCurrencies(Set<CurrencyModel> value)
    {
        getPersistenceContext().setPropertyValue("currencies", value);
    }


    @Accessor(qualifier = "customerAllowedToIgnoreSuggestions", type = Accessor.Type.SETTER)
    public void setCustomerAllowedToIgnoreSuggestions(boolean value)
    {
        getPersistenceContext().setPropertyValue("customerAllowedToIgnoreSuggestions", toObject(value));
    }


    @Accessor(qualifier = "defaultCurrency", type = Accessor.Type.SETTER)
    public void setDefaultCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("defaultCurrency", value);
    }


    @Accessor(qualifier = "defaultDeliveryOrigin", type = Accessor.Type.SETTER)
    public void setDefaultDeliveryOrigin(PointOfServiceModel value)
    {
        getPersistenceContext().setPropertyValue("defaultDeliveryOrigin", value);
    }


    @Accessor(qualifier = "defaultLanguage", type = Accessor.Type.SETTER)
    public void setDefaultLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("defaultLanguage", value);
    }


    @Accessor(qualifier = "deliveryCountries", type = Accessor.Type.SETTER)
    public void setDeliveryCountries(Collection<CountryModel> value)
    {
        getPersistenceContext().setPropertyValue("deliveryCountries", value);
    }


    @Accessor(qualifier = "deliveryModes", type = Accessor.Type.SETTER)
    public void setDeliveryModes(Set<DeliveryModeModel> value)
    {
        getPersistenceContext().setPropertyValue("deliveryModes", value);
    }


    @Accessor(qualifier = "expressCheckoutEnabled", type = Accessor.Type.SETTER)
    public void setExpressCheckoutEnabled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("expressCheckoutEnabled", value);
    }


    @Accessor(qualifier = "externalTaxEnabled", type = Accessor.Type.SETTER)
    public void setExternalTaxEnabled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("externalTaxEnabled", value);
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
    public void setLanguages(Set<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("languages", value);
    }


    @Accessor(qualifier = "maxRadiusForPoSSearch", type = Accessor.Type.SETTER)
    public void setMaxRadiusForPoSSearch(Double value)
    {
        getPersistenceContext().setPropertyValue("maxRadiusForPoSSearch", value);
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


    @Accessor(qualifier = "net", type = Accessor.Type.SETTER)
    public void setNet(boolean value)
    {
        getPersistenceContext().setPropertyValue("net", toObject(value));
    }


    @Accessor(qualifier = "paymentProvider", type = Accessor.Type.SETTER)
    public void setPaymentProvider(String value)
    {
        getPersistenceContext().setPropertyValue("paymentProvider", value);
    }


    @Accessor(qualifier = "pickupInStoreMode", type = Accessor.Type.SETTER)
    public void setPickupInStoreMode(PickupInStoreMode value)
    {
        getPersistenceContext().setPropertyValue("pickupInStoreMode", value);
    }


    @Accessor(qualifier = "pointsOfService", type = Accessor.Type.SETTER)
    public void setPointsOfService(List<PointOfServiceModel> value)
    {
        getPersistenceContext().setPropertyValue("pointsOfService", value);
    }


    @Accessor(qualifier = "productIndexType", type = Accessor.Type.SETTER)
    public void setProductIndexType(SnIndexTypeModel value)
    {
        getPersistenceContext().setPropertyValue("productIndexType", value);
    }


    @Accessor(qualifier = "productSearchStrategy", type = Accessor.Type.SETTER)
    public void setProductSearchStrategy(String value)
    {
        getPersistenceContext().setPropertyValue("productSearchStrategy", value);
    }


    @Accessor(qualifier = "SAPConfiguration", type = Accessor.Type.SETTER)
    public void setSAPConfiguration(SAPConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("SAPConfiguration", value);
    }


    @Accessor(qualifier = "solrFacetSearchConfiguration", type = Accessor.Type.SETTER)
    public void setSolrFacetSearchConfiguration(SolrFacetSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("solrFacetSearchConfiguration", value);
    }


    @Accessor(qualifier = "storelocatorDistanceUnit", type = Accessor.Type.SETTER)
    public void setStorelocatorDistanceUnit(DistanceUnit value)
    {
        getPersistenceContext().setPropertyValue("storelocatorDistanceUnit", value);
    }


    @Accessor(qualifier = "submitOrderProcessCode", type = Accessor.Type.SETTER)
    public void setSubmitOrderProcessCode(String value)
    {
        getPersistenceContext().setPropertyValue("submitOrderProcessCode", value);
    }


    @Accessor(qualifier = "taxEstimationEnabled", type = Accessor.Type.SETTER)
    public void setTaxEstimationEnabled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("taxEstimationEnabled", value);
    }


    @Accessor(qualifier = "taxGroup", type = Accessor.Type.SETTER)
    public void setTaxGroup(UserTaxGroup value)
    {
        getPersistenceContext().setPropertyValue("taxGroup", value);
    }


    @Accessor(qualifier = "ticketemployees", type = Accessor.Type.SETTER)
    public void setTicketemployees(List<EmployeeModel> value)
    {
        getPersistenceContext().setPropertyValue("ticketemployees", value);
    }


    @Accessor(qualifier = "ticketgroups", type = Accessor.Type.SETTER)
    public void setTicketgroups(List<CsAgentGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("ticketgroups", value);
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
    public void setUid(String value)
    {
        getPersistenceContext().setPropertyValue("uid", value);
    }


    @Accessor(qualifier = "warehouses", type = Accessor.Type.SETTER)
    public void setWarehouses(List<WarehouseModel> value)
    {
        getPersistenceContext().setPropertyValue("warehouses", value);
    }
}
