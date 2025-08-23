package de.hybris.platform.webservicescommons.oauth2.token.dao;

import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;
import de.hybris.platform.webservicescommons.model.OAuthRefreshTokenModel;
import java.util.List;

public interface OAuthTokenDao extends Dao
{
    OAuthAccessTokenModel findAccessTokenById(String paramString);


    OAuthRefreshTokenModel findRefreshTokenById(String paramString);


    OAuthAccessTokenModel findAccessTokenByRefreshTokenId(String paramString);


    OAuthAccessTokenModel findAccessTokenByAuthenticationId(String paramString);


    List<OAuthAccessTokenModel> findAccessTokenListByRefreshTokenId(String paramString);


    List<OAuthAccessTokenModel> findAccessTokenListForClient(String paramString);


    List<OAuthAccessTokenModel> findAccessTokenListForUser(String paramString);


    List<OAuthAccessTokenModel> findAccessTokenListForClientAndUser(String paramString1, String paramString2);
}
