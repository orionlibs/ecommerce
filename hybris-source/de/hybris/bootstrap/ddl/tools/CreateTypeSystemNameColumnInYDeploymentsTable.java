package de.hybris.bootstrap.ddl.tools;

import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;
import java.util.Objects;

public class CreateTypeSystemNameColumnInYDeploymentsTable
{
    private static final String COLUMN_NAME = "TypeSystemName";
    private static final String DEFAULT_VALUE = "DEFAULT";
    private final PersistenceInformation persistenceInfo;


    public CreateTypeSystemNameColumnInYDeploymentsTable(PersistenceInformation persistenceInfo)
    {
        this.persistenceInfo = Objects.<PersistenceInformation>requireNonNull(persistenceInfo);
    }


    public Iterable<SqlStatement> getStatementsToExecute()
    {
        CreateTypeSystemNameColumnInYDeploymentsTableStrategy strategy = getStrategy();
        return strategy.getStatementsToExecute();
    }


    private CreateTypeSystemNameColumnInYDeploymentsTableStrategy getStrategy()
    {
        switch(null.$SwitchMap$de$hybris$bootstrap$ddl$DataBaseProvider[this.persistenceInfo.getDbType().ordinal()])
        {
            case 1:
                return (CreateTypeSystemNameColumnInYDeploymentsTableStrategy)new MySqlStrategy(this.persistenceInfo, "TypeSystemName", "DEFAULT");
            case 2:
                return (CreateTypeSystemNameColumnInYDeploymentsTableStrategy)new OracleStrategy(this.persistenceInfo, "TypeSystemName", "DEFAULT");
            case 3:
                return (CreateTypeSystemNameColumnInYDeploymentsTableStrategy)new SqlServerStrategy(this.persistenceInfo, "TypeSystemName", "DEFAULT");
            case 4:
                return (CreateTypeSystemNameColumnInYDeploymentsTableStrategy)new HsqlStrategy(this.persistenceInfo, "TypeSystemName", "DEFAULT");
            case 5:
                return (CreateTypeSystemNameColumnInYDeploymentsTableStrategy)new HanaStrategy(this.persistenceInfo, "TypeSystemName", "DEFAULT");
            case 6:
                return (CreateTypeSystemNameColumnInYDeploymentsTableStrategy)new PostgreSqlStrategy(this.persistenceInfo, "TypeSystemName", "DEFAULT");
        }
        throw new UnsupportedOperationException("Can't find implementation for " + this.persistenceInfo.getDbType());
    }
}
