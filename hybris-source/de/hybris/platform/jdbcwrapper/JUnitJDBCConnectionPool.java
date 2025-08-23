package de.hybris.platform.jdbcwrapper;

import java.sql.Connection;

public interface JUnitJDBCConnectionPool
{
    void addFailingConnection(Connection paramConnection);


    void removeFailingConnection(Connection paramConnection);


    void setAllConnectionsFail(boolean paramBoolean);


    void resetTestMode();


    void resumeConnectionBorrowing();


    void suspendConnectionBorrowing();
}
