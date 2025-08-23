package de.hybris.bootstrap.ddl.tools;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Objects;

abstract class AbstractCopyTableStructureStrategy implements CopyTableStructureStrategy
{
    private final Joiner comaJoiner = Joiner.on(',');


    public Iterable<SqlStatement> getDDLStatementsFor(Iterable<CopyTableOperation> operations)
    {
        Objects.requireNonNull(operations);
        ImmutableList.Builder<SqlStatement> result = ImmutableList.builder();
        for(CopyTableOperation operation : operations)
        {
            if(!operation.isWithData())
            {
                result.addAll(getStatementsForCopyWithoutData(SourceTable.from(operation), operation.getToTable()));
            }
            else if(operation.isWithSelectiveData())
            {
                result.addAll(getStatementsForCopyWithSelectedData(SourceTable.from(operation), operation.getToTable(), operation
                                .getDataToCopySelector(), operation
                                .getDataToCopySelectorParams()));
            }
            else
            {
                result.addAll(getStatementsForCopyWithAllData(SourceTable.from(operation), operation.getToTable()));
            }
            if(isToAddPrimaryKeys() && operation.hasPrimaryKey())
            {
                result.addAll(getStatementsForCreatingPrimaryKey(operation.getToTable(), operation.getPrimaryKeyColumns()));
            }
        }
        return (Iterable<SqlStatement>)result.build();
    }


    protected Iterable<? extends SqlStatement> getStatementsForCreatingPrimaryKey(String tableName, Collection<String> primaryKeyColumns)
    {
        String stmt = String.format("alter table %s add primary key(%s)", new Object[] {tableName, this.comaJoiner.join(primaryKeyColumns)});
        return (Iterable<? extends SqlStatement>)ImmutableList.of(new DDLStatement(stmt));
    }


    protected boolean isToAddPrimaryKeys()
    {
        return true;
    }


    protected abstract Iterable<SqlStatement> getStatementsForCopyWithoutData(SourceTable paramSourceTable, String paramString);


    protected abstract Iterable<SqlStatement> getStatementsForCopyWithSelectedData(SourceTable paramSourceTable, String paramString1, String paramString2, Object... paramVarArgs);


    protected abstract Iterable<SqlStatement> getStatementsForCopyWithAllData(SourceTable paramSourceTable, String paramString);
}
