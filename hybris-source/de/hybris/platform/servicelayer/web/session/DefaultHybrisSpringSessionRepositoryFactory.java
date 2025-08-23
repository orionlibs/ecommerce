package de.hybris.platform.servicelayer.web.session;

import de.hybris.platform.servicelayer.web.session.persister.SessionFilteringStrategy;
import de.hybris.platform.servicelayer.web.session.persister.SessionPersister;
import de.hybris.platform.util.Config;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.serializer.Deserializer;
import org.springframework.session.SessionRepository;

public class DefaultHybrisSpringSessionRepositoryFactory implements HybrisSpringSessionRepositoryFactory
{
    private SessionPersister synchronousSessionPersister;
    private SessionPersister asynchronousSessionPersister;
    private static final EmptySessionFilteringStrategy EMPTY_FILTERING_STRATEGY = new EmptySessionFilteringStrategy();
    @Autowired
    private Map<String, SessionFilteringStrategy> sessionFilteringStrategies;


    public SessionRepository createRepository(Deserializer deSerializer, String extension, String contextRoot)
    {
        SessionPersister persister = getConfiguredSessionPersister(extension);
        if(persister == null)
        {
            return null;
        }
        FilteringSessionPersister filteringSessionPersister = new FilteringSessionPersister(persister, getSessionFilterStrategy(extension));
        return (SessionRepository)new CachedPersistedSessionRepository(deSerializer, (SessionPersister)filteringSessionPersister, extension, contextRoot);
    }


    protected SessionPersister getConfiguredSessionPersister(String extension)
    {
        String saveStrategy = Config.getParameter("spring.session." + extension + ".save");
        if("sync".equalsIgnoreCase(saveStrategy))
        {
            return this.synchronousSessionPersister;
        }
        if("async".equalsIgnoreCase(saveStrategy))
        {
            return this.asynchronousSessionPersister;
        }
        return null;
    }


    protected SessionFilteringStrategy getSessionFilterStrategy(String extension)
    {
        String sessionFilteringStrategyName = Config.getParameter("spring.session." + extension + ".filtering.strategy");
        SessionFilteringStrategy sessionFilteringStrategy = this.sessionFilteringStrategies.get(sessionFilteringStrategyName);
        return (sessionFilteringStrategy != null) ? sessionFilteringStrategy : (SessionFilteringStrategy)EMPTY_FILTERING_STRATEGY;
    }


    @Required
    public void setSynchronousSessionPersister(SessionPersister synchronousSessionPersister)
    {
        this.synchronousSessionPersister = synchronousSessionPersister;
    }


    @Required
    public void setAsynchronousSessionPersister(SessionPersister asynchronousSessionPersister)
    {
        this.asynchronousSessionPersister = asynchronousSessionPersister;
    }
}
