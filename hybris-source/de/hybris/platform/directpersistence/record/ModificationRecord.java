package de.hybris.platform.directpersistence.record;

import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface ModificationRecord extends EntityRecord
{
    Set<PropertyHolder> getChanges();


    Map<Locale, Set<PropertyHolder>> getLocalizedChanges();
}
