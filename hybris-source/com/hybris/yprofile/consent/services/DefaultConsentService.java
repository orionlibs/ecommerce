/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.consent.services;

import static com.hybris.yprofile.constants.ProfileservicesConstants.ANONYMOUS_CONSENTS_COOKIE_NAME;
import static com.hybris.yprofile.constants.ProfileservicesConstants.ANONYMOUS_CONSENTS_HEADER;
import static com.hybris.yprofile.constants.ProfileservicesConstants.CONSENT_REFERENCE_HEADER_NAME;
import static com.hybris.yprofile.constants.ProfileservicesConstants.PROFILE_CONSENT;
import static com.hybris.yprofile.constants.ProfileservicesConstants.PROFILE_CONSENT_GIVEN;
import static com.hybris.yprofile.constants.ProfileservicesConstants.USER_CONSENTS;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.charon.RawResponse;
import com.hybris.yprofile.common.Utils;
import com.hybris.yprofile.consent.cookie.EnhancedCookieGenerator;
import com.hybris.yprofile.constants.ProfileservicesConstants;
import com.hybris.yprofile.dto.cookie.ProfileConsentCookie;
import com.hybris.yprofile.rest.clients.ConsentServiceClient;
import com.hybris.yprofile.services.ProfileConfigurationService;
import com.hybris.yprofile.services.RetrieveRestClientStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.consent.CommerceConsentService;
import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Implementation for {@link ConsentService}. Service is responsible to generate and provide the consent reference.
 */
public class DefaultConsentService implements ConsentService
{
    private static final Logger LOG = Logger.getLogger(DefaultConsentService.class);
    private static final String CONSENT_REFERENCE_SESSION_ATTR_KEY = "consent-reference";
    private static final String CONSENT_REFERENCE_COOKIE_NAME_SUFFIX = "-consentReference";
    private static final String PROFILE_ID_COOKIE_NAME_SUFFIX = "-profileId";
    private static final String NULL = "null";
    public static final String PROFILE_CONSENT_GIVEN_COOKIE_NAME_SUFFIX = "-profile.consent.given";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String UTF_8 = "UTF-8";
    private EnhancedCookieGenerator cookieGenerator;
    private SessionService sessionService;
    private UserService userService;
    private BaseSiteService baseSiteService;
    private ModelService modelService;
    private ConfigurationService configurationService;
    private RetrieveRestClientStrategy retrieveRestClientStrategy;
    private ProfileConfigurationService profileConfigurationService;
    private CommerceConsentService commerceConsentService;


    @Override
    public boolean isProfileTrackingConsentGiven(final HttpServletRequest request)
    {
        if(isAnonymousUser())
        {
            return isProfileTrackingConsentGivenForAnonymousUser(request);
        }
        else
        {
            return isProfileTrackingConsentGivenForLoggedInUser();
        }
    }


    protected boolean isProfileTrackingConsentGivenForLoggedInUser()
    {
        return profileTrackingConsentForLoggedInUser(ProfileservicesConstants.CONSENT_GIVEN);
    }


    protected boolean profileTrackingConsentForLoggedInUser(final String consent)
    {
        boolean track = false;
        try
        {
            final Map<String, String> userConsents = getSessionService().getAttribute(USER_CONSENTS);
            if(userConsents != null
                            && userConsents.containsKey(PROFILE_CONSENT)
                            && consent.equals(userConsents.get(PROFILE_CONSENT)))
            {
                track = true;
            }
        }
        catch(Exception ex)
        {
            LOG.warn("Error while processing user consents", ex);
        }
        return track;
    }


    protected boolean isAnonymousUser()
    {
        return (getUserService().isAnonymousUser(getUserService().getCurrentUser())
                        || isUserSoftLoggedIn()
                        || getSessionService().getAttribute(USER_CONSENTS) == null);
    }


