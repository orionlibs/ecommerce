package de.hybris.platform.core.system.query;

import java.io.Serializable;

public interface QueryProvider extends Serializable
{
    String getQueryForSelect();


    String getQueryForLock();


    String getQueryForUnlock();


    String getQueryForTableCreate();


    String getQueryForRowInsert();


    String getTableName();


    String getQueryForTransactionsIsolation();
}
