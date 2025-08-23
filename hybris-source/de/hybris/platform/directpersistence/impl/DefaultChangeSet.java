package de.hybris.platform.directpersistence.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.MutableChangeSet;
import de.hybris.platform.directpersistence.record.DefaultRelationChanges;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.InsertOneToManyRelationRecord;
import de.hybris.platform.directpersistence.record.LocalizedRelationChanges;
import de.hybris.platform.directpersistence.record.Record;
import de.hybris.platform.directpersistence.record.RelationChanges;
import de.hybris.platform.persistence.framework.EntityInstance;
import de.hybris.platform.tx.Transaction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class DefaultChangeSet implements MutableChangeSet
{
    protected static Logger LOG = Logger.getLogger(DefaultChangeSet.class.getName());
    private SortedMultiset<EntityRecord> entityRecords;
    private TreeMap<String, RelationChanges> relationChanges;
    private boolean jaloWayRecommended = false;


    public Collection<EntityRecord> getEntityRecords()
    {
        if(this.entityRecords == null)
        {
            return Collections.emptyList();
        }
        return (Collection<EntityRecord>)ImmutableList.copyOf((Collection)this.entityRecords);
    }


    public void add(EntityRecord... entityRecords)
    {
        Preconditions.checkNotNull(entityRecords);
        ensureEntityRecordsCreated();
        Collections.addAll((Collection<? super EntityRecord>)this.entityRecords, entityRecords);
        checkIfUsedViaJaloInCurrentTx(Arrays.asList(entityRecords));
    }


    public void addAllEntityRecords(Collection<? extends EntityRecord> entityRecords)
    {
        Preconditions.checkNotNull(entityRecords);
        ensureEntityRecordsCreated();
        this.entityRecords.addAll(entityRecords);
        checkIfUsedViaJaloInCurrentTx((Iterable)entityRecords);
    }


    private void checkIfUsedViaJaloInCurrentTx(PK pk)
    {
        EntityInstance entityInstance = Transaction.current().getAttachedEntityInstance(pk);
        if(entityInstance != null && entityInstance.needsStoring())
        {
            this.jaloWayRecommended = true;
        }
    }


    public Map<String, RelationChanges> getRelationChanges()
    {
        if(this.relationChanges == null)
        {
            return Collections.emptyMap();
        }
        return (Map<String, RelationChanges>)ImmutableMap.copyOf(this.relationChanges);
    }


    private void checkIfUsedViaJaloInCurrentTx(Iterable<EntityRecord> entityRecords)
    {
        if(this.jaloWayRecommended)
        {
            return;
        }
        for(EntityRecord entityRecord : entityRecords)
        {
            if(Transaction.current().isRunning())
            {
                checkIfUsedViaJaloInCurrentTx(entityRecord.getPK());
            }
        }
    }


    public void putRelationChanges(String relationName, RelationChanges changeSet)
    {
        Preconditions.checkNotNull(relationName);
        Preconditions.checkNotNull(changeSet);
        ensureRelationChangesetsCreated();
        if(this.relationChanges.containsKey(relationName))
        {
            throw new IllegalStateException("Relation changes for " + relationName + " already exists.");
        }
        this.relationChanges.put(relationName, changeSet);
    }


    public RelationChanges getRelationChangesForRelation(String relationName)
    {
        Preconditions.checkNotNull(relationName);
        ensureRelationChangesetsCreated();
        return this.relationChanges.get(relationName);
    }


    public void sortEntityRecords()
    {
    }


    public void groupOrderInformation()
    {
        if(this.relationChanges != null)
        {
            for(RelationChanges changes : this.relationChanges.values())
            {
                changes.groupOrderInformation();
            }
        }
    }


    public void sortAll()
    {
        sortEntityRecords();
    }


    private void ensureEntityRecordsCreated()
    {
        if(this.entityRecords == null)
        {
            this.entityRecords = (SortedMultiset<EntityRecord>)TreeMultiset.create((Comparator)new EntityRecordComparator(this));
        }
    }


    private void ensureRelationChangesetsCreated()
    {
        if(this.relationChanges == null)
        {
            this.relationChanges = new TreeMap<>((Comparator<? super String>)new RelationChangesComparator(this));
        }
    }


    public String toString()
    {
        StringBuilder builder = new StringBuilder("\n");
        if(this.entityRecords != null)
        {
            for(Record record : this.entityRecords)
            {
                builder.append(record.toString()).append('\n');
            }
        }
        if(this.relationChanges != null)
        {
            for(Map.Entry<String, RelationChanges> relations : this.relationChanges.entrySet())
            {
                builder.append("Processing relation: ").append(relations.getKey()).append("\n");
                builder.append(((RelationChanges)relations.getValue()).toString());
                builder.append("Processing relation: ").append(relations.getKey()).append(" finished\n");
            }
        }
        return builder.toString();
    }


    public void setJaloWayRecommended(boolean jaloWayRecommended)
    {
        if(this.jaloWayRecommended)
        {
            return;
        }
        this.jaloWayRecommended = jaloWayRecommended;
    }


    public void finish()
    {
        if(isJaloWayRecommended() || this.relationChanges == null)
        {
            return;
        }
        for(RelationChanges changes : this.relationChanges.values())
        {
            checkIfUsedViaJaloInCurrentTx(changes);
        }
    }


    public boolean isJaloWayRecommended()
    {
        return this.jaloWayRecommended;
    }


    private void addAllRelationChanges(Map<String, RelationChanges> theRelationChanges)
    {
        ensureRelationChangesetsCreated();
        this.relationChanges.putAll(theRelationChanges);
    }


    private void checkIfUsedViaJaloInCurrentTx(RelationChanges changes)
    {
        if(changes instanceof DefaultRelationChanges)
        {
            checkIfUsedViaJaloInCurrentTx((DefaultRelationChanges)changes);
        }
        else if(changes instanceof LocalizedRelationChanges)
        {
            LocalizedRelationChanges lrc = (LocalizedRelationChanges)changes;
            for(DefaultRelationChanges defaultRelationChanges : lrc.getRelationChanges().values())
            {
                checkIfUsedViaJaloInCurrentTx(defaultRelationChanges);
            }
        }
    }


    private void checkIfUsedViaJaloInCurrentTx(DefaultRelationChanges defaultRelationChanges)
    {
        for(InsertOneToManyRelationRecord oneToManyRelationRecord : defaultRelationChanges.getOneToManyRelationRecords())
        {
            checkIfUsedViaJaloInCurrentTx(oneToManyRelationRecord.getSourcePk());
            checkIfUsedViaJaloInCurrentTx(oneToManyRelationRecord.getTargetPk());
        }
    }


    public ChangeSet withAdded(ChangeSet otherChangeSet)
    {
        DefaultChangeSet result = new DefaultChangeSet();
        result.addAllEntityRecords(getEntityRecords());
        result.addAllEntityRecords(otherChangeSet.getEntityRecords());
        result.addAllRelationChanges(getRelationChanges());
        result.addAllRelationChanges(otherChangeSet.getRelationChanges());
        result.setJaloWayRecommended((this.jaloWayRecommended || otherChangeSet.isJaloWayRecommended()));
        return (ChangeSet)result;
    }


    public boolean allowEmptyUpdate()
    {
        return false;
    }
}
