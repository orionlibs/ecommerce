package de.hybris.platform.ruleengineservices.setup.tasks.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengineservices.setup.tasks.MigrationTask;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorExecutionPolicy;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class InterceptorUnawareMigrationTaskDecorator implements MigrationTask
{
    private final MigrationTask migrationTask;
    private SessionService sessionService;


    public InterceptorUnawareMigrationTaskDecorator(MigrationTask migrationTask)
    {
        this.migrationTask = migrationTask;
    }


    public void execute(SystemSetupContext systemSetupContext)
    {
        ImmutableMap immutableMap = ImmutableMap.of("disable.interceptor.types",
                        ImmutableSet.of(InterceptorExecutionPolicy.InterceptorType.VALIDATE, InterceptorExecutionPolicy.InterceptorType.PREPARE));
        getSessionService().executeInLocalViewWithParams((Map)immutableMap, (SessionExecutionBody)new Object(this, systemSetupContext));
    }


    protected MigrationTask getMigrationTask()
    {
        return this.migrationTask;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
