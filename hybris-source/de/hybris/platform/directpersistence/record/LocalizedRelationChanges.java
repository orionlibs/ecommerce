package de.hybris.platform.directpersistence.record;

import java.util.Locale;
import java.util.Map;

public interface LocalizedRelationChanges extends RelationChanges
{
    Map<Locale, DefaultRelationChanges> getRelationChanges();


    DefaultRelationChanges getRelationChangeForLanguage(Locale paramLocale);


    void put(Locale paramLocale, DefaultRelationChanges paramDefaultRelationChanges);
}
