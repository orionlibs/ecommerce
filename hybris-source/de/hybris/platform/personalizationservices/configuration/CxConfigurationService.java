package de.hybris.platform.personalizationservices.configuration;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.model.consent.ConsentTemplateModel;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.enums.CxCatalogLookupType;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.model.config.CxPeriodicVoterConfigModel;
import de.hybris.platform.personalizationservices.model.config.CxUrlVoterConfigModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public interface CxConfigurationService
{
    Optional<CxConfigModel> getConfiguration();


    Optional<CxConfigModel> getConfiguration(BaseSiteModel paramBaseSiteModel);


    <T> T getValue(CatalogVersionModel paramCatalogVersionModel, Function<CxConfigModel, T> paramFunction, T paramT);


    Integer getActionResultMaxRepeat();


    Integer getActionResultMaxRepeat(BaseSiteModel paramBaseSiteModel);


    BigDecimal getMinAffinity();


    BigDecimal getMinAffinity(BaseSiteModel paramBaseSiteModel);


    Set<String> getUserChangedActions();


    Set<String> getUserChangedActions(BaseSiteModel paramBaseSiteModel);


    Set<String> getConsentGivenActions();


    Set<String> getConsentGivenActions(BaseSiteModel paramBaseSiteModel);


    List<CxUrlVoterConfigModel> getUrlVoterConfigurations();


    List<CxUrlVoterConfigModel> getUrlVoterConfigurations(BaseSiteModel paramBaseSiteModel);


    String getCalculationProcessName();


    String getCalculationProcessName(BaseSiteModel paramBaseSiteModel);


    Set<ConsentTemplateModel> getConsentTemplates();


    Set<ConsentTemplateModel> getConsentTemplates(BaseSiteModel paramBaseSiteModel);


    Set<RecalculateAction> getDefaultActionsForAnonymous();


    Set<RecalculateAction> getDefaultActionsForAnonymous(BaseSiteModel paramBaseSiteModel);


    CxCatalogLookupType getCatalogLookupType();


    CxCatalogLookupType getCatalogLookupType(BaseSiteModel paramBaseSiteModel);


    Boolean isUserSegmentsStoreInSession();


    Boolean isUserSegmentsStoreInSession(BaseSiteModel paramBaseSiteModel);


    Set<CxPeriodicVoterConfigModel> getPeriodicVoterConfigurations();


    Set<CxPeriodicVoterConfigModel> getPeriodicVoterConfigurations(BaseSiteModel paramBaseSiteModel);
}
