package de.hybris.platform.servicelayer.security;

import de.hybris.platform.persistence.security.SaltedMD5PasswordEncoder;
import de.hybris.platform.servicelayer.user.UserService;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = true)
public class PKbasedSaltedMD5PasswordEncoder extends SaltedMD5PasswordEncoder
{
    private static final Logger LOG = Logger.getLogger(PKbasedSaltedMD5PasswordEncoder.class.getName());
    @Resource
    private UserService userService;


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected String generateUserSpecificSalt(String userId)
    {
        return this.userService.getUserForUID(userId).getPk().toString();
    }
}
