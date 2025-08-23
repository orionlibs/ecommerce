package de.hybris.platform.masterserver.collector.system.impl;

import com.hybris.statistics.collector.SystemStatisticsCollector;
import de.hybris.platform.masterserver.collector.system.impl.spring.SpringConfigOverviewProvider;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringConfigOverviewCollector implements SystemStatisticsCollector<Map<String, Map<String, String>>>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfigOverviewCollector.class);
    static final Charset CONTENT_CHARSET = StandardCharsets.UTF_8;
    static final String OVERVIEW_OBJECT_KEY = "Spring";
    static final String OVERVIEW_STATS_KEY = "spring-beans";
    static final String OVERVIEW_STATS_ENCODING_KEY = "spring-beans-encoding";
    private final Supplier<String> springConfigOverviewProvider;
    private final BooleanSupplier isEnabled;
    private final Function<String, byte[]> compressor;


    public SpringConfigOverviewCollector()
    {
        this(new SpringConfigOverviewProvider()::getCurrentTenantCoreContextOverviewAsJson);
    }


    SpringConfigOverviewCollector(Supplier<String> springConfigOverviewProvider)
    {
        this(springConfigOverviewProvider, new Compressor()::compress, new Configuration()::isSpringOverviewCollectorEnabled);
    }


    SpringConfigOverviewCollector(Supplier<String> springConfigOverviewProvider, Function<String, byte[]> compressor, BooleanSupplier isEnabled)
    {
        this.springConfigOverviewProvider = Objects.<Supplier<String>>requireNonNull(springConfigOverviewProvider);
        this.compressor = Objects.<Function<String, byte[]>>requireNonNull(compressor);
        this.isEnabled = Objects.<BooleanSupplier>requireNonNull(isEnabled);
    }


    public Map<String, Map<String, String>> collectStatistics()
    {
        if(!this.isEnabled.getAsBoolean())
        {
            return Map.of();
        }
        return Map.of("Spring", Map.of("spring-beans", getCompressedSpringOverview(), "spring-beans-encoding", CONTENT_CHARSET.name()));
    }


    private String getCompressedSpringOverview()
    {
        try
        {
            Objects.requireNonNull(Base64.getEncoder());
            return Optional.<String>ofNullable(this.springConfigOverviewProvider.get()).map(String::strip).<byte[]>map((Function)this.compressor).map(Base64.getEncoder()::encodeToString)
                            .orElse("");
        }
        catch(RuntimeException e)
        {
            LOGGER.warn("Failed to obtain Spring Configuration Overview.", e);
            return "";
        }
    }
}
