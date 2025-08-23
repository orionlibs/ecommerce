package de.hybris.platform.servicelayer.security.dynamic;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicLocalizedAttributeHandler;
import java.util.Locale;

public class PrincipalGroupDisplayNameLocalizedAttributeHandler implements DynamicLocalizedAttributeHandler<String, PrincipalGroupModel>
{
    public String get(PrincipalGroupModel model)
    {
        return model.getLocName();
    }


    public void set(PrincipalGroupModel model, String value)
    {
        throw new UnsupportedOperationException();
    }


    public String get(PrincipalGroupModel model, Locale loc)
    {
        return model.getLocName(loc);
    }


    public void set(PrincipalGroupModel model, String value, Locale loc)
    {
        throw new UnsupportedOperationException();
    }
}
