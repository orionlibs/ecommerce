package de.hybris.platform.directpersistence.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import com.google.common.collect.UnmodifiableIterator;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.CacheInvalidator;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.DirectPersistenceUtils;
import de.hybris.platform.directpersistence.PersistResult;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.directpersistence.exception.ModelPersistenceException;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.InsertOneToManyRelationRecord;
import de.hybris.platform.directpersistence.record.RelationChanges;
import de.hybris.platform.directpersistence.record.RemoveManyToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.record.impl.RelationMetaInfo;
import de.hybris.platform.directpersistence.record.impl.UpdateRecord;
import de.hybris.platform.directpersistence.record.visitor.DefaultRelationRecordVisitor;
import de.hybris.platform.directpersistence.record.visitor.RecordVisitorFactory;
import de.hybris.platform.directpersistence.statement.ModifyManyToManyRelationStatementsBuilder;
import de.hybris.platform.directpersistence.statement.ModifyOneToManyRelationStatementsBuilder;
import de.hybris.platform.directpersistence.statement.RemoveManyToManyRelationStatementsBuilder;
import de.hybris.platform.directpersistence.statement.StatementHolder;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.persistence.audit.AuditableOperation;
import de.hybris.platform.persistence.audit.AuditableOperations;
import de.hybris.platform.persistence.audit.Operation;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class DefaultWritePersistenceGateway implements WritePersistenceGateway
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWritePersistenceGateway.class);
    private RecordVisitorFactory recordVisitorFactory;
    private TransactionTemplate transactionTemplate;
    private BatchCollectorFactory batchCollectorFactory;
    private LocalizationService localizationService;
    private JdbcTemplate jdbcTemplate;
    private Config.DatabaseName databaseName;
    private CacheInvalidator cacheInvalidator;
    private final Function<EntityRecord, PK> entityToPkTransformer = (Function<EntityRecord, PK>)new Object(this);


    public Collection<PersistResult> persist(ChangeSet changeSet) throws ModelPersistenceException
    {
        Transaction tx = Transaction.current();
        if(!tx.isRunning())
        {
            return (Collection<PersistResult>)this.transactionTemplate.execute((TransactionCallback)new Object(this, changeSet));
        }
        return persistInternal(changeSet);
    }


    public Collection<PersistResult> persistInternal(ChangeSet changeSet) throws ModelPersistenceException
    {
        Set<PersistResult> results = new LinkedHashSet<>();
        BatchCollector batchCollector = this.batchCollectorFactory.createBatchCollector();
        Set<DefaultRelationRecordVisitor.RelationRecordsContainer> relationContainers = prepareRelationContainers(changeSet.getRelationChanges());
        Collection<EntityRecord> entityRecords = changeSet.getEntityRecords();
        preprocessOneToManyRelationContainers(relationContainers, entityRecords);
        processEntityRecords(batchCollector, results, entityRecords);
        Set<PersistResult> resultsM2M = new LinkedHashSet<>();
        processRelationContainersM2M(relationContainers, batchCollector, resultsM2M);
        results.addAll(resultsM2M);
        processRelationContainersO2M(relationContainers, batchCollector, results);
        if(!changeSet.allowEmptyUpdate() && containsNoChangesInUpdateRecords(entityRecords) &&
                        CollectionUtils.isEmpty(relationContainers))
        {
            results.clear();
            return Collections.emptySet();
        }
        AuditableOperation audit = AuditableOperations.aboutToExecute(operations(results, resultsM2M));
        boolean success = false;
        try
        {
            batchCollector.batchUpdate(this.jdbcTemplate);
            success = true;
        }
        finally
        {
            if(success)
            {
                this.cacheInvalidator.invalidate(changeSet.getResultsToInvalidate(results));
            }
            audit.finish(success);
        }
        return (Collection<PersistResult>)ImmutableSet.copyOf(results);
    }


    private Operation[] operations(Set<PersistResult> items, Set<PersistResult> relations)
    {
        Operations operations = new Operations(items.size() + relations.size());
        Objects.requireNonNull(operations);
        Streams.concat(new Stream[] {items.stream(), relations.stream()}).map(this::toOperation).forEach(operations::add);
        return operations.getArrayOfOperations();
    }


    private Operation toOperation(PersistResult persistResult)
    {
        switch(null.$SwitchMap$de$hybris$platform$directpersistence$CrudEnum[persistResult.getOperation().ordinal()])
        {
            case 1:
                return Operation.create(persistResult.getPk(), persistResult.getTypeCode());
            case 2:
                return Operation.update(persistResult.getPk(), persistResult.getTypeCode());
            case 3:
                return Operation.delete(persistResult.getPk(), persistResult.getTypeCode());
        }
        throw new IllegalStateException("Illegal type of crud operation");
    }


    private boolean containsNoChangesInUpdateRecords(Collection<EntityRecord> entityRecords)
    {
        if(CollectionUtils.isEmpty(entityRecords))
        {
            return true;
        }
        return entityRecords.stream().allMatch(e -> (e instanceof UpdateRecord && ((UpdateRecord)e).getChanges().isEmpty() && ((UpdateRecord)e).getLocalizedChanges().isEmpty()));
    }


    private void processEntityRecords(BatchCollector batchCollector, Set<PersistResult> results, Collection<? extends EntityRecord> records)
    {
        if(CollectionUtils.isNotEmpty(records))
        {
            EntityRecord.EntityRecordVisitor<Set<PersistResult>> recordVisitor = this.recordVisitorFactory.createEntityRecordVisitor(batchCollector, this.jdbcTemplate, this.localizationService, this.databaseName);
            for(EntityRecord record : records)
            {
                results.addAll((Collection<? extends PersistResult>)record.accept(recordVisitor));
            }
        }
    }


    private void preprocessOneToManyRelationContainers(Set<DefaultRelationRecordVisitor.RelationRecordsContainer> containers, Collection<EntityRecord> entityRecords)
    {
        for(DefaultRelationRecordVisitor.RelationRecordsContainer container : containers)
        {
            RelationMetaInfo metaInfo = container.getMetaInfo();
            if(metaInfo.isOneToMany())
            {
                Map<PK, List<InsertOneToManyRelationRecord>> oneToManyRelationRecords = container.getOneToManyRelationRecords();
                for(Map.Entry<PK, List<InsertOneToManyRelationRecord>> entry : oneToManyRelationRecords.entrySet())
                {
                    PK sourcePk = entry.getKey();
                    List<InsertOneToManyRelationRecord> relationRecords = entry.getValue();
                    Set<PK> existingTargets = findExistingOneToManyTargetPks(sourcePk, container.getMetaInfo());
                    Set<PK> expectedTargets = getExpectedOneToManyTargetPks(relationRecords);
                    Sets.SetView<PK> toAdd = Sets.difference(expectedTargets, existingTargets);
                    Sets.SetView<PK> toUpdate = Sets.intersection(existingTargets, expectedTargets);
                    Sets.SetView<PK> toRemove = Sets.difference(existingTargets, expectedTargets);
                    container.addOneToManyPksToRemove(sourcePk, (Set)toRemove.immutableCopy());
                    addForeignKeysForOneToManyTargetsInEntities(entityRecords, metaInfo, sourcePk, relationRecords, toUpdate);
                    addForeignKeysForOneToManyTargetsInEntities(entityRecords, metaInfo, sourcePk, relationRecords, toAdd);
                    if(metaInfo.isSourceOrdered())
                    {
                        addPositionsForOneToManyTargetsInEntities(entityRecords, metaInfo.getForeignKeyOnTarget(), relationRecords);
                    }
                }
            }
        }
    }


    private Set<PK> findExistingOneToManyTargetPks(PK sourcePk, RelationMetaInfo relationMetaInfo)
    {
        TypeInfoMap infoMap = DirectPersistenceUtils.getInfoMapForType(relationMetaInfo.getTargetTypeCode());
        Optional<String> foreignKeyColumnName = getForeignKeyColumnName(infoMap, relationMetaInfo);
        Preconditions.checkArgument(foreignKeyColumnName.isPresent());
        String query = FluentSqlBuilder.builder(this.databaseName).select(new String[] {ServiceCol.PK_STRING.colName()}).from(infoMap.getItemTableName()).where().field((String)foreignKeyColumnName.get()).isEqual().toSql();
        return (Set<PK>)ImmutableSet.builder().addAll(this.jdbcTemplate.query(query, (RowMapper)new Object(this), new Object[] {Long.valueOf(sourcePk.getLongValue())})).build();
    }


    private void addForeignKeysForOneToManyTargetsInEntities(Collection<EntityRecord> entityRecords, RelationMetaInfo metaInfo, PK sourcePk, List<InsertOneToManyRelationRecord> oneToManyRecords, Sets.SetView<PK> toUpdate)
    {
        Object object = new Object(this);
        IterableFinder<EntityRecord, PK> entityRecordFinder = IterableFinder.searchIn(entityRecords);
        IterableFinder<InsertOneToManyRelationRecord, PK> relRecordFinder = IterableFinder.searchIn(oneToManyRecords);
        for(UnmodifiableIterator<PK> unmodifiableIterator = toUpdate.iterator(); unmodifiableIterator.hasNext(); )
        {
            PK pk = unmodifiableIterator.next();
            EntityRecord entityRecord = (EntityRecord)entityRecordFinder.find(pk, this.entityToPkTransformer);
            if(entityRecord instanceof InsertRecord)
            {
                Set<PropertyHolder> entityChanges = ((InsertRecord)entityRecord).getChanges();
                Optional<PropertyHolder> holder = findPropertyHolderForName(entityChanges, metaInfo
                                .getForeignKeyOnTarget());
                if(!holder.isPresent())
                {
                    ((InsertRecord)entityRecord).getChanges()
                                    .add(new PropertyHolder(metaInfo.getForeignKeyOnTarget(), new ItemPropertyValue(sourcePk)));
                    InsertOneToManyRelationRecord relationRecord = (InsertOneToManyRelationRecord)relRecordFinder.find(pk, (Function)object);
                    if(relationRecord != null)
                    {
                        relationRecord.markAsProcessed();
                    }
                }
            }
        }
    }


    private Optional<PropertyHolder> findPropertyHolderForName(Set<PropertyHolder> entityChanges, String propertyName)
    {
        return Iterables.tryFind(entityChanges, (Predicate)new Object(this, propertyName));
    }


    private Set<PK> getExpectedOneToManyTargetPks(List<InsertOneToManyRelationRecord> records)
    {
        return (Set<PK>)ImmutableSet.builder().addAll(Iterables.transform(records, (Function)new Object(this)))
                        .build();
    }


    private Optional<String> getForeignKeyColumnName(TypeInfoMap infoMap, RelationMetaInfo relationMetaInfo)
    {
        int propertyType = infoMap.getPropertyType(relationMetaInfo.getForeignKeyOnTarget());
        TypeInfoMap.PropertyColumnInfo info = null;
        switch(propertyType)
        {
            case 2:
                info = infoMap.getInfoForCoreProperty(relationMetaInfo.getForeignKeyOnTarget());
                break;
            case 0:
                info = infoMap.getInfoForProperty(relationMetaInfo.getForeignKeyOnTarget(), false);
                break;
            case 1:
                info = infoMap.getInfoForProperty(relationMetaInfo.getForeignKeyOnTarget(), true);
                break;
        }
        return (info == null) ? Optional.absent() : Optional.of(info.getColumnName());
    }


    private void addPositionsForOneToManyTargetsInEntities(Collection<EntityRecord> entityRecords, String foreignKeyOnTarget, List<InsertOneToManyRelationRecord> relationRecords)
    {
        IterableFinder<EntityRecord, PK> entityRecordFinder = IterableFinder.searchIn(entityRecords);
        for(InsertOneToManyRelationRecord record : relationRecords)
        {
            if(!record.isForProcessing())
            {
                EntityRecord entityRecord = (EntityRecord)entityRecordFinder.find(record.getTargetPk(), this.entityToPkTransformer);
                if(entityRecord instanceof InsertRecord)
                {
                    Set<PropertyHolder> entityChanges = ((InsertRecord)entityRecord).getChanges();
                    Optional<PropertyHolder> holder = findPropertyHolderForName(entityChanges, foreignKeyOnTarget + "pos");
                    if(!holder.isPresent())
                    {
                        ((InsertRecord)entityRecord).getChanges()
                                        .add(new PropertyHolder(foreignKeyOnTarget + "pos", record
                                                        .getSourceToTargetPosition()));
                    }
                }
            }
        }
    }


    private Set<DefaultRelationRecordVisitor.RelationRecordsContainer> prepareRelationContainers(Map<String, RelationChanges> relationChanges)
    {
        if(MapUtils.isNotEmpty(relationChanges))
        {
            Set<DefaultRelationRecordVisitor.RelationRecordsContainer> result = new LinkedHashSet<>();
            RelationChanges.RelationChangesVisitor<Set<DefaultRelationRecordVisitor.RelationRecordsContainer>> relChangesVisitor = this.recordVisitorFactory.createRelationChangesVisitor(this.localizationService);
            for(Map.Entry<String, RelationChanges> entry : relationChanges.entrySet())
            {
                RelationChanges changes = entry.getValue();
                if(changes != null)
                {
                    result.addAll((Collection<? extends DefaultRelationRecordVisitor.RelationRecordsContainer>)changes.accept(changes.getRelationMetaInfo(), relChangesVisitor));
                }
            }
            return result;
        }
        return Collections.emptySet();
    }


    private void processRelationContainersM2M(Set<DefaultRelationRecordVisitor.RelationRecordsContainer> containers, BatchCollector batchCollector, Set<PersistResult> results)
    {
        for(DefaultRelationRecordVisitor.RelationRecordsContainer container : containers)
        {
            results.addAll(processInsUpdManyToManyRelationRecords(container, batchCollector));
            results.addAll(processDeleteManyToManyRelationRecords(container, batchCollector));
        }
    }


    private void processRelationContainersO2M(Set<DefaultRelationRecordVisitor.RelationRecordsContainer> containers, BatchCollector batchCollector, Set<PersistResult> results)
    {
        for(DefaultRelationRecordVisitor.RelationRecordsContainer container : containers)
        {
            results.addAll(processOneToManyRelationRecords(container, batchCollector));
        }
    }


    private Set<PersistResult> processInsUpdManyToManyRelationRecords(DefaultRelationRecordVisitor.RelationRecordsContainer container, BatchCollector collector)
    {
        ModifyManyToManyRelationStatementsBuilder builder = ModifyManyToManyRelationStatementsBuilder.getInstance(this.jdbcTemplate, container, this.databaseName);
        Set<StatementHolder> statements = builder.createStatements();
        collectQueries(collector, statements);
        return builder.getPersistResults();
    }


    private Set<PersistResult> processDeleteManyToManyRelationRecords(DefaultRelationRecordVisitor.RelationRecordsContainer container, BatchCollector collector)
    {
        Set<PersistResult> results = new HashSet<>();
        for(RemoveManyToManyRelationsRecord record : container.getRemoveRelationsRecords())
        {
            RemoveManyToManyRelationStatementsBuilder builder = RemoveManyToManyRelationStatementsBuilder.builder(this.jdbcTemplate, this.databaseName).forRelation(container.getMetaInfo().getRelationName()).usingRecord(record).build();
            Set<StatementHolder> statements = builder.createStatements();
            collectQueries(collector, statements);
            results.addAll(builder.getPersistResults());
        }
        return results;
    }


    private Set<PersistResult> processOneToManyRelationRecords(DefaultRelationRecordVisitor.RelationRecordsContainer container, BatchCollector collector)
    {
        ModifyOneToManyRelationStatementsBuilder builder = ModifyOneToManyRelationStatementsBuilder.getInstance(container, this.databaseName);
        Set<StatementHolder> statements = builder.createStatements();
        collectQueries(collector, statements);
        return builder.getPersistResults();
    }


    private void collectQueries(BatchCollector collector, Set<StatementHolder> statements)
    {
        for(StatementHolder holder : statements)
        {
            if(holder.isSetterBased())
            {
                collector.collectQuery(holder.getStatement(), holder.getSetter());
                continue;
            }
            collector.collectQuery(holder.getStatement(), holder.getParams());
        }
    }


    @PostConstruct
    public void init()
    {
        this.databaseName = Config.getDatabaseName();
    }


    @Required
    public void setRecordVisitorFactory(RecordVisitorFactory recordVisitorFactory)
    {
        this.recordVisitorFactory = recordVisitorFactory;
    }


    @Required
    public void setTransactionTemplate(TransactionTemplate transactionTemplate)
    {
        this.transactionTemplate = transactionTemplate;
    }


    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Required
    public void setCacheInvalidator(CacheInvalidator cacheInvalidator)
    {
        this.cacheInvalidator = cacheInvalidator;
    }


    @Required
    public void setBatchCollectorFactory(BatchCollectorFactory batchCollectorFactory)
    {
        this.batchCollectorFactory = batchCollectorFactory;
    }


    @Required
    public void setLocalizationService(LocalizationService localizationService)
    {
        this.localizationService = localizationService;
    }
}
