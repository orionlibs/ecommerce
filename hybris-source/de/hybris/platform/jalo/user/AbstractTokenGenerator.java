package de.hybris.platform.jalo.user;

import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTokenGenerator implements TokenGenerator
{
    String encodeToken(String[] tokens, String tokenDelimeter)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < tokens.length; i++)
        {
            sb.append(tokens[i]);
            if(i < tokens.length - 1)
            {
                sb.append(tokenDelimeter);
            }
        }
        return sb.toString();
    }


    public Map<String, String> decodeToken(String token, String tokenDelimiter)
    {
        Map<String, String> dividedTokenMap = new HashMap<>();
        TokenPart[] tokenParts = getTokenParts();
        String[] tokenList = token.split(tokenDelimiter);
        int position = 0;
        for(String valueFromToken : tokenList)
        {
            String key = (position <= tokenParts.length - 1) ? tokenParts[position].getKey() : null;
            position++;
            if(key != null)
            {
                dividedTokenMap.put(key, valueFromToken);
            }
        }
        return dividedTokenMap;
    }


    protected abstract TokenPart[] getTokenParts();


    String getUserPk(TokenParams params)
    {
        return params.getUser().getPK().toString();
    }


    String getLanguageByIsoCode(TokenParams params)
    {
        return C2LManager.getInstance().getLanguageByIsoCode(params.getLanguageISO()).getPK().toString();
    }


    String getEncodedPassword(TokenParams params) throws EJBPasswordEncoderNotFoundException
    {
        return UserManager.getInstance()
                        .getEncodedPasswordForLoginCookie(params.getUid(), params
                                        .getPlainTextPassword(), params.getUser());
    }
}
