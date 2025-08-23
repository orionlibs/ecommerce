package de.hybris.bootstrap.ddl.tools;

interface CopyTableStructureStrategy
{
    Iterable<SqlStatement> getDDLStatementsFor(Iterable<CopyTableOperation> paramIterable);
}
