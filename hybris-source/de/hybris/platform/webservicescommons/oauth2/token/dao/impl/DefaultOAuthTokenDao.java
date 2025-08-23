package de.hybris.platform.webservicescommons.oauth2.token.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;
import de.hybris.platform.webservicescommons.model.OAuthRefreshTokenModel;
import de.hybris.platform.webservicescommons.oauth2.token.dao.OAuthTokenDao;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultOAuthTokenDao extends AbstractItemDao implements OAuthTokenDao
{
    private static final String FIND_ACCESS_TOKEN_BY_ID = "SELECT {pk} FROM {OAuthAccessToken} WHERE {tokenId} = ?id ";
    private static final String FIND_REFRESH_TOKEN_BY_ID = "SELECT {pk} FROM {OAuthRefreshToken} WHERE {tokenId} = ?id ";
    private static final String FIND_ACCESS_TOKEN_BY_AUTHENTICATION_ID = "SELECT {pk} FROM {OAuthAccessToken} WHERE {authenticationId} = ?id ";
    private static final String FIND_ACCESS_TOKEN_BY_REFRESH_TOKEN_ID = "SELECT {accessToken:pk} FROM {OAuthAccessToken as accessToken JOIN OAuthRefreshToken as refreshToken ON {accessToken:refreshToken} = {refreshToken:pk}} WHERE {refreshToken:tokenId} = ?id ";
    private static final String FIND_ACCESS_TOKEN_BY_CLIENT_ID = String.format("select {t.pk} from {%s as t join %s as c on {t.%s}={c.pk}} where {c.%s}=?clientId", new Object[] {"OAuthAccessToken", "OAuthClientDetails", "client", "clientId"});
    private static final String FIND_ACCESS_TOKEN_BY_USER_NAME = String.format("select {t.pk} from {%s as t join %s as u on {t.user}={u.pk}} where {u.%s}= ?username", new Object[] {"OAuthAccessToken", "User", "uid"});
    private static final String FIND_ACCESS_TOKEN_BY_CLIENT_AND_USER_NAME = String.format("select {t.pk} from {%s as t join %s as u on {t.%s}={u.pk} join %s as c on {t.%s}={c.pk}} where {u.%s}= ?username and {c.%s}= ?clientId",
                    new Object[] {"OAuthAccessToken", "User", "user", "OAuthClientDetails", "client", "uid", "clientId"});


    public OAuthAccessTokenModel findAccessTokenById(String id)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return (OAuthAccessTokenModel)searchUnique(new FlexibleSearchQuery("SELECT {pk} FROM {OAuthAccessToken} WHERE {tokenId} = ?id ", params));
    }


    public OAuthRefreshTokenModel findRefreshTokenById(String id)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return (OAuthRefreshTokenModel)searchUnique(new FlexibleSearchQuery("SELECT {pk} FROM {OAuthRefreshToken} WHERE {tokenId} = ?id ", params));
    }


    public OAuthAccessTokenModel findAccessTokenByRefreshTokenId(String refreshTokenId)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("id", refreshTokenId);
        return (OAuthAccessTokenModel)searchUnique(new FlexibleSearchQuery("SELECT {accessToken:pk} FROM {OAuthAccessToken as accessToken JOIN OAuthRefreshToken as refreshToken ON {accessToken:refreshToken} = {refreshToken:pk}} WHERE {refreshToken:tokenId} = ?id ", params));
    }


    public List<OAuthAccessTokenModel> findAccessTokenListByRefreshTokenId(String refreshTokenId)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("id", refreshTokenId);
        return doSearch("SELECT {accessToken:pk} FROM {OAuthAccessToken as accessToken JOIN OAuthRefreshToken as refreshToken ON {accessToken:refreshToken} = {refreshToken:pk}} WHERE {refreshToken:tokenId} = ?id ", params, OAuthAccessTokenModel.class);
    }


    public OAuthAccessTokenModel findAccessTokenByAuthenticationId(String authenticationId)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("id", authenticationId);
        return (OAuthAccessTokenModel)searchUnique(new FlexibleSearchQuery("SELECT {pk} FROM {OAuthAccessToken} WHERE {authenticationId} = ?id ", params));
    }


    public List<OAuthAccessTokenModel> findAccessTokenListForClient(String clientId)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("clientId", clientId);
        return doSearch(FIND_ACCESS_TOKEN_BY_CLIENT_ID, params, OAuthAccessTokenModel.class);
    }


    public List<OAuthAccessTokenModel> findAccessTokenListForUser(String userName)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        return doSearch(FIND_ACCESS_TOKEN_BY_USER_NAME, params, OAuthAccessTokenModel.class);
    }


    public List<OAuthAccessTokenModel> findAccessTokenListForClientAndUser(String clientId, String userName)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        params.put("clientId", clientId);
        return doSearch(FIND_ACCESS_TOKEN_BY_CLIENT_AND_USER_NAME, params, OAuthAccessTokenModel.class);
    }


    protected <T> List<T> doSearch(String query, Map<String, Object> params, Class<T> resultClass)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        if(params != null)
        {
            fQuery.addQueryParameters(params);
        }
        fQuery.setResultClassList(Collections.singletonList(resultClass));
        SearchResult<T> searchResult = search(fQuery);
        return searchResult.getResult();
    }
}
