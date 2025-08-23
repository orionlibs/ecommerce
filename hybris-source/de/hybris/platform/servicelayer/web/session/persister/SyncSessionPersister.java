package de.hybris.platform.servicelayer.web.session.persister;

import de.hybris.platform.core.model.web.StoredHttpSessionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.web.session.HybrisDeserializer;
import de.hybris.platform.servicelayer.web.session.PersistedSession;
import de.hybris.platform.servicelayer.web.session.StoredHttpSessionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.serializer.Deserializer;

public class SyncSessionPersister extends AbstractSessionPersister implements SessionPersister
{
    private static final Logger LOG = LoggerFactory.getLogger(SyncSessionPersister.class);
    private ModelService modelService;
    private StoredHttpSessionDao storedHttpSessionDao;


    public void persist(PersistedSession persistedSession)
    {
        StoredHttpSessionModel storedHttpSession = this.storedHttpSessionDao.findOrCreate(persistedSession.getId());
        if(updateModelIfNecessary(storedHttpSession, persistedSession))
        {
            this.modelService.save(storedHttpSession);
            LOG.debug("Session persister saved {}", persistedSession);
        }
    }


    public void remove(String id)
    {
        this.storedHttpSessionDao.deleteSession(id);
        LOG.debug("Removed session with id {}", id);
    }


    public PersistedSession load(String id, Deserializer deSerializer)
    {
        StoredHttpSessionModel storedHttpSessionModel = this.storedHttpSessionDao.findById(id);
        if(storedHttpSessionModel != null)
        {
            return HybrisDeserializer.deserialize((byte[])storedHttpSessionModel.getSerializedSession(), deSerializer);
        }
        return null;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setStoredHttpSessionDao(StoredHttpSessionDao storedHttpSessionDao)
    {
        this.storedHttpSessionDao = storedHttpSessionDao;
    }
}
