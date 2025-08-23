package de.hybris.platform.directpersistence.record.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.ModificationRecord;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public abstract class AbstractModificationRecord extends AbstractEntityRecord implements ModificationRecord
{
    private Set<PropertyHolder> changes;
    private Map<Locale, Set<PropertyHolder>> localizedChanges;


    public AbstractModificationRecord(PK pk, String type, long version, Set<PropertyHolder> changes)
    {
        super(pk, type, version);
        this.changes = changes;
    }


    public AbstractModificationRecord(PK pk, String type, long version, Set<PropertyHolder> changes, Map<Locale, Set<PropertyHolder>> localizedChanges)
    {
        this(pk, type, version, changes);
        this.localizedChanges = localizedChanges;
    }


    public Set<PropertyHolder> getChanges()
    {
        if(this.changes == null)
        {
            this.changes = new HashSet<>();
        }
        return this.changes;
    }


    public Map<Locale, Set<PropertyHolder>> getLocalizedChanges()
    {
        if(this.localizedChanges == null)
        {
            this.localizedChanges = new HashMap<>();
        }
        return this.localizedChanges;
    }


    public boolean getIncrementOptimisticLockCounter()
    {
        return true;
    }
}
