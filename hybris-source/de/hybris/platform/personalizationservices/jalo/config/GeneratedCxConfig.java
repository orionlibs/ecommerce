package de.hybris.platform.personalizationservices.jalo.config;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.commerceservices.jalo.consent.ConsentTemplate;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCxConfig extends GenericItem
{
    public static final String CODE = "code";
    public static final String MINAFFINITY = "minAffinity";
    public static final String ACTIONRESULTMAXREPEAT = "actionResultMaxRepeat";
    public static final String USERCHANGEDACTIONS = "userChangedActions";
    public static final String CONSENTGIVENACTIONS = "consentGivenActions";
    public static final String CALCULATIONPROCESS = "calculationProcess";
    public static final String IGNORERECALCFORANONYMOUS = "ignoreRecalcForAnonymous";
    public static final String ANONYMOUSUSERDEFAULTACTIONS = "anonymousUserDefaultActions";
    public static final String ANONYMOUSUSERACTIONS = "anonymousUserActions";
    public static final String ANONYMOUSUSERMINREQUESTNUMBER = "anonymousUserMinRequestNumber";
    public static final String ANONYMOUSUSERMINTIME = "anonymousUserMinTime";
    public static final String ANONYMOUSUSERIGNOREOTHERACTIONS = "anonymousUserIgnoreOtherActions";
    public static final String CATALOGLOOKUP = "catalogLookup";
    public static final String CONSENTTEMPLATES = "consentTemplates";
    public static final String USERSEGMENTSSTOREINSESSION = "userSegmentsStoreInSession";
    public static final String OCCPERSONALIZATIONENABLED = "occPersonalizationEnabled";
    public static final String OCCTTL = "occTTL";
    public static final String OCCPERSONALIZATIONIDCOOKIEENABLED = "occPersonalizationIdCookieEnabled";
    public static final String URLVOTERCONFIGS = "urlVoterConfigs";
    public static final String BASESITES = "baseSites";
    public static final String PERIODICVOTERCONFIGS = "periodicVoterConfigs";
    protected static final OneToManyHandler<CxUrlVoterConfig> URLVOTERCONFIGSHANDLER = new OneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXURLVOTERCONFIG, true, "cxConfig", "cxConfigPOS", true, true, 2);
    protected static final OneToManyHandler<BaseSite> BASESITESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.BASESITE, false, "cxConfig", null, false, true, 1);
    protected static final OneToManyHandler<CxPeriodicVoterConfig> PERIODICVOTERCONFIGSHANDLER = new OneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXPERIODICVOTERCONFIG, true, "cxConfig", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("minAffinity", Item.AttributeMode.INITIAL);
        tmp.put("actionResultMaxRepeat", Item.AttributeMode.INITIAL);
        tmp.put("userChangedActions", Item.AttributeMode.INITIAL);
        tmp.put("consentGivenActions", Item.AttributeMode.INITIAL);
        tmp.put("calculationProcess", Item.AttributeMode.INITIAL);
        tmp.put("ignoreRecalcForAnonymous", Item.AttributeMode.INITIAL);
        tmp.put("anonymousUserDefaultActions", Item.AttributeMode.INITIAL);
        tmp.put("anonymousUserActions", Item.AttributeMode.INITIAL);
        tmp.put("anonymousUserMinRequestNumber", Item.AttributeMode.INITIAL);
        tmp.put("anonymousUserMinTime", Item.AttributeMode.INITIAL);
        tmp.put("anonymousUserIgnoreOtherActions", Item.AttributeMode.INITIAL);
        tmp.put("catalogLookup", Item.AttributeMode.INITIAL);
        tmp.put("consentTemplates", Item.AttributeMode.INITIAL);
        tmp.put("userSegmentsStoreInSession", Item.AttributeMode.INITIAL);
        tmp.put("occPersonalizationEnabled", Item.AttributeMode.INITIAL);
        tmp.put("occTTL", Item.AttributeMode.INITIAL);
        tmp.put("occPersonalizationIdCookieEnabled", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getActionResultMaxRepeat(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "actionResultMaxRepeat");
    }


    public Integer getActionResultMaxRepeat()
    {
        return getActionResultMaxRepeat(getSession().getSessionContext());
    }


    public int getActionResultMaxRepeatAsPrimitive(SessionContext ctx)
    {
        Integer value = getActionResultMaxRepeat(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getActionResultMaxRepeatAsPrimitive()
    {
        return getActionResultMaxRepeatAsPrimitive(getSession().getSessionContext());
    }


    public void setActionResultMaxRepeat(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "actionResultMaxRepeat", value);
    }


    public void setActionResultMaxRepeat(Integer value)
    {
        setActionResultMaxRepeat(getSession().getSessionContext(), value);
    }


    public void setActionResultMaxRepeat(SessionContext ctx, int value)
    {
        setActionResultMaxRepeat(ctx, Integer.valueOf(value));
    }


    public void setActionResultMaxRepeat(int value)
    {
        setActionResultMaxRepeat(getSession().getSessionContext(), value);
    }


    public Set<String> getAnonymousUserActions(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "anonymousUserActions");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getAnonymousUserActions()
    {
        return getAnonymousUserActions(getSession().getSessionContext());
    }


    public void setAnonymousUserActions(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "anonymousUserActions", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setAnonymousUserActions(Set<String> value)
    {
        setAnonymousUserActions(getSession().getSessionContext(), value);
    }


    public Set<String> getAnonymousUserDefaultActions(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "anonymousUserDefaultActions");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getAnonymousUserDefaultActions()
    {
        return getAnonymousUserDefaultActions(getSession().getSessionContext());
    }


    public void setAnonymousUserDefaultActions(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "anonymousUserDefaultActions", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setAnonymousUserDefaultActions(Set<String> value)
    {
        setAnonymousUserDefaultActions(getSession().getSessionContext(), value);
    }


    public Boolean isAnonymousUserIgnoreOtherActions(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "anonymousUserIgnoreOtherActions");
    }


    public Boolean isAnonymousUserIgnoreOtherActions()
    {
        return isAnonymousUserIgnoreOtherActions(getSession().getSessionContext());
    }


    public boolean isAnonymousUserIgnoreOtherActionsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAnonymousUserIgnoreOtherActions(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAnonymousUserIgnoreOtherActionsAsPrimitive()
    {
        return isAnonymousUserIgnoreOtherActionsAsPrimitive(getSession().getSessionContext());
    }


    public void setAnonymousUserIgnoreOtherActions(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "anonymousUserIgnoreOtherActions", value);
    }


    public void setAnonymousUserIgnoreOtherActions(Boolean value)
    {
        setAnonymousUserIgnoreOtherActions(getSession().getSessionContext(), value);
    }


    public void setAnonymousUserIgnoreOtherActions(SessionContext ctx, boolean value)
    {
        setAnonymousUserIgnoreOtherActions(ctx, Boolean.valueOf(value));
    }


    public void setAnonymousUserIgnoreOtherActions(boolean value)
    {
        setAnonymousUserIgnoreOtherActions(getSession().getSessionContext(), value);
    }


    public Integer getAnonymousUserMinRequestNumber(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "anonymousUserMinRequestNumber");
    }


    public Integer getAnonymousUserMinRequestNumber()
    {
        return getAnonymousUserMinRequestNumber(getSession().getSessionContext());
    }


    public int getAnonymousUserMinRequestNumberAsPrimitive(SessionContext ctx)
    {
        Integer value = getAnonymousUserMinRequestNumber(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getAnonymousUserMinRequestNumberAsPrimitive()
    {
        return getAnonymousUserMinRequestNumberAsPrimitive(getSession().getSessionContext());
    }


    public void setAnonymousUserMinRequestNumber(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "anonymousUserMinRequestNumber", value);
    }


    public void setAnonymousUserMinRequestNumber(Integer value)
    {
        setAnonymousUserMinRequestNumber(getSession().getSessionContext(), value);
    }


    public void setAnonymousUserMinRequestNumber(SessionContext ctx, int value)
    {
        setAnonymousUserMinRequestNumber(ctx, Integer.valueOf(value));
    }


    public void setAnonymousUserMinRequestNumber(int value)
    {
        setAnonymousUserMinRequestNumber(getSession().getSessionContext(), value);
    }


    public Long getAnonymousUserMinTime(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "anonymousUserMinTime");
    }


    public Long getAnonymousUserMinTime()
    {
        return getAnonymousUserMinTime(getSession().getSessionContext());
    }


    public long getAnonymousUserMinTimeAsPrimitive(SessionContext ctx)
    {
        Long value = getAnonymousUserMinTime(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getAnonymousUserMinTimeAsPrimitive()
    {
        return getAnonymousUserMinTimeAsPrimitive(getSession().getSessionContext());
    }


    public void setAnonymousUserMinTime(SessionContext ctx, Long value)
    {
        setProperty(ctx, "anonymousUserMinTime", value);
    }


    public void setAnonymousUserMinTime(Long value)
    {
        setAnonymousUserMinTime(getSession().getSessionContext(), value);
    }


    public void setAnonymousUserMinTime(SessionContext ctx, long value)
    {
        setAnonymousUserMinTime(ctx, Long.valueOf(value));
    }


    public void setAnonymousUserMinTime(long value)
    {
        setAnonymousUserMinTime(getSession().getSessionContext(), value);
    }


    public Set<BaseSite> getBaseSites(SessionContext ctx)
    {
        return (Set<BaseSite>)BASESITESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<BaseSite> getBaseSites()
    {
        return getBaseSites(getSession().getSessionContext());
    }


    public void setBaseSites(SessionContext ctx, Set<BaseSite> value)
    {
        BASESITESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setBaseSites(Set<BaseSite> value)
    {
        setBaseSites(getSession().getSessionContext(), value);
    }


    public void addToBaseSites(SessionContext ctx, BaseSite value)
    {
        BASESITESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToBaseSites(BaseSite value)
    {
        addToBaseSites(getSession().getSessionContext(), value);
    }


    public void removeFromBaseSites(SessionContext ctx, BaseSite value)
    {
        BASESITESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromBaseSites(BaseSite value)
    {
        removeFromBaseSites(getSession().getSessionContext(), value);
    }


    public String getCalculationProcess(SessionContext ctx)
    {
        return (String)getProperty(ctx, "calculationProcess");
    }


    public String getCalculationProcess()
    {
        return getCalculationProcess(getSession().getSessionContext());
    }


    public void setCalculationProcess(SessionContext ctx, String value)
    {
        setProperty(ctx, "calculationProcess", value);
    }


    public void setCalculationProcess(String value)
    {
        setCalculationProcess(getSession().getSessionContext(), value);
    }


    public EnumerationValue getCatalogLookup(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "catalogLookup");
    }


    public EnumerationValue getCatalogLookup()
    {
        return getCatalogLookup(getSession().getSessionContext());
    }


    public void setCatalogLookup(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "catalogLookup", value);
    }


    public void setCatalogLookup(EnumerationValue value)
    {
        setCatalogLookup(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public Set<String> getConsentGivenActions(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "consentGivenActions");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getConsentGivenActions()
    {
        return getConsentGivenActions(getSession().getSessionContext());
    }


    public void setConsentGivenActions(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "consentGivenActions", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setConsentGivenActions(Set<String> value)
    {
        setConsentGivenActions(getSession().getSessionContext(), value);
    }


    public Set<ConsentTemplate> getConsentTemplates(SessionContext ctx)
    {
        Set<ConsentTemplate> coll = (Set<ConsentTemplate>)getProperty(ctx, "consentTemplates");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<ConsentTemplate> getConsentTemplates()
    {
        return getConsentTemplates(getSession().getSessionContext());
    }


    public void setConsentTemplates(SessionContext ctx, Set<ConsentTemplate> value)
    {
        setProperty(ctx, "consentTemplates", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setConsentTemplates(Set<ConsentTemplate> value)
    {
        setConsentTemplates(getSession().getSessionContext(), value);
    }


    public Boolean isIgnoreRecalcForAnonymous(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ignoreRecalcForAnonymous");
    }


    public Boolean isIgnoreRecalcForAnonymous()
    {
        return isIgnoreRecalcForAnonymous(getSession().getSessionContext());
    }


    public boolean isIgnoreRecalcForAnonymousAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIgnoreRecalcForAnonymous(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIgnoreRecalcForAnonymousAsPrimitive()
    {
        return isIgnoreRecalcForAnonymousAsPrimitive(getSession().getSessionContext());
    }


    public void setIgnoreRecalcForAnonymous(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ignoreRecalcForAnonymous", value);
    }


    public void setIgnoreRecalcForAnonymous(Boolean value)
    {
        setIgnoreRecalcForAnonymous(getSession().getSessionContext(), value);
    }


    public void setIgnoreRecalcForAnonymous(SessionContext ctx, boolean value)
    {
        setIgnoreRecalcForAnonymous(ctx, Boolean.valueOf(value));
    }


    public void setIgnoreRecalcForAnonymous(boolean value)
    {
        setIgnoreRecalcForAnonymous(getSession().getSessionContext(), value);
    }


    public BigDecimal getMinAffinity(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "minAffinity");
    }


    public BigDecimal getMinAffinity()
    {
        return getMinAffinity(getSession().getSessionContext());
    }


    public void setMinAffinity(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "minAffinity", value);
    }


    public void setMinAffinity(BigDecimal value)
    {
        setMinAffinity(getSession().getSessionContext(), value);
    }


    public Boolean isOccPersonalizationEnabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "occPersonalizationEnabled");
    }


    public Boolean isOccPersonalizationEnabled()
    {
        return isOccPersonalizationEnabled(getSession().getSessionContext());
    }


    public boolean isOccPersonalizationEnabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isOccPersonalizationEnabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isOccPersonalizationEnabledAsPrimitive()
    {
        return isOccPersonalizationEnabledAsPrimitive(getSession().getSessionContext());
    }


    public void setOccPersonalizationEnabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "occPersonalizationEnabled", value);
    }


    public void setOccPersonalizationEnabled(Boolean value)
    {
        setOccPersonalizationEnabled(getSession().getSessionContext(), value);
    }


    public void setOccPersonalizationEnabled(SessionContext ctx, boolean value)
    {
        setOccPersonalizationEnabled(ctx, Boolean.valueOf(value));
    }


    public void setOccPersonalizationEnabled(boolean value)
    {
        setOccPersonalizationEnabled(getSession().getSessionContext(), value);
    }


    public Boolean isOccPersonalizationIdCookieEnabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "occPersonalizationIdCookieEnabled");
    }


    public Boolean isOccPersonalizationIdCookieEnabled()
    {
        return isOccPersonalizationIdCookieEnabled(getSession().getSessionContext());
    }


    public boolean isOccPersonalizationIdCookieEnabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isOccPersonalizationIdCookieEnabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isOccPersonalizationIdCookieEnabledAsPrimitive()
    {
        return isOccPersonalizationIdCookieEnabledAsPrimitive(getSession().getSessionContext());
    }


    public void setOccPersonalizationIdCookieEnabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "occPersonalizationIdCookieEnabled", value);
    }


    public void setOccPersonalizationIdCookieEnabled(Boolean value)
    {
        setOccPersonalizationIdCookieEnabled(getSession().getSessionContext(), value);
    }


    public void setOccPersonalizationIdCookieEnabled(SessionContext ctx, boolean value)
    {
        setOccPersonalizationIdCookieEnabled(ctx, Boolean.valueOf(value));
    }


    public void setOccPersonalizationIdCookieEnabled(boolean value)
    {
        setOccPersonalizationIdCookieEnabled(getSession().getSessionContext(), value);
    }


    public Long getOccTTL(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "occTTL");
    }


    public Long getOccTTL()
    {
        return getOccTTL(getSession().getSessionContext());
    }


    public long getOccTTLAsPrimitive(SessionContext ctx)
    {
        Long value = getOccTTL(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getOccTTLAsPrimitive()
    {
        return getOccTTLAsPrimitive(getSession().getSessionContext());
    }


    public void setOccTTL(SessionContext ctx, Long value)
    {
        setProperty(ctx, "occTTL", value);
    }


    public void setOccTTL(Long value)
    {
        setOccTTL(getSession().getSessionContext(), value);
    }


    public void setOccTTL(SessionContext ctx, long value)
    {
        setOccTTL(ctx, Long.valueOf(value));
    }


    public void setOccTTL(long value)
    {
        setOccTTL(getSession().getSessionContext(), value);
    }


    public Set<CxPeriodicVoterConfig> getPeriodicVoterConfigs(SessionContext ctx)
    {
        return (Set<CxPeriodicVoterConfig>)PERIODICVOTERCONFIGSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<CxPeriodicVoterConfig> getPeriodicVoterConfigs()
    {
        return getPeriodicVoterConfigs(getSession().getSessionContext());
    }


    public void setPeriodicVoterConfigs(SessionContext ctx, Set<CxPeriodicVoterConfig> value)
    {
        PERIODICVOTERCONFIGSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setPeriodicVoterConfigs(Set<CxPeriodicVoterConfig> value)
    {
        setPeriodicVoterConfigs(getSession().getSessionContext(), value);
    }


    public void addToPeriodicVoterConfigs(SessionContext ctx, CxPeriodicVoterConfig value)
    {
        PERIODICVOTERCONFIGSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToPeriodicVoterConfigs(CxPeriodicVoterConfig value)
    {
        addToPeriodicVoterConfigs(getSession().getSessionContext(), value);
    }


    public void removeFromPeriodicVoterConfigs(SessionContext ctx, CxPeriodicVoterConfig value)
    {
        PERIODICVOTERCONFIGSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromPeriodicVoterConfigs(CxPeriodicVoterConfig value)
    {
        removeFromPeriodicVoterConfigs(getSession().getSessionContext(), value);
    }


    public List<CxUrlVoterConfig> getUrlVoterConfigs(SessionContext ctx)
    {
        return (List<CxUrlVoterConfig>)URLVOTERCONFIGSHANDLER.getValues(ctx, (Item)this);
    }


    public List<CxUrlVoterConfig> getUrlVoterConfigs()
    {
        return getUrlVoterConfigs(getSession().getSessionContext());
    }


    public void setUrlVoterConfigs(SessionContext ctx, List<CxUrlVoterConfig> value)
    {
        URLVOTERCONFIGSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setUrlVoterConfigs(List<CxUrlVoterConfig> value)
    {
        setUrlVoterConfigs(getSession().getSessionContext(), value);
    }


    public void addToUrlVoterConfigs(SessionContext ctx, CxUrlVoterConfig value)
    {
        URLVOTERCONFIGSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToUrlVoterConfigs(CxUrlVoterConfig value)
    {
        addToUrlVoterConfigs(getSession().getSessionContext(), value);
    }


    public void removeFromUrlVoterConfigs(SessionContext ctx, CxUrlVoterConfig value)
    {
        URLVOTERCONFIGSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromUrlVoterConfigs(CxUrlVoterConfig value)
    {
        removeFromUrlVoterConfigs(getSession().getSessionContext(), value);
    }


    public Set<String> getUserChangedActions(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "userChangedActions");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getUserChangedActions()
    {
        return getUserChangedActions(getSession().getSessionContext());
    }


    public void setUserChangedActions(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "userChangedActions", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setUserChangedActions(Set<String> value)
    {
        setUserChangedActions(getSession().getSessionContext(), value);
    }


    public Boolean isUserSegmentsStoreInSession(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "userSegmentsStoreInSession");
    }


    public Boolean isUserSegmentsStoreInSession()
    {
        return isUserSegmentsStoreInSession(getSession().getSessionContext());
    }


    public boolean isUserSegmentsStoreInSessionAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUserSegmentsStoreInSession(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUserSegmentsStoreInSessionAsPrimitive()
    {
        return isUserSegmentsStoreInSessionAsPrimitive(getSession().getSessionContext());
    }


    public void setUserSegmentsStoreInSession(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "userSegmentsStoreInSession", value);
    }


    public void setUserSegmentsStoreInSession(Boolean value)
    {
        setUserSegmentsStoreInSession(getSession().getSessionContext(), value);
    }


    public void setUserSegmentsStoreInSession(SessionContext ctx, boolean value)
    {
        setUserSegmentsStoreInSession(ctx, Boolean.valueOf(value));
    }


    public void setUserSegmentsStoreInSession(boolean value)
    {
        setUserSegmentsStoreInSession(getSession().getSessionContext(), value);
    }
}
