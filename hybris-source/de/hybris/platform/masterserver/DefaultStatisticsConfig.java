package de.hybris.platform.masterserver;

import de.hybris.platform.util.Config;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import org.apache.commons.lang3.StringUtils;

class DefaultStatisticsConfig implements StatisticsConfig
{
    static final DefaultStatisticsConfig INSTANCE = new DefaultStatisticsConfig(Config::isCloudEnvironment);
    static final String TOMCAT_STATISTICS_ENABLED = "tomcat.statistics.enabled";
    private final BooleanSupplier cloudEnvironmentChecker;


    DefaultStatisticsConfig(BooleanSupplier cloudEnvironmentChecker)
    {
        this.cloudEnvironmentChecker = Objects.<BooleanSupplier>requireNonNull(cloudEnvironmentChecker);
    }


    public boolean isStatisticsPublisherEnabled()
    {
        String strValue = Config.getParameter("tomcat.statistics.enabled");
        if(StringUtils.isEmpty(strValue))
        {
            return this.cloudEnvironmentChecker.getAsBoolean();
        }
        return Config.getBoolean("tomcat.statistics.enabled", this.cloudEnvironmentChecker.getAsBoolean());
    }
}
