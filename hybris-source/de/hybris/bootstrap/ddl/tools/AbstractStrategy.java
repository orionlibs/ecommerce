package de.hybris.bootstrap.ddl.tools;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

abstract class AbstractStrategy implements CreateTypeSystemNameColumnInYDeploymentsTableStrategy
{
    private final PersistenceInformation persistenceInfo;
    private final String deploymentsTableName;
    private final String columnName;
    private final String defaultValue;


    public AbstractStrategy(PersistenceInformation persistenceInfo, String columnName, String defaultValue)
    {
        this.persistenceInfo = Objects.<PersistenceInformation>requireNonNull(persistenceInfo);
        this.deploymentsTableName = persistenceInfo.toRealTableName("ydeployments");
        this.columnName = Objects.<String>requireNonNull(columnName);
        this.defaultValue = Objects.<String>requireNonNull(defaultValue);
    }


    public Iterable<SqlStatement> getStatementsToExecute()
    {
        if(containsColumn())
        {
            return Collections.EMPTY_LIST;
        }
        return Iterables.concat(createColumn(), setDefaultValue(), makeColumnNotNull(), makeColumnPartOfPrimaryKey());
    }


    protected boolean containsColumn()
    {
        return this.persistenceInfo.getDeploymentColumnNames().contains("typesystemname");
    }


    protected abstract Iterable<SqlStatement> createColumn();


    protected Iterable<SqlStatement> setDefaultValue()
    {
        String stmt = "update " + getDeploymentsTableName() + " set " + getColumnName().toLowerCase(LocaleHelper.getPersistenceLocale()) + "=?";
        ImmutableList immutableList = ImmutableList.of(getDefaultValue());
        DMLStatement dMLStatement = new DMLStatement(stmt, (Collection)immutableList);
        return (Iterable<SqlStatement>)ImmutableList.of(dMLStatement);
    }


    protected abstract Iterable<SqlStatement> makeColumnNotNull();


    protected abstract Iterable<SqlStatement> makeColumnPartOfPrimaryKey();


    protected String getDefaultValue()
    {
        return this.defaultValue;
    }


    protected String getColumnName()
    {
        return this.columnName;
    }


    protected String getDeploymentsTableName()
    {
        return this.deploymentsTableName;
    }


    protected PersistenceInformation getPersistenceInfo()
    {
        return this.persistenceInfo;
    }
}
