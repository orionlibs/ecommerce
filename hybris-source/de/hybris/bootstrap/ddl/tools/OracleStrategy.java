package de.hybris.bootstrap.ddl.tools;

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;

class OracleStrategy extends AbstractStrategy
{
    public OracleStrategy(PersistenceInformation persistenceInfo, String columnName, String defaultValue)
    {
        super(persistenceInfo, columnName, defaultValue);
    }


    protected Iterable<SqlStatement> createColumn()
    {
        DDLStatement dDLStatement = new DDLStatement("alter table " + getDeploymentsTableName() + " add " + getColumnName() + " varchar2(255) null");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement);
    }


    protected Iterable<SqlStatement> makeColumnNotNull()
    {
        DDLStatement dDLStatement = new DDLStatement("alter table " + getDeploymentsTableName() + " modify " + getColumnName() + " varchar2(255) not null");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement);
    }


    protected Iterable<SqlStatement> makeColumnPartOfPrimaryKey()
    {
        DDLStatement dDLStatement1 = new DDLStatement("alter table " + getDeploymentsTableName() + " drop primary key");
        DDLStatement dDLStatement2 = new DDLStatement("alter table " + getDeploymentsTableName() + " add primary key(name, " + getColumnName() + ")");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement1, dDLStatement2);
    }
}
