package de.hybris.bootstrap.ddl.sql;

interface SqlBuilderCallback<T extends org.apache.ddlutils.platform.SqlBuilder>
{
    void call(T paramT, Object... paramVarArgs);
}
