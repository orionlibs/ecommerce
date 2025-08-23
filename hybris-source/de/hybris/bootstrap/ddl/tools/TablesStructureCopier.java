package de.hybris.bootstrap.ddl.tools;

import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;
import java.util.Objects;

public class TablesStructureCopier
{
    private final PersistenceInformation databaseInformation;


    public TablesStructureCopier(PersistenceInformation databaseInformation)
    {
        this.databaseInformation = Objects.<PersistenceInformation>requireNonNull(databaseInformation);
    }


    public Iterable<SqlStatement> getCopyStatements(Iterable<CopyTableOperation> operations)
    {
        requireValidTablesMapping(operations);
        CopyTableStructureStrategy copyStrategy = createCopyTableStructureStrategy();
        return copyStrategy.getDDLStatementsFor(operations);
    }


    private void requireValidTablesMapping(Iterable<CopyTableOperation> operations)
    {
        Objects.requireNonNull(operations);
        for(CopyTableOperation operation : operations)
        {
            String srcTable = operation.getFromTable();
            String dstTable = operation.getToTable();
            Objects.requireNonNull(srcTable, "Source table name must be given.");
            Objects.requireNonNull(dstTable, "Destination table name must be given.");
            if(!this.databaseInformation.containsTable(srcTable))
            {
                throw new IllegalArgumentException("Can't find source table '" + srcTable + "' in database.");
            }
            if(this.databaseInformation.containsTable(dstTable))
            {
                throw new IllegalArgumentException("Table '" + dstTable + "' already exists in database.");
            }
        }
    }


    private CopyTableStructureStrategy createCopyTableStructureStrategy()
    {
        switch(null.$SwitchMap$de$hybris$bootstrap$ddl$DataBaseProvider[this.databaseInformation.getDbType().ordinal()])
        {
            case 1:
                return (CopyTableStructureStrategy)new MySqlCopyTableStructureStrategy();
            case 2:
                return (CopyTableStructureStrategy)new OracleCopyTableStructureStrategy();
            case 3:
                return (CopyTableStructureStrategy)new SqlServerCopyTableStructureStrategy();
            case 4:
                return (CopyTableStructureStrategy)new HsqlCopyTableStructureStrategy();
            case 5:
                return (CopyTableStructureStrategy)new HanaCopyTableStructureStrategy();
            case 6:
                return (CopyTableStructureStrategy)new PostgreSqlCopyTableStructureStrategy();
        }
        throw new UnsupportedOperationException("Can't find implementation for " + this.databaseInformation.getDbType());
    }
}
