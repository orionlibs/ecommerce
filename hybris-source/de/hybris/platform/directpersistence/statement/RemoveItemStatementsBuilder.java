package de.hybris.platform.directpersistence.statement;

import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.DirectPersistenceUtils;
import de.hybris.platform.directpersistence.impl.DefaultPersistResult;
import de.hybris.platform.directpersistence.impl.OptimisticLockingAndItemLockingResultCheck;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.impl.DeleteRecord;
import de.hybris.platform.directpersistence.setter.RemoveItemsStatementSetter;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.config.PropertyActionReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class RemoveItemStatementsBuilder extends AbstractEntityStatementsBuilder implements StatementsBuilder
{
    private final Set<String> relationNames;
    protected final Long pkAsLong;
    protected final JdbcTemplate jdbcTemplate;
    private final boolean withOptimisticLockingCheck;


    private RemoveItemStatementsBuilder(DeleteRecord record, JdbcTemplate jdbcTemplate, Config.DatabaseName databaseName, boolean optimisticLockingEnabled)
    {
        super((EntityRecord)record, databaseName);
        this.relationNames = record.getRelationNames();
        this.currentOptimisticLockCounter = new AtomicLong(record.getVersion());
        this.pkAsLong = Long.valueOf(this.itemPk.getLongValue());
        this.jdbcTemplate = jdbcTemplate;
        this.withOptimisticLockingCheck = optimisticLockingEnabled;
    }


    public static RemoveItemStatementsBuilder getInstance(DeleteRecord record, JdbcTemplate jdbcTemplate, Config.DatabaseName databaseName, boolean optimisticLockingEnabled)
    {
        if(Config.DatabaseName.SQLSERVER == databaseName)
        {
            return (RemoveItemStatementsBuilder)new MSSQLRemoveItemStatementsBuilder(record, jdbcTemplate, databaseName, optimisticLockingEnabled);
        }
        if(Config.DatabaseName.HANA == databaseName)
        {
            return (RemoveItemStatementsBuilder)new HanaRemoveItemStatementsBuilder(record, jdbcTemplate, databaseName, optimisticLockingEnabled);
        }
        return new RemoveItemStatementsBuilder(record, jdbcTemplate, databaseName, optimisticLockingEnabled);
    }


    public Set<StatementHolder> createStatements()
    {
        Set<StatementHolder> result = new LinkedHashSet<>();
        addDeleteStatementsToResult(result);
        addLocalizedTableDeleteStatementsToResult(result);
        addPropsTableDeleteStatementsToResult(result);
        addCascadingManyToManyOrphanedLinksDeletesToResult(result);
        addAclEntriesDeleteStatementsToResult(result);
        this.persistResults.add(new DefaultPersistResult(CrudEnum.DELETE,
                        getItemPk(), getCurrentOptimistiLockCounter(), getTypeCode()));
        return result;
    }


    private void addDeleteStatementsToResult(Set<StatementHolder> result)
    {
        FluentSqlBuilder builder = sqlBuilder().delete().from(this.infoMap.getItemTableName()).where().field(ServiceCol.PK_STRING.colName()).isEqual().and().fieldIsNullOr(ServiceCol.SEALED.colName());
        if(this.withOptimisticLockingCheck)
        {
            builder.and().field(ServiceCol.HJMP_TS.colName()).isEqual().toSql();
        }
        String statement = builder.toSql();
        result.add(new StatementHolder(statement, (PreparedStatementSetter)new RemoveItemsStatementSetter(
                        Arrays.asList(new Long[] {this.pkAsLong}, ), Long.valueOf(this.currentOptimisticLockCounter.get()), this.withOptimisticLockingCheck), (BatchCollector.ResultCheck)new OptimisticLockingAndItemLockingResultCheck(this.itemPk, this.jdbcTemplate, this.infoMap
                        .getItemTableName(), this.withOptimisticLockingCheck, this.currentOptimisticLockCounter
                        .get())));
    }


    private void addLocalizedTableDeleteStatementsToResult(Set<StatementHolder> result)
    {
        if(this.infoMap.hasLocalizedColumns())
        {
            result.add(new StatementHolder(sqlBuilder().delete().from(this.infoMap.getTableName(true)).where()
                            .field(ServiceCol.ITEM_PK.colName()).isEqual().toSql(), new Object[] {this.pkAsLong}));
        }
    }


    private void addPropsTableDeleteStatementsToResult(Set<StatementHolder> result)
    {
        if(this.infoMap.hasCorePropsColumns())
        {
            result.add(new StatementHolder(sqlBuilder().delete().from(this.infoMap.getOldPropTableName()).where()
                            .field(ServiceCol.ITEM_PK.colName()).isEqual().toSql(), new Object[] {this.pkAsLong}));
        }
    }


    protected void addCascadingManyToManyOrphanedLinksDeletesToResult(Set<StatementHolder> result)
    {
        Set<String> relationNamesAdjusted = new HashSet<>(this.relationNames);
        for(String relName : relationNamesAdjusted)
        {
            if(shouldAddRelation(relName))
            {
                TypeInfoMap relInfoMap = DirectPersistenceUtils.getInfoMapForType(relName);
                List<Long> linkPks = findLinksForRemove(relInfoMap);
                if(!linkPks.isEmpty())
                {
                    addCascadingManyToManyOrphanedLinksDeletesToResult(result, relInfoMap.getItemTableName(), linkPks, relName);
                }
            }
        }
    }


    protected boolean shouldAddRelation(String relName)
    {
        return !PropertyActionReader.getPropertyActionReader().isActionDisabledForType(relName + ".relation.removal", getTypeCode());
    }


    protected void addCascadingManyToManyOrphanedLinksDeletesToResult(Set<StatementHolder> result, String linksTableName, List<Long> linkPks, String relationName)
    {
        String removeRelSql = sqlBuilder().delete().from(linksTableName).where().field(ServiceCol.PK_STRING.colName()).in(linkPks).toSql();
        result.add(new StatementHolder(removeRelSql, (PreparedStatementSetter)new RemoveItemsStatementSetter(linkPks)));
        createPersistResults(linkPks, relationName);
    }


    protected void addCascadingManyToManyOrphanedLinksDeletesToResultWithLimit(Set<StatementHolder> result, String linksTableName, List<Long> linkPks, String relationName, int parametersLimit)
    {
        List<List<Long>> partition = Lists.partition(linkPks, parametersLimit);
        for(List<Long> part : partition)
        {
            String sql = FluentSqlBuilder.builder(this.databaseName).delete().from(linksTableName).where().field(ServiceCol.PK_STRING.colName()).in(part).toSql();
            result.add(new StatementHolder(sql, (PreparedStatementSetter)new RemoveItemsStatementSetter(part)));
            createPersistResults(part, relationName);
        }
    }


    private void addAclEntriesDeleteStatementsToResult(Set<StatementHolder> result)
    {
        if(!PropertyActionReader.getPropertyActionReader().isActionDisabledForType("acl.removal", getTypeCode()))
        {
            String statement = sqlBuilder().delete().from(getAclEntriesTableName()).where().field(ServiceCol.ITEM_PK.colName()).isEqual().toSql();
            result.add(new StatementHolder(statement, new Object[] {getItemPk().getLong()}));
        }
    }


    String getAclEntriesTableName()
    {
        return Config.getString("db.tableprefix", "") + "aclentries";
    }


    private FluentSqlBuilder sqlBuilder()
    {
        return FluentSqlBuilder.builder(this.databaseName);
    }


    protected void createPersistResults(List<Long> linkPks, String relationName)
    {
        for(Long el : linkPks)
        {
            this.persistResults.add(new DefaultPersistResult(CrudEnum.DELETE, PK.fromLong(el.longValue()), null, relationName));
        }
    }


    private List<Long> findLinksForRemove(TypeInfoMap relInfoMap)
    {
        String findLinkPksSql = sqlBuilder().select(new String[] {ServiceCol.PK_STRING.colName()}).from(relInfoMap.getItemTableName()).where().field(ServiceCol.SOURCE_PK.colName()).isEqual().or().field(ServiceCol.TARGET_PK.colName()).isEqual().toSql();
        return this.jdbcTemplate.queryForList(findLinkPksSql, Long.class, new Object[] {this.pkAsLong, this.pkAsLong});
    }
}
