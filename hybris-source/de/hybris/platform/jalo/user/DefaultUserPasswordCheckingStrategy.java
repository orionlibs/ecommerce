package de.hybris.platform.jalo.user;

import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.security.PasswordEncoderException;
import org.apache.commons.lang3.StringUtils;

public class DefaultUserPasswordCheckingStrategy implements PasswordCheckingStrategy
{
    public boolean checkPassword(User user, String plainPassword)
    {
        try
        {
            if(StringUtils.isEmpty(plainPassword) && !acceptEmptyPassword())
            {
                return false;
            }
            return Registry.getCurrentTenant().getJaloConnection().getPasswordEncoder(user.getPasswordEncoding())
                            .check(user.getUid(), user.getEncodedPassword(), plainPassword);
        }
        catch(PasswordEncoderException e)
        {
            throw new PasswordEncoderException("Exception while checking encoded password for user: " + user
                            .getPK() + ". " + e.getMessage());
        }
    }


    private boolean acceptEmptyPassword()
    {
        return Registry.getCurrentTenant().getConfig().getBoolean("user.password.acceptEmpty", false);
    }
}
