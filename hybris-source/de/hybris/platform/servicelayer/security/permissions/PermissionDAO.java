package de.hybris.platform.servicelayer.security.permissions;

import com.google.common.collect.Sets;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.impl.BatchCollectorFactory;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.jalo.security.PermissionContainer;
import de.hybris.platform.persistence.meta.MetaInformationEJB;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class PermissionDAO
{
    private static final Set<String> ACL_COL_NAMES = Sets.newHashSet((Object[])new String[] {"hjmpTS", "PermissionPK", "Negative", "PrincipalPK", "ItemPK"});
    private String aclEnriesTableName;
    private JdbcTemplate jdbcTemplate;
    private AclStatementsExecutor statementsExecutor;
    private BatchCollectorFactory batchCollectorFactory;


    @PostConstruct
    protected void init()
    {
        this.aclEnriesTableName = Config.getString("db.tableprefix", "") + "aclentries";
        this.statementsExecutor = getStatementsExecutor();
    }


    private AclStatementsExecutor getStatementsExecutor()
    {
        MySQLAclStatementsExecutor mySQLAclStatementsExecutor;
        HsqlAclStatementsExecutor hsqlAclStatementsExecutor;
        OracleAclStatementsExecutor oracleAclStatementsExecutor;
        MssqlStatementsExecutor mssqlStatementsExecutor;
        HanaStatementsExecutor hanaStatementsExecutor;
        Config.DatabaseName dataBaseName = getDataBaseName();
        switch(null.$SwitchMap$de$hybris$platform$util$Config$DatabaseName[dataBaseName.ordinal()])
        {
            case 1:
                return (AclStatementsExecutor)new MySQLAclStatementsExecutor(this);
            case 2:
                return (AclStatementsExecutor)new HsqlAclStatementsExecutor(this);
            case 3:
                return (AclStatementsExecutor)new OracleAclStatementsExecutor(this);
            case 4:
                return (AclStatementsExecutor)new MssqlStatementsExecutor(this);
            case 5:
                return (AclStatementsExecutor)new HanaStatementsExecutor(this);
            case 6:
                return (AclStatementsExecutor)new PostgreSQLStatementsExecutor(this);
        }
        throw new IllegalStateException("unsupported database '" + dataBaseName + "'");
    }


    public AclContainer findAclsForItem(PK itemPk)
    {
        String statement = sqlBuilder().selectAll().from(this.aclEnriesTableName).where().field("ItemPK").isEqual().toSql();
        List<AclEntry> entries = this.jdbcTemplate.query(statement, (RowMapper)new AclEntryRowMapper(this), new Object[] {itemPk.getLong()});
        return new AclContainer(itemPk, entries);
    }


    public AclContainer findGlobalAclsForPrincipalPk(PK principalPk)
    {
        String statement = sqlBuilder().selectAll().from(this.aclEnriesTableName).where().field("ItemPK").isEqual().and().field("PrincipalPK").isEqual().toSql();
        List<AclEntry> result = this.jdbcTemplate.query(statement, (RowMapper)new AclEntryRowMapper(this), new Object[] {getGlobalAclItemPk().getLong(), principalPk
                        .getLong()});
        return new AclContainer(getGlobalAclItemPk(), result);
    }


    public List<PK> findGlobalRestrictedPrincipalsForPermissions(List<PK> permissionPks)
    {
        String statement = sqlBuilder().select(new String[] {"PrincipalPK"}).from(this.aclEnriesTableName).where().field("ItemPK").isEqual().and().field("PermissionPK").in(permissionPks).toSql();
        List<Long> params = new ArrayList<>(permissionPks.size() + 1);
        params.add(getGlobalAclItemPk().getLong());
        params.addAll((Collection<? extends Long>)permissionPks.stream().map(PK::getLong).collect(Collectors.toList()));
        return this.jdbcTemplate.query(statement, (rs, rowNum) -> PK.fromLong(rs.getLong(1)), params.stream().toArray());
    }


    PK getGlobalAclItemPk()
    {
        return MetaInformationEJB.DEFAULT_PRIMARY_KEY;
    }


    public void upsertAclsForItem(PK itemPk, Collection<PermissionContainer> permissions)
    {
        this.statementsExecutor.executeUpsert(itemPk, permissions);
    }


    public void deleteAclsForItem(PK itemPk, Collection<PermissionContainer> permissions)
    {
        String statement = sqlBuilder().delete().from(this.aclEnriesTableName).where().field("ItemPK").isEqual().and().field("PrincipalPK").isEqual().and().field("PermissionPK").isEqual().toSql();
        BatchCollector batchCollector = batchCollector();
        permissions.stream().forEach(p -> batchCollector.collectQuery(statement, new Object[] {itemPk.getLong(), p.getPrincipalPK().getLong(), p.getRightPK().getLong()}));
        batchCollector.batchUpdate(this.jdbcTemplate);
    }


    public void deleteAclsForItemAndPrincipals(PK itemPk, Collection<PK> principalPks)
    {
        String statement = sqlBuilder().delete().from(this.aclEnriesTableName).where().field("ItemPK").isEqual().and().field("PrincipalPK").in(principalPks).toSql();
        List<Long> params = new ArrayList<>(principalPks.size() + 1);
        params.add(itemPk.getLong());
        params.addAll((Collection<? extends Long>)principalPks.stream().map(PK::getLong).collect(Collectors.toList()));
        this.jdbcTemplate.update(statement, params.stream().toArray());
    }


    public void deleteAclsForItemAndPermissions(PK itemPk, Collection<PK> permissionPks)
    {
        String statement = sqlBuilder().delete().from(this.aclEnriesTableName).where().field("ItemPK").isEqual().and().field("PermissionPK").in(permissionPks).toSql();
        List<Long> params = new ArrayList<>();
        params.add(itemPk.getLong());
        params.addAll((Collection<? extends Long>)permissionPks.stream().map(PK::getLong).collect(Collectors.toList()));
        this.jdbcTemplate.update(statement, params.stream().toArray());
    }


    @Required
    public void setBatchCollectorFactory(BatchCollectorFactory batchCollectorFactory)
    {
        this.batchCollectorFactory = batchCollectorFactory;
    }


    public void deleteAclsForPermissions(PK itemPk, Collection<PK> permissionsPKs)
    {
        String statement = sqlBuilder().delete().from(this.aclEnriesTableName).where().field("ItemPK").isEqual().and().field("PermissionPK").in(permissionsPKs).toSql();
        List<Long> params = new ArrayList<>();
        params.add(itemPk.getLong());
        params.addAll((Collection<? extends Long>)permissionsPKs.stream().map(PK::getLong).collect(Collectors.toList()));
        this.jdbcTemplate.update(statement, params.stream().toArray());
    }


    private BatchCollector batchCollector()
    {
        return this.batchCollectorFactory.createBatchCollector();
    }


    private String colNames()
    {
        return ACL_COL_NAMES.stream().collect(Collectors.joining(","));
    }


    private FluentSqlBuilder sqlBuilder()
    {
        return FluentSqlBuilder.builder(getDataBaseName());
    }


    Config.DatabaseName getDataBaseName()
    {
        return Config.getDatabaseName();
    }


    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }
}
