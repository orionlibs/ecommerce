package de.hybris.platform.directpersistence.statement;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.impl.DefaultPersistResult;
import de.hybris.platform.directpersistence.impl.OptimisticLockingAndItemLockingResultCheck;
import de.hybris.platform.directpersistence.impl.UpdateRowResultCheck;
import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.directpersistence.record.impl.AbstractModificationRecord;
import de.hybris.platform.directpersistence.record.impl.UpdateRecord;
import de.hybris.platform.directpersistence.setter.InsertPreparedStatementSetter;
import de.hybris.platform.directpersistence.setter.InsertPropsPreparedStatementSetter;
import de.hybris.platform.directpersistence.setter.UpdateLocalizedPreparedStatementSetter;
import de.hybris.platform.directpersistence.setter.UpdatePreparedStatementSetter;
import de.hybris.platform.directpersistence.setter.UpdatePropsPreparedStatementSetter;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class UpdateItemStatementsBuilder extends NewItemStatementsBuilder
{
    private final boolean optimisticLockingEnabled;
    private final JdbcTemplate jdbcTemplate;
    private final boolean incrementOptimisticLockCounter;
    private final boolean checkBlockedProperty;


    public UpdateItemStatementsBuilder(UpdateRecord record, LocalizationService localizationService, boolean optimisticLockingEnabled, JdbcTemplate jdbcTemplate, Config.DatabaseName databaseName)
    {
        super((AbstractModificationRecord)record, localizationService, databaseName);
        this.currentOptimisticLockCounter = new AtomicLong(record.getVersion());
        this.optimisticLockingEnabled = optimisticLockingEnabled;
        this.jdbcTemplate = jdbcTemplate;
        this.incrementOptimisticLockCounter = record.getIncrementOptimisticLockCounter();
        this.checkBlockedProperty = record.checkIsLockedProperty();
    }


    public Set<StatementHolder> createStatements()
    {
        Set<StatementHolder> result = new LinkedHashSet<>();
        addStandardTableUpdateToResult(result);
        addLpTableAndLocalizedPropsTableUpdateToResult(result);
        addUnlocalizedPropsTableChangesToResult(result);
        this.persistResults.add(new DefaultPersistResult(CrudEnum.UPDATE,
                        getItemPk(), getCurrentOptimistiLockCounter(), getTypeCode()));
        return result;
    }


    private void addStandardTableUpdateToResult(Set<StatementHolder> result)
    {
        Set<ColumnPayload> standardItemPayload = (Set<ColumnPayload>)this.dataBasePayload.get(ColumnPayload.TargetTableType.ITEM);
        long prevOptimisticLocCounter = this.currentOptimisticLockCounter.get();
        if(this.incrementOptimisticLockCounter)
        {
            this.currentOptimisticLockCounter.getAndIncrement();
        }
        Set<ColumnPayload> standardItemServicePayload = buildStandardServiceColumns();
        ImmutableSet immutableSet = Utilities.mergeToImmutableSet(standardItemServicePayload, standardItemPayload);
        addUpdateToResult(result, (Set<ColumnPayload>)immutableSet, prevOptimisticLocCounter, this.optimisticLockingEnabled);
    }


    private void addUpdateToResult(Set<StatementHolder> result, Set<ColumnPayload> payload, long prevOptimisticLocCounter, boolean withOptimisticLockingCheck)
    {
        FluentSqlBuilder builder = FluentSqlBuilder.builder(this.databaseName).update(this.infoMap.getItemTableName()).set(Iterables.transform(payload, (Function)AbstractStoreStatementsBuilder.ColumnPayloadTransformer.COL_NAME)).where().field(ServiceCol.PK_STRING.colName()).isEqual();
        if(withOptimisticLockingCheck)
        {
            builder.and().field(ServiceCol.HJMP_TS.colName()).isEqual().toSql();
        }
        if(this.checkBlockedProperty)
        {
            builder.and().fieldIsNullOr(ServiceCol.SEALED.colName()).toSql();
        }
        String updateStatement = builder.toSql();
        result.add(new StatementHolder(updateStatement, (PreparedStatementSetter)new UpdatePreparedStatementSetter(this.itemPk, payload,
                        Long.valueOf(prevOptimisticLocCounter), this.checkBlockedProperty, withOptimisticLockingCheck), (BatchCollector.ResultCheck)new OptimisticLockingAndItemLockingResultCheck(this.itemPk, this.jdbcTemplate, this.infoMap
                        .getItemTableName(), withOptimisticLockingCheck,
                        Long.valueOf(prevOptimisticLocCounter).longValue())));
    }


    private void addLpTableAndLocalizedPropsTableUpdateToResult(Set<StatementHolder> result)
    {
        if(!this.localizedDataBasePayload.isEmpty())
        {
            for(Map.Entry<PK, Map<ColumnPayload.TargetTableType, Set<ColumnPayload>>> entry : (Iterable<Map.Entry<PK, Map<ColumnPayload.TargetTableType, Set<ColumnPayload>>>>)this.localizedDataBasePayload
                            .entrySet())
            {
                PK langPk = entry.getKey();
                Map<ColumnPayload.TargetTableType, Set<ColumnPayload>> locChanges = entry.getValue();
                addLpTableChangesToResult(result, langPk, locChanges);
                addLocalizedPropsTableChangesToResult(langPk, locChanges.get(ColumnPayload.TargetTableType.PROPS), result);
            }
        }
    }


    private void addLpTableChangesToResult(Set<StatementHolder> result, PK langPk, Map<ColumnPayload.TargetTableType, Set<ColumnPayload>> locChanges)
    {
        Set<ColumnPayload> changesForLang = locChanges.get(ColumnPayload.TargetTableType.LP);
        if(!changesForLang.isEmpty())
        {
            String statement = FluentSqlBuilder.builder(this.databaseName).update(this.infoMap.getTableName(true)).set(Iterables.transform(changesForLang, (Function)AbstractStoreStatementsBuilder.ColumnPayloadTransformer.COL_NAME)).where().field(ServiceCol.ITEM_PK.colName()).isEqual().and()
                            .field(ServiceCol.LANG_PK.colName()).isEqual().toSql();
            UpdateLocalizedPreparedStatementSetter setter = new UpdateLocalizedPreparedStatementSetter(this.itemPk, langPk, changesForLang);
            UpdateRowResultCheck resultCheck = new UpdateRowResultCheck(createLpRescueStatement(langPk, changesForLang), this.jdbcTemplate);
            StatementHolder statementHolder = new StatementHolder(statement, (PreparedStatementSetter)setter, (BatchCollector.ResultCheck)resultCheck);
            resultCheck.setInitialQuery(statementHolder);
            result.add(statementHolder);
        }
    }


    public StatementHolder createLpRescueStatement(PK langPk, Set<ColumnPayload> changesForLang)
    {
        Set<ColumnPayload> localizedServiceColsPayload = buildLocalizedServiceColumnsPayload(langPk);
        ImmutableSet immutableSet = Utilities.mergeToImmutableSet(localizedServiceColsPayload, changesForLang);
        String statement = FluentSqlBuilder.builder(this.databaseName).insert().into(this.infoMap.getTableName(true)).usingFields(Iterables.transform((Iterable)immutableSet, (Function)AbstractStoreStatementsBuilder.ColumnPayloadTransformer.COL_NAME)).values((Set)immutableSet).toSql();
        return new StatementHolder(statement, (PreparedStatementSetter)new InsertPreparedStatementSetter((Set)immutableSet));
    }


    private void addLocalizedPropsTableChangesToResult(PK langPk, Set<ColumnPayload> propsChanges, Set<StatementHolder> result)
    {
        Long langPkAsLong = Long.valueOf(langPk.getLongValue());
        Long itemPkAsLong = Long.valueOf(this.itemPk.getLongValue());
        for(ColumnPayload columnPayload : propsChanges)
        {
            StatementHolder holder = prepareHolderForLocalizedProps(langPk, langPkAsLong, itemPkAsLong, columnPayload);
            result.add(holder);
        }
    }


    private StatementHolder prepareHolderForLocalizedProps(PK langPk, Long langPkAsLong, Long itemPkAsLong, ColumnPayload columnPayload)
    {
        StatementHolder holder;
        if(columnPayload.getValue() == null)
        {
            String statement = FluentSqlBuilder.builder(this.databaseName).delete().from(this.infoMap.getOldPropTableName()).where().field(ServiceCol.ITEM_PK.colName()).isEqual().and().field(ServiceCol.LANG_PK.colName()).isEqual().and().field(ServiceCol.REALNAME.colName()).isEqual().toSql();
            holder = new StatementHolder(statement, new Object[] {itemPkAsLong, langPkAsLong, columnPayload.getColumnName()});
        }
        else
        {
            Set<ColumnPayload> propsRealPayload = buildpPropsColumnsPayload(columnPayload, langPk);
            String statement = FluentSqlBuilder.builder(this.databaseName).update(this.infoMap.getOldPropTableName()).set(new String[] {ServiceCol.VALUESTRING1.colName(), ServiceCol.VALUE1.colName()}).where().field(ServiceCol.ITEM_PK.colName()).isEqual().and().field(ServiceCol.REALNAME.colName())
                            .isEqual().and().field(ServiceCol.LANG_PK.colName()).isEqual().toSql();
            UpdatePropsPreparedStatementSetter setter = new UpdatePropsPreparedStatementSetter(this.itemPk, langPk, columnPayload.getColumnName(), columnPayload.getValue());
            UpdateRowResultCheck resultCheck = new UpdateRowResultCheck(createPropsRescueStatement(propsRealPayload), this.jdbcTemplate);
            holder = new StatementHolder(statement, (PreparedStatementSetter)setter, (BatchCollector.ResultCheck)resultCheck);
            resultCheck.setInitialQuery(holder);
        }
        return holder;
    }


    private void addUnlocalizedPropsTableChangesToResult(Set<StatementHolder> result)
    {
        Set<ColumnPayload> propsTablePayload = (Set<ColumnPayload>)this.dataBasePayload.get(ColumnPayload.TargetTableType.PROPS);
        if(!propsTablePayload.isEmpty())
        {
            for(ColumnPayload propsPayload : propsTablePayload)
            {
                StatementHolder holder = prepareHolderForUnlocalizedProps(propsPayload);
                result.add(holder);
            }
        }
    }


    private StatementHolder prepareHolderForUnlocalizedProps(ColumnPayload propsPayload)
    {
        StatementHolder holder;
        if(propsPayload.getValue() == null)
        {
            Long itemPkAsLong = Long.valueOf(this.itemPk.getLongValue());
            String statement = FluentSqlBuilder.builder(this.databaseName).delete().from(this.infoMap.getOldPropTableName()).where().field(ServiceCol.ITEM_PK.colName()).isEqual().and().field(ServiceCol.REALNAME.colName()).isEqual().toSql();
            holder = new StatementHolder(statement, new Object[] {itemPkAsLong, propsPayload.getColumnName()});
        }
        else
        {
            Set<ColumnPayload> propsRealPayload = buildpPropsColumnsPayload(propsPayload, null);
            String statement = FluentSqlBuilder.builder(this.databaseName).update(this.infoMap.getOldPropTableName()).set(new String[] {ServiceCol.VALUESTRING1.colName(), ServiceCol.VALUE1.colName()}).where().field(ServiceCol.ITEM_PK.colName()).isEqual().and().field(ServiceCol.REALNAME.colName())
                            .isEqual().toSql();
            UpdatePropsPreparedStatementSetter setter = new UpdatePropsPreparedStatementSetter(this.itemPk, propsPayload.getColumnName(), propsPayload.getValue());
            UpdateRowResultCheck resultCheck = new UpdateRowResultCheck(createPropsRescueStatement(propsRealPayload), this.jdbcTemplate);
            holder = new StatementHolder(statement, (PreparedStatementSetter)setter, (BatchCollector.ResultCheck)resultCheck);
            resultCheck.setInitialQuery(holder);
        }
        return holder;
    }


    public StatementHolder createPropsRescueStatement(Set<ColumnPayload> propsRealPayload)
    {
        return new StatementHolder(FluentSqlBuilder.builder(this.databaseName)
                        .insert()
                        .into(this.infoMap.getOldPropTableName())
                        .usingFields(Iterables.transform(propsRealPayload, (Function)AbstractStoreStatementsBuilder.ColumnPayloadTransformer.COL_NAME))
                        .values(propsRealPayload)
                        .toSql(), (PreparedStatementSetter)new InsertPropsPreparedStatementSetter(propsRealPayload));
    }


    private Set<ColumnPayload> buildStandardServiceColumns()
    {
        Date now = new Date();
        Set<ColumnPayload> payload = new LinkedHashSet<>();
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.HJMP_TS).value(getCurrentOptimistiLockCounter()).build());
        payload.add(getTimeColumn(now, ServiceCol.MODIFIED_TS));
        return payload;
    }
}
