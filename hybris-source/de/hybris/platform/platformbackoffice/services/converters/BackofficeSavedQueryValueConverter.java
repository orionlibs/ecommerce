package de.hybris.platform.platformbackoffice.services.converters;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;

public interface BackofficeSavedQueryValueConverter
{
    boolean canHandle(DataAttribute paramDataAttribute);


    Object convertValue(SavedQueryValue paramSavedQueryValue, DataAttribute paramDataAttribute);


    SavedQueryValue convertValue(Object paramObject, DataAttribute paramDataAttribute);
}
