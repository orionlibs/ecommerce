package de.hybris.platform.persistence.security;

import java.io.Serializable;

public interface PasswordEncoder extends Serializable
{
    String encode(String paramString1, String paramString2);


    boolean check(String paramString1, String paramString2, String paramString3);


    String decode(String paramString) throws EJBCannotDecodePasswordException;
}
