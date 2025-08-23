package de.hybris.platform.servicelayer.user.dynamic;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Required;

public class UserPasswordAttributeHandler implements DynamicAttributeHandler<String, UserModel>
{
    private UserService userService;


    public String get(UserModel model)
    {
        throw new UnsupportedOperationException();
    }


    public void set(UserModel model, String password)
    {
        this.userService.setPassword(model, password);
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
