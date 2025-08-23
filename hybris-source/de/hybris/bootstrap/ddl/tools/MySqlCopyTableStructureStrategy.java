package de.hybris.bootstrap.ddl.tools;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

class MySqlCopyTableStructureStrategy extends AbstractCopyTableStructureStrategy
{
    private static final String COPY_TABLE_STRUCTURE_TEMPLATE = "create table %s like %s;";
    private static final String COPY_ALL_DATA_TEMPLATE = "insert into %s select * from %s;";
    private static final String COPY_SELECTED_DATA_TEMPLATE = "insert into %s select * from %s where %s;";


    protected Iterable<SqlStatement> getStatementsForCopyWithoutData(AbstractCopyTableStructureStrategy.SourceTable sourceTable, String toTable)
    {
        String stmt = String.format("create table %s like %s;", new Object[] {toTable, sourceTable.getName()});
        return (Iterable<SqlStatement>)ImmutableList.of(new DMLStatement(stmt));
    }


    protected Iterable<SqlStatement> getStatementsForCopyWithSelectedData(AbstractCopyTableStructureStrategy.SourceTable sourceTable, String toTable, String dataToCopySelector, Object... dataToCopySelectorParams)
    {
        String ddlStm = String.format("create table %s like %s;", new Object[] {toTable, sourceTable.getName()});
        String dmlStmt = String.format("insert into %s select * from %s where %s;", new Object[] {toTable, sourceTable.getName(), dataToCopySelector});
        ImmutableList.Builder<SqlStatement> result = ImmutableList.builder();
        result.add(new DMLStatement(ddlStm));
        result.add(new DMLStatement(dmlStmt, Lists.newArrayList(dataToCopySelectorParams)));
        return (Iterable<SqlStatement>)result.build();
    }


    protected Iterable<SqlStatement> getStatementsForCopyWithAllData(AbstractCopyTableStructureStrategy.SourceTable sourceTable, String toTable)
    {
        String ddlStm = String.format("create table %s like %s;", new Object[] {toTable, sourceTable.getName()});
        String dmlStmt = String.format("insert into %s select * from %s;", new Object[] {toTable, sourceTable.getName()});
        ImmutableList.Builder<SqlStatement> result = ImmutableList.builder();
        result.add(new DDLStatement(ddlStm));
        result.add(new DMLStatement(dmlStmt));
        return (Iterable<SqlStatement>)result.build();
    }


    protected boolean isToAddPrimaryKeys()
    {
        return false;
    }
}
