package de.hybris.platform.servicelayer.web.session;

import de.hybris.platform.core.model.web.StoredHttpSessionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class StoredHttpSessionDao
{
    private static final Logger LOG = LoggerFactory.getLogger(StoredHttpSessionDao.class);
    private static final String STORED_SESSION_BY_ID_QRY = "SELECT {PK} FROM {StoredHttpSession} WHERE {sessionId} = ?id";
    private static final String STORED_SESSION_BY_IDS_QRY = "SELECT {PK} FROM {StoredHttpSession} WHERE {sessionId} IN (?ids)";
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;


    public StoredHttpSessionModel findById(String id)
    {
        SearchResult<StoredHttpSessionModel> result = this.flexibleSearchService.search("SELECT {PK} FROM {StoredHttpSession} WHERE {sessionId} = ?id",
                        Collections.singletonMap("id", id));
        if(result.getCount() == 0)
        {
            return null;
        }
        if(result.getCount() == 1)
        {
            return result.getResult().get(0);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Multiple items found for session id: {}", id);
        }
        else
        {
            LOG.warn("Multiple items found for session. Switch logger to debug to see the id.");
        }
        return result.getResult().get(0);
    }


    public Collection<StoredHttpSessionModel> findByIds(Collection<String> ids)
    {
        if(CollectionUtils.isEmpty(ids))
        {
            return Collections.emptyList();
        }
        SearchResult<StoredHttpSessionModel> sr = this.flexibleSearchService.search("SELECT {PK} FROM {StoredHttpSession} WHERE {sessionId} IN (?ids)",
                        Collections.singletonMap("ids", ids));
        return sr.getResult();
    }


    public StoredHttpSessionModel findOrCreate(String id)
    {
        StoredHttpSessionModel storedHttpSession = findById(id);
        if(storedHttpSession == null)
        {
            StoredHttpSessionModel newStoredHttpSession = (StoredHttpSessionModel)this.modelService.create(StoredHttpSessionModel.class);
            newStoredHttpSession.setSessionId(id);
            return newStoredHttpSession;
        }
        return storedHttpSession;
    }


    public void deleteSession(String id)
    {
        StoredHttpSessionModel byId = findById(id);
        if(byId != null)
        {
            this.modelService.remove(byId);
        }
    }


    public void deleteSessions(Collection<String> ids)
    {
        Collection<StoredHttpSessionModel> models = findByIds(ids);
        if(CollectionUtils.isNotEmpty(models))
        {
            this.modelService.removeAll(models);
        }
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
