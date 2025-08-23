package de.hybris.platform.personalizationservices.segment.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.data.UserToSegmentData;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentSessionService;
import de.hybris.platform.personalizationservices.segment.converters.CxUserSegmentConversionHelper;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxUserSegmentSessionService implements CxUserSegmentSessionService
{
    private static final String USER_NOT_NULL = "user must not be null";
    private SessionService sessionService;
    private UserService userService;
    private CxConfigurationService cxConfigurationService;
    private CxUserSegmentConversionHelper cxUserSegmentConversionHelper;
    private CxUserSegmentService cxUserSegmentService;
    private BaseSiteService baseSiteService;


    public boolean isUserSegmentStoredInSession(UserModel user)
    {
        return (this.userService.isAnonymousUser(user) || this.cxConfigurationService.isUserSegmentsStoreInSession().booleanValue());
    }


    public Collection<UserToSegmentData> getUserSegmentsFromSession(UserModel user)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        Collection<UserToSegmentData> userToSegmentsData = (Collection<UserToSegmentData>)this.sessionService.getAttribute(getUserSegmentsSessionKey(user));
        if(userToSegmentsData == null)
        {
            return Collections.emptyList();
        }
        return new ArrayList<>(userToSegmentsData);
    }


    protected String getUserSegmentsSessionKey(UserModel user)
    {
        return "userToSegment_" + user.getUid();
    }


    public void setUserSegmentsInSession(UserModel user, Collection<? extends UserToSegmentData> userToSegments)
    {
        setUserSegmentsInSession(user, userToSegments, null);
    }


    public void setUserSegmentsInSession(UserModel user, Collection<? extends UserToSegmentData> userToSegments, CxCalculationContext context)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        Set<String> providers = (context == null) ? null : context.getSegmentUpdateProviders();
        if(CollectionUtils.isEmpty(providers))
        {
            this.sessionService.setAttribute(getUserSegmentsSessionKey(user), new ArrayList<>(userToSegments));
            return;
        }
        Map<Boolean, List<UserToSegmentData>> segmentsGroupedForProviders = (Map<Boolean, List<UserToSegmentData>>)getUserSegmentsFromSession(user).stream().collect(Collectors.partitioningBy(us -> providers.contains(us.getProvider())));
        Collection<UserToSegmentData> segmentsToMerge = segmentsGroupedForProviders.get(Boolean.FALSE);
        Collection<UserToSegmentData> allSegments = (Collection<UserToSegmentData>)userToSegments.stream().filter(us -> providers.contains(us.getProvider())).collect(Collectors.toList());
        allSegments.addAll(segmentsToMerge);
        this.sessionService.setAttribute(getUserSegmentsSessionKey(user), allSegments);
    }


    public void addUserSegmentsInSession(UserModel user, Collection<? extends UserToSegmentData> userSegments)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        if(CollectionUtils.isEmpty(userSegments))
        {
            return;
        }
        Collection<UserToSegmentData> existingNotModifiedUserToSegments = (Collection<UserToSegmentData>)getUserSegmentsFromSession(user).stream().filter(u2s -> !existInCollection(u2s, userSegments)).collect(Collectors.toList());
        setUserSegmentsInSession(user, CollectionUtils.union(existingNotModifiedUserToSegments, userSegments));
    }


    public void removeUserSegmentsFromSession(UserModel user, Collection<? extends UserToSegmentData> userSegmentsToRemove)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        if(CollectionUtils.isEmpty(userSegmentsToRemove))
        {
            return;
        }
        Collection<UserToSegmentData> newUserToSegments = (Collection<UserToSegmentData>)getUserSegmentsFromSession(user).stream().filter(us -> !existInCollection(us, userSegmentsToRemove)).collect(Collectors.toList());
        setUserSegmentsInSession(user, newUserToSegments);
    }


    private static boolean existInCollection(UserToSegmentData userToSegmentData, Collection<? extends UserToSegmentData> userToSegments)
    {
        return userToSegments.stream()
                        .anyMatch(u2s -> haveEqualKey(u2s, userToSegmentData));
    }


    private static boolean haveEqualKey(UserToSegmentData us1, UserToSegmentData us2)
    {
        return (StringUtils.equals(us1.getCode(), us2.getCode()) && StringUtils.equals(us1.getProvider(), us2.getProvider()));
    }


    public void loadUserSegmentsIntoSession(UserModel user)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        Collection<UserToSegmentData> userSegmentsData = this.cxUserSegmentConversionHelper.convertToData(this.cxUserSegmentService.getUserSegments(user, this.baseSiteService.getCurrentBaseSite()));
        setUserSegmentsInSession(user, userSegmentsData);
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
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


    protected CxUserSegmentService getCxUserSegmentService()
    {
        return this.cxUserSegmentService;
    }


    @Required
    public void setCxUserSegmentService(CxUserSegmentService cxUserSegmentService)
    {
        this.cxUserSegmentService = cxUserSegmentService;
    }
}
