package de.hybris.platform.persistence.security;

public class PlainTextPasswordEncoder implements PasswordEncoder
{
    public String encode(String uid, String plain)
    {
        return plain;
    }


    public boolean check(String uid, String encoded, String plain)
    {
        if(encoded == null)
        {
            encoded = "";
        }
        if(plain == null)
        {
            plain = "";
        }
        return encoded.equals(plain);
    }


    public String decode(String encoded) throws EJBCannotDecodePasswordException
    {
        return encoded;
    }
}
