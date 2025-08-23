package de.hybris.platform.personalizationservices.action.dao.impl;

import de.hybris.platform.personalizationservices.action.dao.CxActionResultDao;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.model.CxResultsModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultCxActionResultDao extends AbstractCxDao<CxResultsModel> implements CxActionResultDao
{
    private static final String SELECT_RESULTS_BY_KEY = "SELECT {pk} FROM {CxResults} WHERE {key} = ?key ORDER BY {calculationTime} DESC";
    private static final String SELECT_RESULTS_BY_SESSION_KEY = "SELECT {pk} FROM {CxResults} WHERE {sessionKey} = ?sessionKey";
    private static final String KEY_PARAM = "key";
    private static final String SESSION_KEY_PARAM = "sessionKey";


    public DefaultCxActionResultDao()
    {
        super("CxResults");
    }


    public Optional<CxResultsModel> findResultsByKey(String key)
    {
        List<CxResultsModel> results = findAllResultsByKey(key);
        if(CollectionUtils.isNotEmpty(results))
        {
            return Optional.of(results.get(0));
        }
        return Optional.empty();
    }


    public List<CxResultsModel> findAllResultsByKey(String key)
    {
        ServicesUtil.validateParameterNotNull(key, "Results key must not be null");
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        return queryList("SELECT {pk} FROM {CxResults} WHERE {key} = ?key ORDER BY {calculationTime} DESC", params);
    }


    public List<CxResultsModel> findResultsBySessionKey(String sessionKey)
    {
        ServicesUtil.validateParameterNotNull(sessionKey, "Results session key must not be null");
        Map<String, Object> params = new HashMap<>();
        params.put("sessionKey", sessionKey);
        return queryList("SELECT {pk} FROM {CxResults} WHERE {sessionKey} = ?sessionKey", params);
    }
}
