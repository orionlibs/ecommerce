package de.hybris.platform.personalizationservices.setup;

import com.google.common.collect.Sets;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.personalizationservices.dao.CxDaoQueryBuilder;
import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import de.hybris.platform.personalizationservices.enums.CxUserType;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxResultsModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.model.config.CxPeriodicVoterConfigModel;
import de.hybris.platform.personalizationservices.model.config.CxUrlVoterConfigModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.persistence.PersistenceUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@SystemSetup(extension = "personalizationservices")
public class PersonalizationServicesSystemSetup
{
    private static final Logger LOG = LoggerFactory.getLogger(PersonalizationServicesSystemSetup.class);
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;
    private BaseSiteService baseSiteService;
    private CxDaoQueryBuilder cxDaoQueryBuilder;
    private static final int PAGE_SIZE = 100;
    private static final int COUNTER_STEP = 1000;


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void updateData()
    {
        List<CxVariationModel> variationsForUpdate = getVariationsForUpdate();
        variationsForUpdate.forEach(this::updateVariation);
        this.modelService.saveAll(variationsForUpdate);
        List<CxCustomizationModel> customizations = getCustomizationsForStatusUpdate();
        customizations.forEach(this::updateCustomizationStatus);
        this.modelService.saveAll(customizations);
        customizations = getCustomizationsForDescriptionUpdate();
        customizations.forEach(this::updateCustomizationDescription);
        this.modelService.saveAll(customizations);
    }


    protected List<CxVariationModel> getVariationsForUpdate()
    {
        String query = "SELECT {pk}  FROM {CxVariation}  WHERE {status} IS NULL";
        SearchResult<CxVariationModel> searchResult = this.flexibleSearchService.search("SELECT {pk}  FROM {CxVariation}  WHERE {status} IS NULL");
        return searchResult.getResult();
    }


    protected List<CxCustomizationModel> getCustomizationsForStatusUpdate()
    {
        String query = "SELECT {pk}  FROM {CxCustomization}  WHERE {status} IS NULL";
        SearchResult<CxCustomizationModel> searchResult = this.flexibleSearchService.search("SELECT {pk}  FROM {CxCustomization}  WHERE {status} IS NULL");
        return searchResult.getResult();
    }


