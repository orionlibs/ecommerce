package de.hybris.platform.servicelayer.user.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.persistence.security.EJBCannotDecodePasswordException;
import de.hybris.platform.persistence.security.PasswordEncoder;
import de.hybris.platform.persistence.security.PasswordEncoderException;
import de.hybris.platform.persistence.security.PasswordEncoderFactory;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.exceptions.CannotDecodePasswordException;
import de.hybris.platform.util.Config;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaulPasswordEncoderService implements PasswordEncoderService
{
    private PasswordEncoderFactory encoderFactory;


    public String encode(UserModel user, String plainTextPassword, String passwordEncoding) throws PasswordEncoderNotFoundException
    {
        try
        {
            return this.encoderFactory.getEncoder(passwordEncoding).encode(user.getUid(), plainTextPassword);
        }
        catch(PasswordEncoderNotFoundException e)
        {
            throw new PasswordEncoderNotFoundException(e);
        }
    }


    public String decode(UserModel user) throws PasswordEncoderNotFoundException, CannotDecodePasswordException
    {
        try
        {
            return this.encoderFactory.getEncoder(user.getPasswordEncoding()).decode(user.getEncodedPassword());
        }
        catch(PasswordEncoderNotFoundException e)
        {
            throw new PasswordEncoderNotFoundException(e);
        }
        catch(EJBCannotDecodePasswordException e)
        {
            throw new CannotDecodePasswordException(e);
        }
    }


    public boolean isSupportedEncoding(String encoding)
    {
        return this.encoderFactory.isSupportedEncoding(encoding);
    }


    public boolean isValid(UserModel user, String plainPassword)
    {
        try
        {
            if(!validatePasswordEmptiness(plainPassword))
            {
                return false;
            }
            return this.encoderFactory.getEncoder(user.getPasswordEncoding()).check(user.getUid(), user.getEncodedPassword(), plainPassword);
        }
        catch(PasswordEncoderException e)
        {
            throw new PasswordEncoderException("Exception while checking encoded password for user: " + user
                            .getPk() + ". " + e.getMessage());
        }
    }


    private boolean validatePasswordEmptiness(String password)
    {
        return (!StringUtils.isEmpty(password) || acceptEmptyPassword());
    }


    private boolean acceptEmptyPassword()
    {
        return Registry.getCurrentTenant().getConfig().getBoolean("user.password.acceptEmpty", false);
    }


    public boolean isValid(UserModel user, String encoding, String encodedPassword)
    {
        if(!validatePasswordEmptiness(encodedPassword))
        {
            return false;
        }
        PasswordEncoder encoder = this.encoderFactory.getEncoder((encoding != null) ? encoding : "*");
        if(encoder instanceof de.hybris.platform.persistence.security.PlainTextPasswordEncoder)
        {
            PasswordEncoder tokenEncoder = this.encoderFactory.getEncoder(Config.getParameter("login.token.encoder"));
            return tokenEncoder.encode(user.getUid(), user.getEncodedPassword()).equals(encodedPassword);
        }
        return encodedPassword.equals((user.getEncodedPassword() == null) ? "" : user.getEncodedPassword());
    }


    public Collection<String> getSupportedEncodings()
    {
        return this.encoderFactory.getSupportedEncodings();
    }


    @Required
    public void setEncoderFactory(PasswordEncoderFactory encoderFactory)
    {
        this.encoderFactory = encoderFactory;
    }
}
