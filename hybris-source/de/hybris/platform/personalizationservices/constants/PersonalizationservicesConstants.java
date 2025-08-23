package de.hybris.platform.personalizationservices.constants;

public final class PersonalizationservicesConstants extends GeneratedPersonalizationservicesConstants
{
    public static final String EXTENSIONNAME = "personalizationservices";
    public static final String LAST_EVALUATION_KEY = "personalizationservices.AfterSessionUserChanged.last.value";
    public static final String IGNORE_ANONYMOUS = "personalizationservices.calculate.ignoreAnonymous";
    public static final String URL_VOTER_PROPRTY_KEY = "personalizationservices.calculate";
    public static final String URL_VOTER_PROPERTY_PATTERN = "personalizationservices\\.calculate\\.(.*)\\.url";
    public static final String URL_VOTER_ACTIONS_PROPERTY_PATTERN = "personalizationservices\\.calculate\\.(.*)\\.actions";
    public static final String ACTIVE_PERSONALIZATION = "ACTIVE_PERSONALIZATION";
    public static final String ACTIVE_OCC_PERSONALIZATION = "ACTIVE_OCC_PERSONALIZATION";
    public static final String USER_CHANGED_ACTIONS_PROPERTY = "personalizationservices.after.user.changed.actions";
    public static final String CONSENTS_GIVEN_ACTIONS_PROPERTY = "personalizationservices.after.consents.given.actions";
    public static final String LOAD_ACTION_RESULT_MAX_REPEAT = "personalizationservices.actionResult.load.repeat.max";
    public static final String MIN_AFFINITY_PROPERTY = "personalizationservices.segment.trigger.strategy.min.affinity";
    public static final String PROCESS_DEFINITION_NAME = "personalizationservices.calculation.process";
    public static final String ANONYMOUS_USER_DEFAULT_ACTIONS = "personalizationservices.calculate.anonymousUserActions";
    @Deprecated(since = "1905", forRemoval = true)
    public static final String ANONYMOUS_USER_ACTIONS = "personalizationservices.calculate.anonymousUserDefaultActions";
    @Deprecated(since = "1905", forRemoval = true)
    public static final String ANONYMOUS_USER_REQUEST_NUMBER = "personalizationservices.calculate.anonymousUserMinRequestNumber";
    @Deprecated(since = "1905", forRemoval = true)
    public static final String ANONYMOUS_USER_TIME = "personalizationservices.calculate.anonymousUserMinTime";
    @Deprecated(since = "1905", forRemoval = true)
    public static final String ANONYMOUS_USER_IGNORE_OTHER = "personalizationservices.calculate.anonymousUserIgnoreOtherActions";
    public static final String CATALOG_LOOKUP_TYPE = "personalizationservices.calculate.catalogLookup";
    public static final String USER_SEGMENTS_STORE_IN_SESSION = "personalizationservices.user.segments.store.in.session";
    public static final String USER_TO_SEGMENTS_SESSION_KEY = "userToSegment_";
    public static final String BASE_SITE_UID_CX_BUSINESS_PROCESS_PARAMETER = "baseSiteUid";
    public static final String SESSION_TOKEN = "SESSION_TOKEN_KEY";
    public static final String USER_TO_SEGMENTS_PROCESS_PARAMETER = "userToSegmentsProcessParameter";
    public static final String CALCULATION_CONTEXT_PROCESS_PARAMETER = "calcContextProcessParameter";
    public static final String SESSION_CONSENTS = "user-consents";
    public static final String IGNORE_CONSENT_CHECK_WHEN_NO_CONSENT_TEMPLATE = "personalizationservices.consent.ignoreConsentCheckWhenNoConsentTemplate";
    public static final String CONTEXT_USER_KEY = "user";
    public static final String CONTEXT_SEGMENTS_KEY = "segments";
    public static final String CONTEXT_BASESITE_KEY = "baseSite";
    public static final String CONTEXT_CATALOG_VERSIONS_KEY = "catalogVersions";
    public static final String CONSENT_GIVEN = "GIVEN";
    public static final String PERSONALIZATION_DEFAULT_ID_COOKIE = "personalizationId";
    public static final String PERSONALIZATION_ID_COOKIE = "personalizationservices.personalizationIdCookie";
    public static final String PERSONALIZATION_DEFAULT_ID_HEADER = "Occ-Personalization-Id";
    public static final String PERSONALIZATION_ID_HEADER = "personalizationservices.personalizationIdHeader";
    public static final String PERSONALIZATION_DEFAULT_TIME_COOKIE = "CxCt";
    public static final String PERSONALIZATION_TIME_COOKIE = "personalizationservices.personalizationTimeCookie";
    public static final String PERSONALIZATION_DEFAULT_TIME_HEADER = "Occ-Personalization-Time";
    public static final String PERSONALIZATION_TIME_HEADER = "personalizationservices.personalizationTimeHeader";
    public static final Integer MAX_SEGMENT_NAME_LENGTH = Integer.valueOf(255);
}
