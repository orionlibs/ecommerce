package de.hybris.platform.servicelayer.model.attribute;

import java.util.Locale;

public interface DynamicLocalizedAttributeHandler<VALUE, MODEL extends de.hybris.platform.servicelayer.model.AbstractItemModel> extends DynamicAttributeHandler<VALUE, MODEL>
{
    VALUE get(MODEL paramMODEL, Locale paramLocale);


    void set(MODEL paramMODEL, VALUE paramVALUE, Locale paramLocale);
}
