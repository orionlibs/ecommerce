package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.InsertManyToManyRelationRecord;
import de.hybris.platform.directpersistence.record.InsertOneToManyRelationRecord;
import de.hybris.platform.directpersistence.record.ModificationRecord;
import de.hybris.platform.directpersistence.record.RemoveManyToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.RemoveOneToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.impl.DefaultInsertManyToManyRelationRecord;
import de.hybris.platform.directpersistence.record.impl.DefaultInsertOneToManyRelationRecord;
import de.hybris.platform.directpersistence.record.impl.DefaultRemoveManyToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.impl.DefaultRemoveOneToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.record.impl.UpdateRecord;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class RecordFactory
{
    public static InsertOneToManyRelationRecord createOneToManyRelationRecord(RelationInfo info)
    {
        DefaultInsertOneToManyRelationRecord record = new DefaultInsertOneToManyRelationRecord(info.getSourcePk(), info.getTargetPk());
        record.setSourceToTargetPosition(info.getPosition());
        return (InsertOneToManyRelationRecord)record;
    }


    public static RemoveManyToManyRelationsRecord createRemoveManyToManyRelationRecord(RelationInfo info)
    {
        return (RemoveManyToManyRelationsRecord)new DefaultRemoveManyToManyRelationsRecord(info.getSourcePk(), info.isSrcToTgt());
    }


    public static InsertManyToManyRelationRecord createInserManyToManyRelationRecord(RelationInfo info)
    {
        DefaultInsertManyToManyRelationRecord defaultInsertManyToManyRelationRecord;
        if(info.isSrcToTgt())
        {
            defaultInsertManyToManyRelationRecord = new DefaultInsertManyToManyRelationRecord(info.getSourcePk(), info.getTargetPk(), info.isSrcToTgt());
            defaultInsertManyToManyRelationRecord.setSourceToTargetPosition(info.getPosition());
        }
        else
        {
            defaultInsertManyToManyRelationRecord = new DefaultInsertManyToManyRelationRecord(info.getTargetPk(), info.getSourcePk(), info.isSrcToTgt());
            defaultInsertManyToManyRelationRecord.setTargetToSourcePosition(info.getPosition());
        }
        return (InsertManyToManyRelationRecord)defaultInsertManyToManyRelationRecord;
    }


    public static ModificationRecord createEntityRecord(ModelWrapper wrapper, DefaultChangeSetBuilder.ModelInfoProvider info, Set<PropertyHolder> changes, Map<Locale, Set<PropertyHolder>> locChanges)
    {
        String type = info.getType();
        PK pk = wrapper.getResolvedPk();
        if(wrapper.isNew())
        {
            return (ModificationRecord)new InsertRecord(pk, type, changes, locChanges);
        }
        assureNoInitialOnlyUpdate(info, changes);
        assureNoLocalizedInitialOnlyUpdate(info, locChanges);
        return (ModificationRecord)new UpdateRecord(pk, type, info.getContext().getPersistenceVersion(), changes, locChanges);
    }


    private static void assureNoInitialOnlyUpdate(DefaultChangeSetBuilder.ModelInfoProvider info, Set<PropertyHolder> changes)
    {
        if(changes != null)
        {
            for(PropertyHolder change : changes)
            {
                ItemModelConverter.TypeAttributeInfo attributeInfo = info.getConverter().getInfo(change.getName()).getAttributeInfo();
                if(attributeInfo != null && attributeInfo.isInitial() && !attributeInfo.isWritableFlag())
                {
                    throw new IllegalStateException("Can NOT update initial only attribute: " + change.getName());
                }
            }
        }
    }


    private static void assureNoLocalizedInitialOnlyUpdate(DefaultChangeSetBuilder.ModelInfoProvider info, Map<Locale, Set<PropertyHolder>> locChanges)
    {
        if(locChanges != null)
        {
            for(Map.Entry<Locale, Set<PropertyHolder>> entry : locChanges.entrySet())
            {
                assureNoInitialOnlyUpdate(info, entry.getValue());
            }
        }
    }


    public static RemoveOneToManyRelationsRecord createRemoveOneToManyRelationsRecord(RelationInfo info)
    {
        return (RemoveOneToManyRelationsRecord)new DefaultRemoveOneToManyRelationsRecord(info.getSourcePk());
    }
}
