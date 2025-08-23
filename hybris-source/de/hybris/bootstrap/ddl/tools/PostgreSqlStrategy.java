package de.hybris.bootstrap.ddl.tools;

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;

class PostgreSqlStrategy extends AbstractStrategy
{
    public PostgreSqlStrategy(PersistenceInformation persistenceInfo, String columnName, String defaultValue)
    {
        super(persistenceInfo, columnName, defaultValue);
    }


    protected Iterable<SqlStatement> createColumn()
    {
        DDLStatement dDLStatement = new DDLStatement("alter table " + getDeploymentsTableName() + " add " + getColumnName() + " varchar(255) default null");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement);
    }


    protected Iterable<SqlStatement> makeColumnNotNull()
    {
        DDLStatement dDLStatement = new DDLStatement("alter table " + getDeploymentsTableName() + " alter " + getColumnName() + " set not null");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement);
    }


    protected Iterable<SqlStatement> makeColumnPartOfPrimaryKey()
    {
        String pkName = getPKConstraintName();
        ImmutableList.Builder<SqlStatement> result = ImmutableList.builder();
        if(pkName != null)
        {
            DDLStatement dDLStatement1 = new DDLStatement("alter table " + getDeploymentsTableName() + " drop constraint " + pkName);
            result.add(dDLStatement1);
        }
        String pkNameToCreate = (pkName == null) ? ("PK_" + getDeploymentsTableName()) : pkName;
        DDLStatement dDLStatement = new DDLStatement("alter table " + getDeploymentsTableName() + " add constraint " + pkNameToCreate + " primary key(Name, " + getColumnName() + ");");
        result.add(dDLStatement);
        return (Iterable<SqlStatement>)result.build();
    }


    private String getPKConstraintName()
    {
        String query = "select distinct constraint_name from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where UPPER(table_name) = UPPER('" + getDeploymentsTableName() + "') and constraint_type = 'PRIMARY KEY'";
        return (String)getPersistenceInfo().query(query, String.class);
    }
}
