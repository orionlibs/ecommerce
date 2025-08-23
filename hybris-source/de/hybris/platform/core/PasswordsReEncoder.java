package de.hybris.platform.core;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.function.Consumer;

public final class PasswordsReEncoder
{
    private static final ThreadLocal<Boolean> IN_PROGRESS = ThreadLocal.withInitial(() -> Boolean.FALSE);


    public static boolean isReEncodingInProgress()
    {
        return Boolean.TRUE.equals(IN_PROGRESS.get());
    }


    public static void reEncodePasswordFor(Consumer<String> logger, String... ids)
    {
        if(ids == null || ids.length == 0)
        {
            return;
        }
        IN_PROGRESS.set(Boolean.TRUE);
        try
        {
            for(String id : ids)
            {
                try
                {
                    reEncode(logger, id);
                }
                catch(Exception e)
                {
                    logger.accept("Failed to re-encode password for the '" + id + "' user. Reason: '" + e + "'. Will continue with the old password.");
                }
            }
        }
        finally
        {
            IN_PROGRESS.set(Boolean.FALSE);
        }
    }


    private static void reEncode(Consumer<String> logger, String id)
    {
        UserModel user;
        if(id == null)
        {
            return;
        }
        UserService us = (UserService)Registry.getApplicationContext().getBean("userService", UserService.class);
        ModelService ms = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        try
        {
            user = us.getUserForUID(id);
        }
        catch(UnknownIdentifierException e)
        {
            logger.accept("User '" + id + "' doesn't exist.");
            return;
        }
        if(!"plain".equals(user.getPasswordEncoding()))
        {
            return;
        }
        logger.accept("Re-encoding password for the '" + id + "' user.");
        us.setPasswordWithDefaultEncoding(user, us.getPassword(user));
        ms.save(user);
        logger.accept("Password for the '" + id + "' user has been successfully re-encoded.");
    }
}
