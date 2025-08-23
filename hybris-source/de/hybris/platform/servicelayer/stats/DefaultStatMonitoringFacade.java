package de.hybris.platform.servicelayer.stats;

import de.hybris.platform.jalo.JaloConnection;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultStatMonitoringFacade implements MonitoringFacade
{
    private static final String MINUTES = " minutes";
    private static final String MINUTE = " minute";
    private static final String HOURS = " hours ";
    private static final String HOUR = " hour ";
    private static final String DAYS = " days ";
    private static final String DAY = " day ";
    private static final String WEEKS = " weeks ";
    private static final String WEEK = " week ";
    private static final String MONTHS = " months ";
    private static final String MONTH = " month ";
    @Value("#{T(java.lang.management.ManagementFactory).getRuntimeMXBean()}")
    private RuntimeMXBean runtimeMXBean;
    @Value("#{T(java.lang.management.ManagementFactory).getMemoryMXBean()}")
    private MemoryMXBean memoryMXBean;
    @Value("#{T(java.lang.management.ManagementFactory).getThreadMXBean()}")
    private ThreadMXBean threadMXBean;
    @Value("#{T(java.lang.management.ManagementFactory).getOperatingSystemMXBean()}")
    private OperatingSystemMXBean operatingSystemMXBean;
    @Value("#{T(de.hybris.platform.jalo.JaloConnection).getInstance()}")
    private JaloConnection jaloConnection;


    public Map<String, String> getSystemUptime()
    {
        Map<String, String> result = new HashMap<>();
        Period period = new Period(this.runtimeMXBean.getStartTime(), (new DateTime()).getMillis());
        PeriodFormatter formatter = (new PeriodFormatterBuilder()).appendMonths().appendSuffix(" month ", " months ").appendWeeks().appendSuffix(" week ", " weeks ").appendDays().appendSuffix(" day ", " days ").appendHours().appendSuffix(" hour ", " hours ").appendMinutes()
                        .appendSuffix(" minute", " minutes").toFormatter();
        result.put("prettyTime", formatter.withLocale(Locale.ENGLISH).print((ReadablePeriod)period));
        return result;
    }


    public Map<String, Object> getMemoryStat()
    {
        Map<String, Object> result = new HashMap<>();
        result.put("heap", this.memoryMXBean.getHeapMemoryUsage());
        result.put("nonHeap", this.memoryMXBean.getNonHeapMemoryUsage());
        return result;
    }


    public Map<String, Object> getThreadStat()
    {
        Map<String, Object> result = new HashMap<>();
        result.put("threadCount", Integer.valueOf(this.threadMXBean.getThreadCount()));
        result.put("peakThreadCount", Integer.valueOf(this.threadMXBean.getPeakThreadCount()));
        return result;
    }


    public Map<String, Object> getOsStat()
    {
        Map<String, Object> result = new HashMap<>();
        result.put("systemLoadAverage", Double.valueOf(this.operatingSystemMXBean.getSystemLoadAverage()));
        result.put("availableProcessors", Integer.valueOf(this.operatingSystemMXBean.getAvailableProcessors()));
        return result;
    }
}
