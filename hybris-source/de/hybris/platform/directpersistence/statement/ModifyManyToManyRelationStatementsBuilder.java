package de.hybris.platform.directpersistence.statement;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.DirectPersistenceUtils;
import de.hybris.platform.directpersistence.impl.DefaultPersistResult;
import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.directpersistence.record.InsertManyToManyRelationRecord;
import de.hybris.platform.directpersistence.record.visitor.DefaultRelationRecordVisitor;
import de.hybris.platform.directpersistence.setter.InsertPreparedStatementSetter;
import de.hybris.platform.directpersistence.setter.RemoveItemsStatementSetter;
import de.hybris.platform.directpersistence.setter.UpdateManyToManyLinkStatementSetter;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.util.Config;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ModifyManyToManyRelationStatementsBuilder extends AbstractManyToManyRelationStatementsBuilder implements StatementsBuilder
{
    private static final Logger LOG = Logger.getLogger(ModifyManyToManyRelationStatementsBuilder.class);
    protected final DefaultRelationRecordVisitor.RelationRecordsContainer container;
    private final Config.DatabaseName databaseName;
    private ConcurrentMap<PK, AtomicInteger> maxSequenceNumbers;
    private ConcurrentMap<PK, AtomicInteger> maxRevSequenceNumbers;
    private List<AbstractManyToManyRelationStatementsBuilder.LinkRow> existingLinkRows;


    private ModifyManyToManyRelationStatementsBuilder(JdbcTemplate jdbcTemplate, DefaultRelationRecordVisitor.RelationRecordsContainer container, Config.DatabaseName databaseName)
    {
        super(jdbcTemplate);
        Preconditions.checkNotNull(container, "Records Container is required");
        Preconditions.checkNotNull(databaseName, "Current databaseName is required");
        this.infoMap = DirectPersistenceUtils.getInfoMapForType(container.getMetaInfo().getRelationName());
        this.container = container;
        this.databaseName = databaseName;
    }


    public static ModifyManyToManyRelationStatementsBuilder getInstance(JdbcTemplate jdbcTemplate, DefaultRelationRecordVisitor.RelationRecordsContainer container, Config.DatabaseName databaseName)
    {
        if(Config.DatabaseName.SQLSERVER == databaseName)
        {
            return (ModifyManyToManyRelationStatementsBuilder)new MSSQLModifyManyToManyRelationsStatementBuilder(jdbcTemplate, container, databaseName);
        }
        if(Config.DatabaseName.HANA == databaseName)
        {
            return (ModifyManyToManyRelationStatementsBuilder)new HanaModifyManyToManyRelationsStatementBuilder(jdbcTemplate, container, databaseName);
        }
        return new ModifyManyToManyRelationStatementsBuilder(jdbcTemplate, container, databaseName);
    }


    public Set<StatementHolder> createStatements()
    {
        Map<PK, List<InsertManyToManyRelationRecord>> sourceToTargetRecords = this.container.getSourceToTargetRecords();
        Map<PK, List<InsertManyToManyRelationRecord>> targetToSourceRecords = this.container.getTargetToSourceRecords();
        if(sourceToTargetRecords.isEmpty() && targetToSourceRecords.isEmpty())
        {
            return Collections.emptySet();
        }
        this.maxSequenceNumbers = findSeqNumbers();
        this.maxRevSequenceNumbers = findRevSeqNumbers();
        this.existingLinkRows = findExistingLinkRows();
        Set<StatementHolder> result = new LinkedHashSet<>();
        addStatementsFromSourceRecordsToResult(result);
        addStatementsFromTargetRecordsToResult(result);
        return result;
    }


    private ConcurrentMap<PK, AtomicInteger> findSeqNumbers()
    {
        String statement = FluentSqlBuilder.builder(this.databaseName).select(new String[] {ServiceCol.SOURCE_PK.colName(), FluentSqlBuilder.max(ServiceCol.SEQUENCE_NUMBER.colName())}).from(this.infoMap.getItemTableName()).where().field(ServiceCol.SOURCE_PK.colName())
                        .in(this.container.getSourcePksAsLongs()).groupBy(new String[] {ServiceCol.SOURCE_PK.colName()}).toSql();
        if(LOG.isDebugEnabled())
        {
            logStatement(statement, this.container.getSourcePksAsLongs());
        }
        return findMaxSeqNumbers(statement, this.container.getSourcePksAsLongs());
    }


    private void logStatement(String statement, Collection<Long> sourcePksAsLongs)
    {
        LOG.debug(MessageFormat.format("Statement: {0}. Params: {1}", new Object[] {statement, sourcePksAsLongs}));
    }


    private ConcurrentMap<PK, AtomicInteger> findRevSeqNumbers()
    {
        String statement = FluentSqlBuilder.builder(this.databaseName).select(new String[] {ServiceCol.TARGET_PK.colName(), FluentSqlBuilder.max(ServiceCol.RSEQUENCE_NUMBER.colName())}).from(this.infoMap.getItemTableName()).where().field(ServiceCol.TARGET_PK.colName())
                        .in(this.container.getTargetPksAsLongs()).groupBy(new String[] {ServiceCol.TARGET_PK.colName()}).toSql();
        if(LOG.isDebugEnabled())
        {
            logStatement(statement, this.container.getTargetPksAsLongs());
        }
        return findMaxSeqNumbers(statement, this.container.getTargetPksAsLongs());
    }


    protected ConcurrentMap<PK, AtomicInteger> findMaxSeqNumbers(String statement, Iterable<Long> pksAsLongs)
    {
        return (ConcurrentMap<PK, AtomicInteger>)this.jdbcTemplate.query(statement, getMaxSeqNumbersResultSetExtractor(), Iterables.toArray(pksAsLongs, Object.class));
    }


    protected ResultSetExtractor<ConcurrentMap<PK, AtomicInteger>> getMaxSeqNumbersResultSetExtractor()
    {
        return (ResultSetExtractor<ConcurrentMap<PK, AtomicInteger>>)new Object(this);
    }


    private List<AbstractManyToManyRelationStatementsBuilder.LinkRow> findExistingLinkRows()
    {
        Pair<String, List<PK>> statement = getExistingLinkRowsStatement();
        if(statement != null)
        {
            List<Long> params = (List<Long>)((List)statement.getRight()).stream().map(PK::getLong).collect(Collectors.toList());
            String query = (String)statement.getLeft();
            if(LOG.isDebugEnabled())
            {
                logStatement(query, params);
            }
            return queryForExistingLinkRows(query, params);
        }
        return Collections.emptyList();
    }


    protected List<AbstractManyToManyRelationStatementsBuilder.LinkRow> queryForExistingLinkRows(String statement, List<Long> params)
    {
        return this.jdbcTemplate.query(statement, this.linkRowMapper, Iterables.toArray(params, Object.class));
    }


    private Pair<String, List<PK>> getExistingLinkRowsStatement()
    {
        Set<PK> sourcePks = this.container.getSourceToTargetRecords().keySet();
        Set<PK> targetPks = this.container.getTargetToSourceRecords().keySet();
        Pair<FluentSqlBuilder, List<PK>> query1 = null;
        Pair<FluentSqlBuilder, List<PK>> query2 = null;
        if(!Iterables.isEmpty(sourcePks))
        {
            query1 = getFindLinksStatement(sourcePks, true);
        }
        if(!Iterables.isEmpty(targetPks))
        {
            query2 = getFindLinksStatement(targetPks, false);
        }
        if(query1 == null && query2 == null)
        {
            return null;
        }
        if(query1 != null && query2 == null)
        {
            return Pair.of(((FluentSqlBuilder)query1.getLeft()).toSql(), query1.getRight());
        }
        if(query1 == null)
        {
            return Pair.of(((FluentSqlBuilder)query2.getLeft()).toSql(), query2.getRight());
        }
        List<PK> params = new LinkedList<>((Collection<? extends PK>)query1.getRight());
        params.addAll((Collection<? extends PK>)query2.getRight());
        return Pair.of(((FluentSqlBuilder)query1.getLeft()).union((FluentSqlBuilder)query2.getLeft()).toSql(), params);
    }


    private Pair<FluentSqlBuilder, List<PK>> getFindLinksStatement(Set<PK> pks, boolean sourceToTarget)
    {
        FluentSqlBuilder builder = FluentSqlBuilder.builder(this.databaseName).select(new String[] {ServiceCol.PK_STRING.colName(), ServiceCol.TARGET_PK.colName(), ServiceCol.SOURCE_PK.colName(), ServiceCol.SEQUENCE_NUMBER.colName(), ServiceCol.RSEQUENCE_NUMBER.colName()})
                        .from(this.infoMap.getItemTableName()).where().field(sourceToTarget ? ServiceCol.SOURCE_PK.colName() : ServiceCol.TARGET_PK.colName()).in(pks);
        List<PK> params = new LinkedList<>(pks);
        if(this.container.getLanguagePk() != null)
        {
            builder.and().field(ServiceCol.LANGUAGEPK.colName()).isEqual();
            params.add(this.container.getLanguagePk());
        }
        else
        {
            builder.and().field(ServiceCol.LANGUAGEPK.colName()).isNull();
        }
        return Pair.of(builder, params);
    }


    private void addStatementsFromSourceRecordsToResult(Set<StatementHolder> result)
    {
        addStatementsToResult(result, this.container.getSourceToTargetRecords(), true);
    }


    private void addStatementsFromTargetRecordsToResult(Set<StatementHolder> result)
    {
        addStatementsToResult(result, this.container.getTargetToSourceRecords(), false);
    }


    private void addStatementsToResult(Set<StatementHolder> result, Map<PK, List<InsertManyToManyRelationRecord>> changes, boolean sourceToTarget)
    {
        for(Map.Entry<PK, List<InsertManyToManyRelationRecord>> entry : changes.entrySet())
        {
            PK sourceOrTargetPk = entry.getKey();
            List<InsertManyToManyRelationRecord> linksToAdd = entry.getValue();
            addInsertsOrUpdatesToResult(result, linksToAdd);
            if(sourceToTarget)
            {
                addDeletesFromSrcToTrgToResult(result, sourceOrTargetPk, linksToAdd);
                continue;
            }
            addDeletesFromTrgToSrcToResult(result, sourceOrTargetPk, linksToAdd);
        }
    }


    private void addInsertsOrUpdatesToResult(Set<StatementHolder> result, List<InsertManyToManyRelationRecord> records)
    {
        for(InsertManyToManyRelationRecord record : records)
        {
            Optional<AbstractManyToManyRelationStatementsBuilder.LinkRow> existingRow = getExistingLinkRow(record.getSourcePk(), record.getTargetPk());
            if(!existingRow.isPresent())
            {
                addInsertToResult(result, record);
                continue;
            }
            addUpdateToResultIfRequired(result, record, (AbstractManyToManyRelationStatementsBuilder.LinkRow)existingRow.get());
        }
    }


    private Optional<AbstractManyToManyRelationStatementsBuilder.LinkRow> getExistingLinkRow(PK sourcePk, PK targetPk)
    {
        return Iterables.tryFind(this.existingLinkRows, (Predicate)new Object(this, sourcePk, targetPk));
    }


    private void addInsertToResult(Set<StatementHolder> result, InsertManyToManyRelationRecord record)
    {
        PK linkPk = PK.createCounterPK(this.infoMap.getItemTypeCode());
        Set<ColumnPayload> payload = buildColumnPayload(record, linkPk);
        String statement = FluentSqlBuilder.builder(this.databaseName).insert().into(this.infoMap.getItemTableName()).usingFields(Iterables.transform(payload, (Function)AbstractStoreStatementsBuilder.ColumnPayloadTransformer.COL_NAME)).values(payload).toSql();
        if(LOG.isDebugEnabled())
        {
            LOG.debug(MessageFormat.format("Statement: {0}. Params: {1}", new Object[] {statement, payload}));
        }
        result.add(new StatementHolder(statement, (PreparedStatementSetter)new InsertPreparedStatementSetter(payload)));
        this.persistResults.add(new DefaultPersistResult(CrudEnum.CREATE, linkPk, null, this.infoMap.getCode()));
    }


    private void addUpdateToResultIfRequired(Set<StatementHolder> result, InsertManyToManyRelationRecord record, AbstractManyToManyRelationStatementsBuilder.LinkRow existingRow)
    {
        if((record.getSourceToTargetPosition() != null && !existingRow.getSeqNumber().equals(record.getSourceToTargetPosition())) || (record
                        .getTargetToSourcePosition() != null &&
                        !existingRow.getRSeqNumber().equals(record.getTargetToSourcePosition())))
        {
            Set<ColumnPayload> payload = buildColumnPayloadForUpdate(record);
            String statement = FluentSqlBuilder.builder(this.databaseName).update(this.infoMap.getItemTableName()).set(Iterables.transform(payload, (Function)AbstractStoreStatementsBuilder.ColumnPayloadTransformer.COL_NAME)).where().field(ServiceCol.SOURCE_PK.colName()).isEqual().and()
                            .field(ServiceCol.TARGET_PK.colName()).isEqual().toSql();
            if(LOG.isDebugEnabled())
            {
                LOG.debug(MessageFormat.format("Update relation statement: {0}. Params: {1}", new Object[] {statement, payload}));
            }
            result.add(new StatementHolder(statement, (PreparedStatementSetter)new UpdateManyToManyLinkStatementSetter(record
                            .getSourcePk(), record.getTargetPk(), payload)));
            this.persistResults.add(new DefaultPersistResult(CrudEnum.UPDATE, existingRow.getPk(), null, this.infoMap.getCode()));
        }
    }


    private Set<ColumnPayload> buildColumnPayload(InsertManyToManyRelationRecord record, PK relPk)
    {
        Date now = new Date();
        Set<ColumnPayload> payload = new LinkedHashSet<>();
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.HJMP_TS).value(Long.valueOf(0L)).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.TYPE_PK_STR).value(this.infoMap.getTypePK()).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.PK_STRING).value(relPk).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.CREATED_TS).value(now).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.MODIFIED_TS).value(now).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.OWNER_PK_STRING).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.ACL_TS).value(Long.valueOf(0L)).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.PROP_TS).value(Long.valueOf(0L)).build());
        payload.add(
                        ColumnPayload.builder()
                                        .serviceCol(ServiceCol.QUALIFIER)
                                        .value(this.container.getMetaInfo().getRelationName())
                                        .build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.SOURCE_PK).value(record.getSourcePk()).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.TARGET_PK).value(record.getTargetPk()).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.SEQUENCE_NUMBER).value(getSequenceNumber(record)).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.RSEQUENCE_NUMBER).value(getRevSequenceNumber(record)).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.LANGUAGEPK).value(this.container.getLanguagePk()).build());
        return payload;
    }


    private Set<ColumnPayload> buildColumnPayloadForUpdate(InsertManyToManyRelationRecord record)
    {
        Date now = new Date();
        Set<ColumnPayload> payload = new LinkedHashSet<>();
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.MODIFIED_TS).value(now).build());
        if(record.getSourceToTargetPosition() != null)
        {
            payload.add(
                            ColumnPayload.builder()
                                            .serviceCol(ServiceCol.SEQUENCE_NUMBER)
                                            .value(record.getSourceToTargetPosition())
                                            .build());
        }
        if(record.getTargetToSourcePosition() != null)
        {
            payload.add(
                            ColumnPayload.builder()
                                            .serviceCol(ServiceCol.RSEQUENCE_NUMBER)
                                            .value(record.getTargetToSourcePosition())
                                            .build());
        }
        return payload;
    }


    private Integer getSequenceNumber(InsertManyToManyRelationRecord record)
    {
        if(!this.container.getMetaInfo().isSourceOrdered())
        {
            return Integer.valueOf(0);
        }
        AtomicInteger max = this.maxSequenceNumbers.get(record.getSourcePk());
        if(max == null)
        {
            max = new AtomicInteger(0);
            this.maxSequenceNumbers.put(record.getSourcePk(), max);
        }
        int nextMax = max.getAndIncrement();
        return (record.getSourceToTargetPosition() != null) ? record.getSourceToTargetPosition() : Integer.valueOf(nextMax);
    }


    private Integer getRevSequenceNumber(InsertManyToManyRelationRecord record)
    {
        if(!this.container.getMetaInfo().isTargetOrdered())
        {
            return Integer.valueOf(0);
        }
        AtomicInteger max = this.maxRevSequenceNumbers.get(record.getTargetPk());
        if(max == null)
        {
            max = new AtomicInteger(0);
            this.maxRevSequenceNumbers.put(record.getTargetPk(), max);
        }
        int nextMax = max.getAndIncrement();
        return (record.getTargetToSourcePosition() != null) ? record.getTargetToSourcePosition() : Integer.valueOf(nextMax);
    }


    private void addDeletesFromSrcToTrgToResult(Set<StatementHolder> result, PK pk, List<InsertManyToManyRelationRecord> records)
    {
        Iterable<AbstractManyToManyRelationStatementsBuilder.LinkRow> sourceRows = Iterables.filter(this.existingLinkRows, (Predicate)new Object(this, pk));
        Iterable<PK> targetPks = Iterables.transform(records, (Function)new Object(this));
        Object object = new Object(this);
        processRowsToDelete(result, (Set<AbstractManyToManyRelationStatementsBuilder.LinkRow>)ImmutableSet.copyOf(sourceRows), (Set<PK>)ImmutableSet.copyOf(targetPks), (Function<AbstractManyToManyRelationStatementsBuilder.LinkRow, PK>)object);
    }


    private void addDeletesFromTrgToSrcToResult(Set<StatementHolder> result, PK pk, List<InsertManyToManyRelationRecord> records)
    {
        Iterable<AbstractManyToManyRelationStatementsBuilder.LinkRow> targetRows = Iterables.filter(this.existingLinkRows, (Predicate)new Object(this, pk));
        Iterable<PK> sourcePks = Iterables.transform(records, (Function)new Object(this));
        Object object = new Object(this);
        processRowsToDelete(result, (Set<AbstractManyToManyRelationStatementsBuilder.LinkRow>)ImmutableSet.copyOf(targetRows), (Set<PK>)ImmutableSet.copyOf(sourcePks), (Function<AbstractManyToManyRelationStatementsBuilder.LinkRow, PK>)object);
    }


    private void processRowsToDelete(Set<StatementHolder> result, Set<AbstractManyToManyRelationStatementsBuilder.LinkRow> rows, Set<PK> pks, Function<AbstractManyToManyRelationStatementsBuilder.LinkRow, PK> linkRowToPkTransformer)
    {
        Set<Long> toDelete = null;
        for(AbstractManyToManyRelationStatementsBuilder.LinkRow row : rows)
        {
            if(!pks.contains(linkRowToPkTransformer.apply(row)))
            {
                if(toDelete == null)
                {
                    toDelete = new HashSet<>();
                }
                toDelete.add(Long.valueOf(row.getPk().getLongValue()));
                this.persistResults.add(new DefaultPersistResult(CrudEnum.DELETE, row.getPk(), null, this.infoMap.getCode()));
            }
        }
        if(toDelete != null && !toDelete.isEmpty())
        {
            result.addAll(getDeleteStatements(toDelete));
        }
    }


    protected Set<StatementHolder> getDeleteStatements(Set<Long> params)
    {
        String query = FluentSqlBuilder.builder(this.databaseName).delete().from(this.infoMap.getItemTableName()).where().field(ServiceCol.PK_STRING.colName()).in(params).toSql();
        return Sets.newHashSet((Object[])new StatementHolder[] {new StatementHolder(query, (PreparedStatementSetter)new RemoveItemsStatementSetter(Lists.newArrayList(params)))});
    }


    protected Set<StatementHolder> getDeleteStatementsWithLimit(Set<Long> params, int parametersLimit)
    {
        Set<StatementHolder> result = new HashSet<>();
        List<List<Long>> partition = Lists.partition(Lists.newArrayList(params), parametersLimit);
        for(List<Long> parts : partition)
        {
            String query = FluentSqlBuilder.builder(this.databaseName).delete().from(this.infoMap.getItemTableName()).where().field(ServiceCol.PK_STRING.colName()).in(parts).toSql();
            result.add(new StatementHolder(query, (PreparedStatementSetter)new RemoveItemsStatementSetter(Lists.newArrayList(parts))));
        }
        return result;
    }
}
