package de.hybris.platform.jalo.user;

import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;

public class BaseTokenGenerator extends AbstractTokenGenerator
{
    private static final TokenPart[] tokenParts = new TokenPart[] {TokenPart.USER, TokenPart.LANGUAGE, TokenPart.PASSWORD};


    public String generateToken(TokenParams params) throws EJBPasswordEncoderNotFoundException
    {
        return encodeToken(new String[] {getUserPk(params), getLanguageByIsoCode(params), getEncodedPassword(params)}, params
                        .getDelimiter());
    }


    protected TokenPart[] getTokenParts()
    {
        return tokenParts;
    }
}
