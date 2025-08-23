package de.hybris.bootstrap.ddl.tools;

import java.io.Reader;

public abstract class SqlScriptParser
{
    public static SqlScriptParser getOracleSqlScriptParser(String statementDelimiter)
    {
        return (SqlScriptParser)new Object(statementDelimiter);
    }


    public static SqlScriptParser getDefaultSqlScriptParser(String statementDelimiter)
    {
        return (SqlScriptParser)new Object(statementDelimiter);
    }


    public abstract Iterable<String> parse(Reader paramReader);
}
