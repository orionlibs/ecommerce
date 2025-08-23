package de.hybris.bootstrap.ddl.tools;

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;

class HanaStrategy extends AbstractStrategy
{
    public HanaStrategy(PersistenceInformation persistenceInfo, String columnName, String defaultValue)
    {
        super(persistenceInfo, columnName, defaultValue);
    }


    protected Iterable<SqlStatement> createColumn()
    {
        DDLStatement dDLStatement = new DDLStatement("alter table " + getDeploymentsTableName() + " add ( " + getColumnName() + " varchar(255) default null)");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement);
    }


    protected Iterable<SqlStatement> makeColumnNotNull()
    {
        DDLStatement dDLStatement = new DDLStatement("alter table " + getDeploymentsTableName() + " alter (" + getColumnName() + " varchar(255) not null)");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement);
    }


    protected Iterable<SqlStatement> makeColumnPartOfPrimaryKey()
    {
        DDLStatement dDLStatement1 = new DDLStatement("alter table " + getDeploymentsTableName() + " drop primary key");
        DDLStatement dDLStatement2 = new DDLStatement("alter table " + getDeploymentsTableName() + " add constraint prim_key primary key(name, " + getColumnName() + ")");
        return (Iterable<SqlStatement>)ImmutableList.of(dDLStatement1, dDLStatement2);
    }
}
