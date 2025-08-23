package de.hybris.platform.jdbcwrapper;

public class JDBCConnectionPoolInterruptedException extends RuntimeException
{
    public JDBCConnectionPoolInterruptedException(InterruptedException exc)
    {
        super(exc);
    }
}
