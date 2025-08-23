package de.hybris.platform.directpersistence.record.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.EntityRecord;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class InsertRecord extends AbstractModificationRecord
{
    public InsertRecord(PK pk, String type, Set<PropertyHolder> changes)
    {
        super(pk, type, 0L, changes);
    }


    public InsertRecord(PK pk, String type, Set<PropertyHolder> changes, Map<Locale, Set<PropertyHolder>> localizedChanges)
    {
        super(pk, type, 0L, changes, localizedChanges);
    }


    public <V> V accept(EntityRecord.EntityRecordVisitor<V> visitor)
    {
        return (V)visitor.visit(this);
    }


    public String toString()
    {
        return MessageFormat.format("InsertRecord: pk({0}), type({1}), version({2}), changes({3}), localized({4})", new Object[] {getPK(),
                        getType(), Long.toString(getVersion()), getChanges(), getLocalizedChanges()});
    }
}
