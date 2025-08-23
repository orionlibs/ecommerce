package de.hybris.platform.jalo.user;

import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import java.util.Map;

public interface TokenGenerator
{
    String generateToken(TokenParams paramTokenParams) throws EJBPasswordEncoderNotFoundException;


    Map<String, String> decodeToken(String paramString1, String paramString2);
}
