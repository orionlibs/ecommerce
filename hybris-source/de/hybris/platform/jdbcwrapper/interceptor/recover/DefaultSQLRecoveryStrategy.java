package de.hybris.platform.jdbcwrapper.interceptor.recover;

import com.google.common.base.CharMatcher;
import com.google.common.base.Converter;
import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptorContext;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.config.ConfigIntf;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.sql.DataSource;

public class DefaultSQLRecoveryStrategy implements SQLRecoveryStrategy
{
    static final String INITIAL_BACKOFF = "jdbc.recovery.backoff.initial.seconds";
    static final String INCREASE_BACKOFF_FACTOR = "jdbc.recovery.backoff.increase.factor";
    static final String MAX_BACKOFF = "jdbc.recovery.backoff.max.seconds";
    private final Map<Config.DatabaseName, Set<Integer>> errorCodesOfRecoverableExceptions;
    private final Map<Config.DatabaseName, Set<Integer>> errorCodesOfNonRecoverableExceptions;
    private final ConfigIntf config;


    public DefaultSQLRecoveryStrategy(ConfigIntf config)
    {
        this.config = Objects.<ConfigIntf>requireNonNull(config, "config mustn't be null.");
        this.errorCodesOfRecoverableExceptions = mergeDBSpecificWithCommonErrorCodes(config, DefaultSQLRecoveryStrategy::recoverableErrorCodesKey);
        this.errorCodesOfNonRecoverableExceptions = mergeDBSpecificWithCommonErrorCodes(config, DefaultSQLRecoveryStrategy::nonRecoverableErrorCodesKey);
    }


    public Duration calculateBackoffDuration(int numberOfIterations)
    {
        if(numberOfIterations < 0)
        {
            throw new IllegalArgumentException("numberOfIterations mustn't be negative.");
        }
        return Duration.ofSeconds(
                        (long)Math.min(getInitialBackoffSeconds() * Math.pow(getBackoffIncreaseFactor(), numberOfIterations),
                                        getMaxBackoffSeconds()));
    }


    public boolean isRecoverable(SQLException e)
    {
        Config.DatabaseName dbType = getDataBaseType();
        if(dbType == null)
        {
            return false;
        }
        if(e instanceof java.sql.SQLRecoverableException)
        {
            Set<Integer> dbSpecificNonRecoverableErrorCodes = this.errorCodesOfNonRecoverableExceptions.getOrDefault(dbType,
                            Collections.emptySet());
            return !dbSpecificNonRecoverableErrorCodes.contains(Integer.valueOf(e.getErrorCode()));
        }
        Set<Integer> dbSpecificRecoverableErrorCodes = this.errorCodesOfRecoverableExceptions.getOrDefault(dbType,
                        Collections.emptySet());
        return dbSpecificRecoverableErrorCodes.contains(Integer.valueOf(e.getErrorCode()));
    }


    public boolean canRecover(JDBCInterceptorContext ctx)
    {
        return ctx.matchesMethod(DataSource.class, "getConnection");
    }


    protected int getInitialBackoffSeconds()
    {
        return this.config.getInt("jdbc.recovery.backoff.initial.seconds", 10);
    }


    protected double getBackoffIncreaseFactor()
    {
        return this.config.getDouble("jdbc.recovery.backoff.increase.factor", 1.3333333333333333D);
    }


    protected int getMaxBackoffSeconds()
    {
        return this.config.getInt("jdbc.recovery.backoff.max.seconds", 60);
    }


    protected Config.DatabaseName getDataBaseType()
    {
        if(Registry.isCurrentTenantStarted())
        {
            return Config.getDatabaseName();
        }
        return null;
    }


    protected static Map<Config.DatabaseName, Set<Integer>> mergeDBSpecificWithCommonErrorCodes(ConfigIntf config, Function<Config.DatabaseName, String> errorCodesKeyForDB)
    {
        Map<Config.DatabaseName, Set<Integer>> result = new EnumMap<>(Config.DatabaseName.class);
        Objects.requireNonNull(config);
        Set<Integer> commonCodes = toSetOfInts((String)errorCodesKeyForDB.andThen(config::getParameter).apply(null));
        for(Config.DatabaseName db : Config.DatabaseName.values())
        {
            Set<Integer> dbSpecificCodes = new HashSet<>(commonCodes);
            Objects.requireNonNull(config);
            dbSpecificCodes.addAll(toSetOfInts(errorCodesKeyForDB.<String>andThen(config::getParameter).apply(db)));
            result.put(db, dbSpecificCodes);
        }
        return result;
    }


    protected static final String recoverableErrorCodesKey(Config.DatabaseName name)
    {
        return String.format("jdbc.recovery.%s.recoverable.error.codes", new Object[] {(name == null) ? "common" : name.getName()});
    }


    protected static final String nonRecoverableErrorCodesKey(Config.DatabaseName name)
    {
        return String.format("jdbc.recovery.%s.nonrecoverable.error.codes", new Object[] {(name == null) ? "common" : name.getName()});
    }


    protected static Set<Integer> toSetOfInts(String intsToSplit)
    {
        if(intsToSplit == null)
        {
            return Collections.emptySet();
        }
        CharMatcher matcher = CharMatcher.anyOf(",;").or(CharMatcher.whitespace());
        Converter<String, Integer> converter = Ints.stringConverter();
        Objects.requireNonNull(converter);
        return (Set<Integer>)Splitter.on(matcher).splitToList(intsToSplit).stream().filter(s -> !s.trim().isEmpty()).map(converter::convert)
                        .collect(Collectors.toSet());
    }
}
