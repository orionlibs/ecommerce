package de.hybris.platform.directpersistence.statement;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.DirectPersistenceUtils;
import de.hybris.platform.directpersistence.impl.DefaultPersistResult;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;

public class RemoveManyToManyRelationStatementsBuilder extends AbstractManyToManyRelationStatementsBuilder implements StatementsBuilder
{
    private final PK pkToRemove;
    private final String whereColumnName;
    private final Config.DatabaseName databaseName;


    public RemoveManyToManyRelationStatementsBuilder(Builder builder)
    {
        super(builder.jdbcTemplate);
        Preconditions.checkNotNull(builder.pkToRemove, "PK to remove is required");
        Preconditions.checkNotNull(builder.whereColumnName, "WHERE column name is required");
        Preconditions.checkNotNull(builder.databaseName, "Current databaseName is required");
        this.infoMap = DirectPersistenceUtils.getInfoMapForType(builder.relationName);
        this.pkToRemove = builder.pkToRemove;
        this.whereColumnName = builder.whereColumnName;
        this.databaseName = builder.databaseName;
    }


    public Set<StatementHolder> createStatements()
    {
        Set<StatementHolder> result = new HashSet<>();
        String deleteStatement = buildDelete();
        List<AbstractManyToManyRelationStatementsBuilder.LinkRow> toRemove = findCandidatesToRemove();
        if(toRemove.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        for(AbstractManyToManyRelationStatementsBuilder.LinkRow row : toRemove)
        {
            this.persistResults.add(new DefaultPersistResult(CrudEnum.DELETE, row.getPk(), null, this.infoMap.getCode()));
        }
        result.add(new StatementHolder(deleteStatement, new Object[] {Long.valueOf(this.pkToRemove.getLongValue())}));
        return result;
    }


    private String buildDelete()
    {
        return FluentSqlBuilder.builder(this.databaseName).delete().from(this.infoMap.getItemTableName()).where().field(this.whereColumnName)
                        .isEqual().toSql();
    }


    private List<AbstractManyToManyRelationStatementsBuilder.LinkRow> findCandidatesToRemove()
    {
        String query = FluentSqlBuilder.builder(this.databaseName).select(new String[] {ServiceCol.PK_STRING.colName(), ServiceCol.TARGET_PK.colName(), ServiceCol.SOURCE_PK.colName(), ServiceCol.SEQUENCE_NUMBER.colName(), ServiceCol.RSEQUENCE_NUMBER.colName()}).from(this.infoMap.getItemTableName())
                        .where().field(this.whereColumnName).isEqual().toSql();
        return this.jdbcTemplate.query(query, this.linkRowMapper, new Object[] {Long.valueOf(this.pkToRemove.getLongValue())});
    }


    public static Builder builder(JdbcTemplate jdbcTemplate, Config.DatabaseName databaseName)
    {
        return new Builder(jdbcTemplate, databaseName);
    }
}
