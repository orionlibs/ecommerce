package de.hybris.platform.directpersistence.record.visitor;

import com.google.common.collect.Sets;
import de.hybris.platform.directpersistence.record.DefaultRelationChanges;
import de.hybris.platform.directpersistence.record.InsertManyToManyRelationRecord;
import de.hybris.platform.directpersistence.record.InsertOneToManyRelationRecord;
import de.hybris.platform.directpersistence.record.LocalizedRelationChanges;
import de.hybris.platform.directpersistence.record.RelationChanges;
import de.hybris.platform.directpersistence.record.RemoveOneToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.impl.RelationMetaInfo;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DefaultRelationRecordVisitor implements RelationChanges.RelationChangesVisitor<Set<DefaultRelationRecordVisitor.RelationRecordsContainer>>
{
    private final LocalizationService localizationService;


    public DefaultRelationRecordVisitor(LocalizationService localizationService)
    {
        this.localizationService = localizationService;
    }


    public Set<RelationRecordsContainer> visit(RelationMetaInfo metaInfo, DefaultRelationChanges changes)
    {
        RelationRecordsContainer container = new RelationRecordsContainer(this, metaInfo, changes.getRemoveManyToManyRelationsRecords());
        addManyToManyRecordsToContainer(changes.getInsertManyToManyRelationRecords(), container);
        addOneToManyRecordsToContainer(changes.getOneToManyRelationRecords(), container);
        addRemoveOneToManyRecordsToContainer(changes.getRemoveOneToManyRelationsRecords(), container);
        return Sets.newHashSet((Object[])new RelationRecordsContainer[] {container});
    }


    public Set<RelationRecordsContainer> visit(RelationMetaInfo metaInfo, LocalizedRelationChanges changes)
    {
        Set<RelationRecordsContainer> containers = new HashSet<>();
        for(Map.Entry<Locale, DefaultRelationChanges> entry : (Iterable<Map.Entry<Locale, DefaultRelationChanges>>)changes.getRelationChanges().entrySet())
        {
            Locale locale = entry.getKey();
            DefaultRelationChanges changesForLocale = entry.getValue();
            RelationRecordsContainer container = new RelationRecordsContainer(this, metaInfo, this.localizationService.getMatchingPkForDataLocale(locale), changesForLocale.getRemoveManyToManyRelationsRecords());
            addManyToManyRecordsToContainer(changesForLocale.getInsertManyToManyRelationRecords(), container);
            addOneToManyRecordsToContainer(changesForLocale.getOneToManyRelationRecords(), container);
            addRemoveOneToManyRecordsToContainer(changesForLocale.getRemoveOneToManyRelationsRecords(), container);
            containers.add(container);
        }
        return containers;
    }


    private void addManyToManyRecordsToContainer(Collection<InsertManyToManyRelationRecord> records, RelationRecordsContainer container)
    {
        for(InsertManyToManyRelationRecord record : records)
        {
            container.addRecord(record);
        }
    }


    private void addOneToManyRecordsToContainer(Collection<InsertOneToManyRelationRecord> records, RelationRecordsContainer container)
    {
        for(InsertOneToManyRelationRecord record : records)
        {
            container.addRecord(record);
        }
    }


    private void addRemoveOneToManyRecordsToContainer(Collection<RemoveOneToManyRelationsRecord> records, RelationRecordsContainer container)
    {
        for(RemoveOneToManyRelationsRecord record : records)
        {
            container.addRecord(record);
        }
    }
}
