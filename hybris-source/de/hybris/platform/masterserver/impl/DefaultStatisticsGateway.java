package de.hybris.platform.masterserver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hybris.statistics.StatisticsGateway;
import com.hybris.statistics.collector.BusinessStatisticsCollector;
import com.hybris.statistics.collector.StatisticsCollector;
import com.hybris.statistics.collector.SystemStatisticsCollector;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.masterserver.StatisticsConfig;
import de.hybris.platform.masterserver.StatisticsPayloadEncryptor;
import de.hybris.platform.util.collections.fast.YLongSet;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;

public class DefaultStatisticsGateway implements StatisticsGateway<StatisticsPayload>
{
    private static final Logger LOG = Logger.getLogger(DefaultStatisticsGateway.class);
    private static final int SEND_STATS_INTERVAL_IN_HOURS = 8;
    private static final String ANONYMOUS_UID = "anonymous";
    private static final String HOME_URL = "https://license.hybris.com/store";
    public static final String MS_FORCE_DEVELOPMENT_ENV = "msForceDevelopment";
    private final Set<BusinessStatisticsCollector> businessCollectors;
    private final Set<SystemStatisticsCollector> systemCollectors;
    private Map<String, Map<String, Map<String, Object>>> systemStatistics;
    private volatile DateTime lastStatsSentAt;
    private final ConcurrentMap<String, YLongSet> loggedBackOfficeUsers = new ConcurrentHashMap<>();
    private final Map<String, String> webModules;
    private final StatisticsPayloadEncryptor encryptor;
    private final boolean forceDevelopmentModeFromEnv;
    private StatisticsConfig statisticsConfig = StatisticsConfig.defaultConfig();


    public DefaultStatisticsGateway(Set<BusinessStatisticsCollector> businessCollectors, Set<SystemStatisticsCollector> systemCollectors, Map<String, String> existingWebModules, StatisticsPayloadEncryptor encryptor)
    {
        this.businessCollectors = businessCollectors;
        this.systemCollectors = systemCollectors;
        this.webModules = Maps.newHashMap(existingWebModules);
        this.encryptor = encryptor;
        this.forceDevelopmentModeFromEnv = isForceDevelopmentModeFromEnv();
    }


    static Duration getSendStatsInterval()
    {
        return Duration.ofHours(8L);
    }


