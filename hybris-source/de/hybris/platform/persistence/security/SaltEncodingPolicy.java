package de.hybris.platform.persistence.security;

import de.hybris.platform.util.Config;

public class SaltEncodingPolicy
{
    private static final String DELIMITER = "::";
    private static final String DEFAULT_SYSTEM_SPECIFIC_SALT = "hybris blue pepper can be used for creating delicious noodle meals";
    private String salt = null;


    public String generateUserSalt(String uid)
    {
        return uid;
    }


    public String saltify(String uid, String password)
    {
        if(!isSaltedAlready(password))
        {
            String userSpecificSalt = generateUserSalt(uid);
            return getSystemSpecificSalt().concat("::").concat((password == null) ? "" : password)
                            .concat("::").concat(userSpecificSalt);
        }
        return password;
    }


    public boolean isSaltedAlready(String password)
    {
        if(password == null)
        {
            return false;
        }
        return password.startsWith(getSystemSpecificSalt().concat("::"));
    }


    public String getSystemSpecificSalt()
    {
        if(Config.getParameter("password.md5.salt") == null || Config.getParameter("password.md5.salt")
                        .trim()
                        .length() == 0)
        {
            return (this.salt != null) ? this.salt : "hybris blue pepper can be used for creating delicious noodle meals";
        }
        return Config.getParameter("password.md5.salt");
    }


    public void setSalt(String salt)
    {
        this.salt = salt;
    }
}