    protected List<CxCustomizationModel> getCustomizationsForDescriptionUpdate()
    {
        String query = "SELECT {pk}  FROM {CxCustomization}  WHERE {description} IS NOT NULL";
        SearchResult<CxCustomizationModel> searchResult = this.flexibleSearchService.search("SELECT {pk}  FROM {CxCustomization}  WHERE {description} IS NOT NULL");
        return searchResult.getResult();
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void updateActionsRank()
    {
        String query = "SELECT DISTINCT {variation} FROM {CxAbstractAction} WHERE {variationPOS} IS NULL";
        SearchResult<CxVariationModel> searchResult = this.flexibleSearchService.search("SELECT DISTINCT {variation} FROM {CxAbstractAction} WHERE {variationPOS} IS NULL");
        List<CxVariationModel> variations = searchResult.getResult();
        for(CxVariationModel variation : variations)
        {
            List<CxAbstractActionModel> actions = new ArrayList<>(variation.getActions());
            int index = 0;
            for(CxAbstractActionModel action : actions)
            {
                action.setProperty("variationPOS", Integer.valueOf(index));
                index++;
            }
            this.modelService.saveAll(actions);
        }
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void removeCxResults()
    {
        PersistenceUtils.doWithSLDPersistence(() -> {
            int totalToRem;
            int removeLogPageCounter = 0;
            PaginationData pagination = new PaginationData();
            pagination.setCurrentPage(0);
            pagination.setPageSize(100);
            do
            {
                SearchResult<CxResultsModel> sResult = getResultsForDelete(pagination);
                totalToRem = sResult.getTotalCount();
                if(removeLogPageCounter == 0)
                {
                    LOG.debug("Removing personalization results - {} left to remove", Integer.valueOf(totalToRem));
                    removeLogPageCounter = 1000;
                }
                removeLogPageCounter--;
                List<CxResultsModel> cxResults = (sResult.getResult() == null) ? Collections.<CxResultsModel>emptyList() : sResult.getResult();
                this.modelService.removeAll(cxResults);
                Objects.requireNonNull(this.modelService);
                cxResults.forEach(this.modelService::detach);
            }
            while(totalToRem >= 100);
            LOG.info("Removed all personalization results");
            return null;
        });
    }


    protected SearchResult<CxResultsModel> getResultsForDelete(PaginationData pagination)
    {
        String queryStr = "SELECT {pk}  FROM {CxResults}  WHERE ({key} IS NULL  AND {sessionKey} IS NULL  AND {anonymous} IS NULL )  OR {additionalData} IS NULL";
        FlexibleSearchQuery query = this.cxDaoQueryBuilder.buildQuery("SELECT {pk}  FROM {CxResults}  WHERE ({key} IS NULL  AND {sessionKey} IS NULL  AND {anonymous} IS NULL )  OR {additionalData} IS NULL", Collections.emptyMap(), pagination);
        return this.flexibleSearchService.search(query);
    }


    protected void updateVariation(CxVariationModel variation)
    {
        variation.setStatus(getStatus(variation.isEnabled()));
    }


    protected CxItemStatus getStatus(boolean enabled)
    {
        return enabled ? CxItemStatus.ENABLED : CxItemStatus.DISABLED;
    }


    protected void updateCustomizationStatus(CxCustomizationModel customization)
    {
        customization.setStatus(CxItemStatus.ENABLED);
    }


    protected void updateCustomizationDescription(CxCustomizationModel customization)
    {
        customization.setLongDescription(customization.getDescription());
        customization.setDescription(null);
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void updateUserToSegment()
    {
        List<BaseSiteModel> baseSites = new ArrayList<>(this.baseSiteService.getAllBaseSites());
        if(baseSites.isEmpty())
        {
            return;
        }
        PersistenceUtils.doWithSLDPersistence(() -> {
            updateUserToSegment(baseSites);
            return null;
        });
    }


    protected void updateUserToSegment(List<BaseSiteModel> baseSites)
    {
        int start = 0;
        boolean isMore = true;
        do
        {
            SearchResult<CxUserToSegmentModel> searchResults = getUserSegmentsForUpdate(start);
            isMore = (searchResults.getCount() == 100);
            start += 100;
            List<CxUserToSegmentModel> results = searchResults.getResult();
            results.forEach(us -> updateUserToSegment(us, baseSites.get(0)));
            this.modelService.saveAll(results);
            for(BaseSiteModel baseSite : baseSites.subList(1, baseSites.size()))
            {
                List<CxUserToSegmentModel> userSegmentsForBaseSite = (List<CxUserToSegmentModel>)results.stream().map(us -> copyUserToSegment(us, baseSite)).collect(Collectors.toList());
                this.modelService.saveAll(userSegmentsForBaseSite);
            }
        }
        while(isMore);
    }


    protected SearchResult<CxUserToSegmentModel> getUserSegmentsForUpdate(int start)
    {
        String query = "SELECT {pk}  FROM {CxUserToSegment}  WHERE {baseSite} IS NULL ";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk}  FROM {CxUserToSegment}  WHERE {baseSite} IS NULL ");
        fQuery.setCount(100);
        fQuery.setStart(start);
        return this.flexibleSearchService.search(fQuery);
    }


    protected void updateUserToSegment(CxUserToSegmentModel model, BaseSiteModel baseSite)
    {
        model.setBaseSite(baseSite);
    }


    protected CxUserToSegmentModel copyUserToSegment(CxUserToSegmentModel us, BaseSiteModel baseSite)
    {
        CxUserToSegmentModel copy = (CxUserToSegmentModel)this.modelService.clone(us);
        copy.setBaseSite(baseSite);
        return copy;
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void updateCalculationConfig()
    {
        List<CxConfigModel> configList = getCxConfigs().getResult();
        for(CxConfigModel config : configList)
        {
            updateUrlVoterConfigs(config);
            mapAnonymousToPeriodicConfig(config);
            this.modelService.saveAll(new Object[] {config});
        }
    }


    protected SearchResult<CxConfigModel> getCxConfigs()
    {
        String query = "SELECT {pk}  FROM {CxConfig} ";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk}  FROM {CxConfig} ");
        return this.flexibleSearchService.search(fQuery);
    }


    protected void updateUrlVoterConfigs(CxConfigModel config)
    {
        CxUserType userType = shouldIgnoreForAnonymous(config) ? CxUserType.REGISTERED : CxUserType.ALL;
        config.getUrlVoterConfigs().stream()
                        .filter(v -> (v.getUserType() == null))
                        .forEach(v -> setUserType(v, userType));
    }


    private void setUserType(CxUrlVoterConfigModel config, CxUserType userType)
    {
        config.setUserType(userType);
        this.modelService.save(config);
    }


    private static boolean shouldIgnoreForAnonymous(CxConfigModel config)
    {
        boolean anonymousUserIgnoreOtherActions = Boolean.TRUE.equals(config.getAnonymousUserIgnoreOtherActions());
        boolean ignoreRecalcForAnonymous = config.getIgnoreRecalcForAnonymous().booleanValue();
        return (anonymousUserIgnoreOtherActions || ignoreRecalcForAnonymous);
    }


    protected void mapAnonymousToPeriodicConfig(CxConfigModel config)
    {
        if(shouldDefinePeriodicConfig(config))
        {
            CxPeriodicVoterConfigModel periodicVoterConfig = (CxPeriodicVoterConfigModel)this.modelService.create(CxPeriodicVoterConfigModel.class);
            periodicVoterConfig.setCxConfig(config);
            periodicVoterConfig.setCode("anonymousPeriodicVoter");
            periodicVoterConfig.setActions(config.getAnonymousUserActions());
            periodicVoterConfig.setUserMinRequestNumber(config.getAnonymousUserMinRequestNumber());
            periodicVoterConfig.setUserMinTime(config.getAnonymousUserMinTime());
            periodicVoterConfig.setUserType(CxUserType.ANONYMOUS);
            config.setPeriodicVoterConfigs(Sets.newHashSet((Object[])new CxPeriodicVoterConfigModel[] {periodicVoterConfig}));
            config.setAnonymousUserActions(null);
            config.setAnonymousUserMinRequestNumber(null);
            config.setAnonymousUserMinTime(null);
        }
    }


    private static boolean shouldDefinePeriodicConfig(CxConfigModel config)
    {
        boolean isMinRequestNumberDefined = (config.getAnonymousUserMinRequestNumber() != null && config.getAnonymousUserMinRequestNumber().intValue() >= 0);
        boolean isMinTimeDefined = (config.getAnonymousUserMinTime() != null && config.getAnonymousUserMinTime().longValue() >= 0L);
        boolean isAnonymousConfigurationDefined = (!Boolean.TRUE.equals(config.getIgnoreRecalcForAnonymous()) && CollectionUtils.isNotEmpty(config.getAnonymousUserActions()) && isMinRequestNumberDefined && isMinTimeDefined);
        return (CollectionUtils.isEmpty(config.getPeriodicVoterConfigs()) && isAnonymousConfigurationDefined);
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setCxDaoQueryBuilder(CxDaoQueryBuilder cxDaoQueryBuilder)
    {
        this.cxDaoQueryBuilder = cxDaoQueryBuilder;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected CxDaoQueryBuilder getCxDaoQueryBuilder()
    {
        return this.cxDaoQueryBuilder;
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
}
