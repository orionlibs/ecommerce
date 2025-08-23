package de.hybris.platform.directpersistence.record.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import de.hybris.platform.directpersistence.record.DefaultRelationChanges;
import de.hybris.platform.directpersistence.record.InsertManyToManyRelationRecord;
import de.hybris.platform.directpersistence.record.InsertOneToManyRelationRecord;
import de.hybris.platform.directpersistence.record.RelationChanges;
import de.hybris.platform.directpersistence.record.RemoveManyToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.RemoveOneToManyRelationsRecord;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;

public class DefaultNonLocalizedRelationChanges implements DefaultRelationChanges
{
    private static final Joiner NEWLINE_JOINER = Joiner.on("\n").skipNulls();
    private Set<InsertManyToManyRelationRecord> insertManyToManyRelationRecords;
    private Set<RemoveManyToManyRelationsRecord> removeManyToManyRelationsRecords;
    private Set<InsertOneToManyRelationRecord> oneToManyRelationRecords;
    private Set<RemoveOneToManyRelationsRecord> removeOneToManyRelationsRecords;
    private final RelationMetaInfo relationMetaInfo;


    public DefaultNonLocalizedRelationChanges(RelationMetaInfo relationMetaInfo)
    {
        this.relationMetaInfo = relationMetaInfo;
    }


    public Collection<InsertManyToManyRelationRecord> getInsertManyToManyRelationRecords()
    {
        if(this.insertManyToManyRelationRecords == null)
        {
            return Collections.emptyList();
        }
        return (Collection<InsertManyToManyRelationRecord>)ImmutableSet.copyOf(this.insertManyToManyRelationRecords);
    }


    public Collection<RemoveManyToManyRelationsRecord> getRemoveManyToManyRelationsRecords()
    {
        if(this.removeManyToManyRelationsRecords == null)
        {
            return Collections.emptyList();
        }
        return (Collection<RemoveManyToManyRelationsRecord>)ImmutableSet.copyOf(this.removeManyToManyRelationsRecords);
    }


    public Collection<InsertOneToManyRelationRecord> getOneToManyRelationRecords()
    {
        if(this.oneToManyRelationRecords == null)
        {
            return Collections.emptyList();
        }
        return (Collection<InsertOneToManyRelationRecord>)ImmutableSet.copyOf(this.oneToManyRelationRecords);
    }


    public Collection<RemoveOneToManyRelationsRecord> getRemoveOneToManyRelationsRecords()
    {
        if(this.removeOneToManyRelationsRecords == null)
        {
            return Collections.emptyList();
        }
        return (Collection<RemoveOneToManyRelationsRecord>)ImmutableSet.copyOf(this.removeOneToManyRelationsRecords);
    }


    public void add(InsertManyToManyRelationRecord... records)
    {
        Preconditions.checkNotNull(records);
        createInsertManyToManyRelationsContainer();
        Collections.addAll(this.insertManyToManyRelationRecords, records);
    }


    private void createInsertManyToManyRelationsContainer()
    {
        if(this.insertManyToManyRelationRecords == null)
        {
            this.insertManyToManyRelationRecords = new LinkedHashSet<>();
        }
    }


    public void add(RemoveManyToManyRelationsRecord... records)
    {
        Preconditions.checkNotNull(records);
        createRemoveManyToManyRelationsContainer();
        Collections.addAll(this.removeManyToManyRelationsRecords, records);
    }


    public void add(RemoveOneToManyRelationsRecord... records)
    {
        Preconditions.checkNotNull(records);
        createRemoveOneToManyRelationsContainer();
        Collections.addAll(this.removeOneToManyRelationsRecords, records);
    }


    private void createRemoveManyToManyRelationsContainer()
    {
        if(this.removeManyToManyRelationsRecords == null)
        {
            this.removeManyToManyRelationsRecords = new LinkedHashSet<>();
        }
    }


    private void createRemoveOneToManyRelationsContainer()
    {
        if(this.removeOneToManyRelationsRecords == null)
        {
            this.removeOneToManyRelationsRecords = new LinkedHashSet<>();
        }
    }


    public void add(InsertOneToManyRelationRecord... records)
    {
        Preconditions.checkNotNull(records);
        createOneToManyRelationsContainer();
        Collections.addAll(this.oneToManyRelationRecords, records);
    }


    private void createOneToManyRelationsContainer()
    {
        if(this.oneToManyRelationRecords == null)
        {
            this.oneToManyRelationRecords = new LinkedHashSet<>();
        }
    }


    public void groupOrderInformation()
    {
        TreeMap<InsertManyToManyRelationRecord, InsertManyToManyRelationRecord> grouped = new TreeMap<>((Comparator<? super InsertManyToManyRelationRecord>)new InsertRelationRecordComparator(this));
        if(this.insertManyToManyRelationRecords != null)
        {
            for(InsertManyToManyRelationRecord record : this.insertManyToManyRelationRecords)
            {
                if(grouped.containsKey(record))
                {
                    mergeInsertRelationRecord(grouped.get(record), record);
                    continue;
                }
                grouped.put(record, record);
            }
            this.insertManyToManyRelationRecords = new LinkedHashSet<>(grouped.values());
        }
    }


    public <V> V accept(RelationMetaInfo metaInfo, RelationChanges.RelationChangesVisitor<V> visitor)
    {
        return (V)visitor.visit(metaInfo, this);
    }


    public RelationMetaInfo getRelationMetaInfo()
    {
        return this.relationMetaInfo;
    }


    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if(this.insertManyToManyRelationRecords != null)
        {
            builder.append(NEWLINE_JOINER.join(this.insertManyToManyRelationRecords));
        }
        if(this.removeManyToManyRelationsRecords != null)
        {
            builder.append(NEWLINE_JOINER.join(this.removeManyToManyRelationsRecords));
        }
        if(this.oneToManyRelationRecords != null)
        {
            builder.append(NEWLINE_JOINER.join(this.oneToManyRelationRecords));
        }
        return builder.toString();
    }


    private void mergeInsertRelationRecord(InsertManyToManyRelationRecord mergeTo, InsertManyToManyRelationRecord mergeFrom)
    {
        if(mergeFrom.getSourceToTargetPosition() != null)
        {
            mergeTo.setSourceToTargetPosition(mergeFrom.getSourceToTargetPosition());
        }
        if(mergeFrom.getTargetToSourcePosition() != null)
        {
            mergeTo.setTargetToSourcePosition(mergeFrom.getTargetToSourcePosition());
        }
    }
}
