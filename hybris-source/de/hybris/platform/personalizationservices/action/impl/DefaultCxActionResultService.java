package de.hybris.platform.personalizationservices.action.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.personalizationservices.action.CxActionResultService;
import de.hybris.platform.personalizationservices.action.dao.CxActionResultDao;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;
import de.hybris.platform.personalizationservices.data.CxResultsData;
import de.hybris.platform.personalizationservices.data.UserToSegmentData;
import de.hybris.platform.personalizationservices.model.CxResultsModel;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentSessionService;
import de.hybris.platform.servicelayer.event.SerializationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxActionResultService implements CxActionResultService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxActionResultService.class);
    public static final String CX_ACTION_RESULTS = "CxActionResults";
    public static final String CX_ACTION_RESULTS_TIME = "CxActionResultsTime";
    public static final String CX_DEFAULT_ACTION_RESULTS = "CxDefaultActionResults";
    private SessionService sessionService;
    private CxConfigurationService cxConfigurationService;
    private SerializationService serializationService;
    private ModelService modelService;
    private TimeService timeService;
    private UserService userService;
    private DefaultSessionTokenService defaultSessionTokenService;
    private CxActionResultDao actionResultDao;
    private CxUserSegmentSessionService cxUserSegmentSessionService;


    public void setActionResultsInSession(UserModel user, CatalogVersionModel catalogVersion, List<CxAbstractActionResult> actionResults)
    {
        List<CxAbstractActionResult> serializableActionResults = (actionResults instanceof java.io.Serializable) ? actionResults : new ArrayList<>(actionResults);
        this.sessionService.setAttribute(getResultsKey(user, catalogVersion), serializableActionResults);
    }


    public void loadActionResultsInSession(UserModel user, Collection<CatalogVersionModel> catalogVersions)
    {
        int i = 0;
        while(true)
        {
            try
            {
                loadActionResultsInSessionInternal(user, catalogVersions);
                break;
            }
            catch(JaloObjectNoLongerValidException e)
            {
                if(i++ >= this.cxConfigurationService.getActionResultMaxRepeat().intValue())
                {
                    throw e;
                }
                LOG.debug("Experience read from database failed (" + i + ")", (Throwable)e);
            }
        }
    }


    protected void loadActionResultsInSessionInternal(UserModel user, Collection<CatalogVersionModel> catalogVersions)
    {
        catalogVersions.stream()
                        .map(cv -> findResults(user, cv))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(r -> loadActionResultsInSession(user, r));
    }


    protected Optional<CxResultsModel> findResults(UserModel user, CatalogVersionModel catalogVersion)
    {
        Optional<CxResultsModel> results = this.actionResultDao.findResultsByKey(getResultsKey(user, catalogVersion));
        if(results.isPresent())
        {
            return results;
        }
        return this.actionResultDao.findResultsByKey(getDefaultResultsKey(catalogVersion));
    }


    protected void loadActionResultsInSession(UserModel user, CxResultsModel r)
    {
        Date sessionTime = (Date)this.sessionService.getAttribute(getResultsTimeKey(user, r.getCatalogVersion()));
        if(sessionTime != null && !r.getCalculationTime().after(sessionTime))
        {
            return;
        }
        List<CxAbstractActionResult> actionResults = (List<CxAbstractActionResult>)this.serializationService.deserialize((byte[])r.getResults());
        setActionResultsInSession(user, r.getCatalogVersion(), actionResults);
        loadSegmentsIntoSession(user, r);
        this.sessionService.setAttribute(getResultsTimeKey(user, r.getCatalogVersion()), r.getCalculationTime());
    }


    protected void loadSegmentsIntoSession(UserModel user, CxResultsModel r)
    {
        if(this.cxUserSegmentSessionService.isUserSegmentStoredInSession(user) && r.getAdditionalData() != null)
        {
            CxResultsData resultsData = (CxResultsData)this.serializationService.deserialize((byte[])r.getAdditionalData());
            List<UserToSegmentData> segments = resultsData.getSegments();
            this.cxUserSegmentSessionService.setUserSegmentsInSession(user, segments);
        }
    }


    public void storeDefaultActionResults(UserModel user, CatalogVersionModel cv, List<CxAbstractActionResult> actionResults)
    {
        createActionResults(getDefaultResultsKey(cv), user, cv, actionResults)
                        .ifPresent(this::saveDefaultResult);
    }


    protected void saveDefaultResult(CxResultsModel results)
    {
        results.setDefault(true);
        this.modelService.save(results);
    }


    public void storeActionResults(UserModel user, CatalogVersionModel cv, List<CxAbstractActionResult> actionResults)
    {
        Objects.requireNonNull(this.modelService);
        createActionResults(getResultsKey(user, cv), user, cv, actionResults).ifPresent(this.modelService::save);
    }


    protected Optional<CxResultsModel> createActionResults(String resultsKey, UserModel user, CatalogVersionModel cv, List<CxAbstractActionResult> actionResults)
    {
        CxResultsModel cxResults = getSingleResult(resultsKey).orElseGet(CxResultsModel::new);
        cxResults.setCatalogVersion(cv);
        List<CxAbstractActionResult> serializableActionResults = (actionResults instanceof java.io.Serializable) ? actionResults : new ArrayList<>(actionResults);
        cxResults.setResults(this.serializationService.serialize(serializableActionResults));
        CxResultsData data = new CxResultsData();
        data.setSegments(new ArrayList(this.cxUserSegmentSessionService.getUserSegmentsFromSession(user)));
        cxResults.setAdditionalData(this.serializationService.serialize(data));
        cxResults.setCalculationTime(this.timeService.getCurrentTime());
        cxResults.setKey(resultsKey);
        cxResults.setSessionKey(this.defaultSessionTokenService.getOrCreateSessionToken());
        cxResults.setAnonymous(this.userService.isAnonymousUser(user));
        if(!cxResults.isAnonymous())
        {
            cxResults.setUser(user);
        }
        return Optional.of(cxResults);
    }


    protected Optional<CxResultsModel> getSingleResult(String resultsKey)
    {
        List<CxResultsModel> resultsList = this.actionResultDao.findAllResultsByKey(resultsKey);
        if(CollectionUtils.isNotEmpty(resultsList))
        {
            CxResultsModel result = resultsList.get(0);
            try
            {
                resultsList = (List<CxResultsModel>)resultsList.stream().skip(1L).filter(Objects::nonNull).collect(Collectors.toList());
                this.modelService.removeAll(resultsList);
            }
            catch(JaloObjectNoLongerValidException e)
            {
                LOG.debug("Cant remove non existing object - ignoring", (Throwable)e);
            }
            return Optional.ofNullable(result);
        }
        return Optional.empty();
    }


    public Optional<CxResultsModel> getCxResults(UserModel user, CatalogVersionModel cv)
    {
        return this.actionResultDao.findResultsByKey(getResultsKey(user, cv));
    }


    public void clearActionResultsInSession(UserModel user, CatalogVersionModel catalogVersion)
    {
        this.sessionService.removeAttribute(getResultsKey(user, catalogVersion));
    }


    public List<CxAbstractActionResult> getActionResults(UserModel user, CatalogVersionModel catalogVersion)
    {
        List<CxAbstractActionResult> result = (List<CxAbstractActionResult>)this.sessionService.getAttribute(getResultsKey(user, catalogVersion));
        return (result == null) ? Collections.<CxAbstractActionResult>emptyList() : result;
    }


    protected String getResultsKey(UserModel user, CatalogVersionModel catalogVersion)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CxActionResults").append('_');
        if(this.userService.isAnonymousUser(user))
        {
            sb.append(this.defaultSessionTokenService.getOrCreateSessionToken());
        }
        else
        {
            sb.append(user.getPk());
        }
        sb.append('_').append(catalogVersion.getPk());
        return sb.toString();
    }


    protected String getResultsTimeKey(UserModel user, CatalogVersionModel catalogVersion)
    {
        return "CxActionResultsTime_" + user.getPk() + "_" + catalogVersion.getPk();
    }


    protected String getDefaultResultsKey(CatalogVersionModel catalogVersion)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CxDefaultActionResults").append('_').append(catalogVersion.getPk());
        return sb.toString();
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    protected SerializationService getSerializationService()
    {
        return this.serializationService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected DefaultSessionTokenService getDefaultSessionTokenService()
    {
        return this.defaultSessionTokenService;
    }


    protected CxActionResultDao getActionResultDao()
    {
        return this.actionResultDao;
    }


    protected CxUserSegmentSessionService getCxUserSegmentSessionService()
    {
        return this.cxUserSegmentSessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setSerializationService(SerializationService serializationService)
    {
        this.serializationService = serializationService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setDefaultSessionTokenService(DefaultSessionTokenService defaultSessionTokenService)
    {
        this.defaultSessionTokenService = defaultSessionTokenService;
    }


    @Required
    public void setActionResultDao(CxActionResultDao actionResultDao)
    {
        this.actionResultDao = actionResultDao;
    }


    @Required
    public void setCxUserSegmentSessionService(CxUserSegmentSessionService cxUserSegmentSessionService)
    {
        this.cxUserSegmentSessionService = cxUserSegmentSessionService;
    }
}
