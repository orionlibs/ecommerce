package de.hybris.platform.ruleengineservices.maintenance.impl;

import de.hybris.platform.ruleengine.concurrency.GuardStatus;
import de.hybris.platform.ruleengine.concurrency.GuardedSuspension;
import de.hybris.platform.ruleengineservices.maintenance.RulesCompilationInProgressQueryEvent;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;

public class RulesCompilationGuardedSuspension implements GuardedSuspension<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RulesCompilationGuardedSuspension.class);
    protected static final String VERIFICATION_LATCH_TIMEOUT = "ruleengineservices.compiler.guarded.suspension.timeout";
    private EventService eventService;
    private ConfigurationService configurationService;


    public GuardStatus checkPreconditions(String moduleName)
    {
        CountDownLatch verificationLatch = new CountDownLatch(1);
        GuardStatus guardStatus = GuardStatus.of(GuardStatus.Type.GO);
        Object object = new Object(this, moduleName, guardStatus, verificationLatch);
        try
        {
            Long latchTimeout = getConfigurationService().getConfiguration().getLong("ruleengineservices.compiler.guarded.suspension.timeout", Long.valueOf(2L));
            LOGGER.debug("Registering the application listener on RulesCompilationInProgressResponseEvent");
            getEventService().registerEventListener((ApplicationListener)object);
            getEventService().publishEvent((AbstractEvent)new RulesCompilationInProgressQueryEvent(moduleName));
            LOGGER.debug("Waiting for the verificationLatch for [{}] seconds", latchTimeout);
            verificationLatch.await(latchTimeout.longValue(), TimeUnit.SECONDS);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        finally
        {
            LOGGER.debug("Unregistering the application listener on RulesCompilationInProgressResponseEvent");
            getEventService().unregisterEventListener((ApplicationListener)object);
        }
        return guardStatus;
    }


    protected EventService getEventService()
    {
        return this.eventService;
    }


    @Required
    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
