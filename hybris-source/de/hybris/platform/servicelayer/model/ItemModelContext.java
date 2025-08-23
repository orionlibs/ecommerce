package de.hybris.platform.servicelayer.model;

import de.hybris.platform.core.PK;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface ItemModelContext
{
    Set<String> getDirtyAttributes();


    boolean isDirty(String paramString);


    boolean isLoaded(String paramString);


    <T> T getOriginalValue(String paramString);


    boolean isDirty(String paramString, Locale paramLocale);


    boolean isLoaded(String paramString, Locale paramLocale);


    <T> T getOriginalValue(String paramString, Locale paramLocale);


    Map<Locale, Set<String>> getDirtyLocalizedAttributes();


    PK getPK();


    String getItemType();


    String getTenantId();


    long getPersistenceVersion();


    boolean isDirty();


    boolean isNew();


    boolean exists();


    boolean isRemoved();


    boolean isUpToDate();


    Object getSource();
}
