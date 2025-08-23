package de.hybris.platform.servicelayer.security.dynamic;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicLocalizedAttributeHandler;
import java.util.Locale;

public class PrincipalDisplayNameLocalizedAttributeHandler implements DynamicLocalizedAttributeHandler<String, PrincipalModel>
{
    public String get(PrincipalModel model)
    {
        return null;
    }


    public void set(PrincipalModel model, String value)
    {
        throw new UnsupportedOperationException();
    }


    public String get(PrincipalModel model, Locale loc)
    {
        return null;
    }


    public void set(PrincipalModel model, String value, Locale loc)
    {
        throw new UnsupportedOperationException();
    }
}
