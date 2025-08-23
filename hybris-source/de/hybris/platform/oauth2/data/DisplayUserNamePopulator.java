package de.hybris.platform.oauth2.data;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class DisplayUserNamePopulator implements Populator<UserModel, AuthenticatedUserData>
{
    public void populate(UserModel source, AuthenticatedUserData target) throws ConversionException
    {
        String name = source.getDisplayName();
        target.setDisplayName((name == null) ? "UNKNOWN" : name);
    }
}
