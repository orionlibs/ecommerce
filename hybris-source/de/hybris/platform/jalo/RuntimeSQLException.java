package de.hybris.platform.jalo;

import java.sql.SQLException;

public class RuntimeSQLException extends JaloSystemException
{
    public RuntimeSQLException(SQLException exception)
    {
        super(exception);
    }


    public RuntimeSQLException(SQLException exception, String message)
    {
        super(exception, message, 0);
    }
}
