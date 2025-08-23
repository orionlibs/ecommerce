package de.hybris.platform.jalo.user;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeEvent;
import de.hybris.platform.util.Base64;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTokenService implements TokenService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTokenService.class);
    private static final int RANDOM_LENGTH = 16;
    private static final Random RANDOM = new SecureRandom();
    private final ModelService modelService;
    private final UserDao userDao;


    public DefaultTokenService(ModelService modelService, UserDao userDao)
    {
        this.modelService = modelService;
        this.userDao = userDao;
    }


    public String getOrCreateTokenForUser(String userId)
    {
        Objects.requireNonNull(userId);
        User user = getUserById(userId);
        String tokenValue = getTokenValue(user);
        if(tokenValue == null)
        {
            synchronized(user)
            {
                tokenValue = getTokenValue(user);
                if(tokenValue == null)
                {
                    tokenValue = getNextRandomBase64EncodedWithCurrentTimestamp();
                    setTokenValue(user, tokenValue);
                }
            }
        }
        LOG.debug("Getting token value for user {} ", userId);
        return tokenValue;
    }


    private User getUserById(String userId)
    {
        UserModel uModel = this.userDao.findUserByUID(userId);
        return (User)this.modelService.getSource(uModel);
    }


    private String getTokenValue(User user)
    {
        return user.getRandomToken();
    }


    private void setTokenValue(User user, String tokenValue)
    {
        user.setRandomToken(tokenValue);
    }


    public boolean revokeTokenForUser(String userId)
    {
        Objects.requireNonNull(userId);
        LOG.debug("Revoking token for user {} ", userId);
        User user = getUserById(userId);
        setTokenValue(user, getNextRandomBase64EncodedWithCurrentTimestamp());
        User.getPasswordChangeListener().passwordChanged(new PasswordChangeEvent(userId));
        return true;
    }


    public boolean checkIfTokenIsValid(String userId, String tokenToCheck)
    {
        Objects.requireNonNull(userId);
        LOG.debug("Checking if token is valid for user {} ", userId);
        User user = getUserById(userId);
        String savedToken = getTokenValue(user);
        boolean tokenValid = tokenToCheck.equals(savedToken);
        LOG.debug("Token is valid for user? {} value {} ", userId, Boolean.valueOf(tokenValid));
        return tokenValid;
    }


    private static byte[] getNextRandom()
    {
        byte[] nextRandomValue = new byte[16];
        RANDOM.nextBytes(nextRandomValue);
        return nextRandomValue;
    }


    private String getNextRandomBase64EncodedWithCurrentTimestamp()
    {
        return Base64.encodeBytes(getNextRandom(), 8) + Base64.encodeBytes(getNextRandom(), 8);
    }
}
