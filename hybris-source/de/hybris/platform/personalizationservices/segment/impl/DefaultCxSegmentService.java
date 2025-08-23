package de.hybris.platform.personalizationservices.segment.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.CxUpdateSegmentContext;
import de.hybris.platform.personalizationservices.consent.CxConsentService;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentSessionService;
import de.hybris.platform.personalizationservices.segment.converters.CxUserSegmentConversionHelper;
import de.hybris.platform.personalizationservices.segment.dao.CxSegmentDao;
import de.hybris.platform.personalizationservices.segment.dao.CxUserToSegmentDao;
import de.hybris.platform.personalizationservices.strategies.UpdateSegmentStrategy;
import de.hybris.platform.personalizationservices.strategies.UpdateUserSegmentStrategy;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxSegmentService implements CxSegmentService
{
    private static final String USER_NOT_NULL = "user must not be null";
    private CxConsentService cxConsentService;
    private BaseSiteService baseSiteService;
    private CxUserSegmentService cxUserSegmentService;
    private CxUserSegmentSessionService cxUserSegmentSessionService;
    private CxUserSegmentConversionHelper cxUserSegmentConversionHelper;
    private CxSegmentDao cxSegmentDao;
    private CxUserToSegmentDao cxUserToSegmentDao;
    private UpdateUserSegmentStrategy updateUserSegmentStrategy;
    private UpdateSegmentStrategy updateSegmentStrategy;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxSegmentService.class);


    public Optional<CxSegmentModel> getSegment(String code)
    {
        return this.cxSegmentDao.findSegmentByCode(code);
    }


    public SearchPageData<CxSegmentModel> getSegments(Map<String, String> filters, SearchPageData<?> pagination)
    {
        ServicesUtil.validateParameterNotNull(filters, "filters must not be null");
        ServicesUtil.validateParameterNotNull(pagination, "pagination must not be null");
        return this.cxSegmentDao.findSegments(filters, pagination);
    }


    public Collection<CxSegmentModel> getSegmentsForCodes(Collection<String> codes)
    {
        try
        {
            return this.cxSegmentDao.findSegmentsByCodes(codes);
        }
        catch(FlexibleSearchException e)
        {
            LOG.warn("Problem when fetching segments for codes: {}", codes, e);
            return Collections.emptyList();
        }
    }


    public Collection<UserModel> getUsersFromSegment(CxSegmentModel segment)
    {
        ServicesUtil.validateParameterNotNull(segment, "segment must not be null");
        return (Collection<UserModel>)segment.getUserToSegments().stream().map(CxUserToSegmentModel::getUser).collect(Collectors.toList());
    }


    public Collection<CxSegmentModel> getSegmentsFromUser(UserModel user)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        return (Collection<CxSegmentModel>)getUserToSegmentForUser(user).stream().map(CxUserToSegmentModel::getSegment).collect(Collectors.toList());
    }


    public Collection<CxUserToSegmentModel> getUserToSegmentForCalculation(UserModel user)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        if(this.cxConsentService.userHasActiveConsent(user))
        {
            return getUserToSegmentForCurrentBaseSite(user);
        }
        return Collections.emptyList();
    }


    public Collection<CxUserToSegmentModel> getUserToSegmentForUser(UserModel user)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        if(this.cxUserSegmentSessionService.isUserSegmentStoredInSession(user))
        {
            return this.cxUserSegmentConversionHelper.convertToModel(user, this.cxUserSegmentSessionService.getUserSegmentsFromSession(user));
        }
        return this.cxUserSegmentService.getUserSegments(user);
    }


    protected Collection<CxUserToSegmentModel> getUserToSegmentForCurrentBaseSite(UserModel user)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        if(this.cxUserSegmentSessionService.isUserSegmentStoredInSession(user))
        {
            return this.cxUserSegmentConversionHelper.convertToModel(user, this.cxUserSegmentSessionService.getUserSegmentsFromSession(user));
        }
        return this.cxUserSegmentService.getUserSegments(user, this.baseSiteService.getCurrentBaseSite());
    }


    public void saveUserToSegments(Collection<CxUserToSegmentModel> userToSegments)
    {
        ServicesUtil.validateParameterNotNull(userToSegments, "Parameter 'userToSegments' must not be null!");
        if(userToSegments.isEmpty())
        {
            return;
        }
        Map<UserModel, List<CxUserToSegmentModel>> userToSegmentMap = (Map<UserModel, List<CxUserToSegmentModel>>)userToSegments.stream().collect(Collectors.groupingBy(CxUserToSegmentModel::getUser));
        userToSegmentMap.forEach(this::addUserToSegments);
    }


    protected void addUserToSegments(UserModel user, Collection<CxUserToSegmentModel> userToSegments)
    {
        if(this.cxUserSegmentSessionService.isUserSegmentStoredInSession(user))
        {
            this.cxUserSegmentSessionService.addUserSegmentsInSession(user, this.cxUserSegmentConversionHelper.convertToData(userToSegments));
        }
        else
        {
            this.cxUserSegmentService.addUserSegments(user, userToSegments);
        }
    }


    public void removeUserToSegments(Collection<CxUserToSegmentModel> userToSegments)
    {
        ServicesUtil.validateParameterNotNull(userToSegments, "Parameter 'userToSegments' must not be null!");
        if(userToSegments.isEmpty())
        {
            return;
        }
        Map<UserModel, List<CxUserToSegmentModel>> userToSegmentMap = (Map<UserModel, List<CxUserToSegmentModel>>)userToSegments.stream().collect(Collectors.groupingBy(CxUserToSegmentModel::getUser));
        userToSegmentMap.forEach(this::removeUserToSegments);
    }


    protected void removeUserToSegments(UserModel user, Collection<CxUserToSegmentModel> userToSegments)
    {
        if(this.cxUserSegmentSessionService.isUserSegmentStoredInSession(user))
        {
            this.cxUserSegmentSessionService.removeUserSegmentsFromSession(user, this.cxUserSegmentConversionHelper
                            .convertToData(userToSegments));
        }
        else
        {
            this.cxUserSegmentService.removeUserSegments(user, userToSegments);
        }
    }


    public SearchPageData<CxUserToSegmentModel> getUserToSegmentModel(UserModel user, CxSegmentModel segment, BaseSiteModel baseSite, SearchPageData<?> pagination)
    {
        ServicesUtil.validateParameterNotNull(pagination, "pagination must not be null");
        return this.cxUserToSegmentDao.findUserToSegmentRelations(user, segment, baseSite, pagination);
    }


    public void updateUserSegments(UserModel user)
    {
        this.updateUserSegmentStrategy.updateUserSegments(user);
    }


    public void updateUserSegments(UserModel user, CxCalculationContext context)
    {
        this.updateUserSegmentStrategy.updateUserSegments(user, context);
    }


    public void updateSegments(CxUpdateSegmentContext context)
    {
        this.updateSegmentStrategy.updateSegments(context);
    }


    protected CxConsentService getCxConsentService()
    {
        return this.cxConsentService;
    }


    @Required
    public void setCxConsentService(CxConsentService cxConsentService)
    {
        this.cxConsentService = cxConsentService;
    }


    protected CxSegmentDao getCxSegmentDao()
    {
        return this.cxSegmentDao;
    }


    @Required
    public void setCxSegmentDao(CxSegmentDao cxSegmentDao)
    {
        this.cxSegmentDao = cxSegmentDao;
    }


    protected CxUserToSegmentDao getCxUserToSegmentDao()
    {
        return this.cxUserToSegmentDao;
    }


    @Required
    public void setCxUserToSegmentDao(CxUserToSegmentDao cxUserToSegmentDao)
    {
        this.cxUserToSegmentDao = cxUserToSegmentDao;
    }


    protected UpdateUserSegmentStrategy getUpdateUserSegmentStrategy()
    {
        return this.updateUserSegmentStrategy;
    }


    @Required
    public void setUpdateUserSegmentStrategy(UpdateUserSegmentStrategy updateUserSegmentStrategy)
    {
        this.updateUserSegmentStrategy = updateUserSegmentStrategy;
    }


    public UpdateSegmentStrategy getUpdateSegmentStrategy()
    {
        return this.updateSegmentStrategy;
    }


    @Required
    public void setUpdateSegmentStrategy(UpdateSegmentStrategy updateSegmentStrategy)
    {
        this.updateSegmentStrategy = updateSegmentStrategy;
    }


    protected CxUserSegmentSessionService getCxUserSegmentSessionService()
    {
        return this.cxUserSegmentSessionService;
    }


    @Required
    public void setCxUserSegmentSessionService(CxUserSegmentSessionService cxUserSegmentSessionService)
    {
        this.cxUserSegmentSessionService = cxUserSegmentSessionService;
    }


    protected CxUserSegmentService getCxUserSegmentService()
    {
        return this.cxUserSegmentService;
    }


    @Required
    public void setCxUserSegmentService(CxUserSegmentService cxUserSegmentService)
    {
        this.cxUserSegmentService = cxUserSegmentService;
    }


    protected CxUserSegmentConversionHelper getCxUserSegmentConversionHelper()
    {
        return this.cxUserSegmentConversionHelper;
    }


    @Required
    public void setCxUserSegmentConversionHelper(CxUserSegmentConversionHelper cxUserSegmentConversionHelper)
    {
        this.cxUserSegmentConversionHelper = cxUserSegmentConversionHelper;
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
