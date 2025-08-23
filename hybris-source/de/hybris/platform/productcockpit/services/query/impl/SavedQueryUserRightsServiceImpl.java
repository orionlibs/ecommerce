package de.hybris.platform.productcockpit.services.query.impl;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.services.query.SavedQueryUserRightsService;
import de.hybris.platform.cockpit.services.query.daos.SavedQueryUserRightsDao;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SavedQueryUserRightsServiceImpl extends ProductSavedQueryServiceImpl implements SavedQueryUserRightsService
{
    public Collection<CockpitSavedQueryModel> getSavedQueries(SearchProvider provider, UserModel user)
    {
        Collection<CockpitSavedQueryModel> ret = new ArrayList<>();
        ret.addAll(getSavedQueryDao().findReadableSavedQueriesByUser(user));
        return ret;
    }


    public void addReadUser(PrincipalModel user, CockpitSavedQueryModel model)
    {
        ServicesUtil.validateParameterNotNull(user, "Parameter 'user' cannot be null");
        ServicesUtil.validateParameterNotNull(model, "Given 'model' must not be null");
        if(!user.getReadSavedQueries().contains(model))
        {
            Collection<CockpitSavedQueryModel> userReadSavedQueries = new ArrayList<>(user.getReadSavedQueries());
            userReadSavedQueries.add(model);
            user.setReadSavedQueries(userReadSavedQueries);
            getModelService().save(user);
        }
    }


    public void removeReadUser(PrincipalModel user, CockpitSavedQueryModel model)
    {
        ServicesUtil.validateParameterNotNull(user, "Parameter 'user' cannot be null");
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' cannot be null");
        if(user.getReadSavedQueries().contains(model))
        {
            Collection<CockpitSavedQueryModel> userReadSavedQueries = new ArrayList<>(user.getReadSavedQueries());
            userReadSavedQueries.remove(model);
            user.setReadSavedQueries(userReadSavedQueries);
            getModelService().save(user);
        }
    }


    public List<PrincipalModel> getReadUsersForSavedQuery(CockpitSavedQueryModel model)
    {
        return new ArrayList<>(model.getReadSavedQueryPrincipals());
    }


    protected SavedQueryUserRightsDao getSavedQueryDao()
    {
        return (SavedQueryUserRightsDao)super.getSavedQueryDao();
    }


    public Collection<CockpitSavedQueryModel> getSharedQueries(SearchProvider provider, UserModel user)
    {
        Collection<CockpitSavedQueryModel> ret = new ArrayList<>();
        ret.addAll(getSavedQueryDao().findSharedQueriesByUser(user));
        return ret;
    }


    public Collection<CockpitSavedQueryModel> getNotSharedQueries(SearchProvider provider, UserModel user)
    {
        Collection<CockpitSavedQueryModel> ret = new ArrayList<>();
        ret.addAll(getSavedQueryDao().findNotSharedQueriesByUser(user));
        return ret;
    }
}
