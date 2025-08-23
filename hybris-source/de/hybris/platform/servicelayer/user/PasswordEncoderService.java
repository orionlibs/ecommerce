package de.hybris.platform.servicelayer.user;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.exceptions.CannotDecodePasswordException;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;
import java.util.Collection;

public interface PasswordEncoderService
{
    String encode(UserModel paramUserModel, String paramString1, String paramString2) throws PasswordEncoderNotFoundException;


    String decode(UserModel paramUserModel) throws PasswordEncoderNotFoundException, CannotDecodePasswordException;


    boolean isSupportedEncoding(String paramString);


    Collection<String> getSupportedEncodings();


    boolean isValid(UserModel paramUserModel, String paramString);
}
