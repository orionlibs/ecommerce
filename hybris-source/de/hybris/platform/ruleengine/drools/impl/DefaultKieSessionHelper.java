package de.hybris.platform.ruleengine.drools.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.RuleExecutionCountListener;
import de.hybris.platform.ruleengine.drools.KieSessionHelper;
import de.hybris.platform.ruleengine.enums.DroolsSessionType;
import de.hybris.platform.ruleengine.exception.RuleEngineRuntimeException;
import de.hybris.platform.ruleengine.model.DroolsKIESessionModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PreDestroy;
import org.apache.commons.collections.CollectionUtils;
import org.drools.core.event.DebugAgendaEventListener;
import org.drools.core.event.DebugRuleRuntimeEventListener;
import org.kie.api.event.KieRuntimeEventManager;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionsPool;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultKieSessionHelper<T> extends DefaultModuleReleaseIdAware implements KieSessionHelper<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKieSessionHelper.class);
    private Class<? extends RuleExecutionCountListener> ruleExecutionCounterClass;
    private final Map<KieContainer, KieSessionsPool> kieContainerSessionsPools = new ConcurrentHashMap<>();
    private int kieSessionPoolInitialCapacity;
    boolean keepOnlyOneSessionPoolVersion = true;
    boolean kieSessionPoolEnabled = true;


    public T initializeSession(Class<T> kieSessionClass, RuleEvaluationContext context, KieContainer kieContainer)
    {
        assertKieSessionClass(kieSessionClass);
        DroolsRuleEngineContextModel ruleEngineContext = validateRuleEvaluationContext(context);
        return KieSession.class.isAssignableFrom(kieSessionClass) ?
                        (T)initializeKieSessionInternal(context, ruleEngineContext, kieContainer) :
                        (T)initializeStatelessKieSessionInternal(context, ruleEngineContext, kieContainer);
    }


    protected KieSession initializeKieSessionInternal(RuleEvaluationContext context, DroolsRuleEngineContextModel ruleEngineContext, KieContainer kieContainer)
    {
        DroolsKIESessionModel kieSession = ruleEngineContext.getKieSession();
        assertSessionIsStateful(kieSession);
        KieSession session = isKieSessionPoolEnabled() ? getKieContainerSessionsPool(kieContainer, false).newKieSession() : kieContainer.newKieSession(kieSession.getName());
        if(Objects.nonNull(context.getGlobals()))
        {
            Objects.requireNonNull(session);
            context.getGlobals().forEach(session::setGlobal);
        }
        registerKieSessionListeners(context, (KieRuntimeEventManager)session, ruleEngineContext.getRuleFiringLimit());
        return session;
    }


    protected StatelessKieSession initializeStatelessKieSessionInternal(RuleEvaluationContext context, DroolsRuleEngineContextModel ruleEngineContext, KieContainer kieContainer)
    {
        DroolsKIESessionModel kieSession = ruleEngineContext.getKieSession();
        assertSessionIsStateless(kieSession);
        StatelessKieSession session = isKieSessionPoolEnabled() ? getKieContainerSessionsPool(kieContainer, true).newStatelessKieSession() : kieContainer.newStatelessKieSession(kieSession.getName());
        if(Objects.nonNull(context.getGlobals()))
        {
            Objects.requireNonNull(session);
            context.getGlobals().forEach(session::setGlobal);
        }
        registerKieSessionListeners(context, (KieRuntimeEventManager)session, ruleEngineContext.getRuleFiringLimit());
        return session;
    }


    protected void assertKieSessionClass(Class<T> kieSessionClass)
    {
        Preconditions.checkArgument((KieSession.class
                        .isAssignableFrom(kieSessionClass) || StatelessKieSession.class.isAssignableFrom(kieSessionClass)), "No other session types other than KieSession and StatelessKieSession are supported");
    }


    protected void assertSessionIsStateless(DroolsKIESessionModel kieSession)
    {
        Preconditions.checkArgument(kieSession.getSessionType().equals(DroolsSessionType.STATELESS), "Expected STATELESS session type here. Check the invocation parameters");
    }


    protected void assertSessionIsStateful(DroolsKIESessionModel kieSession)
    {
        Preconditions.checkArgument(kieSession.getSessionType().equals(DroolsSessionType.STATEFUL), "Expected STATEFUL session type here. Check the invocation parameters");
    }


    protected void registerKieSessionListeners(RuleEvaluationContext context, KieRuntimeEventManager session, Long maximumExecutions)
    {
        if(CollectionUtils.isNotEmpty(context.getEventListeners()))
        {
            for(Object listener : context.getEventListeners())
            {
                if(listener instanceof AgendaEventListener)
                {
                    session.addEventListener((AgendaEventListener)listener);
                    continue;
                }
                if(listener instanceof RuleRuntimeEventListener)
                {
                    session.addEventListener((RuleRuntimeEventListener)listener);
                    continue;
                }
                if(listener instanceof ProcessEventListener)
                {
                    session.addEventListener((ProcessEventListener)listener);
                    continue;
                }
                throw new IllegalArgumentException("context.eventListeners attribute must only contain instances of the types org.kie.api.event.rule.AgendaEventListener, org.kie.api.event.process.ProcessEventListener or org.kie.api.event.rule.RuleRuntimeEventListener");
            }
        }
        if(Objects.nonNull(getRuleExecutionCounterClass()) && Objects.nonNull(maximumExecutions))
        {
            RuleExecutionCountListener listener = createRuleExecutionCounterListener();
            listener.setExecutionLimit(maximumExecutions.longValue());
            session.addEventListener((AgendaEventListener)listener);
        }
        if(LOGGER.isDebugEnabled())
        {
            session.addEventListener((RuleRuntimeEventListener)new DebugRuleRuntimeEventListener());
            session.addEventListener((AgendaEventListener)new DebugAgendaEventListener());
        }
    }


    protected RuleExecutionCountListener createRuleExecutionCounterListener()
    {
        try
        {
            return getRuleExecutionCounterClass().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch(InstantiationException | IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e)
        {
            throw new RuleEngineRuntimeException(e);
        }
    }


    @PreDestroy
    protected void teardownSessionsPools()
    {
        getKieSessionsPools().values().forEach(KieSessionsPool::shutdown);
    }


    protected KieSessionsPool getKieContainerSessionsPool(KieContainer kieContainer, boolean stateless)
    {
        return getKieSessionsPools().computeIfAbsent(kieContainer, container -> stateless ? container.getKieBase().newKieSessionsPool(getKieSessionPoolInitialCapacity()) : (KieSessionsPool)container.newKieSessionsPool(getKieSessionPoolInitialCapacity()));
    }


    public void shutdownKieSessionPools(String moduleName, String version)
    {
        if(!isKeepOnlyOneSessionPoolVersion())
        {
            return;
        }
        LOGGER.debug("removing old kie session pools for kie container with artifactId:{} version:{}", moduleName, version);
        boolean removed = false;
        for(Iterator<Map.Entry<KieContainer, KieSessionsPool>> it = getKieSessionsPools().entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<KieContainer, KieSessionsPool> e = it.next();
            KieContainer c = e.getKey();
            boolean match = (c.getReleaseId().getArtifactId().equals(moduleName) && !c.getReleaseId().getVersion().equals(version));
            if(match)
            {
                KieSessionsPool pool = e.getValue();
                it.remove();
                pool.shutdown();
            }
        }
    }


    protected Class<? extends RuleExecutionCountListener> getRuleExecutionCounterClass()
    {
        return this.ruleExecutionCounterClass;
    }


    protected Map<KieContainer, KieSessionsPool> getKieSessionsPools()
    {
        return this.kieContainerSessionsPools;
    }


    public void setRuleExecutionCounterClass(Class<? extends RuleExecutionCountListener> ruleExecutionCounterClass)
    {
        this.ruleExecutionCounterClass = ruleExecutionCounterClass;
    }


    protected int getKieSessionPoolInitialCapacity()
    {
        return this.kieSessionPoolInitialCapacity;
    }


    public void setKieSessionPoolInitialCapacity(int kieSessionPoolInitialCapacity)
    {
        this.kieSessionPoolInitialCapacity = kieSessionPoolInitialCapacity;
    }


    protected boolean isKeepOnlyOneSessionPoolVersion()
    {
        return this.keepOnlyOneSessionPoolVersion;
    }


    public void setKeepOnlyOneSessionPoolVersion(boolean keepOnlyOneSessionPoolVersion)
    {
        this.keepOnlyOneSessionPoolVersion = keepOnlyOneSessionPoolVersion;
    }


    protected boolean isKieSessionPoolEnabled()
    {
        return this.kieSessionPoolEnabled;
    }


    public void setKieSessionPoolEnabled(boolean kieSessionPoolEnabled)
    {
        this.kieSessionPoolEnabled = kieSessionPoolEnabled;
    }
}
