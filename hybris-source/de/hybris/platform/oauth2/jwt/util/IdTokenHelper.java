package de.hybris.platform.oauth2.jwt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.oauth2.jwt.exceptions.JwtException;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.Signer;

public class IdTokenHelper
{
    private final Map<String, Object> claims;
    private Map<String, String> headers;
    private final String claimsString;


    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }


    private IdTokenHelper(IdTokenBuilder builder) throws JwtException
    {
        this.headers = builder.getHeaders();
        this.claims = builder.getClaims();
        this.claimsString = buildClaims();
    }


    private final String buildClaims() throws JwtException
    {
        ObjectMapper mapperObj = new ObjectMapper();
        String jsonResp = null;
        try
        {
            jsonResp = mapperObj.writeValueAsString(this.claims);
        }
        catch(IOException e)
        {
            throw new JwtException("Problem with JWT generation", e);
        }
        return jsonResp;
    }


    public final Jwt encodeAndSign(Signer signer)
    {
        return JwtHelper.encode(this.claimsString, signer, this.headers);
    }
}
