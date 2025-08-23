package de.hybris.platform.core.threadregistry;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jdbcwrapper.JdbcSuspendSupport;
import java.util.Objects;

public final class SuspendResumeServices
{
    private static final SuspendResumeServices INSTANCE;
    private final ThreadRegistry threadRegistry;
    private final JdbcSuspendSupport jdbcSuspendSupport;

    static
    {
        ThreadRegistry threadRegistry = new ThreadRegistry(() -> Registry.getMasterTenant().getConfig().getBoolean("debug.only.for.unregistered.threads", true));
        DefaultJdbcSuspendSupport defaultJdbcSuspendSupport = new DefaultJdbcSuspendSupport(threadRegistry);
        INSTANCE = new SuspendResumeServices(threadRegistry, (JdbcSuspendSupport)defaultJdbcSuspendSupport);
    }

    SuspendResumeServices(ThreadRegistry threadRegistry, JdbcSuspendSupport jdbcSuspendSupport)
    {
        this.threadRegistry = Objects.<ThreadRegistry>requireNonNull(threadRegistry);
        this.jdbcSuspendSupport = Objects.<JdbcSuspendSupport>requireNonNull(jdbcSuspendSupport);
    }


    public static SuspendResumeServices getInstance()
    {
        return INSTANCE;
    }


    public JdbcSuspendSupport getJdbcSuspendSupport()
    {
        return this.jdbcSuspendSupport;
    }


    ThreadRegistry getThreadRegistry()
    {
        return this.threadRegistry;
    }
}
