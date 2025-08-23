package de.hybris.platform.servicelayer.user.dynamic;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicLocalizedAttributeHandler;
import java.util.Locale;

public class UserDisplayNameLocalizedAttributeHandler implements DynamicLocalizedAttributeHandler<String, UserModel>
{
    public String get(UserModel model)
    {
        return model.getName();
    }


    public void set(UserModel model, String value)
    {
        throw new UnsupportedOperationException();
    }


    public String get(UserModel model, Locale loc)
    {
        return model.getName();
    }


    public void set(UserModel model, String value, Locale loc)
    {
        throw new UnsupportedOperationException();
    }
}
