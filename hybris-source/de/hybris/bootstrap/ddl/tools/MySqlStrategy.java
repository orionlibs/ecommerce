package de.hybris.bootstrap.ddl.tools;

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;

class MySqlStrategy extends AbstractStrategy
{
    public MySqlStrategy(PersistenceInformation persistenceInfo, String columnName, String defaultValue)
    {
        super(persistenceInfo, columnName, defaultValue);
    }


    protected Iterable<SqlStatement> createColumn()
    {
        DDLStatement dDLStatement = new DDLStatement("alter table " + getDeploymentsTableName() + " add column " + getColumnName() + " varchar(255) default null");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement);
    }


    protected Iterable<SqlStatement> makeColumnNotNull()
    {
        DDLStatement dDLStatement = new DDLStatement("alter table " + getDeploymentsTableName() + " modify " + getColumnName() + " varchar(255) not null");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement);
    }


    protected Iterable<SqlStatement> makeColumnPartOfPrimaryKey()
    {
        DDLStatement dDLStatement = new DDLStatement("alter table " + getDeploymentsTableName() + " drop primary key, add primary key(name, " + getColumnName() + ")");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement);
    }
}
