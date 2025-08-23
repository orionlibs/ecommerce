package de.hybris.platform.persistence.security;

import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import java.util.Collection;
import java.util.Map;

public interface PasswordEncoderFactory
{
    public static final String BEAN_NAME = "core.passwordEncoderFactory";


    PasswordEncoder getEncoder(String paramString) throws PasswordEncoderNotFoundException;


    Collection<String> getSupportedEncodings();


    Map<String, PasswordEncoder> getEncoders();


    boolean isSupportedEncoding(String paramString);
}
