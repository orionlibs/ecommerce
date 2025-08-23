package de.hybris.platform.directpersistence.statement;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.impl.DefaultPersistResult;
import de.hybris.platform.directpersistence.impl.InsertRowResultChecker;
import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.impl.AbstractModificationRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.setter.InsertPreparedStatementSetter;
import de.hybris.platform.directpersistence.setter.InsertPropsPreparedStatementSetter;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class NewItemStatementsBuilder extends AbstractEntityStatementsBuilder implements StatementsBuilder
{
    private static final Logger LOG = Logger.getLogger(NewItemStatementsBuilder.class);
    protected final Map<ColumnPayload.TargetTableType, Set<ColumnPayload>> dataBasePayload;
    protected final Map<PK, Map<ColumnPayload.TargetTableType, Set<ColumnPayload>>> localizedDataBasePayload;
    protected boolean sateliteTablesChanged = false;


    public NewItemStatementsBuilder(AbstractModificationRecord record, LocalizationService localizationService, Config.DatabaseName databaseName)
    {
        super((EntityRecord)record, databaseName);
        Preconditions.checkArgument((localizationService != null), "localizationService is required");
        this.dataBasePayload = prepareDataBasePayload(record.getChanges(), this.infoMap);
        this.localizedDataBasePayload = prepareLocalizedDataBasePayload(record.getLocalizedChanges(), localizationService);
    }


    private Map<PK, Map<ColumnPayload.TargetTableType, Set<ColumnPayload>>> prepareLocalizedDataBasePayload(Map<Locale, Set<PropertyHolder>> localizedChanges, LocalizationService localizationService)
    {
        if(!localizedChanges.isEmpty())
        {
            Map<PK, Map<ColumnPayload.TargetTableType, Set<ColumnPayload>>> classifiedLocalizedPayload = new HashMap<>();
            for(Map.Entry<Locale, Set<PropertyHolder>> entry : localizedChanges.entrySet())
            {
                Locale locale = entry.getKey();
                Set<PropertyHolder> locChanges = entry.getValue();
                PK langPk = localizationService.getMatchingPkForDataLocale(locale);
                if(langPk != null)
                {
                    Map<ColumnPayload.TargetTableType, Set<ColumnPayload>> existingLoclizedPayload = classifiedLocalizedPayload.get(langPk);
                    if(existingLoclizedPayload == null)
                    {
                        existingLoclizedPayload = prepareDataBasePayload(locChanges, this.infoMap);
                    }
                    else
                    {
                        existingLoclizedPayload = mergeDataBasePayload(existingLoclizedPayload, locChanges, this.infoMap);
                    }
                    classifiedLocalizedPayload.put(langPk, existingLoclizedPayload);
                    continue;
                }
                LOG.warn("Trying to pass changes: [" + locChanges + "] with localized data for not supported locale: [" + locale + "]. Skipping!");
            }
            return (Map<PK, Map<ColumnPayload.TargetTableType, Set<ColumnPayload>>>)ImmutableMap.copyOf(classifiedLocalizedPayload);
        }
        return Collections.EMPTY_MAP;
    }


    public Set<StatementHolder> createStatements()
    {
        LinkedHashSet<StatementHolder> result = new LinkedHashSet<>();
        addStandardTableInsertToResult(result);
        addLpTableAndLocalizedPropsTableInsertsToResult(result);
        addUnlocalizedPropsTableInsertsToResult(result);
        this.persistResults.add(new DefaultPersistResult(CrudEnum.CREATE,
                        getItemPk(), getCurrentOptimistiLockCounter(), getTypeCode()));
        return result;
    }


    private void addStandardTableInsertToResult(Set<StatementHolder> result)
    {
        ImmutableSet immutableSet = Utilities.mergeToImmutableSet(buildStandardServiceColumns(), this.dataBasePayload
                        .get(ColumnPayload.TargetTableType.ITEM));
        if(this.infoMap.getItemTableName() == null)
        {
            throw new ModelSavingException("Item " + this.infoMap
                            .getCode() + " does not have table name defined (possibly trying to save abstract type)");
        }
        String sql = addOnConflictDoNothingClause(
                        FluentSqlBuilder.builder(this.databaseName).insert().into(this.infoMap.getItemTableName())
                                        .usingFields(Iterables.transform((Iterable)immutableSet, (Function)AbstractStoreStatementsBuilder.ColumnPayloadTransformer.COL_NAME))
                                        .values((Set)immutableSet).toSql());
        InsertRowResultChecker resultCheck = new InsertRowResultChecker(this.infoMap.getItemTableName());
        StatementHolder statmentHolder = new StatementHolder(sql, (PreparedStatementSetter)new InsertPreparedStatementSetter((Set)immutableSet), (BatchCollector.ResultCheck)resultCheck);
        resultCheck.setQuery(statmentHolder);
        result.add(statmentHolder);
    }


    private void addLpTableAndLocalizedPropsTableInsertsToResult(Set<StatementHolder> result)
    {
        for(Map.Entry<PK, Map<ColumnPayload.TargetTableType, Set<ColumnPayload>>> entry : this.localizedDataBasePayload
                        .entrySet())
        {
            PK langPk = entry.getKey();
            addLpTableInsertsToResult(result, langPk, (Set<ColumnPayload>)((Map)entry.getValue()).get(ColumnPayload.TargetTableType.LP));
            addPorpsTableInsertsToResult(result, langPk, (Set<ColumnPayload>)((Map)entry.getValue()).get(ColumnPayload.TargetTableType.PROPS));
        }
    }


    private void addLpTableInsertsToResult(Set<StatementHolder> result, PK langPk, Set<ColumnPayload> lpPayload)
    {
        if(lpPayload != null && !lpPayload.isEmpty())
        {
            Set<ColumnPayload> localizedServiceColumnsPayload = buildLocalizedServiceColumnsPayload(langPk);
            ImmutableSet<ColumnPayload> localizedItemTablePayload = Utilities.mergeToImmutableSet(localizedServiceColumnsPayload, lpPayload);
            String statement = addOnConflictDoNothingClause(
                            FluentSqlBuilder.builder(this.databaseName).insert().into(this.infoMap.getTableName(true))
                                            .usingFields(
                                                            Iterables.transform((Iterable)localizedItemTablePayload, (Function)AbstractStoreStatementsBuilder.ColumnPayloadTransformer.COL_NAME))
                                            .values((Set)localizedItemTablePayload).toSql());
            InsertRowResultChecker resultCheck = new InsertRowResultChecker(this.infoMap.getItemTableName());
            StatementHolder statmentHolder = new StatementHolder(statement, (PreparedStatementSetter)new InsertPreparedStatementSetter((Set)localizedItemTablePayload), (BatchCollector.ResultCheck)resultCheck);
            resultCheck.setQuery(statmentHolder);
            result.add(statmentHolder);
        }
    }


    private void addPorpsTableInsertsToResult(Set<StatementHolder> result, PK langPk, Set<ColumnPayload> localizedPropsPayload)
    {
        for(ColumnPayload columnPayload : localizedPropsPayload)
        {
            Set<ColumnPayload> propsServiceColumnsPayload = buildpPropsColumnsPayload(columnPayload, langPk);
            String statement = addOnConflictDoNothingClause(
                            FluentSqlBuilder.builder(this.databaseName).insert().into(this.infoMap.getOldPropTableName())
                                            .usingFields(
                                                            Iterables.transform(propsServiceColumnsPayload, (Function)AbstractStoreStatementsBuilder.ColumnPayloadTransformer.COL_NAME))
                                            .values(propsServiceColumnsPayload).toSql());
            InsertRowResultChecker resultCheck = new InsertRowResultChecker(this.infoMap.getItemTableName());
            StatementHolder statmentHolder = new StatementHolder(statement, (PreparedStatementSetter)new InsertPropsPreparedStatementSetter(propsServiceColumnsPayload), (BatchCollector.ResultCheck)resultCheck);
            resultCheck.setQuery(statmentHolder);
            result.add(statmentHolder);
        }
    }


    private void addUnlocalizedPropsTableInsertsToResult(Set<StatementHolder> result)
    {
        Set<ColumnPayload> propsTablePayload = this.dataBasePayload.get(ColumnPayload.TargetTableType.PROPS);
        if(!propsTablePayload.isEmpty())
        {
            for(ColumnPayload propsPayload : propsTablePayload)
            {
                Set<ColumnPayload> propsRealPayload = buildpPropsColumnsPayload(propsPayload, null);
                String statement = addOnConflictDoNothingClause(
                                FluentSqlBuilder.builder(this.databaseName).insert().into(this.infoMap.getOldPropTableName())
                                                .usingFields(Iterables.transform(propsRealPayload, (Function)AbstractStoreStatementsBuilder.ColumnPayloadTransformer.COL_NAME))
                                                .values(propsRealPayload).toSql());
                InsertRowResultChecker resultCheck = new InsertRowResultChecker(this.infoMap.getItemTableName());
                StatementHolder statmentHolder = new StatementHolder(statement, (PreparedStatementSetter)new InsertPropsPreparedStatementSetter(propsRealPayload), (BatchCollector.ResultCheck)resultCheck);
                resultCheck.setQuery(statmentHolder);
                result.add(statmentHolder);
            }
            this.sateliteTablesChanged = true;
        }
    }


    private String addOnConflictDoNothingClause(String sql)
    {
        if(Config.DatabaseName.POSTGRESQL.equals(this.databaseName))
        {
            return sql + " ON CONFLICT DO NOTHING";
        }
        return sql;
    }


    private Set<ColumnPayload> buildStandardServiceColumns()
    {
        Date now = new Date();
        Set<ColumnPayload> payload = new LinkedHashSet<>();
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.HJMP_TS).value(getCurrentOptimistiLockCounter()).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.TYPE_PK_STR).value(this.infoMap.getTypePK()).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.PK_STRING).value(this.itemPk).build());
        payload.add(getTimeColumn(now, ServiceCol.CREATED_TS));
        payload.add(getTimeColumn(now, ServiceCol.MODIFIED_TS));
        return payload;
    }


    protected ColumnPayload getTimeColumn(Date now, ServiceCol timeCol)
    {
        Optional<ColumnPayload> userProvidedCreationTime = ((Set<ColumnPayload>)this.dataBasePayload.get(ColumnPayload.TargetTableType.ITEM)).stream().filter(p -> p.getColumnName().equals(timeCol.colName())).findFirst();
        return userProvidedCreationTime.isPresent() ? userProvidedCreationTime.get() :
                        ColumnPayload.builder().serviceCol(timeCol).value(now).build();
    }


    protected Set<ColumnPayload> buildpPropsColumnsPayload(ColumnPayload businessCol, PK langPk)
    {
        Set<ColumnPayload> payload = new LinkedHashSet<>();
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.HJMP_TS).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.ITEM_PK).value(this.itemPk).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.ITEM_TYPE_PK).value(this.infoMap.getTypePK()).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.NAME).value(businessCol.getColumnName().toLowerCase(LocaleHelper.getPersistenceLocale())).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.REALNAME).value(businessCol.getColumnName()).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.LANG_PK).declaredTypeClass(Long.class)
                        .value((langPk == null) ? Long.valueOf(0L) : Long.valueOf(langPk.getLongValue())).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.TYPE1).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.VALUESTRING1).value(businessCol.getValue()).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.VALUE1).value(businessCol.getValue()).build());
        return payload;
    }


    protected Set<ColumnPayload> buildLocalizedServiceColumnsPayload(PK langPk)
    {
        Set<ColumnPayload> payload = new LinkedHashSet<>();
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.ITEM_PK).value(this.itemPk).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.ITEM_TYPE_PK).value(this.infoMap.getTypePK()).build());
        payload.add(ColumnPayload.builder().serviceCol(ServiceCol.LANG_PK).value(langPk).build());
        return payload;
    }
}
