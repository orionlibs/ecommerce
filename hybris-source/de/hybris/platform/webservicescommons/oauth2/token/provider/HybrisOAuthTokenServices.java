package de.hybris.platform.webservicescommons.oauth2.token.provider;

import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

public class HybrisOAuthTokenServices extends DefaultTokenServices
{
    private static final Logger LOG = Logger.getLogger(HybrisOAuthTokenServices.class);


    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException
    {
        try
        {
            return transactionalCreateAccessToken(authentication);
        }
        catch(ModelSavingException e)
        {
            LOG.debug("HybrisOAuthTokenServices->createAccessToken : ModelSavingException : " + e.getMessage());
            return super.createAccessToken(authentication);
        }
        catch(ModelRemovalException | de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException e)
        {
            LOG.debug("HybrisOAuthTokenServices->createAccessToken : ModelRemovalException | UnknownIdentifierException : " + e
                            .getMessage());
            return super.createAccessToken(authentication);
        }
        catch(Exception e)
        {
            LOG.debug("HybrisOAuthTokenServices->createAccessToken : " + e.getClass().getName() + " : " + e.getMessage());
            return super.createAccessToken(authentication);
        }
    }


    private OAuth2AccessToken transactionalCreateAccessToken(OAuth2Authentication authentication) throws Exception
    {
        Object txAccessToken = Transaction.current().execute((TransactionBody)new Object(this, authentication));
        return (OAuth2AccessToken)txAccessToken;
    }


    private OAuth2AccessToken createAccessTokenSuperClass(OAuth2Authentication authentication)
    {
        return super.createAccessToken(authentication);
    }
}
