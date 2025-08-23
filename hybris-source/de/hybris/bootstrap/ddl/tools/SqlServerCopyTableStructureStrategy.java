package de.hybris.bootstrap.ddl.tools;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

class SqlServerCopyTableStructureStrategy extends AbstractCopyTableStructureStrategy
{
    private static final String COPY_TABLE_WITHOUT_DATA_TEMPLATE = "select * into %s from %s where 0=1";
    private static final String COPY_TABLE_WITH_ALL_DATA_TEMPLATE = "select * into %s from %s";
    private static final String COPY_TABLE_WITH_SELECTED_DATA_TEMPLATE = "select * into %s from %s where %s";


    protected Iterable<SqlStatement> getStatementsForCopyWithoutData(AbstractCopyTableStructureStrategy.SourceTable sourceTable, String toTable)
    {
        String stmt = String.format("select * into %s from %s where 0=1", new Object[] {toTable, sourceTable.getName()});
        return (Iterable<SqlStatement>)ImmutableList.of(new DMLStatement(stmt));
    }


    protected Iterable<SqlStatement> getStatementsForCopyWithSelectedData(AbstractCopyTableStructureStrategy.SourceTable sourceTable, String toTable, String dataToCopySelector, Object... dataToCopySelectorParams)
    {
        String stmt = String.format("select * into %s from %s where %s", new Object[] {toTable, sourceTable.getName(), dataToCopySelector});
        return (Iterable<SqlStatement>)ImmutableList.of(new DMLStatement(stmt, Lists.newArrayList(dataToCopySelectorParams)));
    }


    protected Iterable<SqlStatement> getStatementsForCopyWithAllData(AbstractCopyTableStructureStrategy.SourceTable sourceTable, String toTable)
    {
        String stmt = String.format("select * into %s from %s", new Object[] {toTable, sourceTable.getName()});
        return (Iterable<SqlStatement>)ImmutableList.of(new DMLStatement(stmt));
    }
}
