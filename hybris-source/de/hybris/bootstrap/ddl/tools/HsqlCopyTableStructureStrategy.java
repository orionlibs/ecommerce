package de.hybris.bootstrap.ddl.tools;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

class HsqlCopyTableStructureStrategy extends AbstractCopyTableStructureStrategy
{
    private static final String COPY_TABLE_WITHOUT_DATA_TEMPLATE = "create table %s as (select * from %s) with no data;";
    private static final String COPY_TABLE_WITH_ALL_DATA_TEMPLATE = "create table %s as (select * from %s) with data;";
    private static final String COPY_TABLE_WITH_SELECTED_DATA_TEMPLATE = "create table %s as (select * from %s where %s) with data";


    protected Iterable<SqlStatement> getStatementsForCopyWithoutData(AbstractCopyTableStructureStrategy.SourceTable sourceTable, String toTable)
    {
        String stmt = String.format("create table %s as (select * from %s) with no data;", new Object[] {toTable, sourceTable.getName()});
        return (Iterable<SqlStatement>)ImmutableList.of(new DMLStatement(stmt));
    }


    protected Iterable<SqlStatement> getStatementsForCopyWithSelectedData(AbstractCopyTableStructureStrategy.SourceTable sourceTable, String toTable, String dataToCopySelector, Object... dataToCopySelectorParams)
    {
        String stmt = String.format("create table %s as (select * from %s where %s) with data", new Object[] {toTable, sourceTable.getName(), dataToCopySelector});
        return (Iterable<SqlStatement>)ImmutableList.of(new DMLStatement(stmt, Lists.newArrayList(dataToCopySelectorParams)));
    }


    protected Iterable<SqlStatement> getStatementsForCopyWithAllData(AbstractCopyTableStructureStrategy.SourceTable sourceTable, String toTable)
    {
        String stmt = String.format("create table %s as (select * from %s) with data;", new Object[] {toTable, sourceTable.getName()});
        return (Iterable<SqlStatement>)ImmutableList.of(new DMLStatement(stmt));
    }
}
