package de.hybris.platform.directpersistence.record.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.EntityRecord;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class UpdateRecord extends AbstractModificationRecord
{
    private final boolean checkIsLockedProperty;


    public UpdateRecord(PK pk, String type, long version, Set<PropertyHolder> changes)
    {
        super(pk, type, version, changes);
        this.checkIsLockedProperty = true;
    }


    public UpdateRecord(PK pk, String type, long version, boolean checkIsLockedProperty, Set<PropertyHolder> changes)
    {
        super(pk, type, version, changes);
        this.checkIsLockedProperty = checkIsLockedProperty;
    }


    public UpdateRecord(PK pk, String type, long version, Set<PropertyHolder> changes, Map<Locale, Set<PropertyHolder>> localizedChanges)
    {
        super(pk, type, version, changes, localizedChanges);
        this.checkIsLockedProperty = true;
    }


    public <V> V accept(EntityRecord.EntityRecordVisitor<V> visitor)
    {
        return (V)visitor.visit(this);
    }


    public String toString()
    {
        return MessageFormat.format("UpdateRecord: pk({0}), type({1}), version({2}), changes({3}), localized({4})", new Object[] {getPK(),
                        getType(), Long.toString(getVersion()), getChanges(), getLocalizedChanges()});
    }


    public boolean checkIsLockedProperty()
    {
        return this.checkIsLockedProperty;
    }
}
