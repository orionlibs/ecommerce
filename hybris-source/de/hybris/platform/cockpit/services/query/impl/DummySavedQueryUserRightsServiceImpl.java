package de.hybris.platform.cockpit.services.query.impl;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.services.query.SavedQueryService;
import de.hybris.platform.cockpit.services.query.SavedQueryUserRightsService;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummySavedQueryUserRightsServiceImpl implements SavedQueryUserRightsService
{
    private static final Logger LOG = LoggerFactory.getLogger(DummySavedQueryUserRightsServiceImpl.class);
    private static final String SERVICE_WARN = SavedQueryUserRightsService.class.getSimpleName() + " interface was initially intended for use in product cockpit and print cockpit.";
    private final SavedQueryService savedQueryService;


    public DummySavedQueryUserRightsServiceImpl(SavedQueryService savedQueryService)
    {
        this.savedQueryService = savedQueryService;
    }


    public Collection<CockpitSavedQueryModel> getSavedQueries(SearchProvider provider, UserModel user)
    {
        return this.savedQueryService.getSavedQueries(provider, user);
    }


    public CockpitSavedQueryModel createSavedQuery(String label, Query query, UserModel user)
    {
        return this.savedQueryService.createSavedQuery(label, query, user);
    }


    public Query getQuery(CockpitSavedQueryModel savedQuery)
    {
        return this.savedQueryService.getQuery(savedQuery);
    }


    public void deleteQuery(CockpitSavedQueryModel query)
    {
        this.savedQueryService.deleteQuery(query);
    }


    public void renameQuery(CockpitSavedQueryModel query, String label)
    {
        this.savedQueryService.renameQuery(query, label);
    }


    public void storeUpdates(CockpitSavedQueryModel query)
    {
        this.savedQueryService.storeUpdates(query);
    }


    public void publishSavedQuery(CockpitSavedQueryModel query)
    {
        this.savedQueryService.publishSavedQuery(query);
    }


    public void addReadUser(PrincipalModel user, CockpitSavedQueryModel model)
    {
        LOG.warn(SERVICE_WARN);
    }


    public void removeReadUser(PrincipalModel user, CockpitSavedQueryModel model)
    {
        LOG.warn(SERVICE_WARN);
    }


    public List<PrincipalModel> getReadUsersForSavedQuery(CockpitSavedQueryModel model)
    {
        LOG.warn(SERVICE_WARN);
        return Collections.EMPTY_LIST;
    }


    public Collection<CockpitSavedQueryModel> getSharedQueries(SearchProvider provider, UserModel user)
    {
        LOG.warn(SERVICE_WARN);
        return Collections.EMPTY_LIST;
    }


    public Collection<CockpitSavedQueryModel> getNotSharedQueries(SearchProvider provider, UserModel user)
    {
        LOG.warn(SERVICE_WARN);
        return Collections.EMPTY_LIST;
    }
}