    public StatisticsPayload getStatisticsPayload()
    {
        StatisticsPayload statisticsPayload = null;
        if(this.forceDevelopmentModeFromEnv || (isSendingStatsAgreedInLicense() && (isGenerateStatsRequired() || isStatisticsPublisherEnabled())))
        {
            try
            {
                Map<String, Map> mainResult = new LinkedHashMap<>();
                boolean sendStats = false;
                Map<String, Map<String, Map<String, Object>>> collectorsInfoStatistics = getCollectorsInfoStatistics();
                Map<String, Map<String, Map<String, Object>>> sessionStatistics = getSessionStatistics();
                Map<String, Map<String, Map<String, Object>>> systemStatistics = getSystemStatistics();
                Map<String, Map<String, Map<String, Object>>> businessStatistics = getBusinessStatistics();
                if(!collectorsInfoStatistics.isEmpty())
                {
                    mainResult.putAll((Map)collectorsInfoStatistics);
                    sendStats = true;
                }
                if(sessionStatistics != null && !sessionStatistics.isEmpty())
                {
                    mainResult.putAll((Map)sessionStatistics);
                    sendStats = true;
                }
                if(systemStatistics != null && !systemStatistics.isEmpty())
                {
                    mainResult.putAll((Map)systemStatistics);
                    sendStats = true;
                }
                if(businessStatistics != null && !businessStatistics.isEmpty())
                {
                    mainResult.putAll((Map)businessStatistics);
                    sendStats = true;
                }
                if(sendStats)
                {
                    statisticsPayload = this.encryptor.encrypt(toJson(mainResult), "https://license.hybris.com/store");
                }
            }
            catch(Exception e)
            {
                if(isLogExceptionsInDebug())
                {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }
        return statisticsPayload;
    }


    protected void setStatisticsConfig(StatisticsConfig statisticsConfig)
    {
        this.statisticsConfig = Objects.<StatisticsConfig>requireNonNull(statisticsConfig);
    }


    private boolean isStatisticsPublisherEnabled()
    {
        return this.statisticsConfig.isStatisticsPublisherEnabled();
    }


    private Map<String, Map<String, Map<String, Object>>> getCollectorsInfoStatistics()
    {
        Map<String, Map<String, Map<String, Object>>> result = new LinkedHashMap<>();
        try
        {
            Sets.SetView setView = Sets.union(this.systemCollectors, this.businessCollectors);
            Map<String, Object> collectorsInfoStats = new LinkedHashMap<>();
            int i = 1;
            for(StatisticsCollector collector : setView)
            {
                collectorsInfoStats.put(Integer.toString(i++), collector.getClass().getName());
            }
            if(!collectorsInfoStats.isEmpty())
            {
                ImmutableMap<String, Map<String, Object>> overall = ImmutableMap.builder().put("collectors", collectorsInfoStats).build();
                result.put("collectorsInfo", overall);
            }
        }
        catch(Exception e)
        {
            if(isLogExceptionsInDebug())
            {
                LOG.debug(e.getMessage(), e);
            }
        }
        return result;
    }


    boolean isForceDevelopmentModeFromEnv()
    {
        boolean result = false;
        try
        {
            result = Boolean.parseBoolean(System.getenv("msForceDevelopment"));
        }
        catch(Exception e)
        {
            if(isLogExceptionsInDebug())
            {
                LOG.debug(e.getMessage(), e);
            }
        }
        return result;
    }


    public void updateLoggedInUsersStats(String webAppContext)
    {
        String webApp = this.webModules.get(webAppContext);
        if(webApp != null)
        {
            YLongSet loggedInUsers = getLoggedInUsersContainerForWebApp(webApp);
            PK currentUserPk = getCurrentUserPk();
            if(currentUserPk != null)
            {
                synchronized(this)
                {
                    loggedInUsers.add(currentUserPk.getLongValue());
                }
            }
        }
    }


    private YLongSet getLoggedInUsersContainerForWebApp(String webApp)
    {
        YLongSet container = this.loggedBackOfficeUsers.get(webApp);
        if(container == null)
        {
            container = new YLongSet();
            YLongSet previousContainer = this.loggedBackOfficeUsers.putIfAbsent(webApp, container);
            if(previousContainer != null)
            {
                container = previousContainer;
            }
        }
        return container;
    }


    protected boolean isSendingStatsAgreedInLicense()
    {
        return Licence.getDefaultLicence().isMasterServerEnabled();
    }


    protected boolean isGenerateStatsRequired()
    {
        if(this.lastStatsSentAt == null)
        {
            synchronized(this)
            {
                if(this.lastStatsSentAt == null)
                {
                    this.lastStatsSentAt = DateTime.now();
                    return true;
                }
            }
        }
        DateTime now = DateTime.now();
        synchronized(this)
        {
            boolean isDateBefore = this.lastStatsSentAt.plusHours(8).isBefore((ReadableInstant)now);
            if(isDateBefore)
            {
                this.lastStatsSentAt = now;
                return true;
            }
        }
        return false;
    }


    protected PK getCurrentUserPk()
    {
        User user = null;
        try
        {
            user = (User)JaloSession.getCurrentSession().getAttribute("user");
        }
        catch(Exception e)
        {
            if(isLogExceptionsInDebug())
            {
                LOG.debug(e.getMessage(), e);
            }
        }
        return (user == null || "anonymous".equals(user.getUid())) ? null : user.getPK();
    }


    private Map<String, Map<String, Map<String, Object>>> getSessionStatistics()
    {
        try
        {
            Map<String, Map<String, Map<String, Object>>> result = new LinkedHashMap<>();
            Map<String, Object> userStats = new LinkedHashMap<>();
            for(Map.Entry<String, YLongSet> entry : this.loggedBackOfficeUsers.entrySet())
            {
                int numLoggedUsers = ((YLongSet)entry.getValue()).size();
                if(numLoggedUsers != 0)
                {
                    userStats.put(entry.getKey(), Integer.valueOf(numLoggedUsers));
                }
            }
            if(!userStats.isEmpty())
            {
                ImmutableMap<String, Map<String, Object>> overall = ImmutableMap.builder().put("backOfficeOverallUsers", userStats).build();
                result.put("session", overall);
                return result;
            }
            return null;
        }
        catch(Exception e)
        {
            return null;
        }
    }


    private Map<String, Map<String, Map<String, Object>>> getSystemStatistics()
    {
        if(this.systemStatistics == null)
        {
            this.systemStatistics = getStatisticsFor("system", (Set)this.systemCollectors);
        }
        return this.systemStatistics;
    }


    private Map<String, Map<String, Map<String, Object>>> getBusinessStatistics()
    {
        return getStatisticsFor("business", (Set)this.businessCollectors);
    }


    private Map<String, Map<String, Map<String, Object>>> getStatisticsFor(String statName, Set<? extends StatisticsCollector> statCollectors)
    {
        Map<String, Map<String, Map<String, Object>>> result = new LinkedHashMap<>();
        for(StatisticsCollector<Map<String, Map<String, Object>>> collector : statCollectors)
        {
            try
            {
                Map<String, Map<String, Object>> collectorResult = (Map<String, Map<String, Object>>)collector.collectStatistics();
                if(collectorResult != null)
                {
                    Map<String, Map<String, Object>> stat = result.get(statName);
                    if(stat == null)
                    {
                        result.put(statName, new LinkedHashMap<>());
                    }
                    ((Map<String, Map<String, Object>>)result.get(statName)).putAll(collectorResult);
                }
            }
            catch(Exception e)
            {
                if(isLogExceptionsInDebug())
                {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }
        return result;
    }


    private boolean isLogExceptionsInDebug()
    {
        return (Licence.getDefaultLicence().isDemoOrDevelopLicence() && LOG.isDebugEnabled());
    }


    private String toJson(Map input) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(input);
    }
}
