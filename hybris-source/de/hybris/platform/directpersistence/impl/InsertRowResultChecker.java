package de.hybris.platform.directpersistence.impl;

import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.statement.StatementHolder;
import org.springframework.dao.DuplicateKeyException;

public class InsertRowResultChecker implements BatchCollector.ResultCheck
{
    private final String tableName;
    private StatementHolder query;


    public InsertRowResultChecker(String tableName)
    {
        this.tableName = tableName;
    }


    public void checkResult(int result)
    {
        if(result == 0)
        {
            String duplicateKeyMsg = "ERROR: duplicate key value violates unique constraint for table \"" + this.tableName + "\"";
            if(this.query != null)
            {
                duplicateKeyMsg = duplicateKeyMsg + ". SQL [" + duplicateKeyMsg + "]";
            }
            throw new DuplicateKeyException(duplicateKeyMsg);
        }
    }


    public void setQuery(StatementHolder query)
    {
        this.query = query;
    }
}