    protected boolean isUserSoftLoggedIn()
    {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication instanceof RememberMeAuthenticationToken);
    }


    protected boolean isProfileTrackingConsentGivenForAnonymousUser(final HttpServletRequest request)
    {
        final Optional<Cookie> anonymousConsentCookie = Utils.getCookie(request, ANONYMOUS_CONSENTS_COOKIE_NAME);
        if(anonymousConsentCookie.isPresent())
        {
            return isProfileTrackingConsentGivenInCookieForAnonymousUser(anonymousConsentCookie.get());
        }
        final Optional<String> header = Utils.getHeader(request, ANONYMOUS_CONSENTS_HEADER);
        if(header.isPresent())
        {
            return isProfileTrackingConsentGivenInHeaderForAnonymousUser(header.get());
        }
        return false;
    }


    protected boolean isProfileTrackingConsentGivenInCookieForAnonymousUser(final Cookie anonymousConsentCookie)
    {
        try
        {
            List<ProfileConsentCookie> profileConsentCookieList = new ArrayList<>(Arrays
                            .asList(mapper.readValue(URLDecoder.decode(anonymousConsentCookie.getValue(), UTF_8), ProfileConsentCookie[].class)));
            return isProfileConsentGiven(profileConsentCookieList);
        }
        catch(Exception ex)
        {
            LOG.warn("Error while processing anonymous consents", ex);
        }
        return false;
    }


    protected boolean isProfileTrackingConsentGivenInHeaderForAnonymousUser(final String anonymousConsentHeader)
    {
        try
        {
            final String anonymousConsentsHeader = URLDecoder.decode(anonymousConsentHeader, UTF_8);
            List<ProfileConsentCookie> anonymousConsents = Arrays.asList(mapper.readValue(anonymousConsentsHeader, ProfileConsentCookie[].class));
            return isProfileConsentGiven(anonymousConsents);
        }
        catch(Exception ex)
        {
            LOG.warn("Error while processing anonymous consents", ex);
        }
        return false;
    }


    private boolean isProfileConsentGiven(List<ProfileConsentCookie> anonymousConsents)
    {
        if(anonymousConsents.isEmpty())
        {
            return false;
        }
        return anonymousConsents.stream().filter(x -> PROFILE_CONSENT.equals(x.getTemplateCode()))
                        .map(k -> ProfileservicesConstants.CONSENT_GIVEN.equals(k.getConsentState()))
                        .reduce(true, (x, y) -> x && y);
    }


    @Override
    public void saveConsentReferenceInSessionAndConsentModel(final HttpServletRequest request)
    {
        final String siteId = getSiteId();
        String consentReferenceId = getConsentReferenceFromCookie(siteId, request);
        if(consentReferenceId == null)
        {
            consentReferenceId = getConsentReferenceFromHeader(request);
        }
        setAttributeInSession(CONSENT_REFERENCE_SESSION_ATTR_KEY, consentReferenceId);
        setConsentReferenceInConsentModel(consentReferenceId);
    }


    @Override
    public String getConsentReferenceFromCookie(final String siteId, final HttpServletRequest request)
    {
        final String consentReferenceCookieName = siteId + CONSENT_REFERENCE_COOKIE_NAME_SUFFIX;
        final Optional<Cookie> cookie = Utils.getCookie(request, consentReferenceCookieName);
        if(cookie.isPresent())
        {
            return cookie.get().getValue();
        }
        return null;
    }


    @Override
    public String getConsentReferenceFromHeader(final HttpServletRequest request)
    {
        final String headerName = getConfigurationService().getConfiguration().getString(CONSENT_REFERENCE_HEADER_NAME, StringUtils.EMPTY);
        LOG.debug("Consent reference header name is:" + headerName);
        Optional<String> consentHeader = Utils.getHeader(request, headerName);
        if(consentHeader.isPresent())
        {
            LOG.debug("Consent reference from header is:" + consentHeader.get());
            return consentHeader.get();
        }
        LOG.debug("Consent reference from header is empty");
        return null;
    }


    @Override
    public String getConsentReferenceFromSession()
    {
        if(getSessionService().getAttribute(PROFILE_CONSENT_GIVEN) != null &&
                        Boolean.TRUE.equals(getSessionService().getAttribute(PROFILE_CONSENT_GIVEN)))
        {
            return getSessionService().getAttribute(CONSENT_REFERENCE_SESSION_ATTR_KEY);
        }
        return null;
    }


    @Override
    public String getConsentReferenceFromConsentModel()
    {
        return Utils.getActiveConsentReferenceForCustomerAndBaseSite(
                        getBaseSiteService().getCurrentBaseSite(),
                        (CustomerModel)getUserService().getCurrentUser(),
                        getCommerceConsentService());
    }


    @Override
    public void setProfileConsentCookieAndSession(final HttpServletRequest request, final HttpServletResponse response, final boolean consent)
    {
        final String profileConsentGivenCookieName = getSiteId() + PROFILE_CONSENT_GIVEN_COOKIE_NAME_SUFFIX;
        final Optional<Cookie> cookie = Utils.getCookie(request, profileConsentGivenCookieName);
        setProfileConsent(consent);
        if(Utils.getCookie(request, PROFILE_CONSENT_GIVEN).isPresent())
        {
            Utils.removeCookie(cookieGenerator, response, PROFILE_CONSENT_GIVEN);
        }
        if(!cookie.isPresent())
        {
            Utils.setCookie(cookieGenerator, response, profileConsentGivenCookieName, Boolean.toString(consent), false);
            return;
        }
        if(!cookie.get().getValue().equals(Boolean.toString(consent)))
        {
            Utils.setCookie(cookieGenerator, response, profileConsentGivenCookieName, Boolean.toString(consent), false);
            return;
        }
    }


    @Override
    public void setProfileConsent(final boolean consent)
    {
        setAttributeInSession(PROFILE_CONSENT_GIVEN, consent);
    }


    @Override
    public void removeConsentReferenceInSession()
    {
        try
        {
            setAttributeInSession(CONSENT_REFERENCE_SESSION_ATTR_KEY, null);
        }
        catch(Exception e)
        {
            LOG.warn("Error removing consent reference cookie", e);
        }
    }


    @Override
    public void deleteConsentReferenceInConsentServiceAndInConsentModel(final ConsentModel consentModel, final String baseSiteId)
    {
        final String consentReference = consentModel.getConsentReference();
        if(!shouldSendEvent(consentReference, baseSiteId))
        {
            LOG.warn("CDS Configuration not found");
            return;
        }
        resetConsentReferenceInConsentModel(consentModel);
        final String debugEnabled = getDebugFlagValue(consentModel.getCustomer());
        getClient().deleteConsentReference(consentReference, debugEnabled)
                        .subscribe(response -> logSuccess(Optional.ofNullable(response), "Delete Consent Reference Request sent to CDS"),
                                        error -> logError(error),
                                        () -> logSuccess(Optional.empty(), "Delete Consent Reference Request sent to CDS"));
    }


    @Override
    public void saveConsentReferenceInSession(final String consentId)
    {
        setAttributeInSession(CONSENT_REFERENCE_SESSION_ATTR_KEY, consentId);
    }


    /**
     * Method checks for consents stored in the session - in the same way for both anonymous
     * and logged in user. Consents must be already stored in the session.
     */
    @Override
    public boolean isProfileTrackingConsentGiven()
    {
        return isProfileTrackingConsentGivenForLoggedInUser();
    }


    protected static String getDebugFlagValue(final UserModel userModel)
    {
        if(Boolean.TRUE.equals(userModel.getProfileTagDebug()))
        {
            return "1";
        }
        return "0";
    }


    protected boolean shouldSendEvent(final String consentReference, final String baseSiteId)
    {
        return !getProfileConfigurationService().isProfileTrackingPaused()
                        && getProfileConfigurationService().isConfigurationPresent()
                        && isValidConsentReference(consentReference);
    }


    protected static boolean isValidConsentReference(String consentReferenceId)
    {
        return !isBlank(consentReferenceId) && !NULL.equals(consentReferenceId);
    }


    protected void setAttributeInSession(final String key, final Object value)
    {
        if(value == null)
        {
            return;
        }
        try
        {
            getSessionService().setAttribute(key, value);
        }
        catch(Exception e)
        {
            LOG.warn("Error setting " + key + " in session", e);
        }
    }


    protected String getSiteId()
    {
        return getCurrentBaseSiteModel().isPresent() ? getCurrentBaseSiteModel().get().getUid() : StringUtils.EMPTY;
    }


    protected Optional<BaseSiteModel> getCurrentBaseSiteModel()
    {
        return ofNullable(getBaseSiteService().getCurrentBaseSite());
    }


    protected static void logSuccess(final Optional<RawResponse> rawResponse, final String message)
    {
        LOG.debug(message);
        rawResponse.ifPresent(response -> {
            final int statusCode = response.getStatusCode();
            final Optional<String> optionalContext = response.header("hybris-context-trace-id");
            optionalContext.
                            ifPresent(contextTraceId -> LOG.debug("Event sent to yprofile. " +
                                            "With Status: " + statusCode + " and " +
                                            "Context-Trace-ID: " + contextTraceId));
        });
    }


    protected static void logError(Throwable error)
    {
        LOG.error("Error sending request to consent service", error);
    }


    protected ConsentServiceClient getClient()
    {
        return getRetrieveRestClientStrategy().getConsentServiceRestClient();
    }


    protected EnhancedCookieGenerator getCookieGenerator()
    {
        return cookieGenerator;
    }


    @Required
    public void setCookieGenerator(EnhancedCookieGenerator cookieGenerator)
    {
        this.cookieGenerator = cookieGenerator;
    }


    public SessionService getSessionService()
    {
        return sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public UserService getUserService()
    {
        return userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public ProfileConfigurationService getProfileConfigurationService()
    {
        return profileConfigurationService;
    }


    @Required
    public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService)
    {
        this.profileConfigurationService = profileConfigurationService;
    }


    public RetrieveRestClientStrategy getRetrieveRestClientStrategy()
    {
        return retrieveRestClientStrategy;
    }


    @Required
    public void setRetrieveRestClientStrategy(RetrieveRestClientStrategy retrieveRestClientStrategy)
    {
        this.retrieveRestClientStrategy = retrieveRestClientStrategy;
    }


    public BaseSiteService getBaseSiteService()
    {
        return baseSiteService;
    }


    @Required
    public void setBaseSiteService(final BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    public ModelService getModelService()
    {
        return modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public CommerceConsentService getCommerceConsentService()
    {
        return commerceConsentService;
    }


    @Required
    public void setCommerceConsentService(CommerceConsentService commerceConsentService)
    {
        this.commerceConsentService = commerceConsentService;
    }


    protected void setConsentReferenceInConsentModel(String consentReferenceId)
    {
        final UserModel user = getUserService().getCurrentUser();
        final Optional<ConsentModel> consentModelOptional = Utils.getActiveConsentModelForCustomerAndBaseSite(
                        getBaseSiteService().getCurrentBaseSite(),
                        (CustomerModel)user,
                        getCommerceConsentService());
        if(!getUserService().isAnonymousUser(user) && consentModelOptional.isPresent())
        {
            final ConsentModel consentModel = consentModelOptional.get();
            if(isBlank(consentModel.getConsentReference()) ||
                            (!isBlank(consentReferenceId) && !consentModel.getConsentReference().equals(consentReferenceId)))
            {
                LOG.debug("Setting CR in consent model.");
                consentModel.setConsentReference(consentReferenceId);
                user.setProfileTagDebug(false);
                getModelService().save(consentModel);
                getModelService().refresh(consentModel);
                getModelService().save(user);
                getModelService().refresh(user);
            }
        }
    }


    protected void resetConsentReferenceInConsentModel(final ConsentModel consentModel)
    {
        try
        {
            consentModel.setConsentReference(null);
            getModelService().save(consentModel);
            getModelService().refresh(consentModel);
        }
        catch(Exception e)
        {
            LOG.warn("Error resetting the consent reference", e);
        }
    }


    @Override
    public void setProfileIdCookie(final HttpServletRequest request, final HttpServletResponse response, final String consentReferenceId)
    {
        final String siteId = getSiteId();
        final String profileIdCookieName = siteId + CONSENT_REFERENCE_COOKIE_NAME_SUFFIX;
        final Optional<Cookie> cookie = Utils.getCookie(request, profileIdCookieName);
        if(!cookie.isPresent())
        {
            Utils.setCookie(cookieGenerator, response, siteId + PROFILE_ID_COOKIE_NAME_SUFFIX, consentReferenceId, true);
        }
    }
}

