package de.hybris.platform.directpersistence.statement;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.DirectPersistenceUtils;
import de.hybris.platform.directpersistence.impl.DefaultPersistResult;
import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.directpersistence.record.InsertOneToManyRelationRecord;
import de.hybris.platform.directpersistence.record.visitor.DefaultRelationRecordVisitor;
import de.hybris.platform.directpersistence.setter.UpdateOneToManyTargetStatementSetter;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class ModifyOneToManyRelationStatementsBuilder extends AbstractStoreStatementsBuilder implements StatementsBuilder
{
    private final DefaultRelationRecordVisitor.RelationRecordsContainer container;
    private final Config.DatabaseName databaseName;


    private ModifyOneToManyRelationStatementsBuilder(DefaultRelationRecordVisitor.RelationRecordsContainer container, Config.DatabaseName databaseName)
    {
        Preconditions.checkNotNull(container, "container is required");
        Preconditions.checkNotNull(container.getMetaInfo(), "relation meta info is required");
        this.container = container;
        this.infoMap = DirectPersistenceUtils.getInfoMapForType(container.getMetaInfo().getTargetTypeCode());
        this.databaseName = databaseName;
    }


    public static ModifyOneToManyRelationStatementsBuilder getInstance(DefaultRelationRecordVisitor.RelationRecordsContainer container, Config.DatabaseName databaseName)
    {
        if(Config.DatabaseName.SQLSERVER == databaseName)
        {
            return (ModifyOneToManyRelationStatementsBuilder)new MSSQLModifyOneToManyRelationsStatementBuilder(container, databaseName);
        }
        if(Config.DatabaseName.HANA == databaseName)
        {
            return (ModifyOneToManyRelationStatementsBuilder)new HanaModifyOneToManyRelationsStatementBuilder(container, databaseName);
        }
        return new ModifyOneToManyRelationStatementsBuilder(container, databaseName);
    }


    public Set<StatementHolder> createStatements()
    {
        Map<PK, List<InsertOneToManyRelationRecord>> records = this.container.getOneToManyRelationRecords();
        if(records.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<StatementHolder> result = new LinkedHashSet<>();
        for(Map.Entry<PK, List<InsertOneToManyRelationRecord>> entry : records.entrySet())
        {
            PK sourcePk = entry.getKey();
            ColumnPayload columnPayload = DirectPersistenceUtils.createColumnPayloadForProperty(this.container.getMetaInfo()
                            .getForeignKeyOnTarget(), null, this.infoMap);
            if(columnPayload != null)
            {
                result.addAll(linkTargets(sourcePk, entry.getValue(), columnPayload));
                result.addAll(unlinkOrRemoveTargets(sourcePk, columnPayload));
            }
        }
        return result;
    }


    private Set<StatementHolder> linkTargets(PK sourcePk, List<InsertOneToManyRelationRecord> records, ColumnPayload columnPayload)
    {
        Set<StatementHolder> result = new LinkedHashSet<>();
        Iterable<InsertOneToManyRelationRecord> recordsForProcessing = getRecordsForProcessing(records);
        if(!Iterables.isEmpty(recordsForProcessing))
        {
            Iterable<PK> targetPks = transformToTargetPks(recordsForProcessing);
            if(this.container.getMetaInfo().isSourceOrdered())
            {
                for(InsertOneToManyRelationRecord record : recordsForProcessing)
                {
                    String statement = getUpdateRowStatement(columnPayload);
                    result.add(new StatementHolder(statement, (PreparedStatementSetter)new UpdateOneToManyTargetStatementSetter(Lists.newArrayList((Object[])new PK[] {record
                                    .getTargetPk()}, ), sourcePk, record.getSourceToTargetPosition())));
                }
            }
            else
            {
                String statement = getUpdateAllStatement(columnPayload, recordsForProcessing);
                result.add(new StatementHolder(statement, (PreparedStatementSetter)new UpdateOneToManyTargetStatementSetter(targetPks, sourcePk)));
            }
            collectPersistResults(targetPks, CrudEnum.UPDATE);
        }
        return result;
    }


    private Iterable<PK> transformToTargetPks(Iterable<InsertOneToManyRelationRecord> records)
    {
        return Iterables.transform(records, (Function)new Object(this));
    }


    private Iterable<InsertOneToManyRelationRecord> getRecordsForProcessing(List<InsertOneToManyRelationRecord> records)
    {
        return Iterables.filter(records, (Predicate)new Object(this));
    }


    private Set<StatementHolder> unlinkOrRemoveTargets(PK sourcePk, ColumnPayload columnPayload)
    {
        Set<StatementHolder> result = new LinkedHashSet<>();
        Set<PK> pksMarkedToRemove = (Set<PK>)this.container.getOneToManyPksToRemove().get(sourcePk);
        if(!pksMarkedToRemove.isEmpty() && !this.container.getMetaInfo().isSourcePartOf())
        {
            unlinkTargets(columnPayload, result, pksMarkedToRemove);
        }
        return result;
    }


    private void unlinkTargets(ColumnPayload columnPayload, Set<StatementHolder> result, Set<PK> recordsToUnlink)
    {
        if(this.container.getMetaInfo().isSourceOrdered())
        {
            unlinkTargetsFromOrderedSource(columnPayload, result, recordsToUnlink);
        }
        else
        {
            unlinkTargetsFromUnorderedSource(columnPayload, result, recordsToUnlink);
        }
        collectPersistResults(recordsToUnlink, CrudEnum.UPDATE);
    }


    protected void unlinkTargetsFromOrderedSource(ColumnPayload columnPayload, Set<StatementHolder> result, Set<PK> recordsToUnlink)
    {
        UpdateOneToManyTargetStatementSetter statementSetter = new UpdateOneToManyTargetStatementSetter(recordsToUnlink, null, UpdateOneToManyTargetStatementSetter.RESET_POSITION);
        result.add(new StatementHolder(
                        FluentSqlBuilder.builder(this.databaseName)
                                        .update(this.infoMap.getItemTableName())
                                        .set(new String[] {getPosColumn(), columnPayload
                                                        .getColumnName()}, ).where()
                                        .field(ServiceCol.PK_STRING.colName())
                                        .in(recordsToUnlink)
                                        .toSql(), (PreparedStatementSetter)statementSetter));
    }


    protected void unlinkTargetFromOrderedSourceWithLimit(ColumnPayload columnPayload, Set<StatementHolder> result, Set<PK> recordsToUnlink, int parametersLimit)
    {
        List<List<PK>> partition = Lists.partition(Lists.newArrayList(recordsToUnlink), parametersLimit);
        for(List<PK> part : partition)
        {
            UpdateOneToManyTargetStatementSetter setter = new UpdateOneToManyTargetStatementSetter(part, null, UpdateOneToManyTargetStatementSetter.RESET_POSITION);
            String statement = FluentSqlBuilder.builder(this.databaseName).update(this.infoMap.getItemTableName()).set(new String[] {getPosColumn(), columnPayload.getColumnName()}).where().field(ServiceCol.PK_STRING.colName()).in(part).toSql();
            result.add(new StatementHolder(statement, (PreparedStatementSetter)setter));
        }
    }


    protected void unlinkTargetsFromUnorderedSource(ColumnPayload columnPayload, Set<StatementHolder> result, Set<PK> recordsToUnlink)
    {
        result.add(new StatementHolder(getUpdateAllStatement(columnPayload, recordsToUnlink), (PreparedStatementSetter)new UpdateOneToManyTargetStatementSetter(recordsToUnlink)));
    }


    protected void unlinkTargetsFromUnorderedSourceWithLimit(ColumnPayload columnPayload, Set<StatementHolder> result, Set<PK> recordsToUnlink, int parametersLimit)
    {
        List<List<PK>> partition = Lists.partition(Lists.newArrayList(recordsToUnlink), parametersLimit);
        for(List<PK> part : partition)
        {
            result.add(new StatementHolder(getUpdateAllStatement(columnPayload, part), (PreparedStatementSetter)new UpdateOneToManyTargetStatementSetter(part)));
        }
    }


    protected <T> String getUpdateAllStatement(ColumnPayload columnPayload, Iterable<T> toUpdate)
    {
        return FluentSqlBuilder.builder(this.databaseName)
                        .update(this.infoMap.getItemTableName())
                        .set(new String[] {columnPayload.getColumnName()}).where()
                        .field(ServiceCol.PK_STRING.colName())
                        .in(toUpdate)
                        .toSql();
    }


    private String getUpdateRowStatement(ColumnPayload columnPayload)
    {
        FluentSqlBuilder builder = FluentSqlBuilder.builder(this.databaseName).update(this.infoMap.getItemTableName());
        if(this.container.getMetaInfo().isSourceOrdered())
        {
            builder.set(new String[] {getPosColumn(), columnPayload.getColumnName()});
        }
        else
        {
            builder.set(new String[] {columnPayload.getColumnName()});
        }
        return builder.where().field(ServiceCol.PK_STRING.colName()).isEqual().toSql();
    }


    protected String getPosColumn()
    {
        TypeInfoMap.PropertyColumnInfo info = this.infoMap.getInfoForProperty(this.container.getMetaInfo().getForeignKeyOnTarget() + "pos", false);
        return info.getColumnName();
    }


    protected void collectPersistResults(Iterable<PK> records, CrudEnum operation)
    {
        for(PK pk : records)
        {
            this.persistResults.add(new DefaultPersistResult(operation, pk, Long.valueOf(0L), this.infoMap.getCode()));
        }
    }
}
