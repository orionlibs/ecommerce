package de.hybris.platform.personalizationservices.configuration.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.model.consent.ConsentTemplateModel;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.enums.CxCatalogLookupType;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.model.config.CxPeriodicVoterConfigModel;
import de.hybris.platform.personalizationservices.model.config.CxUrlVoterConfigModel;
import de.hybris.platform.personalizationservices.strategies.CxConfigurationLookupStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxConfigurationService implements CxConfigurationService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxConfigurationService.class);
    private static final String BASE_SITE_NULL_MESSAGE = "Base site must not be null";
    private BaseSiteService baseSiteService;
    private ConfigurationService configurationService;
    private final BigDecimal defaultMinAffinity = BigDecimal.valueOf(0.5D);
    private CxConfigurationLookupStrategy cxConfigurationLookupStrategy;


    public Optional<CxConfigModel> getConfiguration()
    {
        return this.cxConfigurationLookupStrategy.getConfiguration();
    }


    public Optional<CxConfigModel> getConfiguration(BaseSiteModel baseSiteModel)
    {
        return this.cxConfigurationLookupStrategy.getConfiguration(baseSiteModel);
    }


    public <T> T getValue(CatalogVersionModel catalogVersion, Function<CxConfigModel, T> accessor, T defaultValue)
    {
        ServicesUtil.validateParameterNotNull(catalogVersion, "catalogVersion must not be null");
        ServicesUtil.validateParameterNotNull(accessor, "accessor must not be null");
        Set<CxConfigModel> configSet = this.cxConfigurationLookupStrategy.getConfigurations(catalogVersion);
        Set<T> attributeValueSet = (Set<T>)configSet.stream().<T>map(accessor).filter(Objects::nonNull).collect(Collectors.toSet());
        if(attributeValueSet.size() == 1)
        {
            return attributeValueSet.iterator().next();
        }
        if(attributeValueSet.size() > 1)
        {
            LOG.debug("There were conflicting values in cx config : {}. Default value will be returned : {}", attributeValueSet, defaultValue);
        }
        else
        {
            LOG.debug("There was no value in cx config. Default value will be returned : {}", defaultValue);
        }
        return defaultValue;
    }


    public Integer getActionResultMaxRepeat()
    {
        return getCurrentBaseSite()
                        .<Integer>map(this::getActionResultMaxRepeat)
                        .orElseGet(this::getDefaultActionResultMaxRepeat);
    }


    public Integer getActionResultMaxRepeat(BaseSiteModel baseSiteModel)
    {
        ServicesUtil.validateParameterNotNull(baseSiteModel, "Base site must not be null");
        return getConfiguration(baseSiteModel)
                        .<Integer>map(CxConfigModel::getActionResultMaxRepeat)
                        .orElseGet(this::getDefaultActionResultMaxRepeat);
    }


    public BigDecimal getMinAffinity()
    {
        return getCurrentBaseSite()
                        .<BigDecimal>map(this::getMinAffinity)
                        .orElseGet(this::getDefaultMinAffinity);
    }


    public BigDecimal getMinAffinity(BaseSiteModel baseSiteModel)
    {
        ServicesUtil.validateParameterNotNull(baseSiteModel, "Base site must not be null");
        return getConfiguration(baseSiteModel)
                        .<BigDecimal>map(CxConfigModel::getMinAffinity)
                        .orElseGet(this::getDefaultMinAffinity);
    }


    public Set<String> getUserChangedActions()
    {
        return getCurrentBaseSite()
                        .<Set<String>>map(this::getUserChangedActions)
                        .orElseGet(this::getDefaultUserChangedActions);
    }


    public Set<String> getUserChangedActions(BaseSiteModel baseSiteModel)
    {
        ServicesUtil.validateParameterNotNull(baseSiteModel, "Base site must not be null");
        return getConfiguration(baseSiteModel)
                        .<Set<String>>map(CxConfigModel::getUserChangedActions)
                        .orElseGet(this::getDefaultUserChangedActions);
    }


    public Set<String> getConsentGivenActions()
    {
        return getCurrentBaseSite()
                        .<Set<String>>map(this::getConsentGivenActions)
                        .orElseGet(this::getDefaultConsentGivenActions);
    }


    public Set<String> getConsentGivenActions(BaseSiteModel baseSiteModel)
    {
        ServicesUtil.validateParameterNotNull(baseSiteModel, "Base site must not be null");
        return getConfiguration(baseSiteModel)
                        .<Set<String>>map(CxConfigModel::getConsentGivenActions)
                        .orElseGet(this::getDefaultConsentGivenActions);
    }


    public List<CxUrlVoterConfigModel> getUrlVoterConfigurations()
    {
        return getCurrentBaseSite()
                        .<List<CxUrlVoterConfigModel>>map(this::getUrlVoterConfigurations)
                        .orElseGet(Collections::emptyList);
    }


    public List<CxUrlVoterConfigModel> getUrlVoterConfigurations(BaseSiteModel baseSiteModel)
    {
        ServicesUtil.validateParameterNotNull(baseSiteModel, "Base site must not be null");
        return getConfiguration(baseSiteModel)
                        .<List<CxUrlVoterConfigModel>>map(CxConfigModel::getUrlVoterConfigs)
                        .orElseGet(Collections::emptyList);
    }


    public String getCalculationProcessName()
    {
        return getCurrentBaseSite()
                        .<String>map(this::getCalculationProcessName)
                        .orElseGet(this::getDefaultCalculationProcessName);
    }


    public String getCalculationProcessName(BaseSiteModel baseSiteModel)
    {
        ServicesUtil.validateParameterNotNull(baseSiteModel, "Base site must not be null");
        return getConfiguration(baseSiteModel)
                        .<String>map(CxConfigModel::getCalculationProcess)
                        .orElseGet(this::getDefaultCalculationProcessName);
    }


    public Set<ConsentTemplateModel> getConsentTemplates()
    {
        return getCurrentBaseSite()
                        .<Set<ConsentTemplateModel>>map(this::getConsentTemplates)
                        .orElseGet(Collections::emptySet);
    }


    public Set<ConsentTemplateModel> getConsentTemplates(BaseSiteModel baseSiteModel)
    {
        ServicesUtil.validateParameterNotNull(baseSiteModel, "Base site must not be null");
        return getConfiguration(baseSiteModel)
                        .<Set<ConsentTemplateModel>>map(CxConfigModel::getConsentTemplates)
                        .orElseGet(Collections::emptySet);
    }


    public Set<RecalculateAction> getDefaultActionsForAnonymous()
    {
        return getCurrentBaseSite()
                        .<Set<RecalculateAction>>map(this::getDefaultActionsForAnonymous)
                        .orElseGet(this::defaultActionsForAnonymous);
    }


    public Set<RecalculateAction> getDefaultActionsForAnonymous(BaseSiteModel baseSiteModel)
    {
        return getConfiguration(baseSiteModel)
                        .map(CxConfigModel::getAnonymousUserDefaultActions)
                        .map(this::convertActions)
                        .orElseGet(this::defaultActionsForAnonymous);
    }


    public CxCatalogLookupType getCatalogLookupType()
    {
        return getCurrentBaseSite()
                        .<CxCatalogLookupType>map(this::getCatalogLookupType)
                        .orElseGet(this::catalogLookupType);
    }


    public CxCatalogLookupType getCatalogLookupType(BaseSiteModel baseSiteModel)
    {
        return getConfiguration(baseSiteModel)
                        .<CxCatalogLookupType>map(CxConfigModel::getCatalogLookup)
                        .orElseGet(this::catalogLookupType);
    }


    public Boolean isUserSegmentsStoreInSession()
    {
        return getCurrentBaseSite()
                        .<Boolean>map(this::isUserSegmentsStoreInSession)
                        .orElseGet(this::userSegmentsStoreInSession);
    }


    public Boolean isUserSegmentsStoreInSession(BaseSiteModel baseSite)
    {
        return getConfiguration(baseSite)
                        .<Boolean>map(CxConfigModel::getUserSegmentsStoreInSession)
                        .orElseGet(this::userSegmentsStoreInSession);
    }


    public Set<CxPeriodicVoterConfigModel> getPeriodicVoterConfigurations()
    {
        return getCurrentBaseSite()
                        .<Set<CxPeriodicVoterConfigModel>>map(this::getPeriodicVoterConfigurations)
                        .orElseGet(Collections::emptySet);
    }


    public Set<CxPeriodicVoterConfigModel> getPeriodicVoterConfigurations(BaseSiteModel baseSiteModel)
    {
        ServicesUtil.validateParameterNotNull(baseSiteModel, "Base site must not be null");
        return getConfiguration(baseSiteModel)
                        .<Set<CxPeriodicVoterConfigModel>>map(CxConfigModel::getPeriodicVoterConfigs)
                        .orElseGet(Collections::emptySet);
    }


    protected String getDefaultCalculationProcessName()
    {
        return this.configurationService.getConfiguration().getString("personalizationservices.calculation.process");
    }


    protected Integer getDefaultActionResultMaxRepeat()
    {
        return this.configurationService.getConfiguration().getInteger("personalizationservices.actionResult.load.repeat.max",
                        Integer.valueOf(1));
    }


    protected BigDecimal getDefaultMinAffinity()
    {
        return this.configurationService.getConfiguration().getBigDecimal("personalizationservices.segment.trigger.strategy.min.affinity", this.defaultMinAffinity);
    }


    protected Set<String> getDefaultUserChangedActions()
    {
        String actions = this.configurationService.getConfiguration().getString("personalizationservices.after.user.changed.actions");
        return StringUtils.isNotBlank(actions) ? Sets.newHashSet((Object[])actions.split(",")) : Sets.newHashSet();
    }


    protected Set<String> getDefaultConsentGivenActions()
    {
        String actions = this.configurationService.getConfiguration().getString("personalizationservices.after.consents.given.actions");
        return StringUtils.isNotBlank(actions) ? Sets.newHashSet((Object[])actions.split(",")) : Sets.newHashSet();
    }


    protected Optional<BaseSiteModel> getCurrentBaseSite()
    {
        return Optional.ofNullable(this.baseSiteService.getCurrentBaseSite());
    }


    protected Set<RecalculateAction> defaultActionsForAnonymous()
    {
        String actions = this.configurationService.getConfiguration().getString("personalizationservices.calculate.anonymousUserActions");
        Set<String> actionSet = StringUtils.isNotBlank(actions) ? Sets.newHashSet((Object[])actions.split(",")) : Sets.newHashSet();
        return convertActions(actionSet);
    }


    protected CxCatalogLookupType catalogLookupType()
    {
        String type = this.configurationService.getConfiguration().getString("personalizationservices.calculate.catalogLookup", CxCatalogLookupType.ALL_CATALOGS.getCode());
        return CxCatalogLookupType.valueOf(type);
    }


    protected Set<RecalculateAction> convertActions(Set<String> actions)
    {
        return (Set<RecalculateAction>)actions.stream()
                        .filter(this::actionExist)
                        .map(RecalculateAction::valueOf)
                        .collect(Collectors.toSet());
    }


    protected boolean actionExist(String actionName)
    {
        try
        {
            RecalculateAction.valueOf(actionName);
        }
        catch(IllegalArgumentException e)
        {
            LOG.warn("Recalculate action doesn't exist :" + actionName, e);
            return false;
        }
        return true;
    }


    protected Boolean userSegmentsStoreInSession()
    {
        return this.configurationService.getConfiguration()
                        .getBoolean("personalizationservices.user.segments.store.in.session", Boolean.FALSE);
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected CxConfigurationLookupStrategy getCxConfigurationLookupStrategy()
    {
        return this.cxConfigurationLookupStrategy;
    }


    @Required
    public void setCxConfigurationLookupStrategy(CxConfigurationLookupStrategy cxConfigurationLookupStrategy)
    {
        this.cxConfigurationLookupStrategy = cxConfigurationLookupStrategy;
    }
}
