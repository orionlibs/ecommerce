package de.hybris.platform.jalo.user;

import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.util.Base64;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class EnhancedTokenGenerator extends AbstractTokenGenerator
{
    private static final int SALT_LENGTH = 16;
    private static final Random RANDOM = new SecureRandom();
    private final TokenService tokenService;


    public EnhancedTokenGenerator()
    {
        this.tokenService = null;
    }


    public EnhancedTokenGenerator(TokenService tokenService)
    {
        this.tokenService = tokenService;
    }


    private static final TokenPart[] tokenParts = new TokenPart[] {TokenPart.USER, TokenPart.LANGUAGE, TokenPart.PASSWORD, TokenPart.TTLTIMESTAMP, TokenPart.SALT, TokenPart.RANDOM_TOKEN};


    private static byte[] getNextSalt()
    {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }


    public String generateToken(TokenParams params) throws EJBPasswordEncoderNotFoundException
    {
        String userPk = getUserPk(params);
        String languageIsoCode = getLanguageByIsoCode(params);
        String encodedPassword = getEncodedPassword(params);
        String ttlTimestamp = getTTLTimestamp(params.getTtl().intValue());
        String salt = getNextSaltString();
        String encodedPasswordWithSalt = UserManager.getInstance().getEncodedPasswordWithSalt(params.getUser(), encodedPassword, salt);
        String randomGeneratedTokenPart = getTokenService().getOrCreateTokenForUser(params.getUser().getUid());
        return encodeToken(new String[] {userPk, languageIsoCode, encodedPasswordWithSalt, ttlTimestamp, salt, randomGeneratedTokenPart}, params
                        .getDelimiter());
    }


    protected TokenPart[] getTokenParts()
    {
        return tokenParts;
    }


    private String getTTLTimestamp(int ttlSeconds)
    {
        return String.valueOf(Instant.now().plus(ttlSeconds, ChronoUnit.SECONDS).toEpochMilli());
    }


    private String getNextSaltString()
    {
        return Base64.encodeBytes(getNextSalt(), 8);
    }


    private TokenService getTokenService()
    {
        if(this.tokenService != null)
        {
            return this.tokenService;
        }
        return (TokenService)Registry.getCoreApplicationContext().getBean("tokenService", TokenService.class);
    }
}
